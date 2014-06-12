/*
 * Created 13/05/2008-16:53:29
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.parser.xmlHandler;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Properties;
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
 *
 * Parse the Profile XML file from LastFM.  See audioscrobbler.net for a description of the file
 * format.  Usually obtained from the CrawlLastFM data aquisition module. Contains
 * the set of the top 50 artists listened to by this user along with playcounts.
 *
 * @author Daniel McEnnis
 */
public class LastFMUserTopArtists extends Handler {

    Graph graph = null;
    Actor user = null;
    Actor artist = null;
    ToFileParser parser = null;
    Crawler crawler = null;
    String name = "";
    static HashSet<String> seenArtists = new HashSet<String>();
    private Locator locator;

    enum State {

        START, TOPARTISTS, ARTIST, NAME, PLAYCOUNT, OTHER
    };
    State state = State.START;

    /**
     * Set where the given parser will store files when they are parsed
     * @param parser ToFileParser to store files parsed by this parser
     */
    public void setParser(ToFileParser parser) {
        this.parser = parser;
    }
    String[] artistParsers = new String[]{};
    String[] userArtistTagParsers = new String[]{};

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        try {
            if (state == State.NAME) {
                name += new String(ch, start, length);

            } else if (state == State.PLAYCOUNT) {
                String count = new String(ch, start, length);
                if ((user != null) && (artist != null)) {
                    Link[] test = graph.getLink("listensTo", user, artist);
                    if (test == null) {
                        Properties props = new Properties();
                        props.setProperty("LinkClass", "Basic");
                        props.setProperty("LinkType", "ListensTo");
                        Link userArtist = LinkFactory.newInstance().create(props);
                        userArtist.set(user, Integer.parseInt(count), artist);
                        graph.add(userArtist);
                    } else {
                        //System.out.println("Existing lin")
                    }
                }
            }
        } catch (NullPointerException ex) {
            Logger.getLogger(LastFMUserTopArtists.class.getName()).log(Level.SEVERE, "Null Pointer Exception at line " + locator.getLineNumber());
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        try {
            if (state == State.TOPARTISTS) {

                state = State.START;
            } else if (state == State.ARTIST) {
                state = State.TOPARTISTS;
            } else if (state == State.NAME) {
                artist = graph.getActor("artist", name);
                if (artist == null) {
                    Properties props = new Properties();
                    props.setProperty("ActorClass", "Basic");
                    props.setProperty("ActorType", "artist");
                    props.setProperty("ActorID", name);
                    artist = ActorFactory.newInstance().create(props);
                    graph.add(artist);
                }
                if (crawler != null) {
                    try {
                        if (!seenArtists.contains(artist.getID())) {
                            seenArtists.add(artist.getID());
                            crawler.crawl("http://ws.audioscrobbler.com/1.0/artist/" + URLEncoder.encode(artist.getID(), "UTF-8") + "/toptags.xml", artistParsers);
                        }
                        crawler.crawl("http://ws.audioscrobbler.com/1.0/user/" + URLEncoder.encode(user.getID(), "UTF-8") + "/artisttags.xml?artist=" + URLEncoder.encode(artist.getID(), "UTF-8"), userArtistTagParsers);
                    } catch (MalformedURLException ex) {
                        Logger.getLogger(LastFMUserTopArtists.class.getName()).log(Level.SEVERE, null, ex);
                        ex.printStackTrace();
                    } catch (IOException ex) {
                        Logger.getLogger(LastFMUserTopArtists.class.getName()).log(Level.SEVERE, null, ex);
                        ex.printStackTrace();
                    }
                }
                name = "";
                state = State.ARTIST;
            } else if (state == State.PLAYCOUNT) {
                state = State.ARTIST;
            } else if (state == State.OTHER) {
                state = State.ARTIST;
            }
        } catch (NullPointerException ex) {
            Logger.getLogger(LastFMUserTopArtists.class.getName()).log(Level.SEVERE, "Null Pointer Exception at line " + locator.getLineNumber());
        }
    }

    /**
     * Prevent any artist of the given name from being parsed
     * @param artistName name of artist to block from being downloaded
     */
    public static void blockArtist(String artistName) {
        seenArtists.add(artistName);
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        try {
            if ((localName.contentEquals("topartists")) || (qName.contentEquals("topartists"))) {
                state = State.TOPARTISTS;
                String userName = attributes.getValue("user");
                if (parser != null) {
                    parser.setSubDirectory(userName + File.separator);
                    parser.setFilename("topArtists.xml");
                }
                user = graph.getActor("user", userName);
                if (user == null) {
                    Properties props = new Properties();
                    props.setProperty("ActorClass", "Basic");
                    props.setProperty("ActorType", "user");
                    props.setProperty("ActorID", userName);
                    user = ActorFactory.newInstance().create(props);
                    graph.add(user);
                }
            } else if ((localName.contentEquals("artist")) || (qName.contentEquals("artist"))) {
                state = State.ARTIST;
            } else if ((localName.contentEquals("name")) || (qName.contentEquals("name"))) {
                state = State.NAME;
            } else if ((localName.contentEquals("playcount")) || (qName.contentEquals("playcount"))) {
                state = State.PLAYCOUNT;
            } else {
                state = State.OTHER;
            }
        } catch (NullPointerException ex) {
            Logger.getLogger(LastFMUserTopArtists.class.getName()).log(Level.SEVERE, "Null Pointer Exception at line " + locator.getLineNumber());
        }
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
        LastFMUserTopArtists ret = new LastFMUserTopArtists();
        ret.artistParsers = this.artistParsers;
        ret.graph = this.graph;
        ret.parser = this.parser;
        ret.userArtistTagParsers = this.userArtistTagParsers;
        ret.crawler = this.crawler;
        return ret;
    }

    /**
     * Set the array of parser names to process ArtistTag files as they are downloaded.
     * See crawler.crawl()
     * 
     * @param artistParsers array of parser names
     */
    public void setArtistParsers(String[] artistParsers) {
        this.artistParsers = artistParsers;
    }

    /**
     * Set the array of parser names to process UserArtistTag files as they are downloaded.
     * See crawler.crawl()
     * 
     * @param userArtistTagParsers
     */
    public void setUserArtistTagParsers(String[] userArtistTagParsers) {
        this.userArtistTagParsers = userArtistTagParsers;
    }

    @Override
    public void setCrawler(Crawler crawler) {
        this.crawler = crawler;
    }

    @Override
    public void setDocumentLocator(Locator locator) {

        this.locator = locator;

    }
}
