/**
 * GraphByActorXML
 * Created Jan 12, 2009 - 10:28:30 PM
 * Copyright Daniel McEnnis, see license.txt
 */
package org.mcennis.graphrat.query.graph.xml;

import org.dynamicfactory.propertyQuery.Query.State;
import org.mcennis.graphrat.query.GraphQuery;
import org.mcennis.graphrat.query.GraphQueryXML;
import org.mcennis.graphrat.query.graph.AllGraphs;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author Daniel McEnnis
 */
public class AllGraphsXML  implements GraphQueryXML{

    AllGraphs query = new AllGraphs();

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
       ;
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        ;
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        query.buildQuery();
    }

    public State buildingStatus() {
        return query.buildingStatus();
    }

    public GraphQuery getQuery() {
        return query;
    }

    public AllGraphsXML newCopy() {
        return new AllGraphsXML();
    }
}
