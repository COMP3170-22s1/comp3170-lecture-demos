package comp3170.demos.week12.sceneobjects;

import java.awt.event.KeyEvent;
import java.io.IOException;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import comp3170.GLBuffers;
import comp3170.InputManager;
import comp3170.Shader;
import comp3170.demos.SceneObject;
import comp3170.demos.week12.textures.TextureLibrary;
import comp3170.demos.week12.shaders.ShaderLibrary;

public class CylinderWithNormalMap extends SceneObject {

	private static final String VERTEX_SHADER = "normalMapVertex.glsl";
	private static final String FRAGMENT_SHADER = "normalMapFragment.glsl";
	private static final String DIFFUSE_TEXTURE = "brick-diffuse.png";
	private static final String NORMAL_MAP_TEXTURE = "brick-normals.png";
	
	private static final float TAU = (float) (Math.PI * 2);;
	private static final int NSIDES = 12;
	private static final float UMAX = 3;
	private static final float VMAX = 1;
	
	private Shader shader;
	private Vector4f[] vertices;
	private int vertexBuffer;
	private Vector2f[] uvs;
	private int uvBuffer;
	private Matrix3f[] tangentMatrices;
	private int tangetMatrixBuffer;
	private int diffuseTexture;
	private int normalMapTexture;	
	
	public CylinderWithNormalMap() {
		shader = ShaderLibrary.compileShader(VERTEX_SHADER, FRAGMENT_SHADER);
		createVertexBuffer();
		createUVBuffer();
		loadTextures();
		createTangentMatrixBuffer();
		
	}

	private void createVertexBuffer() {
		Vector4f[] top = new Vector4f[NSIDES+1];
		Vector4f[] bottom = new Vector4f[NSIDES+1];
		
		// first construct the points
		
		// form the bottom and top edges by rotating point P = (1,0,0) about the y axis
		Vector4f p = new Vector4f(1,0,0,1);

		Matrix4f rotate = new Matrix4f();
		Matrix4f translate = new Matrix4f().translation(0,1,0);
		for (int i = 0; i <= NSIDES; i++) {
			float angle = i * TAU / NSIDES; 
			rotate.rotationY(angle);

			Vector4f vb = p.mul(rotate, new Vector4f());  // vb = R(p)
			Vector4f vt = p.mul(rotate, new Vector4f()).mul(translate);  // vt = T(R(p))
			
			top[i] = vt; 			
			bottom[i] = vb; 			
		}

		// create the vertex buffer
		
		// Note: we are not using an index buffer, because each
		// triangle will require its own TBN matrix, so it's vertices
		// will need to be duplicated
		
		vertices = new Vector4f[6 * NSIDES];
		int k = 0;
		for (int i = 0; i < NSIDES; i++) {			
			
			// top
			vertices[k++] = top[i];
			vertices[k++] = bottom[i];
			vertices[k++] = top[i+1];

			// bottom
			vertices[k++] = bottom[i];
			vertices[k++] = bottom[i+1];
			vertices[k++] = top[i+1];
		}
			
		vertexBuffer = GLBuffers.createBuffer(vertices);
	}

	private void createUVBuffer() {
		uvs = new Vector2f[6 * NSIDES];
		
		int k = 0;
		for (int i = 0; i < NSIDES; i++) {
			float u0 = UMAX * i / NSIDES;
			float u1 = UMAX * (i+1) / NSIDES;

			uvs[k++] = new Vector2f(u0, VMAX);
			uvs[k++] = new Vector2f(u0, 0);
			uvs[k++] = new Vector2f(u1, VMAX);

			// bottom
			uvs[k++] = new Vector2f(u0, 0);
			uvs[k++] = new Vector2f(u1, 0);
			uvs[k++] = new Vector2f(u1, VMAX);
		}

		uvBuffer = GLBuffers.createBuffer(uvs);
	}
	
	private void loadTextures() {
		try {
			diffuseTexture = TextureLibrary.loadTexture(DIFFUSE_TEXTURE);
			normalMapTexture = TextureLibrary.loadTexture(NORMAL_MAP_TEXTURE);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	private void createTangentMatrixBuffer() {
		tangentMatrices = new Matrix3f[6 * NSIDES];
		
		Matrix3f matrix = new Matrix3f();
		int k = 0;
		for (int i = 0; i < NSIDES; i++) {
			tangentMatrices[k++] = matrix;
			tangentMatrices[k++] = matrix;
			tangentMatrices[k++] = matrix;

			tangentMatrices[k++] = matrix;
			tangentMatrices[k++] = matrix;
			tangentMatrices[k++] = matrix;
		}
		
		tangetMatrixBuffer = GLBuffers.createBuffer(tangentMatrices);

	}

	private static final float ROTATION_SPEED = TAU/4;
	
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
		shader.setAttribute("a_texcoord", uvBuffer);

		gl.glActiveTexture(GL.GL_TEXTURE0);
		gl.glBindTexture(GL.GL_TEXTURE_2D, diffuseTexture);
		shader.setUniform("u_diffuseTexture", 0);
		
		gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL4.GL_FILL);
        gl.glDrawArrays(GL4.GL_TRIANGLES, 0, vertices.length);           	
	}
	
	
}
