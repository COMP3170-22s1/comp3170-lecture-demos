package comp3170.demos.week7.sceneobjects;

import org.joml.Vector4f;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import comp3170.demos.week7.depth.DepthDemo;

public class Triangle extends SceneObject {

	static final private String VERTEX_SHADER = "simpleVertex.glsl";
	static final private String FRAGMENT_SHADER = "simpleFragment.glsl";
	private Vector4f[] vertices;
	private int vertexBuffer;

	public Triangle() {
		super(DepthDemo.compileShader(VERTEX_SHADER, FRAGMENT_SHADER));

		this.vertices = new Vector4f[] {
			new Vector4f( 0, 1, 0, 1),
			new Vector4f( 1, 0, 0, 1),
			new Vector4f(-1, 0, 0, 1),
		};
		
		this.vertexBuffer = shader.createBuffer(vertices);
		
	}

	@Override
	public void draw() {
		GL4 gl = (GL4) GLContext.getCurrentGL();

		// draw the triangle
		
		shader.setAttribute("a_position", this.vertexBuffer);
		
		if (shader.hasUniform("u_colour")) {
			shader.setUniform("u_colour", this.colour);
		}

		gl.glDrawArrays(GL.GL_TRIANGLES, 0, this.vertices.length);

	}

}
