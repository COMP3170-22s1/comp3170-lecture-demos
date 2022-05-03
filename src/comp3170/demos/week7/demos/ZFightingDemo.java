package comp3170.demos.week7.demos;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JFrame;

import org.joml.Matrix4f;
import org.joml.Vector3f;

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
import comp3170.demos.week7.cameras.PerspectiveCamera;
import comp3170.demos.week7.sceneobjects.Quad;

public class ZFightingDemo extends JFrame implements GLEventListener {

	private GLCanvas canvas;

	final private static float TAU = (float) (Math.PI * 2);
	
	// screen size in pixels
	private int screenWidth = 1000;
	private int screenHeight = 1000;
	
	private InputManager input;
	private Animator animator;
	private long oldTime;

	private PerspectiveCamera camera;
	private Quad redQuad;
	private Quad blueQuad;

	private SceneObject root;
	
	public ZFightingDemo() {
		super("Z-fighting demo");
		
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

		root = new SceneObject();
		
		redQuad = new Quad(Color.RED);
		redQuad.setParent(root);
		
		blueQuad = new Quad(Color.BLUE);
		blueQuad.setParent(root);
		blueQuad.getMatrix().rotateY(0.1f);
				
		// camera 
		float aspect = (float)screenWidth / screenHeight;
		camera = new PerspectiveCamera(input, CAMERA_FOVY, aspect, CAMERA_NEAR, CAMERA_FAR);
		camera.setDistance(CAMERA_DISTANCE);
		camera.setHeight(CAMERA_HEIGHT);
	}

	private static final float CAMERA_DISTANCE = 5;
	private static final float CAMERA_HEIGHT = 0f;
	private static final float CAMERA_FOVY = TAU / 8;
	private static final float CAMERA_NEAR = 0.1f;
	private static final float CAMERA_FAR = 100.0f;	

	private static final float ROTATE_SPEED = TAU / 20;
	
	public void update() {
		long time = System.currentTimeMillis();
		float dt = (time - oldTime) / 1000.0f;
		oldTime = time;
		
		if (input.isKeyDown(KeyEvent.VK_NUMPAD1)) {
			blueQuad.getMatrix().rotateY(-ROTATE_SPEED * dt);		
		}
		if (input.isKeyDown(KeyEvent.VK_NUMPAD2)) {
			blueQuad.getMatrix().identity();		
		}
		if (input.isKeyDown(KeyEvent.VK_NUMPAD3)) {
			blueQuad.getMatrix().rotateY(ROTATE_SPEED * dt);		
		}
		
		camera.update(dt);
		input.clear();
	}
	
	private Matrix4f viewMatrix = new Matrix4f();
	private Matrix4f projectionMatrix = new Matrix4f();
	private Matrix4f mvpMatrix = new Matrix4f();
	

	@Override
	/**
	 * Called when the canvas is redrawn
	 */
	public void display(GLAutoDrawable drawable) {
		GL4 gl = (GL4) GLContext.getCurrentGL();

		update();

		gl.glViewport(0, 0, screenWidth, screenHeight);
		
		// set the background colour to black
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);		
		
		// clear the depth buffer
		gl.glClearDepth(1f);
		gl.glClear(GL.GL_DEPTH_BUFFER_BIT);		
				
		// pre-multiply projetion and view matrices
		camera.getViewMatrix(viewMatrix);
		camera.getProjectionMatrix(projectionMatrix);		
		mvpMatrix.set(projectionMatrix).mul(viewMatrix);
			
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
		new ZFightingDemo();
	}


}
