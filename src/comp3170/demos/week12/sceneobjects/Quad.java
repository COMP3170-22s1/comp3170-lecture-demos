package comp3170.demos.week12.sceneobjects;

import org.joml.Vector3f;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import comp3170.SceneObject;
import comp3170.Shader;

public class Quad extends SceneObject {

	private Vector3f[] vertices = {
		new Vector3f(-1,-1,0),
		new Vector3f(-1,1,0),
		new Vector3f(1,-1,0),
		new Vector3f(1,1,0),
	};
	private int vertexBuffer;

	private int[] indices = {
		0,2,3,
		3,1,0,
	};
	private int indexBuffer;
	private int texture;	
	

	public Quad(Shader shader, int texture) {
		super(shader);
		
		this.texture = texture;
		this.indexBuffer = shader.createIndexBuffer(indices);
		this.vertexBuffer = shader.createBuffer(this.vertices);
	}

	@Override
	protected void drawSelf(Shader shader) {
		GL4 gl = (GL4) GLContext.getCurrentGL();

		shader.setUniform("u_mvpMatrix", this.mvpMatrix);
		shader.setAttribute("a_position", this.vertexBuffer);
		gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, this.indexBuffer);
		
		gl.glActiveTexture(GL.GL_TEXTURE0);
		gl.glBindTexture(GL.GL_TEXTURE_2D, this.texture);
		shader.setUniform("u_texture", 0);

		gl.glDrawElements(GL.GL_TRIANGLES, this.indices.length, GL.GL_UNSIGNED_INT, 0);
	}

}
