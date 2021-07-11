package comp3170.demos.week10.sceneobjects;

import java.awt.Color;

import org.joml.Vector4f;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import comp3170.GLBuffers;
import comp3170.demos.week10.cameras.Camera;
import comp3170.demos.week10.shaders.ShaderLibrary;

public class Triangle extends SceneObject {

	static final private String VERTEX_SHADER = "simpleVertex.glsl";
	static final private String FRAGMENT_SHADER = "simpleFragment.glsl";
	// static final private String FRAGMENT_SHADER = "depthFragment.glsl";

	private Vector4f[] vertices;
	private int vertexBuffer;

	public Triangle(Color colour) {
		super(ShaderLibrary.compileShader(VERTEX_SHADER, FRAGMENT_SHADER));

		this.vertices = new Vector4f[] {
			new Vector4f( 0, 1, 0, 1),
			new Vector4f( 1, 0, 0, 1),
			new Vector4f(-1, 0, 0, 1),
		};
		
		this.vertexBuffer = GLBuffers.createBuffer(vertices);
		
		float[] rgb = colour.getComponents(new float[4]);
		this.setColour(colour);
	}

	@Override
	public void draw(Camera camera) {
		GL4 gl = (GL4) GLContext.getCurrentGL();

		shader.enable();

		calcModelMatrix();
		shader.setUniform("u_modelMatrix", modelMatrix);
		shader.setUniform("u_viewMatrix", camera.getViewMatrix(viewMatrix));
		shader.setUniform("u_projectionMatrix", camera.getProjectionMatrix(projectionMatrix));		
		
		shader.setAttribute("a_position", this.vertexBuffer);
		
		if (shader.hasUniform("u_colour")) {
			shader.setUniform("u_colour", this.colour);
		}

		gl.glDrawArrays(GL.GL_TRIANGLES, 0, this.vertices.length);

	}

}
