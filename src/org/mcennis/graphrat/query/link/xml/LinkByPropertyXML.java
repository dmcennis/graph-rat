/**
 * LinkByPropertyXML
 * Created Jan 12, 2009 - 10:29:53 PM
 * Copyright Daniel McEnnis, see license.txt
 */
package org.mcennis.graphrat.query.link.xml;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.dynamicfactory.propertyQuery.xml.PropertyQueryXML;
import org.mcennis.graphrat.query.LinkQuery;
import org.mcennis.graphrat.query.LinkQuery.LinkEnd;
import org.mcennis.graphrat.query.LinkQueryXML;
import org.dynamicfactory.propertyQuery.xml.PropertyQueryXMLFactory;
import org.mcennis.graphrat.query.link.LinkByProperty;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.dynamicfactory.propertyQuery.Query.State;

/**
 *
 * @author Daniel McEnnis
 */
public class LinkByPropertyXML implements LinkQueryXML {

    LinkByProperty linkByProperty = new LinkByProperty();
    
    PropertyQueryXML query = null;
    LinkEnd actorRestriction = LinkEnd.SOURCE;
    String propertyID = "";
    boolean not = false;
    
    enum InternalState {START,NOT,PROPERTY_ID};
    
    InternalState internalState = InternalState.START;
    
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if(query != null){
            query.startElement(uri, localName, qName, attributes);
        }else if((localName.equalsIgnoreCase("Not"))||(qName.equalsIgnoreCase("Not"))){
            internalState = InternalState.NOT;
            not = true;
        }else if((localName.equalsIgnoreCase("PropertyID"))||(qName.equalsIgnoreCase("ProeprtyID"))){
            internalState = InternalState.PROPERTY_ID;
            not = true;
        }else if(localName.equalsIgnoreCase("LinkByProperty")||qName.equalsIgnoreCase("LinkByProperty")){
            internalState = InternalState.START;
        }else{
            String name = "";
            if((localName != null)&&(!localName.contentEquals(""))){
                name = localName;
            }else{
                name = qName;
            }
            query = PropertyQueryXMLFactory.newInstance().create(name);
            query.startElement(uri, localName, qName, attributes);
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        if(query!=null){
            query.characters(ch, start, length);
        }else if(internalState == InternalState.PROPERTY_ID){
            propertyID = new String(ch,start,length);
            }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        if(query != null){
            query.endElement(uri, localName, qName);
            if(query.buildingStatus() == State.READY){
                internalState=  InternalState.START;
                linkByProperty.buildQuery(propertyID,not,query.getQuery());
                query = null;
            }
        }else if(internalState  == InternalState.NOT){
            internalState=  InternalState.START;
        }else if(internalState  == InternalState.PROPERTY_ID){
            internalState=  InternalState.START;
        }
    }

    public State buildingStatus() {
        return linkByProperty.buildingStatus();
    }

    public LinkQuery getQuery() {
        return linkByProperty;
    }


    public LinkByPropertyXML newCopy() {
        return new LinkByPropertyXML();
    }

    LinkEnd parseLinkEnd(String type){
       if(type.equalsIgnoreCase("ALL")){
           return LinkEnd.ALL;
       } else if(type.equalsIgnoreCase("SOURCE")){
           return LinkEnd.SOURCE;
       } else if(type.equalsIgnoreCase("DESTINATION")){
           return LinkEnd.DESTINATION;
       }else{
           Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Unknown LinkQuery.LinkEnd type '"+type+"'");
           return LinkEnd.ALL;
       }
    }
 }
