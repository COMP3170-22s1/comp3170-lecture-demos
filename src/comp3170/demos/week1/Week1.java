package comp3170.demos.week1;


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

import comp3170.GLBuffers;
import comp3170.GLException;
import comp3170.Shader;

/**
 * @author malcolmryan
 *
 */

public class Week1 extends JFrame implements GLEventListener {
	
	private GLCanvas canvas;
	private Shader shader;
	
	final private File DIRECTORY = new File("src/comp3170/demos/week1"); 
	final private String VERTEX_SHADER = "vertex.glsl";
	final private String FRAGMENT_SHADER = "fragment.glsl";

	private float[] vertices;
	private int vertexBuffer;
	
	private int screenWidth = 800;
	private int screenHeight = 800;


	public Week1() {
		super("Week 1 example");
		
		// create an OpenGL canvas and add this as a listener
		
		GLProfile profile = GLProfile.get(GLProfile.GL4);		 
		GLCapabilities capabilities = new GLCapabilities(profile);
		canvas = new GLCanvas(capabilities);
		canvas.addGLEventListener(this);
		add(canvas);
		
		// set up the JFrame
		
		setSize(screenWidth,screenHeight);
		setVisible(true);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});	
		
	}

	/**
	 * Initialise the GLCanvas
	 * 
	 * <img src="images/square.png" />
	 * 
	 */
	@Override
	public void init(GLAutoDrawable drawable) {
		GL4 gl = (GL4) GLContext.getCurrentGL();
		
		// set the background colour to black
		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

		// Compile the shader
		try {
			File vertexShader = new File(DIRECTORY, VERTEX_SHADER);
			File fragementShader = new File(DIRECTORY, FRAGMENT_SHADER);
			shader = new Shader(vertexShader, fragementShader);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (GLException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		// create the shape
		
		// vertices of a square as (x,y) pairs
		vertices = new float[] {
				 1.0f,	1.0f,
				-1.0f,	1.0f,
				-1.0f, -1.0f,
				
				-1.0f, -1.0f,
				 1.0f, -1.0f,
				 1.0f,	1.0f,
		};
		
		// copy the data into a Vertex Buffer Object in graphics memory		
	    vertexBuffer = GLBuffers.createBuffer(vertices, GL4.GL_FLOAT_VEC2);

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
		gl.glClear(GL4.GL_COLOR_BUFFER_BIT);		

		// activate the shader
		shader.enable();
		
        // connect the vertex buffer to the a_position attribute		   
	    shader.setAttribute("a_position", vertexBuffer);

	    // write the colour value into the u_colour uniform 
	    float[] colour = {1.0f, 0.0f, 0.0f};	    
        shader.setUniform("u_colour", colour);
        
        float[] screenSize = new float[] { screenWidth, screenHeight };
        shader.setUniform("u_screenSize", screenSize);	
	    
        // draw the shape as a series of lines in a loop
        gl.glDrawArrays(GL4.GL_TRIANGLES, 0, vertices.length / 2);           	
        
	}

	@Override
	/**
	 * Called when the canvas is resized
	 */
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		this.screenWidth = width;
		this.screenHeight = height;
	}


	public static void main(String[] args) throws IOException, GLException {
		new Week1();
	}


}
