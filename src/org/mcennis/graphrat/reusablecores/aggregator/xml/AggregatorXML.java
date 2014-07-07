/*
 * AggregatorXML - Created 2/02/2009 - 5:45:00 PM
 * Copyright Daniel McEnnis, see license.txt
 */

package org.mcennis.graphrat.reusablecores.aggregator.xml;

import java.io.IOException;
import java.io.Writer;
import org.dynamicfactory.propertyQuery.XMLParserObject;
import org.mcennis.graphrat.reusablecores.aggregator.AggregatorFunction;

/**
 *
 * @author Daniel McEnnis
 */
public interface AggregatorXML extends XMLParserObject{
    public AggregatorFunction getAggregator();

    public void export(Writer writer) throws IOException;

    public AggregatorXML newCopy();
}
