/**
 * ActorQueryFactory
 * Created Jan 26, 2009 - 8:29:21 PM
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
import org.mcennis.graphrat.query.actor.ActorByMode;
import org.mcennis.graphrat.query.actor.ActorByLink;
import org.mcennis.graphrat.query.actor.ActorByProperty;
import org.mcennis.graphrat.query.actor.AndActorQuery;
import org.mcennis.graphrat.query.actor.NullActorQuery;
import org.mcennis.graphrat.query.actor.OrActorQuery;
import org.mcennis.graphrat.query.actor.XorActorQuery;

/**
 *
 * @author Daniel McEnnis
 */
public class ActorQueryFactory extends AbstractFactory<ActorQuery>{

    static ActorQueryFactory instance = null;
    
    public static ActorQueryFactory newInstance(){
        if(instance == null){
            instance = new ActorQueryFactory();
        }
        return instance;
    }
    
    private ActorQueryFactory(){
        ParameterInternal name = ParameterFactory.newInstance().create("QueryClass", String.class);
        SyntaxObject check = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(check);
        name.add("NullQuery");
        properties.add(name);
        
        map.put("AndQuery",new AndActorQuery());
        map.put("NullQuery",new NullActorQuery());
        map.put("OrQuery",new OrActorQuery());
        map.put("XorQuery",new XorActorQuery());
        map.put("ActorByMode",new ActorByMode());
        map.put("ActorByLink",new ActorByLink());
        map.put("ActorByProperty",new ActorByProperty());
    }
    
    @Override
    public ActorQuery create(Properties props) {
        return create(null,props);
    }
    
    public ActorQuery create(String id){
        return create(id,properties);
    }
    
    public ActorQuery create(String id, Properties parameters){
        String classType = "";
        if ((parameters.get("QueryClass") != null) && (parameters.get("QueryClass").getParameterClass().getName().contentEquals(String.class.getName()))) {
            classType = (String) parameters.get("QueryClass").getValue().iterator().next();
        } else {
            classType = (String) properties.get("QueryClass").getValue().iterator().next();
        }

        if(map.containsKey(classType)){
            return map.get(classType).prototype();
        }else{
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE,"ActoQuery class '"+classType+"' nbot found - assuming NullQuery<Actor>");
            return new NullActorQuery();
        }
    }

    public Parameter getClassParameter(){
        return properties.get("QueryClass");
    }
}
