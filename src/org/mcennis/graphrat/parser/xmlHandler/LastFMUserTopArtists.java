/*

 * Created 13/05/2008-16:53:29

 * Copyright Daniel McEnnis, see license.txt

 */

/*
 *   This file is part of GraphRAT.
 *
 *   GraphRAT is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   GraphRAT is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with GraphRAT.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.mcennis.graphrat.parser.xmlHandler;



import java.io.File;

import java.io.IOException;

import java.net.MalformedURLException;

import java.net.URLEncoder;

import java.util.HashSet;

import java.util.Collection;

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

    boolean parseArtist = true;

    boolean parseUserArtist = true;



    enum State {



        START, TOPARTISTS, ARTIST, NAME, PLAYCOUNT, OTHER

    };

    State state = State.START;

    

    public LastFMUserTopArtists(){

        super();

        properties.get("ParserClass").add("LastFMUserTopArtists");

        properties.get("Name").add("LastFMUserTopArtists");

    }



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

                    Collection<Link> test = graph.getLink("listensTo", user, artist);

                    if (test.size()==0) {

                        Link userArtist = LinkFactory.newInstance().create("ListensTo");

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

                    artist = ActorFactory.newInstance().create("artist",name);

                    graph.add(artist);

                }

                if (crawler != null) {

                    try {

                        if (!seenArtists.contains(artist.getID()) && parseArtist) {

                            seenArtists.add(artist.getID());

                            crawler.crawl("http://ws.audioscrobbler.com/1.0/artist/" + URLEncoder.encode(artist.getID(), "UTF-8") + "/toptags.xml", artistParsers);

                        }

                        if(parseUserArtist){

                            crawler.crawl("http://ws.audioscrobbler.com/1.0/user/" + URLEncoder.encode(user.getID(), "UTF-8") + "/artisttags.xml?artist=" + URLEncoder.encode(artist.getID(), "UTF-8"), userArtistTagParsers);

                        }

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



    public boolean isParseArtist() {

        return parseArtist;

    }



    public void setParseArtist(boolean parseArtist) {

        this.parseArtist = parseArtist;

    }



    public boolean isParseUserArtist() {

        return parseUserArtist;

    }



    public void setParseUserArtist(boolean parseUserArtist) {

        this.parseUserArtist = parseUserArtist;

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

                    user = ActorFactory.newInstance().create("user",userName);

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

