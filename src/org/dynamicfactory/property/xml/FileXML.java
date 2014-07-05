/**
 * FileXMLFactory
 * Created Jan 17, 2009 - 12:20:39 AM
 * Copyright Daniel McEnnis, see license.txt
 */
package org.dynamicfactory.property.xml;

import java.io.File;
import java.io.IOException;
import java.io.Writer;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author Daniel McEnnis
 */
public class FileXML implements PropertyValueXML<File>{

    File file = null;
    
    State state = State.UNINITIALIZED;
    
    public File getProperty() {
        return file;
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        state = State.LOADING;
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        file = new File(new String(ch,start,length));
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        state = State.READY;
    }

    public State buildingStatus() {
        return state;
    }

    public FileXML newCopy() {
        return new FileXML();
    }

    public void setProperty(File type){
        file = type;
    }

    public void export(Writer writer) throws IOException {
        writer.append("<File>").append(file.getAbsolutePath()).append("</FILE>\n");
    }

    
    public String getClassName() {
        return "File";
    }
}
