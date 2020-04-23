package comp3170.demos.week8.sceneobjects;

import java.awt.Color;

import org.joml.Vector3f;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import comp3170.SceneObject;
import comp3170.Shader;

public class Quad extends SceneObject {

	private float[] vertices = {
		 1, 1, 0,
		 1, -1, 0,
	    -1, -1, 0,	    

		 1, 1, 0,
		-1, 1, 0,
	    -1, -1, 0,	    

	};

	private int vertexBuffer;
	
	private float[] colour = { 1.0f, 1.0f, 0.0f}; // white
	
	public Quad(Shader shader, Color colour) {
		
		// read the RGB values into this.colour
		colour.getRGBColorComponents(this.colour);
		
		// Calculate the face normal

		Vector3f m = new Vector3f();	// midpoint

		Vector3f[] v = new Vector3f[3];
		for (int i = 0; i < 3; i++) {
			v[i] = new Vector3f(
					vertices[i * 3],
					vertices[i * 3 + 1],
					vertices[i * 3 + 2]);
			
			m.add(v[i]);
		}
		
		m.mul(1.0f/3);

		Vector3f n = new Vector3f();	// normal
		Vector3f a = new Vector3f();	// normal
		Vector3f b = new Vector3f();	// normal
		
		this.vertexBuffer = shader.createBuffer(this.vertices);
	}

	@Override
	protected void drawSelf(Shader shader) {
		GL4 gl = (GL4) GLContext.getCurrentGL();

		// draw the triangle
		
		shader.setAttribute("a_position", this.vertexBuffer);
		shader.setUniform("u_colour", this.colour);

		gl.glDrawArrays(GL.GL_TRIANGLES, 0, this.vertices.length / 3);

	}

}
