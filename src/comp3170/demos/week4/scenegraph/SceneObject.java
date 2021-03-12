package comp3170.demos.week4.scenegraph;

import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix3f;
import org.joml.Vector2f;

import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import comp3170.Shader;

public class SceneObject {
	private Shader shader;

	protected List<SceneObject> children;
	private SceneObject parent;
	
	private Vector2f position; 
	private float angle;
	private Vector2f scale;
	
	private Matrix3f modelMatrix;
	private Matrix3f translationMatrix;
	private Matrix3f rotationMatrix;
	private Matrix3f scaleMatrix;
	
	public SceneObject(Shader shader) {
		this.shader = shader;
		this.parent = null;
		this.children = new ArrayList<SceneObject>();
	}
	
	public SceneObject getParent() {
		return parent;
	}
	
	public void setParent(SceneObject parent) {
		if (this.parent != null) { 
			this.parent.children.remove(this);
		}
		this.parent = parent;
		this.parent.children.add(this);
	}
	
	public Vector2f getPosition() {
		return position;
	}

	public void setPosition(float x, float y) {
		position.x = x;
		position.y = y;
	}

	public float getAngle() {
		return angle;
	}
		
	public void setAngle(float angle) {
		this.angle = angle;
	}
	
	public Vector2f getScale() {
		return scale;
	}

	public void setScale(float sx, float sy) {
		this.scale.x = sx;
		this.scale.y = sy;
	}	
	
	protected void drawSelf() {
		// override to draw this object
	}
	
	public void draw(Matrix3f parentMatrix) {
		GL4 gl = (GL4) GLContext.getCurrentGL();
		
		// set the model matrix
		
		calculateModelMatrix(parentMatrix);
		
		// draw self
		
		drawSelf();
		
		// draw children recursively
		
		for (SceneObject child : children) {
			child.draw(modelMatrix);
		}

	}

	private Matrix3f calculateModelMatrix(Matrix3f parentMatrix) {
		Transform.translationMatrix(position.x, position.y, translationMatrix);
		Transform.rotationMatrix(angle, rotationMatrix);
		Transform.scaleMatrix(scale.x, scale.y, scaleMatrix);

		// M = MP * T * R * S
		
		modelMatrix.set(parentMatrix);
		modelMatrix.mul(translationMatrix);
		modelMatrix.mul(rotationMatrix);
		modelMatrix.mul(scaleMatrix);
		
		return modelMatrix;
	}
	
}
