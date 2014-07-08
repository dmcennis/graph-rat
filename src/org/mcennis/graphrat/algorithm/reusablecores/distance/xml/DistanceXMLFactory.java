/**
 * DistanceXMLFactory
 * Created Jan 17, 2009 - 8:33:28 PM
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
package org.mcennis.graphrat.algorithm.reusablecores.distance.xml;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.dynamicfactory.AbstractFactory;
import org.dynamicfactory.descriptors.*;

/**
 *
 * @author Daniel McEnnis
 */
public class DistanceXMLFactory extends AbstractFactory<DistanceXML>{
    static DistanceXMLFactory instance = null;
    
    static public DistanceXMLFactory newInstance(){
        if(instance == null){
            instance = new DistanceXMLFactory();
        }
        return instance;
    }
    
    DistanceXMLFactory(){
        ParameterInternal name = ParameterFactory.newInstance().create("DistanceXMLClass", String.class);
        SyntaxObject syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("CosineDistance");
        properties.add(name);
        
        
        map.put("ChebyshevDistance",new ChebyshevDistanceXML());
        map.put("CosineDistance",new CosineDistanceXML());
        map.put("DotProductDistance",new DotProductDistanceXML());
        map.put("EuclideanDistance",new EuclideanDistanceXML());
        map.put("JaccardDistance",new JaccardDistanceXML());
        map.put("ManhattanDistance",new ManhattanDistanceXML());
        map.put("PearsonDistance",new PearsonDistanceXML());
        map.put("WeightedKLDDistance", new WeightedKLDDistanceXML());
    }
    
    @Override
    public DistanceXML create(Properties props) {
        return create(null,props);
    }
    
    public DistanceXML create(String name){
        return create(name,properties);
    }
    
    public DistanceXML create(String name, Properties parameters){
        if(name == null){
        if ((parameters.get("DistanceXMLClass") != null) && (parameters.get("DistanceXMLClass").getParameterClass().getName().contentEquals(String.class.getName()))) {
            name = (String) parameters.get("DistanceXMLClass").getValue().iterator().next();
        } else {
            name = (String) properties.get("DistanceXMLClass").getValue().iterator().next();
        }
        }

        if(map.containsKey(name)){
            return (DistanceXML)map.get(name).newCopy();
        }else{
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE,"Distance class '"+name+"' not found - assuming CosineDistanceXML");
            return new CosineDistanceXML();
        }
    }
    
    public Parameter getClassParameter(){
        return properties.get("DistanceXMLClass");
    }
}
