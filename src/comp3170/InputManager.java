package comp3170;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashSet;
import java.util.Set;

import org.joml.Vector3f;

import com.jogamp.opengl.awt.GLCanvas;

/**
 * Input manager class for COMP3170 projects.
 * 
 * Usage:
 * 
 * In the constructor create an InputManager and add it to both the JFrame and the Canvas
 * to listen for mouse events:
 * 
 *		// set up Input manager
 *		this.input = new InputManager();
 *		input.addListener(this);
 *		input.addListener(this.canvas);
 *
 * Every frame, call the accessor methods to check for key and mouse:
 * 
 *      isMouseDown() - mouse is currently held down
 *      wasMouseClicked() - mouse has been clicked since the last frame
 *      getMousePosition() - mouse position within the window
 *      isKeyDown() - the specified key is currently down
 *      wasKeyPressed() - the specified key has been pressed since the last frame
 *      
 * At the end of the frame:
 * 
 * 		clear() - clear the wasPressed and wasClicked flags
 * 
 * @author malcolmryan
 *
 */

public class InputManager implements KeyListener, MouseListener, MouseMotionListener {

	private Set<Integer> keysDown;
	private Set<Integer> keysPressed;
	private boolean mouseDown;
	private boolean mouseClicked;
	private Vector3f mousePosition;
	private GLCanvas canvas;
	
	public InputManager(GLCanvas canvas) {
		this.canvas = canvas;
		this.keysDown = new HashSet<Integer>();
		this.keysPressed = new HashSet<Integer>();
		this.mousePosition = new Vector3f(0,0,1);

		canvas.addKeyListener(this);
		canvas.addMouseListener(this);
		canvas.addMouseMotionListener(this);
		canvas.requestFocus();
	}
	
	/**
	 * Test if the mouse button is currently pressed
	 * 
	 * @return true if the mouse button is pressed
	 */
	public boolean isMouseDown() {
		return mouseDown;
	}

	/**
	 * Test if the mouse button was clicked this frame
	 * 
	 * @return true if the mouse button is pressed
	 */
	public boolean wasMouseClicked() {
		return mouseClicked;
	}

	/**
	 * Write the current mouse position in viewport coordinates
	 * into dest as a 2D homogenous point of the form (x, y, 1)
	 * 
	 * @param dest	The destination vector to write the value
	 * @return the mouse position vector
	 */
	public Vector3f getMousePosition(Vector3f dest) {
		return this.mousePosition.get(dest);
	}
	
	
	/**
	 * Test if the specified key is currently pressed.
	 * Note: the input is a keycode value, as specified on the KeyEvent class.
	 * https://docs.oracle.com/javase/7/docs/api/java/awt/event/KeyEvent.html
	 * 
	 * So, for instance, to test if the up arrow is pressed call:
	 * 
	 * 		input.isKeyDown(KeyEvent.VK_UP)
	 * 
	 * @param keyCode The integer keycode for the key 
	 * @return true if the key is pressed
	 */
	
	public boolean isKeyDown(int keyCode) {
		return keysDown.contains(keyCode);
	}
	
	/**
	 * Test if the specified key has been pressed since the last call to clear().
	 * 
	 * Note: the input is a keycode value, as specified on the KeyEvent class.
	 * https://docs.oracle.com/javase/7/docs/api/java/awt/event/KeyEvent.html
	 * 
	 * So, for instance, to test if the up arrow has been pressed since the last frame:
	 * 
	 * 		input.wasKeyPressed(KeyEvent.VK_UP)
     *
	 * @param keyCode The integer keycode for the key 
	 * @return true if the key has been pressed
	 */
	
	public boolean wasKeyPressed(int keyCode) {
		return keysPressed.contains(keyCode);
	}
	
	public void clear() {
		keysPressed.clear();
		mouseClicked = false;
	}

	// KeyListener methods
	
	@Override
	public void keyTyped(KeyEvent e) {
		// do nothing
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		this.keysDown.add(keyCode);
		this.keysPressed.add(keyCode);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		this.keysDown.remove(e.getKeyCode());
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		mouseDown = true;		
		mouseClicked = true;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		mouseDown = false;				
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		mousePosition.x = 2.0f * e.getX() / canvas.getWidth() - 1;
		mousePosition.y = 2.0f * e.getY() / canvas.getHeight() - 1;		
		mousePosition.y = -mousePosition.y;		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mousePosition.x = 2.0f * e.getX() / canvas.getWidth() - 1;
		mousePosition.y = 2.0f * e.getY() / canvas.getHeight() - 1;		
		mousePosition.y = -mousePosition.y;
	}

	

}
