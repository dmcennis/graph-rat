/**
 * GraphQueryXMLFactory
 * Created Jan 26, 2009 - 11:00:14 PM
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

import java.util.logging.Level;
import java.util.logging.Logger;
import org.dynamicfactory.AbstractFactory;
import org.dynamicfactory.descriptors.*;

import org.mcennis.graphrat.query.GraphQueryXML;

/**
 *
 * @author Daniel McEnnis
 */
public class GraphQueryXMLFactory extends AbstractFactory<GraphQueryXML>{

        static GraphQueryXMLFactory instance = null;
    
    static public GraphQueryXMLFactory newInstance(){
        if(instance == null){
            instance = new GraphQueryXMLFactory();
        }
        return instance;
    }
    
    private GraphQueryXMLFactory(){
        ParameterInternal name = ParameterFactory.newInstance().create("GraphQueryXMLClass", String.class);
        SyntaxObject syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("NullQuery");
        properties.add(name);
        
        map.put("GraphByLink",new GraphByLinkXML());
        map.put("GraphByActor",new GraphByActorXML());
        map.put("GraphByProperty",new GraphByPropertyXML());
        map.put("GraphByID",new GraphByIDXML());
        map.put("GraphByLevel",new GraphByLevelXML());
        map.put("AndQuery",new AndGraphQueryXML());
        map.put("OrQuery",new OrGraphQueryXML());
        map.put("XorQuery",new XorGraphQueryXML());
        map.put("NullQuery",new NullGraphQueryXML());
        
    }

    @Override
    public GraphQueryXML create(Properties props) {
        return create(null,props);
    }
    
    public GraphQueryXML create(String name){
        return create(name,properties);
    }
    
    public GraphQueryXML create(String name, Properties parameters){
        if(name == null){
        if ((parameters.get("GraphQueryXMLClass") != null) && (parameters.get("GraphQueryXMLClass").getParameterClass().getName().contentEquals(String.class.getName()))) {
            name = (String) parameters.get("GraphQueryXMLClass").getValue().iterator().next();
        } else {
            name = (String) properties.get("GraphQueryXMLClass").getValue().iterator().next();
        }
        }

        if(map.containsKey(name)){
            return (GraphQueryXML)map.get(name).newCopy();
        }else{
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE,"Distance class '"+name+"' not found - assuming NullQueryXML<Graph>");
            return new NullGraphQueryXML();
        }
    }
    public Parameter getClassParameter(){
        return properties.get("GraphQueryXMLClass");
    }
}
