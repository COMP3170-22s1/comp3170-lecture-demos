package comp3170.demos.week9.cameras;

import java.awt.event.KeyEvent;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import comp3170.InputManager;
import comp3170.demos.SceneObject;

/**
 * An orthographic camera that revolves around the origin
 * @author malcolmryan
 *
 */

public class Camera extends SceneObject {

	public static final float TAU = (float) (2 * Math.PI);		// https://tauday.com/tau-manifesto

	private Vector3f offset = new Vector3f(0,0.5f,2);
	private Vector3f eulerAngles;

	private float width;	
	private float height;
	private float near;
	private float far;
		
	public Camera(float width, float height, float near, float far) {
		this.width = width;
		this.height = height;
		this.near = near;
		this.far = far;

		eulerAngles = new Vector3f(0,0,0);		
		updateMatrix();				
	}

	public Matrix4f getProjectionMatrix(Matrix4f dest) {		
		return dest.setOrtho(-width/2, width/2, -height/2, height/2, near, far);
	}

	private Matrix4f modelMatrix = new Matrix4f();

	public Matrix4f getViewMatrix(Matrix4f dest) {
		getModelToWorldMatrix(modelMatrix);
		modelMatrix.normalize3x3();		// remove any scaling
		return modelMatrix.invert(dest);
	}
	
	public Vector4f getViewDirection(Vector4f dest) {
		getModelToWorldMatrix(modelMatrix);

		// the viewDirection is the Z axis (i.e. the k vector)
		// because the view direction points towards the camera
		// and the view volume is normally in negative view space.

		// Note: this assumes the camera is orthographic
		// the view direction for a perspective camera will
		// depend on the target's position

		modelMatrix.getColumn(2, dest);
		dest.normalize();
		return dest;
	}
	
	final static float ROTATION_SPEED = TAU / 4;
	final static float MOVEMENT_SPEED = 1;

	public void update(InputManager input, float deltaTime) {
		if (input.isKeyDown(KeyEvent.VK_UP)) {
			eulerAngles.x -= ROTATION_SPEED * deltaTime;
		}
		if (input.isKeyDown(KeyEvent.VK_DOWN)) {
			eulerAngles.x += ROTATION_SPEED * deltaTime;
		}
		if (input.isKeyDown(KeyEvent.VK_LEFT)) {
			eulerAngles.y -= ROTATION_SPEED * deltaTime;
		}
		if (input.isKeyDown(KeyEvent.VK_RIGHT)) {
			eulerAngles.y += ROTATION_SPEED * deltaTime;
		}
		if (input.isKeyDown(KeyEvent.VK_PAGE_DOWN)) {
			offset.z += MOVEMENT_SPEED * deltaTime;
		}
		if (input.isKeyDown(KeyEvent.VK_PAGE_UP)) {
			offset.z -= MOVEMENT_SPEED * deltaTime;
		}

		updateMatrix();		
	}
	
	private void updateMatrix() {
		Matrix4f matrix = getMatrix();
		matrix.identity();
		matrix.rotateY(eulerAngles.y);	// heading
		matrix.rotateX(eulerAngles.x);	// pitch
		matrix.translate(offset);
	}
}
