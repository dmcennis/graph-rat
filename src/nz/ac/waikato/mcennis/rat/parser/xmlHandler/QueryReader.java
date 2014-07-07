/*
 * QueryReader - created 12/02/2009 - 10:40:43 PM
 * Copyright Daniel McEnnis, see license.txt
 */

package nz.ac.waikato.mcennis.rat.parser.xmlHandler;

import org.dynamicfactory.AbstractFactory;
import org.dynamicfactory.propertyQuery.Query.State;
import nz.ac.waikato.mcennis.rat.graph.query.ActorQueryXML;
import nz.ac.waikato.mcennis.rat.graph.query.GraphQueryXML;
import nz.ac.waikato.mcennis.rat.graph.query.LinkQueryXML;
import org.dynamicfactory.propertyQuery.Query;
import nz.ac.waikato.mcennis.rat.graph.query.actor.xml.ActorQueryXMLFactory;
import nz.ac.waikato.mcennis.rat.graph.query.graph.xml.GraphQueryXMLFactory;
import nz.ac.waikato.mcennis.rat.graph.query.link.xml.LinkQueryXMLFactory;
import nz.ac.waikato.mcennis.rat.parser.ParsedObject;
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
