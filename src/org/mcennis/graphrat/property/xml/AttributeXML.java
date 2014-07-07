/**
 * AttributeXMLFactory
 * Created Jan 17, 2009 - 7:03:36 PM
 * Copyright Daniel McEnnis, see license.txt
 */
/*
 *   This file is part of GraphRAT.
 *
 *   GraphRAT is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   GraphRAT is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with GraphRAT.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.mcennis.graphrat.property.xml;

import java.io.IOException;
import java.io.Writer;import java.lang.String;

import org.dynamicfactory.property.xml.PropertyValueXML;
import weka.core.Attribute;
import weka.core.FastVector;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.dynamicfactory.propertyQuery.Query.State;

/**
 *
 * @author Daniel McEnnis
 */
public class AttributeXML implements PropertyValueXML<Attribute>{

    Attribute attribute = null;
    
    State state = State.UNINITIALIZED;
    
    enum InternalState {START,NAME,VALUE};
    
    InternalState internalState = InternalState.START;
    
    String name = "";
    
    FastVector nominalValues = new FastVector();
    
    public Attribute getProperty() {
        return attribute;
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if(state == State.UNINITIALIZED){
            state = State.LOADING;
        }else if(localName.equalsIgnoreCase("Name")||qName.equalsIgnoreCase("Name")){
            internalState = InternalState.NAME;
        }else if(localName.equalsIgnoreCase("Value")||qName.equalsIgnoreCase("Value")){
            internalState = InternalState.VALUE;
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        if(internalState == InternalState.NAME){
            name = new String(ch,start,length);
        }else if(internalState == InternalState.VALUE){
            nominalValues.addElement(new String(ch,start,length));
        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        if(internalState == InternalState.NAME){
            internalState = InternalState.START;
        }else if(internalState == InternalState.VALUE){
            internalState = InternalState.START;
        }else if(internalState == InternalState.START){
            if(nominalValues.size()==0){
                attribute = new Attribute(name);
            }else{
                attribute = new Attribute(name,nominalValues);
            }
            state = State.READY;
        }
    }

    public State buildingStatus() {
        return state;
    }

    public AttributeXML newCopy() {
        return new AttributeXML();
    }

    public void setProperty(Attribute type){
        attribute = type;
    }

    public void export(Writer writer) throws IOException {
        writer.append("<Attribute>\n");
        writer.append("<Name>").append(attribute.name()).append("</Name>\n");
        for(int i=0;i<attribute.numValues();++i){
            writer.append("\t<Value>").append(attribute.value(i)).append("</Value>\n");
        }
        writer.append("</Attribute>\n");
    }
    
    public String getClassName() {
        return "Attribute";
    }

}
