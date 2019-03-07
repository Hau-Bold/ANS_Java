package lookup;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class LookUpRenderer extends DefaultTableCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {

		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		if (column > 0) {

			String x = (String) table.getModel().getValueAt(row, column);
			if (!x.trim().equals("")) {

				setForeground(Color.RED);
			}

		} else {
			setForeground(Color.BLACK);
		}
		if (isSelected) {
			setBackground(Color.GREEN);
		}
		setVerticalAlignment(JLabel.CENTER);
		setHorizontalAlignment(JLabel.CENTER);

		return this;
	}

}
