package comp3170.demos.week3.demos;

import org.joml.Matrix4f;

import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import comp3170.GLBuffers;
import comp3170.Shader;

public class Axes {

	private float[] xAxis;
	private float[] yAxis;
	private int xVertexBuffer;
	private int yVertexBuffer;
	
	private float[] xColour = new float[] {1, 0, 0};	// RED
	private float[] yColour = new float[] {0, 1, 0}; // GREEN
	

	public Axes() {
		xAxis = new float[] {
			0, 0,
			0, 1,
		};

		yAxis = new float[] {
				0, 0,
				1, 0,
		};
		
		xVertexBuffer = GLBuffers.createBuffer(xAxis, GL4.GL_FLOAT_VEC2);
		yVertexBuffer = GLBuffers.createBuffer(yAxis, GL4.GL_FLOAT_VEC2);
	}
	
	public void draw(Shader shader) {
		GL4 gl = (GL4) GLContext.getCurrentGL();
		
		
		// X axis
		
	    shader.setAttribute("a_position", xVertexBuffer);
	    shader.setUniform("u_colour", xColour);	   	    
		gl.glDrawArrays(GL4.GL_LINES, 0, xAxis.length / 2);           	

		// Y axis
		
	    shader.setAttribute("a_position", yVertexBuffer);
	    shader.setUniform("u_colour", yColour);	   	    
		gl.glDrawArrays(GL4.GL_LINES, 0, yAxis.length / 2);           	

	}
}
