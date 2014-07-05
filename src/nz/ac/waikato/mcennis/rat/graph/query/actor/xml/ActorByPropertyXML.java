/**
 * ActorByPropertyXML
 * Created Jan 12, 2009 - 10:27:53 PM
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.graph.query.actor.xml;

import org.dynamicfactory.propertyQuery.PropertyQueryXML;
import nz.ac.waikato.mcennis.rat.graph.query.ActorQuery;
import nz.ac.waikato.mcennis.rat.graph.query.ActorQueryXML;
import org.dynamicfactory.propertyQuery.PropertyQueryXMLFactory;
import nz.ac.waikato.mcennis.rat.graph.query.actor.ActorByProperty;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author Daniel McEnnis
 */
public class ActorByPropertyXML implements ActorQueryXML{

    ActorByProperty actorByProperty = new ActorByProperty();
    
    String propertyID = "";
    
    boolean not = false;
    
    PropertyQueryXML query = null;
    
    enum AbpState {START,PROPERTY_ID,NOT};
    
    AbpState internalState = AbpState.START;
    
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if(query != null){
            query.startElement(uri, localName, qName, attributes);
        }else if(localName.equalsIgnoreCase("ActorByProperty")||qName.equalsIgnoreCase("ActorByProperty")){
            ;
        }else if(localName.equalsIgnoreCase("PropertyID")||qName.equalsIgnoreCase("PropertyID")){
            internalState = AbpState.PROPERTY_ID;
        }else if(localName.equalsIgnoreCase("Not")||qName.equalsIgnoreCase("Not")){
            internalState = AbpState.NOT;
            not = true;
        }else{
            String name = "";
            if((localName != null)&&(!localName.contentEquals(""))){
                name = localName;
            }else{
                name = qName;
            }
            query = PropertyQueryXMLFactory.newInstance().create(name);
            query.startElement(uri, localName, qName, attributes);
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        if(query!=null){
            query.characters(ch, start, length);
	}else if(internalState == AbpState.PROPERTY_ID){
            propertyID = new String(ch,start,length);
        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        if(query != null){
            query.endElement(uri,localName,qName);
	    if(query.buildingStatus() == State.READY){
		actorByProperty.buildQuery( propertyID, not, query.getQuery());
		query = null;
	    }
        }else if(internalState == AbpState.PROPERTY_ID){
            internalState = AbpState.START;
        }else if(internalState == AbpState.NOT){
            internalState = AbpState.START;
        }
    }

    public State buildingStatus() {
        return actorByProperty.buildingStatus();
    }

    public ActorQuery getQuery() {
        return actorByProperty;
    }

    public ActorByPropertyXML newCopy() {
        return new ActorByPropertyXML();
    }
}
