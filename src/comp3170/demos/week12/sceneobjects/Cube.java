package comp3170.demos.week12.sceneobjects;

import org.joml.Vector3f;
import org.joml.Vector4f;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import comp3170.SceneObject;
import comp3170.Shader;

public class Cube extends SceneObject {

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
	private SceneObject light;
	private Vector4f lightPosition;	

	public Cube(Shader shader) {
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
