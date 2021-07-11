package comp3170.demos.week10.sceneobjects;

import org.joml.Vector4f;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import comp3170.GLBuffers;
import comp3170.demos.week10.cameras.Camera;
import comp3170.demos.week10.shaders.ShaderLibrary;

public class Grid extends SceneObject {

	static final private String VERTEX_SHADER = "simpleVertex.glsl";
	static final private String FRAGMENT_SHADER = "simpleFragment.glsl";

	private Vector4f[] vertices;
	private int vertexBuffer;
	private int[] indices;
	private int indexBuffer;

	public Grid(int nLines) {
		super(ShaderLibrary.compileShader(VERTEX_SHADER, FRAGMENT_SHADER));
		
		// Create a 2x2 square grid with the origin in the centre		
		
		this.vertices = new Vector4f[4 * nLines];
		this.indices = new int[4 * nLines];
			
		for (int i = 0; i < nLines; i++) {
			float x = 2.0f * i / (nLines-1) - 1; // -1 to 1 inclusive
			
			this.vertices[4*i]   = new Vector4f(-1, 0, x, 1);
			this.vertices[4*i+1] = new Vector4f( 1, 0, x, 1);
			this.indices[4*i] = 4*i;
			this.indices[4*i+1] = 4*i+1;
			
			this.vertices[4*i+2] = new Vector4f(x, 0, -1, 1);
			this.vertices[4*i+3] = new Vector4f(x, 0,  1, 1);
			this.indices[4*i+2] = 4*i+2;
			this.indices[4*i+3] = 4*i+3;
		}
		
		this.vertexBuffer = GLBuffers.createBuffer(vertices);
		this.indexBuffer = GLBuffers.createIndexBuffer(indices);		
	}
	

	@Override
	public void draw(Camera camera) {
		GL4 gl = (GL4) GLContext.getCurrentGL();

		// activate the shader
		shader.enable();		

		calcModelMatrix();
		shader.setUniform("u_modelMatrix", modelMatrix);
		shader.setUniform("u_viewMatrix", camera.getViewMatrix(viewMatrix));
		shader.setUniform("u_projectionMatrix", camera.getProjectionMatrix(projectionMatrix));		

		// connect the vertex buffer to the a_position attribute
		shader.setAttribute("a_position", vertexBuffer);

		// write the colour value into the u_colour uniform
		shader.setUniform("u_colour", colour);

		gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
		gl.glDrawElements(GL.GL_LINES, indices.length, GL.GL_UNSIGNED_INT, 0);		
	}

}
