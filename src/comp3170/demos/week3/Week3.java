
package comp3170.demos.week3;

import static com.jogamp.opengl.GL.GL_COLOR_BUFFER_BIT;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.Animator;

import comp3170.GLException;
import comp3170.Shader;

public class Week3 extends JFrame implements GLEventListener {

	public static final float TAU = (float) (2 * Math.PI);		// https://tauday.com/tau-manifesto
	
	private int width = 800;
	private int height = 800;

	private GLCanvas canvas;
	private Shader shader;
	
	final private File DIRECTORY = new File("src/comp3170/demos/week3"); 
	final private String VERTEX_SHADER = "vertex.glsl";
	final private String FRAGMENT_SHADER = "fragment.glsl";

	private List<Square> squares;

	private Animator animator;
	private long oldTime;


	public Week3() {
		super("Week 3 example");

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
		
		// set up Animator		
		this.animator = new Animator(canvas);
		this.animator.start();
		this.oldTime = System.currentTimeMillis();		
		
	}

	private static final int NSQUARES = 100;
	
	@Override
	public void init(GLAutoDrawable arg0) {
		GL4 gl = (GL4) GLContext.getCurrentGL();
		
		// set the background colour to black
		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		
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

		// Set up the scene

		
	    this.squares = new ArrayList<Square>();
	    
	    for (int i = 0; i < NSQUARES; i++) {
			Square square = new Square(shader);
			float x = (float) Math.random() * 2 - 1;
			float y = (float) Math.random() * 2 - 1;
			square.setPosition(x, y);
			Color colour = Color.getHSBColor((float) Math.random(), 1, 1);
			square.setColour(colour);
			square.setAngle(0);
			square.setScale(0.1f, 0.1f);
			squares.add(square);
	    }
	    
	}

	private static final float ROTATION_SPEED = TAU / 6;
	private static final float SCALE_SPEED = 1.1f;
	
	private void update() {
		long time = System.currentTimeMillis();
		float deltaTime = (time - oldTime) / 1000f;
		oldTime = time;
		System.out.println("update: dt = " + deltaTime + "s");
		
		
		for (Square sq : squares) {
			sq.scale((float)Math.pow(SCALE_SPEED, deltaTime));
		}
	
	}
	
	@Override
	public void display(GLAutoDrawable arg0) {
		System.out.println("display");
		
		GL4 gl = (GL4) GLContext.getCurrentGL();
		
		// update the scene
		update();	

        // clear the colour buffer
		gl.glClear(GL_COLOR_BUFFER_BIT);		

		// activate the shader
		this.shader.enable();		
				
		// draw the squares
		for (Square sq : squares) {
			sq.draw(shader);
		}
		
	}

	@Override
	public void reshape(GLAutoDrawable d, int x, int y, int width, int height) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void dispose(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub		
	}
	
	public static void main(String[] args) { 
		new Week3();
	}


}
