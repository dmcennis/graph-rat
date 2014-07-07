/**
 * ActorQueryXMLFactory
 * Created Jan 26, 2009 - 10:46:51 PM
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

import java.util.logging.Level;
import java.util.logging.Logger;
import org.dynamicfactory.AbstractFactory;
import org.mcennis.graphrat.query.ActorQueryXML;
import org.dynamicfactory.descriptors.*;


/**
 *
 * @author Daniel McEnnis
 */
public class ActorQueryXMLFactory extends AbstractFactory<ActorQueryXML>{

        static ActorQueryXMLFactory instance = null;
    
    static public ActorQueryXMLFactory newInstance(){
        if(instance == null){
            instance = new ActorQueryXMLFactory();
        }
        return instance;
    }
    
    private ActorQueryXMLFactory(){
        ParameterInternal name = ParameterFactory.newInstance().create("ActorQueryXMLClass", String.class);
        SyntaxObject syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("NullQuery");
        properties.add(name);
        
        map.put("ActorByLink",new ActorByLinkXML());
        map.put("ActorByMode",new ActorByModeXML());
        map.put("ActorByProperty",new ActorByPropertyXML());
        map.put("AndQuery",new AndActorQueryXML());
        map.put("OrQuery",new OrActorQueryXML());
        map.put("XorQuery",new XorActorQueryXML());
        map.put("NullQuery",new NullActorQueryXML());
        
    }

    @Override
    public ActorQueryXML create(Properties props) {
        return create(null,props);
    }
    
    public ActorQueryXML create(String name){
        return create(name,properties);
    }
    
    public ActorQueryXML create(String name, Properties parameters){
        if(name == null){
        if ((parameters.get("ActorQueryXMLClass") != null) && (parameters.get("ActorQueryXMLClass").getParameterClass().getName().contentEquals(String.class.getName()))) {
            name = (String) parameters.get("ActorQueryXMLClass").getValue().iterator().next();
        } else {
            name = (String) properties.get("ActorQueryXMLClass").getValue().iterator().next();
        }
        }

        if(map.containsKey(name)){
            return (ActorQueryXML)map.get(name).newCopy();
        }else{
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE,"Distance class '"+name+"' not found - assuming NullQueryXML<Actor>");
            return new NullActorQueryXML();
        }
    }
    
    public Parameter getClassParameter(){
        return properties.get("ActorQueryXMLClass");
    }
}
