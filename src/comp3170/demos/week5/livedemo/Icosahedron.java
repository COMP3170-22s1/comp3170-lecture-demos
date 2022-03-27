package comp3170.demos.week5.livedemo;

import java.awt.Color;
import java.awt.event.KeyEvent;

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

public class Icosahedron extends SceneObject {

	private static final float TAU = (float) (Math.PI * 2);
	
	private Vector4f[] vertices;
	private int vertexBuffer;
	private Vector3f[] colours;
	private int colourBuffer;
	private int[] indices;
	private int indexBuffer;



	public Icosahedron() {
		createVertexBuffer();
		createColourBuffer();
		createIndexBuffer();
	}

	/**
	 * Reference: https://en.wikipedia.org/wiki/Regular_icosahedron
	 */
	
	private void createVertexBuffer() {
		vertices = new Vector4f[12];
		
		vertices[0] = new Vector4f(0,1,0,1);
		vertices[11] = new Vector4f(0,-1,0,1);
		
		Matrix4f rotate = new Matrix4f();
		
		for (int i = 1; i < 11; i++) {
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

		// poles are white
		
		colours[0] = new Vector3f(1,1,1);
		colours[11] = new Vector3f(1,1,1);
				
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
		}
		
		
		colourBuffer = GLBuffers.createBuffer(colours);
	}
	
	private void createIndexBuffer() {
		indices = new int[] {
			// top
			0, 1, 3,
			0, 3, 5,
			0, 5, 7,
			0, 7, 9, 
			0, 9, 1,
			
			// sides
			1, 3, 2, 
			2, 3, 4,
			3, 5, 4,
			4, 5, 6,
			5, 7, 6,
			6, 7, 8,			
			7, 8, 9, 
			8, 9, 10,			
			9, 1, 10,			
			10, 1, 2,			
			
			// bottom
			
			11, 2, 4,
			11, 4, 6, 
			11, 6, 8,
			11, 8, 10,
			11, 10, 2,
		};
		
		indexBuffer = GLBuffers.createIndexBuffer(indices);
	}
	
	private float[] colour = new float[] {1,1,1};
	
	@Override
	protected void drawSelf(Shader shader, Matrix4f modelMatrix) {
		GL4 gl = (GL4) GLContext.getCurrentGL();

		shader.setAttribute("a_position", vertexBuffer);
		shader.setAttribute("a_colour", colourBuffer);
		shader.setUniform("u_modelMatrix", modelMatrix);
//		shader.setUniform("u_colour", colour);
		
		gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
		gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL4.GL_LINE);
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
