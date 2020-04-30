package comp3170;

import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4d;
import org.joml.Matrix4f;

public class SceneObjectOld {

	public Matrix4f localMatrix;
	private Matrix4f worldMatrix;
	private List<SceneObjectOld> children;
	private SceneObjectOld parent;
	
	public SceneObjectOld() {
		this.localMatrix = new Matrix4f();
		this.localMatrix.identity();
		
		this.worldMatrix = new Matrix4f();
		this.worldMatrix.identity();
			
		this.parent = null;
		this.children = new ArrayList<SceneObjectOld>();
	}
	
	/**
	 * Set the parent of this object in the scene graph
	 * 
	 * @param parent
	 */
	
	public void setParent(SceneObjectOld parent) {
		if (this.parent != null) {
			this.parent.children.remove(this);
		}
		
		this.parent = parent;
		
		if (this.parent != null) {
			this.parent.children.add(this);
		}
	}
	
	/**
	 * Get the global transformation matrix for this object.
	 * 
	 * Note that you must pre-allocate a matrix in which to store the result
	 * 
	 * @param matrix	The matrix in which to store the result
	 * @return
	 */
	public Matrix4f getWorldMatrix(Matrix4f matrix) {
		localMatrix.get(matrix);
		
		SceneObjectOld obj = this.parent;
		
		while (obj != null) {
			matrix.mulLocal(obj.localMatrix);
			obj = obj.parent;
		}
		return matrix;
	}
	
	/**
	 * Draw this object. 
	 * Override this to draw specific objects.
	 * 
	 * @param shader
	 */
	protected void drawSelf(Shader shader) {
		// do nothing
	}
	

	/**
	 * Recursively draw the object and all its children
	 * 
	 * @param shader		The shader with which to draw
	 * @param parentMatrix	The parent's world matrix
	 */
	public void draw(Shader shader, Matrix4f parentMatrix) {
		
		// copy the parent matrix
		parentMatrix.get(this.worldMatrix);
		
		// multiply it by the local matrix
		this.worldMatrix.mul(this.localMatrix);
		
		// send this matrix to the shader
		shader.setUniform("u_worldMatrix", this.worldMatrix);
		
		// draw this objects
		drawSelf(shader);
	
		// draw the children 
		for (SceneObjectOld child : children) {
			child.draw(shader, worldMatrix);
		}
		
	}

}
