/*
 * AndActorQueryXML - created 1/02/2009 - 2:24:24 AM
 * Copyright Daniel McEnnis, see license.txt
 */

package nz.ac.waikato.mcennis.rat.graph.query.graph.xml;

import nz.ac.waikato.mcennis.rat.XMLParserObject.State;
import nz.ac.waikato.mcennis.rat.graph.query.GraphQuery;
import nz.ac.waikato.mcennis.rat.graph.query.GraphQueryXML;
import nz.ac.waikato.mcennis.rat.graph.query.graph.OrGraphQuery;
import nz.ac.waikato.mcennis.rat.graph.query.xml.OrQueryXML;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author Daniel McEnnis
 */
public class OrGraphQueryXML implements GraphQueryXML{
    OrQueryXML core = new OrQueryXML();

    public GraphQuery getQuery() {
        return (OrGraphQuery)(core.getQuery());
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

    public OrGraphQueryXML newCopy() {
        return new OrGraphQueryXML();
    }
}
