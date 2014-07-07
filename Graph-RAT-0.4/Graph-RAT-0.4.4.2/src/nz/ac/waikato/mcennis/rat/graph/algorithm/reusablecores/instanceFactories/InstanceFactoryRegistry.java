/**
 * Jul 23, 2008-7:14:12 PM
 * Copyright Daniel McEnnis, see license.txt
 */

package org.mcennis.graphrat.algorithm.reusablecores.instanceFactories;

import java.util.HashMap;
import weka.core.Instance;

/**
 *
 * Static class that holds a reference to all known factories for transforming objects
 * to Instance objects indexed by object class.  If the class is not in the registry, 
 * the DefaultInstanceFactory is returned instead.
 * 
 * @author Daniel McEnnis
 */
public class InstanceFactoryRegistry {
    static InstanceFactoryRegistry instance = null;
    
    HashMap<Class,InstanceFactory> map;
    
    /**
     * Creates the factory if necessary and then returns a reference to this registry
     * @return reference to InstanceFactoryRegistry object.
     */
    public static InstanceFactoryRegistry newInstance(){
        if(instance == null){
            instance = new InstanceFactoryRegistry();
        }
        return instance;
    }
    
    private InstanceFactoryRegistry(){
        map = new HashMap<Class,InstanceFactory>();
        map.put((new double[]{}).getClass(),new DoubleArrayInstanceFactory());
        map.put(Double.class,new DoubleInstanceFactory());
        map.put(Long.class,new LongInstanceFactory());
        map.put(String.class,new StringInstanceFactory());
        map.put(Integer.class, new IntegerInstanceFactory());
        map.put(Instance.class, new InstanceInstanceFactory());
    }
    
    /**
     * Returns the InstanceFactory that transforms object of the type determined
     * by the given class object or the DefaultInstanceFactory if no factory of the
     * correct type is given.
     * 
     * @param type class of object to transform 
     * @return appropriate factory to create an instance from this object type
     */
    public InstanceFactory getFactory(Class type){
        if(map.containsKey(type)){
            return map.get(type);
        }else{
            return new DefaultInstanceFactory();
        }
    }
}
