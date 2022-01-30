package comp3170.demos.week2.transforms.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import org.joml.Matrix3f;
import org.joml.Vector3f;


public class EquationPanel extends JPanel {
	
	private static final int SMALL_FONT_SIZE = 18;
	private static final int BIG_FONT_SIZE = 100;
	private static final int CELL_WIDTH = 50;
	private static final int CELL_HEIGHT = 40;
	
	private VectorModel pModel;
	private VectorModel pWorld;
	private MatrixModel matrix;
	private MatrixColumnModel iAxis;
	private MatrixColumnModel jAxis;
	private MatrixColumnModel origin;
	private VectorRowModel x;
	private VectorRowModel y;	
		
	Color lightRed = new Color(255, 196, 196);
	Color lightGreen = new Color(196, 255, 196);
	Color lightBlue = new Color(196, 196, 255);
	Color[] matrixColors = {lightRed, lightGreen, lightBlue};
	
	/**
	 * Create the panel.
	 */
	public EquationPanel(Vector3f pModel, Vector3f pWorld, Matrix3f matrix) {
		
		setPreferredSize(new Dimension(1400,300));
		
		this.pModel = new VectorModel(pModel);
		this.pWorld = new VectorModel(pWorld);
		this.matrix = new MatrixModel(matrix);
		this.iAxis = new MatrixColumnModel(matrix, 0);
		this.jAxis = new MatrixColumnModel(matrix, 1);
		this.origin = new MatrixColumnModel(matrix, 2);
		this.x = new VectorRowModel(pModel, 0);
		this.y = new VectorRowModel(pModel, 1);
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
				
		JPanel topPanel = new JPanel();
		add(topPanel);
		
		makeVector("P_model", topPanel, this.pModel, Color.white);
		makeVector("  i", topPanel, iAxis, lightRed);
		makeVector("  j", topPanel, jAxis, lightGreen);
		makeVector("  T", topPanel, origin, lightBlue);

		makeText("  M = ", topPanel);
		makeMatrix(topPanel, this.matrix, matrixColors);

		JPanel bottomPanel = new JPanel();
		add(bottomPanel);

		makeText("P_world = M P_model = ", bottomPanel);
		makeMatrix(bottomPanel, this.matrix, matrixColors);
		makeVector(bottomPanel, this.pModel, Color.white);
		makeText(" = ", bottomPanel);
		
		makeValue(bottomPanel, x);
		makeVector(bottomPanel, iAxis, lightRed);

		makeText(" + ", bottomPanel);
		makeValue(bottomPanel, y);
		makeVector(bottomPanel, jAxis, lightGreen);
		
		makeText(" + 1", bottomPanel);
		makeVector(bottomPanel, origin, lightBlue);

		makeText(" = ", bottomPanel);
		makeVector(bottomPanel, this.pWorld, Color.white);
	}
	
	private void makeValue(JPanel panel, TableModel data) {
		JTable table = new JTable(data);
		table.setFont(new Font("Lucida Grande", Font.PLAIN, SMALL_FONT_SIZE));
		table.setRowHeight(CELL_HEIGHT);

		TableColumn col = table.getColumnModel().getColumn(0);
		col.setPreferredWidth(CELL_WIDTH);
		DefaultTableCellRenderer r = new DefaultTableCellRenderer();
		r.setHorizontalAlignment(SwingConstants.RIGHT);

		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		panel.add(table);

	}

	private void makeText(String string, JPanel panel) {
		JLabel label = new JLabel(string);
		label.setFont(new Font("Lucida Grande", Font.PLAIN, SMALL_FONT_SIZE));
		panel.add(label);
	}
	
	private void makeMatrix(JPanel panel, TableModel data, Color[] color) {
		JLabel lblMatrixLB = new JLabel("[");
		panel.add(lblMatrixLB);
		lblMatrixLB.setFont(new Font("Lucida Grande", Font.PLAIN, BIG_FONT_SIZE));

		JTable table = new JTable(data);
		table.setFont(new Font("Lucida Grande", Font.PLAIN, SMALL_FONT_SIZE));
		table.setRowHeight(CELL_HEIGHT);

		for (int i = 0; i < 3; i++)
		{
			TableColumn col = table.getColumnModel().getColumn(i);
			col.setPreferredWidth(CELL_WIDTH);
			DefaultTableCellRenderer r = new DefaultTableCellRenderer();
			r.setBackground(color[i]);
			r.setHorizontalAlignment(SwingConstants.RIGHT);

			col.setCellRenderer(r);
		}
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		panel.add(table);
		
		JLabel lblMatrixRB = new JLabel("]");
		panel.add(lblMatrixRB);
		lblMatrixRB.setFont(new Font("Lucida Grande", Font.PLAIN, BIG_FONT_SIZE));
	}

	private void makeVector(String name , JPanel panel, TableModel data, Color color) {
		JLabel lblVector = new JLabel(name + " = ");
		panel.add(lblVector);
		lblVector.setFont(new Font("Lucida Grande", Font.PLAIN, SMALL_FONT_SIZE));
		
		makeVector(panel, data, color);
	}

	private void makeVector(JPanel panel, TableModel data, Color color) {
		JLabel lblLB = new JLabel("(");
		panel.add(lblLB);
		lblLB.setFont(new Font("Lucida Grande", Font.PLAIN, BIG_FONT_SIZE));
		
		JTable table = new JTable(data);
		table.setFont(new Font("Lucida Grande", Font.PLAIN, SMALL_FONT_SIZE));
		table.setRowHeight(CELL_HEIGHT);
		
		TableColumn col = table.getColumnModel().getColumn(0);
		col.setPreferredWidth(CELL_WIDTH);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		DefaultTableCellRenderer r = new DefaultTableCellRenderer();
		r.setBackground(color);
		r.setHorizontalAlignment(SwingConstants.RIGHT);

		col.setCellRenderer(r);
		panel.add(table);
		
		JLabel lblRB = new JLabel(")");
		panel.add(lblRB);
		lblRB.setFont(new Font("Lucida Grande", Font.PLAIN, BIG_FONT_SIZE));
	}

	public void update() {
		pModel.fireTableDataChanged();
		pWorld.fireTableDataChanged();
		matrix.fireTableDataChanged();
		iAxis.fireTableDataChanged();
		jAxis.fireTableDataChanged();
		origin.fireTableDataChanged();
		x.fireTableDataChanged();
		y.fireTableDataChanged();
	}
	
	
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		
		Vector3f pModel = new Vector3f(-0.8f, 0.6f, 1);
		Vector3f pWorld = new Vector3f(0, 0, 1);
		Matrix3f matrix = new Matrix3f();
		matrix.identity();		
		pModel.mul(matrix, pWorld);
		
		EquationPanel panel = new EquationPanel(pModel, pWorld, matrix);
		frame.add(panel);

		
		// set up the JFrame
		
		frame.setSize(1400,300);
		frame.setVisible(true);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}
	
	
}
