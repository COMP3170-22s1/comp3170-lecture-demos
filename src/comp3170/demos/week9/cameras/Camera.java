package comp3170.demos.week9.cameras;

import java.awt.event.KeyEvent;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import comp3170.InputManager;

/**
 * A camera that revolves around the origin
 * @author malcolmryan
 *
 */

public abstract class Camera {

	public static final float TAU = (float) (2 * Math.PI);		// https://tauday.com/tau-manifesto

	private Matrix4f modelMatrix;

	private float distance;
	private Vector3f angle;

	private float height;
	
	public Camera() {
		this.distance = 1;
		this.height = 0;
		this.modelMatrix = new Matrix4f();
		this.angle = new Vector3f(0,0,0);

		modelMatrix.translate(0,height,distance);		
		
	}
	
	public void setDistance(float distance) {
		this.distance = distance;
		updateModelMatrix();		
	}
	
	public void setHeight(float height) {
		this.height = height;
		updateModelMatrix();		
	}
	
	public Matrix4f getModelMatrix(Matrix4f dest) {
		return dest.set(modelMatrix);
	}

	public Matrix4f getViewMatrix(Matrix4f dest) {
		// invert the model matrix (we have never applied any scale)
		return modelMatrix.invert(dest);
	}
	
	abstract public Matrix4f getProjectionMatrix(Matrix4f dest);
	
	final static float ROTATION_SPEED = TAU / 4;
	final static float MOVEMENT_SPEED = 1;

	public void update(InputManager input, float deltaTime) {
		if (input.isKeyDown(KeyEvent.VK_UP)) {
			angle.x -= ROTATION_SPEED * deltaTime;
		}
		if (input.isKeyDown(KeyEvent.VK_DOWN)) {
			angle.x += ROTATION_SPEED * deltaTime;
		}
		if (input.isKeyDown(KeyEvent.VK_LEFT)) {
			angle.y -= ROTATION_SPEED * deltaTime;
		}
		if (input.isKeyDown(KeyEvent.VK_RIGHT)) {
			angle.y += ROTATION_SPEED * deltaTime;
		}
		if (input.isKeyDown(KeyEvent.VK_PAGE_DOWN)) {
			distance += MOVEMENT_SPEED * deltaTime;
		}
		if (input.isKeyDown(KeyEvent.VK_PAGE_UP)) {
			distance -= MOVEMENT_SPEED * deltaTime;
		}

		updateModelMatrix();		
	}
	
	private void updateModelMatrix() {
		modelMatrix.identity();
		modelMatrix.rotateY(angle.y);	// heading
		modelMatrix.rotateX(angle.x);	// pitch
		modelMatrix.translate(0,height,distance);
	}
}
