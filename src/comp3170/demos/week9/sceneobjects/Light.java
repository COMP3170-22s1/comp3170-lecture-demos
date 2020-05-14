package comp3170.demos.week9.sceneobjects;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import comp3170.SceneObject;
import comp3170.Shader;

public class Light extends SceneObject {

	public float[] vertices = {
			// front
			1.0f,  1.0f, 1.0f,
            -1.0f, -1.0f, 1.0f,
            1.0f, -1.0f, 1.0f,

            -1.0f, -1.0f, 1.0f,
            1.0f,  1.0f, 1.0f,
            -1.0f,  1.0f, 1.0f,

            // back 
            -1.0f, -1.0f, -1.0f,
            1.0f,  1.0f, -1.0f,
            1.0f, -1.0f, -1.0f,

            1.0f,  1.0f, -1.0f,
            -1.0f, -1.0f, -1.0f,
            -1.0f,  1.0f, -1.0f,

            // left
            -1.0f, -1.0f, -1.0f, 
            -1.0f,  1.0f,  1.0f,
            -1.0f,  1.0f, -1.0f,

            -1.0f,  1.0f,  1.0f,
            -1.0f, -1.0f, -1.0f, 
            -1.0f,  -1.0f, 1.0f,

            // right
             1.0f,  1.0f,  1.0f,
             1.0f, -1.0f, -1.0f, 
             1.0f,  1.0f, -1.0f,

             1.0f, -1.0f, -1.0f, 
             1.0f,  1.0f,  1.0f,
             1.0f,  -1.0f, 1.0f,

            // top
            1.0f, 1.0f,  1.0f,
            -1.0f, 1.0f, -1.0f,
            -1.0f, 1.0f,  1.0f,

            -1.0f, 1.0f, -1.0f,
            1.0f, 1.0f,  1.0f,
            1.0f, 1.0f,  -1.0f,

            // bottom
            -1.0f, -1.0f, -1.0f,
            1.0f, -1.0f,  1.0f,
            -1.0f, -1.0f,  1.0f,

            1.0f, -1.0f,  1.0f,
            -1.0f, -1.0f, -1.0f,
            1.0f, -1.0f,  -1.0f,
	};

	private int vertexBuffer;

	private float[] colour = { 1.0f, 1.0f, 0.0f, 1.0f }; // yellow
	
	public Light(Shader shader) {
		super(shader);
	
		this.vertexBuffer = shader.createBuffer(this.vertices, GL4.GL_FLOAT_VEC3);
	}
	
	@Override
	protected void drawSelf(Shader shader) {
		GL4 gl = (GL4) GLContext.getCurrentGL();

		shader.setUniform("u_mvpMatrix", this.mvpMatrix);
		shader.setAttribute("a_position", this.vertexBuffer);
		shader.setUniform("u_colour", this.colour);
		
		gl.glDrawArrays(GL.GL_TRIANGLES, 0, this.vertices.length);

	}

}
