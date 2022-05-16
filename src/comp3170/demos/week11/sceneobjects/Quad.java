package comp3170.demos.week11.sceneobjects;

import java.io.IOException;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import comp3170.GLBuffers;
import comp3170.Shader;
import comp3170.demos.SceneObject;
import comp3170.demos.week11.shaders.ShaderLibrary;
import comp3170.demos.week11.textures.TextureLibrary;

public class Quad extends SceneObject {

	static final private String VERTEX_SHADER = "textureVertex.glsl";
	static final private String FRAGMENT_SHADER = "textureFragment.glsl";
	static final private String TEXTURE = "colours.png";
	
	private Shader shader;
	private Vector4f[] vertices;
	private int vertexBuffer;
	private Vector2f[] uvs;
	private int uvBuffer;
	private int[] indices;
	private int indexBuffer;
	private int texture;

	public Quad() {
		shader = ShaderLibrary.compileShader(VERTEX_SHADER, FRAGMENT_SHADER);

		//  1---3
		//  |\  |   y
		//  | * |   |
		//  |  \|   +--x
		//  0---2
		
		vertices = new Vector4f[] {
			new Vector4f(-1, -1, 0, 1),
			new Vector4f(-1,  1, 0, 1),
			new Vector4f( 1, -1, 0, 1),
			new Vector4f( 1,  1, 0, 1),
		};
		
		vertexBuffer = GLBuffers.createBuffer(vertices);

		uvs = new Vector2f[] {
			new Vector2f(0, 1),
			new Vector2f(0, 0),
			new Vector2f(1, 1),
			new Vector2f(1, 0),
		};
			
		uvBuffer = GLBuffers.createBuffer(uvs);
		
		indices = new int[] {
//			0, 1, 3,
//			3, 2, 0,
			0, 1, 2,
			3, 2, 1,
		};
		indexBuffer = GLBuffers.createIndexBuffer(indices);
		
		try {
			texture = TextureLibrary.loadTexture(TEXTURE);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	@Override
	public void drawSelf(Matrix4f mvpMatrix) {
		GL4 gl = (GL4) GLContext.getCurrentGL();

		shader.enable();
		shader.setUniform("u_mvpMatrix", mvpMatrix);
		
		gl.glActiveTexture(GL.GL_TEXTURE0);
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture);
		shader.setUniform("u_texture", 0);
		
		shader.setAttribute("a_position", vertexBuffer);		
		shader.setAttribute("a_texcoord", uvBuffer);		

		gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
		gl.glDrawElements(GL.GL_TRIANGLES, indices.length, GL.GL_UNSIGNED_INT, 0);		
	}

}
