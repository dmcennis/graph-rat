/*

 * GraphParser.java

 *

 * Created on 1 May 2007, 20:36

 *

 * copyright Daniel McEnnis - published under Aferro GPL (see license.txt)

 */



package nz.ac.waikato.mcennis.rat.parser;



import java.io.InputStream;

import nz.ac.waikato.mcennis.rat.crawler.Crawler;


import nz.ac.waikato.mcennis.rat.parser.xmlHandler.Handler;

import org.dynamicfactory.descriptors.Parameter;

import org.dynamicfactory.descriptors.Properties;


/**

 * Class for parsing XML documents.  Utilizes custom SAX parsers to handle the 

 * documents.

 *

 * @author Daniel McEnnis

 * 

 */

public class XMLParser extends AbstractParser{

    

    Handler handler;

    String id = null;

    

    /** Creates a new instance of GraphParser */

    public XMLParser() {

        super();

        properties.get("ParserClass").add("XMLParser");

        properties.get("Name").add("XMLParser");

        handler = null;

    }

    

    @Override

    public void parse(InputStream data, String site) throws Exception{

//        try {

            javax.xml.parsers.SAXParser parser = javax.xml.parsers.SAXParserFactory.newInstance().newSAXParser();

            handler.setSite(site);
            parser.parse(data, handler);

//        } catch (ParserConfigurationException e) {

//            e.printStackTrace();

//        } catch (SAXException e) {

//            System.err.println(e.getMessage());

//        } catch (IOException e) {

//            System.err.println(e.getMessage());

//            e.printStackTrace();

//        } catch (NullPointerException e){

//            e.printStackTrace();

//        }

    }

    

    @Override

    public Parser duplicate() {

        XMLParser ret =  new XMLParser();

        ret.properties = this.properties.duplicate();

        ret.setHandler(handler.duplicate());

        return ret;

    }

    

    @Override

    public void parse(InputStream data, Crawler crawler,String site) throws Exception{

//        try {

            handler.setCrawler(crawler);
            handler.setSite(site);

            javax.xml.parsers.SAXParser parser = javax.xml.parsers.SAXParserFactory.newInstance().newSAXParser();

            parser.parse(data, handler);

     //       parser.setErrorHandler

//            parser.parse(data, handler);

//        } catch (ParserConfigurationException e) {

//            e.printStackTrace();

//        } catch (SAXException e) {

//            System.err.println(e.getMessage());

//        } catch (IOException e) {

//            System.err.println(e.getMessage());

//        }

    }

    

    /**

     * Returns the parsed object of the handler

     * @return handler's parsed object

     */

    public ParsedObject get(){

        return handler.get();

    }

    

    /**

     * Sets the handler to use when parsing XML documents

     * @param h handler to use

     */

    public void setHandler(Handler h){

        properties.get("ParserClass").clear();

        properties.get("ParserClass").add(h.getParameter("ParserClass").getValue().iterator().next());

        properties.get("Name").clear();

        properties.get("Name").add(h.getParameter("Name").getValue().iterator().next());

        handler = h;

    }

    

    /**

     * Returns a copy of the currently set handler

     * @return handler

     */

    public Handler getHandler(){

        return handler;

    }

    

    /**

     * Sets the ParsedObject of the Handler

     * @param o object to be set

     */

    public void set(ParsedObject o) {

        handler.set(o);

    }

    

    public boolean check(Properties properties){

        boolean good = this.properties.check(properties);

        if(handler != null){

            if(!handler.check(properties)){

                good =false;

            }

        }

        return good;

    }

    

    public boolean check(Parameter param){

        boolean good  = this.properties.check(param);

        if(handler!=null){

            if(!handler.check(param)){

                good =false;

            }

        }

        return good;

    }



}

