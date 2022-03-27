package comp3170.demos.week6.backface;

import java.awt.Color;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import comp3170.GLBuffers;
import comp3170.Shader;
import comp3170.demos.SceneObject;

public class Triangle extends SceneObject {

	private Vector4f[] vertices;
	private int vertexBuffer;
	private float[] colour = {1f, 1f, 1f};
	
	public Triangle(Color colour) {

		// convert colour to RGB array of floats
		colour.getRGBColorComponents(this.colour);

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
			
		vertexBuffer = GLBuffers.createBuffer(vertices);
	}

	@Override
	public void drawSelf(Shader shader, Matrix4f modelMatrix) {
		GL4 gl = (GL4) GLContext.getCurrentGL();

		shader.setUniform("u_modelMatrix", modelMatrix);
		shader.setAttribute("a_position", vertexBuffer);
		shader.setUniform("u_colour", colour);

		// Draw a solid triangle
		gl.glDrawArrays(GL.GL_TRIANGLES, 0, vertices.length);           	
	}

	
}
