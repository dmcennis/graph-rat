/*
 * ArtistHandlerFactory.java
 *
 * Created on 8 June 2007, 17:14
 *
 * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt)
 */

package nz.ac.waikato.mcennis.rat.parser.xmlHandler;

import java.util.Properties;
import nz.ac.waikato.bibliography.XMLParser.ParseBibliographyXML;
import nz.ac.waikato.bibliography.XMLParser.ParseClassLabel;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import nz.ac.waikato.mcennis.rat.graph.GraphFactory;

/**
 * Factory class for creating SAX XML parsers for use with the XML Parser class.
 * 
 * @author Daniel McEnnis
 */
public class HandlerFactory {
    
    static HandlerFactory instance = null;
    
    /**
     * Return a reference to a singelton HandlerFactory
     * 
     * @return newly created reference to a HandlerFactory
     */
    public static HandlerFactory newInstance(){
        if(instance == null){
            instance = new HandlerFactory();
        }
        return instance;
    }
    
    /** Creates a new instance of ArtistHandlerFactory */
    private HandlerFactory() {
    }
    
    /**
     * Creates a new Handler object.  The class of XML handler is controlled by the 
     * 'HandlerType' key.  Returns 'FOAF2Graph' if the key matches no handler type.
     * 
     * <ul>
     * <li/>'LastFM' - LastFm XML handler
     * <li/>'Null' - FOAF with graph created with properties (assumed to be creating a null graph)
     * <li/>'UserList' - FOAF with graph created with properties (assumed to be creating a UserList graph)
     * <li/>'Graph' - GraphReader handler returned
     * <li/>'ArtistDecider' - XMLArtistReader returned
     * <li/>'RATExecution' - RATExecution handler returned
     * <li/>'FOAF' - create a FOAF handler with properties set. There are a number of parameters<ul>
     *  <li/>'actorClass' - Classname of the kind of actor.
     *  <li/>'actorType' - type (mode) of the actors produced
     *  <li/>'linkType' - type (relation) of the links created
     *  <li/>'linkClass' - Classname for the kind of links
     *  <li/>'propertyClass' - Classname for the kind of Properties created
     *  </ul>
     * <li/>'YahooArtistDecider' - return YahooArtistDecider XML parser.
     * <li/>'BibliographyXML' - return Bibliography Parser. Has a number of parameters <ul>
     *  <li/>'authorMode' - type for author actors
     *  <li/>'paperModel' - type for paper actors
     *  <li/>'wroteRelation' - relation between authors and their papers
     *  <li/>'referencesRelation' - relation between a paper and the papers it references
     *  <li/>'bidirectionalReferences' - whether citations are bidirectional or not. default 'false'</ul>
     * <li/>'LastFMArtistTag' - returns a new LastFMArtistTag parser
     * </ul>
     * 
     * 
     * @param props parameters for creating a HandlerObject
     * @return newly created Handler object
     */
    public Handler create(Properties props){
        if("LastFm".contentEquals(props.getProperty("HandlerType"))){
            return new LastFmArtistHandler();
        }else if("Null".contentEquals(props.getProperty("HandlerType"))){
            FOAF2Graph foaf = new FOAF2Graph();
            foaf.setGraph(GraphFactory.newInstance().create(props));
            return foaf;
        }else if("UserList".contentEquals(props.getProperty("HandlerType"))){
            FOAF2Graph foaf = new FOAF2Graph();
            foaf.setGraph(GraphFactory.newInstance().create(props));
            return foaf;
        }else if("Graph".equalsIgnoreCase(props.getProperty("HandlerType"))){
            GraphReader ret = new GraphReader();
            ret.setGraph(GraphFactory.newInstance().create(props));
            return ret;
        }else if("ArtistDecider".equalsIgnoreCase(props.getProperty("HandlerType"))){
            return new ArtistDeciderXML();
        }else if("RATExecution".equalsIgnoreCase(props.getProperty("HandlerType"))){
            return new RATExecution();
        }else if("FOAF".equalsIgnoreCase(props.getProperty("HandlerType"))){
            FOAF2Graph foaf = new FOAF2Graph();
            Graph graph = GraphFactory.newInstance().create(props);
            if(props.getProperty("actorClass") != null){
                foaf.setActorClass(props.getProperty("actorClass"));
            }
            if(props.getProperty("actorType") != null){
                foaf.setActorType(props.getProperty("actorType"));
            }
            if(props.getProperty("linkType") != null){
                foaf.setLinkType(props.getProperty("linkType"));
            }
            if(props.getProperty("linkClass") != null){
                foaf.setLinkClass(props.getProperty("linkClass"));
            }
            if(props.getProperty("propertyClass") != null){
                foaf.setPropertyClass(props.getProperty("propertyClass"));
            }
            foaf.setGraph(graph);
            return foaf;
        }else if("YahooArtistDecider".equalsIgnoreCase(props.getProperty("HandlerType"))){
            return new YahooArtistHandler();
        }else if("LastFMArtistTag".equalsIgnoreCase(props.getProperty("HandlerType"))){
            return new LastFMArtistTag();
        }else if("BibliographyXML".equalsIgnoreCase(props.getProperty("HandlerType"))){
            ParseBibliographyXML ret =  new ParseBibliographyXML();
            if(props.getProperty("authorMode")!=null){
                ret.setAuthorMode(props.getProperty("authorMode"));
            }
            if(props.getProperty("paperMode")!=null){
                ret.setPaperMode(props.getProperty("paperMode"));
            }
            if(props.getProperty("wroteRelation")!=null){
                ret.setWroteRelation(props.getProperty("wroteRelation"));
            }
            if(props.getProperty("referencesRelation")!=null){
                ret.setReferencesRelation(props.getProperty("referencesRelation"));
            }
            if("false".equalsIgnoreCase(props.getProperty("addClusters"))){
                ret.setAddClusters(false);
            }
            if("true".equalsIgnoreCase(props.getProperty("bidirectionalReferences"))){
                ret.setBiDirectionalReferences(true);
            }
            return ret;
        }else if("ParseClassLabel".equalsIgnoreCase(props.getProperty("HandlerType"))){
            ParseClassLabel ret =  new ParseClassLabel();
            return ret;
        }else{
            return new FOAF2Graph();
        }
    }
}
