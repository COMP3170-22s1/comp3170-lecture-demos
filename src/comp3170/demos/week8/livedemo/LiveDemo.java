
package comp3170.demos.week8.livedemo;

import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
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
import comp3170.Shader;
import comp3170.demos.SceneObject;

public class LiveDemo extends JFrame implements GLEventListener {

	public static final float TAU = (float) (2 * Math.PI); // https://tauday.com/tau-manifesto

	private int width = 800;
	private int height = 800;

	private GLCanvas canvas;
	private Shader shader;

	final private File DIRECTORY = new File("src/comp3170/demos/week8/livedemo");
	final private String VERTEX_SHADER = "vertex.glsl";
	final private String FRAGMENT_SHADER = "fragment.glsl";

	private Animator animator;
	private long oldTime;
	private InputManager input;

	private SceneObject root;

	private Icosahedron icosahedron;

	public LiveDemo() {
		super("Week 5 live demo");

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

		setSize(width, height);
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
		
		// enable backface culling
//		gl.glEnable(GL.GL_CULL_FACE);
		gl.glCullFace(GL.GL_BACK);	// default
		
		// enable depth testing
		gl.glEnable(GL.GL_DEPTH_TEST);
//		gl.glDepthFunc(GL.GL_LEQUAL);
		gl.glDepthFunc(GL.GL_LESS);	// default
		
		// enable transparency
		gl.glEnable(GL.GL_BLEND);
		// c = a * c_src + (1-a) c_dest
		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
		
		shader = compileShader(VERTEX_SHADER, FRAGMENT_SHADER);
		
		root = new SceneObject();
		
		icosahedron = new Icosahedron();
		icosahedron.setParent(root);
	}

	private Shader compileShader(String vertex, String fragement) {
		// Compile the shader
		try {
			File vertexShader = new File(DIRECTORY, vertex);
			File fragementShader = new File(DIRECTORY, fragement);
			return new Shader(vertexShader, fragementShader);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (GLException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return null;

	}

	private float cameraAngle = 0;
	private float cameraDistance = 2;

	// perspective camera
	private float cameraFOVY = TAU / 6;
	private float cameraAspect = 1;

	private static final float CAMERA_ROTATION = TAU / 6;
	private static final float CAMERA_MOVEMENT = 2;
	private static final float CAMERA_D_FOVY = TAU / 6;

	private void update() {
		long time = System.currentTimeMillis();
		float dt = (time - oldTime) / 1000f;
		oldTime = time;

		if (input.isKeyDown(KeyEvent.VK_LEFT)) {
			cameraAngle -= CAMERA_ROTATION * dt;
		}
		if (input.isKeyDown(KeyEvent.VK_RIGHT)) {
			cameraAngle += CAMERA_ROTATION * dt;
		}
		if (input.isKeyDown(KeyEvent.VK_UP)) {
			cameraDistance -= CAMERA_MOVEMENT * dt;
		}
		if (input.isKeyDown(KeyEvent.VK_DOWN)) {
			cameraDistance += CAMERA_MOVEMENT * dt;
		}
		if (input.isKeyDown(KeyEvent.VK_Z)) {
			cameraFOVY += CAMERA_D_FOVY * dt;
		}
		if (input.isKeyDown(KeyEvent.VK_X)) {
			cameraFOVY -= CAMERA_D_FOVY * dt;
		}

		icosahedron.update(dt, input);
	}

	// pre-allocate matrices
	private Matrix4f viewMatrix = new Matrix4f();
	private Matrix4f projectionMatrix = new Matrix4f();

	private static final float CAMERA_NEAR = 1;
	private static final float CAMERA_FAR = 10;
	// orthographic camera
	private static final float CAMERA_WIDTH = 5;
	private static final float CAMERA_HEIGHT = 5;

	@Override
	public void display(GLAutoDrawable arg0) {
		GL4 gl = (GL4) GLContext.getCurrentGL();

		update();

		// set the background colour to black
		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);
		// clear the depth buffer to 1
		gl.glClearDepth(1);
		gl.glClear(GL.GL_DEPTH_BUFFER_BIT);

		// the view matrix is the inverse of the camera model matrix
		viewMatrix.identity();
		viewMatrix.translate(0, 0, cameraDistance);
		viewMatrix.rotateZ(cameraAngle);
		viewMatrix.invert();

//		projectionMatrix.setOrtho(
//				-CAMERA_WIDTH / 2, CAMERA_WIDTH / 2, 
//				-CAMERA_HEIGHT / 2, CAMERA_HEIGHT / 2, 
//				CAMERA_NEAR, CAMERA_FAR);

		projectionMatrix.setPerspective(cameraFOVY, cameraAspect, CAMERA_NEAR, CAMERA_FAR);

		// activate the shader
		shader.enable();
		shader.setUniform("u_viewMatrix", viewMatrix);
		shader.setUniform("u_projectionMatrix", projectionMatrix);
		
		// draw the back faces
		gl.glEnable(GL.GL_CULL_FACE);
		gl.glCullFace(GL.GL_FRONT);	// default

		// draw the scene
		root.draw(shader);

		gl.glCullFace(GL.GL_BACK);	// default

		// draw the scene
		root.draw(shader);

	}

	@Override
	public void reshape(GLAutoDrawable d, int x, int y, int width, int height) {
		this.width = width;
		this.height = height;

		// set the camera aspect equal to the window aspect
		// note: make sure this is a float division, not an integer division
		cameraAspect = (float) width / height;
	}

	@Override
	public void dispose(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) {
		new LiveDemo();
	}

}
