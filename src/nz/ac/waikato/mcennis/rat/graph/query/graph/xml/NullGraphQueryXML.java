/*
 * NullGraphQueryXML - created 1/02/2009 - 1:58:40 AM
 * Copyright Daniel McEnnis, see license.txt
 */

package nz.ac.waikato.mcennis.rat.graph.query.graph.xml;

import nz.ac.waikato.mcennis.rat.XMLParserObject.State;
import nz.ac.waikato.mcennis.rat.graph.query.GraphQuery;
import nz.ac.waikato.mcennis.rat.graph.query.GraphQueryXML;
import nz.ac.waikato.mcennis.rat.graph.query.graph.NullGraphQuery;
import nz.ac.waikato.mcennis.rat.graph.query.xml.NullQueryXML;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author Daniel McEnnis
 */
public class NullGraphQueryXML implements GraphQueryXML{
    NullQueryXML core = new NullQueryXML();

    public GraphQuery getQuery() {
        return (NullGraphQuery)(core.getQuery());
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        core.startElement(uri, localName, qName, attributes);
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        core.characters(ch, start, length);
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        core.endElement(uri, localName, qName);
    }

    public State buildingStatus() {
        return core.buildingStatus();
    }

    public NullGraphQueryXML newCopy() {
        return new NullGraphQueryXML();
    }
}
