/*
 * AggregatorXML - Created 2/02/2009 - 5:45:00 PM
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
