/**
 * ActorByModeXML
 * Created Jan 12, 2009 - 10:27:36 PM
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
import org.mcennis.graphrat.query.actor.ActorByMode;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author Daniel McEnnis
 */
public class ActorByModeXML implements ActorQueryXML{

    ActorByMode actorByMode = new ActorByMode();
    
    String mode = "";
    String id="";
    boolean not = false;
    
    enum AbmState {START,MODE,ID,NOT,QUERY};
    
    AbmState internalState = AbmState.START;

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if(localName.equalsIgnoreCase("ActorByMode")||qName.equalsIgnoreCase("ActorByMode")){
            ;
        }else if(localName.equalsIgnoreCase("mode")||qName.equalsIgnoreCase("mode")){
            internalState = AbmState.MODE;
        }else if(localName.equalsIgnoreCase("id")||qName.equalsIgnoreCase("id")){
            internalState = AbmState.ID;
        }else if(localName.equalsIgnoreCase("not")||qName.equalsIgnoreCase("not")){
            internalState = AbmState.NOT;
            not = true;
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        if(internalState == AbmState.MODE){
            mode = new String(ch,start,length);
        }else if(internalState == AbmState.ID){
            id = new String(ch,start,length);
        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        if((internalState == AbmState.MODE)||(internalState == AbmState.ID)||(internalState == AbmState.NOT)){
            internalState = AbmState.START;
        }else if(internalState == AbmState.START){
            actorByMode.buildQuery(mode, id,not);
        }
    }

    public State buildingStatus() {
        return actorByMode.buildingStatus();
    }

    public ActorQuery getQuery() {
        return actorByMode;
    }

    public ActorByModeXML newCopy() {
        return new ActorByModeXML();
    }
}
