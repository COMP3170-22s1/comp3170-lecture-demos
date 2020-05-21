package comp3170.demos.week12.sceneobjects;

import org.joml.Vector3f;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import comp3170.Shader;

public class Ground extends ShadowObject {

	private Vector3f[] vertices = {
		new Vector3f(-1,0,-1),
		new Vector3f(-1,0,1),
		new Vector3f(1,0,-1),
		new Vector3f(1,0,1),
	};
	private int vertexBuffer;

	private int[] indices = {
		0,1,3,
		3,2,0,
	};
	private int indexBuffer;
	
	private float[] colour = {1.0f, 1.0f, 1.0f, 1.0f};

	public Ground(Shader shader) {
		super(shader);
		
		this.indexBuffer = shader.createIndexBuffer(indices);
		this.vertexBuffer = shader.createBuffer(this.vertices);
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
