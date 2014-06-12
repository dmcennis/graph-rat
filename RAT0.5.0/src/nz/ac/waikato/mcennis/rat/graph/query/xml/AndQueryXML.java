/**
 * AndQueryXML
 * Created Jan 12, 2009 - 9:56:21 PM
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.graph.query.xml;

import java.util.LinkedList;
import nz.ac.waikato.mcennis.rat.graph.query.AndQuery;
import nz.ac.waikato.mcennis.rat.XMLParserObject.State;
import nz.ac.waikato.mcennis.rat.graph.query.ActorQueryXML;
import nz.ac.waikato.mcennis.rat.graph.query.GraphQueryXML;
import nz.ac.waikato.mcennis.rat.graph.query.LinkQueryXML;
import nz.ac.waikato.mcennis.rat.graph.query.actor.xml.ActorQueryXMLFactory;
import nz.ac.waikato.mcennis.rat.graph.query.link.xml.LinkQueryXMLFactory;
import nz.ac.waikato.mcennis.rat.graph.query.graph.xml.GraphQueryXMLFactory;
import nz.ac.waikato.mcennis.rat.graph.query.actor.AndActorQuery;
import nz.ac.waikato.mcennis.rat.graph.query.graph.AndGraphQuery;
import nz.ac.waikato.mcennis.rat.graph.query.link.AndLinkQuery;
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
