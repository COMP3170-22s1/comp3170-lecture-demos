package comp3170.demos.week5.mesh;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import comp3170.GLBuffers;
import comp3170.Shader;
import comp3170.demos.SceneObject;

public class SimpleCube extends SceneObject {
	private Vector4f[] vertices;
	private int vertexBuffer;
	private int[] indices;
	private int indexBuffer;
	private float[] colour = {1.0f, 1.0f, 1.0f};


	public SimpleCube() {

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
		
		vertexBuffer = GLBuffers.createBuffer(vertices);

		// indices for the lines forming each face

		indices = new int[] {
			// front
			0, 1, 2,
			2, 3, 0,
			
			// back
			4, 5, 6,
			6, 7, 4,
			
			// top
			0, 7, 6,
			6, 1, 0,
			
			// bottom 
			2, 5, 4,
			4, 3, 2,
			
			// left
			2, 1, 6,
			6, 5, 2,
			
			// right
			7, 0, 3,
			3, 4, 7,
			
		};

		indexBuffer = GLBuffers.createIndexBuffer(indices);
		
		// scale down to fit in window
		getMatrix().scale((float) (1.0f / Math.sqrt(3)));
	}
	
	@Override
	protected void drawSelf(Shader shader, Matrix4f modelMatrix) {
		GL4 gl = (GL4) GLContext.getCurrentGL();

		shader.setUniform("u_modelMatrix", modelMatrix);

		// connect the vertex buffer to the a_position attribute
		shader.setAttribute("a_position", vertexBuffer);

		// write the colour value into the u_colour uniform
		shader.setUniform("u_colour", colour);

		gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
		gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL4.GL_LINE);
		gl.glDrawElements(GL.GL_TRIANGLES, indices.length, GL.GL_UNSIGNED_INT, 0);
	}

}
