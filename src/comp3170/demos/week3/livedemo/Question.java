package comp3170.demos.week3.livedemo;

import static com.jogamp.opengl.GL.GL_TRIANGLES;

import org.joml.Matrix4f;
import org.joml.Vector2f;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import comp3170.GLBuffers;
import comp3170.Shader;

public class Question {
	
	private float[] vertices;
	private int vertexBuffer;
	private int[] indices;
	private int indexBuffer;
	private Shader shader;
	
	private Vector2f position = new Vector2f();
	private Matrix4f modelMatrix = new Matrix4f();
	private float angle = 0; // radians
	private float scale = 1.0f;
	
	public Question(Shader shader) {
		this.shader = shader;
		
		vertices = new float [] {
			-0.1f, 0,
			 0.1f, 0,
			-0.1f, 0.2f,
			 0.1f, 0.2f,
			-0.1f, 0.25f,
			 0.1f, 0.25f,
			-0.1f, 0.45f,
			 0.1f, 0.45f,
			 0.3f, 0.8f,
			 0.5f, 0.8f,
			-0.3f, 0.8f,
			-0.3f, 1.0f,
			 0.3f, 1.0f,
		};

		vertexBuffer = GLBuffers.createBuffer(vertices, GL4.GL_FLOAT_VEC2);
		
		indices = new int[] {
			0,1,3,
			0,3,2,
			4,5,7,
			4,7,6,
			6,7,8,
			7,9,8,
			8,9,12,
			10,8,12,
			10,12,11,
		};
		
		indexBuffer = GLBuffers.createIndexBuffer(indices);
		
		position.x = 0;
		position.y = 0;
		
		modelMatrix.identity();
	}
	
	public void setPosition(float x, float y) {
		position.x = x;
		position.y = y;
	}
	
	public void setAngle(float radians) {
		angle = radians;
	}
	
	public void setScale(float scale) {
		this.scale = scale;
	}
	
	private void calcModelMatrix() {
		
		//  T = [ 1 0 0 x ]
		//      [ 0 1 0 y ]
		//      [ 0 0 1 0 ]
		//      [ 0 0 0 1 ]
			
		modelMatrix.identity(); // M = I
		modelMatrix.translate(position.x, position.y, 0);  // M = M * T
				
		//  R = [ cos  -sin  0  0 ]
		//      [ sin  cos   0  0 ]
		//      [ 0    0     1  0 ]
		//      [ 0    0     0  1 ]

		modelMatrix.rotateZ(angle);  // M = M * R;

		//  S = [ s  0  0  0 ]
		//      [ 0  s  0  0 ]
		//      [ 0  0  1  0 ]
		//      [ 0  0  0  1 ]
		
		modelMatrix.scale(scale, scale, 1);
		
	}
	
	private float[] colour = new float[] {0.5f, 0.5f, 1.0f};
	
	public void draw() {
		GL4 gl = (GL4) GLContext.getCurrentGL();

		shader.enable();

		calcModelMatrix();
		shader.setUniform("u_modelMatrix", modelMatrix);
		
		shader.setAttribute("a_position", vertexBuffer);		
		shader.setUniform("u_colour", colour);
		
	    // draw using an index buffer
	    gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
	    gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL4.GL_FILL);
	    gl.glDrawElements(GL_TRIANGLES, indices.length, GL.GL_UNSIGNED_INT, 0);		
	}

}
