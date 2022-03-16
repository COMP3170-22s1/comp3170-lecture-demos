package comp3170.demos;

import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;

import comp3170.Shader;

/**
 * A simple implementation of a scene graph.
 * 
 * To use:
 * 
 * 1) Create a 'root' object in your scene:
 * 
 * root = new SceneObject();
 * 
 * 2) Extend this class to implement specific objects in the graph. 
 * 
 * 3) Subclasses should override the drawSelf() method to draw themselves. 
 *    Children will automatically be drawn using the draw() method below.
 *    
 * 4) Add new objects to the graph using the setParent method, e.g.:
 * 
 * Ship ship = new Ship(); // a subclass of SceneObject
 * ship.setParent(root);   // ship is now a child of root in the graph
 * 
 * Pirate pirate = new Pirate(): // a subclass of SceneObject
 * private.setParent(ship);		 // pirate is now a child of ship 	
 * 
 * 5) You can use this class without extending it to make 'pivot' objects in the graph that are not drawn.
 * 
 * 6) Use the getMatrix() method to access the local coordinate frame matrix from this object to its parent.
 * 
 * Matrix4f pirateToShipMatrix = pirate.getMatrix();
 * pirateToShipMatrix.translate(1,0,0);		// move the pirate 1 unit to the right in its local coordinate frame
 * 
 * 7) Use the draw() method to draw the entire scene graph with a given shader. 
 * 
 * root.draw(shader); // recursively draws the ship and the pirate 
 * 
 * @author malcolmryan
 *
 */
public class SceneObject {

	private static final Matrix4f IDENTITY = new Matrix4f().identity();
	
	private Matrix4f modelToParentMatrix;
	private List<SceneObject> children;
	private SceneObject parent;
	
	public SceneObject() {
		// Allocate model matrix and initialise to the identity matrix
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
	 * Get the model->parent matrix
	 * @return	The model matrix
	 */
	public Matrix4f getMatrix()
	{
		return modelToParentMatrix;
	}	

	/**
	 * Draw this object. Override this in subclasses to draw specific objects.
	 * 
	 * @param shader	The shader to use.
	 * @param worldMatrix	The world matrix to use.
	 */
	protected void drawSelf(Shader shader, Matrix4f worldMatrix) {
		// do nothing
	}

	/**
	 * Draw this object and all its children in the subgraph.
	 * 
	 * @param shader	The shader to use.
	 */
	public void draw(Shader shader) {
		draw(shader, IDENTITY);
	}

	// Allocate a matrix for use in draw() to calculate the model->world Matrix.
	// (Note: this isn't guaranteed to hold an up-to-date value, don't use it in other methods)
	private Matrix4f tempModelMatrix = new Matrix4f();

	/**
	 * Draw this object and all its children in the subgraph.
	 * 
	 * @param shader	The shader to use.
	 * @param parentMatrix	The model->world matrix for the parent. Set to the identity matrix when drawing the root.
	 */
	private void draw(Shader shader, Matrix4f parentMatrix) {
		// W = P * M
		tempModelMatrix.set(parentMatrix);			// W = P
		tempModelMatrix.mul(modelToParentMatrix);	// W = W * M
		
		// draw the object using the shader and calculated model matrix
		drawSelf(shader, tempModelMatrix);
	
		// recursively draw the children
		for (SceneObject child : children) {
			child.draw(shader, tempModelMatrix);
		}
		
	}

}
