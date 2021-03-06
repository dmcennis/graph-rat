

/*

 * Created 13/05/2008-16:54:27

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

import java.util.logging.Level;

import java.util.logging.Logger;

import org.mcennis.graphrat.graph.Graph;

import org.mcennis.graphrat.actor.Actor;

import org.mcennis.graphrat.actor.ActorFactory;

import org.mcennis.graphrat.link.Link;
import org.dynamicfactory.property.Property;
import org.dynamicfactory.property.PropertyFactory;
import org.dynamicfactory.property.InvalidObjectTypeException;

import org.mcennis.graphrat.link.LinkFactory;

import org.mcennis.graphrat.parser.ParsedObject;

import org.mcennis.graphrat.parser.ToFileParser;

import org.xml.sax.Attributes;

import org.xml.sax.Locator;

import org.xml.sax.SAXException;



/**

 * Parse the UserArtist XML file from LastFM.  See audioscrobbler.net for a description of the file

 * format.  Usually obtained from the CrawlLastFM data aquisition module. Provides the

 * set of all tags that have been applied to this artist by this user.

 *

 * @author Daniel McEnnis

 */

public class LastFMUserArtistTags extends Handler {



    Graph graph = null;

    ToFileParser parser = null;

    Actor user = null;

    Actor artist = null;

    Link userArtistLink = null;

    Property userArtistTag = null;

    private Locator locator;



    enum State { START, ARTISTTAGS, TAG, NAME, COUNT, URL};



    State state = State.START;

    

    public LastFMUserArtistTags(){

        super();

        properties.get("ParserClass").add("LastFMUserArtistTags");

        properties.get("Name").add("LastFMUserArtistTags");

    }



    /**

     * Set where the given parser will store files when they are parsed

     * @param parser ToFileParser to store files parsed by this parser

     */

    public void setParser(ToFileParser parser) {

        this.parser = parser;

    }



    @Override

    public void characters(char[] ch, int start, int length) throws SAXException {

try{        if(state == State.NAME){

            String tagName = new String(ch,start,length);

            if(userArtistLink != null) {

                if (userArtistLink.getProperty("Tags") == null) {

                    try {

                        userArtistTag = PropertyFactory.newInstance().create("BasicProperty","Tags",String.class);

                        userArtistTag.add(tagName);

                        userArtistLink.add(userArtistTag);

                        userArtistLink.set(userArtistLink.getStrength() + 1.0);

                    } catch (InvalidObjectTypeException ex) {

                        Logger.getLogger(LastFMUserArtistTags.class.getName()).log(Level.SEVERE, null, ex);

                    }

                } else {

                    try {

                        userArtistLink.getProperty("Tags").add(tagName);

                        userArtistLink.set(userArtistLink.getStrength() + 1.0);

                    } catch (InvalidObjectTypeException ex) {

                        Logger.getLogger(LastFMUserArtistTags.class.getName()).log(Level.SEVERE, null, ex);

                    }

                }

            }

        }

        } catch (NullPointerException ex) {

            Logger.getLogger(LastFMUserArtistTags.class.getName()).log(Level.SEVERE, "Null Pointer Exception at line " + locator.getLineNumber());

        }

    }



    @Override

    public void endElement(String uri, String localName, String qName) throws SAXException {

try{        if (state == State.ARTISTTAGS) {

            state = State.START;

        } else if (state == State.TAG) {

            state = State.ARTISTTAGS;

        } else if (state == State.NAME) {

            state = State.TAG;

        } else if (state == State.COUNT) {

            state = State.TAG;

        } else if (state == State.URL) {

            state = State.TAG;

        }

        } catch (NullPointerException ex) {

            Logger.getLogger(LastFMUserArtistTags.class.getName()).log(Level.SEVERE, "Null Pointer Exception at line " + locator.getLineNumber());

        }

    }



    @Override

    public void startDocument() throws SAXException {

        super.startDocument();

    }



    @Override

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

try{        if ((localName.contentEquals("artisttags") || (qName.contentEquals("artisttags")))) {

            String username = attributes.getValue("user");

            String artistName = attributes.getValue("artist");

            if (parser != null) {

                parser.setSubDirectory(username + File.separator);

                parser.setFilename(artistName + ".xml");

            }



            user = graph.getActor("user", username);

            if (user == null) {

                user = ActorFactory.newInstance().create("user",username);

                graph.add(user);

            }



            artist = graph.getActor("artist", artistName);

            if (artist == null) {

                artist = ActorFactory.newInstance().create("artist",artistName);

                graph.add(artist);

            }



            state = State.ARTISTTAGS;

        } else if ((localName.contentEquals("tag") || (qName.contentEquals("tag")))) {

            if ((user != null)&&(artist != null)&&(userArtistLink == null)) {

                userArtistLink = LinkFactory.newInstance().create("UserArtistTag");

                userArtistLink.set(user, 0.0, artist);

                graph.add(userArtistLink);

            }

            state = State.TAG;

        } else if ((localName.contentEquals("count") || (qName.contentEquals("count")))) {

            state = State.COUNT;

        } else if ((localName.contentEquals("url") || (qName.contentEquals("url")))) {

            state = State.URL;

        }

        } catch (NullPointerException ex) {

            Logger.getLogger(LastFMUserArtistTags.class.getName()).log(Level.SEVERE, "Null Pointer Exception at line " + locator.getLineNumber());

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

        LastFMUserArtistTags ret = new LastFMUserArtistTags();

        ret.graph = graph;

        ret.parser = parser;

        return ret;

    }



    @Override

    public void setDocumentLocator(Locator locator) {



        this.locator = locator;



    }

}

