package comp3170.demos.week9.demos;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
import com.jogamp.opengl.util.Animator;

import comp3170.GLException;
import comp3170.InputManager;
import comp3170.Shader;
import comp3170.demos.week7.shaders.ShaderLibrary;
import comp3170.demos.week9.cameras.OrthographicCamera;
import comp3170.demos.week9.sceneobjects.Cylinder;
import comp3170.demos.week9.sceneobjects.Grid;

public class NormalsDemo extends JFrame implements GLEventListener {

	private GLCanvas canvas;

	final private static float TAU = (float) (Math.PI * 2);
	
	// screen size in pixels
	private int screenWidth = 1000;
	private int screenHeight = 1000;
	
	private InputManager input;
	private Animator animator;
	private long oldTime;

	private final static String SIMPLE_VERTEX = "simpleVertex.glsl";
	private final static String SIMPLE_FRAGMENT = "simpleFragment.glsl";
	
	private OrthographicCamera camera;
	private Grid grid;

	private Cylinder cylinder;
	
	public NormalsDemo() {
		super("Z-fighting demo");
		
		GLProfile profile = GLProfile.get(GLProfile.GL4);		 
		GLCapabilities capabilities = new GLCapabilities(profile);
		this.canvas = new GLCanvas(capabilities);
		this.canvas.addGLEventListener(this);
		this.add(canvas);
		
		// set up Animator		
		this.animator = new Animator(canvas);
		this.animator.start();
		this.oldTime = System.currentTimeMillis();
				
		// set up Input manager
		this.input = new InputManager(canvas);
		
		// set up the JFrame		
		// make it twice as wide as the view width
		
		this.setSize(screenWidth, screenHeight);
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
				
		// enable depth testing
		
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glEnable(GL.GL_CULL_FACE);

		this.grid = new Grid(10);
		
		Shader simpleShader = ShaderLibrary.compileShader(SIMPLE_VERTEX, SIMPLE_FRAGMENT);		
		this.cylinder = new Cylinder(simpleShader);
		
		// camera 
		this.camera = new OrthographicCamera(input, CAMERA_WIDTH, CAMERA_HEIGHT, CAMERA_NEAR, CAMERA_FAR);
		camera.setHeight(1);
		camera.setDistance(CAMERA_DISTANCE);
	}

	private static final float CAMERA_DISTANCE = 2;
	private static final float CAMERA_WIDTH = 3f;
	private static final float CAMERA_HEIGHT = 3f;
	private static final float CAMERA_NEAR = 0.1f;
	private static final float CAMERA_FAR = 10.0f;	
		
	public void update() {
		long time = System.currentTimeMillis();
		float dt = (time - oldTime) / 1000.0f;
		oldTime = time;
		
		camera.update(dt);
		input.clear();
	}
	
	
	@Override
	/**
	 * Called when the canvas is redrawn
	 */
	public void display(GLAutoDrawable drawable) {
		GL4 gl = (GL4) GLContext.getCurrentGL();

		update();

		gl.glViewport(0, 0, screenWidth, screenHeight);
		
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);		
		
		// clear the depth buffer
		gl.glClearDepth(1f);
		gl.glClear(GL.GL_DEPTH_BUFFER_BIT);		
				
		// draw
		this.grid.draw(camera);
		this.cylinder.draw(camera);
	}

	@Override
	/**
	 * Called when the canvas is resized
	 */
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		GL4 gl = (GL4) GLContext.getCurrentGL();
		
		this.screenWidth = width;
		this.screenHeight = height;
		
	}

	@Override
	/**
	 * Called when we dispose of the canvas 
	 */
	public void dispose(GLAutoDrawable drawable) {

	}

	public static void main(String[] args) throws IOException, GLException {
		new NormalsDemo();
	}


}
