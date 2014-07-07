/**
 * DataVectorXMLFactory
 * Created Jan 17, 2009 - 10:01:36 PM
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
package org.mcennis.graphrat.reusablecores.datavector.xml;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.dynamicfactory.AbstractFactory;
import org.dynamicfactory.descriptors.*;

/**
 *
 * @author Daniel McEnnis
 */
public class DataVectorXMLFactory extends AbstractFactory<DataVectorXML>{
    static DataVectorXMLFactory instance= null;

    public static DataVectorXMLFactory newInstance(){
        if(instance==null){
            instance = new DataVectorXMLFactory();
        }
        return instance;
    }
    
    DataVectorXMLFactory(){
        ParameterInternal name = ParameterFactory.newInstance().create("DataVectorXMLClass", String.class);
        SyntaxObject syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("DoubleArrayVector");
        properties.add(name);
        
        map.put("DoubleArrayVector",new DoubleArrayVectorXML());
        map.put("InstanceVector",new InstanceVectorXML());
        map.put("MapVector",new MapVectorXML());
    }
    
    public DataVectorXML create(String name){
        return create(name,properties);
    }
    
    public DataVectorXML create(Properties props){
        return create(null,props);
    }
    
    public DataVectorXML create(String name, Properties parameters){
        if(name == null){
        if ((parameters.get("DataVectorXMLClass") != null) && (parameters.get("DataVectorXMLClass").getParameterClass().getName().contentEquals(String.class.getName()))) {
            name = (String) parameters.get("DataVectorXMLClass").getValue().iterator().next();
        } else {
            name = (String) properties.get("DataVectorXMLClass").getValue().iterator().next();
        }
        }

        if(map.containsKey(name)){
            return (DataVectorXML)map.get(name).newCopy();
        }else{
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE,"DataVectorXML class '"+name+"' not found - assuming DoubleArrayVector");
            return new DoubleArrayVectorXML();
        }
    }
    public Parameter getClassParameter(){
        return properties.get("DataVectorXMLClass");
    }
}
