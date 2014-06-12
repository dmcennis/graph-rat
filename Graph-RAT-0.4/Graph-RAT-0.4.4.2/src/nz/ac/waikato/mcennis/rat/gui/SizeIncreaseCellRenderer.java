/*
 * Created 19-02-08
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.gui;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Class that increases font sizes to a larger font over the default.
 * @author Daniel McEnnis
 */
public class SizeIncreaseCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel tmp = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
                    row, column);
            tmp.setFont(tmp.getFont().deriveFont((float)16.0));
            return tmp;
    }
}
