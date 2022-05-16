package comp3170.demos.week12.sceneobjects;

import org.joml.Vector2f;
import org.joml.Vector4f;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import comp3170.GLBuffers;
import comp3170.Shader;
import comp3170.demos.SceneObject;


public class RenderQuad extends SceneObject {

	private Shader shader;
	private Vector4f[] vertices;
	private int vertexBuffer;
	private Vector2f[] uvs;
	private int uvBuffer;
	private int[] indices;
	private int indexBuffer;
	private int texture;

	public RenderQuad(Shader shader, int renderTexture) {
		this.shader = shader;

		texture = renderTexture;
		
		vertices = new Vector4f[] {
			new Vector4f( 1,  1, 0, 1),
			new Vector4f(-1,  1, 0, 1),
			new Vector4f( 1, -1, 0, 1),
			new Vector4f(-1, -1, 0, 1),
		};
		
		vertexBuffer = GLBuffers.createBuffer(vertices);

		uvs = new Vector2f[] {
			new Vector2f(1, 1),
			new Vector2f(0, 1),
			new Vector2f(1, 0),
			new Vector2f(0, 0),
		};
			
		uvBuffer = GLBuffers.createBuffer(uvs);
		
		indices = new int[] {
			0, 1, 2,
			3, 2, 1,
		};
		indexBuffer = GLBuffers.createIndexBuffer(indices);
	}
	
	public Shader getShader() {
		return shader;
	}
	
	public void draw() {
		GL4 gl = (GL4) GLContext.getCurrentGL();

		shader.enable();

		// no need for matrices as the Quad is rendered in NDC
		
		gl.glActiveTexture(GL.GL_TEXTURE0);
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture);
		shader.setUniform("u_texture", 0);
		
		shader.setAttribute("a_position", vertexBuffer);		
		shader.setAttribute("a_texcoord", uvBuffer);		

		gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
		gl.glDrawElements(GL.GL_TRIANGLES, indices.length, GL.GL_UNSIGNED_INT, 0);		
	}

}
