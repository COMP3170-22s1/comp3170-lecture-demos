package comp3170.demos.week2.transforms.sceneobjects;

import java.awt.Color;

import org.joml.Matrix4f;

public class Axes extends SceneObject{

	private Point origin;
	private Vector iAxis;
	private Vector jAxis;

	public Axes() {

		Matrix4f matrix; 
		
		iAxis = new Vector(Color.red);
		iAxis.setParent(this);
		jAxis = new Vector(Color.green);
		jAxis.setParent(this);
		matrix = jAxis.getMatrix();
		matrix.rotateZ((float) (Math.PI / 2));

		origin = new Point(Color.blue);
		origin.setParent(this);
	}
	
}
