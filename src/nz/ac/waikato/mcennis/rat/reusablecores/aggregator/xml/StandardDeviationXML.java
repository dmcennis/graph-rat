/*
 * StandardDeviationXML - created 2/02/2009 - 5:47:22 PM
 * Copyright Daniel McEnnis, see license.txt
 */

package nz.ac.waikato.mcennis.rat.reusablecores.aggregator.xml;

import java.io.IOException;
import java.io.Writer;
import nz.ac.waikato.mcennis.rat.XMLParserObject.State;
import nz.ac.waikato.mcennis.rat.reusablecores.aggregator.AggregatorFunction;
import nz.ac.waikato.mcennis.rat.reusablecores.aggregator.StandardDeviationAggregator;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author Daniel McEnnis
 */
public class StandardDeviationXML implements AggregatorXML{

    StandardDeviationAggregator sd = new StandardDeviationAggregator();

    transient State state = State.UNINITIALIZED;
    public AggregatorFunction getAggregator() {
        return sd;
    }

    public void export(Writer writer) throws IOException {
        writer.append("<StandardDeviation/>\n");
    }

    public StandardDeviationXML newCopy() {
        return new StandardDeviationXML();
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        state = State.LOADING;
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        state = State.READY;
    }

    public State buildingStatus() {
        return state;
    }

}