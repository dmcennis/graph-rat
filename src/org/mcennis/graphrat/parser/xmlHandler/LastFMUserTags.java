/*

 * Created 13/05/2008-16:39:28

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

import java.util.Collection;

import java.util.logging.Level;

import java.util.logging.Logger;

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

 * Parse the UserTags XML file from LastFM.  See audioscrobbler.net for a description of the file

 * format.  Usually obtained from the CrawlLastFM data aquisition module. Contains the

 * set of all tags that this user has given to artists.

 *

 * @author Daniel McEnnis

 */

public class LastFMUserTags extends Handler {



    ToFileParser parser = null;

    Graph graph = null;

    Actor tag = null;

    Actor user = null;

    private Locator locator;



    enum State {



        START, TOPTAGS, TAG, NAME, COUNT, URL

    };

    State state = State.START;

    

    public LastFMUserTags(){

        super();

        properties.get("ParserClass").add("LastFMUserTags");

        properties.get("Name").add("LastFMUserTags");

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

        try {

            if (state == State.NAME) {

                String tagName = new String(ch, start, length);

                tag = graph.getActor("tag", tagName);

                if (tag == null) {

                    tag = ActorFactory.newInstance().create("tag",tagName);

                    graph.add(tag);

                }

            } else if ((user != null)&&(tag != null)&&(state == State.COUNT)) {

                String count = new String(ch, start, length);

                Collection<Link> test = graph.getLink("tag", user, tag);

                if (test.size() > 0) {

                    Logger.getLogger(LastFMUserTags.class.getName()).log(Level.WARNING, "Link between '" + user.getID() + "' and '" + tag.getID() + "' should not exist");

                } else {

                    Link userTag = LinkFactory.newInstance().create("tag");

                    userTag.set(user, Integer.parseInt(count), tag);

                    graph.add(userTag);

                }

            }

        } catch (NullPointerException ex) {

            Logger.getLogger(LastFMUserTags.class.getName()).log(Level.SEVERE, "Null Pointer Exception at line " + locator.getLineNumber());

        }

    }



    @Override

    public void endElement(String uri, String localName, String qName) throws SAXException {

        try {

            if (state == State.TOPTAGS) {

                state = State.START;

            } else if (state == State.TAG) {

                state = State.TOPTAGS;

            } else if (state == State.NAME) {

                state = State.TAG;

            } else if (state == State.COUNT) {

                state = State.TAG;

            } else if (state == State.URL) {

                state = State.TAG;

            }

        } catch (NullPointerException ex) {

            Logger.getLogger(LastFMUserTags.class.getName()).log(Level.SEVERE, "Null Pointer Exception at line " + locator.getLineNumber());

        }

    }



    @Override

    public void startDocument() throws SAXException {

    }



    @Override

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

        try {

            if ((localName.contentEquals("toptags")) || (qName.contentEquals("toptags"))) {

                state = State.TOPTAGS;

                String userName = attributes.getValue("user");

                if (parser != null) {

                    parser.setSubDirectory(userName + File.separator);

                    parser.setFilename("tags.xml");

                }

                user = graph.getActor("user", userName);

                if (user == null) {

                    user = ActorFactory.newInstance().create("user",userName);

                    graph.add(user);

                }

            } else if ((localName.contentEquals("tag")) || (qName.contentEquals("tag"))) {

                state = State.TAG;

            } else if ((localName.contentEquals("name")) || (qName.contentEquals("name"))) {

                state = State.NAME;

            } else if ((localName.contentEquals("url")) || (qName.contentEquals("url"))) {

                state = State.URL;

            }

        } catch (NullPointerException ex) {

            Logger.getLogger(LastFMUserTags.class.getName()).log(Level.SEVERE, "Null Pointer Exception at line " + locator.getLineNumber());

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

        LastFMUserTags ret = new LastFMUserTags();

        ret.graph = this.graph;

        ret.parser = this.parser;

        return ret;

    }



    public void setDocumentLocator(Locator locator) {



        this.locator = locator;



    }

}

