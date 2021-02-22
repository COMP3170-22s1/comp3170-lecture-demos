package comp3170.demos.week12.sceneobjects;

import java.awt.Color;

import org.joml.Vector3f;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import comp3170.Shader;

public class Triangle extends ShadowObject {

	private Vector3f[] vertices = {
		new Vector3f(-1, 0, 0),
		new Vector3f( 1, 0, 0),
		new Vector3f(0.5f, 1, 0),
	};
	private int vertexBuffer;


	
	private float[] colour = {1.0f, 1.0f, 0.0f, 1.0f};
	public Triangle(Shader shader) {
		super(shader);		
		this.vertexBuffer = shader.createBuffer(this.vertices);
	}

	public void setColour(Color colour) {
		colour.getComponents(this.colour);
	}
	
	@Override
	protected void drawSelf(Shader shader) {
		GL4 gl = (GL4) GLContext.getCurrentGL();

		shader.setUniform("u_mvpMatrix", this.mvpMatrix);
		shader.setAttribute("a_position", this.vertexBuffer);
		if (shader.hasUniform("u_colour")) {
			shader.setUniform("u_colour", this.colour);
		}
			
		setLightUniforms(shader);	
		
		gl.glDrawArrays(GL.GL_TRIANGLES, 0, this.vertices.length);
	}


}
