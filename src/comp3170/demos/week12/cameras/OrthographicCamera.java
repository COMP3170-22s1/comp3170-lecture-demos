package comp3170.demos.week12.cameras;

/**
 * An orthographic camera that revolves around the origin
 * @author malcolmryan
 */

public class OrthographicCamera extends Camera {

	public OrthographicCamera(float width, float height, float near, float far) {
		super(width / height, near, far);
		projectionMatrix.setOrtho(-width/2, width/2, -height/2, height/2, near, far);
	}

}
