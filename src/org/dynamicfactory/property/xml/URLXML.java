/**
 * URLXMLFactory
 * Created Jan 17, 2009 - 12:19:28 AM
 * Copyright Daniel McEnnis, see license.txt
 */
package org.dynamicfactory.property.xml;

import java.io.IOException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author Daniel McEnnis
 */
public class URLXML implements PropertyValueXML<URL>{

    URL location;
        
    Properties props;
    
    State state = State.UNINITIALIZED;
    
    public URL getProperty() {
        if(state == State.READY){
            return location;
        }else{
            return null;
        }
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        state = State.LOADING;
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        try {
            location = new URL(new String(ch, start, length));
        } catch (MalformedURLException ex) {
            Logger.getLogger(URLXML.class.getName()).log(Level.SEVERE, "URL '"+new String(ch,start,length)+"' is a malformed URL", ex);
        }
        
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        state = State.READY;
    }

    public State buildingStatus() {
        return state;
    }

    public URLXML newCopy() {
        return new URLXML();
    }

    public void export(Writer writer) throws IOException {
        writer.append("<URL>").append(location.toString()).append("</URL>\n");
    }

    public void setProperty(URL type) {
        location = type;
    }

    
    public String getClassName() {
        return "URL";
    }
}
