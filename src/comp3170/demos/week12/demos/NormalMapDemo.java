package comp3170.demos.week12.demos;

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
import comp3170.demos.week12.sceneobjects.Cubemap;
import comp3170.demos.week12.sceneobjects.CylinderWithNormalMap;
import comp3170.demos.week12.sceneobjects.Grid;

public class NormalMapDemo extends JFrame implements GLEventListener {

	private GLCanvas canvas;

	final private static float TAU = (float) (Math.PI * 2);
	
	// screen size in pixels
	private int screenWidth = 1000;
	private int screenHeight = 1000;
	
	private InputManager input;
	private Animator animator;
	private long oldTime;

	private SceneObject root;
	private Cubemap cubemap;
	
	public NormalMapDemo() {
		super("Normal map demo");
		
		GLProfile profile = GLProfile.get(GLProfile.GL4);		 
		GLCapabilities capabilities = new GLCapabilities(profile);
		canvas = new GLCanvas(capabilities);
		canvas.addGLEventListener(this);
		add(canvas);
		
		animator = new Animator(canvas);
		animator.start();
		oldTime = System.currentTimeMillis();
		input = new InputManager(canvas);
		
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
		
		gl.glEnable(GL.GL_DEPTH_TEST);	
		gl.glDepthFunc(GL.GL_LEQUAL);	
		gl.glEnable(GL.GL_CULL_FACE);	
		
		root = new SceneObject();
		Grid grid = new Grid(20);
		grid.setParent(root);
		grid.getMatrix().scale(2);
		
		CylinderWithNormalMap cylinder = new CylinderWithNormalMap();
		cylinder.setParent(root);
		
	}

	private static final float CAMERA_HEIGHT = 0.5f;
	private Vector3f cameraPosition = new Vector3f(0,CAMERA_HEIGHT,0);
	private Vector3f cameraAngles = new Vector3f(0,0,0);
	private static final float CAMERA_ROTATION = TAU / 10;
	private static final float CAMERA_MOVEMENT = 1;
	private static final float CAMERA_FOVY = TAU / 8;
	private static final float CAMERA_NEAR = 0.1f;
	private static final float CAMERA_FAR = 10.0f;		
	
	public void update() {
		long time = System.currentTimeMillis();
		float dt = (time - oldTime) / 1000.0f;
		oldTime = time;
		
		
		if (input.isKeyDown(KeyEvent.VK_LEFT)) {
			cameraAngles.y += CAMERA_ROTATION * dt;
		}
		if (input.isKeyDown(KeyEvent.VK_RIGHT)) {
			cameraAngles.y -= CAMERA_ROTATION * dt;
		}
		if (input.isKeyDown(KeyEvent.VK_UP)) {
			cameraAngles.x += CAMERA_ROTATION * dt;
		}
		if (input.isKeyDown(KeyEvent.VK_DOWN)) {
			cameraAngles.x -= CAMERA_ROTATION * dt;
		}
		
		if (input.isKeyDown(KeyEvent.VK_A)) {
			cameraPosition.x -= CAMERA_MOVEMENT * dt;
		}
		if (input.isKeyDown(KeyEvent.VK_D)) {
			cameraPosition.x += CAMERA_MOVEMENT * dt;
		}
		if (input.isKeyDown(KeyEvent.VK_S)) {
			cameraPosition.z += CAMERA_MOVEMENT * dt;
		}
		if (input.isKeyDown(KeyEvent.VK_W)) {
			cameraPosition.z -= CAMERA_MOVEMENT * dt;
		}

		cameraMatrix.identity().translate(cameraPosition).rotateY(cameraAngles.y).rotateX(cameraAngles.x);
		
		
		input.clear();
	}
	
	private Matrix4f cameraMatrix = new Matrix4f();
	private Matrix4f viewMatrix = new Matrix4f();
	private Matrix4f cubemapViewMatrix = new Matrix4f();
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

		cameraMatrix.invert(viewMatrix);
		cubemapViewMatrix.set3x3(viewMatrix); // view matrix with no translation
		projectionMatrix.setPerspective(CAMERA_FOVY, 1, CAMERA_NEAR, CAMERA_FAR);

		// draw the rest of the scale over the top
		mvpMatrix.set(projectionMatrix).mul(viewMatrix);
		root.draw(mvpMatrix);

		// draw the cubemap behind everything else
		mvpMatrix.set(projectionMatrix).mul(cubemapViewMatrix);
		cubemap.draw(mvpMatrix);
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
		new NormalMapDemo();
	}


}
