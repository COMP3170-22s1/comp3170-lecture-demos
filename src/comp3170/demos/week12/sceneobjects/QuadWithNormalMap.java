package comp3170.demos.week12.sceneobjects;

import java.awt.event.KeyEvent;
import java.io.IOException;

import org.joml.Matrix2f;
import org.joml.Matrix3f;
import org.joml.Matrix3x2f;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import comp3170.GLBuffers;
import comp3170.InputManager;
import comp3170.Shader;
import comp3170.demos.SceneObject;
import comp3170.demos.week12.shaders.ShaderLibrary;
import comp3170.demos.week12.textures.TextureLibrary;

public class QuadWithNormalMap extends SceneObject {

	private static final String VERTEX_SHADER = "normalMapVertex.glsl";
	private static final String FRAGMENT_SHADER = "normalMapFragment.glsl";
	private static final String DIFFUSE_TEXTURE = "brick-diffuse.png";
	private static final String NORMAL_MAP_TEXTURE = "brick-normals.png";
	
	private static final float TAU = (float) (Math.PI * 2);;
	
	private Shader shader;
	private Vector4f[] vertices;
	private int vertexBuffer;
	private Vector2f[] uvs;
	private int uvBuffer;
	private Matrix3f[] tangentMatrices;
	private int tangetMatrixBuffer;
	private int diffuseTexture;
	private int normalMapTexture;	
	
	public QuadWithNormalMap() {
		shader = ShaderLibrary.compileShader(VERTEX_SHADER, FRAGMENT_SHADER);
		createVertexBuffer();
		createUVBuffer();
		loadTextures();
		createTangentMatrixBuffer();
		
	}

	private void createVertexBuffer() {
		vertices = new Vector4f[] {
			new Vector4f(-1, 1,0,1),
			new Vector4f(-1,-1,0,1),
			new Vector4f( 1, 1,0,1),

			new Vector4f( 1,-1,0,1),
			new Vector4f( 1, 1,0,1),
			new Vector4f(-1,-1,0,1),
		};
			
		vertexBuffer = GLBuffers.createBuffer(vertices);
	}

	private void createUVBuffer() {
		uvs = new Vector2f[] {
			new Vector2f(0,1),
			new Vector2f(0,0),
			new Vector2f(1,1),
			
			new Vector2f(1,0),
			new Vector2f(1,1),
			new Vector2f(0,0),
		};
		
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
		tangentMatrices = new Matrix3f[6];

		int k = 0;
		Matrix3f matrix;
		matrix = calculateTangentMatrix(0);
		tangentMatrices[k++] = matrix;
		tangentMatrices[k++] = matrix;
		tangentMatrices[k++] = matrix;

		matrix = calculateTangentMatrix(1);
		tangentMatrices[k++] = matrix;
		tangentMatrices[k++] = matrix;
		tangentMatrices[k++] = matrix;
		
		tangetMatrixBuffer = GLBuffers.createBuffer(tangentMatrices);

	}

	Vector3f one = new Vector3f(0,0,1);
	Vector3f dP10 = new Vector3f();
	Vector3f dP20 = new Vector3f();
	Vector3f dUV10 = new Vector3f();
	Vector3f dUV20 = new Vector3f();

	Matrix3f edgeMatrix = new Matrix3f();
	Matrix3f uvMatrix = new Matrix3f();

	private Matrix3f calculateTangentMatrix(int triangle) {
		
		Vector4f p0, p1, p2;
		Vector2f uv0, uv1, uv2;
		
		p0 = vertices[triangle * 3];
		p1 = vertices[triangle * 3 + 1];
		p2 = vertices[triangle * 3 + 2];

		uv0 = uvs[triangle * 3];
		uv1 = uvs[triangle * 3 + 1];
		uv2 = uvs[triangle * 3 + 2];

		// the JOML class Matrix3x2f doesn't suit our needs, so pad everything to 3x3
		dP10.set(p1.x - p0.x, p1.y - p0.y, p1.z - p0.z);
		dP20.set(p2.x - p0.x, p2.y - p0.y, p2.z - p0.z);
		edgeMatrix.identity();
		edgeMatrix.setColumn(0, dP10);
		edgeMatrix.setColumn(1, dP20);

		dUV10.set(uv1.x - uv0.x, uv1.y - uv0.y, 0);
		dUV20.set(uv2.x - uv0.x, uv2.y - uv0.y, 0);
		uvMatrix.identity();
		uvMatrix.setColumn(0, dUV10);
		uvMatrix.setColumn(1, dUV10);
		uvMatrix.invert();		

		Matrix3f tangentMatrix = edgeMatrix.mul(uvMatrix, new Matrix3f());		
		return tangentMatrix;		
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
