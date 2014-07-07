/**
 * DistanceXML
 * Created Jan 17, 2009 - 8:34:18 PM
 * Copyright Daniel McEnnis, see license.txt
 */
package org.mcennis.graphrat.reusablecores.distance.xml;

import org.mcennis.graphrat.reusablecores.distance.DistanceFunction;
import org.dynamicfactory.propertyQuery.XMLParserObject;
import java.io.Writer;
import java.io.IOException;

/**
 *
 * @author Daniel McEnnis
 */
public interface DistanceXML extends XMLParserObject{

    public DistanceFunction getDistanceMeasure();
    
    public void exportAsXML(Writer writer) throws IOException;
}
