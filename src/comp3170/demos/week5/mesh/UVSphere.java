package comp3170.demos.week5.mesh;

import static com.jogamp.opengl.GL.GL_ELEMENT_ARRAY_BUFFER;
import static com.jogamp.opengl.GL.GL_UNSIGNED_INT;

import java.awt.Color;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import comp3170.Shader;
import comp3170.demos.week5.Mesh;

public class UVSphere extends Mesh {
	private Vector4f[] vertices;
	private int vertexBuffer;
	private int[] indices;
	private int indexBuffer;

	private final float TAU = (float) (Math.PI * 2);
	
	public UVSphere(Shader shader, int nSegments) {
		super(shader);		
		//
		// Create a (2n+1) * (n+1) grid of points in polar space
		//
		int width = 2 * nSegments;
		int height = nSegments;
		
		this.vertices = new Vector4f[(width +1) * (height+1)];
		
		Matrix4f rotateY = new Matrix4f();
		Matrix4f rotateX = new Matrix4f();

		int k = 0;
		for (int i = 0; i <= width; i++) {
			float heading = i * TAU / width;	// from 0 to TAU
			rotateY.rotationY(heading);
			
			for (int j = 0; j <= height; j++) {
				float pitch = (TAU /2) * (float)j / height - TAU / 4; // from -TAU/4 to TAU/4
				rotateX.rotationX(pitch);
				
				vertices[k] = new Vector4f(0, 0, 1, 1);	// unit vector in z direction
				vertices[k].mul(rotateY).mul(rotateX);
				k++;
			}			
		}
		
		// copy the data into a Vertex Buffer Object in graphics memory		
	    this.vertexBuffer = shader.createBuffer(vertices);
	    
	    // Each quad looks like
	    //
	    // k+1 +--+ k + h + 2
	    //     |\ |
	    //     | \|
	    //   k +--+ k + h + 1
	    
	    this.indices = new int[12 * width * height]; // 2 tris * 3 lines * 2 verts * width height

	    int n = 0;
		for (int i = 0; i < width; i++) {		// note there is no quad for the top row
			for (int j = 0; j < height; j++) { 	// or the right-hand column
				k = i * (height+1) + j;
				
				indices[n++] = k;
				indices[n++] = k + height + 1;

				indices[n++] = k + height + 1;
				indices[n++] = k + 1;

				indices[n++] = k + 1;
				indices[n++] = k;

				indices[n++] = k + height + 2;
				indices[n++] = k + 1;

				indices[n++] = k + 1;
				indices[n++] = k + height + 1;

				indices[n++] = k + height + 1;
				indices[n++] = k + height + 2;
			}			
		}
	    
	    this.indexBuffer = shader.createIndexBuffer(indices);

	    this.colour = new Vector3f(1f, 1f, 1f);	// default is white
	}
	
	@Override
	public void draw() {
		GL4 gl = (GL4) GLContext.getCurrentGL();

		calcModelMatrix();
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
