package comp3170.demos.week8.sceneobjects;

import java.awt.Color;

import org.joml.Vector3f;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import comp3170.SceneObject;
import comp3170.Shader;

public class Triangle extends SceneObject {

	private float[] vertices = {
		 0, 1, 0,
		 1, 0, 0,
	    -1, 0, 0,	    
	};

	private int vertexBuffer;

	// the barycentric coordinates of each vertex
	
	private float[] barycentric = {
			 1, 0, 0,
			 0, 1, 0,
		     0, 0, 1,	    
	};

	private int barycentricBuffer;

	
	private float[] colour = { 1.0f, 1.0f, 1.0f, 1.0f }; // white
	
	public Triangle(Shader shader, Color colour) {

		// read the RGBA values into this.colour
		colour.getComponents(this.colour);				
		this.vertexBuffer = shader.createBuffer(this.vertices);
		this.barycentricBuffer = shader.createBuffer(this.barycentric);
	}

	@Override
	protected void drawSelf(Shader shader) {
		GL4 gl = (GL4) GLContext.getCurrentGL();

		// draw the triangle
		
		shader.setAttribute("a_position", this.vertexBuffer);
		
		if (shader.hasUniform("u_colour")) {
			shader.setUniform("u_colour", this.colour);
		}

		if (shader.hasAttribute("a_barycentric")) {
			shader.setAttribute("a_barycentric", this.barycentricBuffer);
		}
		
		gl.glDrawArrays(GL.GL_TRIANGLES, 0, this.vertices.length / 3);

	}

}
