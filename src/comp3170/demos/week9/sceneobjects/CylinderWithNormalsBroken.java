package comp3170.demos.week9.sceneobjects;

import java.awt.event.KeyEvent;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import comp3170.InputManager;
import comp3170.demos.week9.cameras.Camera;
import comp3170.demos.week9.shaders.ShaderLibrary;

public class CylinderWithNormalsBroken extends SceneObject {

	private static final float TAU = (float) (Math.PI * 2);;
	private static final int NSIDES = 12;
	private final static String VERTEX = "normalVertex.glsl";
	private final static String FRAGMENT = "normalFragment.glsl";

	private Vector4f[] vertices;
	private int vertexBuffer;
	private Vector3f[] normals;
	private int normalBuffer;	
	private int[] indices;
	private int indexBuffer;
	
	private Matrix3f normalMatrix = new Matrix3f();
	
	public CylinderWithNormalsBroken() {
		super(ShaderLibrary.compileShader(VERTEX, FRAGMENT));
		
		createVertexBuffer();
		createIndexBuffer();
	}

	private void createVertexBuffer() {
		// Cylinder with pivot at the centre of the base, height 1 and radius 1.

		// vertices[0] = centre of base
		// vertices[1] = centre of top
		// vertices[2*i] = points around bottom edge
		// vertices[2*i+1] = points around top edge
		
		// TODO: This code contains an error. 
		// The normals for the top and bottom faces are not computed correctly.
		
		this.vertices = new Vector4f[NSIDES * 2 + 2];
		this.normals = new Vector3f[NSIDES * 2 + 2];
		
		int kv = 0;			
		int kn = 0;
		vertices[kv++] = new Vector4f(0,0,0,1);
		normals[kn++] = new Vector3f(0,-1,0);
		
		vertices[kv++] = new Vector4f(0,1,0,1);
		normals[kn++] = new Vector3f(0,1,0);
		
		// form the bottom and top edges by rotating point P = (1,0,0) about the y axis
		Vector4f p = new Vector4f(1,0,0,1);
		Vector3f n = new Vector3f(1,0,0);
		Matrix4f rotate4 = new Matrix4f();
		Matrix3f rotate3 = new Matrix3f();
		Matrix4f translate = new Matrix4f().translation(0,1,0);
		
		for (int i = 0; i < NSIDES; i++) {
			float angle = i * TAU / NSIDES; 
			rotate3.rotationY(angle);
			rotate4.rotationY(angle);
			
			vertices[kv++] = p.mul(rotate4, new Vector4f());  // vi = R(p)
			normals[kn++] = n.mul(rotate3, new Vector3f());   // ni = R(n)
			vertices[kv++] = p.mul(rotate4, new Vector4f()).mul(translate);  // v = T(R(p))			
			normals[kn++] = n.mul(rotate3, new Vector3f());   // ni = R(n)
		}
		
		this.vertexBuffer = shader.createBuffer(vertices);
		this.normalBuffer = shader.createBuffer(normals);
	}
	
	private void createIndexBuffer() {
		this.indices = new int[NSIDES * 3 * 4];
		
		int k = 0;
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

	private static final float ROTATION_SPEED = TAU/4;
	private Vector3f angle = new Vector3f();
	
	@Override
	public void update(InputManager input, float dt) {
		getAngle(angle);		
		if (input.isKeyDown(KeyEvent.VK_Z)) {
			angle.y = (angle.y - ROTATION_SPEED * dt) % TAU;
		}
		if (input.isKeyDown(KeyEvent.VK_X)) {
			angle.y = (angle.y + ROTATION_SPEED * dt) % TAU;
		}
		setAngle(angle);		
	}

	
	@Override
	public void draw(Camera camera) {
		GL4 gl = (GL4) GLContext.getCurrentGL();
		shader.enable();
		
		calcModelMatrix();
		shader.setUniform("u_modelMatrix", modelMatrix);
		shader.setUniform("u_viewMatrix", camera.getViewMatrix(viewMatrix));
		shader.setUniform("u_projectionMatrix", camera.getProjectionMatrix(projectionMatrix));
		shader.setUniform("u_normalMatrix", modelMatrix.normal(normalMatrix));
		
		shader.setAttribute("a_position", vertexBuffer);
		shader.setAttribute("a_normal", normalBuffer);
		
		gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
		gl.glDrawElements(GL.GL_TRIANGLES, indices.length, GL.GL_UNSIGNED_INT, 0);		

	}
	
	
}
