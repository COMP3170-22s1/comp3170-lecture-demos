package comp3170.demos.week6.cameras;

import org.joml.Matrix4f;

import comp3170.InputManager;

public class PerspectiveCamera extends Camera {

	private float near;
	private float far;
	private float fovy;
	private float aspect;

	public PerspectiveCamera(float distance, InputManager input, float fovy, float aspect, float near, float far) {
		super(distance, input);
		
		this.fovy = fovy;
		this.aspect = aspect;
		this.near = near;
		this.far = far;
	}
	
	@Override
	public Matrix4f getProjectionMatrix(Matrix4f dest) {		
		return dest.setPerspective(fovy, aspect, near, far);
	}

}
