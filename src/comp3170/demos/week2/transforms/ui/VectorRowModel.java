package comp3170.demos.week2.transforms.ui;

import javax.swing.table.AbstractTableModel;

import org.joml.Vector3f;

public class VectorRowModel extends AbstractTableModel {

	private Vector3f vector;
	private int index;

	public VectorRowModel(Vector3f vector, int index) {
		this.vector = vector;
		this.index = index;
	}

	@Override
	public int getRowCount() {
		return 1;
	}

	@Override
	public int getColumnCount() {
		return 1;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		float v = vector.get(index);
		return String.format("%.1f", v);
	}
}
