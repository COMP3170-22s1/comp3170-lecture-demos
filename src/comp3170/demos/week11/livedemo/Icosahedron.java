package comp3170.demos.week11.livedemo;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.io.IOException;

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
import comp3170.demos.week11.shaders.ShaderLibrary;
import comp3170.demos.week11.textures.TextureLibrary;

public class Icosahedron extends SceneObject {

	private static final float TAU = (float) (Math.PI * 2);
	final private String VERTEX_SHADER = "textureVertex.glsl";
	final private String FRAGMENT_SHADER = "textureFragment.glsl";
	final private String TEXTURE = "brick-diffuse.png";
	
	private Shader shader;
	private Vector4f[] vertices;
	private int vertexBuffer;
	private Vector3f[] colours;
	private int colourBuffer;
	private Vector3f[] edgeColours;
	private int edgeColourBuffer;
	private int[] indices;
	private int indexBuffer;
	private Vector2f[] uvs;
	private int uvBuffer;
	private int texture;

	public Icosahedron() {
		shader = ShaderLibrary.compileShader(VERTEX_SHADER, FRAGMENT_SHADER);
		
		createVertexBuffer();
		createIndexBuffer();
		createUVBuffer();

		try {
			texture = TextureLibrary.loadTexture(TEXTURE);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

	}

	/**
	 * Reference: https://en.wikipedia.org/wiki/Regular_icosahedron
	 */
	
	private void createVertexBuffer() {
		vertices = new Vector4f[14];
		
		vertices[0] = new Vector4f(0,1,0,1);
		vertices[13] = new Vector4f(0,-1,0,1);
		
		Matrix4f rotate = new Matrix4f();
		
		for (int i = 1; i < 13; i++) {
			vertices[i] = new Vector4f(1,0,0,1);
			
			float angleY = (i-1) * TAU / 10;
			float angleZ = (float) Math.atan(1./2.);
						
			rotate.rotationY(angleY);		// M = Ry(angleY)
						
			if (i % 2 == 1) {
				// odd points rotate up
				rotate.rotateZ(angleZ);		// M = M * Rz(angleZ)
			}
			else {
				// even points rotate down
				rotate.rotateZ(-angleZ);	// M = M * Rz(-angleZ)
			}
			
			vertices[i].mul(rotate);
		}

		vertexBuffer = GLBuffers.createBuffer(vertices);
	}
	
	private void createColourBuffer() {
		colours = new Vector3f[12];
		edgeColours = new Vector3f[12];

		// poles are white
		
		colours[0] = new Vector3f(1,1,1);
		colours[11] = new Vector3f(1,1,1);
		edgeColours[0] = new Vector3f(0,0,0);
		edgeColours[11] = new Vector3f(0,0,0);
				
		float h = 0;
		float s = 1;
		float b = 1;
		
		float[] rgb = new float[3];
		
		// vary the hue from 0 to 1 around the icosahedron
		
		for (int i = 1; i < 11; i++) {
			h = (i-1)/10f;
			
			Color c = Color.getHSBColor(h,s,b);
			c.getRGBColorComponents(rgb);
			colours[i] = new Vector3f(rgb[0], rgb[1], rgb[2]);
			edgeColours[i] = new Vector3f(0,0,0);
		}
		
		
		colourBuffer = GLBuffers.createBuffer(colours);
		edgeColourBuffer = GLBuffers.createBuffer(edgeColours);
	}
	
	private void createUVBuffer() {
		uvs = new Vector2f[14];

		// poles are white
		
		uvs[0] = new Vector2f(1,1);
		uvs[13] = new Vector2f(0,0);
				
		// vary the u value from 0 to 1 around the icosahedron
		
		for (int i = 1; i < 13; i++) {
			float u = (i-1) / 10f;
			float v = (i % 2 == 0 ? 1 : 0);
			uvs[i] = new Vector2f(u,v);
		}		
		
		uvBuffer = GLBuffers.createBuffer(uvs);

	}
	
	private void createIndexBuffer() {
		
		indices = new int[] {
			// top
			0, 1, 3,
			0, 3, 5,
			0, 5, 7,
			0, 7, 9, 
			0, 9, 11,
			
			// sides
			1, 3, 2, 
			2, 3, 4,
			3, 5, 4,
			4, 5, 6,
			5, 7, 6,
			6, 7, 8,			
			7, 8, 9, 
			8, 9, 10,			
			9, 11, 10,			
			10, 11, 12,			
			
			// bottom
			
			13, 2, 4,
			13, 4, 6, 
			13, 6, 8,
			13, 8, 10,
			13, 10, 12,
		};
		
		indexBuffer = GLBuffers.createIndexBuffer(indices);
	}
	
	@Override
	protected void drawSelf(Matrix4f mvpMatrix) {
		GL4 gl = (GL4) GLContext.getCurrentGL();

		shader.enable();
		shader.setUniform("u_mvpMatrix", mvpMatrix);
		shader.setAttribute("a_position", vertexBuffer);
		shader.setAttribute("a_texcoord", uvBuffer);

		gl.glActiveTexture(GL.GL_TEXTURE0);
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture);
		shader.setUniform("u_texture", 0);
		
		gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
		gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL4.GL_FILL);
		gl.glDrawElements(GL.GL_TRIANGLES, indices.length, GL.GL_UNSIGNED_INT, 0);

	}

	private final static float ROTATION_SPEED = TAU / 10;
	
	public void update(float dt, InputManager input) {
		
		Matrix4f matrix = getMatrix();
		
		// Apply rotations on the left, to rotate around world axes instead of model axes
		
		if (input.isKeyDown(KeyEvent.VK_W)) {
			matrix.rotateLocalX(ROTATION_SPEED * dt);	// M = Rx(angle) * M 
		}
		if (input.isKeyDown(KeyEvent.VK_S)) {
			matrix.rotateLocalX(-ROTATION_SPEED * dt);
		}
		if (input.isKeyDown(KeyEvent.VK_A)) {
			matrix.rotateLocalY(ROTATION_SPEED * dt);	// M = Ry(angle) * M
		}
		if (input.isKeyDown(KeyEvent.VK_D)) {
			matrix.rotateLocalY(-ROTATION_SPEED * dt);
		}

	}	
	
}
