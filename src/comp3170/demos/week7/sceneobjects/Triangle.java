package comp3170.demos.week7.sceneobjects;

import java.awt.Color;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import comp3170.GLBuffers;
import comp3170.Shader;
import comp3170.demos.SceneObject;
import comp3170.demos.week7.shaders.ShaderLibrary;

public class Triangle extends SceneObject {

	static final private String VERTEX_SHADER = "simpleVertex.glsl";
	static final private String FRAGMENT_SHADER = "simpleFragment.glsl";
	// static final private String FRAGMENT_SHADER = "depthFragment.glsl";

	private Shader shader;
	private Vector4f[] vertices;
	private int vertexBuffer;
	private Vector3f colour = new Vector3f();

	public Triangle(Color colour) {
		shader = ShaderLibrary.compileShader(VERTEX_SHADER, FRAGMENT_SHADER);

		vertices = new Vector4f[] {
			new Vector4f( 0, 1, 0, 1),
			new Vector4f( 1, 0, 0, 1),
			new Vector4f(-1, 0, 0, 1),
		};
		
		vertexBuffer = GLBuffers.createBuffer(vertices);
		
		float[] rgb = colour.getComponents(new float[4]);
		this.colour.set(rgb[0], rgb[1], rgb[2]);
	}

	@Override
	public void drawSelf(Matrix4f mvpMatrix) {
		GL4 gl = (GL4) GLContext.getCurrentGL();

		shader.enable();
		shader.setUniform("u_mvpMatrix", mvpMatrix);
		shader.setAttribute("a_position", vertexBuffer);
		
		if (shader.hasUniform("u_colour")) {
			shader.setUniform("u_colour", colour);
		}

		gl.glDrawArrays(GL.GL_TRIANGLES, 0, vertices.length);

	}

}
