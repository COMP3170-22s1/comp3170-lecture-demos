package comp3170.demos.week9.sceneobjects;

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
import comp3170.demos.week9.cameras.Camera;
import comp3170.demos.week9.shaders.ShaderLibrary;

public class Cylinder extends SceneObject {

	private static final float TAU = (float) (Math.PI * 2);;
	private static final int NSIDES = 12;
	private final static String VERTEX = "lightVertex.glsl";
	private final static String FRAGMENT = "lightFragment.glsl";

	private Vector4f[] vertices;
	private int vertexBuffer;
	private Vector4f[] normals;
	private int normalBuffer;	
	private int[] indices;
	private int indexBuffer;
	
	private Matrix4f normalMatrix = new Matrix4f();
	private List<Integer> topIndices;
	private List<Integer> bottomIndices;
	private List<Integer> sideIndices;
	
	private Vector4f lightDirection = new Vector4f();
	private Vector3f lightIntensity = new Vector3f();
	private Vector3f ambientIntensity = new Vector3f();
	private Vector4f viewDirection = new Vector4f();
	
	private Vector3f diffuseMaterial = new Vector3f(1,1,1);		// default to white
	private Vector3f specularMaterial = new Vector3f(1,1,1);	// default to white
	
	private Shader shader;
	private DirectionalLight light;
	private Camera camera;


	public Cylinder() {
		shader = ShaderLibrary.compileShader(VERTEX, FRAGMENT);
		
		createVertexBuffer();
		createIndexBuffer();
	}

	public void setDiffuseMaterial(Color colour) {
		float[] rgb = colour.getRGBColorComponents(new float[3]);
		diffuseMaterial.x = rgb[0];
		diffuseMaterial.y = rgb[1];
		diffuseMaterial.z = rgb[2];
	}

	public void setSpecularMaterial(Color colour) {
		float[] rgb = colour.getRGBColorComponents(new float[3]);
		specularMaterial.x = rgb[0];
		specularMaterial.y = rgb[1];
		specularMaterial.z = rgb[2];
	}
	
	public void setLight(DirectionalLight light) {
		this.light = light;
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
	}

	private void createVertexBuffer() {
		// Cylinder with pivot at the centre of the base, height 1 and radius 1.

		// vertices[0] = centre of base
		// vertices[1] = centre of top
		// vertices[2*i] = points around bottom edge
		// vertices[2*i+1] = points around top edge
		
		// TODO: This code contains an error. 
		// The normals for the top and bottom faces are not computed correctly.
		
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
			getMatrix().rotateY(-ROTATION_SPEED * dt);
		}
		if (input.isKeyDown(KeyEvent.VK_X)) {
			getMatrix().rotateY(ROTATION_SPEED * dt);
		}
	}

	private Matrix4f modelMatrix = new Matrix4f();
	
	@Override
	public void drawSelf(Matrix4f mvpMatrix) {
		GL4 gl = (GL4) GLContext.getCurrentGL();
		shader.enable();
		
		getModelToWorldMatrix(modelMatrix);
		shader.setUniform("u_mvpMatrix", mvpMatrix);
		shader.setUniform("u_modelMatrix", modelMatrix);
		shader.setUniform("u_normalMatrix", modelMatrix.normal(normalMatrix));
		
		shader.setUniform("u_lightDirection", light.getDirection(lightDirection));
		shader.setUniform("u_intensity", light.getIntensity(lightIntensity));
		shader.setUniform("u_ambientIntensity", light.getAmbientIntensity(ambientIntensity));
		shader.setUniform("u_viewDirection", camera.getViewDirection(viewDirection));

		shader.setUniform("u_diffuseMaterial", diffuseMaterial);
		shader.setUniform("u_specularMaterial", specularMaterial);
		
		shader.setAttribute("a_position", vertexBuffer);
		shader.setAttribute("a_normal", normalBuffer);
		
		gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
		gl.glDrawElements(GL.GL_TRIANGLES, indices.length, GL.GL_UNSIGNED_INT, 0);		

	}
	
	
}
