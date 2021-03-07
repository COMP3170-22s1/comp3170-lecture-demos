
package comp3170.demos.week4.camera;

import static com.jogamp.opengl.GL.GL_COLOR_BUFFER_BIT;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import org.joml.Matrix3f;

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
import comp3170.demos.week3.Square;

public class CameraDemo extends JFrame implements GLEventListener {

	public static final float TAU = (float) (2 * Math.PI);		// https://tauday.com/tau-manifesto
	
	private int width = 800;
	private int height = 800;

	private GLCanvas canvas;
	private Shader shader;
	
	final private File DIRECTORY = new File("src/comp3170/demos/week4/camera"); 
	final private String VERTEX_SHADER = "vertex.glsl";
	final private String FRAGMENT_SHADER = "fragment.glsl";

	private List<Square> squares;

	private Animator animator;
	private long oldTime;
	private InputManager input;

	private Camera camera;
	private boolean showCamera = true;
	private Matrix3f viewMatrix;
	private Matrix3f projectionMatrix;

	public CameraDemo() {
		super("Week 4 camera demo");

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
		
		// set up Input manager
		this.input = new InputManager();
		input.addListener(this);
		input.addListener(this.canvas);

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
	    
	    // Set up the camera

	    this.camera = new Camera(shader);
	    this.camera.setPosition(0,0);
	    this.camera.setAngle(0);
	    this.camera.setZoom(width * 2f);	// pixels per world unit
	    this.camera.setSize(width, height);
	    
	    // allocation view and projection matrices
	    viewMatrix = new Matrix3f();
	    projectionMatrix = new Matrix3f();
	}

	private static final float ROTATION_SPEED = TAU / 6;
	private static final float CAMERA_ROTATION_SPEED = TAU / 6;
	private static final float CAMERA_MOVEMENT_SPEED = 0.5f;
	private static final float CAMERA_ZOOM_SPEED = 1.5f;
	
	private void update() {
		long time = System.currentTimeMillis();
		float deltaTime = (time - oldTime) / 1000f;
		oldTime = time;
		
		for (Square sq : squares) {
			sq.rotate(ROTATION_SPEED * deltaTime);
		}
		
		if (input.isKeyDown(KeyEvent.VK_LEFT)) {
			camera.rotate(-CAMERA_ROTATION_SPEED * deltaTime);
		}

		if (input.isKeyDown(KeyEvent.VK_RIGHT)) {
			camera.rotate(CAMERA_ROTATION_SPEED * deltaTime);
		}

		if (input.isKeyDown(KeyEvent.VK_W)) {
			camera.translate(0, CAMERA_MOVEMENT_SPEED * deltaTime);
		}
		if (input.isKeyDown(KeyEvent.VK_S)) {
			camera.translate(0, -CAMERA_MOVEMENT_SPEED * deltaTime);
		}
		if (input.isKeyDown(KeyEvent.VK_A)) {
			camera.translate(-CAMERA_MOVEMENT_SPEED * deltaTime, 0);
		}
		if (input.isKeyDown(KeyEvent.VK_D)) {
			camera.translate(CAMERA_MOVEMENT_SPEED * deltaTime, 0);
		}
		if (input.isKeyDown(KeyEvent.VK_PAGE_UP)) {
			camera.zoom((float) Math.pow(1.0f / CAMERA_ZOOM_SPEED, deltaTime));
		}
		if (input.isKeyDown(KeyEvent.VK_PAGE_DOWN)) {
			camera.zoom((float) Math.pow(CAMERA_ZOOM_SPEED, deltaTime));
		}

		if (input.wasKeyPressed(KeyEvent.VK_SPACE)) {
			showCamera = !showCamera;
		}
		input.clear();		
	}
	
	@Override
	public void display(GLAutoDrawable arg0) {
		GL4 gl = (GL4) GLContext.getCurrentGL();
		
		// update the scene
		update();	

        // clear the colour buffer
		gl.glClear(GL_COLOR_BUFFER_BIT);		

		// activate the shader
		this.shader.enable();		
		
		
		if (showCamera) {
			viewMatrix.identity();
			projectionMatrix.identity();
		}
		else {
			camera.setSize(width, height);
			camera.getViewMatrix(viewMatrix);
			camera.getProjectionMatrix(projectionMatrix);
		}
		
		shader.setUniform("u_viewMatrix", viewMatrix);
		shader.setUniform("u_projectionMatrix", projectionMatrix);
		
		// draw the squares
		for (Square sq : squares) {
			sq.draw(shader);
		}
		
		if (showCamera) {
			// draw the camera rectangle
			camera.draw(shader);			
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
		new CameraDemo();
	}


}
