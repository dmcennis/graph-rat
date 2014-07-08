/**
 * Jul 23, 2008-7:14:12 PM
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

package org.mcennis.graphrat.algorithm.reusablecores.instancefactory;

import org.dynamicfactory.AbstractFactory;
import org.dynamicfactory.descriptors.*;

import weka.core.Instance;

/**
 *
 * Static class that holds a reference to all known factories for transforming objects
 * to Instance objects indexed by object class.  If the class is not in the registry, 
 * the DefaultInstanceFactory is returned instead.
 * 
 * @author Daniel McEnnis
 */
public class InstanceFactoryFactory extends AbstractFactory<InstanceFactory>{
    static InstanceFactoryFactory instance = null;
    
    /**
     * Creates the factory if necessary and then returns a reference to this registry
     * @return reference to InstanceFactoryRegistry object.
     */
    public static InstanceFactoryFactory newInstance(){
        if(instance == null){
            instance = new InstanceFactoryFactory();
        }
        return instance;
    }
    
    private InstanceFactoryFactory(){
        ParameterInternal name = ParameterFactory.newInstance().create("InstanceFactoryClass",String.class);
        SyntaxObject syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        name.add(String.class.getName());
        properties.add(name);
        
        
        map.put((new double[]{}).getClass().getName(),new DoubleArrayInstanceFactory());
        map.put(Double.class.getName(),new DoubleInstanceFactory());
        map.put(Long.class.getName(),new LongInstanceFactory());
        map.put(String.class.getName(),new StringInstanceFactory());
        map.put(Integer.class.getName(), new IntegerInstanceFactory());
        map.put(Instance.class.getName(), new InstanceInstanceFactory());
    }
    
    /**
     * Returns the InstanceFactory that transforms object of the type determined
     * by the given class object or the DefaultInstanceFactory if no factory of the
     * correct type is given.
     * 
     * @param type class of object to transform 
     * @return appropriate factory to create an instance from this object type
     */
    public InstanceFactory create(Class type){
        if(map.containsKey(type.getSimpleName())){
            return map.get(type.getSimpleName());
        }else{
            return new DefaultInstanceFactory();
        }
    }
    
    public InstanceFactory create(String classType, Properties parameters){
        if(classType == null){
        if ((parameters.get("InstanceFactoryClass") != null) && (parameters.get("InstanceFactoryClass").getParameterClass().getName().contentEquals(String.class.getName()))) {
            classType = (String) parameters.get("InstanceFactoryClass").getValue().iterator().next();
        } else {
            classType = (String) properties.get("InstanceFactoryClass").getValue().iterator().next();
        }
        }
        InstanceFactory ret = null;
        if(map.containsKey(classType)){
            ret = (InstanceFactory)map.get(classType).prototype();
        }else{
            ret = new DefaultInstanceFactory();
        }
        return ret;        
    }
    
    public InstanceFactory create(Properties props){
        return create(null,props);
    }
    
    public InstanceFactory create(String name){
        return create(name,properties);
    }
    public Parameter getClassParameter(){
        return properties.get("InstanceFactoryClass");
    }
}
