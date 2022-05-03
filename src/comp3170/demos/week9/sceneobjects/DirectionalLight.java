package comp3170.demos.week9.sceneobjects;

import java.awt.Color;
import java.awt.event.KeyEvent;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import comp3170.GLBuffers;
import comp3170.InputManager;
import comp3170.Shader;
import comp3170.demos.SceneObject;
import comp3170.demos.week9.shaders.ShaderLibrary;

public class DirectionalLight extends SceneObject {

	final private static float TAU = (float) (Math.PI * 2);
	static final private String VERTEX_SHADER = "simpleVertex.glsl";
	static final private String FRAGMENT_SHADER = "simpleFragment.glsl";

	private Shader shader;
	private Vector3f intensity = new Vector3f();
	private Vector3f ambientIntensity = new Vector3f();
	private Vector4f[] vertices;
	private int vertexBuffer;
	private int indexBuffer;
	
	public DirectionalLight(Color intensity, Color ambientIntensity) {
		shader = ShaderLibrary.compileShader(VERTEX_SHADER, FRAGMENT_SHADER);
		
		// A set of i,j,k axes		
		
		vertices = new Vector4f[] {
			new Vector4f(0,0,0,1),
			new Vector4f(0,0,-10,1),
		};
		vertexBuffer = GLBuffers.createBuffer(vertices);
		indexBuffer = GLBuffers.createIndexBuffer(new int[] {0,1});		
		
		setIntensity(intensity);		
		setAmbientIntensity(ambientIntensity);	
			
	}
	
	public Vector3f getIntensity(Vector3f dest) {
		return intensity.get(dest);
	}
	
	public void setIntensity(Color colour) {
		float[] rgb = colour.getRGBColorComponents(new float[3]);
		intensity.x = rgb[0];
		intensity.y = rgb[1];
		intensity.z = rgb[2];
	}

	public Vector3f getAmbientIntensity(Vector3f dest) {
		return ambientIntensity.get(dest);
	}
	
	public void setAmbientIntensity(Color colour) {
		float[] rgb = colour.getRGBColorComponents(new float[3]);
		ambientIntensity.x = rgb[0];
		ambientIntensity.y = rgb[1];
		ambientIntensity.z = rgb[2];
	}

	private Matrix4f modelMatrix = new Matrix4f(); 
	
	public Vector4f getDirection(Vector4f dest) {
		getModelToWorldMatrix(modelMatrix);
		dest.set(0,0,-1,0);		// source direction points backwards towards light
		dest.mul(modelMatrix);	// rotate it by the model matrix
		dest.normalize(); 		// normalise to remove scaling
		
		return dest;
	}
	
	private final static float ROTATION_SPEED = TAU / 6;
	private Vector3f eulerAngles = new Vector3f();

	public void update(InputManager input, float dt) {
		
		if (input.isKeyDown(KeyEvent.VK_W)) {
			eulerAngles.x = (eulerAngles.x + ROTATION_SPEED * dt) % TAU; 
		}
		if (input.isKeyDown(KeyEvent.VK_S)) {
			eulerAngles.x = (eulerAngles.x - ROTATION_SPEED * dt) % TAU; 
		}
		if (input.isKeyDown(KeyEvent.VK_A)) {
			eulerAngles.y = (eulerAngles.y - ROTATION_SPEED * dt) % TAU; 
		}
		if (input.isKeyDown(KeyEvent.VK_D)) {
			eulerAngles.y = (eulerAngles.y + ROTATION_SPEED * dt) % TAU; 
		}
		
		getMatrix().identity().rotateY(eulerAngles.y).rotateX(eulerAngles.x);		
	}
	
	private static final float[] YELLOW = new float[] {1,1,0};

	@Override
	public void drawSelf(Matrix4f mvpMatrix) {
		GL4 gl = (GL4) GLContext.getCurrentGL();

		shader.enable();		
		shader.setUniform("u_mvpMatrix", mvpMatrix);
		shader.setAttribute("a_position", vertexBuffer);

		shader.setUniform("u_colour", YELLOW);
		gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
		gl.glDrawElements(GL.GL_LINES, 2, GL.GL_UNSIGNED_INT, 0);		
	}
}