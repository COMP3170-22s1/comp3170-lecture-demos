
package comp3170.demos.week2;

import static com.jogamp.opengl.GL.GL_COLOR_BUFFER_BIT;
import static com.jogamp.opengl.GL.GL_TRIANGLES;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;

import comp3170.GLBuffers;
import comp3170.GLException;
import comp3170.Shader;

public class Week2 extends JFrame implements GLEventListener {

	public double TAU = 2 * Math.PI;		// https://tauday.com/tau-manifesto
	
	private int width = 800;
	private int height = 800;

	private GLCanvas canvas;
	private Shader shader;
	
	final private File DIRECTORY = new File("src/comp3170/demos/week2"); 
	final private String VERTEX_SHADER = "colour_vertex.glsl";
	final private String FRAGMENT_SHADER = "colour_fragment.glsl";

	private float[] vertices;
	private int vertexBuffer;
	private float[] colours;
	private int colourBuffer;

	private int[] indices;

	private int indexBuffer;

	public Week2() {
		super("Week 2 example");

		// set up a GL canvas
		GLProfile profile = GLProfile.get(GLProfile.GL4);		 
		GLCapabilities capabilities = new GLCapabilities(profile);
		this.canvas = new GLCanvas(capabilities);
		this.canvas.addGLEventListener(this);
		this.add(canvas);
		
		// set up the JFrame
		
		this.setSize(width,height);
		this.setVisible(true);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});	
		
	}

	@Override
	public void init(GLAutoDrawable arg0) {
		GL4 gl = (GL4) GLContext.getCurrentGL();
		
		// set the background colour to white
		gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		
		// Compile the shader
		try {
			File vertexShader = new File(DIRECTORY, VERTEX_SHADER);
			File fragementShader = new File(DIRECTORY, FRAGMENT_SHADER);
			this.shader = new Shader(vertexShader, fragementShader);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (GLException e) {
			e.printStackTrace();
			System.exit(1);
		}

		// calculate the vertices of a hexagon as (x,y) pairs

		this.vertices = new float[7 * 2];
		
		// the centre

		int n = 0;
		vertices[n++] = 0;	// x
		vertices[n++] = 0;	// y
		
		// the outer ring
		
		float radius = 0.8f;
		
		for (int i = 1; i <= 6; i++) {
			double angle = i * TAU / 6;
			vertices[n++] = (float) (radius * Math.cos(angle));	// x 
			vertices[n++] = (float) (radius * Math.sin(angle)); // y
		}
				
		// copy the data into a Vertex Buffer Object in graphics memory		
	    this.vertexBuffer = GLBuffers.createBuffer(vertices, GL4.GL_FLOAT_VEC2);

		this.colours = new float[] {
				 1.0f, 1.0f, 1.0f,  // WHITE
				 1.0f, 0.0f, 0.0f,  // RED
				 1.0f, 1.0f, 0.0f,  // YELLOW
				 0.0f, 1.0f, 0.0f,  // GREEN
				 0.0f, 1.0f, 1.0f,  // CYAN
				 0.0f, 0.0f, 1.0f,  // BLUE
				 1.0f, 0.0f, 1.0f,  // MAGENTA
		};

		// copy the data into a Vertex Buffer Object in graphics memory		
	    this.colourBuffer = GLBuffers.createBuffer(colours, GL4.GL_FLOAT_VEC3);
	    
	    this.indices = new int[] {
	    	0, 1, 2,
	    	0, 2, 3,
	    	0, 3, 4,
	    	0, 4, 5,
	    	0, 5, 6,
	    	0, 6, 1,	    		
	    };
	    
	    this.indexBuffer = GLBuffers.createIndexBuffer(indices);
	    		 
	    
	}

	@Override
	public void display(GLAutoDrawable arg0) {
		GL4 gl = (GL4) GLContext.getCurrentGL();

        // clear the colour buffer
		gl.glClear(GL_COLOR_BUFFER_BIT);		

		// activate the shader
		this.shader.enable();
		
        // connect the vertex buffer to the a_position attribute		   
	    this.shader.setAttribute("a_position", vertexBuffer);
	    this.shader.setAttribute("a_colour", colourBuffer);

	    // write the colour value into the u_colour uniform 
//	    float[] colour = {1.0f, 0.0f, 1.0f};	    
//      this.shader.setUniform("u_colour", colour);	    
	    
        // draw the shape as a series of lines in a loop
//        gl.glDrawArrays(GL_TRIANGLES, 0, vertices.length / 2);           	

	    gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, this.indexBuffer);
	    gl.glDrawElements(GL_TRIANGLES, indices.length, GL.GL_UNSIGNED_INT, 0);
	}

	@Override
	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3, int arg4) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub
		
	}


	
	public static void main(String[] args) { 
		new Week2();
	}


}
