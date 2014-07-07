/**
 * InstancesXMLFactory
 * Created Jan 17, 2009 - 12:20:15 AM
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.graph.property.xml;

import java.io.IOException;
import java.io.Writer;

import org.dynamicfactory.property.xml.PropertyValueXML;import weka.core.Instances;
import weka.core.FastVector;
import weka.core.Attribute;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author Daniel McEnnis
 */
public class InstancesXML implements PropertyValueXML<Instances>{

    Instances instances = null;
    
    String name = "";
    
    int classIndex = -1;
    
    int dataSize = 100;
    
    FastVector attributes = new FastVector();
    
    AttributeXML attributeXML = null;
    
    State state = State.UNINITIALIZED;
    
    enum InternalState {START,NAME,CLASS_INDEX,DATA_SIZE,ATTRIBUTE};
    
    InternalState internalState = InternalState.START;
    
    public Instances getProperty() {
        return instances;
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if(state == State.UNINITIALIZED){
            state = State.LOADING;
        }else if(internalState == InternalState.ATTRIBUTE){
            attributeXML.startElement(uri, localName, qName, attributes);
        }else if(localName.equalsIgnoreCase("Name")||qName.equalsIgnoreCase("Name")){
            internalState = InternalState.NAME;
        }else if(localName.equalsIgnoreCase("ClassIndex")||qName.equalsIgnoreCase("ClassIndex")){
            internalState = InternalState.CLASS_INDEX;
        }else if(localName.equalsIgnoreCase("DataSize")||qName.equalsIgnoreCase("DataSize")){
            internalState = InternalState.DATA_SIZE;
        }else if(localName.equalsIgnoreCase("Attribute")||qName.equalsIgnoreCase("Attribute")){
            internalState = InternalState.ATTRIBUTE;
            attributeXML = new AttributeXML();
            attributeXML.startElement(uri, localName, qName, attributes);
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        if(internalState == InternalState.ATTRIBUTE){
            attributeXML.characters(ch, start, length);
        }else if(internalState == InternalState.NAME){
            name = new String(ch,start,length);
        }else if(internalState == InternalState.CLASS_INDEX){
            classIndex = Integer.parseInt(new String(ch,start,length));
        }else if(internalState == InternalState.DATA_SIZE){
            dataSize = Integer.parseInt(new String(ch,start,length));
        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        if(internalState == InternalState.NAME){
            internalState = InternalState.START;
        }else if(internalState == InternalState.CLASS_INDEX){
            internalState = InternalState.START;
        }else if(internalState == InternalState.DATA_SIZE){
            internalState = InternalState.START;
        }else if(internalState == InternalState.ATTRIBUTE){
            attributeXML.endElement(uri, localName, qName);
            if(attributeXML.buildingStatus()==State.READY){
                attributes.addElement(attributeXML.getProperty());
                internalState = InternalState.START;
            }
        }else if(internalState == InternalState.START){
            instances = new Instances(name,attributes,dataSize);
            if(classIndex > 0){
                instances.setClassIndex(classIndex);
            }
            state = State.READY;
        }
    }

    public State buildingStatus() {
        return state;
    }
    public InstancesXML newCopy() {
        return new InstancesXML();
    }

    public void setProperty(Instances type){
        instances = type;
    }

    public void export(Writer writer) throws IOException {
        writer.append("<Instances>\n");
        if(instances.classIndex() >= 0){
            writer.append("\t<ClassIndex>").append(Integer.toString(instances.classIndex())).append("</ClassIndex>\n");
        }
        writer.append("\t<DataSize>").append(Integer.toString(instances.numInstances())).append("</DataSize>\n");
        AttributeXML factory = new AttributeXML();
        for(int i=0;i<instances.numAttributes();++i){
            factory.setProperty(instances.attribute(i));
            factory.export(writer);
        }
        writer.append("</Instances>\n");
    }
    
    public String getClassName() {
        return "Instances";
    }
}