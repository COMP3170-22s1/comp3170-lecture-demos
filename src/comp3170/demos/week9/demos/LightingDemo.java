package comp3170.demos.week9.demos;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JFrame;

import org.joml.Matrix4f;

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
import comp3170.demos.SceneObject;
import comp3170.demos.week9.cameras.Camera;
import comp3170.demos.week9.sceneobjects.Axes;
import comp3170.demos.week9.sceneobjects.Cylinder;
import comp3170.demos.week9.sceneobjects.DirectionalLight;
import comp3170.demos.week9.sceneobjects.Grid;

public class LightingDemo extends JFrame implements GLEventListener {

	private GLCanvas canvas;

	final private static float TAU = (float) (Math.PI * 2);
	
	// screen size in pixels
	private int screenWidth = 1000;
	private int screenHeight = 1000;
	
	private InputManager input;
	private Animator animator;
	private long oldTime;

	private Camera camera;
	private Grid grid;
	private Cylinder cylinder;

	private Axes axes;

	private DirectionalLight light;
	
	private Matrix4f viewMatrix = new Matrix4f();
	private Matrix4f projectionMatrix = new Matrix4f();
	private Matrix4f mvpMatrix = new Matrix4f();

	private SceneObject root;
	
	public LightingDemo() {
		super("Lighting Demo");
		
		GLProfile profile = GLProfile.get(GLProfile.GL4);		 
		GLCapabilities capabilities = new GLCapabilities(profile);
		canvas = new GLCanvas(capabilities);
		canvas.addGLEventListener(this);
		add(canvas);
		
		// set up Animator		
		animator = new Animator(canvas);
		animator.start();
		oldTime = System.currentTimeMillis();
				
		// set up Input manager
		input = new InputManager(canvas);
		
		// set up the JFrame		
		// make it twice as wide as the view width
		
		setSize(screenWidth, screenHeight);
		setVisible(true);
		addWindowListener(new WindowAdapter() {
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

		root = new SceneObject();
		
		grid = new Grid(10);
		grid.setParent(root);

		cylinder = new Cylinder();
		cylinder.setParent(root);
		cylinder.setDiffuseMaterial(Color.red);
		cylinder.getMatrix().scale(0.5f, 1, 0.5f);

		axes = new Axes();
		axes.setParent(root);
		axes.getMatrix().translate(0,2,0);
		
		// light source
		Color ambientLight = new Color(0.1f, 0.1f, 0.1f);
		light = new DirectionalLight(Color.white, ambientLight);
		light.setParent(root);
		light.getMatrix().translate(0,0.5f,0);
		cylinder.setLight(light);
		
		// camera 
		camera = new Camera(CAMERA_WIDTH, CAMERA_HEIGHT, CAMERA_NEAR, CAMERA_FAR);
		camera.setParent(root);
		cylinder.setCamera(camera);
	}

	private static final float CAMERA_WIDTH = 3f;
	private static final float CAMERA_HEIGHT = 3f;
	private static final float CAMERA_NEAR = 0.1f;
	private static final float CAMERA_FAR = 10.0f;	
		
	public void update() {
		long time = System.currentTimeMillis();
		float dt = (time - oldTime) / 1000.0f;
		oldTime = time;
		
		cylinder.update(input, dt);
		camera.update(input, dt);
		light.update(input, dt);
		
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
				
		// get the camera matrices
		
		// Note: rather than pass the view and projection matrices to every object
		// we multiply them into the matrix before descending the scene graph
		// and pass a single Model-View-Projection matrix to the shader
		//
		// M_mvp = M_projection * M_view * M_model
		
		camera.getViewMatrix(viewMatrix);
		camera.getProjectionMatrix(projectionMatrix);		
		mvpMatrix.set(projectionMatrix).mul(viewMatrix);
		
		// draw the scenegraph
		root.draw(mvpMatrix);
	}

	@Override
	/**
	 * Called when the canvas is resized
	 */
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		GL4 gl = (GL4) GLContext.getCurrentGL();
		
		screenWidth = width;
		screenHeight = height;
		
	}

	@Override
	/**
	 * Called when we dispose of the canvas 
	 */
	public void dispose(GLAutoDrawable drawable) {

	}

	public static void main(String[] args) throws IOException, GLException {
		new LightingDemo();
	}


}
