package comp3170.demos.week9.sceneobjects;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import comp3170.Shader;
import comp3170.demos.week9.cameras.Camera;

public class Cylinder extends SceneObject {

	private static final float TAU = (float) (Math.PI * 2);;
	private static final int NSIDES = 10;
	private Vector4f[] vertices;
	private int vertexBuffer;
	
	private int[] indices;
	private int indexBuffer;
	
	public Cylinder(Shader shader) {
		super(shader);
		
		// Cylinder with pivot at the centre of the base, height 1 and radius 1.

		// vertices[0] = centre of base
		// vertices[1] = centre of top
		// vertices[2*i] = points around bottom edge
		// vertices[2*i+1] = points around top edge
		
		this.vertices = new Vector4f[NSIDES * 2 + 2];
		
		int k = 0;			
		vertices[k++] = new Vector4f(0,0,0,1);
		vertices[k++] = new Vector4f(0,1,0,1);
		
		// form the bottom and top edges by rotating point P = (1,0,0) about the y axis
		Vector4f p = new Vector4f(1,0,0,1);
		Matrix4f rotate = new Matrix4f();
		Matrix4f translate = new Matrix4f().translation(0,1,0);
		
		for (int i = 0; i < NSIDES; i++) {
			float angle = i * TAU / NSIDES; 
			rotate.rotationY(angle);
			
			vertices[k++] = p.mul(rotate, new Vector4f());  // v = R(p)
			vertices[k++] = p.mul(rotate, new Vector4f()).mul(translate);  // v = T(R(p))			
		}
		
		this.vertexBuffer = shader.createBuffer(vertices);

		this.indices = new int[NSIDES * 3 * 4];
		
		k = 0;
		for (int i = 1; i <= NSIDES; i++) {
			int b0 = i * 2;
			int b1 = (i % NSIDES) * 2 + 2;
			int t0 = i * 2 + 1;
			int t1 = (i % NSIDES) * 2 + 3;
			
			// bottom 
			indices[k++] = 0;
			indices[k++] = b1; 
			indices[k++] = b0;

			// top 
			indices[k++] = 1;
			indices[k++] = t0;
			indices[k++] = t1;

			// side
			indices[k++] = b0;
			indices[k++] = b1;
			indices[k++] = t0;

			// side
			indices[k++] = b1;
			indices[k++] = t1;
			indices[k++] = t0;
		}
		
		this.indexBuffer = shader.createIndexBuffer(indices);
	}

	@Override
	public void draw(Camera camera) {
		GL4 gl = (GL4) GLContext.getCurrentGL();
		shader.enable();
		
		calcModelMatrix();
		shader.setUniform("u_modelMatrix", modelMatrix);
		shader.setUniform("u_viewMatrix", camera.getViewMatrix(viewMatrix));
		shader.setUniform("u_projectionMatrix", camera.getProjectionMatrix(projectionMatrix));
		
		shader.setUniform("u_colour", colour);		
		shader.setAttribute("a_position", vertexBuffer);
		
		gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
		gl.glDrawElements(GL.GL_TRIANGLES, indices.length, GL.GL_UNSIGNED_INT, 0);		

	}
	
	
}
