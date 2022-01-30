package comp3170.demos.week2.transforms.sceneobjects;

import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;

import comp3170.Shader;

public class SceneObject {

	private Matrix4f modelToParentMatrix;
	private List<SceneObject> children;
	private SceneObject parent;
	
	public SceneObject() {
		// Allocate local coordinate frame matrix and initialise to the identity matrix
		modelToParentMatrix = new Matrix4f();
		modelToParentMatrix.identity();
					
		// Initialise the scenegraph connections to parent and children
		parent = null;
		children = new ArrayList<SceneObject>();
	}
	
	/**
	 * Set the parent of this object in the scenegraph.
	 * 
	 * @param newParent The new parent object
	 */
	public void setParent(SceneObject newParent) {
		// disconnect from the old part if necessary
		if (parent != null) {
			parent.children.remove(this);
		}
		
		parent = newParent;
		
		if (newParent != null) {
			newParent.children.add(this);
		}
	}
	
	/**
	 * Get the local coordinate frame matrix
	 * @return	The matrix
	 */
	public Matrix4f getMatrix()
	{
		return modelToParentMatrix;
	}	

	/**
	 * Draw this object. Override this in subclasses to draw specific objects.
	 * 
	 * @param shader	The shader to use.
	 * @param modelToWorldMatrix	The model->world matrix to use.
	 */
	protected void drawSelf(Shader shader, Matrix4f modelToWorldMatrix) {
		// do nothing
	}
	
	// Allocate a matrix for use in draw() to calculate the model->world matrix.
	// This is allocated once outside the method to avoid reallocating on every new frame.
	// (Note: this isn't guaranteed to hold an up-to-date value, don't use it in other methods)
	private Matrix4f modelToWorldMatrix = new Matrix4f();

	/**
	 * Draw this object and all its children in the subgraph.
	 * 
	 * @param shader	The shader to use.
	 * @param parentMatrix	The model->world matrix for the parent. Set to the identity matrix when drawing the root.
	 */
	public void draw(Shader shader, Matrix4f parentToWorldMatrix) {
		// M_(m->w) = M_(p->w) * M_(m->p)
		modelToWorldMatrix.set(parentToWorldMatrix);
		modelToWorldMatrix.mul(modelToParentMatrix);
		
		drawSelf(shader, modelToWorldMatrix);
	
		for (SceneObject child : children) {
			child.draw(shader, modelToWorldMatrix);
		}
		
	}

}
