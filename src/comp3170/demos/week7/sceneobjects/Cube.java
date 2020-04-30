package comp3170.demos.week7.sceneobjects;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import comp3170.SceneObjectOld;
import comp3170.Shader;

public class Cube extends SceneObjectOld {

	// (-1,1,1)    (1,1,1)
	//       +-----+
	//       |    /|
	//       |  /  |
	//       |/    |
	//       +-----+
	// (-1,-1,1)   (1,-1,1)
	
	
	private float[] vertices = {
		// front
		-1, -1, 1,
		 1, 1, 1,
		-1, 1, 1,
		
		-1, -1, 1,
		1, -1, 1,
		1, 1, 1,

		// back
		-1, -1, -1,
		-1, 1, -1,
		 1, 1, -1,
		
		-1, -1, -1,
		1, 1, -1,
		1, -1, -1,

		// left,
		-1, 1, 1,
		-1, 1, -1,
		-1, -1, -1,
		
		-1, 1, 1,
		-1, -1, -1,
		-1, -1, 1,
		
		
			
	};
	
	private int vertexBuffer;

	private float[] colour = { 0.5f, 0.5f, 0.5f }; // grey

	public Cube(Shader shader) {
		this.vertexBuffer = shader.createBuffer(this.vertices);
	}

	@Override
	protected void drawSelf(Shader shader) {
		GL4 gl = (GL4) GLContext.getCurrentGL();

		shader.setAttribute("a_position", this.vertexBuffer);
		shader.setUniform("u_colour", this.colour);

		gl.glDrawArrays(GL.GL_TRIANGLES, 0, this.vertices.length / 3);

	}
	
}
