package comp3170.demos.week12.sceneobjects;

import org.joml.Vector2f;
import org.joml.Vector4f;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import comp3170.Shader;
import comp3170.demos.week12.shaders.ShaderLibrary;


public class RenderQuad extends SceneObject {

	private Vector4f[] vertices;
	private int vertexBuffer;
	private Vector2f[] uvs;
	private int uvBuffer;
	private int[] indices;
	private int indexBuffer;
	private int texture;

	public RenderQuad(Shader shader, int renderTexture) {
		super(shader);

		this.texture = renderTexture;
		
		this.vertices = new Vector4f[] {
			new Vector4f( 1,  1, 0, 1),
			new Vector4f(-1,  1, 0, 1),
			new Vector4f( 1, -1, 0, 1),
			new Vector4f(-1, -1, 0, 1),
		};
		
		this.vertexBuffer = shader.createBuffer(vertices);

		this.uvs = new Vector2f[] {
			new Vector2f(1, 1),
			new Vector2f(0, 1),
			new Vector2f(1, 0),
			new Vector2f(0, 0),
		};
			
		this.uvBuffer = shader.createBuffer(uvs);
		
		this.indices = new int[] {
			0, 1, 2,
			3, 2, 1,
		};
		this.indexBuffer = shader.createIndexBuffer(indices);
	}
	
	public void draw() {
		GL4 gl = (GL4) GLContext.getCurrentGL();

		shader.enable();

		// no need for matrices as the Quad is rendered in NDC
		
		gl.glActiveTexture(GL.GL_TEXTURE0);
		gl.glBindTexture(GL.GL_TEXTURE_2D, this.texture);
		shader.setUniform("u_texture", 0);
		
		shader.setAttribute("a_position", this.vertexBuffer);		
		shader.setAttribute("a_texcoord", this.uvBuffer);		

		gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
		gl.glDrawElements(GL.GL_TRIANGLES, indices.length, GL.GL_UNSIGNED_INT, 0);		
	}

}
