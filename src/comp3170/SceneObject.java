package comp3170;

import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class SceneObject {

	private Shader shader;
	
	public Matrix4f localMatrix;
	protected Matrix4f worldMatrix;
	protected Matrix4f mvpMatrix;
	
	private List<SceneObject> children;
	private SceneObject parent;

	
	public SceneObject() {
		// Shader is null. For use in empty objects only
		this(null);
	}
	
	public SceneObject(Shader shader) {
		this.shader = shader;
		this.localMatrix = new Matrix4f();
		this.localMatrix.identity();

		this.worldMatrix = new Matrix4f();
		this.mvpMatrix = new Matrix4f();
			
		this.parent = null;
		this.children = new ArrayList<SceneObject>();
	}
	
	/**
	 * Change the shader 
	 * @param shader
	 */
	public void setShader(Shader shader) {
		this.shader = shader;
	}
	
	/**
	 * Set the parent of this object in the scene graph
	 * 
	 * @param parent
	 */
	
	public void setParent(SceneObject parent) {
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
		
		SceneObject obj = this.parent;
		
		while (obj != null) {
			matrix.mulLocal(obj.localMatrix);
			obj = obj.parent;
		}
		return matrix;
	}

	/**
	 * Get the normal transformation matrix for this object.
	 * 
	 * Note that you must pre-allocate a matrix in which to store the result
	 * 
	 * @param matrix	The matrix in which to store the result
	 * @return
	 */
	public Matrix3f getNormalMatrix(Matrix3f matrix) {
		getWorldMatrix(this.worldMatrix);
		this.worldMatrix.normal(matrix);
		return matrix;
	}
	
	/**
	 * Write the position of this object into the 4D position homogeneous vector given 
	 * @return
	 */
	public Vector4f getPosition(Vector4f position) {
		getWorldMatrix(this.worldMatrix);
		position.set(0,0,0,1);
		position.mul(this.worldMatrix);		
		
		return position;		
	}
	
	private Vector4f position = new Vector4f();
	
	/**
	 * Write the position of this object into the 3D position vector given 
	 * @return
	 */
	public Vector3f getPosition(Vector3f position) {
		getWorldMatrix(this.worldMatrix);
		this.position.set(0,0,0,1);
		this.position.mul(this.worldMatrix);		
		position.set(this.position.x, this.position.y, this.position.z);
		
		return position;		
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
	 * @param parentMatrix	The parent's MVP matrix
	 */
	public void draw(Matrix4f parentMatrix) {
		
		// copy the parent matrix
		parentMatrix.get(this.mvpMatrix);	// mvpMatrix = parentMatrix
		
		// multiply it by the local matrix
		this.mvpMatrix.mul(this.localMatrix);	// mvpMatrix = mvpMatrix * localMatrix
		
		if (this.shader != null) {
			this.shader.enable();
			// draw this object
			drawSelf(this.shader);
		}
		
	
		// draw the children 
		for (SceneObject child : children) {
			child.draw(mvpMatrix);
		}
		
	}

}
