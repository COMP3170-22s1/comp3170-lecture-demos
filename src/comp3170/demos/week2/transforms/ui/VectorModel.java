package comp3170.demos.week2.transforms.ui;

import javax.swing.table.AbstractTableModel;

import org.joml.Vector3f;

public class VectorModel extends AbstractTableModel {

	private Vector3f vector;

	public VectorModel(Vector3f vector) {
		this.vector = vector;
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
		float v = vector.get(rowIndex);
		return String.format("%.1f", v);
	}
}
