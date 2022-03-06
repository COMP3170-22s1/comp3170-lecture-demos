
package comp3170.demos.week3.demos;

import static com.jogamp.opengl.GL.GL_COLOR_BUFFER_BIT;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;

import org.joml.Matrix4f;

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

public class ModelWorldViewDemo extends JFrame implements GLEventListener {

	public static final float TAU = (float) (2 * Math.PI);		// https://tauday.com/tau-manifesto
	
	private int width = 2400;
	private int height = 800;

	private GLCanvas canvas;
	private Shader shader;
	
	final private File DIRECTORY = new File("src/comp3170/demos/week3/demos"); 
	final private String VERTEX_SHADER = "vertex.glsl";
	final private String FRAGMENT_SHADER = "fragment.glsl";

	private InputManager input;
	private Animator animator;
	private long oldTime;

	private Axes axes;

	public ModelWorldViewDemo() {
		super("Model/World/View demo");

		// set up a GL canvas
		GLProfile profile = GLProfile.get(GLProfile.GL4);		 
		GLCapabilities capabilities = new GLCapabilities(profile);
		canvas = new GLCanvas(capabilities);
		canvas.addGLEventListener(this);
		add(canvas);
		
		// set up the JFrame
		
		setSize(width,height);
		setVisible(true);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});		
		
		// set up Animator		
		animator = new Animator(canvas);
		animator.start();
		oldTime = System.currentTimeMillis();		
		
		// set up Input manager
		input = new InputManager(canvas);

	}

	@Override
	public void init(GLAutoDrawable arg0) {
		GL4 gl = (GL4) GLContext.getCurrentGL();
		gl.glEnable(GL4.GL_SCISSOR_TEST);
		
		shader = compileShaders(VERTEX_SHADER, FRAGMENT_SHADER);
	    
		// set up the scene
		axes = new Axes();
	}
	
	private Shader compileShaders(String vertex, String fragment) {
		// Compile the shader
		try {
			File vertexShader = new File(DIRECTORY, vertex);
			File fragementShader = new File(DIRECTORY, fragment);
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
	
	private Matrix4f identity = new Matrix4f().identity();
	private Matrix4f modelMatrix = new Matrix4f();
	private Matrix4f viewMatrix = new Matrix4f();

	private void update() {
		long time = System.currentTimeMillis();
		float dt = (time - oldTime) / 1000f;
		oldTime = time;
		
		
	}
	
	
	private static final int MARGIN = 0;

	@Override
	public void display(GLAutoDrawable arg0) {
		GL4 gl = (GL4) GLContext.getCurrentGL();

		update();
		
		// set the background colour to whites
		gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);			
		gl.glClear(GL_COLOR_BUFFER_BIT);		

		// activate the shader
		shader.enable();		

		// MODEL
		
		gl.glViewport(MARGIN,MARGIN,width/3-2*MARGIN,height-2*MARGIN);
		gl.glScissor(MARGIN,MARGIN,width/3-2*MARGIN,height-2*MARGIN);
		gl.glClearColor(0.1f, 0.0f, 0.0f, 1.0f);			
		gl.glClear(GL_COLOR_BUFFER_BIT);		
		
		shader.setUniform("u_modelMatrix", identity);
		shader.setUniform("u_viewMatrix", identity);		
		axes.draw(shader);

		// WORLD
		
		gl.glViewport(width/3+MARGIN,MARGIN,width/3-2*MARGIN,height-2*MARGIN);
		gl.glScissor(width/3+MARGIN,MARGIN,width/3-2*MARGIN,height-2*MARGIN);
		gl.glClearColor(0.0f, 0.1f, 0.0f, 1.0f);			
		gl.glClear(GL_COLOR_BUFFER_BIT);		

		shader.setUniform("u_modelMatrix", modelMatrix);
		shader.setUniform("u_viewMatrix", identity);		
		axes.draw(shader);

		// View
		
		gl.glViewport(2*width/3+MARGIN,MARGIN,width/3-2*MARGIN,height-2*MARGIN);
		gl.glScissor(2*width/3+MARGIN,MARGIN,width/3-2*MARGIN,height-2*MARGIN);
		gl.glClearColor(0.0f, 0.0f, 0.1f, 1.0f);			
		gl.glClear(GL_COLOR_BUFFER_BIT);		

		shader.setUniform("u_modelMatrix", modelMatrix);
		shader.setUniform("u_viewMatrix", viewMatrix);		
		axes.draw(shader);

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
		new ModelWorldViewDemo();
	}


}
