
package comp3170.demos.week12.demos;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JFrame;

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
import comp3170.demos.week12.cameras.Camera;
import comp3170.demos.week12.cameras.OrthographicCamera;
import comp3170.demos.week12.sceneobjects.Cylinder;
import comp3170.demos.week12.sceneobjects.Plane;
import comp3170.demos.week12.shaders.ShaderLibrary;

public class ShadowBufferDemo extends JFrame implements GLEventListener {

	private GLCanvas canvas;

	final private static float TAU = (float) (Math.PI * 2);
	static final private String DEPTH_VERTEX_SHADER = "depthVertex.glsl";
	static final private String DEPTH_FRAGMENT_SHADER = "depthFragment.glsl";	
	static final private String SIMPLE_VERTEX_SHADER = "simpleVertex.glsl";
	static final private String SIMPLE_FRAGMENT_SHADER = "simpleFragment.glsl";	
	
	// screen size in pixels
	private int screenWidth = 1000;
	private int screenHeight = 1000;
	
	private InputManager input;
	private Animator animator;
	private long oldTime;

	private Camera camera;
	private Cylinder cylinder;
	private Plane plane;
	
	private int renderSize = 4096;
	private int renderTexture;
	private int frameBuffer;

	private boolean useEffect = false;

	private int depthTexture;


	
	public ShadowBufferDemo() {
		super("Shadow buffer demo");
		
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
		
		// enable depth testing
		
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glClearDepth(1f);
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

		Shader simpleShader = ShaderLibrary.compileShader(SIMPLE_VERTEX_SHADER, SIMPLE_FRAGMENT_SHADER);
		Shader depthShader = ShaderLibrary.compileShader(DEPTH_VERTEX_SHADER, DEPTH_FRAGMENT_SHADER);

		this.cylinder = new Cylinder(depthShader, Color.RED);
		cylinder.setScale(1,2,1);
		this.plane = new Plane(depthShader, Color.WHITE);
		plane.setScale(10,1,10);
		
		float aspect = (float)screenWidth / screenHeight;
		this.camera = new OrthographicCamera(CAMERA_HEIGHT * aspect, CAMERA_HEIGHT, CAMERA_NEAR, CAMERA_FAR);
//		this.camera = new PerspectiveCamera(CAMERA_FOVY, aspect, CAMERA_NEAR, CAMERA_FAR);
		camera.setDistance(CAMERA_DISTANCE);
		camera.setTarget(0,1,0);	
				
		this.renderTexture = Shader.createRenderTexture(renderSize, renderSize);
		this.depthTexture = Shader.createDepthTexture(renderSize, renderSize);
		
		try {
			this.frameBuffer = Shader.createFrameBuffer(renderTexture, depthTexture);
		} catch (GLException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static final float CAMERA_DISTANCE = 20;
	private static final float CAMERA_HEIGHT = 30;
	private static final float CAMERA_FOVY = TAU / 8;
	private static final float CAMERA_NEAR = 0.1f;
	private static final float CAMERA_FAR = 50.0f;	
	
	public void update() {
		long time = System.currentTimeMillis();
		float dt = (time - oldTime) / 1000.0f;
		oldTime = time;
		
		if (input.wasKeyPressed(KeyEvent.VK_SPACE)) {
			this.useEffect = !this.useEffect ;
		}
		
		camera.update(input, dt);
		input.clear();
	}
	
	
	@Override
	/**
	 * Called when the canvas is redrawn
	 */
	public void display(GLAutoDrawable drawable) {
		GL4 gl = (GL4) GLContext.getCurrentGL();

		update();

		gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, 0);
		gl.glViewport(0, 0, this.screenWidth, this.screenHeight);			
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);		
		gl.glClear(GL.GL_DEPTH_BUFFER_BIT);		
		this.cylinder.draw(camera);
		this.plane.draw(camera);
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
		new ShadowBufferDemo();
	}


}
