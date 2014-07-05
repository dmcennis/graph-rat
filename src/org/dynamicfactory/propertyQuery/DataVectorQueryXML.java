/**
 * DataVectorQueryXML
 * Created Jan 12, 2009 - 8:13:15 PM
 * Copyright Daniel McEnnis, see license.txt
 */
package org.dynamicfactory.propertyQuery;

import nz.ac.waikato.mcennis.rat.reusablecores.datavector.DataVector;
import nz.ac.waikato.mcennis.rat.reusablecores.distance.xml.DistanceXML;
import nz.ac.waikato.mcennis.rat.reusablecores.distance.xml.DistanceXMLFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

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
