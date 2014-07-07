/*
 * GraphParser.java
 *
 * Created on 1 May 2007, 20:36
 *
 * copyright Daniel McEnnis - published under Aferro GPL (see license.txt)
 */

package org.mcennis.graphrat.parser;

import java.io.InputStream;
import org.mcennis.graphrat.crawler.Crawler;

import org.mcennis.graphrat.parser.xmlHandler.Handler;




/**
 * Class for parsing XML documents.  Utilizes custom SAX parsers to handle the 
 * documents.
 *
 * @author Daniel McEnnis
 * 
 */
public class XMLParser implements Parser{
    
    Handler handler;
    String id = null;
    
    /** Creates a new instance of GraphParser */
    public XMLParser() {
        handler = null;
    }
    
    @Override
    public void parse(InputStream data) throws Exception{
//        try {
            javax.xml.parsers.SAXParser parser = javax.xml.parsers.SAXParserFactory.newInstance().newSAXParser();
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
        ret.setHandler(handler.duplicate());
        return ret;
    }
    
    @Override
    public void parse(InputStream data, Crawler crawler) throws Exception{
//        try {
            handler.setCrawler(crawler);
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
        if(id == null){
            id = h.getClass().getSimpleName();
        }
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
    

    public void setName(String name) {
        id = name;
    }

    public String getName() {
        return id;
    }
    
}
