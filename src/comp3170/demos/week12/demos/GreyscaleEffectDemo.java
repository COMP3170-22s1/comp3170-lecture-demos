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
import comp3170.demos.week12.cameras.PerspectiveCamera;
import comp3170.demos.week12.sceneobjects.Cylinder;
import comp3170.demos.week12.sceneobjects.RenderQuad;
import comp3170.demos.week12.shaders.ShaderLibrary;

public class GreyscaleEffectDemo extends JFrame implements GLEventListener {

	private GLCanvas canvas;

	final private static float TAU = (float) (Math.PI * 2);
	static final private String GREYSCALE_VERTEX_SHADER = "greyscaleVertex.glsl";
	static final private String GREYSCALE_FRAGMENT_SHADER = "greyscaleFragment.glsl";	
	static final private String SIMPLE_VERTEX_SHADER = "simpleVertex.glsl";
	static final private String SIMPLE_FRAGMENT_SHADER = "simpleFragment.glsl";	
	
	// screen size in pixels
	private int screenWidth = 1000;
	private int screenHeight = 1000;
	
	private InputManager input;
	private Animator animator;
	private long oldTime;

	private PerspectiveCamera camera;
	private Cylinder cylinder;
	
	private int renderWidth = 1000;
	private int renderHeight = 1000;
	private RenderQuad quad;
	private int renderTexture;
	private int frameBuffer;

	private boolean useEffect = false;

	
	public GreyscaleEffectDemo() {
		super("Greyscale effect demo");
		
		GLProfile profile = GLProfile.get(GLProfile.GL4);		 
		GLCapabilities capabilities = new GLCapabilities(profile);
		canvas = new GLCanvas(capabilities);
		canvas.addGLEventListener(this);
		add(canvas);
		
		// set up Animator		
		animator = new Animator(canvas);
		animator.start();
		oldTime = System.currentTimeMillis();
				
		// set up Input manager
		input = new InputManager(canvas);
		
		// set up the JFrame		
		// make it twice as wide as the view width
		
		setSize(screenWidth, screenHeight);
		setVisible(true);
		addWindowListener(new WindowAdapter() {
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
		gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

		Shader simpleShader = ShaderLibrary.compileShader(SIMPLE_VERTEX_SHADER, SIMPLE_FRAGMENT_SHADER);

		cylinder = new Cylinder(simpleShader, Color.RED);
		cylinder.setScale(1,2,1);
		
		float aspect = (float)screenWidth / screenHeight;
		camera = new PerspectiveCamera(CAMERA_FOVY, aspect, CAMERA_NEAR, CAMERA_FAR);
		camera.setDistance(CAMERA_DISTANCE);
		camera.setTarget(0,1,0);	
		
		Shader greyscaleShder = ShaderLibrary.compileShader(GREYSCALE_VERTEX_SHADER, GREYSCALE_FRAGMENT_SHADER);
		
		renderTexture = Shader.createRenderTexture(renderWidth, renderHeight);
		quad = new RenderQuad(greyscaleShder, renderTexture);
		
		try {
			frameBuffer = Shader.createFrameBuffer(renderTexture);
		} catch (GLException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static final float CAMERA_DISTANCE = 5;
	private static final float CAMERA_FOVY = TAU / 8;
	private static final float CAMERA_NEAR = 0.1f;
	private static final float CAMERA_FAR = 10.0f;	
	
	public void update() {
		long time = System.currentTimeMillis();
		float dt = (time - oldTime) / 1000.0f;
		oldTime = time;
		
		if (input.wasKeyPressed(KeyEvent.VK_SPACE)) {
			useEffect = !useEffect ;
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

		
		if (useEffect) {
			// Pass 1: render to buffer
			gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, frameBuffer);
			gl.glViewport(0, 0, renderWidth, renderHeight);			
		}
		else {
			gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, 0);
			gl.glViewport(0, 0, screenWidth, screenHeight);
			
		}		
		
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);		
		gl.glClear(GL.GL_DEPTH_BUFFER_BIT);		
		cylinder.draw(camera);
		
		if (useEffect) {
			// Pass 2: render quad to screen
			
			gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, 0);
			gl.glViewport(0, 0, screenWidth, screenHeight);
	
			gl.glClear(GL.GL_COLOR_BUFFER_BIT);
			gl.glClear(GL.GL_DEPTH_BUFFER_BIT);
			
			quad.draw();
		}
	}

	@Override
	/**
	 * Called when the canvas is resized
	 */
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		GL4 gl = (GL4) GLContext.getCurrentGL();
		
		screenWidth = width;
		screenHeight = height;		
	}

	@Override
	/**
	 * Called when we dispose of the canvas 
	 */
	public void dispose(GLAutoDrawable drawable) {

	}

	public static void main(String[] args) throws IOException, GLException {
		new GreyscaleEffectDemo();
	}


}
