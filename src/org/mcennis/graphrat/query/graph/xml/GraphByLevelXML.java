/**
 * GraphByLevelXML
 * Created Jan 12, 2009 - 10:28:51 PM
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
package org.mcennis.graphrat.query.graph.xml;

import org.dynamicfactory.propertyQuery.Query.State;
import org.mcennis.graphrat.query.GraphQuery;
import org.mcennis.graphrat.query.GraphQueryXML;
import org.mcennis.graphrat.query.graph.GraphByLevel;
import org.mcennis.graphrat.query.graph.GraphByLevel.Operation;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author Daniel McEnnis
 */
public class GraphByLevelXML implements GraphQueryXML {

    GraphByLevel graphByLevel = new GraphByLevel();
    
    int level =1;
    
    Operation op = Operation.EQ;
    
    enum InternalState {START,LEVEL,OPERATION};
    
    InternalState internalState =  InternalState.START;
    
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if(localName.equalsIgnoreCase("Level")||qName.equalsIgnoreCase("Level")){
            internalState = InternalState.LEVEL;
        }else if(localName.equalsIgnoreCase("Operation")||qName.equalsIgnoreCase("Operation")){
            internalState = InternalState.OPERATION;
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        if(internalState == InternalState.LEVEL){
            //TODO: Error checking
            level = Integer.parseInt(new String(ch,start,length));
        }else if(internalState == InternalState.OPERATION){
            String operation = new String(ch,start,length);
            if(operation.equalsIgnoreCase("EQ")){
                op = Operation.EQ;
            }else if(operation.equalsIgnoreCase("GT")){
                op = Operation.GT;
            }else if(operation.equalsIgnoreCase("LT")){
                op =Operation.LT;
            }else if(operation.equalsIgnoreCase("GTE")){
                op = Operation.GTE;
            }else if(operation.equalsIgnoreCase("LTE")){
                op = Operation.LTE;
            }else if(operation.equalsIgnoreCase("NE")){
                op = Operation.NE;
            }
        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        if(internalState == InternalState.OPERATION){
            internalState = InternalState.START;
        } else if(internalState == InternalState.LEVEL){
            internalState = InternalState.START;
        } else if(internalState == InternalState.START){
            graphByLevel.buildQuery(level, op);
        }
    }

    public State buildingStatus() {
        return graphByLevel.buildingStatus();
    }

    public GraphQuery getQuery() {
        return graphByLevel;
    }

    public GraphByLevelXML newCopy() {
        return new GraphByLevelXML();
    }
}
