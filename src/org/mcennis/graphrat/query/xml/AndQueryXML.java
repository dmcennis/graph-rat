/**
 * AndQueryXML
 * Created Jan 12, 2009 - 9:56:21 PM
 * Copyright Daniel McEnnis, see license.txt
 */
package org.mcennis.graphrat.query.xml;

import java.util.LinkedList;
import org.mcennis.graphrat.query.AndQuery;
import org.dynamicfactory.propertyQuery.Query.State;
import org.mcennis.graphrat.query.ActorQueryXML;
import org.mcennis.graphrat.query.GraphQueryXML;
import org.mcennis.graphrat.query.LinkQueryXML;
import org.mcennis.graphrat.query.actor.xml.ActorQueryXMLFactory;
import org.mcennis.graphrat.query.link.xml.LinkQueryXMLFactory;
import org.mcennis.graphrat.query.graph.xml.GraphQueryXMLFactory;
import org.mcennis.graphrat.query.actor.AndActorQuery;
import org.mcennis.graphrat.query.graph.AndGraphQuery;
import org.mcennis.graphrat.query.link.AndLinkQuery;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author Daniel McEnnis
 */
public class AndQueryXML{

    AndQuery andQuery = null;
    
    LinkedList entries = new LinkedList();

    enum Kind {ACTOR,GRAPH,LINK};

    Kind kind = Kind.ACTOR;
    
    transient Object currentQuery = null;
    
    transient String type = "Graph";
    
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if(currentQuery != null){
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
        }else if(localName.equalsIgnoreCase("And")||qName.equalsIgnoreCase("And")){
            type = attributes.getValue("Class");
            if(type.equalsIgnoreCase("Actor")){
                kind = Kind.ACTOR;
                andQuery = new AndActorQuery();
            }else if(type.equalsIgnoreCase("Graph")){
                kind=Kind.GRAPH;
                andQuery = new AndGraphQuery();
            }else{
                kind =Kind.LINK;
                andQuery = new AndLinkQuery();
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
        }else if((localName.equalsIgnoreCase("And"))||(qName.equalsIgnoreCase("And"))){
            switch(kind){
                case ACTOR:
            ((AndActorQuery)andQuery).buildQuery(entries);
                    break;
                case GRAPH:
            ((AndGraphQuery)andQuery).buildQuery(entries);
                     break;
                case LINK:
            ((AndLinkQuery)andQuery).buildQuery(entries);
                     break;
            }
        }
    }

    public State buildingStatus() {
        return andQuery.buildingStatus();
    }

    public AndQuery getQuery(){
        return andQuery;
    }
    
    public AndQueryXML newCopy() {
        return new AndQueryXML();
    }
    
}
