package comp3170.demos.week7.fog.sceneobjects;

import org.joml.Vector4f;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import comp3170.Shader;

public class Grid extends SceneObject {
	
	private Vector4f[] vertices;
	private int vertexBuffer;
	private int[] indices;
	private int indexBuffer;

	public Grid(Shader shader, int nLines) {
		super(shader);
		
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
		
		this.vertexBuffer = shader.createBuffer(vertices);
		this.indexBuffer = shader.createIndexBuffer(indices);		
	}
	

	@Override
	public void draw() {
		GL4 gl = (GL4) GLContext.getCurrentGL();

		// activate the shader
		shader.enable();		

		calcModelMatrix();
		shader.setUniform("u_modelMatrix", modelMatrix);

		// connect the vertex buffer to the a_position attribute
		shader.setAttribute("a_position", vertexBuffer);

		// write the colour value into the u_colour uniform
		shader.setUniform("u_colour", colour);

		gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
		gl.glDrawElements(GL.GL_LINES, indices.length, GL.GL_UNSIGNED_INT, 0);		
	}

}
