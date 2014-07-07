/*
 * Created 13-5-08
 * Copyright Daniel McEnnis, see license.txt
 */
package org.mcennis.graphrat.parser.xmlHandler;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mcennis.graphrat.crawler.Crawler;
import org.mcennis.graphrat.graph.Graph;
import org.mcennis.graphrat.actor.Actor;
import org.mcennis.graphrat.actor.ActorFactory;
import org.mcennis.graphrat.link.Link;
import org.mcennis.graphrat.link.LinkFactory;
import org.mcennis.graphrat.parser.ParsedObject;
import org.mcennis.graphrat.parser.ToFileParser;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * Parse the Friends XML file from LastFM.  See audioscrobbler.net for a description of the file
 * format.  Usually obtained from the CrawlLastFM data aquisition module. Contains
 * all LastFM users that this person has declared to be their friend.
 * 
 * @author Daniel McEnnis
 */
public class LastFMFriends extends Handler {

    Graph graph = null;
    Actor source = null;
    Actor dest = null;
    Crawler crawler = null;
    LastFMUserExpansion expansion = null;
    ToFileParser parser = null;
    private Locator locator;

    /**
     * Set where the ToFileParser will store files parsed by this parser.
     * @param parser reference to ToFileParser to use.
     */
    public void setParser(ToFileParser parser) {
        this.parser = parser;
    }

    enum State {

        START, FRIENDS, USER
    };
    State state = State.START;

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
        LastFMFriends ret = new LastFMFriends();
        ret.graph = this.graph;
        ret.crawler = this.crawler;
        ret.expansion = this.expansion;
        return ret;
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        try {
            if ((localName.contentEquals("user")) || (qName.contentEquals("user"))) {
                state = State.FRIENDS;
            } else if (state == State.FRIENDS) {
                state = State.START;
            }
        } catch (NullPointerException ex) {
            Logger.getLogger(LastFMFriends.class.getName()).log(Level.SEVERE, "Null Pointer Exception at line " + locator.getLineNumber());
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        try {
            if ((localName.contentEquals("friends")) || (qName.contentEquals("friends"))) {
                state = State.FRIENDS;
                String username = attributes.getValue("user");
                if ((source = graph.getActor("user", username)) == null) {
                    Properties props = new Properties();
                    props.setProperty("ActorClass", "Basic");
                    props.setProperty("ActorType", "user");
                    props.setProperty("ActorID", username);
                    source = ActorFactory.newInstance().create(props);
                    graph.add(source);
                }

                if (parser != null) {
                    parser.setSubDirectory(username + File.separator);
                    parser.setFilename("friends.xml");
                }
            } else if ((localName.contentEquals("user")) || (qName.contentEquals("user"))) {
                try {
                    state = State.USER;
                    String username = attributes.getValue("username");
                    if ((dest = graph.getActor("user", username)) == null) {
                        Properties props = new Properties();
                        props.setProperty("ActorClass", "Basic");
                        props.setProperty("ActorType", "user");
                        props.setProperty("ActorID", username);
                        dest = ActorFactory.newInstance().create(props);
                        graph.add(dest);
                    }
                    Properties props = new Properties();
                    props.setProperty("LinkClass", "Basic");
                    props.setProperty("LinkType", "Friend");
                    Link friend = LinkFactory.newInstance().create(props);
                    if ((source != null) && (dest != null)) {
                        friend.set(source, 1.0, dest);
                        graph.add(friend);
                    }
                    if (crawler != null) {
                        expansion.expandUser(crawler, username);
                    }

                } catch (MalformedURLException ex) {
                    Logger.getLogger(LastFMFriends.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(LastFMFriends.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (NullPointerException ex) {
            Logger.getLogger(LastFMFriends.class.getName()).log(Level.SEVERE, "Null Pointer Exception at line " + locator.getLineNumber());
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
