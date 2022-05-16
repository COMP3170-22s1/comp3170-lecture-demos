package comp3170.demos.week12.sceneobjects;

import java.awt.event.KeyEvent;

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

public class Cube extends SceneObject {

	private static final float TAU = (float) (Math.PI * 2);;

	private Shader shader;
	private Vector4f[] vertices;
	private int vertexBuffer;
	private int[] indices;
	private int indexBuffer;
	private int texture;
	private Vector2f[] uvs;
	private int uvBuffer;
	

	public Cube(Shader shader, int texture) {
		this.shader = shader;

		this.texture = texture;
		
		this.vertices = new Vector4f[] {
			new Vector4f(-1, -1, -1, 1),
			new Vector4f(-1,  1, -1, 1),
			
			new Vector4f(-1, -1,  1, 1),
			new Vector4f(-1,  1,  1, 1),

			new Vector4f( 1, -1,  1, 1),
			new Vector4f( 1,  1,  1, 1),

			new Vector4f( 1, -1, -1, 1),
			new Vector4f( 1,  1, -1, 1),
			
			new Vector4f(-1, -1, -1, 1),	// wrap around for uvs
			new Vector4f(-1,  1, -1, 1),
		};
		
		this.vertexBuffer = GLBuffers.createBuffer(vertices);
		
		// v
		// 1 1--3--5--7--9
		//   |\ |\ |\ |\ |
		//   | \| \| \| \|
		// 0 0--2--4--6--8
		//   0  1  2  3  4 u
		
		
		this.uvs = new Vector2f[] {
			new Vector2f(0,0),
			new Vector2f(0,1),
			new Vector2f(1,0),
			new Vector2f(1,1),
			new Vector2f(2,0),
			new Vector2f(2,1),
			new Vector2f(3,0),
			new Vector2f(3,1),
			new Vector2f(4,0),
			new Vector2f(4,1),
		};
		
		this.uvBuffer = GLBuffers.createBuffer(uvs);
		
		this.indices = new int[] {
			0,2,1,
			3,1,2,
			2,4,3,
			5,3,4,
			4,6,5,
			7,5,6,
			6,8,7,
			9,7,8,
		};

		this.indexBuffer = GLBuffers.createIndexBuffer(indices);
		
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

	private static final float[] RED = new float[] {1.0f, 0.0f, 0.0f, 1.0f};
	
	@Override
	public void drawSelf(Matrix4f mvpMatrix) {
		GL4 gl = (GL4) GLContext.getCurrentGL();
		shader.enable();
		
		shader.setUniform("u_mvpMatrix", mvpMatrix);
		shader.setAttribute("a_position", vertexBuffer);
		
		if (shader.hasUniform("u_texture")) {
			gl.glActiveTexture(GL.GL_TEXTURE0);
			gl.glBindTexture(GL.GL_TEXTURE_2D, this.texture);
			shader.setUniform("u_texture", 0);
			shader.setAttribute("a_texcoord", this.uvBuffer);					
		}
		
		if (shader.hasUniform("u_colour")) {
			shader.setUniform("u_colour", RED);
		}
		
		gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
		gl.glDrawElements(GL.GL_TRIANGLES, indices.length, GL.GL_UNSIGNED_INT, 0);		

	}
	
	
}
