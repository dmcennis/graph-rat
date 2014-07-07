/**
 * LinkQueryXMLFactory
 * Created Jan 26, 2009 - 11:06:22 PM
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

import java.util.logging.Level;
import java.util.logging.Logger;
import org.dynamicfactory.AbstractFactory;
import org.dynamicfactory.descriptors.*;
import org.mcennis.graphrat.query.LinkQueryXML;

/**
 *
 * @author Daniel McEnnis
 */
public class LinkQueryXMLFactory extends AbstractFactory<LinkQueryXML>{

        static LinkQueryXMLFactory instance = null;
    
    static public LinkQueryXMLFactory newInstance(){
        if(instance == null){
            instance = new LinkQueryXMLFactory();
        }
        return instance;
    }
    
    private LinkQueryXMLFactory(){
        ParameterInternal name = ParameterFactory.newInstance().create("LinkQueryXMLClass", String.class);
        SyntaxObject syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("NullQuery");
        properties.add(name);
        
        map.put("LinkByActor",new LinkByActorXML());
        map.put("LinkByRelation",new LinkByRelationXML());
        map.put("LinkByProperty",new LinkByPropertyXML());
        map.put("AndQuery",new AndLinkQueryXML());
        map.put("OrQuery",new OrLinkQueryXML());
        map.put("XorQuery",new XorLinkQueryXML());
        map.put("NullQuery",new NullLinkQueryXML());
    }

    @Override
    public LinkQueryXML create(Properties props) {
        return create(null,props);
    }
    
    public LinkQueryXML create(String name){
        return create(name,properties);
    }
    
    public LinkQueryXML create(String name, Properties parameters){
        if(name == null){
        if ((parameters.get("LinkQueryXMLClass") != null) && (parameters.get("LinkQueryXMLClass").getParameterClass().getName().contentEquals(String.class.getName()))) {
            name = (String) parameters.get("LinkQueryXMLClass").getValue().iterator().next();
        } else {
            name = (String) properties.get("LinkQueryXMLClass").getValue().iterator().next();
        }
        }

        if(map.containsKey(name)){
            return (LinkQueryXML)map.get(name).newCopy();
        }else{
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE,"Distance class '"+name+"' not found - assuming NullQueryXML<Link>");
            return new NullLinkQueryXML();
        }
    }
    
    public Parameter getClassParameter(){
        return properties.get("LinkQueryXMLClass");
    }
}
