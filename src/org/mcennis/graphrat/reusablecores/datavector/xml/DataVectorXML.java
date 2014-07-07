/**
 * DataVectorXML
 * Created Jan 16, 2009 - 11:20:17 PM
 * Copyright Daniel McEnnis, see license.txt
 */
package org.mcennis.graphrat.reusablecores.datavector.xml;

import org.mcennis.graphrat.reusablecores.datavector.DataVector;
import org.dynamicfactory.propertyQuery.XMLParserObject;

/**
 *
 * @author Daniel McEnnis
 */
public interface DataVectorXML extends XMLParserObject{

    public DataVector getDataVector();

}
