/**
 * NullQueryXML
 * Created Jan 12, 2009 - 9:26:21 PM
 * Copyright Daniel McEnnis, see license.txt
 */
package org.dynamicfactory.propertyQuery;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author Daniel McEnnis
 */
public class NullPropertyQueryXML implements PropertyQueryXML{

    NullPropertyQuery nullQuery = new NullPropertyQuery();
    
    enum InternalState {START,NOT};
    
    InternalState state = InternalState.START;
    
    boolean not = false;
    
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if(localName.equalsIgnoreCase("StringQuery")&&qName.equalsIgnoreCase("NumericQuery")){
            
        }else if(localName.equalsIgnoreCase("Not")&&qName.equalsIgnoreCase("Not")){
            state = InternalState.NOT;
            not = true;
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        ;
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        switch (state){
            case START:
                nullQuery.buildQuery(not);
                break;
            case NOT:
                state = InternalState.START;
                break;
        }
    }

    public State buildingStatus() {
        return nullQuery.buildingStatus();
    }

    public NullPropertyQuery getQuery() {
        return nullQuery;
    }

    public NullPropertyQueryXML newCopy() {
        return new NullPropertyQueryXML();
    }
}
