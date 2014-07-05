/*

 * Created 13/05/2008

 * Copyright Daniel McEnnis, see license.txt

 */

package nz.ac.waikato.mcennis.rat.parser.xmlHandler;



import java.io.File;

import java.io.IOException;

import java.net.MalformedURLException;

import java.net.URLEncoder;

import java.util.logging.Level;

import java.util.logging.Logger;

import nz.ac.waikato.mcennis.rat.crawler.Crawler;

import nz.ac.waikato.mcennis.rat.graph.Graph;

import nz.ac.waikato.mcennis.rat.graph.actor.Actor;

import nz.ac.waikato.mcennis.rat.graph.actor.ActorFactory;

import nz.ac.waikato.mcennis.rat.graph.link.Link;

import nz.ac.waikato.mcennis.rat.graph.link.LinkFactory;

import nz.ac.waikato.mcennis.rat.parser.ParsedObject;

import nz.ac.waikato.mcennis.rat.parser.ToFileParser;

import org.xml.sax.Attributes;

import org.xml.sax.Locator;

import org.xml.sax.SAXException;



/**

 * Parse the Neighbour XML file from LastFM.  See audioscrobbler.net for a description of the file

 * format.  Usually obtained from the CrawlLastFM data aquisition module.  Contains all

 * usernames that are suffeciently similar to this user by artist playcount.  Also provides the

 * degree of similarity in a double between 0-1.

 * 

 * @author Daniel McEnnis

 */

public class LastFMSimilarUsers extends Handler {



    Graph graph = null;

    Actor source = null;

    Actor dest = null;

    Crawler crawler = null;

    LastFMUserExpansion expansion = null;

    ToFileParser parser = null;

    private Locator locator;



    public LastFMSimilarUsers(){

        super();

        properties.get("ParserClass").add("LastFMSimilarUsers");

        properties.get("Name").add("LastFMSimilarUsers");

    }

    /**

     * Set where the given parser will store files when they are parsed

     * @param parser ToFileParser to store files parsed by this parser

     */

    public void setParser(ToFileParser parser) {

        this.parser = parser;

    }



    enum State {START, NEIGHBORS, USER, MATCH, OTHER    };



     State state = State.START  ;

    

    @Override

    public   ParsedObject get() {         

        return graph;

    }



    @Override

    public void set(ParsedObject o) {

        graph = (Graph)o;

    }



    @Override

    public Handler duplicate() {

        LastFMSimilarUsers ret = new LastFMSimilarUsers();

        ret.graph = this.graph;

        ret.expansion = this.expansion;

        ret.crawler = this.crawler;

        return ret;

    }



    @Override

    public void endElement(String uri, String localName, String qName) throws SAXException {

try{        if(state == State.USER){

            state=State.NEIGHBORS;

        }else if(state == State.NEIGHBORS){

            state=State.START;

        }else if(state == State.MATCH){

            state = state.USER;

        }else if(state == State.OTHER){

            state = state.USER;

        }

        } catch (NullPointerException ex) {

            Logger.getLogger(LastFMSimilarUsers.class.getName()).log(Level.SEVERE, "Null Pointer Exception at line " + locator.getLineNumber());

        }

    }



    @Override

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

try{        if((localName.contentEquals("neighbors")) || (qName.contentEquals("neighbors"))) {

            state = State.NEIGHBORS;

            String username = attributes.getValue("user");

            if ((source = graph.getActor("user", username)) == null) {

                source = ActorFactory.newInstance().create("user",username);

                graph.add(source);

            }

            if (parser != null) {

                parser.setSubDirectory(username + File.separator);

                parser.setFilename("neighbors.xml");

            }

        } else if ((localName.contentEquals("user")) || (qName.contentEquals("user"))) {

            state = State.USER;

            String username = attributes.getValue("username");



            if ((dest = graph.getActor("user", username)) == null) {

                dest = ActorFactory.newInstance().create("user",username);

                graph.add(dest);

            }



        } else if ((localName.contentEquals("match")) || (qName.contentEquals("match"))) {

            state = State.MATCH;

        } else {

            state = state.OTHER;

        }

        } catch (NullPointerException ex) {

            Logger.getLogger(LastFMSimilarUsers.class.getName()).log(Level.SEVERE, "Null Pointer Exception at line " + locator.getLineNumber());

        }

    }



    @Override

    public void characters(char[] ch, int start, int length) throws SAXException {

try{

    if (state == State.MATCH) {

            double match = Double.parseDouble(new String(ch, start, length));

            Link similar = LinkFactory.newInstance().create("Similar");

            if((source != null)&&(dest != null)){

                similar.set(source, match, dest);

                graph.add(similar);

            }

            if ((expansion != null)&&(crawler != null)) {

                try {

                    expansion.expandUser(crawler, dest.getID());

                } catch (MalformedURLException ex) {

                    Logger.getLogger(LastFMSimilarUsers.class.getName()).log(Level.SEVERE, null, ex);

                } catch (IOException ex) {

                    Logger.getLogger(LastFMSimilarUsers.class.getName()).log(Level.SEVERE, null, ex);

                }

            }

        }

        } catch (NullPointerException ex) {

            Logger.getLogger(LastFMSimilarUsers.class.getName()).log(Level.SEVERE, "Null Pointer Exception at line " + locator.getLineNumber());

        }

    }



    @Override

    public void setCrawler(Crawler crawler) {

        this.crawler = crawler;

    }



    /**

     * Set the reference to the object that handles setting up the crawling of new users

     * using LastFM webservices.

     * @param expansion object to handle crawling of users

     */ 

    public void setExpansion(LastFMUserExpansion expansion) {

        this.expansion = expansion;

    }



    @Override

    public void setDocumentLocator(Locator locator) {



        this.locator = locator;



    }

}

