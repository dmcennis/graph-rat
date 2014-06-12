/*
 * AndActorQueryXML - created 1/02/2009 - 2:24:24 AM
 * Copyright Daniel McEnnis, see license.txt
 */

package nz.ac.waikato.mcennis.rat.graph.query.link.xml;

import nz.ac.waikato.mcennis.rat.graph.query.LinkQuery;
import nz.ac.waikato.mcennis.rat.graph.query.LinkQueryXML;
import nz.ac.waikato.mcennis.rat.graph.query.link.OrLinkQuery;
import nz.ac.waikato.mcennis.rat.graph.query.xml.OrQueryXML;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author Daniel McEnnis
 */
public class OrLinkQueryXML implements LinkQueryXML{
    OrQueryXML core = new OrQueryXML();

    public LinkQuery getQuery() {
        return (OrLinkQuery)(core.getQuery());
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

    public OrLinkQueryXML newCopy() {
        return new OrLinkQueryXML();
    }
}
