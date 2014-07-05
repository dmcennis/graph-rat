/**
 * NumericQueryXML
 * Created Jan 12, 2009 - 8:52:45 PM
 * Copyright Daniel McEnnis, see license.txt
 */
package org.dynamicfactory.propertyQuery;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author Daniel McEnnis
 */
public class NumericQueryXML implements PropertyQueryXML{

    NumericQuery numericQuery = new NumericQuery();
    
    enum InternalState {START, OPERATION, NOT, VALUE};
    
    InternalState state = InternalState.START;
    
    boolean not;
    
    double value = 0.0;
    
    NumericQuery.Operation op = NumericQuery.Operation.GT;
    
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if(localName.equalsIgnoreCase("NumericQuery")&&qName.equalsIgnoreCase("NumericQuery")){
            
        }else if(localName.equalsIgnoreCase("Operation")&&qName.equalsIgnoreCase("Operation")){
            state = InternalState.OPERATION;
        }else if(localName.equalsIgnoreCase("Not")&&qName.equalsIgnoreCase("Not")){
            state = InternalState.NOT;
        }else if(localName.equalsIgnoreCase("Value")&&qName.equalsIgnoreCase("Value")){
            state = InternalState.VALUE;
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
            String contents = new String(ch,start,length);
        if(state == InternalState.OPERATION){
            if(contents.equalsIgnoreCase("EQ")){
                op = NumericQuery.Operation.EQ;
            }else if(contents.equalsIgnoreCase("GT")){
                op = NumericQuery.Operation.GT;
            }else if(contents.equalsIgnoreCase("LT")){
                op = NumericQuery.Operation.LT;
            }else if(contents.equalsIgnoreCase("GTE")){
                op = NumericQuery.Operation.GTE;
            }else if(contents.equalsIgnoreCase("LTE")){
                op = NumericQuery.Operation.LTE;
            }else if(contents.equalsIgnoreCase("NE")){
                op = NumericQuery.Operation.NE;
            }else{
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE,"Unknown operation '"+contents+"' - assuming GT");
            }
        }else if(state == InternalState.VALUE){
            try{
            value = Double.parseDouble(contents);
            }catch(NumberFormatException ex){
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE,null,ex);
            }
        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        switch (state){
            case START:
                numericQuery.buildQuery(value, not, op);
                break;
            case NOT:
                state = InternalState.START;
                break;
            case OPERATION:
                state = InternalState.START;
                break;
            case VALUE:
                state = InternalState.START;
                break;                
        }
    }

    public State buildingStatus() {
        return numericQuery.buildingStatus();
    }

    public NumericQuery getQuery() {
        return numericQuery;
    }

    public NumericQueryXML newCopy() {
        return new NumericQueryXML();
    }
}
