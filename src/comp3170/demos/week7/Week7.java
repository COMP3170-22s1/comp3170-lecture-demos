package comp3170.demos.week7;

import static com.jogamp.opengl.GL.GL_COLOR_BUFFER_BIT;

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
import comp3170.demos.week7.sceneobjects.Cube;
import comp3170.demos.week7.sceneobjects.Plane;
import comp3170.demos.week7.sceneobjects.Triangle;

public class Week7 extends JFrame implements GLEventListener {

	private GLCanvas canvas;
	private Shader shader;

	final private float TAU = (float) (Math.PI * 2);
	
	final private File DIRECTORY = new File("src/comp3170/demos/week7"); 
	final private String VERTEX_SHADER = "vertex.glsl";
	final private String FRAGMENT_SHADER = "fragment.glsl";
	
	private SceneObject root;	
	private Matrix4f worldMatrix;
	private Matrix4f viewMatrix;
	private Matrix4f projectionMatrix;
	
	// screen size in pixels
	private int screenWidth = 1000;
	private int screenHeight = 1000;
	
	private InputManager input;
	private SceneObject camera;
	private Animator animator;
	private long oldTime;
	private SceneObject cameraPivot;
	
	public Week7() {
		super("COMP3170 Week 7");
		
		// create an OpenGL 4 canvas and add this as a listener
		// enabling full-screen super-sampled anti-aliasing		
		
		GLProfile profile = GLProfile.get(GLProfile.GL4);		 
		GLCapabilities capabilities = new GLCapabilities(profile);
		capabilities.setSampleBuffers(true);
		capabilities.setNumSamples(16);

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
		
		// enable backface culling
		
		gl.glEnable(GL.GL_CULL_FACE);  
		gl.glCullFace(GL.GL_BACK);	// cull the back face
		gl.glFrontFace(GL.GL_CCW);	// the front face has CCW winding order
		
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
		
		// allocate matrices
		
		this.worldMatrix = new Matrix4f();
		this.viewMatrix = new Matrix4f();
		this.projectionMatrix = new Matrix4f();
		
		// construct objects and attach to the scene-graph
		this.root = new SceneObject();
		
		Plane plane = new Plane(shader, 10);
		plane.setParent(this.root);
		plane.localMatrix.scale(5,5,5);
		
		Triangle triangle = new Triangle(shader);
		triangle.setParent(this.root);;

//		Cube cube = new Cube(shader);
//		cube.setParent(this.root);
		
		// camera rectangle
		
		this.cameraPivot = new SceneObject();
		this.cameraPivot.setParent(this.root);

		this.camera = new SceneObject();
		this.camera.setParent(this.cameraPivot);
		this.camera.localMatrix.translate(0, 0, 5);
		
	}
	
	private final float CAMERA_TURN = TAU/8;	
	private final float CAMERA_ZOOM = 1;	
	private float cameraYaw = 0;
	private float cameraPitch = 0;
	private float cameraDistance = 5;
	
	
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
		
		if (this.input.isKeyDown(KeyEvent.VK_PAGE_DOWN)) {
			this.cameraDistance -= CAMERA_ZOOM * dt;
		}
		
		if (this.input.isKeyDown(KeyEvent.VK_PAGE_UP)) {
			this.cameraDistance += CAMERA_ZOOM * dt;
		}

		this.cameraPivot.localMatrix.identity();
		this.cameraPivot.localMatrix.rotateY(cameraYaw);
		this.cameraPivot.localMatrix.rotateX(cameraPitch);
		this.camera.localMatrix.identity();
		this.camera.localMatrix.translate(0, 0, cameraDistance);
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
		gl.glClear(GL_COLOR_BUFFER_BIT);		
		
		this.shader.enable();

		// set the view matrix		
		this.camera.getWorldMatrix(this.viewMatrix);
		this.viewMatrix.invert();
		shader.setUniform("u_viewMatrix", this.viewMatrix);
		
		// set the projection matrix

		float fovy = TAU / 6;
		float aspect = (float)screenWidth / screenHeight;
		float near = 0.1f;
		float far = 100.0f;

//		this.projectionMatrix.setOrtho(left, right, top, bottom, near, far);
		this.projectionMatrix.setPerspective(fovy, aspect, near, far);
		shader.setUniform("u_projectionMatrix", this.projectionMatrix);
		
		// draw the objects in the scene graph recursively
		this.worldMatrix.identity();
		this.root.draw(shader, worldMatrix);
	}

	@Override
	/**
	 * Called when the canvas is resized
	 */
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		GL4 gl = (GL4) GLContext.getCurrentGL();
		
		this.screenWidth = width;
		this.screenHeight = height;
		
	}

	@Override
	/**
	 * Called when we dispose of the canvas 
	 */
	public void dispose(GLAutoDrawable drawable) {

	}

	public static void main(String[] args) throws IOException, GLException {
		new Week7();
	}


}
