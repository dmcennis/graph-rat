/*
 * NullLinkQueryXML - created 1/02/2009 - 1:54:04 AM
 * Copyright Daniel McEnnis, see license.txt
 */

package nz.ac.waikato.mcennis.rat.graph.query.link.xml;

import org.dynamicfactory.propertyQuery.XMLParserObject;
import org.dynamicfactory.propertyQuery.Query.State;
import nz.ac.waikato.mcennis.rat.graph.query.LinkQuery;
import nz.ac.waikato.mcennis.rat.graph.query.LinkQueryXML;
import nz.ac.waikato.mcennis.rat.graph.query.link.NullLinkQuery;
import nz.ac.waikato.mcennis.rat.graph.query.xml.NullQueryXML;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author Daniel McEnnis
 */
public class NullLinkQueryXML implements LinkQueryXML{
    NullQueryXML core = new NullQueryXML();
   
    public LinkQuery getQuery() {
        return (NullLinkQuery)(core.getQuery());
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
        return new NullLinkQueryXML();
    }
}
