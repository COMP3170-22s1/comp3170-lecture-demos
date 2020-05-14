package comp3170.demos.week9.sceneobjects;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import comp3170.SceneObject;
import comp3170.SceneObjectOld;
import comp3170.Shader;

public class Plane extends SceneObject {

	private float[] vertices;
	private int vertexBuffer;

	private float[] colour = { 0.5f, 0.5f, 0.5f, 1.0f}; // grey

	public Plane(Shader shader, int nLines) {
		super(shader);
		
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
		
		this.vertexBuffer = shader.createBuffer(this.vertices, GL4.GL_FLOAT_VEC3);
	}

	@Override
	protected void drawSelf(Shader shader) {
		GL4 gl = (GL4) GLContext.getCurrentGL();

		shader.setUniform("u_mvpMatrix", this.mvpMatrix);		
		shader.setAttribute("a_position", this.vertexBuffer);
		shader.setUniform("u_colour", this.colour);

		gl.glDrawArrays(GL.GL_LINES, 0, this.vertices.length / 3);

	}

}
