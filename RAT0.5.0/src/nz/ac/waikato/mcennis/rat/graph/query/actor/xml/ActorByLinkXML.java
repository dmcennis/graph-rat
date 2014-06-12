/**
 * ActoByLinkXML
 * Created Jan 12, 2009 - 10:27:24 PM
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.graph.query.actor.xml;

import nz.ac.waikato.mcennis.rat.XMLParserObject.State;
import nz.ac.waikato.mcennis.rat.graph.query.ActorQuery;
import nz.ac.waikato.mcennis.rat.graph.query.ActorQueryXML;
import nz.ac.waikato.mcennis.rat.graph.query.LinkQuery.LinkEnd;
import nz.ac.waikato.mcennis.rat.graph.query.LinkQueryXML;
import nz.ac.waikato.mcennis.rat.graph.query.link.xml.LinkQueryXMLFactory;
import nz.ac.waikato.mcennis.rat.graph.query.actor.ActorByLink;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author Daniel McEnnis
 */
public class ActorByLinkXML implements ActorQueryXML{

    ActorByLink actorByLink = new ActorByLink();
    
    LinkQueryXML query = null;
    boolean not = false;
    LinkEnd end = LinkEnd.SOURCE;
    
    enum AblState {START,NOT,END};
    
    AblState internalState = AblState.START;

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if(query != null){
            query.startElement(uri, localName, qName, attributes);
        }else if(localName.equalsIgnoreCase("ActorByLink")||qName.equalsIgnoreCase("ActorByLink")){
            query = null;
        }else if((query != null)&&(query.buildingStatus()==State.LOADING)){
            query.startElement(uri, localName, qName, attributes);
        }else if(localName.equalsIgnoreCase("not")||qName.equalsIgnoreCase("not")){
            internalState = AblState.NOT;
            not = true;
        }else if(localName.equalsIgnoreCase("LinkEnd")||qName.equalsIgnoreCase("LinkEnd")){
            internalState = AblState.END;
            not = true;
        }else{
            String name = "";
            if((localName != null)&&(!localName.contentEquals(""))){
                name = localName;
            }else{
                name = qName;
            }
            query = LinkQueryXMLFactory.newInstance().create(name);
            query.startElement(uri, localName, qName, attributes);
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
	if(query != null){
            query.characters(ch, start, length);
        }else if(internalState == AblState.END){
            String string = new String(ch,start,length);
            if(string.equalsIgnoreCase("DESTINATION")){
                end = LinkEnd.DESTINATION;
            }else if(string.equalsIgnoreCase("ALL")){
                end = LinkEnd.ALL;
            }else{
                end = LinkEnd.SOURCE;
            }
        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        if(query != null){
            query.endElement(uri, localName, qName);
            if(query.buildingStatus() == State.READY){
		actorByLink.buildQuery(end,not,query.getQuery());
		query = null;
            }
        }else if(internalState == AblState.NOT){
            internalState = AblState.START;
        }else if(internalState == AblState.END){
            internalState = AblState.START;
        }
    }

    public State buildingStatus() {
        return actorByLink.buildingStatus();
    }

    public ActorQuery getQuery() {
        return actorByLink;
    }

    public ActorByLinkXML newCopy() {
        return new ActorByLinkXML();
    }
}
