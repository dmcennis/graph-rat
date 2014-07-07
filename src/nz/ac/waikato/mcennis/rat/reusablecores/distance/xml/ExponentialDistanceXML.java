/**
 * ExponentialDistanceXML
 * Created Jan 19, 2009 - 10:02:53 PM
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.reusablecores.distance.xml;

import java.io.IOException;
import java.io.Writer;
import org.dynamicfactory.propertyQuery.XMLParserObject;
import org.dynamicfactory.propertyQuery.Query.State;
import nz.ac.waikato.mcennis.rat.reusablecores.distance.ExponentialDistance;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author Daniel McEnnis
 */
public class ExponentialDistanceXML implements DistanceXML{

    ExponentialDistance exponent = null;
    
    State state = State.UNINITIALIZED;
    
    enum InternalState {START, PB, PE, NB, NE};
    
    InternalState internalState = InternalState.START;
    
    public ExponentialDistance getDistanceMeasure() {
        return exponent;
    }

    public void exportAsXML(Writer writer) throws IOException {
        writer.append("<ExponentialDistance>\n");
        writer.append("\t<PositiveBase>").append(Double.toString(exponent.getPositiveBase())).append("</PositiveBase>\n");
        writer.append("\t<PositiveExponent>").append(Double.toString(exponent.getPositiveExponent())).append("</PositiveExponent>\n");
        writer.append("\t<NegativeBase>").append(Double.toString(exponent.getNegativeBase())).append("</NegativeBase>\n");
        writer.append("\t<NegativeExponent>").append(Double.toString(exponent.getNegativeExponent())).append("</NegativeExponent>\n");
        writer.append("</ExponentialDistance>\n");
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if(localName.equalsIgnoreCase("PositiveBase")||qName.equalsIgnoreCase("PositiveBase")){
            internalState = InternalState.PB;
        }else if(localName.equalsIgnoreCase("PositiveExponent")||qName.equalsIgnoreCase("PositiveExponent")){
            internalState = InternalState.PE;            
        }else if(localName.equalsIgnoreCase("NegativeBase")||qName.equalsIgnoreCase("NegativeBase")){
            internalState = InternalState.NB;            
        }else if(localName.equalsIgnoreCase("NegativeExponent")||qName.equalsIgnoreCase("NegativeExponent")){
            internalState = InternalState.NE;            
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        if(internalState == InternalState.PB){
            exponent.setPositiveBase(Double.parseDouble(new String(ch,start,length)));
        }else if(internalState == InternalState.PE){
            exponent.setPositiveExponent(Double.parseDouble(new String(ch,start,length)));            
        }if(internalState == InternalState.NB){
            exponent.setNegativeBase(Double.parseDouble(new String(ch,start,length)));           
        }if(internalState == InternalState.NE){
            exponent.setNegativeExponent(Double.parseDouble(new String(ch,start,length)));            
        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        if(internalState == InternalState.PB){
            internalState = InternalState.START;
        }else if(internalState == InternalState.PE){
            internalState = InternalState.START;
        }else if(internalState == InternalState.NB){
            internalState = InternalState.START;
        }else if(internalState == InternalState.NE){
            internalState = InternalState.START;
        }else if(internalState == InternalState.START){
            state = State.READY;
        }
    }

    public State buildingStatus() {
        return state;
    }

    public ExponentialDistanceXML newCopy() {
        return new ExponentialDistanceXML();
    }

}
