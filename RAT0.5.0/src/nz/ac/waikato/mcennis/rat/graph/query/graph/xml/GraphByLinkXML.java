/**
 * GraphByLinkXML
 * Created Jan 12, 2009 - 10:29:04 PM
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.graph.query.graph.xml;

import nz.ac.waikato.mcennis.rat.XMLParserObject.State;
import nz.ac.waikato.mcennis.rat.graph.query.GraphQuery;
import nz.ac.waikato.mcennis.rat.graph.query.GraphQueryXML;
import nz.ac.waikato.mcennis.rat.graph.query.LinkQuery.LinkEnd;
import nz.ac.waikato.mcennis.rat.graph.query.LinkQueryXML;
import nz.ac.waikato.mcennis.rat.graph.query.graph.GraphByLink;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author Daniel McEnnis
 */
public class GraphByLinkXML implements GraphQueryXML {

    GraphByLink graphByLink = new GraphByLink();
    
    boolean not = false;

    LinkEnd end = LinkEnd.SOURCE;

    GraphQueryXML graphQuery = null;
    
    LinkQueryXML query = null;

    GraphQuery graph = null;
    
    enum InternalState {START,END,NOT,POST_GRAPH_QUERY};
    
    InternalState internalState = InternalState.START;
    
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if(graphQuery != null){
            graphQuery.startElement(uri, localName, qName, attributes);
        }else if(query != null){
            query.startElement(uri, localName, qName, attributes);
        }else if(localName.equalsIgnoreCase("GraphByLink")||qName.equalsIgnoreCase("GraphByLink")){
            ;
        }else if(localName.equalsIgnoreCase("Not")||qName.equalsIgnoreCase("Not")){
            not = true;
            internalState = InternalState.NOT;
        }else if(localName.equalsIgnoreCase("LinkEnd")||qName.equalsIgnoreCase("LinkEnd")){
            internalState = InternalState.END;
        } else if(internalState == InternalState.START){
            String name = "";
            if((localName != null)&&(!localName.contentEquals(""))){
                name = localName;
            }else{
                name = qName;
            }
            graphQuery = GraphQueryXMLFactory.newInstance().create(name);
            graphQuery.startElement(uri, localName, qName, attributes);
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        if(query != null){
            query.characters(ch, start, length);
        }else if(graphQuery != null){
            graphQuery.characters(ch, start, length);
        }else if(internalState == InternalState.END){
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
            query.endElement(uri,localName,qName);
	    if(query.buildingStatus()==State.READY){
		graphByLink.buildQuery(end,not,graph,query.getQuery());
		query = null;
	    }
	}else if(graphQuery != null){
            graphQuery.endElement(uri,localName,qName);
	    if(graphQuery.buildingStatus()==State.READY){
		graph = graphQuery.getQuery();
		graphQuery = null;
		internalState = InternalState.POST_GRAPH_QUERY;
	    }
        }else if(internalState == InternalState.NOT){
            internalState = InternalState.START;
        }else if(internalState == InternalState.END){
            internalState = InternalState.START;
        }
    }

    public State buildingStatus() {
        return graphByLink.buildingStatus();
    }

    public GraphQuery getQuery() {
        return graphByLink;
    }

    public GraphByLinkXML newCopy() {
        return new GraphByLinkXML();
    }
}
