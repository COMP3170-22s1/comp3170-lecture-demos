package comp3170.demos.week9.sceneobjects;

import static com.jogamp.opengl.GL.GL_TRIANGLES;

import java.awt.event.KeyEvent;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import comp3170.GLBuffers;
import comp3170.InputManager;
import comp3170.demos.week7.shaders.ShaderLibrary;
import comp3170.demos.week9.cameras.Camera;

public class CylinderWireframe extends SceneObject {

	private static final float TAU = (float) (Math.PI * 2);;
	private static final int NSIDES = 12;
	private final static String VERTEX = "simpleVertex.glsl";
	private final static String FRAGMENT = "simpleFragment.glsl";

	private Vector4f[] vertices;
	private int vertexBuffer;	
	private int[] indices;
	private int indexBuffer;
	
	private Vector3f normalsColour = new Vector3f(1,1,0); // YELLOW
	private Vector4f[] normals;
	private Vector4f[] normalVertices;
	private int normalVertexBuffer;
	private boolean showNormals = false;
	
	public CylinderWireframe() {
		super(ShaderLibrary.compileShader(VERTEX, FRAGMENT));
		
		createVertexBuffer();
		createIndexBuffer();

		// draw the normals
		createNormals();
		createNormalVerticesBuffer();

	}

	private void createVertexBuffer() {
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
		
		this.vertexBuffer = GLBuffers.createBuffer(vertices);
	}


	private void createIndexBuffer() {
		this.indices = new int[NSIDES * 2 * 6];
		
		int k = 0;
		for (int i = 1; i <= NSIDES; i++) {
			int b0 = i * 2;
			int b1 = (i % NSIDES) * 2 + 2;
			int t0 = i * 2 + 1;
			int t1 = (i % NSIDES) * 2 + 3;
			
			// bottom star
			indices[k++] = 0;
			indices[k++] = b1; 
			
			// bottom circle
			indices[k++] = b0;
			indices[k++] = b1;

			// top star
			indices[k++] = 1;
			indices[k++] = t0;

			// top circle
			indices[k++] = t0;
			indices[k++] = t1;
			
			// side
			indices[k++] = b0;
			indices[k++] = t0;

			// side
			indices[k++] = t0;
			indices[k++] = b1;
		}
		
		this.indexBuffer = GLBuffers.createIndexBuffer(indices);
	}

	
	private void createNormals() {
		// The normal at each vertex points along th radius of the circle
		// Note: normals are vectors, not points, so w = 0

		this.normals = new Vector4f[NSIDES * 2 + 2];
		
		int k = 0;			
		normals[k++] = new Vector4f(0,-1,0,0); // bottom, n = down
		normals[k++] = new Vector4f(0,1,0,0);  // top, n = up
		
		// form the bottom and top edges by rotating point P = (1,0,0) about the y axis
		Vector4f n = new Vector4f(1,0,0,0);
		Matrix4f rotate = new Matrix4f();
		
		for (int i = 0; i < NSIDES; i++) {
			float angle = i * TAU / NSIDES; 
			rotate.rotationY(angle);
			Vector4f ni = n.mul(rotate, new Vector4f());  // ni = R(n)
			
			// the normal is the same at the top and bottom;
			normals[k++] = ni;
			normals[k++] = ni;			
		}		
	}
	
	private void createNormalVerticesBuffer() {
		
		// draw the normals as lines extending from each vertex
		
		this.normalVertices = new Vector4f[vertices.length * 2];
		
		int k = 0;
		for (int i = 0; i < this.vertices.length; i++) {			
			this.normalVertices[k++] = vertices[i];
			this.normalVertices[k++] = vertices[i].add(normals[i], new Vector4f());
		}
		
		this.normalVertexBuffer = GLBuffers.createBuffer(normalVertices);
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
		if (input.wasKeyPressed(KeyEvent.VK_SPACE)) {
			this.showNormals = !this.showNormals;
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
		
		shader.setUniform("u_colour", colour);		
		shader.setAttribute("a_position", vertexBuffer);
		
		gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
		gl.glDrawElements(GL.GL_LINES, indices.length, GL.GL_UNSIGNED_INT, 0);
		
		if (showNormals) {
			// draw the normals
			shader.setUniform("u_colour", normalsColour);		
			shader.setAttribute("a_position", normalVertexBuffer);
	        gl.glDrawArrays(GL.GL_LINES, 0, normalVertices.length);           	
		}

	}
	
	
}
