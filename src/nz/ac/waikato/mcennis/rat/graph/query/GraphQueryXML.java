/*
 * GraphQueryXML - Created 31/01/2009 - 11:10:15 PM
 * Copyright Daniel McEnnis, see license.txt
 */

package nz.ac.waikato.mcennis.rat.graph.query;

import org.dynamicfactory.propertyQuery.XMLParserObject;

/**
 *
 * @author Daniel McEnnis
 */
public interface GraphQueryXML extends XMLParserObject{
    public GraphQuery getQuery();
}
