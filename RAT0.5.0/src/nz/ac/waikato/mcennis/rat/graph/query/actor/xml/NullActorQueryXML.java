/*
 * NullActorQueryXML - created 1/02/2009 - 1:50:21 AM
 * Copyright Daniel McEnnis, see license.txt
 */

package nz.ac.waikato.mcennis.rat.graph.query.actor.xml;

import nz.ac.waikato.mcennis.rat.XMLParserObject;
import nz.ac.waikato.mcennis.rat.XMLParserObject.State;
import nz.ac.waikato.mcennis.rat.graph.query.ActorQuery;
import nz.ac.waikato.mcennis.rat.graph.query.ActorQueryXML;
import nz.ac.waikato.mcennis.rat.graph.query.actor.NullActorQuery;
import nz.ac.waikato.mcennis.rat.graph.query.xml.NullQueryXML;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author Daniel McEnnis
 */
public class NullActorQueryXML implements ActorQueryXML{
    NullQueryXML core = new NullQueryXML();

    public ActorQuery getQuery() {
        return (NullActorQuery)(core.getQuery());
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

    public XMLParserObject newCopy() {
        return new NullActorQueryXML();
    }
}
