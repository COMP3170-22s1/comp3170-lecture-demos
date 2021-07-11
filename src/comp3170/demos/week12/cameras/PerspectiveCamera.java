package comp3170.demos.week12.cameras;

/**
 * A perspective camera that revolves around the origin
 * @author malcolmryan
 */

public class PerspectiveCamera extends Camera {

	private float fovy;
	
	public PerspectiveCamera(float fovy, float aspect, float near, float far) {
		super(aspect, near, far);
		this.fovy = fovy;
		projectionMatrix.setPerspective(fovy, aspect, near, far);
	}

}
