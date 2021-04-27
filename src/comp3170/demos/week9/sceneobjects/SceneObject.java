package comp3170.demos.week9.sceneobjects;

import java.awt.Color;
import java.awt.event.KeyEvent;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import comp3170.InputManager;
import comp3170.Shader;
import comp3170.demos.week9.cameras.Camera;

public class SceneObject {

	protected Shader shader;
	protected Vector3f colour = new Vector3f(1,1,1); // white
	protected Vector3f position = new Vector3f();
	protected Vector3f angle = new Vector3f();
	protected Vector3f scale = new Vector3f(1,1,1);
	protected Matrix4f modelMatrix= new Matrix4f();
	protected Matrix4f viewMatrix= new Matrix4f();
	protected Matrix4f projectionMatrix= new Matrix4f();

	public SceneObject() {
		this(null);
	}
	
	public SceneObject(Shader shader) {
		this.shader = shader;		
	}

	public Vector3f getPosition(Vector3f dest) {
		return dest.get(position);
	}

	public void setPosition(float x, float y, float z) {
		this.position.set(x, y, z);
	}

	public void setPosition(Vector3f position) {
		this.position.set(position);
	}

	public Vector3f getAngle(Vector3f dest) {
		return dest.set(this.angle);
	}

	public void setAngle(float pitch, float heading, float roll) {
		this.angle.set(pitch, heading, roll);
	}

	public void setAngle(Vector3f angle) {
		this.angle.set(angle);
	}

	public Vector3f getScale(Vector3f dest) {
		return dest.set(scale);
	}

	public void setScale(float scale) {
		this.scale.set(scale, scale, scale);
	}

	public void setScale(float sx, float sy, float sz) {
		this.scale.set(sx, sy, sz);
	}

	public Vector3f getColour(Vector3f dest) {
		return dest.set(colour);
	}

	public void setColour(Color color) {		
		colour.x = color.getRed() / 255f;
		colour.y = color.getGreen() / 255f;
		colour.z = color.getBlue() / 255f;
	}

	protected void calcModelMatrix() {
		modelMatrix.identity();
		modelMatrix.translate(this.position);
		modelMatrix.rotateY(this.angle.y);	// heading
		modelMatrix.rotateX(this.angle.x); 	// pitch
		modelMatrix.rotateZ(this.angle.z); 	// roll
		modelMatrix.scale(this.scale);
	}

	public void draw(Camera camera) {
		// does nothing by default		
	}

	public void update(InputManager input, float deltaTime) {
		// does nothing by default		
	}

}