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
import comp3170.demos.week9.cameras.Camera;
import comp3170.demos.week9.shaders.ShaderLibrary;

public class DirectionalLight extends SceneObject {

	final private static float TAU = (float) (Math.PI * 2);
	static final private String VERTEX_SHADER = "simpleVertex.glsl";
	static final private String FRAGMENT_SHADER = "simpleFragment.glsl";

	private Vector3f intensity = new Vector3f();
	private Vector3f ambientIntensity = new Vector3f();
	private Vector4f[] vertices;
	private int vertexBuffer;
	private int indexBuffer;
	
	public DirectionalLight(Color intensity, Color ambientIntensity) {
		super(ShaderLibrary.compileShader(VERTEX_SHADER, FRAGMENT_SHADER));
		
		// A set of i,j,k axes		
		
		this.vertices = new Vector4f[] {
			new Vector4f(0,0,0,1),
			new Vector4f(0,0,-10,1),
		};
		this.vertexBuffer = GLBuffers.createBuffer(vertices);
		this.indexBuffer = GLBuffers.createIndexBuffer(new int[] {0,1});		
		
		setIntensity(intensity);		
		setAmbientIntensity(ambientIntensity);	
			
	}
	
	public Vector3f getIntensity(Vector3f dest) {
		return intensity.get(dest);
	}
	
	public void setIntensity(Color colour) {
		float[] rgb = colour.getRGBColorComponents(new float[3]);
		this.intensity.x = rgb[0];
		this.intensity.y = rgb[1];
		this.intensity.z = rgb[2];
	}

	public Vector3f getAmbientIntensity(Vector3f dest) {
		return ambientIntensity.get(dest);
	}
	
	public void setAmbientIntensity(Color colour) {
		float[] rgb = colour.getRGBColorComponents(new float[3]);
		this.ambientIntensity.x = rgb[0];
		this.ambientIntensity.y = rgb[1];
		this.ambientIntensity.z = rgb[2];
	}

	private Matrix4f rotationMatrix = new Matrix4f(); 

	public Vector4f getDirection(Vector4f dest) {
		// assumes the light is pointing along the Z axis		

		rotationMatrix.identity();
		rotationMatrix.rotateY(this.angle.y);	// heading
		rotationMatrix.rotateX(this.angle.x); 	// pitch
		rotationMatrix.rotateZ(this.angle.z); 	// roll

		// source direction points backwards towards light
		dest.set(0,0,-1,0);
		return dest.mul(rotationMatrix);
	}
	
	private final static float ROTATION_SPEED = TAU / 6;

	public void update(InputManager input, float dt) {
		
		if (input.isKeyDown(KeyEvent.VK_W)) {
			angle.x = (angle.x + ROTATION_SPEED * dt) % TAU; 
		}
		if (input.isKeyDown(KeyEvent.VK_S)) {
			angle.x = (angle.x - ROTATION_SPEED * dt) % TAU; 
		}
		if (input.isKeyDown(KeyEvent.VK_A)) {
			angle.y = (angle.y - ROTATION_SPEED * dt) % TAU; 
		}
		if (input.isKeyDown(KeyEvent.VK_D)) {
			angle.y = (angle.y + ROTATION_SPEED * dt) % TAU; 
		}
	}
	
	private static final float[] YELLOW = new float[] {1,1,0};

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

		shader.setUniform("u_colour", YELLOW);
		gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
		gl.glDrawElements(GL.GL_LINES, 2, GL.GL_UNSIGNED_INT, 0);		
	}
}