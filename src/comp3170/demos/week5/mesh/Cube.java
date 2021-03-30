package comp3170.demos.week5.mesh;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import comp3170.Shader;
import comp3170.demos.week5.Mesh;

public class Cube extends Mesh {
	private Vector4f[] vertices;
	int vertexBuffer;
	int[] indices;
	int indexBuffer;
	private final float TAU = (float) (Math.PI * 2);

	public Cube(Shader shader, int nSegments) {
		super(shader);

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

		this.vertices = new Vector4f[sides.length * grid.length];

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
		this.vertexBuffer = shader.createBuffer(vertices);

		//
		// 3. create the index buffer for each face
		//
		
		// Each quad looks like
		//
		// k+1 +--+ k + n + 2
		//     |\ |
		//     | \|
		//   k +--+ k + n + 1

		this.indices = new int[12 * vertices.length]; // 2 tris * 3 lines * 2 verts * width * height

		int n = 0;
		for (int s = 0; s < sides.length; s++) { 
			for (int i = 0; i < nSegments; i++) {	// note there is no quad for the last row
				for (int j = 0; j < nSegments; j++) {	// ... or column
					k = s * grid.length + i * (nSegments + 1) + j;
					
					indices[n++] = k;
					indices[n++] = k + nSegments + 1;

					indices[n++] = k + nSegments + 1;
					indices[n++] = k + 1;

					indices[n++] = k + 1;
					indices[n++] = k;

					indices[n++] = k + nSegments + 2;
					indices[n++] = k + 1;

					indices[n++] = k + 1;
					indices[n++] = k + nSegments + 1;

					indices[n++] = k + nSegments + 1;
					indices[n++] = k + nSegments + 2;
				}
			}
		}

		this.indexBuffer = shader.createIndexBuffer(indices);

		this.colour = new Vector3f(1f, 1f, 1f); // default is white
	}
	
	public void draw() {
		GL4 gl = (GL4) GLContext.getCurrentGL();

		calcModelMatrix();
		shader.setUniform("u_modelMatrix", modelMatrix);

		// connect the vertex buffer to the a_position attribute
		shader.setAttribute("a_position", vertexBuffer);

		// write the colour value into the u_colour uniform
		shader.setUniform("u_colour", colour);

		gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
		gl.glDrawElements(GL.GL_LINES, indices.length, GL.GL_UNSIGNED_INT, 0);
	}

}
