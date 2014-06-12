/**
 * DoubleArrayXML
 * Created Jan 16, 2009 - 11:24:40 PM
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.reusablecores.datavector.xml;

import nz.ac.waikato.mcennis.rat.reusablecores.datavector.DataVector;
import nz.ac.waikato.mcennis.rat.reusablecores.datavector.DoubleArrayDataVector;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author Daniel McEnnis
 */
public class DoubleArrayVectorXML implements DataVectorXML{

    DoubleArrayDataVector vector = null;
    
    int size = -1;
    
    int pos=0;
    
    double[] data = null;
    
    enum InternalState {START,SIZE,DATA};
    
    InternalState internalState = InternalState.START;
    
    State state = State.UNINITIALIZED;
    
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        state = State.LOADING;
        if(localName.equalsIgnoreCase("Size")||qName.equalsIgnoreCase("Size")){
            internalState = InternalState.SIZE;
        }else if(localName.equalsIgnoreCase("Data")||qName.equalsIgnoreCase("Data")){
            internalState = InternalState.DATA;
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        if(internalState == InternalState.SIZE){
            size = Integer.parseInt(new String(ch,start,length));
            data = new double[size];
            java.util.Arrays.fill(data, Double.NaN);
        }else{
            data[pos++] = Double.parseDouble(new String(ch,start,length));
        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        if(internalState == InternalState.SIZE){
            internalState = InternalState.START;
        }else if(internalState == InternalState.DATA){
            internalState = InternalState.START;
        }else{
            vector = new DoubleArrayDataVector(data);
            state = State.READY;
        }
    }

    public DoubleArrayDataVector getDataVector() {
        return vector;
    }
    
    public State buildingStatus(){
        return state;
    }

    public DoubleArrayVectorXML newCopy() {
        return new DoubleArrayVectorXML();
    }
}
