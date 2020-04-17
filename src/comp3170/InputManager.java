package comp3170;




import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashSet;
import java.util.Set;

import org.joml.Vector4f;

public class InputManager implements KeyListener, MouseListener, MouseMotionListener {

	private Set<Integer> keysDown;
	private Set<Integer> keysPressed;
	private boolean mouseDown;
	private boolean mouseClicked;
	private Vector4f mousePosition;
	
	public InputManager() {
		this.keysDown = new HashSet<Integer>();
		this.keysPressed = new HashSet<Integer>();
		this.mousePosition = new Vector4f(0,0,0,1);
	}
	
	public void addListener(Component component) {
		component.addKeyListener(this);
		component.addMouseListener(this);
		component.addMouseMotionListener(this);
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
	 * Write the current mouse position into dest as a vector of the form
	 * (x, y, 0, 1)
	 * 
	 * @param dest	The destination vector to write the value
	 * @return the mouse position vector
	 */
	public Vector4f getMousePosition(Vector4f dest) {
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		this.mousePosition.x = e.getX();
		this.mousePosition.y = e.getY();		
	}

	

}
