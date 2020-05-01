package comp3170.demos.week9.sceneobjects;

import org.joml.Vector3f;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import comp3170.SceneObject;
import comp3170.Shader;

public class Triangle extends SceneObject {

	private Vector3f[] vertices;
	private int vertexBuffer;

	private float[] colour = { 1.0f, 1.0f, 0.0f, 1.0f}; // yellow

	
	public Triangle(Shader shader) {
		super(shader);
	
        this.vertices = new Vector3f[3];
        this.vertices[0] = new Vector3f(0,1,0);
        this.vertices[1] = new Vector3f(-1,0,0);
        this.vertices[2] = new Vector3f(1,0,0);
        
		this.vertexBuffer = shader.createBuffer(this.vertices);
		
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
