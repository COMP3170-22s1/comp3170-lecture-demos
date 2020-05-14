package comp3170.demos.week9.sceneobjects;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import comp3170.SceneObject;
import comp3170.Shader;

public class Axes extends SceneObject {

	private float[] vertices = {
			0,0,0,	// x
			1,0,0,

			0,0,0,	// y
			0,1,0,

			0,0,0,	// z
			0,0,1,
	};
	private int vertexBuffer;

	private float[] colours = {
			1,0,0,	// x = red
			1,0,0,

			0,1,0,	// y = green
			0,1,0,

			0,0,1,	// z = blue
			0,0,1,

	}; 
	private int colourBuffer;

	public Axes(Shader shader) {
		super(shader);
		
		this.vertexBuffer = shader.createBuffer(this.vertices, GL4.GL_FLOAT_VEC3);
		this.colourBuffer = shader.createBuffer(this.colours, GL4.GL_FLOAT_VEC3);
	}

	@Override
	protected void drawSelf(Shader shader) {
		GL4 gl = (GL4) GLContext.getCurrentGL();

		shader.setUniform("u_mvpMatrix", this.mvpMatrix);		
		shader.setAttribute("a_position", this.vertexBuffer);
		shader.setAttribute("a_colour", this.colourBuffer);

		gl.glDrawArrays(GL.GL_LINES, 0, this.vertices.length / 3);

	}

}
