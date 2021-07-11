package comp3170.demos.week8.sceneobjects;

import java.awt.event.KeyEvent;

import org.joml.Vector3f;
import org.joml.Vector4f;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import comp3170.GLBuffers;
import comp3170.InputManager;
import comp3170.demos.week8.cameras.Camera;
import comp3170.demos.week8.shaders.ShaderLibrary;

public class Quad extends SceneObject {

	static final private String VERTEX_SHADER = "distortVertex.glsl";
	static final private String FRAGMENT_SHADER = "distortFragment.glsl";
	private Vector4f[] vertices;
	private int vertexBuffer;
	private Vector3f[] colours;
	private int colourBuffer;
	private int[] indices;
	private int indexBuffer;

	public Quad() {
		super(ShaderLibrary.compileShader(VERTEX_SHADER, FRAGMENT_SHADER));

		this.vertices = new Vector4f[] {
			new Vector4f(-1,  1, 0, 1),
			new Vector4f( 1,  1, 0, 1),
			new Vector4f(-1, -1, 0, 1),
			new Vector4f( 1, -1, 0, 1),
		};
		
		this.vertexBuffer = GLBuffers.createBuffer(vertices);

		this.colours = new Vector3f[] {
			new Vector3f(0, 1, 0),	// GREEN
			new Vector3f(1, 0, 0),	// RED
			new Vector3f(1, 0, 0),	// BLUE
			new Vector3f(0, 1, 0),	// BLUE
		};
			
		this.colourBuffer = GLBuffers.createBuffer(colours);
		
		this.indices = new int[] {
			0, 1, 2,
			3, 2, 1,
		};
		this.indexBuffer = GLBuffers.createIndexBuffer(indices);
	}

	private boolean maximise = false;
	private float distort = 0;
	private static final float DISTORT = 0.25f;

	public void update(InputManager input, float deltaTime) {
		if (input.wasKeyPressed(KeyEvent.VK_SPACE)) {
			maximise = !maximise;
		}
		
		if (input.isKeyDown(KeyEvent.VK_X)) {
			distort = Math.min(1f, distort + DISTORT * deltaTime);			
		}
		if (input.isKeyDown(KeyEvent.VK_Z)) {
			distort = Math.max(0f, distort - DISTORT * deltaTime);			
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
		
		shader.setUniform("u_distort" , distort);
		shader.setUniform("u_maximise" , maximise);
		
		shader.setAttribute("a_position", this.vertexBuffer);		
		shader.setAttribute("a_colour", this.colourBuffer);		

		gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
		gl.glDrawElements(GL.GL_TRIANGLES, indices.length, GL.GL_UNSIGNED_INT, 0);		
	}

}
