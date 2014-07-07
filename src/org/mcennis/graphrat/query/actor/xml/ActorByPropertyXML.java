/**
 * ActorByPropertyXML
 * Created Jan 12, 2009 - 10:27:53 PM
 * Copyright Daniel McEnnis, see license.txt
 */
package org.mcennis.graphrat.query.actor.xml;
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

import org.dynamicfactory.propertyQuery.xml.PropertyQueryXML;
import org.mcennis.graphrat.query.ActorQuery;
import org.mcennis.graphrat.query.ActorQueryXML;
import org.dynamicfactory.propertyQuery.xml.PropertyQueryXMLFactory;
import org.dynamicfactory.propertyQuery.Query.State;
import org.mcennis.graphrat.query.actor.ActorByProperty;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author Daniel McEnnis
 */
public class ActorByPropertyXML implements ActorQueryXML{

    ActorByProperty actorByProperty = new ActorByProperty();
    
    String propertyID = "";
    
    boolean not = false;
    
    PropertyQueryXML query = null;
    
    enum AbpState {START,PROPERTY_ID,NOT};
    
    AbpState internalState = AbpState.START;
    
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if(query != null){
            query.startElement(uri, localName, qName, attributes);
        }else if(localName.equalsIgnoreCase("ActorByProperty")||qName.equalsIgnoreCase("ActorByProperty")){
            ;
        }else if(localName.equalsIgnoreCase("PropertyID")||qName.equalsIgnoreCase("PropertyID")){
            internalState = AbpState.PROPERTY_ID;
        }else if(localName.equalsIgnoreCase("Not")||qName.equalsIgnoreCase("Not")){
            internalState = AbpState.NOT;
            not = true;
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
	}else if(internalState == AbpState.PROPERTY_ID){
            propertyID = new String(ch,start,length);
        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        if(query != null){
            query.endElement(uri,localName,qName);
	    if(query.buildingStatus() == State.READY){
		actorByProperty.buildQuery( propertyID, not, query.getQuery());
		query = null;
	    }
        }else if(internalState == AbpState.PROPERTY_ID){
            internalState = AbpState.START;
        }else if(internalState == AbpState.NOT){
            internalState = AbpState.START;
        }
    }

    public State buildingStatus() {
        return actorByProperty.buildingStatus();
    }

    public ActorQuery getQuery() {
        return actorByProperty;
    }

    public ActorByPropertyXML newCopy() {
        return new ActorByPropertyXML();
    }
}
