/*
 * FirstItemXML - created 2/02/2009 - 5:46:17 PM
 * Copyright Daniel McEnnis, see license.txt
 */

package nz.ac.waikato.mcennis.rat.reusablecores.aggregator.xml;

import java.io.IOException;
import java.io.Writer;
import org.dynamicfactory.propertyQuery.XMLParserObject;
import org.dynamicfactory.propertyQuery.Query.State;
import nz.ac.waikato.mcennis.rat.reusablecores.aggregator.AggregatorFunction;
import nz.ac.waikato.mcennis.rat.reusablecores.aggregator.FirstItemAggregator;
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
