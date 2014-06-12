/**
 * ActorByModeXML
 * Created Jan 12, 2009 - 10:27:36 PM
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.graph.query.actor.xml;

import nz.ac.waikato.mcennis.rat.XMLParserObject.State;
import nz.ac.waikato.mcennis.rat.graph.query.ActorQuery;
import nz.ac.waikato.mcennis.rat.graph.query.ActorQueryXML;
import nz.ac.waikato.mcennis.rat.graph.query.actor.ActorByMode;
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
