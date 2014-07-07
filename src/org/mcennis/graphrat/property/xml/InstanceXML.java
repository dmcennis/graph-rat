/**
 * InstanceXMLFactory
 * Created Jan 17, 2009 - 12:20:26 AM
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
import java.io.Writer;
import java.util.Iterator;
import java.util.LinkedList;

import org.dynamicfactory.property.xml.PropertyValueXML;import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import weka.core.Instance;
import org.dynamicfactory.propertyQuery.Query.State;


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
