/*
 * Created 2-2-08
 * Copyright Daniel McEnnis, see license.txt
 */

package nz.ac.waikato.mcennis.rat.graph.property;

import nz.ac.waikato.mcennis.rat.graph.Graph;

/**
 * Factory for serializing and deserializing Double objects
 * @author Daniel McEnnis
 */
public class DoubleFactory implements PropertyValueFactory<Double>{

    @Override
    public Double importFromString(String data, Graph g) {
        return Double.parseDouble(data);
    }

    @Override
    public String exportToString(Double type, Graph g) {
        return Double.toString(type);
    }
    
}