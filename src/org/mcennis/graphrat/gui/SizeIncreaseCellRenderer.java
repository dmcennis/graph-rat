/*

 * Created 19-02-08

 * Copyright Daniel McEnnis, see license.txt

 */

/*
 *   This file is part of GraphRAT.
 *
 *   GraphRAT is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   GraphRAT is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with GraphRAT.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.mcennis.graphrat.gui;



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

