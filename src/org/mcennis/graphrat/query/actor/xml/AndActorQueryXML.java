/*
 * AndActorQueryXML - created 1/02/2009 - 2:24:24 AM
 * Copyright Daniel McEnnis, see license.txt
 */

package org.mcennis.graphrat.query.actor.xml;
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

import org.mcennis.graphrat.query.ActorQuery;
import org.mcennis.graphrat.query.ActorQueryXML;
import org.mcennis.graphrat.query.actor.AndActorQuery;
import org.mcennis.graphrat.query.xml.AndQueryXML;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.dynamicfactory.propertyQuery.Query.State;

/**
 *
 * @author Daniel McEnnis
 */
public class AndActorQueryXML implements ActorQueryXML{
    AndQueryXML core = new AndQueryXML();

    public ActorQuery getQuery() {
        return (AndActorQuery)(core.getQuery());
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

    public AndActorQueryXML newCopy() {
        return new AndActorQueryXML();
    }
}
