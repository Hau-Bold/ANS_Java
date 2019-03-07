package lookup;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;

public class CommonModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private String[] nameOfColumns = null;

	private Object[][] dataEntries = null;

	/**
	 * Constructor.
	 * 
	 * @param nameofCOlumns
	 *            - the column names
	 */
	public CommonModel(String[] nameofColumns) {
		this.nameOfColumns = nameofColumns;
	}

	public int getColumnCount() {
		return nameOfColumns.length;
	}

	public int getRowCount() {
		if (dataEntries != null) {
			return dataEntries.length;
		}
		return 0;
	}

	public Object getValueAt(int row, int column) {
		return dataEntries[row][column];
	}

	/** to receive name of a column */
	@Override
	public String getColumnName(int column) {
		return nameOfColumns[column];
	}

	/**
	 * to add a row
	 * 
	 * @param row
	 *            - the row
	 */
	public void addDataRow(Vector<String> vector) {
		Object[][] newData = null;

		if (dataEntries == null) {
			dataEntries = new Object[1][getColumnCount()];
			newData = new Object[1][getColumnCount()];
		}

		else {
			newData = new Object[getRowCount() + 1][getColumnCount()];
			/** copy data */
			for (int row = 0; row < dataEntries.length; row++) {
				for (int column = 0; column < getColumnCount(); column++) {
					newData[row][column] = dataEntries[row][column];
				}
			}

		}
		/** append the vector */
		for (int i = 0; i < vector.size(); i++) {
			newData[newData.length - 1][i] = vector.get(i);
		}

		dataEntries = newData;
		this.fireTableDataChanged();
	}

	/**
	 * to append an empty row
	 */
	public void appendEmptyRow() {
		Object[][] newData;
		if (dataEntries.equals(null)) {
			newData = new Object[1][nameOfColumns.length];
		} else {
			newData = new Object[dataEntries.length + 1][nameOfColumns.length];

			for (int row = 0; row < dataEntries.length; row++) {
				for (int column = 0; column < nameOfColumns.length; column++) {
					newData[row][column] = dataEntries[row][column];
				}
			}

		}

		dataEntries = newData;
		this.fireTableDataChanged();
	}

	/**
	 * to delete selected rows.
	 * 
	 * @param arrayOfSelectedRows
	 *            - the array of selected rows
	 */
	public void deleteRow(int[] arrayOfSelectedRows) {
		if (dataEntries != null) {
			Object[][] newData = new Object[dataEntries.length - arrayOfSelectedRows.length][nameOfColumns.length];

			/** copy upper part */
			for (int row = 0; row < arrayOfSelectedRows[0]; row++) {
				for (int column = 0; column < nameOfColumns.length; column++) {
					newData[row][column] = dataEntries[row][column];
				}
			}

			/** copy lower part */
			for (int row = arrayOfSelectedRows[arrayOfSelectedRows.length - 1] + 1; row < dataEntries.length; row++) {
				for (int column = 0; column < nameOfColumns.length; column++) {
					newData[row - arrayOfSelectedRows.length][column] = dataEntries[row][column];
				}
			}

			dataEntries = newData;
			this.fireTableDataChanged();
		}

		else {
			return;
		}
	}

	/**
	 * 
	 * yields the state
	 */
	public Boolean isEmpty() {
		return dataEntries == null;
	}

	/**
	 * to clear the model
	 */
	public void clear() {
		if (dataEntries == null) {
			return;
		} else {
			Object[][] newData = null;
			dataEntries = newData;
		}
		this.fireTableDataChanged();

	}

	/**
	 * reloads the id column.
	 */
	public void revalidate() {
		if (dataEntries != null) {
			Object[][] newData = new Object[dataEntries.length][nameOfColumns.length];

			for (int row = 0; row < dataEntries.length; row++) {
				for (int column = 0; column < nameOfColumns.length; column++) {
					if (column == 0) {
						newData[row][column] = row + 1;
					} else {
						newData[row][column] = dataEntries[row][column];
					}
				}
			}

			dataEntries = newData;
			this.fireTableDataChanged();
		}

		else {
			return;
		}

	}

	/**
	 * to delete selected rows
	 * 
	 * @param row
	 */
	public void deleteSelectedRow(int[] row) {
		if (dataEntries != null) {
			Object[][] newData = new Object[dataEntries.length - row.length][nameOfColumns.length];
			int rowCounter = 0;
			// Kopiere den oberen Teil:
			for (int index = 0; index < row[0]; index++) {
				rowCounter++;
				for (int column = 0; column < nameOfColumns.length; column++) {
					newData[index][column] = dataEntries[index][column];
				}
			}
			// Kopiere den unteren Teil
			for (int index = row[row.length - 1] + 1; index < dataEntries.length; index++) {
				rowCounter++;
				newData[index - row.length][0] = rowCounter;
				for (int column = 1; column < nameOfColumns.length; column++) {
					newData[index - row.length][column] = dataEntries[index][column];
				}
			}
			dataEntries = newData;
			this.fireTableDataChanged();
		} else {
			return;
		}

	}
}
