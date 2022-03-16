
package comp3170.demos.week5.extrusion;

import static com.jogamp.opengl.GL.GL_COLOR_BUFFER_BIT;

import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;

import org.joml.Matrix3f;
import org.joml.Vector2f;
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

public class ExtrusionDemo extends JFrame implements GLEventListener {

	public static final float TAU = (float) (2 * Math.PI);		// https://tauday.com/tau-manifesto
	
	private int width = 800;
	private int height = 800;

	private GLCanvas canvas;
	private Shader shader;
	
	final private File DIRECTORY = new File("src/comp3170/demos/week5/extrusion"); 
	final private String VERTEX_SHADER = "vertex.glsl";
	final private String FRAGMENT_SHADER = "fragment.glsl";

	private Animator animator;
	private long oldTime;
	private InputManager input;

	private Extrusion mesh;

	public ExtrusionDemo() {
		super("Week 5 Extrusion demo");

		// set up a GL canvas
		GLProfile profile = GLProfile.get(GLProfile.GL4);		 
		GLCapabilities capabilities = new GLCapabilities(profile);
		canvas = new GLCanvas(capabilities);
		canvas.addGLEventListener(this);
		add(canvas);
		
		// set up Animator		

		animator = new Animator(canvas);
		animator.start();
		oldTime = System.currentTimeMillis();		

		// input
		
		input = new InputManager(canvas);
		
		// set up the JFrame
		
		setSize(width,height);
		setVisible(true);
		addWindowListener(new WindowAdapter() {
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
			shader = new Shader(vertexShader, fragementShader);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (GLException e) {
			e.printStackTrace();
			System.exit(1);
		}

		// The cross-section is a square, specified in anticlockwise order
		
		float size = 0.2f;
		Vector2f[] crossSection = new Vector2f[] {
			new Vector2f(-size/2, -size/2),
			new Vector2f(-size/2,  size/2),
			new Vector2f( size/2,  size/2),
			new Vector2f( size/2, -size/2),
		};

		// The curve is a semicircle	
		
		int nPoints = 20;
		float radius = 0.8f;
		Vector3f[] curve = new Vector3f[nPoints];
		Matrix3f rotate = new Matrix3f();

		for (int i = 0; i < nPoints; i++) {
			float angle = i * TAU / 2 / (nPoints-1);			// 0 to TAU/2 (inclusive)
			rotate.rotationZ(angle);							// rotation = Rz(angle)
			curve[i] = new Vector3f(radius, 0, 0).mul(rotate);	// curve[i] = Rz * (r, 0, 0, 1)
		}
		
		// Extrude the mesh 
		
		Vector3f up = new Vector3f(0,0,1);
		
		mesh = new Extrusion(crossSection, curve, up);		
	}

	private final float ROTATION_SPEED = TAU / 8;
	
	private boolean isTurning = true;
	
	private void update() {
		long time = System.currentTimeMillis();
		float deltaTime = (time-oldTime) / 1000f;
		oldTime = time;
		
		if (input.wasKeyPressed(KeyEvent.VK_SPACE)) {
			isTurning = !isTurning;
		}
		
		if (isTurning) {
			mesh.getMatrix().rotateY(ROTATION_SPEED * deltaTime);
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
		shader.enable();		
		
		// draw the curve
		mesh.draw(shader);
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
		new ExtrusionDemo();
	}


}
