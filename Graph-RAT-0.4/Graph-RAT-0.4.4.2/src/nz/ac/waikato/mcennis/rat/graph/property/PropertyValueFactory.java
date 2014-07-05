/*
 * Copyright Daniel McEnnis, see license.txt
 */

package org.dynamicfactory.property;

import nz.ac.waikato.mcennis.rat.graph.Graph;

/**
 * A factory interface for importing and exporting a given java data type from
 * Strings. Must use only non-XML characters.
 * 
 * @author Daniel McEnnis
 */
public interface PropertyValueFactory<Type> {
    /**
     * Create an instance of this type from a string.
     * @param data string containing the data used to create the object
     * @param g graph context of the string
     * @return newly constructed object
     */
    public Type importFromString(String data, Graph g);
    /**
     * Create a string representing the contents of this object.
     * @param type object to be serialized
     * @param g graph context of the object
     * @return string representing the content of the object
     */
    public String exportToString(Type type, Graph g);
}
