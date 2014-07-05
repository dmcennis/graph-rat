/**
 * PropertiesInternal
 * Created Jan 25, 2009 - 7:48:09 PM
 * Copyright Daniel McEnnis, see license.txt
 */
package org.dynamicfactory.descriptors;

/**
 *
 * @author Daniel McEnnis
 */
public interface PropertiesInternal extends Properties{

    void add(ParameterInternal parameter);

    void add(String type,Object value);
        
    void remove(String type);
    
    void replace(Parameter type);

    void setDefaultRestriction(SyntaxObject restriction);

    SyntaxObject getDefaultRestriction();
    
    PropertiesInternal duplicate();

    ParameterInternal get(String string);

    PropertiesInternal merge(Properties right);
}
