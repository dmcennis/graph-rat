/*
 * SumXML - created 2/02/2009 - 5:48:16 PM
 * Copyright Daniel McEnnis, see license.txt
 */

package org.mcennis.graphrat.reusablecores.aggregator.xml;

import java.io.IOException;
import java.io.Writer;
import org.dynamicfactory.propertyQuery.Query.State;
import org.mcennis.graphrat.reusablecores.aggregator.AggregatorFunction;
import org.mcennis.graphrat.reusablecores.aggregator.SumAggregator;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author Daniel McEnnis
 */
public class SumXML implements AggregatorXML{

    SumAggregator sum = new SumAggregator();

    transient State state = State.UNINITIALIZED;

    public AggregatorFunction getAggregator() {
        return sum;
    }

    public void export(Writer writer) throws IOException {
        writer.append("<Sum/>\n");
    }

    public SumXML newCopy() {
        return new SumXML();
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        state=State.LOADING;
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
