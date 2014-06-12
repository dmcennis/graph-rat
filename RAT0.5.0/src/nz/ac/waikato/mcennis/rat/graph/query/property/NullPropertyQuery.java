/**
 * NullQuery
 * Created Jan 11, 2009 - 3:19:46 PM
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.graph.query.property;

import java.io.Reader;
import java.io.Writer;
import java.util.Properties;
import nz.ac.waikato.mcennis.rat.graph.property.Property;
import nz.ac.waikato.mcennis.rat.XMLParserObject.State;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author Daniel McEnnis
 */
public class NullPropertyQuery implements PropertyQuery{

    boolean ret = false;
    
    transient State state = State.UNINITIALIZED;
    
    public boolean execute(Property property) {
        return ret;
    }

    public void exportQuery(Writer writer) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void importQuery(Reader reader) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void buildQuery(boolean ret) {
        state = State.LOADING;
        this.ret = ret;
        state = State.READY;
    }

    public int compareTo(Object o) {
        return this.getClass().getName().compareTo(o.getClass().getName());
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public State buildingStatus() {
        return state;
    }

    public NullPropertyQuery prototype() {
        return new NullPropertyQuery();
    }
}
