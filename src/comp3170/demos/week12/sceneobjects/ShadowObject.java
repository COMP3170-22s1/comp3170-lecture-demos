package comp3170.demos.week12.sceneobjects;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import comp3170.SceneObject;
import comp3170.Shader;

public class ShadowObject extends SceneObject {

	protected Light light;
	protected Vector4f lightPosition;
	protected Matrix4f lightMatrix;

	public ShadowObject() {
		super();
	}

	public ShadowObject(Shader shader) {
		super(shader);
	}

	public void setLight(Light light) {
		this.light = light;		
		this.lightPosition = new Vector4f();
		this.lightMatrix = new Matrix4f();
	}
	
	protected void setLightUniforms(Shader shader) {
		GL4 gl = (GL4) GLContext.getCurrentGL();
		if (shader.hasUniform("u_worldMatrix")) {
			getWorldMatrix(this.worldMatrix);
			shader.setUniform("u_worldMatrix", this.worldMatrix);			
		}
		
		if (shader.hasUniform("u_lightPosition")) {
			this.light.getPosition(this.lightPosition);
			shader.setUniform("u_lightPosition", this.lightPosition);					
		}

		if (shader.hasUniform("u_lightMatrix")) {
			this.light.getLightMatrix(this.lightMatrix);
			shader.setUniform("u_lightMatrix", this.lightMatrix);
		}
		
		if (shader.hasUniform("u_shadowBuffer")) {
			int shadowBuffer = this.light.getShadowBuffer();

			gl.glActiveTexture(GL.GL_TEXTURE0);
			gl.glBindTexture(GL.GL_TEXTURE_2D, shadowBuffer);
			shader.setUniform("u_shadowBuffer", 0);
		}
	}


}