/*

 * Created 5-5-08

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

import java.net.MalformedURLException;

import java.net.URL;

import java.util.logging.Level;

import java.util.logging.Logger;

import org.mcennis.graphrat.graph.Graph;

import org.mcennis.graphrat.actor.Actor;

import org.mcennis.graphrat.actor.ActorFactory;

import org.mcennis.graphrat.link.Link;

import org.mcennis.graphrat.link.LinkFactory;

import org.mcennis.graphrat.parser.ParsedObject;

import org.mcennis.graphrat.parser.ToFileParser;

import org.dynamicfactory.property.InvalidObjectTypeException;
import org.dynamicfactory.property.Property;
import org.dynamicfactory.property.PropertyFactory;
import org.xml.sax.Attributes;

import org.xml.sax.Locator;

import org.xml.sax.SAXException;



/**

 * Parse the ArtistTag XML.  See www.audioscrobbler.net's web services page for a

 * description of the XML format.  Contains the tags and their frequency used to describe

 * this artist across all LastFM users.

 * 

 * @author Daniel McEnnis

 */

public class LastFMArtistTag extends Handler {



    public LastFMArtistTag(){

        super();

        properties.get("ParserClass").add("LastFMArtistTag");

        properties.get("Name").add("LastFMArtistTag"); 

    }

    

    Graph graph = null;

    private Locator locator;



    enum State {



        START, TOP_TAGS, TAG, NAME, COUNT, URL

    };

    State state = State.START;

    Actor artist;

    Actor tag;

    boolean ignore = false;

    ToFileParser parser = null;



    /**

     * Reference to a ToFileParser to control where this XML file is stored. 

     * @param parser reference to a ToFileParser

     */

    public void setParser(ToFileParser parser) {

        this.parser = parser;

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

        LastFMArtistTag ret = new LastFMArtistTag();

        ret.graph = graph;

        return ret;

    }



    @Override

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

        try {

            if ((localName.contentEquals("toptags")) || (qName.contentEquals("toptags"))) {

                state = State.TOP_TAGS;

                String artistName = attributes.getValue("artist");

                if (parser != null) {

                    parser.setSubDirectory("ArtistDirectory" + File.separator);

                    parser.setFilename(artistName + ".xml");

                }

                if ((artist = graph.getActor("artist", artistName)) == null) {

                    artist = ActorFactory.newInstance().create("artist",artistName);

                    graph.add(artist);



                }

            } else if ((localName.contentEquals("tag")) || (qName.contentEquals("tag"))) {

                state = State.TAG;

            } else if ((localName.contentEquals("name")) || (qName.contentEquals("name"))) {

                state = State.NAME;

            } else if ((localName.contentEquals("count")) || (qName.contentEquals("count"))) {

                state = State.COUNT;

            } else if ((localName.contentEquals("url")) || (qName.contentEquals("url"))) {

                state = State.URL;

            }

        } catch (NullPointerException ex) {

            Logger.getLogger(LastFMArtistTag.class.getName()).log(Level.SEVERE, "Null Pointer Exception at line " + locator.getLineNumber());

        }

    }



    @Override

    public void characters(char[] ch, int start, int length) throws SAXException {

        try {

            String content = new String(ch, start, length);

            if (state == State.NAME) {

                if ((tag = graph.getActor("tag", content)) == null) {

                    tag = ActorFactory.newInstance().create("tag",content);

                    graph.add(tag);

                }

            } else if (state == State.COUNT) {

                if ((tag != null)&&(artist != null)&&(graph.getLink("ArtistTags", tag, artist) == null)) {

                    Link tagLink = LinkFactory.newInstance().create("ArtistTag");

                    tagLink.set(artist, Integer.parseInt(content), tag);

                    graph.add(tagLink);

                } else {

                    Logger.getLogger(LastFMArtistTag.class.getName()).log(Level.WARNING, "Tag '" + content + "' already connects to '" + artist.getID() + "'");

                }

            } else if (state == State.URL) {

                if ((tag != null) && (tag.getProperty("URL") == null)) {

                    try {

                        Property property = PropertyFactory.newInstance().create("BasicProperty","URL",URL.class);

                        property.add(new URL(content));

                        tag.add(property);

                    } catch (MalformedURLException ex) {

                        Logger.getLogger(LastFMArtistTag.class.getName()).log(Level.SEVERE, null, ex);

                    } catch (InvalidObjectTypeException ex) {

                        Logger.getLogger(LastFMArtistTag.class.getName()).log(Level.SEVERE, null, ex);

                    }

                }

            }

        } catch (NullPointerException ex) {

            Logger.getLogger(LastFMArtistTag.class.getName()).log(Level.SEVERE, "Null Pointer Exception at line " + locator.getLineNumber());

        }

    }



    @Override

    public void endElement(String uri, String localName, String qName) throws SAXException {

        try {

            if (state == State.TOP_TAGS) {

                state = State.START;

            } else if (state == State.TAG) {

                state = State.TOP_TAGS;

            } else if (state == State.NAME) {

                state = State.TAG;

            } else if (state == State.COUNT) {

                state = State.TAG;

            } else if (state == State.URL) {

                state = State.TAG;

            }

        } catch (NullPointerException ex) {

            Logger.getLogger(LastFMArtistTag.class.getName()).log(Level.SEVERE, "Null Pointer Exception at line " + locator.getLineNumber());

        }

    }



    @Override

    public void setDocumentLocator(Locator locator) {



        this.locator = locator;



    }



    @Override

    public void startDocument() throws SAXException {

    }



    @Override

    public void endDocument() throws SAXException {

    }

}

