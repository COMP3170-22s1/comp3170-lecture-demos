package comp3170.demos.week12;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;

import org.joml.Matrix3f;
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
import comp3170.SceneObject;
import comp3170.Shader;
import comp3170.demos.week10.sceneobjects.Plane;
import comp3170.demos.week12.sceneobjects.Cube;
import comp3170.demos.week12.sceneobjects.Quad;

public class Reflection extends JFrame implements GLEventListener {

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


	private Quad mirror;
	private int mirrorSize = 1024;
	private int mirrorTexture;
	private int frameBuffer;

	private SceneObject mirrorCamera;
	private Vector3f cameraPosition;
	private Vector3f mirrorPosition;
	private Vector3f mirrorNormal;
	private Matrix3f mirrorMatrix;


	public Reflection() {
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
		this.input = new InputManager(canvas);

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

		// objects in the scene
		
		Plane plane = new Plane(simpleShader, 10);
		plane.setParent(this.scene);
		plane.localMatrix.scale(5,5,5);

		Cube redCube = new Cube(simpleShader);
		redCube.setParent(this.scene);
		redCube.setColour(Color.red);
		redCube.localMatrix.translate(-3, 1, 3);

		Cube yellowCube = new Cube(simpleShader);
		yellowCube.setParent(this.scene);
		yellowCube.setColour(Color.yellow);
		yellowCube.localMatrix.translate(-5, 1, 2);

		// the mirror
		
		this.mirrorTexture = Shader.createRenderTexture(mirrorSize, mirrorSize);
		this.mirror = new Quad(textureShader, mirrorTexture);
		this.mirror.setParent(this.scene);
		this.mirror.localMatrix.translate(0, this.mirrorHeight, 0);
		this.mirror.localMatrix.rotateY(this.mirrorYaw);
		
		this.mirrorCamera = new SceneObject();
		this.mirrorCamera.setParent(this.scene);
		Cube cameraCube = new Cube(this.simpleShader);
		cameraCube.setParent(this.mirrorCamera);
		cameraCube.localMatrix.scale(0.2f, 0.2f, 0.2f);
		
		this.cameraPosition = new Vector3f();
		this.mirrorPosition = new Vector3f();
		this.mirrorNormal = new Vector3f();
		this.mirrorMatrix = new Matrix3f();
		
		// set up render texture & framebuffer
		
		try {
			this.frameBuffer = Shader.createFrameBuffer(this.mirrorTexture);
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

	private final float MIRROR_TURN = TAU / 8;
	private float mirrorHeight = 1;
	private float mirrorYaw = 0;

	private void update(float dt) {

		// turn the mirror

		if (this.input.isKeyDown(KeyEvent.VK_A)) {
			this.mirrorYaw -= MIRROR_TURN * dt;
		}

		if (this.input.isKeyDown(KeyEvent.VK_D)) {
			this.mirrorYaw += MIRROR_TURN * dt;
		}

		this.mirror.localMatrix.identity();
		this.mirror.localMatrix.translate(0, this.mirrorHeight, 0);
		this.mirror.localMatrix.rotateY(this.mirrorYaw);

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

		// reflect the mirror camera position in the mirror
		
		this.camera.getPosition(this.cameraPosition);
		this.mirror.getPosition(this.mirrorPosition);
		this.cameraPosition.sub(this.mirrorPosition);

		this.mirror.getNormalMatrix(this.mirrorMatrix);
		this.mirrorNormal.set(0,0,1).mul(mirrorMatrix);
		this.mirrorNormal.normalize();
		
		this.cameraPosition.reflect(this.mirrorNormal);
		this.mirrorCamera.localMatrix.setTranslation(this.cameraPosition);
		
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

		// Pass 1: Render from mirror camera's point of view


		// Pass 2: Render from the camera's point of view
		
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

		// draw the objects in the scene graph recursively
		this.mvpMatrix.identity();
		this.mvpMatrix.mul(projectionMatrix);
		this.mvpMatrix.mul(viewMatrix);
		this.scene.draw(mvpMatrix);

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
		new Reflection();
	}

}
