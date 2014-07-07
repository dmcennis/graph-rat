/**
 * DataVectorQueryXML
 * Created Jan 12, 2009 - 8:13:15 PM
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
package org.mcennis.graphrat.query.property.xml;

import org.mcennis.graphrat.reusablecores.datavector.DataVector;
import org.mcennis.graphrat.reusablecores.distance.xml.DistanceXML;
import org.mcennis.graphrat.reusablecores.distance.xml.DistanceXMLFactory;
import org.mcennis.graphrat.query.property.DataVectorQuery;
import org.dynamicfactory.propertyQuery.xml.NumericQueryXML;
import org.dynamicfactory.propertyQuery.xml.PropertyQueryXML;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;import java.lang.String;
import org.dynamicfactory.propertyQuery.Query.State;

/**
 *
 * @author Daniel McEnnis
 */
public class DataVectorQueryXML implements PropertyQueryXML{

    DataVectorQuery item = new DataVectorQuery();
    
    boolean not = false;
    
    DataVector right;
    
    DistanceXML comparison;
    
    NumericQueryXML numericQuery = new NumericQueryXML();
    
    enum InternalState {START,NOT,DATA_VECTOR,DISTANCE_FUNCTION,NUMERIC_QUERY};
    
    InternalState state = InternalState.START;
    
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if((localName.equalsIgnoreCase("DataVectorQuery"))||(qName.equalsIgnoreCase("DataVectorQuery"))){
            ;//
        }else if((localName.equalsIgnoreCase("DistanceFunction"))||(qName.equalsIgnoreCase("DistanceFunction"))){
            state = InternalState.DISTANCE_FUNCTION;
            String name = "";
            if((localName != null)&&(!localName.contentEquals(""))){
                name = localName;
            }else{
                name = qName;
            }
            comparison = DistanceXMLFactory.newInstance().create(name);
            comparison.startElement(uri, localName, qName, attributes);
        }else if((localName.equalsIgnoreCase("Not"))||(qName.equalsIgnoreCase("Not"))){
            not = true;
            state = InternalState.NOT;
        }else if((localName.equalsIgnoreCase("DataVector"))||(qName.equalsIgnoreCase("DataVector"))){
            state = InternalState.DATA_VECTOR;
        }else if((localName.equalsIgnoreCase("NumericQuery"))||(qName.equalsIgnoreCase("NumericQuery"))){
            state = InternalState.NUMERIC_QUERY;
            numericQuery.startElement(uri, localName, qName, attributes);
        }else if(state == InternalState.NUMERIC_QUERY){
            numericQuery.startElement(uri, localName, qName, attributes);
        }else{
            
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        if(state == InternalState.DATA_VECTOR){
            
        }else if(state == InternalState.DISTANCE_FUNCTION){
            comparison.characters(ch, start, length);
        }else if(state == InternalState.NUMERIC_QUERY){
            numericQuery.characters(ch, start, length);
        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        switch(state){
            case DISTANCE_FUNCTION:
                comparison.endElement(uri, localName, qName);
                if(comparison.buildingStatus()==State.READY){
                    state = InternalState.START;
                }
                break;
            case START:
                item.buildQuery(comparison.getDistanceMeasure(), not, numericQuery.getQuery(), right);
                break;
            case NOT:
                state = InternalState.START;
                break;
            case DATA_VECTOR:
                state = InternalState.START;
                break;
            case NUMERIC_QUERY:
                numericQuery.endElement(uri, localName, qName);
                if(numericQuery.buildingStatus() == State.READY){
                    state = InternalState.START;
                }
                break;
        }
    }

    public State buildingStatus() {
        return item.buildingStatus();
    }

    public DataVectorQuery getQuery() {
        return item;
    }

    public DataVectorQueryXML newCopy() {
        return new DataVectorQueryXML();
    }
}
