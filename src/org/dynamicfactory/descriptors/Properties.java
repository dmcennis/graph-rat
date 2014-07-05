/**
 * Properties
 * Created Jan 25, 2009 - 7:45:59 PM
 * Copyright Daniel McEnnis, see license.txt
 */
package org.dynamicfactory.descriptors;

import java.util.List;
import org.dynamicfactory.property.Property;

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
