package comp3170.demos.week2.transforms.ui;

import javax.swing.table.AbstractTableModel;

import org.joml.Matrix3f;

public class MatrixModel extends AbstractTableModel {

	private Matrix3f matrix;

	public MatrixModel(Matrix3f matrix) {
		this.matrix = matrix;
	}

	@Override
	public int getRowCount() {
		return 3;
	}

	@Override
	public int getColumnCount() {
		return 3;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		float v = matrix.get(columnIndex, rowIndex);
		return String.format("%.1f", v);
	}
}
