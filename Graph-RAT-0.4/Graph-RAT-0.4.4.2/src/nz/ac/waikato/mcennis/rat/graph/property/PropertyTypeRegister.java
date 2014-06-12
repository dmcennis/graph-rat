/*
 * Copyright Daniel McEnnis, see license.txt
 */

package nz.ac.waikato.mcennis.rat.graph.property;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import weka.core.Instance;
import weka.core.Instances;


/**
 * Singleton registry object for obtaining PropertyValueFactory objects of the given
 * class without needing to hard-code the classes in the parsers.
 * @author Daniel McEnnis
 */
public class PropertyTypeRegister {
    
    static PropertyTypeRegister instance = null;
    
    HashMap<Class,PropertyValueFactory> factoryMap;
    
    /**
     * Obtain a static instance of this class, creeeating if necessary
     * @return static instance of this object
     */
    public static PropertyTypeRegister newInstance(){
        if(instance == null){
            instance = new PropertyTypeRegister();
        }
        return instance;
    }
    
    /**
     * Get the appropriate factory of this type using a class object of the 
     * given type as a key.  Returns null if no factory exists for the given object
     * @param type class object describing the class of what is to be4 serialized or deserialized
     * @return factory for processing this class type or null
     */
    public PropertyValueFactory getFactory(java.lang.Class type){
        return factoryMap.get(type);
    }
    
    /**
     * Register the given factory for the given class of objects.  This replaces
     * silently if anonther factory is already registered for this class.
     * @param type class this factory serializes
     * @param value factory to perform the serialization
     */
    public void addFactory(java.lang.Class type,PropertyValueFactory value){
        factoryMap.put(type, value);
    }
    
    protected PropertyTypeRegister(){
        factoryMap = new HashMap<Class,PropertyValueFactory>();
        factoryMap.put(String.class, new StringFactory());
        factoryMap.put(Double.class,new DoubleFactory());
        factoryMap.put(Long.class, new LongFactory());
        factoryMap.put(Integer.class, new IntegerFactory());
        factoryMap.put(HashMap.class, new HashMapStringDoubleFactory());
        factoryMap.put(Instance.class, new InstanceFactory());
        factoryMap.put(Instances.class,new InstancesFactory());
        factoryMap.put(URL.class, new URLFactory());
        factoryMap.put(File.class, new FileFactory());
    }
    
}

