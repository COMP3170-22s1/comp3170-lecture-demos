
package comp3170.demos.week5.mesh;

import static com.jogamp.opengl.GL.GL_COLOR_BUFFER_BIT;

import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;

import org.joml.Vector3f;

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
import comp3170.demos.week5.Mesh;

public class MeshDemo extends JFrame implements GLEventListener {

	public static final float TAU = (float) (2 * Math.PI);		// https://tauday.com/tau-manifesto
	
	private int width = 800;
	private int height = 800;

	private GLCanvas canvas;
	private Shader shader;
	
	final private File DIRECTORY = new File("src/comp3170/demos/week5/mesh"); 
	final private String VERTEX_SHADER = "vertex.glsl";
	final private String FRAGMENT_SHADER = "fragment.glsl";

	private Animator animator;
	private long oldTime;
	private InputManager input;

	private Mesh[] spheres;
	private int currentSphere;

	public MeshDemo() {
		super("Week 5 mesh demo");

		// set up a GL canvas
		GLProfile profile = GLProfile.get(GLProfile.GL4);		 
		GLCapabilities capabilities = new GLCapabilities(profile);
		this.canvas = new GLCanvas(capabilities);
		this.canvas.addGLEventListener(this);
		this.add(canvas);
		
		// set up Animator		

		this.animator = new Animator(canvas);
		this.animator.start();
		this.oldTime = System.currentTimeMillis();		

		// input
		
		this.input = new InputManager(canvas);
		
		// set up the JFrame
		
		this.setSize(width,height);
		this.setVisible(true);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}

	@Override
	public void init(GLAutoDrawable arg0) {
		GL4 gl = (GL4) GLContext.getCurrentGL();
		
		// set the background colour to black
		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		
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

		// Set up the scene
		this.spheres = new Mesh[] {
			new SimpleCube(shader),
			new UVSphere(shader, 17),
			new Cube(shader, 10),
		};
		currentSphere = 0;
		
		for (int i = 0; i < spheres.length; i++) {
			this.spheres[i].setAngle(TAU/8, 0, TAU/8);			
		}
	}

	private final float ROTATION_SPEED = TAU / 8;
	private final float PERIOD = 2;
	
	private Vector3f angle = new Vector3f();
	private float sphericity = 0; 
	
	private void update() {
		long time = System.currentTimeMillis();
		float deltaTime = (time-oldTime) / 1000f;
		oldTime = time;
		
		spheres[currentSphere].getAngle(angle);
		angle.y = angle.y + ROTATION_SPEED * deltaTime;
		spheres[currentSphere].setAngle(angle);

		if (input.isKeyDown(KeyEvent.VK_UP)) {
			sphericity = Math.min(1, sphericity + deltaTime / PERIOD);
		}
		if (input.isKeyDown(KeyEvent.VK_DOWN)) {
			sphericity = Math.max(0, sphericity - deltaTime / PERIOD);
		}
		if (input.wasKeyPressed(KeyEvent.VK_SPACE)) {
			currentSphere = (currentSphere + 1) % spheres.length;
		}
		
		input.clear();
	}
	
	@Override
	public void display(GLAutoDrawable arg0) {
		GL4 gl = (GL4) GLContext.getCurrentGL();
		
		update();
		
        // clear the colour buffer
		gl.glClear(GL_COLOR_BUFFER_BIT);		

		// activate the shader
		this.shader.enable();		
		
		this.shader.setUniform("u_sphericity", sphericity);
		
		// draw the curve
		this.spheres[currentSphere].draw();
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
		new MeshDemo();
	}


}
