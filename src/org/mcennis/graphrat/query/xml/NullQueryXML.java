/**
 * NullQueryXML
 * Created Jan 12, 2009 - 10:21:30 PM
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
package org.mcennis.graphrat.query.xml;

import org.mcennis.graphrat.query.NullQuery;
import org.dynamicfactory.propertyQuery.Query.State;
import org.mcennis.graphrat.query.actor.NullActorQuery;
import org.mcennis.graphrat.query.graph.NullGraphQuery;
import org.mcennis.graphrat.query.link.NullLinkQuery;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author Daniel McEnnis
 */
public class NullQueryXML{

    NullQuery nullQuery = null;
    
    String type = "Graph";
    
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        type = attributes.getValue("Class");
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
       
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        if(type.contentEquals("Actor")){
            ((NullActorQuery)(nullQuery = new NullActorQuery())).buildQuery();
        }else if(type.contentEquals("Graph")){
            ((NullGraphQuery)(nullQuery = new NullGraphQuery())).buildQuery();
        }else{
            ((NullLinkQuery)(nullQuery = new NullLinkQuery())).buildQuery();
        }
    }

    public State buildingStatus() {
        return nullQuery.buildingStatus();
    }
    
    public NullQuery getQuery(){
        return nullQuery;
    }

    public NullQueryXML newCopy() {
        return new NullQueryXML();
    }
}
