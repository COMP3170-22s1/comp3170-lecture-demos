package comp3170.demos.week6.cameras;

import org.joml.Matrix4f;

public class PerspectiveCamera extends Camera {

	private float near;
	private float far;
	private float fovy;
	private float aspect;

	public PerspectiveCamera(float fovy, float aspect, float near, float far) {
		super();
		
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
