/**
 * PropertyXML
 * Created Jan 30, 2009 - 5:14:22 PM
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.graph.property.xml;

import java.io.IOException;
import nz.ac.waikato.mcennis.rat.XMLParserObject;
import nz.ac.waikato.mcennis.rat.graph.property.Property;
import nz.ac.waikato.mcennis.rat.graph.property.xml.PropertyXML;
import java.io.Writer;

/**
 *
 * @author Daniel McEnnis
 */
public interface PropertyXML extends XMLParserObject{

    public Property getProperty();
    
    public void setProperty(Property property);
    
    public void export(Writer output) throws IOException;
    
    public PropertyXML newCopy();
}
