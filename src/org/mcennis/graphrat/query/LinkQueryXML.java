/*
 * LinkQueryXML - Created 31/01/2009 - 11:10:53 PM
 * Copyright Daniel McEnnis, see license.txt
 */

package org.mcennis.graphrat.query;

import org.dynamicfactory.propertyQuery.XMLParserObject;

/**
 *
 * @author Daniel McEnnis
 */
public interface LinkQueryXML extends XMLParserObject{
    public LinkQuery getQuery();
}
