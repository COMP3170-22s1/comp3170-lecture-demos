package comp3170.demos.week5.extrusion;

import static com.jogamp.opengl.GL.GL_ELEMENT_ARRAY_BUFFER;
import static com.jogamp.opengl.GL.GL_UNSIGNED_INT;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import comp3170.GLBuffers;
import comp3170.Shader;
import comp3170.demos.SceneObject;

public class Extrusion extends SceneObject {

	private Shader shader;
	private Vector4f[] vertices;
	private int vertexBuffer;

	private int[] indices;
	private int indexBuffer;
	
	private float[] colour = {1f, 1f, 1f};
	
	public Extrusion(Shader shader, Vector2f[] crossSection, Vector3f[] curve, Vector3f up) {
		this.shader = shader;
		createVertexBuffer(crossSection, curve, up);
		createIndexBuffer(crossSection, curve);
	}

	private void createVertexBuffer(Vector2f[] crossSection, Vector3f[] curve, Vector3f up) {
		// allocate vectors for the three coordinate axes 
		// we need both non-homogeneous and homogenous forms
		// because the cross product is only implemented on Vector3f (annoying)
		
		Vector3f iAxis = new Vector3f();
		Vector3f jAxis = new Vector3f();
		Vector3f kAxis = new Vector3f();

		Vector4f iAxis4 = new Vector4f();
		Vector4f jAxis4 = new Vector4f();
		Vector4f kAxis4 = new Vector4f();
		Vector4f curve4 = new Vector4f();
		
		Matrix4f matrix = new Matrix4f();

		
		vertices = new Vector4f[curve.length * crossSection.length];
		
		int k = 0;
		for (int i = 0; i < curve.length; i++) {
			// approximate the tangent by the vector between successive points
			// this requires a special case for the last point
			
			if (i == curve.length - 1) {
				curve[i].sub(curve[i-1], kAxis);	// k = P[i] - P[i-1] 
			}
			else {
				curve[i+1].sub(curve[i], kAxis);	// k = P[i+1] - P[i] 
			}

			// normalise this to the get k-axis
			kAxis.normalize();
			
			up.cross(kAxis, iAxis);		// i = up x k
			iAxis.normalize();
			
			kAxis.cross(iAxis, jAxis);	// j = k x i
						
			// convert to homogeneous form
			iAxis4.set(iAxis, 0);
			jAxis4.set(jAxis, 0);
			kAxis4.set(kAxis, 0);
			curve4.set(curve[i], 1);
			
			// create the coordinate frame matrix
			
			matrix.set(iAxis4, jAxis4, kAxis4, curve4);

			// We can also transform the cross section as we go.
			// E.g. Scale the cross-section along the curve 
//			matrix.scale(1.0f - (float)i / (curve.length-1));
			
			// using this matrix to transform the cross-section points into 3D
			
			for (int j = 0; j < crossSection.length; j++) {
				vertices[k] = new Vector4f(crossSection[j].x, crossSection[j].y, 0, 1);  // v = (x, y, 0, 1)
				vertices[k].mul(matrix);
				k++;				
			}
		}
		
	    vertexBuffer = GLBuffers.createBuffer(vertices);
	}

	private void createIndexBuffer(Vector2f[] crossSection, Vector3f[] curve) {
		//
		// a = size of cross-section
		// b = length of curve
		//
		//  0 --- a --- ... --- (b-1) * a 
		//  |     |              |
		// ... - ... -  ... --- ... 
		//  |     |              |
		// a-1 - 2a-1 - ... --- (b-1) * a + (a-1)
		//  |     |              |
		//  0     a     ...     (b-1) * a	<-- loop around to top
		
		
		indices = new int[2 * crossSection.length * (2 * curve.length - 1)];
		
		int a = crossSection.length;
		int b = curve.length;
		
		int k = 0;
		for (int i = 0; i < b; i++) {
			for (int j = 0; j < a; j++) {
				// lines going around the cross-section
				indices[k++] = a * i + j;
				indices[k++] = a * i + (j + 1) % a; // wrap around when j = a-1

				// lines joining to the next cross-section
				if (i < b-1) {
					indices[k++] = a * i + j;
					indices[k++] = a * i + j + a;										
				}
			}
		}
		indexBuffer = GLBuffers.createIndexBuffer(indices);
	}

	@Override
	protected void drawSelf(Matrix4f modelMatrix) {
		GL4 gl = (GL4) GLContext.getCurrentGL();

		shader.enable();				
		shader.setUniform("u_modelMatrix", modelMatrix);
		
        // connect the vertex buffer to the a_position attribute		   
	    shader.setAttribute("a_position", vertexBuffer);

	    // write the colour value into the u_colour uniform 
	    shader.setUniform("u_colour", colour);	    
	    
	    gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer);

	    // DEBUG: just draw the vertices
//	    gl.glDrawElements(GL.GL_POINTS, indices.length, GL_UNSIGNED_INT, 0);		

	    // Draw the wireframe as lines
	    gl.glDrawElements(GL.GL_LINES, indices.length, GL_UNSIGNED_INT, 0);		
	}

}
