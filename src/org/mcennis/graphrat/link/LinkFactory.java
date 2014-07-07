/*

 * LinkFactory.java

 *

 * Created on 1 May 2007, 17:03

 *

 * copyright Daniel McEnnis - published under Aferro GPL (see license.txt)

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


package org.mcennis.graphrat.link;



import java.util.logging.Level;
import java.util.logging.Logger;
import org.dynamicfactory.AbstractFactory;
import org.dynamicfactory.descriptors.*;


/**

 *


 * 

 * Class for generating Links from a Properties map.

 * @author Daniel McEnnis
 */

public class LinkFactory extends AbstractFactory<Link>{

    

    static LinkFactory instance = null;

 
    /**

     * Create a new reference to a singelton LinkFactory object

     * 

     * @return a reference to a LinkFactory

     */

    public static LinkFactory newInstance(){

        if(instance == null){

            instance = new LinkFactory();

        }

        return instance;

    }

    /** Creates a new instance of LinkFactory */

    protected LinkFactory() {
        ParameterInternal param = ParameterFactory.newInstance().create("LinkClass", String.class);
        SyntaxObject restrictionPart = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        param.setRestrictions(restrictionPart);
        param.add("BasicUserLink");
        properties.add(param);

        param = ParameterFactory.newInstance().create("LinkRelation", String.class);
        restrictionPart = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        param.setRestrictions(restrictionPart);
        param.add("ListensTo");
        properties.add(param);

        param = ParameterFactory.newInstance().create("DerbyDirectory", String.class);
        restrictionPart = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        param.setRestrictions(restrictionPart);
        param.add("/tmp/");
        properties.add(param);

        param = ParameterFactory.newInstance().create("Database", String.class);
        restrictionPart = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        param.setRestrictions(restrictionPart);
        param.add("LiveJournal");
        properties.add(param);

        map.put("BasicUserLink", new BasicUserLink());
        map.put("DBLink",new DBLink());
    }

    

    /**

     * Creates a new Link based on the 'LinkClass' attribute.  Currently only BasicLink

     * is supported, so this value is ignored.

     * 

     * 'LinkType' sets the link type (relation) of this link before returning.

     * 

     * @param relation map of properties for creating a map

     * @return a newly created Link

     */

    public Link create(String relation){
        return create(relation,properties);
    }
    
    public Link create(Properties props){
        return create(null,props);
    }
    
    public Link create(String relation, Properties parameters){
        String linkClass = "";
        if ((parameters.get("ActorClass") != null) && (parameters.get("ActorClass").getParameterClass().getName().contentEquals(String.class.getName()))) {
            linkClass = (String) parameters.get("ActorClass").getValue().iterator().next();
        } else {
            linkClass = (String) properties.get("ActorClass").getValue().iterator().next();
        }
        
        Link ret = null;
        if(map.containsKey(linkClass)){
            ret = map.get(linkClass).prototype();
        }else{
            ret = new BasicUserLink();
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Link class '"+linkClass+"' not found - assuming BasicUserLink");
        }
        
        ret.init(parameters);
        
        if (relation == null) {
            if ((parameters.get("Relation") != null) && (parameters.get("Relation").getParameterClass().getName().contentEquals(String.class.getName()))) {
                relation = (String) parameters.get("Relation").getValue().iterator().next();
            } else {
                relation = (String) properties.get("Relation").getValue().iterator().next();
            }
        }
        ret.setRelation(relation);
        
        return ret;
    }

    public Parameter getClassParameter(){
        return properties.get("LinkClass");
    }
}

