/*
 * MemGraphReader.java
 *
 * Created on 11 June 2007, 11:47
 *
 * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt)
 */
package org.mcennis.graphrat.parser.xmlHandler;

import java.util.Collection;
import org.mcennis.graphrat.graph.Graph;
import org.mcennis.graphrat.actor.Actor;
import org.mcennis.graphrat.actor.ActorFactory;
import org.mcennis.graphrat.link.Link;
import org.mcennis.graphrat.link.LinkFactory;
import org.mcennis.graphrat.parser.ParsedObject;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.dynamicfactory.propertyQuery.Query;
import org.mcennis.graphrat.graph.MemGraph;
import org.dynamicfactory.property.Property;
import org.dynamicfactory.property.PropertyXMLFactory;
import org.dynamicfactory.property.xml.PropertyXML;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 *
 * SAX XML parser that reads serialized MemGraph graphs. The DTD of the file created
 * is as follows:
 * 
 * <pre> 
 * &lt;!DOCTYPE dataObject [
 *   &lt;!ELEMENT dataObject (graph)&gt;
 *   &lt;!ELEMENT graph (graphClass,graphName,graphProperties*,pathSet*,user*,userLink*,graph*)&gt;
 *   &lt;!ELEMENT graphClass (#PCDATA)&gt;
 *   &lt;!ELEMENT graphName (#PCDATA)&gt;
 *   &lt;!ELEMENT graphProperties (gClass,gValueClass,gType,gValue+)&gt;
 *   &lt;!ELEMENT gClass (#PCDATA)&gt;
 *   &lt;!ELEMENT gValueClass (#PCDATA)&gt;
 *   &lt;!ELEMENT gType (#PCDATA)&gt;
 *   &lt;!ELEMENT gValue (#PCDATA)&gt;
 *   &lt;!ELEMENT pathSet (path+)&gt;
 *   &lt;!ELEMENT path (actor+)&gt;
 *   &lt;!ELEMENT actor (actorType,actorID)&gt;
 *   &lt;!ELEMENT actorType (#PCDATA)&gt;
 *   &lt;!ELEMENT actorID (#PCDATA)&gt;
 *   &lt;!ELEMENT user (userClass,ID,properties*,page*)&gt;
 *   &lt;!ELEMENT ID (#PCDATA)&gt;
 *   &lt;!ELEMENT properties (propertiesClass,valueClass,type,value*)&gt;
 *   &lt;!ELEMENT type (#PCDATA)&gt;
 *   &lt;!ELEMENT value (#PCDATA)&gt;
 *   &lt;!ELEMENT valueClass (#PCDATA)&gt;
 *   &lt;!ELEMENT userLink (uClass,uStrength,uSourceType,uSourceID,uDestinationType,uDestinationID,uProperties)&gt;
 *   &lt;!ELEMENT uClass (#PCDATA)&gt;
 *   &lt;!ELEMENT uSourceType (#PCDATA)&gt;
 *   &lt;!ELEMENT uSourceID (#PCDATA)&gt;
 *   &lt;!ELEMENT uStrength (#PCDATA)&gt;
 *   &lt;!ELEMENT uDestinationType (#PCDATA)&gt;
 *   &lt;!ELEMENT uDestinationID (#PCDATA)&gt;
 *   &lt;!ELEMENT uProperties (uPropertiesClass,uValueClass,uPropertiesType,uPropertiesValue+)&gt;
 *   &lt;!ELEMENT uPropertiesClass (#PCDATA)&gt;
 *   &lt;!ELEMENT uPropertiesValueClass (#PCDATA)&gt;
 *   &lt;!ELEMENT uPropertiesType (#PCDATA)&gt;
 *   &lt;!ELEMENT uPropertiesValue (#PCDATA)&gt;
 *   &lt;!ELEMENT page (#PCDATA)&gt;
 * ]&gt;
 * </pre>
 * 
 * FIX: page should allow a type declaration
 *
 * @author Daniel McEnnis
 */
public class GraphReader extends Handler {

    Graph graph = null;

    enum State {

        START, DATAOBJECT, GRAPH, GRAPHNAME, 
        USER, USERCLASS, TYPE, ID, USERLINK,
        UCLASS, USTRENGTH, USOURCETYPE, USOURCEID, UDESTINATIONTYPE, UDESTINATIONID, 
        PATHSET, PATH, ACTOR, INNER_GRAPH
    };
    State state = State.START;
    Actor a;
    String artistType;
    Actor u;
    String userType;
    Property prop;
    String propType;
    Link ul;
    String linkType;
    Actor sourceActor;
    Actor destActor;
    double strength;
    Link al;
    Link sa;
    Locator locator;
    Stack<Graph> nestedGraphs;
    Graph masterGraph;
    String valuesClass = "";
    PropertyXML propertyXML = null;
    StringBuffer characters = new StringBuffer();
    int actorCount=0;
    int linkCount=0;

    /** Creates a new instance of MemGraphReader */
    public GraphReader() {
        super();
        properties.get("ParserClass").add("MemGraph");
        properties.get("Name").add("MemGraph");
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        try {
            characters.setLength(0);
//        System.out.println("Start: " + uri + " - " + localName + " - " + qName);

            if (propertyXML != null) {
                propertyXML.startElement(uri, localName, qName, attributes);
            } else if (localName.equalsIgnoreCase("DataObject") || qName.equalsIgnoreCase("DataObject")) {
                state = State.DATAOBJECT;
            } else if (localName.equalsIgnoreCase("Graph") || qName.equalsIgnoreCase("Graph")) {
                if (state == State.DATAOBJECT) {
                    state = State.GRAPH;
                } else {
                    state = State.INNER_GRAPH;
                    nestedGraphs.push(graph);
                    graph = new MemGraph();
                }

            } else if ((state == State.GRAPH)&&localName.equalsIgnoreCase("ID") || qName.equalsIgnoreCase("ID")) {

                state = State.GRAPHNAME;

            } else if (localName.equalsIgnoreCase("Property") || qName.equalsIgnoreCase("Property")) {
                propertyXML = PropertyXMLFactory.newInstance().create(attributes.getValue("Class"));
                propertyXML.startElement(uri, localName, qName, attributes);

            } else if (localName.equalsIgnoreCase("Actor") || qName.equalsIgnoreCase("Actor")) {

                state = State.USER;

            } else if ((state == state.USER)&&localName.equalsIgnoreCase("Class") || qName.equalsIgnoreCase("Class")) {

                state = State.USERCLASS;

            } else if (localName.equalsIgnoreCase("ID") || qName.equalsIgnoreCase("ID")) {

                state = State.ID;

            } else if (localName.equalsIgnoreCase("Mode") || qName.equalsIgnoreCase("Mode")) {

                state = State.TYPE;

            } else if (localName.equalsIgnoreCase("Link") || qName.equalsIgnoreCase("Link")) {

                state = State.USERLINK;

            } else if ((state==State.USERLINK)&&localName.equalsIgnoreCase("Class") || qName.equalsIgnoreCase("Class")) {

                state = State.UCLASS;

            } else if (localName.equalsIgnoreCase("Strength") || qName.equalsIgnoreCase("Strength")) {

                state = State.USTRENGTH;

            } else if (localName.equalsIgnoreCase("SourceMode") || qName.equalsIgnoreCase("SourceMode")) {

                state = State.USOURCETYPE;

            } else if (localName.equalsIgnoreCase("SourceID") || qName.equalsIgnoreCase("SourceID")) {

                state = State.USOURCEID;

            } else if (localName.equalsIgnoreCase("DestinationMode") || qName.equalsIgnoreCase("DestinationMode")) {

                state = State.UDESTINATIONTYPE;

            } else if (localName.equalsIgnoreCase("DestinationID") || qName.equalsIgnoreCase("DestinationID")) {

                state = State.UDESTINATIONID;

            } else if (localName.equalsIgnoreCase("pathset") || qName.equalsIgnoreCase("pathset")) {

                state = State.PATHSET;

            } else if (localName.equalsIgnoreCase("path") || qName.equalsIgnoreCase("path")) {

                state = State.PATH;

            } else if ((state==State.PATHSET)&&(localName.equalsIgnoreCase("actor") || qName.equalsIgnoreCase("actor"))) {

                state = State.ACTOR;

            } else {

                Logger.getLogger(GraphReader.class.getName()).log(Level.SEVERE, "Unknown Start Tag '" + qName + "'  at line " + locator.getLineNumber());

            }
        } catch (NullPointerException ex) {
            Logger.getLogger(GraphReader.class.getName()).log(Level.SEVERE, "Null Pointer at line " + locator.getLineNumber());
        }
    }

    @Override
    public void startDocument() throws SAXException {

        state = State.START;
        nestedGraphs = new Stack<Graph>();
        masterGraph = graph;
    }

    @Override
    public ParsedObject get() {

        return graph;

    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {

//		System.out.println("End: "+uri + " - " + localName + " - " + qName);
        try {
            if (propertyXML!=null) {
                propertyXML.endElement(uri, localName, qName);

                if(propertyXML.buildingStatus()==Query.State.READY){
                    switch(state){
                        case GRAPH:
                            graph.add(propertyXML.getProperty());
                            break;
                        case USER:
                            u.add(propertyXML.getProperty());
                            break;
                        case USERLINK:
                            ul.add(propertyXML.getProperty());
                            break;
                    }
                    propertyXML = null;
                }
            } else if (localName.equalsIgnoreCase("DataObject") || qName.equalsIgnoreCase("DataObject")) {

                state = State.START;

            } else if (localName.equalsIgnoreCase("Graph") || qName.equalsIgnoreCase("Graph")) {

                if (nestedGraphs.size() > 0) {
                    Graph tempGraph = nestedGraphs.pop();
                    tempGraph.addChild(graph);
                    graph = tempGraph;
                    state = State.INNER_GRAPH;
                } else {
                    state = State.START;
                }

            } else if (state==State.GRAPHNAME) {
                graph.setID(characters.toString());

                if (nestedGraphs.size() == 0) {
                    state = State.GRAPH;
                } else {
                    state = State.INNER_GRAPH;
                }

            } else if (state==State.USER) {

                if (nestedGraphs.size() == 0) {
                    state = State.GRAPH;
                } else {
                    state = State.INNER_GRAPH;
                }

                graph.add(u);

            } else if (state == State.USERCLASS) {
                userType = characters.toString();
                state = State.USER;

            } else if (localName.equalsIgnoreCase("id") || qName.equalsIgnoreCase("id")) {

                u = masterGraph.getActor(userType, characters.toString());
                if (u == null) {
                    u = ActorFactory.newInstance().create(userType,characters.toString());
                    actorCount++;
                }
                if(actorCount % 10000 == 0){
                    Logger.getLogger(GraphReader.class.getName()).log(Level.INFO,"Actor Count: "+actorCount);
                }
                state = State.USER;

            } else if (state == State.USERLINK) {
                graph.add(ul);
                if (nestedGraphs.size() == 0) {
                    state = State.GRAPH;
                } else {
                    state = State.INNER_GRAPH;
                }


            } else if (state == State.UCLASS){
                linkType = characters.toString();
                state = State.USERLINK;
            } else if (state == State.USTRENGTH){
                try {
                    strength = Double.parseDouble(characters.toString());
                } catch (NumberFormatException ex) {

                    Logger.getLogger(GraphReader.class.getName()).log(Level.SEVERE, "Bad Number - " + characters.toString() + " at line " + locator.getLineNumber());

                    ex.printStackTrace();

                    strength = 0.0;

                }
                state = State.USERLINK;

            } else if (state == State.USOURCETYPE) {

                userType = characters.toString();
                state = State.USERLINK;

            } else if (state == State.USOURCEID){
                sourceActor = masterGraph.getActor(userType, characters.toString());
                if (sourceActor == null) {
                    Logger.getLogger(GraphReader.class.getName()).log(Level.SEVERE, "Actor Type:'" + userType + "' ID:'" + characters.toString() + "' does not exist at line " + locator.getLineNumber());
                }
                state = State.USERLINK;

            } else if (state == State.UDESTINATIONTYPE){
                artistType = characters.toString();
                state = State.USERLINK;

            } else if (state==State.UDESTINATIONID){
                destActor = masterGraph.getActor(artistType, characters.toString());
                if (destActor == null) {
                    Logger.getLogger(GraphReader.class.getName()).log(Level.SEVERE, "Actor Type:'" + artistType + "' ID:'" + characters.toString() + "' does not exist at line " + locator.getLineNumber());
                }
                try {
                    if ((sourceActor != null) && (destActor != null)) {
                        Collection<Link> linkArray = masterGraph.getLink(linkType, sourceActor, destActor);

                        if (linkArray.size()==0) {
                            ul = LinkFactory.newInstance().create(linkType);
                            ul.set(sourceActor, strength, destActor);
                            linkCount++;
                        } else {
                            ul = linkArray.iterator().next();
                        }
                        if(linkCount % 100000 == 0){
                           Logger.getLogger(GraphReader.class.getName()).log(Level.INFO,"Link Count "+linkCount);
                        }
                    }
                } catch (NullPointerException e) {

                    Logger.getLogger(GraphReader.class.getName()).log(Level.SEVERE, "Null Pointer at line " + locator.getLineNumber());

                }
                state = State.USERLINK;

            } else {

                Logger.getLogger(GraphReader.class.getName()).log(Level.SEVERE, "Unknown End Tag '" + qName + "' at line " + locator.getLineNumber());

            }

        } catch (NullPointerException ex) {
            Logger.getLogger(GraphReader.class.getName()).log(Level.SEVERE, "Null Pointer at line " + locator.getLineNumber());
        }
    }

    @Override
    public void endDocument() throws SAXException {
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        characters.append(ch, start, length);

    }

    /**
     * Set the graph from which to load the data from the parsed object
     * @param type graph to be loaded by this parser
     */
    public void setGraph(Graph type) {

        graph = type;

    }

    @Override
    public Handler duplicate() {

        return new GraphReader();

    }

    @Override
    public void set(ParsedObject o) {

        if (o instanceof Graph) {

            graph = (Graph) o;

        }

    }

    @Override
    public void setDocumentLocator(Locator locator) {

        this.locator = locator;

    }
}

