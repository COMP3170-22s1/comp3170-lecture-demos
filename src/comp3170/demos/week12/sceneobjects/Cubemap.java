package comp3170.demos.week12.sceneobjects;

import java.io.IOException;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import comp3170.GLBuffers;
import comp3170.Shader;
import comp3170.demos.SceneObject;
import comp3170.demos.week12.shaders.ShaderLibrary;
import comp3170.demos.week12.textures.TextureLibrary;

public class Cubemap extends SceneObject {

	private static final String VERTEX_SHADER = "cubemapVertex.glsl";
	private static final String FRAGMENT_SHADER = "cubemapFragment.glsl";
	
	private static final String[] TEXTURE_FILES = new String[] {
		"X.png", "X.png", 
		"Y.png", "Y.png", 
		"Z.png", "Z.png", 
	};
	
	private Shader shader;
	private int cubemapTexture;
	
	private Vector4f[] vertices = new Vector4f[] {
		new Vector4f( 1, 1, 1, 1),
		new Vector4f(-1, 1, 1, 1),
		new Vector4f( 1,-1, 1, 1),
		new Vector4f(-1,-1, 1, 1),
		new Vector4f( 1, 1,-1, 1),
		new Vector4f(-1, 1,-1, 1),
		new Vector4f( 1,-1,-1, 1),
		new Vector4f(-1,-1,-1, 1),
	};
	private int vertexBuffer;
	
	// NOTE: These triangles are in the opposite to the usual winding order
	// as the cube is designed to be viewed from the inside
	private int[] indices = new int[] {
		// front
		0,2,1,	
		3,1,2,
		// back
		4,5,6,
		7,6,5,
		// left
		5,1,7,
		3,7,1,
		// right
		0,4,2,
		6,2,4,
		// top
		0,1,4,
		5,4,1,
		// bottom
		2,6,3,
		7,3,6,
	};
	private int indexBuffer;
	
	
	public Cubemap() {
		shader = ShaderLibrary.compileShader(VERTEX_SHADER, FRAGMENT_SHADER);

		vertexBuffer = GLBuffers.createBuffer(vertices);
		indexBuffer = GLBuffers.createIndexBuffer(indices);
		
		try {
			cubemapTexture = TextureLibrary.loadCubemap(TEXTURE_FILES);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}		
	}

	@Override
	protected void drawSelf(Matrix4f mvpMatrix) {
		GL4 gl = (GL4) GLContext.getCurrentGL();

		shader.enable();
		shader.setAttribute("a_position", vertexBuffer);
		shader.setUniform("u_mvpMatrix", mvpMatrix);		

		gl.glActiveTexture(GL.GL_TEXTURE0);
		gl.glBindTexture(GL.GL_TEXTURE_CUBE_MAP, cubemapTexture);
		shader.setUniform("u_cubemap", 0);

		gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
		gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL4.GL_FILL);
		gl.glDrawElements(GL.GL_TRIANGLES, indices.length, GL.GL_UNSIGNED_INT, 0);		
	}
	
	
}
