/*

 * RATExecution.java

 *

 * Created on 7 October 2007, 15:26

 *

 * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt)

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



import java.util.logging.Level;

import java.util.logging.Logger;

import org.mcennis.graphrat.dataAquisition.DataAquisitionFactory;

import org.mcennis.graphrat.graph.GraphFactory;

import org.mcennis.graphrat.algorithm.Algorithm;

import org.mcennis.graphrat.algorithm.AlgorithmFactory;

import org.mcennis.graphrat.parser.ParsedObject;
import org.dynamicfactory.descriptors.*;
import org.dynamicfactory.property.Property;
import org.dynamicfactory.property.PropertyFactory;

import org.mcennis.graphrat.scheduler.Scheduler;

import org.mcennis.graphrat.scheduler.SchedulerFactory;

import org.mcennis.graphrat.query.graph.xml.GraphQueryXMLFactory;

import org.dynamicfactory.propertyQuery.Query;

import org.dynamicfactory.property.PropertyXMLFactory;

import org.dynamicfactory.property.xml.PropertyXML;

import org.mcennis.graphrat.query.GraphQuery;

import org.mcennis.graphrat.query.GraphQueryXML;

import org.xml.sax.Attributes;

import org.xml.sax.SAXException;



/**

 * Class for parsing a scheduler XML configuration file.  The file is in three parts.

 * The first describes the graph representation, the second describes the data 

 * aquisition modules, and the final section describes the algorithms used.

 *

 * @author Daniel McEnnis

 * 

 */

public class RATExecution extends Handler{

    

    Scheduler scheduler = null;

    

    String className = "";

    

    String propertyName = "";

    

    Properties props = null;

    

    Property entry = null;

    

    State now = State.START;

    

    State type = State.START;

    

    State innerType = State.START;

    

    java.util.Vector<Algorithm> algs = new java.util.Vector<Algorithm>();

    

    GraphQueryXML queryXML = null;

    

    GraphQuery query = null;

    

    PropertyXML valueXML = null;

    

    enum State {CONFIG, START, SCHEDULER, PROPERTY, NAME, VALUE, CLASS, QUERY, GRAPH, DATA, ALGORITHM};

    

    /** Creates a new instance of RATExecution */

    public RATExecution() {

        super();

        properties.get("ParserClass").add("BasicScheduler");

        properties.get("Name").add("BasicScheduler");

    }

    

    @Override

    public ParsedObject get() {

        return scheduler;

    }

    

    @Override

    public void set(ParsedObject o) {

        scheduler = (Scheduler)o;

    }

    

    @Override

    public Handler duplicate() {

        RATExecution ret = new RATExecution();

        ret.set(scheduler);

        return ret;

    }

    

    @Override

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

        if(queryXML != null){

            queryXML.startElement(uri, localName, qName, attributes);

        }else if(valueXML !=null){

            valueXML.startElement(uri,localName,qName,attributes);

        }else if(localName.contentEquals("Config")||qName.contentEquals("Config")){

            now = State.CONFIG;

            innerType = State.CONFIG;

            type = State.CONFIG;

        }else if(localName.contentEquals("Scheduler")||qName.contentEquals("Scheduler")){

             props = SchedulerFactory.newInstance().getParameter();

           now = State.SCHEDULER;

            type = State.SCHEDULER;

            innerType = State.SCHEDULER;

        }else if(localName.contentEquals("class")||qName.contentEquals("class")){

            now = State.CLASS;

        }else if(localName.contentEquals("property")||qName.contentEquals("property")){

            valueXML = PropertyXMLFactory.newInstance().create(attributes.getValue("Class"));

            valueXML.startElement(uri, localName, qName, attributes);

        }else if(localName.contentEquals("name")||qName.contentEquals("name")){

            now = State.NAME;

        }else if(localName.contentEquals("value")||qName.contentEquals("value")){

            now = State.VALUE;

        }else if(localName.contentEquals("Graph")||qName.contentEquals("Graph")){

            props = GraphFactory.newInstance().getParameter().duplicate();

            type = State.GRAPH;

            now = State.GRAPH;

            innerType = State.GRAPH;

        }else if(localName.contentEquals("DataAquisition")||qName.contentEquals("DataAquisition")){

            props = DataAquisitionFactory.newInstance().getParameter().duplicate();

            type = State.DATA;

            now = State.DATA;

            innerType = State.DATA;

        }else if(localName.contentEquals("Algorithm")||qName.contentEquals("Algorithm")){

            props = AlgorithmFactory.newInstance().getParameter().duplicate();

            type = State.ALGORITHM;

            now = State.ALGORITHM;

            innerType = State.ALGORITHM;

        }else if(localName.equalsIgnoreCase("Query")||qName.equalsIgnoreCase("Query")){

            type = State.ALGORITHM;

            innerType = State.ALGORITHM;

            now = State.QUERY;

        }else if(now == State.QUERY){

            String name = localName;

            if((localName ==null) || (localName.equals(""))){

                name = qName;

            }

            queryXML = GraphQueryXMLFactory.newInstance().create(name);

            queryXML.startElement(uri, localName, qName, attributes);

        }

    }

    

    @Override

    public void characters(char[] ch, int start, int length) throws SAXException {

        if (queryXML != null){

            queryXML.characters(ch,start,length);

        }else if (valueXML != null){

            valueXML.characters(ch,start,length);

        }else if(now == State.CLASS){

            className = new String(ch,start,length);

            if(innerType == State.PROPERTY){

                try{

                entry = PropertyFactory.newInstance().create("BasicProperty",propertyName,Class.forName(className));

                }catch(ClassNotFoundException ex){

                    Logger.getLogger(RATExecution.class.getName()).log(Level.SEVERE, null, ex);                    

                }

            }else if(innerType == State.ALGORITHM){

                props.add("AlgorithmClass",className);

            }else if(innerType == State.SCHEDULER){

                props.add("SchedulerClass",className);

            }else if(innerType == State.GRAPH){

                props.add("GraphClass",className);

            }else if(innerType == State.DATA){

                props.add("DataAquisitionClass",className);

            }

        } else if(now == State.NAME){

            propertyName = new String(ch,start,length);

        }

    }



    @Override

    public void endElement(String uri, String localName, String qName) throws SAXException {

        if(queryXML != null){

            queryXML.endElement(uri,localName,qName);

            if(queryXML.buildingStatus() == Query.State.READY){

                query = queryXML.getQuery();

                queryXML = null;

                now = type;

            }

        } else if(valueXML != null){

            valueXML.endElement(uri,localName,qName);

            if(valueXML.buildingStatus() == Query.State.READY){

                Property property = valueXML.getProperty();

                props.set(property);

                valueXML = null;

            }

        } else if(("Scheduler".compareToIgnoreCase(localName)==0)||("Scheduler".compareToIgnoreCase(qName)==0)){

            now = State.CONFIG;

            type = State.CONFIG;

            scheduler = SchedulerFactory.newInstance().create(props);

            props = null;

        }else if(("Config".compareToIgnoreCase(localName)==0)||("Config".compareToIgnoreCase(qName)==0)){

            now = State.START;

            type = State.START;

            props = null;

        }else if(("Graph".compareToIgnoreCase(localName)==0)||("Graph".compareToIgnoreCase(qName)==0)){

            now = State.CONFIG;

            type = State.CONFIG;

            scheduler.set(GraphFactory.newInstance().create(props));

        }else if(("DataAquisition".compareToIgnoreCase(localName)==0)||("DataAquisition".compareToIgnoreCase(qName)==0)){

            now = State.CONFIG;

            type = State.CONFIG;

            scheduler.load(DataAquisitionFactory.newInstance().create(props));

        }else if(("Algorithm".compareToIgnoreCase(localName)==0)||("Algorithm".compareToIgnoreCase(qName)==0)){

            now = State.CONFIG;

            type = State.CONFIG;

            Algorithm alg = AlgorithmFactory.newInstance().create(props);

            scheduler.load(alg, query);

        }else if(("Property".compareToIgnoreCase(localName)==0)||("Property".compareToIgnoreCase(qName)==0)){

            innerType = type;

            now = type;

            props.set(entry);

        }else if(("Name".compareToIgnoreCase(localName)==0)||("Name".compareToIgnoreCase(qName)==0)){

            now = State.PROPERTY;

        }else if(("Value".compareToIgnoreCase(localName)==0)||("Value".compareToIgnoreCase(qName)==0)){

            now = State.PROPERTY;

        }else if(("Class".compareToIgnoreCase(localName)==0)||("Class".compareToIgnoreCase(qName)==0)){

            now = innerType;

        }else if(("Query".compareToIgnoreCase(localName)==0)||("Query".compareToIgnoreCase(qName)==0)){

            now = State.ALGORITHM;

        }

    }





}

