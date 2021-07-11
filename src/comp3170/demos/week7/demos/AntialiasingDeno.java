package comp3170.demos.week7.demos;

import static com.jogamp.opengl.GL.GL_COLOR_BUFFER_BIT;
import static com.jogamp.opengl.GL.GL_TRIANGLES;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;

import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.Animator;

import comp3170.GLBuffers;
import comp3170.GLException;
import comp3170.Shader;
import comp3170.demos.week7.shaders.ShaderLibrary;

public class AntialiasingDeno extends JFrame implements GLEventListener {

	private GLCanvas canvas;
	private Shader shader;
	
	final private String VERTEX_SHADER = "vertex.glsl";
	final private String FRAGMENT_SHADER = "fragment.glsl";

	private float[] vertices;
	private int vertexBuffer;
	
	private int width = 800;
	private int height = 800;


	public AntialiasingDeno() {
		super("Antialiasing example");
		
		// create an OpenGL canvas and add this as a listener
		
		GLProfile profile = GLProfile.get(GLProfile.GL4);		 
		GLCapabilities capabilities = new GLCapabilities(profile);
		
		// uncomment these lines to add anti-aliasing
		
		capabilities.setSampleBuffers(false);
		capabilities.setNumSamples(4);

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
	/**
	 * Initialise the GLCanvas
	 */
	public void init(GLAutoDrawable drawable) {
		GL4 gl = (GL4) GLContext.getCurrentGL();
		
		// set the background colour to black
		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

		this.shader = ShaderLibrary.compileShader(VERTEX_SHADER, FRAGMENT_SHADER);
		
		// create the shape
		
		// vertices of a square as (x,y) pairs
		this.vertices = new float[] {
				 0.0f,	1.0f,
				 1.0f, -1.0f,
				-1.0f, -1.0f,
		};
		
		// copy the data into a Vertex Buffer Object in graphics memory		
	    this.vertexBuffer = GLBuffers.createBuffer(vertices, GL4.GL_FLOAT_VEC2);

	}

	@Override
	/**
	 * Called when we dispose of the canvas 
	 */
	public void dispose(GLAutoDrawable drawable) {
		// nothing to do
	}

		
	@Override
	/**
	 * Called when the canvas is redrawn
	 */
	public void display(GLAutoDrawable drawable) {
		GL4 gl = (GL4) GLContext.getCurrentGL();

        // clear the colour buffer
		gl.glClear(GL_COLOR_BUFFER_BIT);		

		// activate the shader
		this.shader.enable();
		
        // connect the vertex buffer to the a_position attribute		   
	    this.shader.setAttribute("a_position", vertexBuffer);

	    // write the colour value into the u_colour uniform 
	    float[] colour = {1.0f, 0.0f, 0.0f, 1.0f};	    
        this.shader.setUniform("u_colour", colour);
   
        // draw the shape as a series of lines in a loop
        gl.glDrawArrays(GL_TRIANGLES, 0, vertices.length / 2);           	
        
	}

	@Override
	/**
	 * Called when the canvas is resized
	 */
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		this.width = width;
		this.height = height;
	}


	public static void main(String[] args) throws IOException, GLException {
		new AntialiasingDeno();
	}


}
