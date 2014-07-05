/**
 * ChebyshevDistanceXML
 * Created Jan 17, 2009 - 8:37:12 PM
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.reusablecores.distance.xml;

import java.io.Writer;
import java.io.IOException;
import nz.ac.waikato.mcennis.rat.XMLParserObject.State;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import nz.ac.waikato.mcennis.rat.reusablecores.distance.ChebyshevDistance;
import nz.ac.waikato.mcennis.rat.reusablecores.distance.DistanceFunction;

/**
 *
 * @author Daniel McEnnis
 */
public class ChebyshevDistanceXML implements DistanceXML{

    ChebyshevDistance distance = null;
    
    State state = State.UNINITIALIZED;
    
    public ChebyshevDistance getDistanceMeasure() {
        return distance;
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        state = State.LOADING;
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        distance = new ChebyshevDistance();
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        state = State.READY;
    }

    public State buildingStatus() {
        return state;
    }

    public ChebyshevDistanceXML newCopy() {
        return new ChebyshevDistanceXML();
    }

    public void exportAsXML(Writer writer) throws IOException{
        writer.append("<ChebyshevDistance/>\n");
    }
}
