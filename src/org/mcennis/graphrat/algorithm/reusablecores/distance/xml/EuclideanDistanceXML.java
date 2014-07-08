/**
 * EuclideanDistanceXML
 * Created Jan 17, 2009 - 8:37:55 PM
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
package org.mcennis.graphrat.algorithm.reusablecores.distance.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import java.io.Writer;
import java.io.IOException;
import org.mcennis.graphrat.algorithm.reusablecores.distance.EuclideanDistance;
import org.dynamicfactory.propertyQuery.Query.State;

/**
 *
 * @author Daniel McEnnis
 */
public class EuclideanDistanceXML implements DistanceXML{
    EuclideanDistance distance = null;
    
    State state = State.UNINITIALIZED;
    
    public EuclideanDistance getDistanceMeasure() {
        return distance;
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        state = State.LOADING;
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        distance = new EuclideanDistance();
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        state = State.READY;
    }

    public State buildingStatus() {
        return state;
    }

    public EuclideanDistanceXML newCopy() {
        return new EuclideanDistanceXML();
    }

    public void exportAsXML(Writer writer) throws IOException{
        writer.append("<EuclideanDistance/>\n");
    }
}
