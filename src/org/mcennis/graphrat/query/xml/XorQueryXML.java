/**
 * XorQuery
 * Created Jan 12, 2009 - 10:21:53 PM
 * Copyright Daniel McEnnis, see license.txt
 */
package org.mcennis.graphrat.query.xml;

import java.util.Vector;
import org.dynamicfactory.propertyQuery.Query.State;
import org.mcennis.graphrat.query.ActorQueryXML;
import org.mcennis.graphrat.query.GraphQueryXML;
import org.mcennis.graphrat.query.LinkQueryXML;
import org.mcennis.graphrat.query.actor.xml.ActorQueryXMLFactory;
import org.mcennis.graphrat.query.link.xml.LinkQueryXMLFactory;
import org.mcennis.graphrat.query.graph.xml.GraphQueryXMLFactory;
import org.mcennis.graphrat.query.XorQuery;
import org.mcennis.graphrat.query.actor.XorActorQuery;
import org.mcennis.graphrat.query.graph.XorGraphQuery;
import org.mcennis.graphrat.query.link.XorLinkQuery;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author Daniel McEnnis
 */
public class XorQueryXML {

    XorQuery xorQuery= new XorLinkQuery();
    
    Object left = null;
    Object right = null;
    
    Object currentQuery = null;
    
    enum Kind {ACTOR,GRAPH,LINK};

    Kind kind = Kind.ACTOR;

    String type = "Graph";
    
    boolean leftDone = false;
    
    enum InternalState {START,LEFT,RIGHT};
    
    InternalState internalState = InternalState.START;
    
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if(internalState == InternalState.LEFT){
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
        }else if(internalState == InternalState.RIGHT){
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
        }else if(localName.equalsIgnoreCase("Xor")||qName.equalsIgnoreCase("Xor")){
            type = attributes.getValue("Class");
            if(type.equalsIgnoreCase("Actor")){
                kind = Kind.ACTOR;
                xorQuery = new XorActorQuery();
            }else if(type.equalsIgnoreCase("Graph")){
                kind=Kind.GRAPH;
                xorQuery = new XorGraphQuery();
            }else{
                kind =Kind.LINK;
                xorQuery = new XorLinkQuery();
            }
        }else if(currentQuery == null){
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
            if((((ActorQueryXML)currentQuery).buildingStatus()==State.READY)&&(internalState == InternalState.LEFT)){
                left = ((ActorQueryXML)currentQuery).getQuery();
                internalState = InternalState.START;
                currentQuery = null;
            }else if(((ActorQueryXML)currentQuery).buildingStatus()==State.READY){
                right = ((ActorQueryXML)currentQuery).getQuery();
                currentQuery= null;
                internalState = InternalState.START;
            }
                    break;
                case GRAPH:
            ((GraphQueryXML)currentQuery).endElement(uri, localName, qName);
            if((((GraphQueryXML)currentQuery).buildingStatus()==State.READY)&&(internalState == InternalState.LEFT)){
                left = ((GraphQueryXML)currentQuery).getQuery();
                internalState = InternalState.START;
                currentQuery = null;
            }else if(((GraphQueryXML)currentQuery).buildingStatus()==State.READY){
                right = ((GraphQueryXML)currentQuery).getQuery();
                currentQuery= null;
                internalState = InternalState.START;
            }
                     break;
                case LINK:
            ((LinkQueryXML)currentQuery).endElement(uri, localName, qName);
            if((((LinkQueryXML)currentQuery).buildingStatus()==State.READY)&&(internalState == InternalState.LEFT)){
                left = ((LinkQueryXML)currentQuery).getQuery();
                internalState = InternalState.START;
                currentQuery = null;
            }else if(((LinkQueryXML)currentQuery).buildingStatus()==State.READY){
                right = ((LinkQueryXML)currentQuery).getQuery();
                currentQuery= null;
                internalState = InternalState.START;
            }
                     break;
            }
        }else if((localName.equalsIgnoreCase("Xor"))||(qName.equalsIgnoreCase("Xor"))){
            Vector list = new Vector();
            list.add(left);
            list.add(right);
            switch(kind){
                case ACTOR:
            ((XorActorQuery)xorQuery).buildQuery(list);
                    break;
                case GRAPH:
            ((XorGraphQuery)xorQuery).buildQuery(list);
                     break;
                case LINK:
            ((XorLinkQuery)xorQuery).buildQuery(list);
                     break;
            }
        }
    }

    public State buildingStatus() {
        return xorQuery.buildingStatus();
    }

    public XorQuery getQuery() {
        return xorQuery;
    }

    public XorQueryXML newCopy() {
        return new XorQueryXML();
    }
}
