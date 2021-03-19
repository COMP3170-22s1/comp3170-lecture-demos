package comp3170.demos.week5;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import comp3170.Shader;

public class Cube extends Shape {
	private Vector4f[] vertices;
	int vertexBuffer;
	int[] indices;
	int indexBuffer;
	private final float TAU = (float) (Math.PI * 2);
	private final int NSEGMENTS = 10;

	public Cube(Shader shader) {
		super(shader);

		//
		// Create a cube with each face divide into an n x n grid
		//

		Vector4f[] grid = new Vector4f[(NSEGMENTS + 1) * (NSEGMENTS + 1)];

		int k = 0;
		for (int i = 0; i <= NSEGMENTS; i++) {
			float x = (float) 2 * i / NSEGMENTS - 1;	// from -1 to 1

			for (int j = 0; j <= NSEGMENTS; j++) {
				float y = (float) 2 * j / NSEGMENTS - 1;		// from -1 to 1

				grid[k++] = new Vector4f(x, y, 1, 1);
				
			}
		}

		Matrix4f[] sides = new Matrix4f[] { 
				new Matrix4f(), // front
				new Matrix4f().rotateY(TAU / 2), // back
				new Matrix4f().rotateY(TAU / 4), // right
				new Matrix4f().rotateY(-TAU / 4), // left
				new Matrix4f().rotateX(TAU / 4), // bottom
				new Matrix4f().rotateX(-TAU / 4), // top
		};

		this.vertices = new Vector4f[sides.length * grid.length];

		Matrix4f scale = new Matrix4f().scaling(1.0f / (float) Math.sqrt(3));
		
		k = 0;
		for (int s = 0; s < sides.length; s++) {
			for (int i = 0; i < grid.length; i++) {
				vertices[k] = new Vector4f(grid[i]).mul(sides[s]).mul(scale);
				k++;
			}
		}

		// copy the data into a Vertex Buffer Object in graphics memory
		this.vertexBuffer = shader.createBuffer(vertices);

		// Each quad looks like
		//
		// k+1 +--+ k + n + 2
		//     |\ |
		//     | \|
		//   k +--+ k + n + 1

		this.indices = new int[12 * vertices.length]; // 2 tris * 3 lines * 2 verts * width * height

		int n = 0;
		for (int s = 0; s < sides.length; s++) { // note there is no quad for the top row
			for (int i = 0; i < NSEGMENTS; i++) {
				for (int j = 0; j < NSEGMENTS; j++) {
					k = s * grid.length + i * (NSEGMENTS + 1) + j;
					
					indices[n++] = k;
					indices[n++] = k + NSEGMENTS + 1;

					indices[n++] = k + NSEGMENTS + 1;
					indices[n++] = k + 1;

					indices[n++] = k + 1;
					indices[n++] = k;

					indices[n++] = k + NSEGMENTS + 2;
					indices[n++] = k + 1;

					indices[n++] = k + 1;
					indices[n++] = k + NSEGMENTS + 1;

					indices[n++] = k + NSEGMENTS + 1;
					indices[n++] = k + NSEGMENTS + 2;
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
//		gl.glDrawElements(GL.GL_POINTS, indices.length, GL.GL_UNSIGNED_INT, 0);
		 gl.glDrawElements(GL.GL_LINES, indices.length, GL.GL_UNSIGNED_INT, 0);
	}

}
