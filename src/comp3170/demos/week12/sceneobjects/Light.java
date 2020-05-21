package comp3170.demos.week12.sceneobjects;

import org.joml.Matrix4f;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import comp3170.GLException;
import comp3170.SceneObject;
import comp3170.Shader;

public class Light extends SceneObject {
	private final float TAU = (float) (Math.PI * 2);
	private final float FOVY = TAU / 4;
	private final float ASPECT = 1.0f;
	private final float NEAR = 0.1f;
	private final float FAR = 40.0f;
	private final int SIZE = 1024;

	private Matrix4f viewMatrix;
	private Matrix4f projectionMatrix;
	private Matrix4f lightMatrix;
	private int shadowBuffer;

	public Light() {
		GL4 gl = (GL4) GLContext.getCurrentGL();

		this.viewMatrix = new Matrix4f();
		this.projectionMatrix = new Matrix4f();
		this.lightMatrix = new Matrix4f();

		// Set up render to texture

		int[] rt = new int[1];
		gl.glGenTextures(1, rt, 0);
		gl.glBindTexture(GL.GL_TEXTURE_2D, rt[0]);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL4.GL_RGBA32F, SIZE, SIZE, 0, GL4.GL_RGBA, GL.GL_FLOAT, null);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);
		this.shadowBuffer = rt[0];

	}

	public int getShadowBuffer() {
		return this.shadowBuffer;
	}

	/**
	 * Calculate the view-projection matrix for the light
	 * 
	 * @param lightMatrix
	 * @return
	 */

	public Matrix4f getLightMatrix(Matrix4f lightMatrix) {

		// set the view matrix
		getWorldMatrix(this.viewMatrix);
		this.viewMatrix.invert();

		// set the projection matrix

		this.projectionMatrix.setPerspective(FOVY, ASPECT, NEAR, FAR);

		// draw the objects in the scene graph recursively
		this.lightMatrix.identity();
		this.lightMatrix.mul(projectionMatrix);
		this.lightMatrix.mul(viewMatrix);

		return this.lightMatrix;
	}

	public int getSize() {
		return SIZE;
	}

}
