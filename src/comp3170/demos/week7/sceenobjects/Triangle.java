package comp3170.demos.week7.sceenobjects;

import org.joml.Vector3f;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import comp3170.SceneObject;
import comp3170.Shader;

public class Triangle extends SceneObject {

	private float[] vertices = {
		 0, 1, 0,
		 1, 0, 0,
	    -1, 0, 0,	    
	};

	private int vertexBuffer;

	private float[] normalVertices;	
	private int normalVertexBuffer;
	
	private float[] colour = { 1.0f, 1.0f, 0.0f}; // white
	private float[] normalColour = { 1.0f, 0.0f, 0.0f }; // red
	
	public Triangle(Shader shader) {
		
		// Calculate the face normal

		Vector3f m = new Vector3f();	// midpoint

		Vector3f[] v = new Vector3f[3];
		for (int i = 0; i < 3; i++) {
			v[i] = new Vector3f(
					vertices[i * 3],
					vertices[i * 3 + 1],
					vertices[i * 3 + 2]);
			
			m.add(v[i]);
		}
		
		m.mul(1.0f/3);

		Vector3f n = new Vector3f();	// normal
		Vector3f a = new Vector3f();	// normal
		Vector3f b = new Vector3f();	// normal
		
		// a = v1 - v0
		// b = v2 - v0
		// n = a x b
		
		a.set(v[1]);
		a.sub(v[0]);
		b.set(v[2]);
		b.sub(v[0]);
		a.cross(b, n);	// NOTE: odd syntax
		
		// normalise n to length 1
		
		n.normalize();
		
		// draw the normal as a line from m to m+n
		
		n.add(m);
		normalVertices = new float[6];
		normalVertices[0] = m.x;
		normalVertices[1] = m.y;
		normalVertices[2] = m.z;
		normalVertices[3] = n.x;
		normalVertices[4] = n.y;
		normalVertices[5] = n.z;
		
		this.vertexBuffer = shader.createBuffer(this.vertices);
		this.normalVertexBuffer = shader.createBuffer(this.normalVertices);
	}

	@Override
	protected void drawSelf(Shader shader) {
		GL4 gl = (GL4) GLContext.getCurrentGL();

		// draw the triangle
		
		shader.setAttribute("a_position", this.vertexBuffer);
		shader.setUniform("u_colour", this.colour);

		gl.glDrawArrays(GL.GL_TRIANGLES, 0, this.vertices.length / 3);

		// draw the normal
		
		shader.setAttribute("a_position", this.normalVertexBuffer);
		shader.setUniform("u_colour", this.normalColour);

		gl.glDrawArrays(GL.GL_LINES, 0, this.normalVertices.length / 3);
		
		
	}

}
