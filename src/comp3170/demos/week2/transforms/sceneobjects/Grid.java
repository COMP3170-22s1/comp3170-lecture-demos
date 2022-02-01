package comp3170.demos.week2.transforms.sceneobjects;

import java.awt.Color;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import comp3170.GLBuffers;
import comp3170.Shader;

public class Grid extends SceneObject {
	private static final float TAU = (float) (Math.PI * 2);

	private float[] vertices;
	private int vertexBuffer;
	
	private Vector3f colour;
	

	public Grid(int nLines, Color colour) {
		
		// Grid on horizontal and vertical lines from (-1,-1) to (1,1)
		
		vertices = new float[(nLines+1) * 2 * 2 * 2]; // 2 lines * 2 endpoints per line, 2 coordinates per vertex 
		
		int j = 0;
		for (int i = 0; i <= nLines; i++) {
			float v = i * 2.0f / nLines - 1;
			
			// horizontal line from (-1, v) to (1, v)
			vertices[j++] = -1;
			vertices[j++] = v;
			vertices[j++] = 1;
			vertices[j++] = v;
			
			// vertical line from (v, -1) to (v, 1)
			vertices[j++] = v;
			vertices[j++] = -1;
			vertices[j++] = v;
			vertices[j++] = 1;			
		}
		
		// copy the data into a Vertex Buffer Object in graphics memory		
	    vertexBuffer = GLBuffers.createBuffer(vertices, GL4.GL_FLOAT_VEC2);
	    
	    float red = colour.getRed() / 255f;
	    float green = colour.getGreen() / 255f; 
	    float blue = colour.getBlue() / 255f;
	    this.colour = new Vector3f(red, green, blue);
	}
	
	protected void drawSelf(Shader shader, Matrix4f modelMatrix) {
		GL4 gl = (GL4) GLContext.getCurrentGL();
		
		// set the model matrix
		
		shader.setUniform("u_modelMatrix", modelMatrix);
		
        // connect the vertex buffer to the a_position attribute		   
	    shader.setAttribute("a_position", vertexBuffer);

	    // write the colour value into the u_colour uniform 
	    shader.setUniform("u_colour", colour);	    
	    
        gl.glDrawArrays(GL.GL_LINES, 0, vertices.length / 2);           	
	}

	
}
