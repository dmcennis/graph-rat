/*
 * NullActorQueryXML - created 1/02/2009 - 1:50:21 AM
 * Copyright Daniel McEnnis, see license.txt
 */

package org.mcennis.graphrat.query.actor.xml;

import org.dynamicfactory.propertyQuery.XMLParserObject;
import org.dynamicfactory.propertyQuery.Query.State;
import org.mcennis.graphrat.query.ActorQuery;
import org.mcennis.graphrat.query.ActorQueryXML;
import org.mcennis.graphrat.query.actor.NullActorQuery;
import org.mcennis.graphrat.query.xml.NullQueryXML;
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
