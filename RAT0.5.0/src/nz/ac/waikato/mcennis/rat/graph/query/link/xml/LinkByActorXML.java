/**
 * LinkByActorXML
 * Created Jan 12, 2009 - 10:29:41 PM
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.graph.query.link.xml;

import java.util.logging.Level;
import java.util.logging.Logger;
import nz.ac.waikato.mcennis.rat.XMLParserObject.State;
import nz.ac.waikato.mcennis.rat.graph.query.ActorQueryXML;
import nz.ac.waikato.mcennis.rat.graph.query.LinkQuery;
import nz.ac.waikato.mcennis.rat.graph.query.LinkQuery.LinkEnd;
import nz.ac.waikato.mcennis.rat.graph.query.LinkQuery.SetOperation;
import nz.ac.waikato.mcennis.rat.graph.query.LinkQueryXML;
import nz.ac.waikato.mcennis.rat.graph.query.actor.xml.ActorQueryXMLFactory;
import nz.ac.waikato.mcennis.rat.graph.query.link.LinkByActor;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * 
 * 
 * 
 * @author Daniel McEnnis
 */
public class LinkByActorXML  implements LinkQueryXML{

    LinkByActor linkByActor = new LinkByActor();
    
    ActorQueryXML sourceActorQuery = null;
    ActorQueryXML destinationActorQuery = null;
    boolean not = false;
    SetOperation op = SetOperation.AND;
    
    enum InternalState {START,NOT,OPERATION,SOURCE,DESTINATION};
    
    InternalState internalState = InternalState.START;
    
    
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if(internalState == InternalState.SOURCE){
            sourceActorQuery.startElement(uri, localName, qName, attributes);
        }else if(internalState == InternalState.DESTINATION){
            destinationActorQuery.startElement(uri, localName, qName, attributes);
        }else if(localName.equalsIgnoreCase("Not")||qName.equalsIgnoreCase("Not")){
            internalState = InternalState.NOT;
            not = true;
        }else if(localName.equalsIgnoreCase("Operation")||qName.equalsIgnoreCase("Operation")){
            internalState = InternalState.OPERATION;
        }else if(localName.equalsIgnoreCase("SourceActorQuery")||qName.equalsIgnoreCase("SourceActorQuery")){
            internalState = InternalState.SOURCE;
            String name = "";
            if((localName != null)&&(!localName.contentEquals(""))){
                name = localName;
            }else{
                name = qName;
            }
            sourceActorQuery = ActorQueryXMLFactory.newInstance().create(name);
//            sourceActorQuery.startElement(uri, localName, qName, attributes);
        }else if(localName.equalsIgnoreCase("DestinationActorQuery")||qName.equalsIgnoreCase("DestinationActorQuery")){
            internalState = InternalState.DESTINATION;
            String name = "";
            if((localName != null)&&(!localName.contentEquals(""))){
                name = localName;
            }else{
                name = qName;
            }
            destinationActorQuery = ActorQueryXMLFactory.newInstance().create(name);
//            destinationActorQuery.startElement(uri, localName, qName, attributes);
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        if(internalState == InternalState.OPERATION){
            String operation = new String(ch,start,length);
            if(operation.equalsIgnoreCase("AND")){
                op = SetOperation.AND;
            }else if(operation.equalsIgnoreCase("OR")){
                op = SetOperation.OR;
            }else if(operation.equalsIgnoreCase("XOR")){
                op = SetOperation.XOR;
            }else{
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Unknown set operation '"+operation+"' - using default AND");
                op = SetOperation.AND;
            }
        }else if(internalState == InternalState.SOURCE){
            sourceActorQuery.characters(ch, start, length);
        }else if(internalState == InternalState.DESTINATION){
            destinationActorQuery.characters(ch, start, length);
        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        if(internalState == InternalState.NOT){
            internalState = InternalState.START;
        }else if(internalState == InternalState.OPERATION){
            internalState = InternalState.START;
        }else if(internalState == InternalState.SOURCE){
            sourceActorQuery.endElement(uri, localName, qName);
            if(sourceActorQuery.buildingStatus()==State.READY){
                internalState = InternalState.START;
            }
        }else if(internalState == InternalState.DESTINATION){
            destinationActorQuery.endElement(uri, localName, qName);
            if(destinationActorQuery.buildingStatus()==State.READY){
                internalState = InternalState.START;
            }
        }else if(internalState == InternalState.START){
            linkByActor.buildQuery(not, sourceActorQuery.getQuery(), destinationActorQuery.getQuery(), op);
        }

    }

    public State buildingStatus() {
        return linkByActor.buildingStatus();
    }

    public LinkQuery getQuery() {
        return linkByActor;
    }

    LinkEnd parseLinkEnd(String type){
       if(type.equalsIgnoreCase("ALL")){
           return LinkEnd.ALL;
       } else if(type.equalsIgnoreCase("SOURCE")){
           return LinkEnd.SOURCE;
       } else if(type.equalsIgnoreCase("DESTINATION")){
           return LinkEnd.DESTINATION;
       }else{
           Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Unknown LinkQuery.LinkEnd type '"+type+"'");
           return LinkEnd.ALL;
       }
    }
    public LinkByActorXML newCopy() {
        return new LinkByActorXML();
    }
}
