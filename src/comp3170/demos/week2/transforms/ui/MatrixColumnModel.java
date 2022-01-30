package comp3170.demos.week2.transforms.ui;

import javax.swing.table.AbstractTableModel;

import org.joml.Matrix3f;

public class MatrixColumnModel extends AbstractTableModel {

	private Matrix3f matrix;
	private int column;

	public MatrixColumnModel(Matrix3f matrix, int column) {
		this.matrix = matrix;
		this.column = column;
	}

	@Override
	public int getRowCount() {
		return 3;
	}

	@Override
	public int getColumnCount() {
		return 1;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		float v = matrix.get(column, rowIndex);
		return String.format("%.1f", v);

	}
}
