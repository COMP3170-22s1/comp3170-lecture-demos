package comp3170.demos.week9.sceneobjects;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import comp3170.demos.week9.cameras.Camera;
import comp3170.demos.week9.shaders.ShaderLibrary;

public class Axes extends SceneObject {
	static final private String VERTEX_SHADER = "simpleVertex.glsl";
	static final private String FRAGMENT_SHADER = "simpleFragment.glsl";

	private Vector4f[] vertices;
	private int vertexBuffer;
	private int indexBufferX;
	private int indexBufferY;
	private int indexBufferZ;

	public Axes() {
		super(ShaderLibrary.compileShader(VERTEX_SHADER, FRAGMENT_SHADER));
		
		// A set of i,j,k axes		
		
		this.vertices = new Vector4f[] {
			new Vector4f(0,0,0,1),
			new Vector4f(1,0,0,1),
			new Vector4f(0,1,0,1),
			new Vector4f(0,0,1,1),				
		};
		this.vertexBuffer = shader.createBuffer(vertices);

		this.indexBufferX = shader.createIndexBuffer(new int[] {0,1});		
		this.indexBufferY = shader.createIndexBuffer(new int[] {0,2});		
		this.indexBufferZ = shader.createIndexBuffer(new int[] {0,3});		
	}
	
	private static final float[] RED = new float[] {1,0,0};
	private static final float[] GREEN = new float[] {0,1,0};
	private static final float[] BLUE = new float[] {0,0,1};

	
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

		// X axis in red

		shader.setUniform("u_colour", RED);
		gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, indexBufferX);
		gl.glDrawElements(GL.GL_LINES, 2, GL.GL_UNSIGNED_INT, 0);		

		// Y axis in green

		shader.setUniform("u_colour", GREEN);
		gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, indexBufferY);
		gl.glDrawElements(GL.GL_LINES, 2, GL.GL_UNSIGNED_INT, 0);		

		// Z axis in blue

		shader.setUniform("u_colour", BLUE);
		gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, indexBufferZ);
		gl.glDrawElements(GL.GL_LINES, 2, GL.GL_UNSIGNED_INT, 0);		

	}

}
