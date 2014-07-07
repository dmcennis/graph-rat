/**
 * DotProductDistanceXML
 * Created Jan 17, 2009 - 8:37:42 PM
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
package org.mcennis.graphrat.reusablecores.distance.xml;

import java.io.Writer;
import java.io.IOException;

import org.dynamicfactory.propertyQuery.Query.State;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.mcennis.graphrat.reusablecores.distance.DotProductDistance;

/**
 *
 * @author Daniel McEnnis
 */
public class DotProductDistanceXML implements DistanceXML{

    DotProductDistance distance = null;
    
    State state = State.UNINITIALIZED;
    
    public DotProductDistance getDistanceMeasure() {
        return distance;
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        state = State.LOADING;
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        distance = new DotProductDistance();
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        state = State.READY;
    }

    public State buildingStatus() {
        return state;
    }

    public DotProductDistanceXML newCopy() {
        return new DotProductDistanceXML();
    }

    public void exportAsXML(Writer writer) throws IOException{
        writer.append("<DotProductDistance/>\n");
    }
}
