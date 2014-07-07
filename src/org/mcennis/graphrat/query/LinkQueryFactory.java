/**
 * LinkQueryFactory
 * Created Jan 26, 2009 - 8:52:23 PM
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
import org.mcennis.graphrat.query.link.AndLinkQuery;
import org.mcennis.graphrat.query.link.LinkByRelation;
import org.mcennis.graphrat.query.link.LinkByActor;
import org.mcennis.graphrat.query.link.LinkByProperty;
import org.mcennis.graphrat.query.link.NullLinkQuery;
import org.mcennis.graphrat.query.link.OrLinkQuery;
import org.mcennis.graphrat.query.link.XorLinkQuery;
import org.dynamicfactory.descriptors.*;

/**
 *
 * @author Daniel McEnnis
 */
public class LinkQueryFactory extends AbstractFactory<LinkQuery>{

    static LinkQueryFactory instance = null;
    
    public static LinkQueryFactory newInstance(){
        if(instance == null){
            instance = new LinkQueryFactory();
        }
        return instance;
    }
    
    private LinkQueryFactory(){
        ParameterInternal name = ParameterFactory.newInstance().create("QueryClass", String.class);
        SyntaxObject check = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(check);
        name.add("NullQuery");
        properties.add(name);
        
        map.put("AndQuery",new AndLinkQuery());
        map.put("NullQuery",new NullLinkQuery());
        map.put("OrQuery",new OrLinkQuery());
        map.put("XorQuery",new XorLinkQuery());
        map.put("LinkByRelation",new LinkByRelation());
        map.put("LinkByActor",new LinkByActor());
        map.put("LinkByProperty",new LinkByProperty());
    }
    
    @Override
    public LinkQuery create(Properties props) {
        return create(null,props);
    }
    
    public LinkQuery create(String id){
        return create(id,properties);
    }
    
    public LinkQuery create(String id, Properties parameters){
        String classType = "";
        if ((parameters.get("QueryClass") != null) && (parameters.get("QueryClass").getParameterClass().getName().contentEquals(String.class.getName()))) {
            classType = (String) parameters.get("QueryClass").getValue().iterator().next();
        } else {
            classType = (String) properties.get("QueryClass").getValue().iterator().next();
        }

        if(map.containsKey(classType)){
            return map.get(classType).prototype();
        }else{
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE,"LinkQuery class '"+classType+"' not found - assuming NullQuery<Link>");
            return new NullLinkQuery();
        }
    }

    public Parameter getClassParameter(){
        return properties.get("QueryClass");
    }
}
