/**
 * DoubleXMLFactory
 * Created Jan 17, 2009 - 12:20:49 AM
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
public class DoubleXML implements PropertyValueXML<Double>{

    double value = Double.NaN;
    
    State state = State.UNINITIALIZED;
    
    public Double getProperty() {
        return value;
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        state = State.LOADING;
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        value = Double.parseDouble(new String(ch,start,length));
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        state = State.READY;
    }

    public State buildingStatus() {
        return state;
    }

    public DoubleXML newCopy() {
        return new DoubleXML();
    }

    public void setProperty(Double type){
        value = type;
    }

    public void export(Writer writer) throws IOException {
        writer.append("<Double>").append(Double.toString(value)).append("</Double>\n");
    }
    
    public String getClassName() {
        return "Double";
    }
}
