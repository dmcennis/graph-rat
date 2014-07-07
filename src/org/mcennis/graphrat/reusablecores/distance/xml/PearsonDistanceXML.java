/**
 * PearsonDistanceXML
 * Created Jan 17, 2009 - 8:38:38 PM
 * Copyright Daniel McEnnis, see license.txt
 */
package org.mcennis.graphrat.reusablecores.distance.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import java.io.Writer;
import java.io.IOException;
import org.mcennis.graphrat.reusablecores.distance.PearsonDistance;
import org.dynamicfactory.propertyQuery.Query.State;

/**
 *
 * @author Daniel McEnnis
 */
public class PearsonDistanceXML implements DistanceXML{
    PearsonDistance distance = null;
    
    State state = State.UNINITIALIZED;
    
    public PearsonDistance getDistanceMeasure() {
        return distance;
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        state = State.LOADING;
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        distance = new PearsonDistance();
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        state = State.READY;
    }

    public State buildingStatus() {
        return state;
    }

    public PearsonDistanceXML newCopy() {
        return new PearsonDistanceXML();
    }

    public void exportAsXML(Writer writer) throws IOException{
        writer.append("<PearsonDistance/>\n");
    }
}
