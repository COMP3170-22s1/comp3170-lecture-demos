package comp3170.demos.week6.backface;

import static com.jogamp.opengl.GL.GL_COLOR_BUFFER_BIT;

import java.awt.Color;
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
import comp3170.demos.week6.camera3d.cameras.Camera;
import comp3170.demos.week6.camera3d.cameras.PerspectiveCamera;
import comp3170.demos.week6.camera3d.sceneobjects.Axes;
import comp3170.demos.week6.camera3d.sceneobjects.Grid;

public class BackfaceCullingDemo extends JFrame implements GLEventListener {

	public static final float TAU = (float) (2 * Math.PI);		// https://tauday.com/tau-manifesto
	
	private int width = 800;
	private int height = 800;

	private GLCanvas canvas;
	private Shader shader;
	
	final private File DIRECTORY = new File("src/comp3170/demos/week6"); 
	final private String VERTEX_SHADER = "vertex.glsl";
	final private String FRAGMENT_SHADER = "fragment.glsl";

	private Animator animator;
	private long oldTime;
	private InputManager input;

	private Grid grid;
	private Triangle triangle;
	private Axes axes;

	private Camera camera;
	private Matrix4f viewMatrix = new Matrix4f();
	private Matrix4f projectionMatrix = new Matrix4f();
	private Matrix4f mvpMatrix = new Matrix4f();

	private boolean isCulling = false;
	private int cullFace = GL.GL_BACK;

	private SceneObject root;


	public BackfaceCullingDemo() {
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
		
		if (isCulling) {
			gl.glEnable(GL.GL_CULL_FACE);
		}
		else {
			gl.glDisable(GL.GL_CULL_FACE);			
		}
		
		gl.glCullFace(cullFace);
		

		shader = compileShader(VERTEX_SHADER, FRAGMENT_SHADER);
		
		// Set up the scene
		root = new SceneObject();
		grid = new Grid(shader, 11);
		grid.setParent(root);
		
		axes = new Axes(shader);
		axes.setParent(root);
		
		triangle = new Triangle(shader, Color.YELLOW);
		triangle.setParent(root);
		
		camera = new PerspectiveCamera(2, TAU/6, 1, 0.1f, 10f);		
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

	
	private void update() {
		GL4 gl = (GL4) GLContext.getCurrentGL();

		long time = System.currentTimeMillis();
		float deltaTime = (time-oldTime) / 1000f;
		oldTime = time;
		
		// enable/disable face culling
		if (input.wasKeyPressed(KeyEvent.VK_SPACE)) {
			isCulling = !isCulling;
			
			if (isCulling) {
				gl.glEnable(GL.GL_CULL_FACE);
			}
			else {
				gl.glDisable(GL.GL_CULL_FACE);
			}
		}

		// set which face to cull
		if (input.wasKeyPressed(KeyEvent.VK_F)) {
			gl.glCullFace(GL.GL_FRONT);
		}

		if (input.wasKeyPressed(KeyEvent.VK_B)) {
			gl.glCullFace(GL.GL_BACK);
		}

		camera.update(input, deltaTime);
		
		input.clear();
	}
	
	@Override
	public void display(GLAutoDrawable arg0) {
		GL4 gl = (GL4) GLContext.getCurrentGL();
		
		update();
		
        // clear the colour buffer
		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);		
		gl.glClear(GL_COLOR_BUFFER_BIT);		
		
		camera.getViewMatrix(viewMatrix);
		camera.getProjectionMatrix(projectionMatrix);
		
		// pre-multiply projetion and view matrices
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
		new BackfaceCullingDemo();
	}


}
