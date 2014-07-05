/**
 * BasicPropertyXML
 * Created Jan 30, 2009 - 5:36:16 PM
 * Copyright Daniel McEnnis, see license.txt
 */
package org.dynamicfactory.property.xml;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;

import org.dynamicfactory.property.BasicProperty;
import org.dynamicfactory.property.Property;
import org.dynamicfactory.property.PropertyValueXMLFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author Daniel McEnnis
 */
public class BasicPropertyXML implements PropertyXML{

    BasicProperty property = null;
    PropertyValueXML value = null;
    String propertyClass = "String";
    String type = "";
    State state = State.UNINITIALIZED;
    enum InternalState {START,CLASS,TYPE};
    InternalState internalState = InternalState.START;
    
    public void export(Writer writer) throws IOException{
        List content = property.getValue();
        propertyClass = property.getPropertyClass().getSimpleName();
        value = PropertyValueXMLFactory.newInstance().create(propertyClass);
        writer.append("<Property Class=\"BasicProperty\">\n");
        writer.append("\t<Type>").append(property.getType()).append("\t</Type>\n");
        writer.append("\t<Class>").append(propertyClass).append("</Class>\n");
        Iterator it = content.iterator();
        while(it.hasNext()){
            value.setProperty(it.next());
            value.export(writer);
        }
        writer.append("</Property>\n");
        
    }

    public Property getProperty() {
        return property;
    }

    public PropertyXML newCopy() {
        return new BasicPropertyXML();
    }

    public void setProperty(Property property) {
        this.property = (BasicProperty)property;
    }

    public State buildingStatus() {
        return state;
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        if(value != null){
            value.characters(ch,start,length);
        }else if(internalState==InternalState.CLASS){
            propertyClass = new String(ch,start,length);
        }else if(internalState == InternalState.TYPE){
            type = new String(ch,start,length);
        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        if(value != null){
            value.endElement(uri, localName, qName);
            if(value.buildingStatus() == State.READY){
                property.add(value.getProperty());
                value = null;
            }
        }else if(internalState == InternalState.CLASS){
            internalState = InternalState.START;
        }else if(internalState == InternalState.TYPE){
            internalState = InternalState.START;
        }else if(internalState == InternalState.START){
            state = State.READY;
        }
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        state=State.LOADING;
        if(value != null){
            value.startElement(uri, localName, qName, attributes);
        }else if(localName.equalsIgnoreCase("Class")||qName.equalsIgnoreCase("Class")){
            internalState = InternalState.CLASS;
        }else if(localName.equalsIgnoreCase("Type")||qName.equalsIgnoreCase("Type")){
            internalState = InternalState.TYPE;
        }else{
            String name = localName;
            if((localName==null)||(localName.contentEquals(""))){
                name = qName;
            }
            value = PropertyValueXMLFactory.newInstance().create(propertyClass);
            value.startElement(uri, localName, qName, attributes);
        }
    }
}
