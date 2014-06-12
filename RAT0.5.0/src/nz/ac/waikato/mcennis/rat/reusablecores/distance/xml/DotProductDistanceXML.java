/**
 * DotProductDistanceXML
 * Created Jan 17, 2009 - 8:37:42 PM
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.reusablecores.distance.xml;

import java.io.Writer;
import java.io.IOException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import nz.ac.waikato.mcennis.rat.reusablecores.distance.DotProductDistance;

/**
 *
 * @author Daniel McEnnis
 */
public class DotProductDistanceXML implements DistanceXML{

    DotProductDistance distance = null;
    
    State state = State.UNINITIALIZED;
    
    public DotProductDistance getDistanceMeasure() {
        return distance;
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        state = State.LOADING;
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        distance = new DotProductDistance();
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        state = State.READY;
    }

    public State buildingStatus() {
        return state;
    }

    public DotProductDistanceXML newCopy() {
        return new DotProductDistanceXML();
    }

    public void exportAsXML(Writer writer) throws IOException{
        writer.append("<DotProductDistance/>\n");
    }
}
