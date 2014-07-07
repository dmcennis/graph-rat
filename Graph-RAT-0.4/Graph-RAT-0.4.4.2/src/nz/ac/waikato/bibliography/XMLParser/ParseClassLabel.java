/*
 * ParseBibliographyXML.java
 *
 * Created on 8/01/2008, 14:02:23
 * Coptright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.bibliography.XMLParser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mcennis.graphrat.graph.Graph;
import org.mcennis.graphrat.actor.Actor;
import org.mcennis.graphrat.parser.ParsedObject;
import org.mcennis.graphrat.parser.xmlHandler.Handler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Parser for reading ground truth XML files associated with bibliographic XML files
 * by paperID attribute.<br/>
 * <br/>
 * DTD is as follows:<br/>
 * <pre>
 * &lt;!DOCTYPE corpus [
 * &lt;!ELEMENT corpus (paper)&gt;
 * &lt;!ELEMENT paper (paperID,keyword)&gt;
 * &lt;!ELEMENT paperID (#PCDATA)&gt;
 * &lt;!ELEMENT keyword (#PCDATA)&gt;
 * ]&gt;
 * </pre>
 * @author Daniel McEnnis
 */
public class ParseClassLabel extends Handler {

    Graph graph = null;

    enum State {

        START, CORPUS, PAPER, PAPER_ID, KEYWORD
    }

    
    
       ;
    State state = State.START  ;
     
    
    Actor paper = null;
    HashMap<String, HashSet<Actor>> subgraph = new HashMap<String, HashSet<Actor>>();

    public ParseClassLabel() {
    }

    public ParsedObject get() {
        return graph;
    }

    public void set(ParsedObject o) {
        graph = (Graph) o;
    }

    public Handler duplicate() {
        ParseClassLabel ret = new ParseClassLabel();
        ret.graph = graph;
        return ret;
    }

    @Override
    public void startDocument() throws SAXException {
        state = State.START;
    }

    @Override
    public void endDocument() throws SAXException {
        java.util.Properties props = new Properties();
        props.setProperty("GraphClass", "MemGraph");
        Iterator<String> it = subgraph.keySet().iterator();
        while (it.hasNext()) {
            try {
                String cluster = it.next();
                props.setProperty("Graph", "MemGraph");
                props.setProperty("GraphID", cluster);
                Graph child = graph.getSubGraph(props, subgraph.get(cluster));
                graph.addChild(child);
            } catch (Exception ex) {
                Logger.getLogger(ParseClassLabel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if ((localName.equalsIgnoreCase("corpus")) || (qName.equalsIgnoreCase("corpus"))) {
            state = State.CORPUS;
        } else if ((localName.equalsIgnoreCase("paper")) || (qName.equalsIgnoreCase("paper"))) {
            state = State.PAPER;
        } else if ((localName.equalsIgnoreCase("paperID")) || (qName.equalsIgnoreCase("paperID"))) {
            state = State.PAPER_ID;
        } else if ((localName.equalsIgnoreCase("keyword")) || (qName.equalsIgnoreCase("keyword"))) {
            state = State.KEYWORD;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if ((localName.equalsIgnoreCase("corpus")) || (qName.equalsIgnoreCase("corpus"))) {
            state = State.START;
        } else if ((localName.equalsIgnoreCase("paper")) || (qName.equalsIgnoreCase("paper"))) {
            state = State.CORPUS;
        } else if ((localName.equalsIgnoreCase("paperID")) || (qName.equalsIgnoreCase("paperID"))) {
            state = State.PAPER;
        } else if ((localName.equalsIgnoreCase("keyword")) || (qName.equalsIgnoreCase("keyword"))) {
            state = State.PAPER;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String str = new String(ch, start, length);
        if (state == State.PAPER_ID) {
            paper = graph.getActor("Paper", str);
            if (paper == null) {
                Logger.getLogger(ParseClassLabel.class.getName()).log(Level.SEVERE,"Paper '" + str + "' not found");
            }
        } else if (state == State.KEYWORD) {
            if (paper != null) {
                java.util.Properties props = new java.util.Properties();
                props.setProperty("PropertyClass", "java.lang.String");
                props.setProperty("PropertyID", "cluster");
                props.setProperty("PropertyType", "Basic");
                Property property = PropertyFactory.newInstance().create(props);
                try {
                    property.add(str);
                } catch (InvalidObjectTypeException ex) {
                    Logger.getLogger("global").log(Level.SEVERE, "Property class doesn't match java.lang.String", ex);
                }
                paper.add(property);

                if (!subgraph.containsKey(str)) {
                    subgraph.put(str, new HashSet<Actor>());
                }
                subgraph.get(str).add(paper);
            } else {
            //
            }
        }
    }
}
