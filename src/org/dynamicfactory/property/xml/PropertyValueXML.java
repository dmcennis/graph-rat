/**
 * PropertyXML
 * Created Jan 16, 2009 - 11:56:41 PM
 * Copyright Daniel McEnnis, see license.txt
 */
package org.dynamicfactory.property.xml;

import java.io.IOException;
import java.io.Writer;

import nz.ac.waikato.mcennis.rat.XMLParserObject;

/**
 *
 * @author Daniel McEnnis
 */
public interface PropertyValueXML<Type> extends XMLParserObject{

    public Type getProperty();
    
    public void setProperty(Type type);
    
    public void export(Writer writer) throws IOException;
    
    public PropertyValueXML<Type> newCopy();
    
    public String getClassName();
}
