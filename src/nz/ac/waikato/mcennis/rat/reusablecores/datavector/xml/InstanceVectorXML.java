/**
 * InstanceVectorXML
 * Created Jan 16, 2009 - 11:25:02 PM
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.reusablecores.datavector.xml;

import org.dynamicfactory.property.PropertyValueXMLFactory;
import nz.ac.waikato.mcennis.rat.reusablecores.datavector.DataVector;
import nz.ac.waikato.mcennis.rat.reusablecores.datavector.InstanceDataVector;
import org.dynamicfactory.property.xml.InstanceXML;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import weka.core.Instance;

/**
 *
 * @author Daniel McEnnis
 */
public class InstanceVectorXML implements DataVectorXML{
    InstanceDataVector instanceVector = null;
    InstanceXML instance = null;
    
    enum InternalState {START,INSTANCE};
    
    InternalState internalState = InternalState.START;
    
    State state = State.UNINITIALIZED;

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if(internalState == internalState.INSTANCE){
            instance.startElement(uri, localName, qName, attributes);
        }else if(localName.equalsIgnoreCase("Instance")||qName.equalsIgnoreCase("Instance")){
            internalState = internalState.INSTANCE;
            instance = (InstanceXML) PropertyValueXMLFactory.newInstance().create(Instance.class);
            instance.startElement(uri, localName, qName, attributes);
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        if(internalState == internalState.INSTANCE){
            instance.characters(ch, start, length);
        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        if(internalState == internalState.INSTANCE){
            instance.endElement(uri, localName, qName);
            if(instance.buildingStatus() == State.READY){
                internalState = InternalState.START;
            }
        }else{
            instanceVector = new InstanceDataVector(instance.getProperty());
            state = State.READY;
        }
    }

    public DataVector getDataVector() {
        return instanceVector;
    }
    
    public State buildingStatus(){
        return state;
    }

    public InstanceVectorXML newCopy() {
        return new InstanceVectorXML();
    }
}
