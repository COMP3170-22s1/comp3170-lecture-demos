package comp3170.demos.week7.livedemo;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import comp3170.GLBuffers;
import comp3170.Shader;
import comp3170.demos.SceneObject;

public class Quad extends SceneObject {

	private Vector4f[] vertices;
	private int vertexBuffer;
	private int[] indices;
	private int indexBuffer;
	private Vector3f[] colours;
	private int colourBuffer;
	
	private Vector2f screenSize;
	

	public Quad(float width, float height) {
		screenSize = new Vector2f(width, height);
		
		// 2--3
		// |\ |    y z
		// | \|    |/
		// 0--1    +--x  Drawing in NDC = left-handed

	
		vertices = new Vector4f[] {
			new Vector4f(-0.9f,-0.9f,0,1),
			new Vector4f( 0.9f,-0.9f,0,1),
			new Vector4f(-0.9f, 0.9f,0,1),
			new Vector4f( 0.9f, 0.9f,0,1),
		};
		
		vertexBuffer = GLBuffers.createBuffer(vertices);

		
		indices = new int[] {
			0, 1, 2,
			3, 2, 1,
		};
		
		indexBuffer = GLBuffers.createIndexBuffer(indices);
	}

	public void reshape(int width, int height) {
		screenSize.x = width;
		screenSize.y = height;
	}
	
	
	@Override
	protected void drawSelf(Shader shader, Matrix4f modelMatrix) {
		GL4 gl = (GL4) GLContext.getCurrentGL();

		shader.setAttribute("a_position", vertexBuffer);
		shader.setUniform("u_screenSize", screenSize);
		
		gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
		gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL4.GL_FILL);
		gl.glDrawElements(GL.GL_TRIANGLES, indices.length, GL.GL_UNSIGNED_INT, 0);		
	}
}
