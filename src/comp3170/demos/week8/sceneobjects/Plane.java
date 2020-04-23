package comp3170.demos.week8.sceneobjects;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import comp3170.SceneObject;
import comp3170.Shader;

public class Plane extends SceneObject {

	private float[] vertices;
	private int vertexBuffer;

	private float[] colour = { 0.5f, 0.5f, 0.5f }; // grey

	public Plane(Shader shader, int nLines) {
		
		// add grid lines
		
		// (size + 1) lines
		// 2 orientations (horizontal and vertical)
		// 2 vertices per line
		// 3 coordinates per vertex
		
		vertices = new float[(nLines + 1) * 2 * 2 * 3];
		
		// create a 2x2 square with the origin (0,0) at the centre
		// and draw grid lines on it
		
		int j = 0;
		
		for (int i = 0; i <= nLines; i++) {
			
			float p = (float) 2 * i / nLines - 1; 
			
			// line parallel to the x axis
			vertices[j++] = -1;		// x
			vertices[j++] = 0;		// y
			vertices[j++] = p;		// z
			
			vertices[j++] = 1;		// x
			vertices[j++] = 0;		// x
			vertices[j++] = p;		// z

			// line parallel to the z axis
			vertices[j++] = p;		// x
			vertices[j++] = 0;		// y
			vertices[j++] = -1;		// z
			
			vertices[j++] = p;		// x
			vertices[j++] = 0;		// y
			vertices[j++] = 1;		// z
		}		
		
		this.vertexBuffer = shader.createBuffer(this.vertices);
	}

	@Override
	protected void drawSelf(Shader shader) {
		GL4 gl = (GL4) GLContext.getCurrentGL();

		shader.setAttribute("a_position", this.vertexBuffer);
		if (shader.hasUniform("u_colour")) {
			shader.setUniform("u_colour", this.colour);
		}

		// draw as lines
		// note that the number of vertices is this.vertices.length / 3
		// as there are three floats per vertex
		gl.glDrawArrays(GL.GL_LINES, 0, this.vertices.length / 3);

	}

}
