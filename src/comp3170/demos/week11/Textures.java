package comp3170.demos.week11;

import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;

import org.joml.Matrix4f;
import org.joml.Vector3f;
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
import comp3170.SceneObject;
import comp3170.Shader;
import comp3170.demos.week9.sceneobjects.Axes;
import comp3170.demos.week9.sceneobjects.Cylinder;
import comp3170.demos.week9.sceneobjects.Light;
import comp3170.demos.week9.sceneobjects.Plane;

public class Textures extends JFrame implements GLEventListener {

	private GLCanvas canvas;

	final private float TAU = (float) (Math.PI * 2);
	
	final private File TEXTURE_DIRECTORY = new File("src/comp3170/demos/week11/textures");
	final private File SHADER_DIRECTORY = new File("src/comp3170/demos/week11/shaders");
	
	// simple uniform colour shader
	private Shader simpleShader;
	final private String SIMPLE_VERTEX_SHADER = "simpleVertex.glsl";
	final private String SIMPLE_FRAGMENT_SHADER = "simpleFragment.glsl";
		
	private Matrix4f mvpMatrix;
	private Matrix4f viewMatrix;
	private Matrix4f projectionMatrix;
	private Matrix4f lightMatrix;
	
	// screen size in pixels
	private int screenWidth = 1000;
	private int screenHeight = 1000;
	
	private InputManager input;
	private Animator animator;
	private long oldTime;

	private SceneObject root;	
	private SceneObject camera;
	private SceneObject cameraPivot;
	private Cylinder cylinderBottom;
	private Cylinder cylinderTop;

	public Textures() {
		super("COMP3170 Week 11 Textures");
		
		// create an OpenGL 4 canvas and add this as a listener
		// enabling full-screen super-sampled anti-aliasing		
		
		GLProfile profile = GLProfile.get(GLProfile.GL4);		 
		GLCapabilities capabilities = new GLCapabilities(profile);
		capabilities.setSampleBuffers(true);
		capabilities.setNumSamples(4);

		this.canvas = new GLCanvas(capabilities);
		this.canvas.addGLEventListener(this);
		this.add(canvas);
		
		// set up Animator		
		this.animator = new Animator(canvas);
		this.animator.start();
		this.oldTime = System.currentTimeMillis();
				
		// set up Input manager
		this.input = new InputManager();
		input.addListener(this);
		input.addListener(this.canvas);
		
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
		
		// Compile the shaders
		this.simpleShader = loadShader(SIMPLE_VERTEX_SHADER, SIMPLE_FRAGMENT_SHADER);
		
		// allocate matrices
		
		this.mvpMatrix = new Matrix4f();
		this.viewMatrix = new Matrix4f();
		this.projectionMatrix = new Matrix4f();
		this.lightMatrix = new Matrix4f();
		
		// construct objects and attach to the scene-graph
		this.root = new SceneObject();
		
		Plane plane = new Plane(simpleShader, 10);
		plane.setParent(this.root);
		plane.localMatrix.scale(5,5,5);
		
		// camera and light
		
		this.cameraPivot = new SceneObject();
		this.cameraPivot.setParent(this.root);

		this.camera = new SceneObject();
		this.camera.setParent(this.cameraPivot);
		this.camera.localMatrix.translate(0, cameraHeight, cameraDistance);

	}
	
	/**
	 * Load and compile a vertex shader and fragment shader 
	 * 
	 * @param vs	The name of the vertex shader
	 * @param fs	The name of the fragment shader
	 * @return
	 */
	private Shader loadShader(String vs, String fs) {		
		try {
			File vertexShader = new File(SHADER_DIRECTORY, vs);
			File fragmentShader = new File(SHADER_DIRECTORY, fs);
			return new Shader(vertexShader, fragmentShader);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (GLException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		// Unreachable
		return null;
	}

	private final float CAMERA_TURN = TAU/8;	
	private final float CAMERA_ZOOM = 1;	
	private float cameraYaw = 0;
	private float cameraPitch = 0;
	private float cameraDistance = 10;
	private float cameraHeight = 1;

	private float cameraFOV = 4;
	private float cameraAspect = (float)screenWidth / screenHeight;
	private float cameraNear = 0.1f;
	private float cameraFar = 20.0f;
	private Vector4f viewDir = new Vector4f();
	
	private float lightDistance = 5;
	private float lightYaw = TAU/4;
	private float lightPitch = 0;
	private Vector4f lightDir = new Vector4f();
	
	public void update(float dt) {

		// rotate the camera 
		
		if (this.input.isKeyDown(KeyEvent.VK_UP)) {
			this.cameraPitch -= CAMERA_TURN * dt;
		}
		
		if (this.input.isKeyDown(KeyEvent.VK_DOWN)) {
			this.cameraPitch += CAMERA_TURN * dt;
		}
		
		if (this.input.isKeyDown(KeyEvent.VK_LEFT)) {
			this.cameraYaw -= CAMERA_TURN * dt;
		}
		
		if (this.input.isKeyDown(KeyEvent.VK_RIGHT)) {
			this.cameraYaw += CAMERA_TURN * dt;
		}
		
		if (this.input.isKeyDown(KeyEvent.VK_PAGE_UP)) {
			this.cameraFOV -= CAMERA_ZOOM * dt;
		}
		
		if (this.input.isKeyDown(KeyEvent.VK_PAGE_DOWN)) {
			this.cameraFOV += CAMERA_ZOOM * dt;
		}

		this.cameraPivot.localMatrix.identity();
		this.cameraPivot.localMatrix.rotateY(cameraYaw);
		this.cameraPivot.localMatrix.rotateX(cameraPitch);

		// calculate the view direction
		// note: this assumes the camera pivot is at the root

		this.viewDir.set(0,0,-1,0);
		this.camera.getWorldMatrix(this.viewMatrix);
		this.viewDir.mul(this.viewMatrix);
		this.cylinderTop.setViewDir(viewDir);
		this.cylinderBottom.setViewDir(viewDir);
		
		// rotate the light 

		if (this.input.isKeyDown(KeyEvent.VK_W)) {
			this.lightPitch -= CAMERA_TURN * dt;
		}
		
		if (this.input.isKeyDown(KeyEvent.VK_S)) {
			this.lightPitch += CAMERA_TURN * dt;
		}
		
		if (this.input.isKeyDown(KeyEvent.VK_A)) {
			this.lightYaw -= CAMERA_TURN * dt;
		}
		
		if (this.input.isKeyDown(KeyEvent.VK_D)) {
			this.lightYaw += CAMERA_TURN * dt;
		}

		this.lightPivot.localMatrix.identity();
		this.lightPivot.localMatrix.rotateY(lightYaw);
		this.lightPivot.localMatrix.rotateX(lightPitch);

		// calculate the light direction
		
		this.lightDir.set(0,0,1,0);
		this.light.getWorldMatrix(this.lightMatrix);
		this.lightDir.mul(this.lightMatrix);
		this.cylinderTop.setLightDir(this.lightDir);
		this.cylinderBottom.setLightDir(this.lightDir);

		input.clear();
	}
	
	
	@Override
	/**
	 * Called when the canvas is redrawn
	 */
	public void display(GLAutoDrawable drawable) {
		GL4 gl = (GL4) GLContext.getCurrentGL();

		// compute time since last frame
		long time = System.currentTimeMillis();
		float dt = (time - oldTime) / 1000.0f;
		oldTime = time;
		update(dt);

		gl.glViewport(0, 0, screenWidth, screenHeight);
		
		// set the background colour to black
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);		
		
		// clear the depth buffer
		gl.glClearDepth(1f);
		gl.glClear(GL.GL_DEPTH_BUFFER_BIT);		
				
		// set the view matrix		
		this.camera.getWorldMatrix(this.viewMatrix);
		this.viewMatrix.invert();
		
		// set the projection matrix
		float width = cameraAspect * cameraFOV;
		this.projectionMatrix.setOrtho(-width/2, width/2, -cameraFOV/2, cameraFOV/2, cameraNear, cameraFar);
		
		// draw the objects in the scene graph recursively
		this.mvpMatrix.identity();
		this.mvpMatrix.mul(this.projectionMatrix);
		this.mvpMatrix.mul(this.viewMatrix);

		this.root.draw(mvpMatrix);
	}

	@Override
	/**
	 * Called when the canvas is resized
	 */
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		GL4 gl = (GL4) GLContext.getCurrentGL();
		
		this.screenWidth = width;
		this.screenHeight = height;
		cameraAspect= ((float)width) / height;
		
	}

	@Override
	/**
	 * Called when we dispose of the canvas 
	 */
	public void dispose(GLAutoDrawable drawable) {

	}

	public static void main(String[] args) throws IOException, GLException {
		new Textures();
	}


}
