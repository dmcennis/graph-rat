/*
 * Created 18-02-08
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.gui;

import java.util.Properties;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import nz.ac.waikato.mcennis.rat.graph.descriptors.Parameter;

/**
 * Model to back the editing of components using the EditComponent frame
 * @author Daniel McEnnis
 */
public class PropertiesTableModel extends DefaultTableModel {

    Parameter[] parameters = null;

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
    public void loadProperties(Parameter[] parameters) {
        this.parameters = parameters;
        dataVector.clear();
        if (parameters != null) {
            for (int i = 0; i < parameters.length; ++i) {
                if(parameters[i].getValue()==null){
                    super.addRow(new Object[]{parameters[i].getName(), parameters[i].getValue()});
                }else{
                    super.addRow(new Object[]{parameters[i].getName(), parameters[i].getValue().toString()});
                }
            }
        }
    }

    /**
     * Return the Properties object that will save the given settings into the component
     * @return Properties object to load these settings into the component.
     */
    public Properties getProperties() {
        Properties ret = new Properties();
        if (parameters != null) {
            for (int i = 0; i < parameters.length; ++i) {
                String value = (String) ((Vector) this.getDataVector().get(i)).get(1);
                if(parameters[i].getValue() == null){
                    if((value != null)&&(!(value.equals("")))){
                        ret.setProperty(parameters[i].getName(),value);
                    }
                }else {// if(!(value.equals(parameters[i].getValue().toString()))) {
                    ret.setProperty(parameters[i].getName(), value);
                }
            }
        }
        return ret;
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
