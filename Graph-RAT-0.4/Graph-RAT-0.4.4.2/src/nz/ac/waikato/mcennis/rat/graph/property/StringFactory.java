/*
 * Copyright Daniel McEnnis, see license.txt
 */

package nz.ac.waikato.mcennis.rat.graph.property;

import nz.ac.waikato.mcennis.rat.graph.Graph;

/**
 * Factory for serializing and deserializing string objects
 * @author Daniel McEnnis
 */
public class StringFactory implements PropertyValueFactory<String>{

    @Override
    public String importFromString(String data, Graph g) {
        return data;
    }

    @Override
    public String exportToString(String type, Graph g) {
        return type;
    }    
}
