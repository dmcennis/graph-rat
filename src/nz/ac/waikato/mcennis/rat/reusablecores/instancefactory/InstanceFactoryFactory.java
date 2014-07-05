/**
 * Jul 23, 2008-7:14:12 PM
 * Copyright Daniel McEnnis, see license.txt
 */

package nz.ac.waikato.mcennis.rat.reusablecores.instancefactory;

import nz.ac.waikato.mcennis.rat.AbstractFactory;
import org.dynamicfactory.descriptors.Parameter;
import org.dynamicfactory.descriptors.ParameterFactory;
import org.dynamicfactory.descriptors.ParameterInternal;
import org.dynamicfactory.descriptors.Properties;
import org.dynamicfactory.descriptors.SyntaxCheckerFactory;
import org.dynamicfactory.descriptors.SyntaxObject;
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
