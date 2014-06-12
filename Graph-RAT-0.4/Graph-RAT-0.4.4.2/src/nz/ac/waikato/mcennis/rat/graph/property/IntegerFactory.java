/**
 *
 * Created Jun 8, 2008 - 1:37:37 AM
 * Copyright Daniel McEnnis, see license.txt
 *
 */
package nz.ac.waikato.mcennis.rat.graph.property;

import nz.ac.waikato.mcennis.rat.graph.Graph;

/**
 * Factory for serializing and deserializing Integer objects
 * @author Daniel McEnnis
 */
public class IntegerFactory implements PropertyValueFactory<Integer>{

    @Override
    public Integer importFromString(String data, Graph g) {
        return Integer.parseInt(data);
    }

    @Override
    public String exportToString(Integer type, Graph g) {
        return type.toString();
    }

}
