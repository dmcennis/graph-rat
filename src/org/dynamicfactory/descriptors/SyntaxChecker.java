/**
 * SyntaxChecker
 * Created Jan 25, 2009 - 7:38:02 PM
 * Copyright Daniel McEnnis, see license.txt
 */
package org.dynamicfactory.descriptors;

import java.util.List;
import org.dynamicfactory.property.Property;

/**
 *
 * @author Daniel McEnnis
 */
public interface SyntaxChecker {

    public boolean check(Property property);

    public boolean check(Property property,Object value);
    
    public boolean check(String type, Object value);
    
    public boolean check(String type, List value);
    
    public boolean check(Property property,List value);
    
    public boolean check(Parameter parameter);
    
    public int getMinCount();
    
    public int getMaxCount();
    
    public Class getClassType();
}
