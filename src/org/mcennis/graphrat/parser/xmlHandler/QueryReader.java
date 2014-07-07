/*
 * QueryReader - created 12/02/2009 - 10:40:43 PM
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
package org.mcennis.graphrat.parser.xmlHandler;

import org.dynamicfactory.AbstractFactory;
import org.dynamicfactory.propertyQuery.Query.State;
import org.mcennis.graphrat.query.ActorQueryXML;
import org.mcennis.graphrat.query.GraphQueryXML;
import org.mcennis.graphrat.query.LinkQueryXML;
import org.dynamicfactory.propertyQuery.Query;
import org.mcennis.graphrat.query.actor.xml.ActorQueryXMLFactory;
import org.mcennis.graphrat.query.graph.xml.GraphQueryXMLFactory;
import org.mcennis.graphrat.query.link.xml.LinkQueryXMLFactory;
import org.mcennis.graphrat.parser.ParsedObject;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author Daniel McEnnis
 */
public class QueryReader extends Handler{

    Query result = null;

    GraphQueryXML graphXML = null;

    LinkQueryXML linkXML = null;

    ActorQueryXML actorXML = null;
    
    boolean graph=false;
    
    boolean actor = false;
    
    boolean link = false;

    AbstractFactory type;

    
    @Override
    public ParsedObject get() {
        ParsedObject ret = new ParsedObject() {
            public Query q = result;
        };
        return ret;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if(graphXML != null){
            graphXML.characters(ch,start,length);
        }else if(actorXML != null){
            actorXML.characters(ch, start, length);
        }else if(graphXML != null){
            linkXML.characters(ch, start, length);
        }  
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if(graphXML != null){
            graphXML.endElement(uri, localName, qName);
            if(graphXML.buildingStatus() == State.READY){
                result = graphXML.getQuery();
                graphXML = null;
            }
        }else if(actorXML != null){
            actorXML.endElement(uri, localName, qName);
            if(actorXML.buildingStatus() == State.READY){
                result = actorXML.getQuery();
                actorXML = null;
            }
        }else if(linkXML != null){
            linkXML.endElement(uri, localName, qName);
            if(linkXML.buildingStatus() == State.READY){
                result = linkXML.getQuery();
                linkXML = null;
            }
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if(graphXML != null){
            graphXML.startElement(uri, localName, qName, attributes);
        }else if(actorXML != null){
            actorXML.startElement(uri, localName, qName, attributes);
        }else if(graphXML != null){
            linkXML.startElement(uri, localName, qName, attributes);
        }else if(localName.equalsIgnoreCase("Graph")||qName.equalsIgnoreCase("Graph")){
            type = GraphQueryXMLFactory.newInstance();
            graph = true;

        }else if(localName.equalsIgnoreCase("Actor")||qName.equalsIgnoreCase("Actor")){
            type = ActorQueryXMLFactory.newInstance();
            actor = true;
        }else if(localName.equalsIgnoreCase("Link")||qName.equalsIgnoreCase("Link")){
            type = LinkQueryXMLFactory.newInstance();
            link = true;
        }else{
            String name = localName;
            if((localName==null)||(localName.equalsIgnoreCase(""))){
                name = qName;
            }
            if(graph){
                graphXML = ((GraphQueryXMLFactory)type).create(name);
            }else if(actor){
                actorXML = ((ActorQueryXMLFactory)type).create(name);
            }else{
                linkXML = ((LinkQueryXMLFactory)type).create(name);
            }
        }
    }

    @Override
    public void set(ParsedObject o) {
        result = (Query)o;
    }

    @Override
    public Handler duplicate() {
        QueryReader ret = new QueryReader();
        ret.result = result;
        return ret;
    }

}
