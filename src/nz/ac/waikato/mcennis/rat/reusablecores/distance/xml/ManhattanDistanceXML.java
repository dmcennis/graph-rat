/**
 * ManhattanDistanceXML
 * Created Jan 17, 2009 - 8:38:24 PM
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.reusablecores.distance.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import java.io.Writer;
import java.io.IOException;
import nz.ac.waikato.mcennis.rat.reusablecores.distance.ManhattanDistance;

/**
 *
 * @author Daniel McEnnis
 */
public class ManhattanDistanceXML implements DistanceXML{
    ManhattanDistance distance = null;
    
    State state = State.UNINITIALIZED;
    
    public ManhattanDistance getDistanceMeasure() {
        return distance;
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        state = State.LOADING;
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        distance = new ManhattanDistance();
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        state = State.READY;
    }

    public State buildingStatus() {
        return state;
    }

    public ManhattanDistanceXML newCopy() {
        return new ManhattanDistanceXML();
    }

    public void exportAsXML(Writer writer) throws IOException{
        writer.append("<ManhattanDistance/>\n");
    }
}
