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
import comp3170.demos.week12.sceneobjects.Ground;
import comp3170.demos.week12.sceneobjects.Light;
import comp3170.demos.week12.sceneobjects.Quad;
import comp3170.demos.week12.sceneobjects.ShadowObject;

public class Shadows extends JFrame implements GLEventListener {

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

	private Shader depthShader;
	final private String DEPTH_VERTEX_SHADER = "depthVertex.glsl";
	final private String DEPTH_FRAGMENT_SHADER = "depthFragment.glsl";

	private Shader shadowShader;
	final private String SHADOW_VERTEX_SHADER = "shadowVertex.glsl";
	final private String SHADOW_FRAGMENT_SHADER = "shadowFragment.glsl";

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

	private SceneObject root;
	private SceneObject camera;
	private SceneObject cameraPivot;
	private SceneObject lightPivot;

	private int renderTexture;
	private int renderTextureSize = 1024;

	private int frameBuffer;

	private Quad renderQuad;

	private Light light;

	private Ground ground;

	private ShadowObject cube;

	public Shadows() {
		super("Shadows");

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
		
		// enable flags
		
		gl.glEnable(GL.GL_CULL_FACE);
		gl.glEnable(GL.GL_DEPTH_TEST);
		
		this.simpleShader = loadShader(SIMPLE_VERTEX_SHADER, SIMPLE_FRAGMENT_SHADER);
		this.textureShader = loadShader(TEXTURE_VERTEX_SHADER, TEXTURE_FRAGMENT_SHADER);
		this.coloursShader = loadShader(COLOURS_VERTEX_SHADER, COLOURS_FRAGMENT_SHADER);
		this.depthShader = loadShader(DEPTH_VERTEX_SHADER, DEPTH_FRAGMENT_SHADER);
		this.shadowShader = loadShader(SHADOW_VERTEX_SHADER, SHADOW_FRAGMENT_SHADER);

		// allocate matrices
		
		this.mvpMatrix = new Matrix4f();
		this.viewMatrix = new Matrix4f();
		this.projectionMatrix = new Matrix4f();
		
		// construct objects and attach to the scene-graph
		this.root = new SceneObject();
		
		// camera
		
		this.cameraPivot = new SceneObject();
		this.cameraPivot.setParent(this.root);

		this.camera = new SceneObject();
		this.camera.setParent(this.cameraPivot);
		this.camera.localMatrix.translate(0, cameraHeight, cameraDistance);

		// light
		
		this.lightPivot = new SceneObject();
		this.lightPivot.setParent(this.root);

		this.light = new Light();
		this.light.setParent(this.lightPivot);
		this.light.localMatrix.translate(0, lightDistance, 0);
		
		// objects in scenes
		
		this.ground = new Ground(depthShader);
		this.ground.setParent(this.root);
		this.ground.localMatrix.scale(4,4,4);
		this.ground.setLight(this.light);
		
		this.cube = new Cube(depthShader);
		this.cube.setParent(this.root);
		this.cube.localMatrix.translate(0,1,0);
		this.cube.setLight(this.light);
		
		int shadowBuffer = light.getShadowBuffer();
		try {
			this.frameBuffer = Shader.createFrameBuffer(shadowBuffer);
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

	private final float LIGHT_TURN = TAU / 30; // 1 orbit every 30 seconds
	private final float LIGHT_ZOOM = 1;
	private float lightDistance = 10;
	private float lightYaw = 0;
	private float lightPitch = 0;

	private void update(float dt) {


		// the light orbits the scene


		if (this.input.isKeyDown(KeyEvent.VK_W)) {
			this.lightPitch -= LIGHT_TURN * dt;
		}

		if (this.input.isKeyDown(KeyEvent.VK_S)) {
			this.lightPitch += LIGHT_TURN * dt;
		}

		if (this.input.isKeyDown(KeyEvent.VK_A)) {
			this.lightYaw -= LIGHT_TURN * dt;
		}

		if (this.input.isKeyDown(KeyEvent.VK_D)) {
			this.lightYaw += LIGHT_TURN * dt;
		}

		if (this.input.isKeyDown(KeyEvent.VK_Q)) {
			this.lightDistance += LIGHT_ZOOM * dt;
		}

		if (this.input.isKeyDown(KeyEvent.VK_E)) {
			this.lightDistance -= LIGHT_ZOOM * dt;
		}

		this.lightPivot.localMatrix.identity();
		this.lightPivot.localMatrix.rotateY(lightYaw);
		this.lightPivot.localMatrix.rotateX(lightPitch);
		this.light.localMatrix.setTranslation(0, 0, lightDistance);

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

		// adjust the view volume

		if (this.input.isKeyDown(KeyEvent.VK_NUMPAD1)) {
			this.cameraNear -= CAMERA_ZOOM * dt;
		}

		if (this.input.isKeyDown(KeyEvent.VK_NUMPAD4)) {
			this.cameraNear += CAMERA_ZOOM * dt;
		}

		if (this.input.isKeyDown(KeyEvent.VK_NUMPAD2)) {
			this.cameraFar -= CAMERA_ZOOM * dt;
		}

		if (this.input.isKeyDown(KeyEvent.VK_NUMPAD5)) {
			this.cameraFar += CAMERA_ZOOM * dt;
		}

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

		// Pass 1: Render from light's point of view

		gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, this.frameBuffer);
		gl.glViewport(0, 0, renderTextureSize, renderTextureSize);

		// set the background colour to black
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);

		// clear the depth buffer
		gl.glClearDepth(1);
		gl.glClear(GL.GL_DEPTH_BUFFER_BIT);

		// set the shaders
		this.cube.setShader(this.depthShader);
		this.ground.setShader(this.depthShader);
		
		// draw the objects in the scene graph recursively
		this.light.getLightMatrix(this.mvpMatrix);
		this.root.draw(mvpMatrix);

		// Pass 2: render to screen
		
		gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, 0);
		gl.glViewport(0, 0, this.screenWidth, this.screenHeight);

		// set the background colour to black
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);

		// clear the depth buffer
		gl.glClearDepth(1);
		gl.glClear(GL.GL_DEPTH_BUFFER_BIT);

		// set the view matrix
		this.camera.getWorldMatrix(this.viewMatrix);
		this.viewMatrix.invert();

		// set the projection matrix

		this.projectionMatrix.setPerspective(cameraFOVY, cameraAspect, cameraNear, cameraFar);

		// set the shaders
		this.cube.setShader(this.simpleShader);
		this.ground.setShader(this.simpleShader);
		
		// draw the objects in the scene graph recursively
		this.mvpMatrix.identity();
		this.mvpMatrix.mul(projectionMatrix);
		this.mvpMatrix.mul(viewMatrix);
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
		this.cameraAspect = (float)width / (float)height; 
	}

	@Override
	/**
	 * Called when we dispose of the canvas
	 */
	public void dispose(GLAutoDrawable drawable) {

	}

	public static void main(String[] args) throws IOException, GLException {
		new Shadows();
	}

}
