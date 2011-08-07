package view;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class PrimeCellBackgroundRenderer extends DefaultTableCellRenderer {

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		Component c = super.getTableCellRendererComponent(table, value,
				isSelected, hasFocus, row, column);

		if (table.getValueAt(row, 1).equals("yes")) {
			setBackground(Color.RED);
		} else if (table.isRowSelected(row)) {
			setBackground(new Color(200,255,255));
		} else {
			setBackground(null);
		}

		return c;
	}

}
