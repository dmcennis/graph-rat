/**
 * GraphByIDXML
 * Created Jan 12, 2009 - 10:28:40 PM
 * Copyright Daniel McEnnis, see license.txt
 */
package org.mcennis.graphrat.query.graph.xml;

import java.util.regex.Pattern;
import org.dynamicfactory.propertyQuery.Query.State;
import org.mcennis.graphrat.query.GraphQuery;
import org.mcennis.graphrat.query.GraphQueryXML;
import org.mcennis.graphrat.query.graph.GraphByID;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author Daniel McEnnis
 */
public class GraphByIDXML implements GraphQueryXML {

    GraphByID graphByID = new GraphByID();
    
    Pattern pattern = Pattern.compile(".*");
    
    enum InternalState {START,PATTERN};
    
    InternalState is = InternalState.START;
    
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if(localName.equalsIgnoreCase("Pattern")||qName.equalsIgnoreCase("Pattern")){
            is = InternalState.PATTERN;
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        if(is == InternalState.PATTERN){
            // TODO: Error checking
            pattern = Pattern.compile(new String(ch,start,length));
        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        if(is == InternalState.PATTERN){
            is = InternalState.START;
        }else{
            graphByID.buildQuery(pattern);
        }
    }

    public State buildingStatus() {
        return graphByID.buildingStatus();
    }

    public GraphQuery getQuery() {
        return graphByID;
    }

    public GraphByIDXML newCopy() {
        return new GraphByIDXML();
    }
}
