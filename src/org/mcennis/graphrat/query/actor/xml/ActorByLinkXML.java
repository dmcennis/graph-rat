/**
 * ActoByLinkXML
 * Created Jan 12, 2009 - 10:27:24 PM
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
package org.mcennis.graphrat.query.actor.xml;

import org.dynamicfactory.propertyQuery.Query.State;
import org.mcennis.graphrat.query.ActorQuery;
import org.mcennis.graphrat.query.ActorQueryXML;
import org.mcennis.graphrat.query.LinkQuery.LinkEnd;
import org.mcennis.graphrat.query.LinkQueryXML;
import org.mcennis.graphrat.query.link.xml.LinkQueryXMLFactory;
import org.mcennis.graphrat.query.actor.ActorByLink;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author Daniel McEnnis
 */
public class ActorByLinkXML implements ActorQueryXML{

    ActorByLink actorByLink = new ActorByLink();
    
    LinkQueryXML query = null;
    boolean not = false;
    LinkEnd end = LinkEnd.SOURCE;
    
    enum AblState {START,NOT,END};
    
    AblState internalState = AblState.START;

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if(query != null){
            query.startElement(uri, localName, qName, attributes);
        }else if(localName.equalsIgnoreCase("ActorByLink")||qName.equalsIgnoreCase("ActorByLink")){
            query = null;
        }else if((query != null)&&(query.buildingStatus()==State.LOADING)){
            query.startElement(uri, localName, qName, attributes);
        }else if(localName.equalsIgnoreCase("not")||qName.equalsIgnoreCase("not")){
            internalState = AblState.NOT;
            not = true;
        }else if(localName.equalsIgnoreCase("LinkEnd")||qName.equalsIgnoreCase("LinkEnd")){
            internalState = AblState.END;
            not = true;
        }else{
            String name = "";
            if((localName != null)&&(!localName.contentEquals(""))){
                name = localName;
            }else{
                name = qName;
            }
            query = LinkQueryXMLFactory.newInstance().create(name);
            query.startElement(uri, localName, qName, attributes);
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
	if(query != null){
            query.characters(ch, start, length);
        }else if(internalState == AblState.END){
            String string = new String(ch,start,length);
            if(string.equalsIgnoreCase("DESTINATION")){
                end = LinkEnd.DESTINATION;
            }else if(string.equalsIgnoreCase("ALL")){
                end = LinkEnd.ALL;
            }else{
                end = LinkEnd.SOURCE;
            }
        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        if(query != null){
            query.endElement(uri, localName, qName);
            if(query.buildingStatus() == State.READY){
		actorByLink.buildQuery(end,not,query.getQuery());
		query = null;
            }
        }else if(internalState == AblState.NOT){
            internalState = AblState.START;
        }else if(internalState == AblState.END){
            internalState = AblState.START;
        }
    }

    public State buildingStatus() {
        return actorByLink.buildingStatus();
    }

    public ActorQuery getQuery() {
        return actorByLink;
    }

    public ActorByLinkXML newCopy() {
        return new ActorByLinkXML();
    }
}
