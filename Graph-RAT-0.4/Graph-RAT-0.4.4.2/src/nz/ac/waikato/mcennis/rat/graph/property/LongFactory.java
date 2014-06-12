/*
 * Copyright Daniel McEnnis, see license.txt
 */

package nz.ac.waikato.mcennis.rat.graph.property;

import nz.ac.waikato.mcennis.rat.graph.Graph;

/**
 * Factory object for serializing and deserializing a Long object
 * @author Daniel McEnnis
 */
public class LongFactory implements PropertyValueFactory<Long>{

    @Override
    public Long importFromString(String data, Graph g) {
        return Long.parseLong(data);
    }

    @Override
    public String exportToString(Long type, Graph g) {
        return Long.toString(type);
    }
    
}
