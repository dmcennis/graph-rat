/**
 * WeightedKLDDistanceXML
 * Created Jan 17, 2009 - 8:38:53 PM
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.reusablecores.distance.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import java.io.Writer;
import java.io.IOException;
import nz.ac.waikato.mcennis.rat.reusablecores.distance.WeightedKLDDistance;
import org.dynamicfactory.propertyQuery.Query.State;

/**
 *
 * @author Daniel McEnnis
 */
public class WeightedKLDDistanceXML implements DistanceXML{
    WeightedKLDDistance distance = null;
    
    State state = State.UNINITIALIZED;
    
    public WeightedKLDDistance getDistanceMeasure() {
        return distance;
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        state = State.LOADING;
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        distance = new WeightedKLDDistance();
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        state = State.READY;
    }

    public State buildingStatus() {
        return state;
    }

    public WeightedKLDDistanceXML newCopy() {
        return new WeightedKLDDistanceXML();
    }

    public void exportAsXML(Writer writer) throws IOException{
        writer.append("<WeightedKLDDistance/>\n");
    }
}
