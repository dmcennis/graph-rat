/*
 * FirstItemXML - created 2/02/2009 - 5:46:17 PM
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

package org.mcennis.graphrat.reusablecores.aggregator.xml;

import java.io.IOException;
import java.io.Writer;

import org.dynamicfactory.propertyQuery.Query.State;
import org.mcennis.graphrat.reusablecores.aggregator.AggregatorFunction;
import org.mcennis.graphrat.reusablecores.aggregator.FirstItemAggregator;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author Daniel McEnnis
 */
public class FirstItemXML implements  AggregatorXML{
    FirstItemAggregator firstItem = new FirstItemAggregator();

    State state = State.UNINITIALIZED;

    public AggregatorFunction getAggregator() {
        return firstItem;
    }

    public void export(Writer writer) throws IOException {
        writer.append("<FirstItem/>\n");
    }

    public FirstItemXML newCopy() {
        return new FirstItemXML();
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        state = State.LOADING;
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        ;
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        state = State.READY;
    }

    public State buildingStatus() {
        return state;
    }

}
