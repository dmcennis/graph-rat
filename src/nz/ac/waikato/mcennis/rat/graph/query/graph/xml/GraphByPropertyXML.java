/**
 * GraphByPropertyXML
 * Created Jan 12, 2009 - 10:29:17 PM
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.graph.query.graph.xml;

import org.dynamicfactory.propertyQuery.xml.NullPropertyQueryXML;
import org.dynamicfactory.propertyQuery.xml.PropertyQueryXML;
import nz.ac.waikato.mcennis.rat.graph.query.GraphQuery;
import nz.ac.waikato.mcennis.rat.graph.query.GraphQueryXML;
import nz.ac.waikato.mcennis.rat.graph.query.graph.GraphByProperty;
import org.dynamicfactory.propertyQuery.xml.PropertyQueryXMLFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.dynamicfactory.propertyQuery.Query.State;

/**
 *
 * @author Daniel McEnnis
 */
public class GraphByPropertyXML implements GraphQueryXML {

    GraphByProperty graphByProperty = new GraphByProperty();
    
    String propertyID = "";
    
    boolean not = false;

    GraphQueryXML graphQuery = null;

    GraphQuery graph = null;
    
    PropertyQueryXML query = new NullPropertyQueryXML();
    
    enum InternalState {START,PROPERTY_ID,NOT,POST_GRAPH_QUERY};
    
    InternalState internalState = InternalState.START;
    
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if(graphQuery != null){
            graphQuery.startElement(uri, localName, qName, attributes);
        }else if(query != null){
            query.startElement(uri, localName, qName, attributes);
        }else if((localName.equalsIgnoreCase("GraphByActor"))||(qName.equalsIgnoreCase("GraphByActor"))){
            ;
        }else if((localName.equalsIgnoreCase("not"))||(qName.equalsIgnoreCase("not"))){
            internalState = InternalState.NOT;
            not= true;
        } else if((localName.equalsIgnoreCase("PropertyID"))||(qName.equalsIgnoreCase("PropertyID"))){
            internalState = InternalState.PROPERTY_ID;
        }else if((internalState == InternalState.START)){
            String name = "";
            if((localName != null)&&(!localName.contentEquals(""))){
                name = localName;
            }else{
                name = qName;
            }
            graphQuery = GraphQueryXMLFactory.newInstance().create(name);
            graphQuery.startElement(uri, localName, qName, attributes);
        }else if((internalState == InternalState.POST_GRAPH_QUERY)){
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
                if(query != null){
            query.characters(ch, start, length);
        }else if(graphQuery != null){
            graphQuery.characters(ch, start, length);
        }else if(internalState == InternalState.PROPERTY_ID){
            propertyID = new String(ch,start,length);
        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
                if(graphQuery != null){
	    graphQuery.endElement(uri,localName,qName);
	    if(graphQuery.buildingStatus()==State.READY){
		graph = graphQuery.getQuery();
		graphQuery = null;
		internalState = InternalState.POST_GRAPH_QUERY;
	    }
	}else if(query != null){
	    query.endElement(uri,localName,qName);
	    if(query.buildingStatus()==State.READY){
		graphByProperty.buildQuery(propertyID,not, graph, query.getQuery());
		query = null;
		internalState = InternalState.POST_GRAPH_QUERY;
	    }
	}else if(internalState == InternalState.NOT){
            internalState = InternalState.START;
        }else if(internalState == InternalState.PROPERTY_ID){
            internalState = InternalState.START;
        }
    }

    public State buildingStatus() {
        return graphByProperty.buildingStatus();
    }

    public GraphQuery getQuery() {
        return graphByProperty;
    }

    public GraphByPropertyXML newCopy() {
        return new GraphByPropertyXML();
    }
}
