package comp3170.demos.week3.demos;

import static com.jogamp.opengl.GL.GL_TRIANGLES;

import java.awt.Color;

import org.joml.Matrix3f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import comp3170.GLBuffers;
import comp3170.Shader;

public class Square {
	private float[] vertices;
	private int vertexBuffer;
	
	private float[] colour = new float[] {1f, 1f, 1f};	// default is white

	
	public Square() {
		
		vertices = new float[] {
			-1f, -1f,
			 1f, -1f,
			 1f,  1f,
			 -1f,  1f,
		};
		
		// copy the data into a Vertex Buffer Object in graphics memory		
	    vertexBuffer = GLBuffers.createBuffer(vertices, GL4.GL_FLOAT_VEC2);
	    
	}
	

	public void draw(Shader shader) {
		GL4 gl = (GL4) GLContext.getCurrentGL();
		
		// connect the vertex buffer to the a_position attribute		   
	    shader.setAttribute("a_position", vertexBuffer);
	    shader.setUniform("u_colour", colour);	    
		gl.glDrawArrays(GL4.GL_LINE_LOOP, 0, vertices.length / 2);           		    
	}
	
}
