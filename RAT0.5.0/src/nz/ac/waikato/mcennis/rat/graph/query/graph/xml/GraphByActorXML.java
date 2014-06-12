/**
 * GraphByActorXML
 * Created Jan 12, 2009 - 10:28:30 PM
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.graph.query.graph.xml;

import nz.ac.waikato.mcennis.rat.XMLParserObject.State;
import nz.ac.waikato.mcennis.rat.graph.query.ActorQueryXML;
import nz.ac.waikato.mcennis.rat.graph.query.GraphQuery;
import nz.ac.waikato.mcennis.rat.graph.query.GraphQueryXML;
import nz.ac.waikato.mcennis.rat.graph.query.actor.xml.ActorQueryXMLFactory;
import nz.ac.waikato.mcennis.rat.graph.query.graph.GraphByActor;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author Daniel McEnnis
 */
public class GraphByActorXML  implements GraphQueryXML{

    GraphByActor graphByActor = new GraphByActor();
    
    ActorQueryXML query = null;

    GraphQuery graph = null;

    GraphQueryXML graphQuery = null;
    
    
    boolean not = false;
    
    enum InternalState {START, POST_GRAPH_QUERY, NOT};
    
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
            query = ActorQueryXMLFactory.newInstance().create(name);
            query.startElement(uri, localName, qName, attributes);
        } 
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        if(query != null){
            query.characters(ch, start, length);
        }else if(graphQuery != null){
            graphQuery.characters(ch, start, length);
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
		graphByActor.buildQuery(not, graph, query.getQuery());
		query = null;
		internalState = InternalState.POST_GRAPH_QUERY;
	    }
	}else if(internalState == InternalState.NOT){
            internalState = InternalState.START;
        }
    }

    public State buildingStatus() {
        return graphByActor.buildingStatus();
    }

    public GraphQuery getQuery() {
        return graphByActor;
    }

    public GraphByActorXML newCopy() {
        return new GraphByActorXML();
    }
}
