//Philip Garcia CS2251 Spring 2021
// Lab 6.2 Employee Db with GUI

import javax.swing.table.AbstractTableModel;


class TableModel extends AbstractTableModel {
	// TableModel's default column names
	private String[] columnNames = { "We", "will", "we", "will", "rock", "you" };

	// TableModel's default data
	private String[][] data = { { "Buddy", "you're a", "boy", "make a", "big", "noise" },
			{ "Playing in", "the street", "gonna", "be a", "big man", "someday" },
			{ "You got", "mud on", "your face", "you", "big", "disgrace" },
			{ "Kicking", "your", "can", "all over", "the place", "singin'" }, };

	/**
	 * Returns the number of rows in the table model.
	 */
	public int getRowCount() {
		return data.length;
	}

	/**
	 * Returns the number of columns in the table model.
	 */
	public int getColumnCount() {
		return columnNames.length;
	}

	/**
	 * Returns the column name for the column index.
	 */
	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}

	/**
	 * Returns data type of the column specified by its index.
	 */
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return getValueAt(0, columnIndex).getClass();
	}

	/**
	 * Returns the value of a table model at the specified row index and column
	 * index.
	 */
	public Object getValueAt(int rowIndex, int columnIndex) {
		return data[rowIndex][columnIndex];
	}

	public void updateTable(String[] columnNames1, String[][] data1) {

		columnNames = columnNames1;
		data = data1;
		fireTableStructureChanged();
	}
}