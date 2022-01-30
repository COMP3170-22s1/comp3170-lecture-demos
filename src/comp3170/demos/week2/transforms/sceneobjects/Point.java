package comp3170.demos.week2.transforms.sceneobjects;

import static com.jogamp.opengl.GL.GL_TRIANGLES;

import java.awt.Color;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import comp3170.GLBuffers;
import comp3170.Shader;

public class Point extends SceneObject {
	private static final float TAU = (float) (Math.PI * 2);

	private float[] vertices;
	private int vertexBuffer;
	
	private Vector3f colour;
	
	private final static int NSIDES = 20;
	private final static float RADIUS = 0.05f;
	
	public Point(Color colour) {
		
		// Polygon with NSIDES to simulate a circle
		// made up of NSIDES triangles
		
		int nVertices = 3 * NSIDES;
		
		vertices = new float[nVertices * 2]; // 2 coordinates per vertex 
		
		int j = 0;
		for (int i = 0; i < NSIDES; i++) {
			float a0 = TAU * i / NSIDES;
			float a1 = TAU * (i+1) / NSIDES;			
			
			// center point (0,0)
			vertices[j++] = 0;
			vertices[j++] = 0;
			
			// point at angle a0 = (cos(a0), sin(a0))
			vertices[j++] = RADIUS * (float) Math.cos(a0);
			vertices[j++] = RADIUS * (float) Math.sin(a0);
			
			// point at angle a1 = (cos(a1), sin(a1))
			vertices[j++] = RADIUS * (float) Math.cos(a1);
			vertices[j++] = RADIUS * (float) Math.sin(a1);
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
	    
        gl.glDrawArrays(GL.GL_TRIANGLES, 0, vertices.length / 2);           	
	}

	
}
