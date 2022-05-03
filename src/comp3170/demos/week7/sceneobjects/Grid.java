package comp3170.demos.week7.sceneobjects;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import comp3170.GLBuffers;
import comp3170.Shader;
import comp3170.demos.SceneObject;
import comp3170.demos.week7.shaders.ShaderLibrary;

public class Grid extends SceneObject {

	static final private String VERTEX_SHADER = "simpleVertex.glsl";
	static final private String FRAGMENT_SHADER = "simpleFragment.glsl";

	private Shader shader;
	private Vector4f[] vertices;
	private int vertexBuffer;
	private int[] indices;
	private int indexBuffer;
	private Vector3f colour = new Vector3f(1,1,1);
	
	public Grid(int nLines) {
		shader = ShaderLibrary.compileShader(VERTEX_SHADER, FRAGMENT_SHADER);
		
		// Create a 2x2 square grid with the origin in the centre		
		
		vertices = new Vector4f[4 * nLines];
		indices = new int[4 * nLines];
			
		for (int i = 0; i < nLines; i++) {
			float x = 2.0f * i / (nLines-1) - 1; // -1 to 1 inclusive
			
			vertices[4*i]   = new Vector4f(-1, 0, x, 1);
			vertices[4*i+1] = new Vector4f( 1, 0, x, 1);
			indices[4*i] = 4*i;
			indices[4*i+1] = 4*i+1;
			
			vertices[4*i+2] = new Vector4f(x, 0, -1, 1);
			vertices[4*i+3] = new Vector4f(x, 0,  1, 1);
			indices[4*i+2] = 4*i+2;
			indices[4*i+3] = 4*i+3;
		}
		
		vertexBuffer = GLBuffers.createBuffer(vertices);
		indexBuffer = GLBuffers.createIndexBuffer(indices);		
	}
	

	@Override
	public void drawSelf(Matrix4f mvpMatrix) {
		GL4 gl = (GL4) GLContext.getCurrentGL();

		// activate the shader
		shader.enable();		
		shader.setUniform("u_mvpMatrix", mvpMatrix);
		shader.setAttribute("a_position", vertexBuffer);
		shader.setUniform("u_colour", colour);

		gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
		gl.glDrawElements(GL.GL_LINES, indices.length, GL.GL_UNSIGNED_INT, 0);		
	}

}
