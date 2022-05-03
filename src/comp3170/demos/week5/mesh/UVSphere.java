package comp3170.demos.week5.mesh;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import comp3170.GLBuffers;
import comp3170.Shader;
import comp3170.demos.SceneObject;

public class UVSphere extends SceneObject {
	private Shader shader;
	private Vector4f[] vertices;
	private int vertexBuffer;
	private int[] indices;
	private int indexBuffer;
	private float[] colour = {1f, 1f, 1f};

	private final float TAU = (float) (Math.PI * 2);
	
	public UVSphere(Shader shader, int nSegments) {
		this.shader = shader;
		//
		// Create a (2n+1) * (n+1) grid of points in polar space
		//
		int width = 2 * nSegments;
		int height = nSegments;
		
		vertices = new Vector4f[(width +1) * (height+1)];
		
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
	    vertexBuffer = GLBuffers.createBuffer(vertices);
	    
	    // Each quad looks like
	    //
	    // k+1 +--+ k + h + 2
	    //     |\ |
	    //     | \|
	    //   k +--+ k + h + 1
	    
	    indices = new int[12 * width * height]; // 2 tris * 3  verts * width height

	    int n = 0;
		for (int i = 0; i < width; i++) {		// note there is no quad for the top row
			for (int j = 0; j < height; j++) { 	// or the right-hand column
				k = i * (height+1) + j;
				
				indices[n++] = k;
				indices[n++] = k + height + 1;
				indices[n++] = k + 1;

				indices[n++] = k + height + 2;
				indices[n++] = k + 1;
				indices[n++] = k + height + 1;
			}			
		}
	    
	    indexBuffer = GLBuffers.createIndexBuffer(indices);

	}
	
	@Override
	protected void drawSelf(Matrix4f modelMatrix) {
		GL4 gl = (GL4) GLContext.getCurrentGL();

		shader.enable();
		shader.setUniform("u_modelMatrix", modelMatrix);
	    shader.setAttribute("a_position", vertexBuffer);
	    shader.setUniform("u_colour", colour);	    
	    
		gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
		gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL4.GL_LINE);
		gl.glDrawElements(GL.GL_TRIANGLES, indices.length, GL.GL_UNSIGNED_INT, 0);
	}

	
}
