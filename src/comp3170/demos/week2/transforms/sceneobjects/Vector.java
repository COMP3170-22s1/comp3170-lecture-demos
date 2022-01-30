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

public class Vector extends SceneObject {

	private final static float WIDTH = 0.05f;
	private float[] vertices;
	private int vertexBuffer;
	
	private Vector3f colour;
	
	public Vector(Color colour) {
		
		// TODO: Replace this with a solid mesh to draw thicker lines
		// since glLineWidth is deprecated
		
		// Drawn as an arrow:
		//                   (1-w,w)
		//                      +
		//                      |\
		//  (0,w/2) +-----------+ \
		//          |           |  \
		//    (0,0) *           |   + (1.0)
		//          |           |  /
		// (0,-w/2) +-----------+ /
		//                      |/
        //                      +
		//                   (1-w,-w)
		
		vertices = new float[] {
			0, WIDTH/2,
			0, -WIDTH/2,
			1-WIDTH, -WIDTH/2, 

			0, WIDTH/2,
			1-WIDTH, -WIDTH/2, 
			1-WIDTH,  WIDTH/2, 

			1,0,
			1-WIDTH,  WIDTH, 
			1-WIDTH, -WIDTH, 
			
		};		

		
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
