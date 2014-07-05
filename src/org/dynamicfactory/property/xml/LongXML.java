/**
 * LongXMLFactory
 * Created Jan 17, 2009 - 12:19:51 AM
 * Copyright Daniel McEnnis, see license.txt
 */
package org.dynamicfactory.property.xml;

import java.io.IOException;
import java.io.Writer;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author Daniel McEnnis
 */
public class LongXML implements PropertyValueXML<Long>{

    long value = 0;
    
    State state = State.UNINITIALIZED;
    
    public Long getProperty() {
        return value;
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        state = State.LOADING;
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        value = Long.parseLong(new String(ch,start,length));
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        state = State.READY;
    }

    public State buildingStatus() {
        return state;
    }

    public LongXML newCopy() {
        return new LongXML();
    }

    public void setProperty(Long type){
        value = type;
    }

    public void export(Writer writer) throws IOException {
        writer.append("<Long>").append(Long.toString(value)).append("</Long>\n");
    }
    
    public String getClassName() {
        return "Long";
    }
}
