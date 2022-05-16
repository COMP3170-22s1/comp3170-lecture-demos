package comp3170.demos.week12.sceneobjects;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import comp3170.GLBuffers;
import comp3170.InputManager;
import comp3170.Shader;
import comp3170.demos.SceneObject;
import comp3170.demos.week12.cameras.Camera;

public class Cylinder extends SceneObject {

	private static final float TAU = (float) (Math.PI * 2);;
	private static final int NSIDES = 12;

	private Shader shader;
	private Vector4f[] vertices;
	private int vertexBuffer;
	private Vector4f[] normals;
	private int normalBuffer;	
	private int[] indices;
	private int indexBuffer;
	
	private List<Integer> topIndices;
	private List<Integer> bottomIndices;
	private List<Integer> sideIndices;

	private float[] colour;

	public Cylinder(Shader shader, Color colour) {
		this.shader = shader;
		this.colour = colour.getRGBComponents(new float[4]);
		
		createVertexBuffer();
		createIndexBuffer();
	}

	private void createVertexBuffer() {
		vertices = new Vector4f[NSIDES * 4 + 2];
		normals = new Vector4f[NSIDES * 4 + 2];
		
		int kv = 0;			
		int kn = 0;

		Vector4f nUp = new Vector4f(0,1,0,0);
		Vector4f nDown = new Vector4f(0,-1,0,0);

		vertices[kv++] = new Vector4f(0,0,0,1);
		normals[kn++] = nDown;
		
		vertices[kv++] = new Vector4f(0,1,0,1);
		normals[kn++] = nUp;
		
		// form the bottom and top edges by rotating point P = (1,0,0) about the y axis
		Vector4f p = new Vector4f(1,0,0,1);
		Vector4f n = new Vector4f(1,0,0,0);

		Matrix4f rotate = new Matrix4f();
		Matrix4f translate = new Matrix4f().translation(0,1,0);
		
		topIndices = new ArrayList<Integer>();
		bottomIndices = new ArrayList<Integer>();
		sideIndices = new ArrayList<Integer>();
		
		for (int i = 0; i < NSIDES; i++) {
			float angle = i * TAU / NSIDES; 
			rotate.rotationY(angle);

			Vector4f vb = p.mul(rotate, new Vector4f());  // vb = R(p)
			Vector4f vt = p.mul(rotate, new Vector4f()).mul(translate);  // vt = T(R(p))
			Vector4f ni = n.mul(rotate, new Vector4f());   // ni = R(n)
			
			// bottom
			bottomIndices.add(kv);
			vertices[kv++] = vb;
			normals[kn++] = nDown;

			// top
			topIndices.add(kv);
			vertices[kv++] = vt;
			normals[kn++] = nUp;
			
			// side
			sideIndices.add(kv);
			vertices[kv++] = vb; 			
			normals[kn++] = ni;
			sideIndices.add(kv);
			vertices[kv++] = vt; 			
			normals[kn++] = ni;
		}
		
		vertexBuffer = GLBuffers.createBuffer(vertices);
		normalBuffer = GLBuffers.createBuffer(normals);
	}
	
	private void createIndexBuffer() {
		indices = new int[NSIDES * 3 * 4];
		
		int k = 0;
		for (int i = 0; i < NSIDES; i++) {
			int j = (i+1) % NSIDES;
			
			// bottom 
			indices[k++] = 0;
			indices[k++] = bottomIndices.get(j);
			indices[k++] = bottomIndices.get(i); 

			// top 
			indices[k++] = 1;
			indices[k++] = topIndices.get(i);
			indices[k++] = topIndices.get(j);

			// side
			indices[k++] = sideIndices.get(2 * i);
			indices[k++] = sideIndices.get(2 * j);
			indices[k++] = sideIndices.get(2 * i + 1);

			// side
			indices[k++] = sideIndices.get(2 * i + 1);
			indices[k++] = sideIndices.get(2 * j);
			indices[k++] = sideIndices.get(2 * j + 1);
		}
		
		indexBuffer = GLBuffers.createIndexBuffer(indices);
	}


	private static final float ROTATION_SPEED = TAU/4;
	private Vector3f angle = new Vector3f();
	
	public void update(InputManager input, float dt) {
		if (input.isKeyDown(KeyEvent.VK_Z)) {
			getMatrix().rotateY(- ROTATION_SPEED * dt);
		}
		if (input.isKeyDown(KeyEvent.VK_X)) {
			getMatrix().rotateY(ROTATION_SPEED * dt);
		}
	}

	@Override
	public void drawSelf(Matrix4f mvpMatrix) {
		GL4 gl = (GL4) GLContext.getCurrentGL();
		shader.enable();		
		shader.setUniform("u_mvpMatrix", mvpMatrix);
		shader.setAttribute("a_position", vertexBuffer);
				
		if (shader.hasUniform("u_colour")) {
			shader.setUniform("u_colour", colour);
		}

	
		gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
		gl.glDrawElements(GL.GL_TRIANGLES, indices.length, GL.GL_UNSIGNED_INT, 0);		

	}
	
	
}
