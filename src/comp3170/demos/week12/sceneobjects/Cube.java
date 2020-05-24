package comp3170.demos.week12.sceneobjects;

import java.awt.Color;

import org.joml.Vector3f;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import comp3170.Shader;

public class Cube extends ShadowObject {

	private Vector3f[] vertices = {
		new Vector3f(-1,-1,-1),
		new Vector3f(-1,-1, 1),
		new Vector3f( 1,-1,-1),
		new Vector3f( 1,-1, 1),
		new Vector3f(-1, 1,-1),
		new Vector3f(-1, 1, 1),
		new Vector3f( 1, 1,-1),
		new Vector3f( 1, 1, 1),
	};
	private int vertexBuffer;

	private int[] indices = {
		// bottom
		0,2,3,
		3,1,0,
		// top
		4,5,7,
		7,6,4,
		// left
		0,1,5,
		5,4,0,
		// right
		2,6,7,
		7,3,2,
		// back
		0,4,6,
		6,2,0,
		// front
		1,3,7,
		7,5,1,
	};
	private int indexBuffer;
	
	private float[] colour = {1.0f, 1.0f, 0.0f, 1.0f};
	public Cube(Shader shader) {
		super(shader);
		
		this.indexBuffer = shader.createIndexBuffer(indices);
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
		
		gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, this.indexBuffer);
		gl.glDrawElements(GL.GL_TRIANGLES, this.indices.length, GL.GL_UNSIGNED_INT, 0);
	}


}
