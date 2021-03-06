/**
 * GraphQueryFactory
 * Created Jan 26, 2009 - 8:57:14 PM
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
package org.mcennis.graphrat.query;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.dynamicfactory.AbstractFactory;
import org.dynamicfactory.descriptors.*;
import org.mcennis.graphrat.query.graph.GraphByLink;
import org.mcennis.graphrat.query.graph.GraphByActor;
import org.mcennis.graphrat.query.graph.GraphByProperty;
import org.mcennis.graphrat.query.graph.GraphByID;
import org.mcennis.graphrat.query.graph.GraphByLevel;
import org.mcennis.graphrat.query.graph.AllGraphs;
import org.mcennis.graphrat.query.graph.AndGraphQuery;
import org.mcennis.graphrat.query.graph.NullGraphQuery;
import org.mcennis.graphrat.query.graph.OrGraphQuery;
import org.mcennis.graphrat.query.graph.XorGraphQuery;
/**
 *
 * @author Daniel McEnnis
 */
public class GraphQueryFactory extends AbstractFactory<GraphQuery>{

    static GraphQueryFactory instance = null;
    
    public static GraphQueryFactory newInstance(){
        if(instance == null){
            instance = new GraphQueryFactory();
        }
        return instance;
    }
    
    private GraphQueryFactory(){
        ParameterInternal name = ParameterFactory.newInstance().create("QueryClass", String.class);
        SyntaxObject check = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(check);
        name.add("NullQuery");
        properties.add(name);
        
        map.put("AndQuery",new AndGraphQuery());
        map.put("NullQuery",new NullGraphQuery());
        map.put("OrQuery",new OrGraphQuery());
        map.put("XorQuery",new XorGraphQuery());
        map.put("GraphByLink",new GraphByLink());
        map.put("GraphByActor",new GraphByActor());
        map.put("GraphByProperty",new GraphByProperty());
        map.put("GraphByID",new GraphByID());
        map.put("GraphByLevel",new GraphByLevel());
	map.put("AllGraphs",new AllGraphs());
    }
    
    @Override
    public GraphQuery create(Properties props) {
        return create(null,props);
    }
    
    public GraphQuery create(String id){
        return create(id,properties);
    }
    
    public GraphQuery create(String id, Properties parameters){
        String classType = "";
        if ((parameters.get("QueryClass") != null) && (parameters.get("QueryClass").getParameterClass().getName().contentEquals(String.class.getName()))) {
            classType = (String) parameters.get("QueryClass").getValue().iterator().next();
        } else {
            classType = (String) properties.get("QueryClass").getValue().iterator().next();
        }

        if(map.containsKey(classType)){
            return map.get(classType).prototype();
        }else{
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE,"GraphQuery class '"+classType+"' not found - assuming NullQuery<Graph>");
            return new NullGraphQuery();
        }
    }

    public Parameter getClassParameter(){
        return properties.get("QueryClass");
    }
}
