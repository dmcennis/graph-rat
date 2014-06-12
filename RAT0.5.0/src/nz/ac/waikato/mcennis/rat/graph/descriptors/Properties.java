/**
 * Properties
 * Created Jan 25, 2009 - 7:45:59 PM
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.graph.descriptors;

import java.util.Collection;
import java.util.List;
import nz.ac.waikato.mcennis.rat.graph.property.Property;
import nz.ac.waikato.mcennis.rat.graph.descriptors.Parameter;

/**
 *
 * @author Daniel McEnnis
 */
public interface Properties {

    void add(String type, Object value);

    void add(String type, List value);

    void set(String type, Object value);

    void set(String type, List value);

    Properties duplicate();
    
    List<Parameter> get();

    Parameter get(String string);

    SyntaxChecker getDefaultRestriction();

    void set(Property value);

    boolean check(Parameter type);
    
    boolean check(Properties props);
    
}
