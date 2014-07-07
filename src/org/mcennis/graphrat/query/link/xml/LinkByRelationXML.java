/**
 * LinkByRelationXML
 * Created Jan 12, 2009 - 10:30:05 PM
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
package org.mcennis.graphrat.query.link.xml;

import org.dynamicfactory.propertyQuery.Query.State;
import org.mcennis.graphrat.query.LinkQuery;
import org.mcennis.graphrat.query.LinkQueryXML;
import org.mcennis.graphrat.query.link.LinkByRelation;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author Daniel McEnnis
 */
public class LinkByRelationXML implements LinkQueryXML {

    LinkByRelation linkByRelation = new LinkByRelation();
    
    String relation = "";
    boolean not = false;
    
    enum InternalState {START,NOT,RELATION};
    
    InternalState internalState = InternalState.START;
    
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if(localName.equalsIgnoreCase("Not")||qName.equalsIgnoreCase("Not")){
            internalState = InternalState.NOT;
            not = true;
        }else if(localName.equalsIgnoreCase("Relation")||qName.equalsIgnoreCase("Relation")){
            internalState = InternalState.RELATION;
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        if(internalState == InternalState.RELATION){
            relation = new String(ch,start,length);
        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        if(internalState == InternalState.NOT){
            internalState = InternalState.START;
        }else if(internalState == InternalState.RELATION){
            internalState = InternalState.START;
        }else if(internalState == InternalState.START){
            linkByRelation.buildQuery(relation, not);
        }
    }

    public State buildingStatus() {
        return linkByRelation.buildingStatus();
    }

    public LinkQuery getQuery() {
        return linkByRelation;
    }

    public LinkByRelationXML newCopy() {
        return new LinkByRelationXML();
    }

 }
