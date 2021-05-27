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
import comp3170.demos.week12.sceneobjects.Cube;
import comp3170.demos.week12.sceneobjects.Cylinder;
import comp3170.demos.week12.sceneobjects.RenderQuad;
import comp3170.demos.week12.shaders.ShaderLibrary;

public class RenderTextureDemo extends JFrame implements GLEventListener {

	private GLCanvas canvas;

	final private static float TAU = (float) (Math.PI * 2);
	static final private String TEXTURE_VERTEX_SHADER = "textureVertex.glsl";
	static final private String TEXTURE_FRAGMENT_SHADER = "textureFragment.glsl";	
	static final private String SIMPLE_VERTEX_SHADER = "simpleVertex.glsl";
	static final private String SIMPLE_FRAGMENT_SHADER = "simpleFragment.glsl";	
	
	// screen size in pixels
	private int screenWidth = 1000;
	private int screenHeight = 1000;
	
	private InputManager input;
	private Animator animator;
	private long oldTime;

	private Camera camera1;
	private Cylinder cylinder;
	private Camera camera2;
	private Cube cube;

	private int renderTexture;
	private int frameBuffer;
	private int renderWidth = 1000;
	private int renderHeight = 1000;

	private boolean useEffect = false;



	
	public RenderTextureDemo() {
		super("Render texture demo");
		
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
		
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glClearDepth(1f);

		// Scene 1
		
		Shader simpleShader = ShaderLibrary.compileShader(SIMPLE_VERTEX_SHADER, SIMPLE_FRAGMENT_SHADER);
		
		this.cylinder = new Cylinder(simpleShader, Color.RED);
		cylinder.setScale(1,2,1);
		
		float aspect = (float)screenWidth / screenHeight;
		this.camera1 = new Camera(CAMERA_FOVY, aspect, CAMERA_NEAR, CAMERA_FAR);
		camera1.setDistance(CAMERA_DISTANCE);
		camera1.setTarget(0,1,0);	

		// Scene 2
		
		this.camera2 = new Camera(CAMERA_FOVY, aspect, CAMERA_NEAR, CAMERA_FAR);
		camera2.setDistance(CAMERA_DISTANCE);
		camera2.setTarget(0,0,0);	
		
		Shader textureShader = ShaderLibrary.compileShader(TEXTURE_VERTEX_SHADER, TEXTURE_FRAGMENT_SHADER);
		
		this.renderTexture = Shader.createRenderTexture(renderWidth, renderHeight);
		this.cube = new Cube(textureShader, renderTexture);
//		this.cube = new Cube(simpleShader, renderTexture);
		
		try {
			this.frameBuffer = Shader.createFrameBuffer(renderTexture);
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
			this.useEffect = !this.useEffect ;
		}
		
		if (useEffect) {
			camera2.update(input, dt);
			cube.update(input, dt);
		}
		else {
			camera1.update(input, dt);			
			cylinder.update(input, dt);
		}
		
		
		input.clear();
	}
	
	
	@Override
	/**
	 * Called when the canvas is redrawn
	 */
	public void display(GLAutoDrawable drawable) {
		GL4 gl = (GL4) GLContext.getCurrentGL();

		update();

		
		gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		if (useEffect) {
			// Pass 1: render to buffer
			gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, this.frameBuffer);
			gl.glViewport(0, 0, renderWidth, renderHeight);			
		}
		else {
			gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, 0);
			gl.glViewport(0, 0, this.screenWidth, this.screenHeight);			
		}		
		
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);		
		gl.glClear(GL.GL_DEPTH_BUFFER_BIT);		
		this.cylinder.draw(camera1);
		
		if (useEffect) {
			// Pass 2: render cube to screen
			
			gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
			gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, 0);
			gl.glViewport(0, 0, this.screenWidth, this.screenHeight);
	
			gl.glClear(GL.GL_COLOR_BUFFER_BIT);
			gl.glClear(GL.GL_DEPTH_BUFFER_BIT);
			
			this.cube.draw(camera2);
		}
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
		new RenderTextureDemo();
	}


}
