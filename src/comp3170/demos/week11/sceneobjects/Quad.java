package comp3170.demos.week11.sceneobjects;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import comp3170.SceneObject;
import comp3170.Shader;

public class Quad extends SceneObject {

	private float[] vertices = new float[] {
		-1, -1, 0,
		 1, -1, 0,
		-1,  1, 0,
		 1,  1, 0		
	};
	
	private int vertexBuffer;
	
	private int[] indices = new int[] {		
		0,1,3,
		3,2,0,
	};

	private int indexBuffer;
	
	private float[] uvs = new float[] {
		0,0,
		1,0,
		0,1,
		1,1,
	};
	
	private int uvBuffer;
	
	private int texture;
	
	public Quad(Shader shader, int texture) {
		super(shader);
		
		this.texture = texture;
		this.vertexBuffer = shader.createBuffer(vertices, GL4.GL_FLOAT_VEC3);
		this.uvBuffer = shader.createBuffer(uvs, GL4.GL_FLOAT_VEC2);
		this.indexBuffer = shader.createIndexBuffer(indices);
	}

	@Override
	protected void drawSelf(Shader shader) {
		GL4 gl = (GL4) GLContext.getCurrentGL();

		shader.setUniform("u_mvpMatrix", this.mvpMatrix);
		shader.setAttribute("a_position", this.vertexBuffer);
		shader.setAttribute("a_texcoord", this.uvBuffer);

		gl.glActiveTexture(GL.GL_TEXTURE0);
		gl.glBindTexture(GL.GL_TEXTURE_2D, this.texture);
		shader.setUniform("u_texture", 0);

		gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, this.indexBuffer);		
		gl.glDrawElements(GL.GL_TRIANGLES, this.indices.length, GL.GL_UNSIGNED_INT, 0);		
	}	
}
