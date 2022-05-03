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
import comp3170.demos.week7.cameras.Camera;
import comp3170.demos.week7.shaders.ShaderLibrary;

public class Cube extends SceneObject {
	private Shader shader;
	private Vector4f[] vertices;
	private int vertexBuffer;
	private int[] indices;
	private int indexBuffer;

	private Camera camera;
	private Matrix4f cameraModelMatrix = new Matrix4f();
	private Vector4f cameraPosition = new Vector4f();
	private Vector3f colour;
	
	final private static String VERTEX_SHADER = "fogVertex.glsl";
	final private static String FRAGMENT_SHADER = "fogFragment.glsl";

	public Cube(Camera camera) {
		shader = ShaderLibrary.compileShader(VERTEX_SHADER, FRAGMENT_SHADER);
		this.camera = camera;

		//          6-----7
		//         /|    /|
		//        / |   / |
		//       1-----0  |     y    RHS coords
		//       |  |  |  |     | 
		//       |  5--|--4     +--x
		//       | /   | /     /
		//       |/    |/     z
		//       2-----3
		
		vertices = new Vector4f[] {
			new Vector4f( 1, 1, 1, 1),
			new Vector4f(-1, 1, 1, 1),
			new Vector4f(-1,-1, 1, 1),
			new Vector4f( 1,-1, 1, 1),
			new Vector4f( 1,-1,-1, 1),
			new Vector4f(-1,-1,-1, 1),
			new Vector4f(-1, 1,-1, 1),
			new Vector4f( 1, 1,-1, 1),
		};
		
		vertexBuffer = GLBuffers.createBuffer(vertices);

		// indices for the triangle forming each face

		indices = new int[] {
			// front
			0, 1, 2,
			2, 3, 0,
			
			// back
			4, 5, 6,
			6, 7, 4,
			
			// top
			0, 7, 6,
			6, 1, 0,
			
			// bottom 
			2, 5, 4,
			4, 3, 2,
			
			// left
			1, 2, 5,
			5, 6, 1,
			
			// right
			0, 3, 4,
			4, 7, 0,
			
		};

		indexBuffer = GLBuffers.createIndexBuffer(indices);
		
		// scale down to fit in window
		getMatrix().scale((float) (1.0f / Math.sqrt(3)));

		colour = new Vector3f(1f, 1f, 1f); // default is white
	}
	
	public void setColour(Color colour) {
		float[] rgb = colour.getComponents(new float[4]);
		this.colour.set(rgb[0], rgb[1], rgb[2]);
	}
	
	private Matrix4f modelMatrix = new Matrix4f();
	
	@Override
	public void drawSelf(Matrix4f mvpMatrix) {
		GL4 gl = (GL4) GLContext.getCurrentGL();
		
		shader.enable();
		
		getModelToWorldMatrix(modelMatrix);
		shader.setUniform("u_mvpMatrix", mvpMatrix);
		shader.setUniform("u_modelMatrix", modelMatrix);
		shader.setAttribute("a_position", vertexBuffer);
		shader.setUniform("u_colour", colour);

		camera.getModelMatrix(cameraModelMatrix);
		cameraModelMatrix.getColumn(3, cameraPosition);
		shader.setUniform("u_cameraPosition", cameraPosition);
		shader.setUniform("u_fogColour", new float[] { 1, 1, 1 });

		gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
		gl.glDrawElements(GL.GL_TRIANGLES, indices.length, GL.GL_UNSIGNED_INT, 0);
	}

}
