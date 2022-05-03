package comp3170.demos.week7.demos;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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

import comp3170.InputManager;
import comp3170.demos.SceneObject;
import comp3170.demos.week7.cameras.PerspectiveCamera;
import comp3170.demos.week7.sceneobjects.Axes;
import comp3170.demos.week7.sceneobjects.Cube;
import comp3170.demos.week7.sceneobjects.Grid;

public class FogDemo extends JFrame implements GLEventListener {

	public static final float TAU = (float) (2 * Math.PI);		// https://tauday.com/tau-manifesto
	
	private int width = 800;
	private int height = 800;

	private GLCanvas canvas;
	
	private Animator animator;
	private long oldTime;
	private InputManager input;

	private Grid grid;
	private Cube[] cubes;
	private Axes axes;
	private PerspectiveCamera camera;

	private SceneObject root;

	public FogDemo() {
		super("Week 6 3D camera demo");

		// set up a GL canvas
		GLProfile profile = GLProfile.get(GLProfile.GL4);		 
		GLCapabilities capabilities = new GLCapabilities(profile);
		canvas = new GLCanvas(capabilities);
		canvas.addGLEventListener(this);
		add(canvas);
		
		// set up Animator		

		animator = new Animator(canvas);
		animator.start();
		oldTime = System.currentTimeMillis();		

		// input
		
		input = new InputManager(canvas);
		
		// set up the JFrame
		
		setSize(width,height);
		setVisible(true);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}

	@Override
	public void init(GLAutoDrawable arg0) {
		GL4 gl = (GL4) GLContext.getCurrentGL();
		
		gl.glEnable(GL.GL_DEPTH_TEST);	
		
		root = new SceneObject();
		
		// Set up the scene
		Grid grid = new Grid(11);
		grid.setParent(root);
		
		axes = new Axes();
		axes.setParent(root);

		camera = new PerspectiveCamera(input, TAU/6, 1, 0.1f, 10f);
		camera.setDistance(2);
		camera.setHeight(0);

		cubes = new Cube[3];
		for (int i = 0; i < 3; i++) {
			cubes[i] = new Cube(camera);
			cubes[i].setParent(grid);
		}
		
		
		cubes[0].getMatrix().translate(0.7f, 0.05f, -0.3f).scale(0.05f);
		cubes[0].setColour(Color.RED);

		cubes[1].getMatrix().translate(-0.5f, 0.05f, 0.3f).scale(0.05f);
		cubes[1].setColour(Color.BLUE);

		cubes[2].getMatrix().translate(0.1f, 0.05f, 0.1f).scale(0.05f);
		cubes[2].setColour(Color.GREEN);

	}

	
	private void update() {
		long time = System.currentTimeMillis();
		float deltaTime = (time-oldTime) / 1000f;
		oldTime = time;
		
		camera.update(deltaTime);		
		input.clear();
	}
	
	private Matrix4f viewMatrix = new Matrix4f();
	private Matrix4f projectionMatrix = new Matrix4f();
	private Matrix4f mvpMatrix = new Matrix4f();
	
	@Override
	public void display(GLAutoDrawable arg0) {
		GL4 gl = (GL4) GLContext.getCurrentGL();
		
		update();
		
        // clear the colour & depth buffers
		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);		
		gl.glClear(GL.GL_DEPTH_BUFFER_BIT);				
				
		// pre-multiply projetion and view matrices
		camera.getViewMatrix(viewMatrix);
		camera.getProjectionMatrix(projectionMatrix);		
		mvpMatrix.set(projectionMatrix).mul(viewMatrix);
			
		// draw the scene
		root.draw(mvpMatrix);		
	}

	@Override
	public void reshape(GLAutoDrawable d, int x, int y, int width, int height) {
		this.width = width;
		this.height = height;		
	}

	@Override
	public void dispose(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args) { 
		new FogDemo();
	}


}
