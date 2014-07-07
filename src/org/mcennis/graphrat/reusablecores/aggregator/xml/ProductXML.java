/*
 * ProductXML - created 2/02/2009 - 5:47:08 PM
 * Copyright Daniel McEnnis, see license.txt
 */

package org.mcennis.graphrat.reusablecores.aggregator.xml;

import java.io.IOException;
import java.io.Writer;
import org.dynamicfactory.propertyQuery.Query.State;
import org.mcennis.graphrat.reusablecores.aggregator.AggregatorFunction;
import org.mcennis.graphrat.reusablecores.aggregator.ProductAggregator;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author Daniel McEnnis
 */
public class ProductXML implements AggregatorXML{

    ProductAggregator product = new ProductAggregator();

    State state = State.UNINITIALIZED;

    public AggregatorFunction getAggregator() {
        return product;
    }

    public void export(Writer writer) throws IOException {
        writer.append("<Product/>\n");
    }

    public NullXML newCopy() {
        return new NullXML();
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
