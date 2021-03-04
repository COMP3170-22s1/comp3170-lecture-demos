package comp3170.demos.week3;

import org.joml.Matrix3f;
import org.joml.Vector2f;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import comp3170.Shader;

public class Camera {

    
    private Vector2f position;
	private float angle;
	private float zoom;
	private float aspect;
	
	private Matrix3f modelMatrix;
	private Matrix3f translationMatrix;
	private Matrix3f rotationMatrix;
	private Matrix3f perspectiveMatrix;
	private float[] vertices;
	private int vertexBuffer;
		
	public Camera(Shader shader) {
		// verices for a 1x1 square with origin in the centre
		// 
		//  (-0.5,0.5)   (0.5,0.5)
		//       2-----------3
		//       | \         |
		//       |   \       |
		//       |     \     |
		//       |       \   |
		//       |         \ |
		//       0-----------1
		//  (-0.5,-0.5)  (0.5,-0.5)		
		
		this.vertices = new float[] {
			-0.5f, -0.5f,
			 0.5f, -0.5f,
			 0.5f,  0.5f,
			-0.5f,  0.5f,
		};
		
		// copy the data into a Vertex Buffer Object in graphics memory		
	    this.vertexBuffer = shader.createBuffer(vertices, GL4.GL_FLOAT_VEC2);
	    
	    this.position = new Vector2f(0,0);
        this.angle = 0;
        this.zoom = 1;
        this.aspect = 1;

	    this.modelMatrix = new Matrix3f();	    	    
	    this.translationMatrix = new Matrix3f();    
	    this.rotationMatrix = new Matrix3f();
	    this.perspectiveMatrix = new Matrix3f();      	
    }
	
	public Vector2f getPosition() {
		return position;
	}

	public void setPosition(float x, float y) {
		position.x = x;
		position.y = y;
	}

	public void translate(Vector2f movement) {
		position.x += movement.x;
		position.y += movement.y;
	}
	
	public float getAngle() {
		return angle;
	}
		
	public void setAngle(float angle) {
		this.angle = angle;
	}
	
	public void rotate(float radians) {
		angle += radians;
	}
	
	public float getZoom() {
		return zoom;
	}

	public void setZoom(float zoom) {
		this.zoom = zoom;
	}

	public void zoom(float factor) {
		zoom *= factor;		
	}	

	public float getAspect() {
		return aspect;
	}

	public void setAspect(float aspect) {
		this.aspect = aspect;
	}

	private Matrix3f getModelMatrix() {
		//      [ 1  0  Tx ]
		// MT = [ 0  1  Ty ]
		//      [ 0  0  1  ]
		
		translationMatrix.m20(position.x);
		translationMatrix.m21(position.y);
		
		//      [ cos(a)  -sin(a)  0 ]
		// MR = [ sin(a)   cos(a)  0 ]
		//      [ 0        0       1 ]
		
		float s = (float) Math.sin(angle);
		float c = (float) Math.cos(angle);
		rotationMatrix.m00(c);
		rotationMatrix.m01(s);
		rotationMatrix.m10(-s);
		rotationMatrix.m11(c);

		//      [ sx  0   0 ]
		// MS = [ 0   sy  0 ]
		//      [ 0   0   1 ]

		perspectiveMatrix.m00(zoom * aspect);
		perspectiveMatrix.m11(zoom);	
		
		// M = MT * MR * MS

		modelMatrix.identity();
		modelMatrix.mul(translationMatrix);
		modelMatrix.mul(rotationMatrix);
		modelMatrix.mul(perspectiveMatrix);

		return modelMatrix;
	}
	
	public Matrix3f getViewMatrix() {
		//      [ 1  0  Tx ]
		// MT = [ 0  1  Ty ]
		//      [ 0  0  1  ]
		
		translationMatrix.m20(position.x);
		translationMatrix.m21(position.y);
		
		//      [ cos(a)  -sin(a)  0 ]
		// MR = [ sin(a)   cos(a)  0 ]
		//      [ 0        0       1 ]
		
		float s = (float) Math.sin(angle);
		float c = (float) Math.cos(angle);
		rotationMatrix.m00(c);
		rotationMatrix.m01(s);
		rotationMatrix.m10(-s);
		rotationMatrix.m11(c);

		modelMatrix.identity();
		modelMatrix.mul(translationMatrix);
		modelMatrix.mul(rotationMatrix);

		// view matrix is the inverse of the camera's model matrix
		return modelMatrix.invert();		
	}
	
	public Matrix3f getPerspectiveMatrix() {
		//      [ sx  0   0 ]
		// MS = [ 0   sy  0 ]
		//      [ 0   0   1 ]

		perspectiveMatrix.m00(zoom * aspect);
		perspectiveMatrix.m11(zoom);		

		return perspectiveMatrix.invert();
		
	}
	
	public void draw(Shader shader) {
		GL4 gl = (GL4) GLContext.getCurrentGL();
		
		// set the model matrix
		shader.setUniform("u_modelMatrix", getModelMatrix());
		
        // connect the vertex buffer to the a_position attribute		   
	    shader.setAttribute("a_position", vertexBuffer);

	    // write the colour value into the u_colour uniform 
	    shader.setUniform("u_colour", new float[] {1,1,1});	    
	    
	    // draw a rectangle as a line loop
	    gl.glDrawArrays(GL.GL_LINE_LOOP, 0, vertices.length / 2);		
	}

	
}
