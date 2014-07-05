/*

 * ArtistHandlerFactory.java

 *

 * Created on 8 June 2007, 17:14

 *

 * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt)

 */



package nz.ac.waikato.mcennis.rat.parser.xmlHandler;



import java.util.logging.Logger;

import java.util.logging.Level;

import nz.ac.waikato.bibliography.XMLParser.ParseBibliographyXML;

import nz.ac.waikato.bibliography.XMLParser.ParseClassLabel;

import nz.ac.waikato.mcennis.rat.AbstractFactory;

import org.dynamicfactory.descriptors.Parameter;

import org.dynamicfactory.descriptors.ParameterFactory;

import org.dynamicfactory.descriptors.ParameterInternal;

import org.dynamicfactory.descriptors.Properties;

import org.dynamicfactory.descriptors.SyntaxCheckerFactory;

import org.dynamicfactory.descriptors.SyntaxObject;



/**

 * Factory class for creating SAX XML parsers for use with the XML Parser class.

 * 

 * @author Daniel McEnnis

 */

public class HandlerFactory extends AbstractFactory<Handler>{

    

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

        ParameterInternal name = ParameterFactory.newInstance().create("ParserClass",String.class);

        SyntaxObject syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);

        name.setRestrictions(syntax);

        name.add("MemGraph");

        properties.add(name);

        

        map.put("MemGraph", new GraphReader());

        map.put("BasicScheduler", new RATExecution());

        map.put("FOAF", new FOAF2Graph());

        map.put("LastFMArtistTag", new LastFMArtistTag());

        map.put("LastFMFriends", new LastFMFriends());

        map.put("LastFMProfile", new LastFMProfile());

        map.put("LastFMSimilarUsers", new LastFMSimilarUsers());

        map.put("LastFMUserArtistTags", new LastFMUserArtistTags());

        map.put("LastFMUserTags", new LastFMUserTags());

        map.put("LastFMUserTopArtists", new LastFMUserTopArtists());

        map.put("BibliographyXML", new ParseBibliographyXML());

        map.put("ParseClassLabel", new ParseClassLabel());

        map.put("QueryReader",new QueryReader());

    }

/*    public Handler create(Properties props){

if("BibliographyXML".equalsIgnoreCase(props.getProperty("HandlerType"))){

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

        }

    }*/



    @Override

    public Handler create(Properties props) {

        return create(null,props);

    }

    

    public Handler create(String classType){

        return create(classType,properties);

    }

    

    public Handler create(String classType, Properties parameters){

        if(classType != null){

        if ((parameters.get("ParserClass") != null) && (parameters.get("ParserClass").getParameterClass().getName().contentEquals(String.class.getName()))) {

            classType = (String) parameters.get("ParserClass").getValue().iterator().next();

        } else {

            classType = (String) properties.get("ParserClass").getValue().iterator().next();

        }

        }

        Handler ret = null;

        if(map.containsKey(classType)){

            ret = (Handler)map.get(classType).duplicate();

        }else{

            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE,"Parser class '"+classType+"' not found - assuming MemGraph");

            ret = new GraphReader();

        }

        ret.init(parameters);

        return ret;

     }



    @Override

    public boolean check(Properties parameters) {

        String classType = "";

        if ((parameters.get("ParserClass") != null) && (parameters.get("ParserClass").getParameterClass().getName().contentEquals(String.class.getName()))) {

            classType = (String) parameters.get("ParserClass").getValue().iterator().next();

        } else {

            classType = (String) properties.get("ParserClass").getValue().iterator().next();

        }



        if(map.containsKey(classType)){

            return map.get(classType).getParameter().check(parameters);

        }else{

            Logger.getLogger(this.getClass().getName()).log(Level.INFO,"Parser '"+classType+"' does not exist");

            return false;

        }

    }

    @Override

    public Parameter getClassParameter() {

        return properties.get("ParserClass");

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

}

