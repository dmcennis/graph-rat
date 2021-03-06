/*

 * ParseBibliographyXML.java

 *

 * Created on 8/01/2008, 14:02:23

 * Coptright Daniel McEnnis, see license.txt

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



import java.util.HashMap;

import java.util.TreeSet;

import java.util.Iterator;

import java.util.Properties;

import java.util.logging.Level;

import java.util.logging.Logger;

import org.mcennis.graphrat.graph.Graph;

import org.mcennis.graphrat.graph.GraphFactory;
import org.mcennis.graphrat.actor.Actor;

import org.mcennis.graphrat.parser.ParsedObject;

import org.dynamicfactory.property.InvalidObjectTypeException;
import org.dynamicfactory.property.Property;
import org.dynamicfactory.property.PropertyFactory;
import org.xml.sax.Attributes;

import org.xml.sax.SAXException;



/**

 * Parser for reading ground truth XML files associated with bibliographic XML files

 * by paperID attribute.<br/>

 * <br/>

 * DTD is as follows:<br/>

 * <pre>

 * &lt;!DOCTYPE corpus [

 * &lt;!ELEMENT corpus (paper)&gt;

 * &lt;!ELEMENT paper (paperID,keyword)&gt;

 * &lt;!ELEMENT paperID (#PCDATA)&gt;

 * &lt;!ELEMENT keyword (#PCDATA)&gt;

 * ]&gt;

 * </pre>

 * @author Daniel McEnnis

 */

public class ParseClassLabel extends Handler {



    Graph graph = null;



    enum State {



        START, CORPUS, PAPER, PAPER_ID, KEYWORD

    }



    

    

       ;

    State state = State.START  ;

     

    

    Actor paper = null;

    HashMap<String, TreeSet<Actor>> subgraph = new HashMap<String, TreeSet<Actor>>();



    public ParseClassLabel() {

    }



    public ParsedObject get() {

        return graph;

    }



    public void set(ParsedObject o) {

        graph = (Graph) o;

    }



    public Handler duplicate() {

        ParseClassLabel ret = new ParseClassLabel();

        ret.graph = graph;

        return ret;

    }



    @Override

    public void startDocument() throws SAXException {

        state = State.START;

    }



    @Override

    public void endDocument() throws SAXException {

        Properties props = new Properties();

        props.setProperty("GraphClass", "MemGraph");

        Iterator<String> it = subgraph.keySet().iterator();

        while (it.hasNext()) {

            try {

                String cluster = it.next();

                props.setProperty("Graph", "MemGraph");

                props.setProperty("GraphID", cluster);

                Graph child = GraphFactory.newInstance().create(cluster);

                graph.addChild(child);

            } catch (Exception ex) {

                Logger.getLogger(ParseClassLabel.class.getName()).log(Level.SEVERE, null, ex);

            }

        }

    }



    @Override

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

        if ((localName.equalsIgnoreCase("corpus")) || (qName.equalsIgnoreCase("corpus"))) {

            state = State.CORPUS;

        } else if ((localName.equalsIgnoreCase("paper")) || (qName.equalsIgnoreCase("paper"))) {

            state = State.PAPER;

        } else if ((localName.equalsIgnoreCase("paperID")) || (qName.equalsIgnoreCase("paperID"))) {

            state = State.PAPER_ID;

        } else if ((localName.equalsIgnoreCase("keyword")) || (qName.equalsIgnoreCase("keyword"))) {

            state = State.KEYWORD;

        }

    }



    @Override

    public void endElement(String uri, String localName, String qName) throws SAXException {

        if ((localName.equalsIgnoreCase("corpus")) || (qName.equalsIgnoreCase("corpus"))) {

            state = State.START;

        } else if ((localName.equalsIgnoreCase("paper")) || (qName.equalsIgnoreCase("paper"))) {

            state = State.CORPUS;

        } else if ((localName.equalsIgnoreCase("paperID")) || (qName.equalsIgnoreCase("paperID"))) {

            state = State.PAPER;

        } else if ((localName.equalsIgnoreCase("keyword")) || (qName.equalsIgnoreCase("keyword"))) {

            state = State.PAPER;

        }

    }



    @Override

    public void characters(char[] ch, int start, int length) throws SAXException {

        String str = new String(ch, start, length);

        if (state == State.PAPER_ID) {

            paper = graph.getActor("Paper", str);

            if (paper == null) {

                Logger.getLogger(ParseClassLabel.class.getName()).log(Level.SEVERE,"Paper '" + str + "' not found");

            }

        } else if (state == State.KEYWORD) {

            if (paper != null) {

                Property property = PropertyFactory.newInstance().create("BasicProperty","cluster",String.class);

                try {

                    property.add(str);

                } catch (InvalidObjectTypeException ex) {

                    Logger.getLogger("global").log(Level.SEVERE, "Property class doesn't match java.lang.String", ex);

                }

                paper.add(property);



                if (!subgraph.containsKey(str)) {

                    subgraph.put(str, new TreeSet<Actor>());

                }

                subgraph.get(str).add(paper);

            } else {

            //

            }

        }

    }

}

