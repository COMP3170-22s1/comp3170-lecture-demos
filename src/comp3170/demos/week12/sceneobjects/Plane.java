package comp3170.demos.week12.sceneobjects;

import java.awt.Color;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import comp3170.GLBuffers;
import comp3170.Shader;
import comp3170.demos.SceneObject;

public class Plane extends SceneObject {

	private Vector4f[] vertices;
	private int vertexBuffer;
	private int[] indices;
	private int indexBuffer;
	private float[] colour;
	private Shader shader;

	public Plane(Shader shader, Color colour) {
		this.shader = shader;

		this.colour = colour.getRGBComponents(new float[4]);
		
		vertices = new Vector4f[] {
			new Vector4f( 1, 0,  1, 1),
			new Vector4f(-1, 0,  1, 1),
			new Vector4f( 1, 0, -1, 1),
			new Vector4f(-1, 0, -1, 1),
		};
		
		vertexBuffer = GLBuffers.createBuffer(vertices);
		
		indices = new int[] {
			0, 1, 2,
			3, 2, 1,
		};
		indexBuffer = GLBuffers.createIndexBuffer(indices);
	}
	
	public void drawSelf(Matrix4f mvpMatrix) {
		GL4 gl = (GL4) GLContext.getCurrentGL();
		shader.enable();

		shader.setUniform("u_mvpMatrix", mvpMatrix);
		shader.setAttribute("a_position", vertexBuffer);

		if (shader.hasUniform("u_colour")) {
			shader.setUniform("u_colour", colour);
		}
				
		gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
		gl.glDrawElements(GL.GL_TRIANGLES, indices.length, GL.GL_UNSIGNED_INT, 0);		
	}

}
