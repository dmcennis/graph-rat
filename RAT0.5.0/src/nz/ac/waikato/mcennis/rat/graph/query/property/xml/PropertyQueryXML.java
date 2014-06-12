/**
 * QueryXMLLoader
 * Created Jan 12, 2009 - 8:15:21 PM
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.graph.query.property.xml;

import nz.ac.waikato.mcennis.rat.XMLParserObject;
import nz.ac.waikato.mcennis.rat.graph.query.property.PropertyQuery;
import nz.ac.waikato.mcennis.rat.graph.query.Query;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author Daniel McEnnis
 */
public interface PropertyQueryXML extends XMLParserObject{
    
    public PropertyQuery getQuery();
}
