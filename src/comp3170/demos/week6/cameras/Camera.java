package comp3170.demos.week6.cameras;

import org.joml.Matrix4f;

import comp3170.demos.week6.sceneobjects.SceneObject;

public abstract class Camera extends SceneObject {

	public Camera() {
		super();
	}
	
	public Matrix4f getViewMatrix(Matrix4f dest) {
		// get the translation and rotation and scale without the scale 
		
		dest.translation(this.position);
		dest.rotateY(this.angle.y);	// heading
		dest.rotateX(this.angle.x); 	// pitch
		dest.rotateZ(this.angle.z); 	// roll
		
		// invert to get the view matrix
		
		return dest.invert();
	}
	
	abstract public Matrix4f getProjectionMatrix(Matrix4f dest);
}
