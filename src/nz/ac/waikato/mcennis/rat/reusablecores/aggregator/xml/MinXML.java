/*
 * MinXML - created 2/02/2009 - 5:46:51 PM
 * Copyright Daniel McEnnis, see license.txt
 */

package nz.ac.waikato.mcennis.rat.reusablecores.aggregator.xml;

import java.io.IOException;
import java.io.Writer;
import nz.ac.waikato.mcennis.rat.reusablecores.aggregator.AggregatorFunction;
import nz.ac.waikato.mcennis.rat.reusablecores.aggregator.MinAggregator;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.dynamicfactory.propertyQuery.Query.State;

/**
 *
 * @author Daniel McEnnis
 */
public class MinXML implements AggregatorXML{
    MinAggregator min = new MinAggregator();

    State state = State.UNINITIALIZED;

    public AggregatorFunction getAggregator() {
        return min;
    }

    public void export(Writer writer) throws IOException {
        writer.append("<Min/>\n");
    }

    public MinXML newCopy() {
        return new MinXML();
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
