package comp3170.demos.week11.sceneobjects;

import java.io.IOException;

import org.joml.Vector2f;
import org.joml.Vector4f;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import comp3170.GLBuffers;
import comp3170.demos.week11.cameras.Camera;
import comp3170.demos.week11.shaders.ShaderLibrary;
import comp3170.demos.week11.textures.TextureLibrary;

public class Quad extends SceneObject {

	static final private String VERTEX_SHADER = "textureVertex.glsl";
	static final private String FRAGMENT_SHADER = "textureFragment.glsl";
	static final private String TEXTURE = "brick-diffuse.png";
	
	private Vector4f[] vertices;
	private int vertexBuffer;
	private Vector2f[] uvs;
	private int uvBuffer;
	private int[] indices;
	private int indexBuffer;
	private int texture;

	public Quad() {
		super(ShaderLibrary.compileShader(VERTEX_SHADER, FRAGMENT_SHADER));

		this.vertices = new Vector4f[] {
			new Vector4f( 1,  1, 0, 1),
			new Vector4f(-1,  1, 0, 1),
			new Vector4f( 1, -1, 0, 1),
			new Vector4f(-1, -1, 0, 1),
		};
		
		this.vertexBuffer = GLBuffers.createBuffer(vertices);

		this.uvs = new Vector2f[] {
			new Vector2f(0, 0),
			new Vector2f(1, 0),
			new Vector2f(0, 1),
			new Vector2f(1, 1),
		};
			
		this.uvBuffer = GLBuffers.createBuffer(uvs);
		
		this.indices = new int[] {
			0, 1, 2,
			3, 2, 1,
		};
		this.indexBuffer = GLBuffers.createIndexBuffer(indices);
		
		try {
			this.texture = TextureLibrary.loadTexture(TEXTURE);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	@Override
	public void draw(Camera camera) {
		GL4 gl = (GL4) GLContext.getCurrentGL();

		shader.enable();

		calcModelMatrix();
		shader.setUniform("u_modelMatrix", modelMatrix);
		shader.setUniform("u_viewMatrix", camera.getViewMatrix(viewMatrix));
		shader.setUniform("u_projectionMatrix", camera.getProjectionMatrix(projectionMatrix));		
		
		gl.glActiveTexture(GL.GL_TEXTURE0);
		gl.glBindTexture(GL.GL_TEXTURE_2D, this.texture);
		shader.setUniform("u_texture", 0);
		
		shader.setAttribute("a_position", this.vertexBuffer);		
		shader.setAttribute("a_texcoord", this.uvBuffer);		

		gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
		gl.glDrawElements(GL.GL_TRIANGLES, indices.length, GL.GL_UNSIGNED_INT, 0);		
	}

}
