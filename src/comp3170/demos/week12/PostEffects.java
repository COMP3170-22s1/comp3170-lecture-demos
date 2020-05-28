package comp3170.demos.week12;

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
import comp3170.SceneObject;
import comp3170.Shader;
import comp3170.demos.week12.sceneobjects.Cube;
import comp3170.demos.week12.sceneobjects.Light;
import comp3170.demos.week12.sceneobjects.Plane;
import comp3170.demos.week12.sceneobjects.Quad;

public class PostEffects extends JFrame implements GLEventListener {

	final private float TAU = (float) (Math.PI * 2);

	private GLCanvas canvas;

	final private File SHADER_DIRECTORY = new File("src/comp3170/demos/week12/shaders");

	private Shader simpleShader;
	final private String SIMPLE_VERTEX_SHADER = "simpleVertex.glsl";
	final private String SIMPLE_FRAGMENT_SHADER = "simpleFragment.glsl";

	private Shader textureShader;
	final private String TEXTURE_VERTEX_SHADER = "textureVertex.glsl";
	final private String TEXTURE_FRAGMENT_SHADER = "textureFragment.glsl";

	private Shader coloursShader;
	final private String COLOURS_VERTEX_SHADER = "coloursVertex.glsl";
	final private String COLOURS_FRAGMENT_SHADER = "coloursFragment.glsl";

	private Shader greyscaleShader;
	final private String GREYSCALE_VERTEX_SHADER = "greyscaleVertex.glsl";
	final private String GREYSCALE_FRAGMENT_SHADER = "greyscaleFragment.glsl";

	private Shader blurShader;
	final private String BLUR_VERTEX_SHADER = "blurVertex.glsl";
	final private String BLUR_FRAGMENT_SHADER = "blurFragment.glsl";


	private Matrix4f mvpMatrix;
	private Matrix4f viewMatrix;
	private Matrix4f projectionMatrix;

	// screen size in pixels
	private int screenWidth = 1000;
	private int screenHeight = 1000;

	private InputManager input;
	private Animator animator;
	private long oldTime;

	// Scene objects

	private SceneObject scene;
	private SceneObject camera;
	private SceneObject cameraPivot;

	// render to buffer
	
	private int frameBuffer;
	private Quad renderQuad;
	private int renderSize = 1000;

	public PostEffects() {
		super("PostEffects");

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
		
		// enable flags
		
		gl.glEnable(GL.GL_CULL_FACE);
		gl.glEnable(GL.GL_DEPTH_TEST);
		
		this.simpleShader = loadShader(SIMPLE_VERTEX_SHADER, SIMPLE_FRAGMENT_SHADER);
		this.textureShader = loadShader(TEXTURE_VERTEX_SHADER, TEXTURE_FRAGMENT_SHADER);
		this.coloursShader = loadShader(COLOURS_VERTEX_SHADER, COLOURS_FRAGMENT_SHADER);
		this.greyscaleShader = loadShader(GREYSCALE_VERTEX_SHADER, GREYSCALE_FRAGMENT_SHADER);
		this.blurShader = loadShader(BLUR_VERTEX_SHADER, BLUR_FRAGMENT_SHADER);

		// allocate matrices
		
		this.mvpMatrix = new Matrix4f();
		this.viewMatrix = new Matrix4f();
		this.projectionMatrix = new Matrix4f();
		
		// construct objects and attach to the scene-graph
		this.scene = new SceneObject();
			
		// camera
		
		this.cameraPivot = new SceneObject();
		this.cameraPivot.setParent(this.scene);

		this.camera = new SceneObject();
		this.camera.setParent(this.cameraPivot);
		this.camera.localMatrix.translate(0, cameraHeight, cameraDistance);

		// objects in both scenes
		
		Plane plane = new Plane(simpleShader, 10);
		plane.setParent(this.scene);
		plane.localMatrix.scale(5,5,5);
		
		Cube cube1 = new Cube(simpleShader);
		cube1.setParent(this.scene);
		cube1.localMatrix.translate(0,1,0);

		// set up framebuffer that renders to a renderTexture
		// and a quad with the post-effect shader
		
		try {
			int renderTexture = Shader.createRenderTexture(renderSize, renderSize);
			this.renderQuad = new Quad(blurShader, renderTexture); 
			this.frameBuffer = Shader.createFrameBuffer(renderTexture);
		} catch (GLException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

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
		
		return null;
	}

	private final float CAMERA_TURN = TAU / 8;
	private final float CAMERA_ZOOM = 1;
	private float cameraYaw = 0;
	private float cameraPitch = 0;
	private float cameraDistance = 5;
	private float cameraHeight = 1;

	private float cameraFOVY = TAU / 6;
	private float cameraAspect = (float) screenWidth / screenHeight;
	private float cameraNear = 1.0f;
	private float cameraFar = 40.0f;


	private void update(float dt) {

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
			this.cameraDistance -= CAMERA_ZOOM * dt;
		}

		if (this.input.isKeyDown(KeyEvent.VK_PAGE_DOWN)) {
			this.cameraDistance += CAMERA_ZOOM * dt;
		}

		this.cameraPivot.localMatrix.identity();
		this.cameraPivot.localMatrix.rotateY(cameraYaw);
		this.cameraPivot.localMatrix.rotateX(cameraPitch);
		this.camera.localMatrix.identity();
		this.camera.localMatrix.translate(0, cameraHeight, cameraDistance);

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

		// Pass 1: Render scene to texture

		gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, this.frameBuffer);
		gl.glViewport(0, 0, this.renderSize, this.renderSize);

		// set the background colour to black
		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);

		// clear the depth buffer
		gl.glClearDepth(1);
		gl.glClear(GL.GL_DEPTH_BUFFER_BIT);

		// set the view matrix
		this.camera.getWorldMatrix(this.viewMatrix);
		this.viewMatrix.invert();
		this.projectionMatrix.setPerspective(cameraFOVY, cameraAspect, cameraNear, cameraFar);
		
		this.mvpMatrix.identity();
		this.mvpMatrix.mul(projectionMatrix);
		this.mvpMatrix.mul(viewMatrix);
		this.scene.draw(mvpMatrix);

		// Pass 2: quad to screen
		
		gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, 0);
		gl.glViewport(0, 0, this.screenWidth, this.screenHeight);

		// set the background colour to black
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);

		// clear the depth buffer
		gl.glClearDepth(1);
		gl.glClear(GL.GL_DEPTH_BUFFER_BIT);

		this.mvpMatrix.identity();
		this.renderQuad.draw(mvpMatrix);

	}

	@Override
	/**
	 * Called when the canvas is resized
	 */
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		GL4 gl = (GL4) GLContext.getCurrentGL();

		this.screenWidth = width;
		this.screenHeight = height;
		this.cameraAspect = (float)width / (float)height; 
	}

	@Override
	/**
	 * Called when we dispose of the canvas
	 */
	public void dispose(GLAutoDrawable drawable) {

	}

	public static void main(String[] args) throws IOException, GLException {
		new PostEffects();
	}

}
