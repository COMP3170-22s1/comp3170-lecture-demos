package comp3170.demos.week12.sceneobjects;

import java.awt.event.KeyEvent;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import comp3170.InputManager;
import comp3170.Shader;
import comp3170.demos.week12.cameras.Camera;

public class Cube extends SceneObject {

	private static final float TAU = (float) (Math.PI * 2);;

	private Vector4f[] vertices;
	private int vertexBuffer;
	private int[] indices;
	private int indexBuffer;
	private int texture;
	private Vector2f[] uvs;
	private int uvBuffer;
	

	public Cube(Shader shader, int texture) {
		super(shader);

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
		
		this.vertexBuffer = shader.createBuffer(vertices);
		
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
		
		this.uvBuffer = shader.createBuffer(uvs);
		
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

		this.indexBuffer = shader.createIndexBuffer(indices);
		
	}


	private static final float ROTATION_SPEED = TAU/4;
	private Vector3f angle = new Vector3f();
	
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

	private static final float[] RED = new float[] {1.0f, 0.0f, 0.0f, 1.0f};
	
	@Override
	public void draw(Camera camera) {
		GL4 gl = (GL4) GLContext.getCurrentGL();
		shader.enable();
		
		calcModelMatrix();
		shader.setUniform("u_modelMatrix", modelMatrix);
		shader.setUniform("u_viewMatrix", camera.getViewMatrix(viewMatrix));
		shader.setUniform("u_projectionMatrix", camera.getProjectionMatrix(projectionMatrix));		
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
