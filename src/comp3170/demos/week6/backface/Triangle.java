package comp3170.demos.week6.backface;

import org.joml.Vector4f;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import comp3170.GLBuffers;
import comp3170.Shader;
import comp3170.demos.week6.camera3d.sceneobjects.SceneObject;

public class Triangle extends SceneObject {

	private Vector4f[] vertices;
	private int vertexBuffer;

	public Triangle(Shader shader) {
		super(shader);

		//         0
		//       /   \
		//     /       \       y
		//   /           \     |   RH 
		//  1------*------2    +-x
		//                    /
		//                   z
		
		
		vertices = new Vector4f[] {
			new Vector4f(0, 1, 0, 1),
			new Vector4f(-1, 0, 0, 1),
			new Vector4f( 1, 0, 0, 1),
		};
			
		this.vertexBuffer = GLBuffers.createBuffer(vertices);
	}

	public void draw() {
		GL4 gl = (GL4) GLContext.getCurrentGL();

		calcModelMatrix();
		shader.setUniform("u_modelMatrix", modelMatrix);

		// connect the vertex buffer to the a_position attribute
		shader.setAttribute("a_position", vertexBuffer);

		// write the colour value into the u_colour uniform
		shader.setUniform("u_colour", colour);

		// Draw a solid triangle
		gl.glDrawArrays(GL.GL_TRIANGLES, 0, vertices.length);           	
	}

	
}
