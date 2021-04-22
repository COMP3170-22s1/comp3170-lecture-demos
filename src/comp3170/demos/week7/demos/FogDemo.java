package comp3170.demos.week7.demos;

import static com.jogamp.opengl.GL.GL_COLOR_BUFFER_BIT;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;

import org.joml.Matrix4f;
import org.joml.Vector4f;

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

	public FogDemo() {
		super("Week 6 3D camera demo");

		// set up a GL canvas
		GLProfile profile = GLProfile.get(GLProfile.GL4);		 
		GLCapabilities capabilities = new GLCapabilities(profile);
		this.canvas = new GLCanvas(capabilities);
		this.canvas.addGLEventListener(this);
		this.add(canvas);
		
		// set up Animator		

		this.animator = new Animator(canvas);
		this.animator.start();
		this.oldTime = System.currentTimeMillis();		

		// input
		
		this.input = new InputManager(canvas);
		
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
		
		gl.glEnable(GL.GL_DEPTH_TEST);	

		// set the background colour to black
		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		
		// Set up the scene
		this.grid = new Grid(11);
		grid.setAngle(0, 0, 0);
		grid.setPosition(0,0,0);
		
		this.axes = new Axes();
		
		this.cubes = new Cube[] {
			new Cube(),
			new Cube(),
			new Cube(),
		};
		
		cubes[0].setPosition(0.7f, 0.05f, -0.3f);
		cubes[0].setScale(0.05f);
		cubes[0].setColour(Color.RED);

		cubes[1].setPosition(-0.5f, 0.05f, 0.3f);
		cubes[1].setScale(0.05f);
		cubes[1].setColour(Color.BLUE);

		cubes[2].setPosition(0.1f, 0.05f, 0.1f);
		cubes[2].setScale(0.05f);
		cubes[2].setColour(Color.GREEN);

		this.camera = new PerspectiveCamera(input, TAU/6, 1, 0.1f, 10f);
		camera.setDistance(2);
		camera.setHeight(0);
	}

	
	private void update() {
		long time = System.currentTimeMillis();
		float deltaTime = (time-oldTime) / 1000f;
		oldTime = time;
		
		camera.update(deltaTime);		
		input.clear();
	}
	
	@Override
	public void display(GLAutoDrawable arg0) {
		GL4 gl = (GL4) GLContext.getCurrentGL();
		
		update();
		
        // clear the colour & depth buffers
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);		
		gl.glClear(GL.GL_DEPTH_BUFFER_BIT);				
				
		// draw the scene
		this.grid.draw(camera);
		this.axes.draw(camera);
		for (int i = 0; i < cubes.length; i++) {
			cubes[i].draw(camera);
		}
		
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
