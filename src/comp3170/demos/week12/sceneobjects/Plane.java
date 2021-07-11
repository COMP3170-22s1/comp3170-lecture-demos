package comp3170.demos.week12.sceneobjects;

import java.awt.Color;

import org.joml.Vector4f;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import comp3170.GLBuffers;
import comp3170.Shader;
import comp3170.demos.week12.cameras.Camera;

public class Plane extends SceneObject {

	private Vector4f[] vertices;
	private int vertexBuffer;
	private int[] indices;
	private int indexBuffer;
	private float[] colour;

	public Plane(Shader shader, Color colour) {
		super(shader);

		this.colour = colour.getRGBComponents(new float[4]);
		
		this.vertices = new Vector4f[] {
			new Vector4f( 1, 0,  1, 1),
			new Vector4f(-1, 0,  1, 1),
			new Vector4f( 1, 0, -1, 1),
			new Vector4f(-1, 0, -1, 1),
		};
		
		this.vertexBuffer = GLBuffers.createBuffer(vertices);
		
		this.indices = new int[] {
			0, 1, 2,
			3, 2, 1,
		};
		this.indexBuffer = GLBuffers.createIndexBuffer(indices);
	}
	
	public void draw(Camera camera) {
		GL4 gl = (GL4) GLContext.getCurrentGL();
		shader.enable();

		calcModelMatrix();
		shader.setUniform("u_modelMatrix", modelMatrix);
		shader.setUniform("u_viewMatrix", camera.getViewMatrix(viewMatrix));
		shader.setUniform("u_projectionMatrix", camera.getProjectionMatrix(projectionMatrix));		
		shader.setAttribute("a_position", vertexBuffer);

		if (shader.hasUniform("u_colour")) {
			shader.setUniform("u_colour", colour);
		}
				
		gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
		gl.glDrawElements(GL.GL_TRIANGLES, indices.length, GL.GL_UNSIGNED_INT, 0);		
	}

}
