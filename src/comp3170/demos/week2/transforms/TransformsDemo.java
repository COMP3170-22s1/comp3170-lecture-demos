
package comp3170.demos.week2.transforms;

import static com.jogamp.opengl.GL.GL_COLOR_BUFFER_BIT;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
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
import comp3170.demos.week2.transforms.sceneobjects.Axes;
import comp3170.demos.week2.transforms.sceneobjects.Grid;
import comp3170.demos.week2.transforms.sceneobjects.Point;
import comp3170.demos.week2.transforms.sceneobjects.SceneObject;
import comp3170.demos.week2.transforms.ui.EquationPanel;

public class TransformsDemo extends JFrame implements GLEventListener {

	public static final float TAU = (float) (2 * Math.PI);		// https://tauday.com/tau-manifesto
	
	private int frameWidth = 1400;
	private int frameHeight = 1300;

	private int canvasWidth = 1400;
	private int canvasHeight = 1000;

	private GLCanvas canvas;
	private Shader shader;
	
	final private File DIRECTORY = new File("src/comp3170/demos/week2/transforms"); 
	final private String VERTEX_SHADER = "vertex.glsl";
	final private String FRAGMENT_SHADER = "fragment.glsl";

	private Animator animator;
	private long oldTime;
	private InputManager input;
	
	private SceneObject root;	
	private Matrix4f modelToWorldMatrix;

	private Vector2f translation;
	private float rotation;
	private float scale;
	private float shear;

	// for the UI only
	private Vector3f pModel = new Vector3f(0.8f, 0.6f, 1);
	private Vector3f pWorld  = new Vector3f();
	private Matrix3f matrix  = new Matrix3f();

	private EquationPanel equationPanel;


	public TransformsDemo() {
		super("2D transforms demo");

		setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));

		equationPanel = new EquationPanel(pModel, pWorld, matrix);
		JPanel joglPanel = new JPanel();
		joglPanel.setLayout(new BorderLayout());
		add(equationPanel);
		add(joglPanel);
		
		// set up a GL canvas
		GLProfile profile = GLProfile.get(GLProfile.GL4);		 
		GLCapabilities capabilities = new GLCapabilities(profile);
		canvas = new GLCanvas(capabilities);
		canvas.addGLEventListener(this);
		joglPanel.add(canvas);
		
		// set up the JFrame
		
		setSize(frameWidth,frameHeight);
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
		input = new InputManager(canvas);
	}
	
	@Override
	public void init(GLAutoDrawable arg0) {
		GL4 gl = (GL4) GLContext.getCurrentGL();
		
		// set the background colour to white
		gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		
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

		// Set up the scene
		root = new SceneObject();
		
		Matrix4f matrix; 
		
		Axes worldAxes = new Axes();
		worldAxes.setParent(root);
		
		Color lightBlue = new Color(128, 128, 255);
		Grid grid = new Grid(40, lightBlue);
		grid.setParent(worldAxes);
		matrix = grid.getMatrix();
		matrix.scale(2.0f);

		Axes modelAxes = new Axes();
		modelAxes.setParent(worldAxes);
		modelToWorldMatrix = modelAxes.getMatrix();

		Color lightMagenta = new Color(255, 128, 255);
		grid = new Grid(40, lightMagenta);
		grid.setParent(modelAxes);
		matrix = grid.getMatrix();
		matrix.scale(2.0f);

		Point point = new Point(Color.black);
		matrix = point.getMatrix();
		matrix.translate(pModel.x, pModel.y, 0);
		point.setParent(modelAxes);
		
		translation = new Vector2f(0, 0);
		rotation = 0;
		scale = 1;
		shear = 0;
		updateModelMatrix();
	}

	private Matrix4f shearMatrix = new Matrix4f();
	private void updateModelMatrix()
	{
		// M = T * R * Sh * S
		modelToWorldMatrix.identity();
		modelToWorldMatrix.translate(translation.x, translation.y, 0);
		modelToWorldMatrix.rotateZ(rotation);
		
		// There is no built-in shear transform, so we have to build
		// the matrix ourselves:
		// 
		//          [ 1   h   0   0 ]
		//  Sh(h) = [ 0   1   0   0 ]
		//          [ 0   0   1   0 ]
		//          [ 0   0   0   1 ]
		
		shearMatrix.identity();
		shearMatrix.m10(shear);
		modelToWorldMatrix.mul(shearMatrix);
		
		modelToWorldMatrix.scale(scale);
	}
	
	private void updateUI() {
		// convert 4x4 matrix into 3x3 matrix
		matrix.m00 = modelToWorldMatrix.m00(); 
		matrix.m01 = modelToWorldMatrix.m01(); 
		matrix.m10 = modelToWorldMatrix.m10(); 
		matrix.m11 = modelToWorldMatrix.m11(); 
		matrix.m20 = modelToWorldMatrix.m30(); 
		matrix.m21 = modelToWorldMatrix.m31(); 

		pModel.mul(matrix, pWorld);
		equationPanel.update();
	}
	
	private final static float TRANSLATION_SPEED = 0.5f;
	private final static float SCALE_SPEED = 0.5f;
	private final static float ROTATION_SPEED = TAU/4;
	private final static float SHEAR_SPEED = 0.5f;
	
	private void update() {
		long time = System.currentTimeMillis();
		float dt = (time - oldTime) / 1000f;
		oldTime = time;
		
		if (input.isKeyDown(KeyEvent.VK_LEFT))
		{
			translation.x -= TRANSLATION_SPEED * dt;
		}
		if (input.isKeyDown(KeyEvent.VK_RIGHT))
		{
			translation.x += TRANSLATION_SPEED * dt;
		}
		if (input.isKeyDown(KeyEvent.VK_DOWN))
		{
			translation.y -= TRANSLATION_SPEED * dt;
		}
		if (input.isKeyDown(KeyEvent.VK_UP))
		{
			translation.y += TRANSLATION_SPEED * dt;
		}
		
		if (input.isKeyDown(KeyEvent.VK_PAGE_DOWN))
		{
			scale *= Math.pow(SCALE_SPEED, dt);
		}
		if (input.isKeyDown(KeyEvent.VK_PAGE_UP))
		{
			scale /= Math.pow(SCALE_SPEED, dt);
		}
		if (input.isKeyDown(KeyEvent.VK_SPACE)) {
			scale = 1;
		}

		if (input.isKeyDown(KeyEvent.VK_A)) {
			rotation += ROTATION_SPEED * dt;
		}		
		if (input.isKeyDown(KeyEvent.VK_D)) {
			rotation -= ROTATION_SPEED * dt;
		}
		if (input.isKeyDown(KeyEvent.VK_S)) {
			rotation = 0;
		}		
		
		if (input.isKeyDown(KeyEvent.VK_Q)) {
			shear -= SHEAR_SPEED * dt;
		}		
		if (input.isKeyDown(KeyEvent.VK_E)) {
			shear += SHEAR_SPEED * dt;
		}		
		if (input.isKeyDown(KeyEvent.VK_W)) {
			shear = 0;
		}		

		updateModelMatrix();
		updateUI();
	}
	
	private Matrix4f viewMatrix = new Matrix4f().identity().scale(0.5f);
	
	@Override
	public void display(GLAutoDrawable arg0) {
		GL4 gl = (GL4) GLContext.getCurrentGL();
		
		// update the scene
		update();	

        // clear the colour buffer
		gl.glClear(GL_COLOR_BUFFER_BIT);		

		// activate the shader
		shader.enable();
		
		root.draw(shader, viewMatrix);					
	}

	@Override
	public void reshape(GLAutoDrawable d, int x, int y, int width, int height) {
		canvasWidth = width;
		canvasHeight = height;
		
		float aspect = (float)width / height;
		viewMatrix = new Matrix4f().identity().scale(0.5f, 0.5f * aspect, 1);
		
		// TODO Auto-generated method stub		
	}

	@Override
	public void dispose(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub		
	}
	
	public static void main(String[] args) { 
		new TransformsDemo();
	}


}
