/*
 * Copyright Daniel McEnnis, see license.txt
 */

package org.dynamicfactory.property;

import org.mcennis.graphrat.graph.Graph;

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
