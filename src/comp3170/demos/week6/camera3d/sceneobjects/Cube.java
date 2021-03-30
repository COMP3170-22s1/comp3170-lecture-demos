package comp3170.demos.week6.camera3d.sceneobjects;

import org.joml.Vector3f;
import org.joml.Vector4f;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import comp3170.Shader;

public class Cube extends SceneObject {
	private Vector4f[] vertices;
	int vertexBuffer;
	int[] indices;
	int indexBuffer;

	public Cube(Shader shader) {
		super(shader);

		//          6-----7
		//         /|    /|
		//        / |   / |
		//       1-----0  |     y    RHS coords
		//       |  |  |  |     | 
		//       |  5--|--4     +--x
		//       | /   | /     /
		//       |/    |/     z
		//       2-----3
		
		vertices = new Vector4f[] {
			new Vector4f( 1, 1, 1, 1),
			new Vector4f(-1, 1, 1, 1),
			new Vector4f(-1,-1, 1, 1),
			new Vector4f( 1,-1, 1, 1),
			new Vector4f( 1,-1,-1, 1),
			new Vector4f(-1,-1,-1, 1),
			new Vector4f(-1, 1,-1, 1),
			new Vector4f( 1, 1,-1, 1),
		};
		
		this.vertexBuffer = shader.createBuffer(vertices);

		// indices for the lines forming each face

		this.indices = new int[] {
			// front
			0, 1,
			1, 2,
			2, 3,
			3, 0,
			0, 2,
			
			// back
			4, 5,
			5, 6,
			6, 7,
			7, 4,
			4, 6,
			
			// top
			0, 7,
			6, 1,
			6, 0,
			
			// bottom 
			2, 5,
			4, 3,
			4, 2,
			
			// left
			0, 4,
			6, 2,
			
		};

		this.indexBuffer = shader.createIndexBuffer(indices);
		
		// scale down to fit in window
		this.setScale((float) (1.0f / Math.sqrt(3)));

		this.colour = new Vector3f(1f, 1f, 1f); // default is white
	}
	
	public void draw() {
		GL4 gl = (GL4) GLContext.getCurrentGL();

		calcModelMatrix();
		shader.setUniform("u_modelMatrix", modelMatrix);

		// connect the vertex buffer to the a_position attribute
		shader.setAttribute("a_position", vertexBuffer);

		// write the colour value into the u_colour uniform
		shader.setUniform("u_colour", colour);

		gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, indexBuffer);

		// DEBUG: Just draw the vertices
//		gl.glDrawElements(GL.GL_POINTS, indices.length, GL.GL_UNSIGNED_INT, 0);

		// Draw the wireframe as lines
		gl.glDrawElements(GL.GL_LINES, indices.length, GL.GL_UNSIGNED_INT, 0);
	}

}
