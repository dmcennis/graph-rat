/*
 * MaxXML - created 2/02/2009 - 5:46:28 PM
 * Copyright Daniel McEnnis, see license.txt
 */

package org.mcennis.graphrat.reusablecores.aggregator.xml;

import java.io.IOException;
import java.io.Writer;
import org.mcennis.graphrat.reusablecores.aggregator.AggregatorFunction;
import org.mcennis.graphrat.reusablecores.aggregator.MaxAggregator;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.dynamicfactory.propertyQuery.Query.State;

/**
 *
 * @author Daniel McEnnis
 */
public class MaxXML implements AggregatorXML {
    MaxAggregator max = new MaxAggregator();

    State state = State.UNINITIALIZED;

    public AggregatorFunction getAggregator() {
        return max;
    }

    public void export(Writer writer) throws IOException {
        writer.append("<Max/>\n");
    }

    public MaxXML newCopy() {
        return new MaxXML();
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
