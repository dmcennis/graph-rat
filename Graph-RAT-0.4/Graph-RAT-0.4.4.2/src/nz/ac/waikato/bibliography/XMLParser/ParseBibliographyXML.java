/*
 * ParseBibliographyXML.java
 *
 * Created on 8/01/2008, 14:02:23
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.bibliography.XMLParser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import nz.ac.waikato.mcennis.rat.graph.actor.Actor;
import nz.ac.waikato.mcennis.rat.graph.actor.ActorFactory;
import nz.ac.waikato.mcennis.rat.graph.link.Link;
import nz.ac.waikato.mcennis.rat.graph.link.LinkFactory;
import nz.ac.waikato.mcennis.rat.graph.link.LinkFactory;
import nz.ac.waikato.mcennis.rat.graph.property.InvalidObjectTypeException;
import nz.ac.waikato.mcennis.rat.graph.property.Property;
import nz.ac.waikato.mcennis.rat.graph.property.PropertyFactory;
import nz.ac.waikato.mcennis.rat.parser.ParsedObject;
import nz.ac.waikato.mcennis.rat.parser.xmlHandler.Handler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * XML SAX parser for parsing a custom bibliography XML format with most of the 
 * information inside a bibtex file containing journal entries.
 * <br/>
 * The DTD of the XML file is as follows:<br/>
 * <pre>
 * &lt;!DOCTYPE bibliography [
 *       &lt;!ELEMENT bibliography (authorRef+,paper+)&gt;
 *      &lt;!ELEMENT authorRef (authorName,authorID)&gt;
 *       &lt;!ELEMENT authorName (#PCDATA)&gt;
 *       &lt;!ELEMENT authorID (#PCDATA)&gt;
 *       &lt;!ELEMENT paper (paperID,title,abstract,authorList,year,file,type,journal,referenceList,citationList,clusterId?)&gt;
 *       &lt;!ELEMENT paperID (#PCDATA)&gt;
 *       &lt;!ELEMENT title (#PCDATA)&gt;
 *       &lt;!ELEMENT abstract (#PCDATA)&gt;
 *       &lt;!ELEMENT authorList (author+)&gt;
 *       &lt;!ELEMENT author (#PCDATA)&gt;
 *       &lt;!ELEMENT year (#PCDATA)&gt;
 *       &lt;!ELEMENT file (#PCDATA)&gt;
 *       &lt;!ELEMENT type (#PCDATA)&gt;
 *       &lt;!ELEMENT journal (#PCDATA)&gt;
 *       &lt;!ELEMENT referenceList (reference*)&gt;
 *       &lt;!ELEMENT reference (#PCDATA)&gt;
 *       &lt;!ELEMENT citationList (citation*)&gt;
 *       &lt;!ELEMENT citation (#PCDATA)&gt;
 *       &lt;!ELEMENT clusterId (#PCDATA)&gt;
 *]&gt;
 *</pre>
 * 
 * @author Daniel McEnnis
 */
public class ParseBibliographyXML extends Handler {

    Graph graph = null;
    
    boolean biDirectionalReferences = false;

    /**
     * are created citations bidirectional
     * @return are citations bidirectional
     */
    public boolean isBiDirectionalReferences() {
        return biDirectionalReferences;
    }

    /**
     * Set whether citations are bidirectional
     * @param biDirectionalReferences are citations bidirectional
     */
    public void setBiDirectionalReferences(boolean biDirectionalReferences) {
        this.biDirectionalReferences = biDirectionalReferences;
    }

    enum State {

        START, BIBLIOGRAPHY, AUTHOR_REF, AUTHOR_NAME, AUTHOR_ID, PAPER,
        PAPER_ID, TITLE, ABSTRACT, AUTHOR_LIST, AUTHOR, YEAR, FILE, TYPE, JOURNAL,
        REFERENCE_LIST, REFERENCE, CITATION_LIST, CITATION, CLUSTER_ID
    };

     State  state  = State.START  ;
     String abstractContent = "";
     
     /**
      * Return the mode used for authors of papers.
      * @return mode for authors
      */
    public   String getAuthorMode( ) {
        return authorMode;
    }

    /**
     * Return the mode used for papers.
     * @return mode of papers
     */
    public   String getPaperMode() {
        return paperMode;
    }

    /**
     * Return the relation for authorship links
     * @return relation for authorship
     */
    public String getWroteRelation() {
        return wroteRelation;
    }
    
    /**
     * return the relation for citation links
     * @return relation for citation links
     */
    public String getReferencesRelation() {
        return referencesRelation;
    }

    /**
     * Set the mode used for authors of papers.
     * @param authorMode mode for authors
     */
    public void setAuthorMode(String authorMode) {
        this.authorMode = authorMode;
    }

    /**
     * Set the mode used for papers.
     * @param paperMode mode for papers.
     */
    public void setPaperMode(String paperMode) {
        this.paperMode = paperMode;
    }

    /**
     * Set the relation for authorship links
     * @param wroteRelation relation for authorship
     */
    public void setWroteRelation(String wroteRelation) {
        this.wroteRelation = wroteRelation;
    }

    /**
     * Set the relation for citation links
     * @param referencesRelation relation for citation links
     */
    public void setReferencesRelation(String referencesRelation) {
        this.referencesRelation = referencesRelation;
    }

    /**
     * Are subgraphs created from cluster designations in the XML file
     * @return are subgraphs created
     */
    public boolean isAddClusters() {
        return addClusters;
    }

    /**
     * Sets whether subgraphs are created or not from cluster data in the bibligoraphic entry
     * @param addClusters are subgraphs created
     */
    public void setAddClusters(boolean addClusters) {
        this.addClusters = addClusters;
    }
    
    boolean addClusters = true;
    String authorMode = "Author";
    String paperMode = "Paper";
    String wroteRelation = "Authored";
    String referencesRelation = "References";
    String authorName = "";
    String authorID = "";
    String paperID = "";
    Actor currentActor = null;
    Link currentLink = null;
    HashMap<String, HashSet<Actor>> subgraph = new HashMap<String, HashSet<Actor>>();
    int total = 0;
    int zero = 0;
    int one = 0;
    int two = 0;

    public ParseBibliographyXML() {
    }

    @Override
    public ParsedObject get() {
        return graph;
    }

    @Override
    public void set(ParsedObject o) {
        graph = (Graph) o;
    }

    @Override
    public Handler duplicate() {
        ParseBibliographyXML ret = new ParseBibliographyXML();
        ret.addClusters = addClusters;
        ret.authorMode = authorMode;
        ret.paperMode = paperMode;
        ret.wroteRelation = wroteRelation;
        ret.referencesRelation = referencesRelation;
        ret.authorName = authorName;
        ret.authorID = authorID;
        ret.paperID = paperID;
        ret.graph = graph;
        ret.biDirectionalReferences = biDirectionalReferences;
        return ret;
    }

    @Override
    public void startDocument() throws SAXException {
        state = State.START;
    }

    @Override
    public void endDocument() throws SAXException {
        if (addClusters) {
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
                    Logger.getLogger(ParseBibliographyXML.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (("bibliography".equalsIgnoreCase(localName)) || ("bibliography".equalsIgnoreCase(qName))) {
            state = State.BIBLIOGRAPHY;
        } else if (("authorRef".equalsIgnoreCase(localName)) || ("authorRef".equalsIgnoreCase(qName))) {
            state = State.AUTHOR_REF;
        } else if (("authorName".equalsIgnoreCase(localName)) || ("authorName".equalsIgnoreCase(qName))) {
            state = State.AUTHOR_NAME;
        } else if (("authorID".equalsIgnoreCase(localName)) || ("authorID".equalsIgnoreCase(qName))) {
            state = State.AUTHOR_ID;
        } else if (("paper".equalsIgnoreCase(localName)) || ("paper".equalsIgnoreCase(qName))) {
            state = State.PAPER;
        } else if (("paperID".equalsIgnoreCase(localName)) || ("paperID".equalsIgnoreCase(qName))) {
            state = State.PAPER_ID;
        } else if (("title".equalsIgnoreCase(localName)) || ("title".equalsIgnoreCase(qName))) {
            state = State.TITLE;
        } else if (("abstract".equalsIgnoreCase(localName)) || ("abstract".equalsIgnoreCase(qName))) {
            state = State.ABSTRACT;
            abstractContent = "";
        } else if (("authorList".equalsIgnoreCase(localName)) || ("authorList".equalsIgnoreCase(qName))) {
            state = State.AUTHOR_LIST;
        } else if (("author".equalsIgnoreCase(localName)) || ("author".equalsIgnoreCase(qName))) {
            state = State.AUTHOR;
        } else if (("year".equalsIgnoreCase(localName)) || ("year".equalsIgnoreCase(qName))) {
            state = State.YEAR;
        } else if (("file".equalsIgnoreCase(localName)) || ("file".equalsIgnoreCase(qName))) {
            state = State.FILE;
        } else if (("type".equalsIgnoreCase(localName)) || ("type".equalsIgnoreCase(qName))) {
            state = State.TYPE;
        } else if (("journal".equalsIgnoreCase(localName)) || ("journal".equalsIgnoreCase(qName))) {
            state = State.JOURNAL;
        } else if (("referenceList".equalsIgnoreCase(localName)) || ("referenceList".equalsIgnoreCase(qName))) {
            state = State.REFERENCE_LIST;
        } else if (("reference".equalsIgnoreCase(localName)) || ("reference".equalsIgnoreCase(qName))) {
            state = State.REFERENCE;
        } else if (("citationList".equalsIgnoreCase(localName)) || ("citationList".equalsIgnoreCase(qName))) {
            state = State.CITATION_LIST;
        } else if (("citation".equalsIgnoreCase(localName)) || ("citation".equalsIgnoreCase(qName))) {
            state = State.CITATION;
        } else if (("clusterId".equalsIgnoreCase(localName)) || ("clusterId".equalsIgnoreCase(qName))) {
            state = State.CLUSTER_ID;
        } else {

        }

    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (state == State.BIBLIOGRAPHY) {
            state = State.START;
            Logger.getLogger(ParseBibliographyXML.class.getName()).log(Level.FINE,"Total papers: " + total);
            Logger.getLogger(ParseBibliographyXML.class.getName()).log(Level.FINE,"Cluster 0: " + zero);
            Logger.getLogger(ParseBibliographyXML.class.getName()).log(Level.FINE,"Cluster 1: " + one);
            Logger.getLogger(ParseBibliographyXML.class.getName()).log(Level.FINE,"Cluster 2 " + two);
        } else if (state == State.AUTHOR_REF) {
            // Create a new Author node
            Properties props = new Properties();
            props.setProperty("ActorClass", "Basic");
            props.setProperty("ActorType", authorMode);
            props.setProperty("ActorID", authorID);
            currentActor = ActorFactory.newInstance().create(props);
            props.setProperty("PropertyClass", "java.lang.String");
            props.setProperty("PropertyID", "name");
            props.setProperty("PropertyType", "Basic");
            Property name = PropertyFactory.newInstance().create(props);
            try {
                name.add(authorName);
            } catch (InvalidObjectTypeException ex) {
                Logger.getLogger("global").log(Level.SEVERE, null, ex);
            }
            currentActor.add(name);
            graph.add(currentActor);
            state = State.BIBLIOGRAPHY;
        } else if (state == State.AUTHOR_NAME) {
            state = State.AUTHOR_REF;
        } else if (state == State.AUTHOR_ID) {
            state = State.AUTHOR_REF;
        } else if (state == State.PAPER) {
            state = State.BIBLIOGRAPHY;
            total++;
        } else if (state == State.PAPER_ID) {
            state = State.PAPER;
        } else if (state == State.TITLE) {
            state = State.PAPER;
        } else if (state == State.ABSTRACT) {
            java.util.Properties props = new java.util.Properties();
            props.setProperty("PropertyClass", "java.lang.String");
            props.setProperty("PropertyID", "abstract");
            props.setProperty("PropertyType", "Basic");
            nz.ac.waikato.mcennis.rat.graph.property.Property property = nz.ac.waikato.mcennis.rat.graph.property.PropertyFactory.newInstance().create(props);
            try {
                property.add(abstractContent);
            } catch (InvalidObjectTypeException ex) {
                Logger.getLogger("global").log(Level.SEVERE, null, ex);
            }
            currentActor.add(property);
            state = State.PAPER;
        } else if (state == State.AUTHOR_LIST) {
            state = State.PAPER;
        } else if (state == State.AUTHOR) {
            state = State.PAPER;
        } else if (state == State.YEAR) {
            state = State.PAPER;
        } else if (state == State.FILE) {
            state = State.PAPER;
        } else if (state == State.TYPE) {
            state = State.PAPER;
        } else if (state == State.JOURNAL) {
            state = State.PAPER;
        } else if (state == State.REFERENCE_LIST) {
            state = State.PAPER;
        } else if (state == State.REFERENCE) {
            state = State.REFERENCE_LIST;
        } else if (state == State.CITATION_LIST) {
            state = State.PAPER;
        } else if (state == State.CITATION) {
            state = State.CITATION_LIST;
        } else if (state == State.CLUSTER_ID) {
            state = State.PAPER;
        } else {
            Logger.getLogger(ParseBibliographyXML.class.getName()).log(Level.SEVERE,"BAD STATE" + localName);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String str = new String(ch, start, length);
        if (state == State.AUTHOR_ID) {
            authorID = str;
        } else if (state == State.AUTHOR_NAME) {
            authorName = str;
        } else if (state == State.PAPER_ID) {
            currentActor = graph.getActor(paperMode, str);
            if (currentActor == null) {
                Properties props = new Properties();
                props.setProperty("ActorClass", "Basic");
                props.setProperty("ActorType", paperMode);
                props.setProperty("ActorID", str);
                currentActor = ActorFactory.newInstance().create(props);
                graph.add(currentActor);
            }
        } else if (state == State.YEAR) {
            java.util.Properties props = new java.util.Properties();
            props.setProperty("PropertyClass", "java.lang.String");
            props.setProperty("PropertyID", "year");
            props.setProperty("PropertyType", "Basic");
            nz.ac.waikato.mcennis.rat.graph.property.Property property = nz.ac.waikato.mcennis.rat.graph.property.PropertyFactory.newInstance().create(props);
            try {
                property.add(str);
            } catch (InvalidObjectTypeException ex) {
                Logger.getLogger("global").log(Level.SEVERE, "Property class does not match java.lang.String", ex);
            }
            currentActor.add(property);
        } else if (state == State.FILE) {
            java.util.Properties props = new java.util.Properties();
            props.setProperty("PropertyClass", "java.lang.String");
            props.setProperty("PropertyID", "file");
            props.setProperty("PropertyType", "Basic");
            nz.ac.waikato.mcennis.rat.graph.property.Property property = nz.ac.waikato.mcennis.rat.graph.property.PropertyFactory.newInstance().create(props);
            try {
                property.add(str);
            } catch (InvalidObjectTypeException ex) {
                Logger.getLogger("global").log(Level.SEVERE, "Property class does not match java.lang.String", ex);
            }
            currentActor.add(property);
        } else if (state == State.TYPE) {
            java.util.Properties props = new java.util.Properties();
            props.setProperty("PropertyClass", "java.lang.String");
            props.setProperty("PropertyID", "type");
            props.setProperty("PropertyType", "Basic");
            nz.ac.waikato.mcennis.rat.graph.property.Property property = nz.ac.waikato.mcennis.rat.graph.property.PropertyFactory.newInstance().create(props);
            try {
                property.add(str);
            } catch (InvalidObjectTypeException ex) {
                Logger.getLogger("global").log(Level.SEVERE, "Property class does not match java.lang.String", ex);
            }
            currentActor.add(property);
        } else if (state == State.JOURNAL) {
            java.util.Properties props = new java.util.Properties();
            props.setProperty("PropertyClass", "java.lang.String");
            props.setProperty("PropertyID", "journal");
            props.setProperty("PropertyType", "Basic");
            nz.ac.waikato.mcennis.rat.graph.property.Property property = nz.ac.waikato.mcennis.rat.graph.property.PropertyFactory.newInstance().create(props);
            try {
                property.add(str);
            } catch (InvalidObjectTypeException ex) {
                Logger.getLogger("global").log(Level.SEVERE, "Property class does not match java.lang.String", ex);
            }
            currentActor.add(property);
        } else if (state == State.REFERENCE) {
            Actor referent = graph.getActor(paperMode, str);
            Properties props = new Properties();
            if (referent == null) {
                props.setProperty("ActorClass", "Basic");
                props.setProperty("ActorType", paperMode);
                props.setProperty("ActorID", str);
                referent = ActorFactory.newInstance().create(props);
                graph.add(referent);
            }
            props.setProperty("LinkClass", "Basic");
            props.setProperty("LinkType", referencesRelation);
            Link referencesLink = LinkFactory.newInstance().create(props);
            referencesLink.set(currentActor, 1.0, referent);
            graph.add(referencesLink);
            if(biDirectionalReferences){
                referencesLink = LinkFactory.newInstance().create(props);
                referencesLink.set(referent, 1.0, currentActor);
                graph.add(referencesLink);
            }
        } else if (state == State.AUTHOR) {
            Actor author = graph.getActor(authorMode, str);
            if (author != null) {
                java.util.Properties props = new java.util.Properties();
                props.setProperty("LinkClass", "Basic");
                props.setProperty("LinkType", wroteRelation);
                Link authorLink = LinkFactory.newInstance().create(props);
                authorLink.set(author, 1.0, currentActor);
                graph.add(authorLink);
            }
        } else if (state == State.CLUSTER_ID) {
            if (str.contentEquals("0")) {
                zero++;
            } else if (str.contentEquals("1")) {
                one++;
            } else if (str.contentEquals("2")) {
                two++;
            }

            java.util.Properties props = new java.util.Properties();
            props.setProperty("PropertyClass", "java.lang.String");
            props.setProperty("PropertyID", "cluster");
            props.setProperty("PropertyType", "Basic");
            nz.ac.waikato.mcennis.rat.graph.property.Property property = nz.ac.waikato.mcennis.rat.graph.property.PropertyFactory.newInstance().create(props);
            try {
                property.add(str);
            } catch (InvalidObjectTypeException ex) {
                Logger.getLogger("global").log(Level.SEVERE, "Property class does not match java.lang.String", ex);
            }
            currentActor.add(property);
            if (!subgraph.containsKey(str)) {
                subgraph.put(str, new HashSet<Actor>());
            }
            subgraph.get(str).add(currentActor);
        } else if (state == State.TITLE) {
            java.util.Properties props = new java.util.Properties();
            props.setProperty("PropertyClass", "java.lang.String");
            props.setProperty("PropertyID", "title");
            props.setProperty("PropertyType", "Basic");
            nz.ac.waikato.mcennis.rat.graph.property.Property property = nz.ac.waikato.mcennis.rat.graph.property.PropertyFactory.newInstance().create(props);
            try {
                property.add(str);
            } catch (InvalidObjectTypeException ex) {
                Logger.getLogger("global").log(Level.SEVERE, "Property class does not match java.lang.String", ex);
            }
            currentActor.add(property);
        } else if (state == State.ABSTRACT) {
            abstractContent += str;
        }
    }
}
