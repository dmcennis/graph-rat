/*
 * AndActorQueryXML - created 1/02/2009 - 2:24:24 AM
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
package org.mcennis.graphrat.query.link.xml;

import org.mcennis.graphrat.query.LinkQuery;
import org.mcennis.graphrat.query.LinkQueryXML;
import org.mcennis.graphrat.query.link.XorLinkQuery;
import org.mcennis.graphrat.query.xml.XorQueryXML;
import org.dynamicfactory.propertyQuery.Query.State;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author Daniel McEnnis
 */
public class XorLinkQueryXML implements LinkQueryXML{
    XorQueryXML core = new XorQueryXML();

    public LinkQuery getQuery() {
        return (XorLinkQuery)(core.getQuery());
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        core.startElement(uri, localName, qName, attributes);
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        core.characters(ch, start, length);
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        core.endElement(uri, localName, qName);
    }

    public State buildingStatus() {
        return core.buildingStatus();
    }

    public XorLinkQueryXML newCopy() {
        return new XorLinkQueryXML();
    }
}
