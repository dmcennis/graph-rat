/**
 * StringXMLFactory
 * Created Jan 17, 2009 - 12:19:38 AM
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.graph.property.xml;

import java.io.IOException;
import java.io.Writer;
import java.util.Properties;
import nz.ac.waikato.mcennis.rat.XMLParserObject.State;
import nz.ac.waikato.mcennis.rat.graph.property.Property;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author Daniel McEnnis
 */
public class StringXML implements PropertyValueXML<String>{

    String value = "";
    
    State state = State.UNINITIALIZED;
    
    public String getProperty() {
        return value;
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        state = State.LOADING;
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        value = new String(ch,start,length);
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        state = State.READY;
    }

    public State buildingStatus() {
        return state;
    }

    public StringXML newCopy() {
        return new StringXML();
    }

    public void export(Writer writer) throws IOException {
        writer.append("<String>").append(value).append("</String>\n");
    }

    public void setProperty(String type) {
        value = type;
    }
    
    public String getClassName() {
        return "String";
    }
}
