/**
 * OrQuery
 * Created Jan 12, 2009 - 10:21:40 PM
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
package org.mcennis.graphrat.query.xml;

import java.util.LinkedList;
import org.mcennis.graphrat.query.OrQuery;
import org.dynamicfactory.propertyQuery.Query.State;
import org.mcennis.graphrat.query.ActorQueryXML;
import org.mcennis.graphrat.query.GraphQueryXML;
import org.mcennis.graphrat.query.LinkQueryXML;
import org.mcennis.graphrat.query.actor.OrActorQuery;
import org.mcennis.graphrat.query.actor.xml.ActorQueryXMLFactory;
import org.mcennis.graphrat.query.graph.OrGraphQuery;
import org.mcennis.graphrat.query.link.xml.LinkQueryXMLFactory;
import org.mcennis.graphrat.query.graph.xml.GraphQueryXMLFactory;
import org.mcennis.graphrat.query.link.OrLinkQuery;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author Daniel McEnnis
 */
public class OrQueryXML {

    OrQuery orQuery = new OrLinkQuery();
    
    LinkedList entries = new LinkedList();
    
    Object currentQuery = null;

    enum Kind {ACTOR,GRAPH,LINK};

    Kind kind = Kind.ACTOR;

    String type = null;
    
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if(currentQuery == null){
            switch(kind){
                case ACTOR:
            ((ActorQueryXML)currentQuery).startElement(uri, localName, qName, attributes);
                    break;
                case GRAPH:
           ((GraphQueryXML)currentQuery).startElement(uri, localName, qName, attributes);
                     break;
                case LINK:
           ((LinkQueryXML)currentQuery).startElement(uri, localName, qName, attributes);
                     break;
            }
        }else if(localName.equalsIgnoreCase("Or")||qName.equalsIgnoreCase("Or")){
            type = attributes.getValue("Class");
            if(type.equalsIgnoreCase("Actor")){
                kind = Kind.ACTOR;
                orQuery = new OrActorQuery();
            }else if(type.equalsIgnoreCase("Graph")){
                kind=Kind.GRAPH;
                orQuery = new OrGraphQuery();
            }else{
                kind =Kind.LINK;
                orQuery = new OrLinkQuery();
            }
        }else{
            String name = "";
            if((localName != null)&&(!localName.contentEquals(""))){
                name = localName;
            }else{
                name = qName;
            }
            switch(kind){
                case ACTOR:
            currentQuery = ActorQueryXMLFactory.newInstance().create(name);
            ((ActorQueryXML)currentQuery).startElement(uri, localName, qName, attributes);
                    break;
                case GRAPH:
            currentQuery = GraphQueryXMLFactory.newInstance().create(name);
            ((GraphQueryXML)currentQuery).startElement(uri, localName, qName, attributes);
                     break;
                case LINK:
            currentQuery = LinkQueryXMLFactory.newInstance().create(name);
            ((LinkQueryXML)currentQuery).startElement(uri, localName, qName, attributes);
                     break;
            }
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        if(currentQuery != null){
            switch(kind){
                case ACTOR:
            ((ActorQueryXML)currentQuery).characters(ch, start, length);
                    break;
                case GRAPH:
            ((GraphQueryXML)currentQuery).characters(ch, start, length);
                     break;
                case LINK:
            ((LinkQueryXML)currentQuery).characters(ch, start, length);
                     break;
            }
        }             
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        if(currentQuery != null){
            switch(kind){
                case ACTOR:
            ((ActorQueryXML)currentQuery).endElement(uri, localName, qName);
            if(((ActorQueryXML)currentQuery).buildingStatus()==State.READY){
                entries.add(((ActorQueryXML)currentQuery).getQuery());
                currentQuery = null;
            }
                    break;
                case GRAPH:
            ((GraphQueryXML)currentQuery).endElement(uri, localName, qName);
            if(((GraphQueryXML)currentQuery).buildingStatus()==State.READY){
                entries.add(((GraphQueryXML)currentQuery).getQuery());
                currentQuery = null;
            }
                     break;
                case LINK:
            ((LinkQueryXML)currentQuery).endElement(uri, localName, qName);
            if(((LinkQueryXML)currentQuery).buildingStatus()==State.READY){
                entries.add(((LinkQueryXML)currentQuery).getQuery());
                currentQuery = null;
            }
                     break;
            }
        }else if((localName.equalsIgnoreCase("Or"))||(qName.equalsIgnoreCase("Or"))){
            switch(kind){
                case ACTOR:
            ((OrActorQuery)orQuery).buildQuery(entries);
                    break;
                case GRAPH:
            ((OrGraphQuery)orQuery).buildQuery(entries);
                     break;
                case LINK:
            ((OrLinkQuery)orQuery).buildQuery(entries);
                     break;
            }
        }
    }

    public State buildingStatus() {
        return orQuery.buildingStatus();
    }

    public OrQuery getQuery() {
        return orQuery;
    }

    public OrQueryXML newCopy() {
        return new OrQueryXML();
    }
    
}
