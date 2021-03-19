package comp3170.demos.week5;

import static com.jogamp.opengl.GL.GL_ELEMENT_ARRAY_BUFFER;
import static com.jogamp.opengl.GL.GL_UNSIGNED_INT;

import java.awt.Color;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import comp3170.Shader;

abstract public class Shape {

	protected Shader shader;
	protected Vector3f colour;
	protected Vector3f position;
	protected Vector3f angle;
	protected float scale;
	protected Matrix4f modelMatrix;

	public Shape(Shader shader) {
		this.shader = shader;
		
		this.position = new Vector3f();
		this.angle = new Vector3f();
		this.scale = 1;
		this.modelMatrix = new Matrix4f();
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

	public Vector3f getAngle(Vector3f angle) {
		return angle.set(this.angle);
	}

	public void setAngle(float pitch, float heading, float roll) {
		this.angle.set(pitch, heading, roll);
	}

	public void setAngle(Vector3f angle) {
		this.angle.set(angle);
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public Vector3f getColour(Vector3f dest) {
		return dest.set(colour);
	}

	public void setColour(Color color) {		
		colour.x = color.getRed() / 255f;
		colour.y = color.getBlue() / 255f;
		colour.z = color.getGreen() / 255f;
	}

	abstract public void draw();

	protected void calcModelMatrix() {
		modelMatrix.identity();
		modelMatrix.translate(this.position);
		modelMatrix.rotateY(this.angle.y);	// heading
		modelMatrix.rotateX(this.angle.x); 	// pitch
		modelMatrix.rotateZ(this.angle.z); 	// roll
		modelMatrix.scale(this.scale);
	}

}