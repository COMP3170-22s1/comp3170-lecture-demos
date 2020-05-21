package comp3170.demos.week12.sceneobjects;

import org.joml.Vector3f;
import org.joml.Vector4f;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import comp3170.SceneObject;
import comp3170.Shader;

public class Ground extends SceneObject {

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

	private SceneObject light;
	private Vector4f lightPosition;

	public Ground(Shader shader) {
		super(shader);
		
		this.indexBuffer = shader.createIndexBuffer(indices);
		this.vertexBuffer = shader.createBuffer(this.vertices);
	}

	public void setLight(SceneObject light) {
		this.light = light;		
		this.lightPosition = new Vector4f();
	}

	@Override
	protected void drawSelf(Shader shader) {
		GL4 gl = (GL4) GLContext.getCurrentGL();

		shader.setUniform("u_mvpMatrix", this.mvpMatrix);
		shader.setAttribute("a_position", this.vertexBuffer);
		
		getWorldMatrix(this.worldMatrix);
		shader.setUniform("u_worldMatrix", this.worldMatrix);
		
		this.light.getPosition(this.lightPosition);
		shader.setUniform("u_lightPosition", this.lightPosition);		

		if (shader.hasUniform("u_colour")) {
			shader.setUniform("u_colour", this.colour);			
		}
		
		gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, this.indexBuffer);
		gl.glDrawElements(GL.GL_TRIANGLES, this.indices.length, GL.GL_UNSIGNED_INT, 0);
	}

}
