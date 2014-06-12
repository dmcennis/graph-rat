/**
 * InstanceXMLFactory
 * Created Jan 17, 2009 - 12:20:26 AM
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.graph.property.xml;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.LinkedList;
import nz.ac.waikato.mcennis.rat.XMLParserObject.State;
import nz.ac.waikato.mcennis.rat.graph.property.Property;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import weka.core.Instance;

/**
 *
 * @author Daniel McEnnis
 */
public class InstanceXML implements PropertyValueXML<Instance>{

    Instance instance = null;
    
    State state = State.UNINITIALIZED;
    
    double weight;
    
    LinkedList<Double> values = new LinkedList<Double>();
    
    enum InternalState {START,WEIGHT,DATA};
    
    InternalState internalState = InternalState.START;
    
    public Instance getProperty() {
        return instance;
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if(state == State.UNINITIALIZED){
            state = State.LOADING;
        }else if(localName.equalsIgnoreCase("Weight")||qName.equalsIgnoreCase("Weight")){
            
        }else if(localName.equalsIgnoreCase("Data")||qName.equalsIgnoreCase("Data")){
            
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        if(internalState == InternalState.WEIGHT){
            weight = Double.parseDouble(new String(ch,start,length));
        }else if(internalState == InternalState.DATA){
            values.add(Double.parseDouble(new String(ch,start,length)));
        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        if(internalState == InternalState.WEIGHT){
            internalState = InternalState.START;
        }else if(internalState == InternalState.DATA){
            internalState = InternalState.START;
        }else if(internalState == InternalState.START){
            double[] data = new double[values.size()];
            Iterator<Double> it = values.iterator();
            int count=0;
            while(it.hasNext()){
                data[count++]=it.next();
            }
            instance = new Instance(weight,data);
        }
    }

    public State buildingStatus() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    public InstanceXML newCopy() {
        return new InstanceXML();
    }

    public void setProperty(Instance type){
        instance = type;
    }

    public void export(Writer writer) throws IOException {
        writer.append("<Instance>\n");
        writer.append("\t<Weight>").append(Double.toString(instance.weight())).append("</Weight>\n");
        for(int i=0;i<instance.numValues();++i){
            writer.append("\t<Data>").append(Double.toString(instance.value(i))).append("</Data>\n");
        }
        writer.append("</Instance>\n");
    }
    
    public String getClassName() {
        return "Instance";
    }
}
