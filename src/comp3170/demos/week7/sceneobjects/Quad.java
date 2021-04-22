package comp3170.demos.week7.sceneobjects;

import java.awt.Color;

import org.joml.Vector4f;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import comp3170.demos.week7.cameras.Camera;
import comp3170.demos.week7.shaders.ShaderLibrary;

public class Quad extends SceneObject {

	static final private String VERTEX_SHADER = "simpleVertex.glsl";
	static final private String FRAGMENT_SHADER = "simpleFragment.glsl";
	private Vector4f[] vertices;
	private int vertexBuffer;
	private int[] indices;
	private int indexBuffer;

	public Quad(Color colour) {
		super(ShaderLibrary.compileShader(VERTEX_SHADER, FRAGMENT_SHADER));

		this.vertices = new Vector4f[] {
			new Vector4f( 1,  1, 0, 1),
			new Vector4f( 1, -1, 0, 1),
			new Vector4f(-1,  1, 0, 1),
			new Vector4f(-1, -1, 0, 1),
		};
		
		this.vertexBuffer = shader.createBuffer(vertices);

		this.indices = new int[] {
			0, 1, 2,
			3, 2, 1,
		};
		this.indexBuffer = shader.createIndexBuffer(indices);
		
		float[] rgb = colour.getComponents(new float[4]);
		this.colour.set(rgb[0], rgb[1], rgb[2]);
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
		shader.setUniform("u_colour", this.colour);

		gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
		gl.glDrawElements(GL.GL_TRIANGLES, indices.length, GL.GL_UNSIGNED_INT, 0);		
	}

}
