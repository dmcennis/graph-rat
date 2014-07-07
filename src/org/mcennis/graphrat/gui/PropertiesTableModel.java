/*

 * Created 18-02-08

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


import org.dynamicfactory.descriptors.Properties;
import org.dynamicfactory.descriptors.Parameter;

import java.util.Iterator;
import java.util.List;

import javax.swing.table.DefaultTableModel;


/**

 * Model to back the editing of components using the EditComponent frame

 * @author Daniel McEnnis

 */

public class PropertiesTableModel extends DefaultTableModel {



    Properties parameters = null;



    /**

     * Initialize the model with the appropriate column headers.

     */

    public PropertiesTableModel() {

        super(new Object[][]{}, new Object[]{"Property Name", "Property Value"});

    }



    /**

     * Populate the model with the property values with the given array of parameters

     * @param parameters array of parameters associated with the given component

     */

    public void loadProperties(Properties parameters) {

        this.parameters = parameters;

        dataVector.clear();

        List<Parameter> parameterList = parameters.get();
        Iterator<Parameter> parameterIt = parameterList.iterator();
        if (!parameterList.isEmpty()) {

            while (parameterIt.hasNext()) {
                Parameter p = parameterIt.next();
                if(p.getValue().isEmpty()){

                    super.addRow(new Object[]{p.getType(), ""});

                }else{

                    super.addRow(new Object[]{p.getType(), p.get().toString()});

                }

            }

        }

    }



    /**

     * Return the Properties object that will save the given settings into the component

     * @return Properties object to load these settings into the component.

     */

    public Properties getProperties() {

        return parameters;
    }

    @Override
    public void setValueAt(Object aValue, int row, int column) {
        if(row ==1){
            super.setValueAt(aValue, row, column);
            parameters.set((String)super.getValueAt(0, column), aValue);
            fireTableCellUpdated(row, column);
        }
    }



    @Override

    public boolean isCellEditable(int row, int column) {

        if (column == 1) {

            return true;

        } else {

            return false;

        }

    }

}

