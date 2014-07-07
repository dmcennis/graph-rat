/*
 * Created 19-02-08
 * Copyright Daniel McEnnis, see license.txt
 */
package org.mcennis.graphrat.gui;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Renderer for displaying property names in bold and property values in normal font
 * @author Daniel McEnnis
 */
public class PropertiesCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (column == 0) {
            JLabel tmp = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
                    row, column);
            tmp.setFont(tmp.getFont().deriveFont(java.awt.Font.BOLD));
            return tmp;
        } else {
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
                    row, column);
        }
    }
}
