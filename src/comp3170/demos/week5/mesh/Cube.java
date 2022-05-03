package comp3170.demos.week5.mesh;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import comp3170.GLBuffers;
import comp3170.Shader;
import comp3170.demos.SceneObject;

public class Cube extends SceneObject {
	private static final float TAU = (float) (Math.PI * 2);

	private Shader shader;
	
	private Vector4f[] vertices;
	private int vertexBuffer;
	private int[] indices;
	private int indexBuffer;
	private float[] colour = {1.0f, 1.0f, 1.0f};

	public Cube(Shader shader, int nSegments) {
		
		this.shader = shader;

		//
		// Create a cube with each face divided into an n x n grid
		//
		// 1. create the vertices on the grid for one face
		//

		Vector4f[] grid = new Vector4f[(nSegments + 1) * (nSegments + 1)];

		int k = 0;
		for (int i = 0; i <= nSegments; i++) {
			float x = (float) 2 * i / nSegments - 1;	// from -1 to 1

			for (int j = 0; j <= nSegments; j++) {
				float y = (float) 2 * j / nSegments - 1;		// from -1 to 1

				grid[k++] = new Vector4f(x, y, 1, 1);
				
			}
		}
		
		//
		// 2. Rotate copies of the grid point to form the six faces
		// 

		Matrix4f[] sides = new Matrix4f[] { 
			new Matrix4f(), // front
			new Matrix4f().rotateY(TAU / 2), // back
			new Matrix4f().rotateY(TAU / 4), // right
			new Matrix4f().rotateY(-TAU / 4), // left
			new Matrix4f().rotateX(TAU / 4), // bottom
			new Matrix4f().rotateX(-TAU / 4), // top
		};

		vertices = new Vector4f[sides.length * grid.length];

		// scale of 1/sqrt(3) means the corner points lie on the unit cube
		Matrix4f scale = new Matrix4f().scaling(1.0f / (float) Math.sqrt(3));
		
		k = 0;
		for (int s = 0; s < sides.length; s++) {
			for (int i = 0; i < grid.length; i++) {
				// rotate and scale each point
				vertices[k] = new Vector4f(grid[i]).mul(sides[s]).mul(scale);
//				vertices[k] = new Vector4f(grid[i]).mul(sides[s]).mul(scale).normalize3();
				k++;
			}
		}
		vertexBuffer = GLBuffers.createBuffer(vertices);

		//
		// 3. create the index buffer for each face
		//
		
		// Each quad looks like
		//
		// k+1 +--+ k + n + 2
		//     |\ |
		//     | \|
		//   k +--+ k + n + 1

		indices = new int[6 * vertices.length]; // 2 tris * 3  verts * width * height

		int n = 0;
		for (int s = 0; s < sides.length; s++) { 
			for (int i = 0; i < nSegments; i++) {	// note there is no quad for the last row
				for (int j = 0; j < nSegments; j++) {	// ... or column
					k = s * grid.length + i * (nSegments + 1) + j;
					
					indices[n++] = k;
					indices[n++] = k + nSegments + 1;
					indices[n++] = k + 1;

					indices[n++] = k + nSegments + 2;
					indices[n++] = k + 1;
					indices[n++] = k + nSegments + 1;
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
		shader.setAttribute("a_position", vertexBuffer);
		shader.setUniform("u_colour", colour);

		gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
		gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL4.GL_LINE);
		gl.glDrawElements(GL.GL_TRIANGLES, indices.length, GL.GL_UNSIGNED_INT, 0);
	}

}
