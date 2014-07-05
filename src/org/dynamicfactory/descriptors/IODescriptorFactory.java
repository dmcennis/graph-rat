/*

 * DescriptorFactory.java

 *

 * Created on 14 September 2007, 13:58

 *

 * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt)

 */



package org.dynamicfactory.descriptors;

import nz.ac.waikato.mcennis.rat.AbstractFactory;
import nz.ac.waikato.mcennis.rat.graph.query.Query;


/**

 *


 * 

 * Class for generating descriptors from a Properties map. 

 * @author Daniel McEnnis
 */

public class IODescriptorFactory extends AbstractFactory<IODescriptorInternal>{

    

    private static IODescriptorFactory instance = null;
    
    /**

     * Create a new reference to a singelton DescriptorFactory object

     * 

     * @return reference to the Factory.

     */

    public static IODescriptorFactory newInstance(){
        if(instance == null){
            instance = new IODescriptorFactory();
        }
        return instance;
    }

    

    /** Creates a new instance of DescriptorFactory */

    private IODescriptorFactory() {
        ParameterInternal name = ParameterFactory.newInstance().create("IODescriptorClass", String.class);
        SyntaxObject restriction = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(restriction);
        name.add("BasicIODescriptor");
        properties.add(name);
        
        name = ParameterFactory.newInstance().create("AlgorithmName", String.class);
        restriction = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(restriction);
        name.add("AggregateByLinkProperty");
        properties.add(name);
        
        name = ParameterFactory.newInstance().create("Relation", String.class);
        restriction = SyntaxCheckerFactory.newInstance().create(0, 1, null, String.class);
        name.setRestrictions(restriction);
        properties.add(name);
        
        name = ParameterFactory.newInstance().create("Query", Query.class);
        restriction = SyntaxCheckerFactory.newInstance().create(0, 1, null, Query.class);
        name.setRestrictions(restriction);
        properties.add(name);
        
        name = ParameterFactory.newInstance().create("Property", String.class);
        restriction = SyntaxCheckerFactory.newInstance().create(0, 1, null, String.class);
        name.setRestrictions(restriction);
        properties.add(name);
        
        name = ParameterFactory.newInstance().create("Type", IODescriptor.Type.class);
        restriction = SyntaxCheckerFactory.newInstance().create(1, 1, null, IODescriptor.Type.class);
        name.setRestrictions(restriction);
        name.add(IODescriptor.Type.ACTOR_PROPERTY);
        properties.add(name);
        
        map.put("BasicIODescriptor",new BasicIODescriptor());
    }

    


    public IODescriptorInternal create(IODescriptor.Type type, String algorithm, String relation, Query query, String property, String description){
        return create(type,algorithm,relation,query,property,description,properties);
    }

    public IODescriptorInternal create(IODescriptor.Type type, String algorithm, String relation, Query query, String property,String description, boolean appendGraphID){
        return create(type,algorithm,relation,query,property,description,appendGraphID,properties);
    }


    public IODescriptorInternal create(IODescriptor.Type type, String algorithm, String relation, Query query, String property,String description, Properties props)   {
        return create(type,algorithm,relation,query,property,description,false,properties);
    }
    
    public IODescriptorInternal create(IODescriptor.Type type, String algorithm, String relation, Query query, String property,String description,boolean appendGraphID, Properties props){

        String descriptorType = "";
            if((props.get("IODescriptorClass")!=null)&&(props.get("IODescriptorClass").getParameterClass().getName().contentEquals(String.class.getName()))){
                descriptorType = (String)props.get("IODescriptorClass").getValue().iterator().next();
            }else{
                descriptorType = (String)properties.get("IODescriptorClass").getValue().iterator().next();
            }
        IODescriptorInternal ret = null;
        if(map.containsKey(descriptorType)){
            ret = map.get(descriptorType).prototype();
        }
        
        if(type == null){
            if((props.get("Type")!=null)&&(props.get("Type").getParameterClass().getName().contentEquals(IODescriptor.Type.class.getName()))){
                type = (IODescriptor.Type)props.get("Type").getValue().iterator().next();
            }else{
                type = (IODescriptor.Type)properties.get("Type").getValue().iterator().next();
            }
        }
        ret.setClassType(type);
        
        if(algorithm == null){
            if((props.get("AlgorithmName")!=null)&&(props.get("AlgorithmName").getParameterClass().getName().contentEquals(String.class.getName()))){
                algorithm = (String)props.get("AlgorithmName").getValue().iterator().next();
            }else{
                algorithm = (String)properties.get("AlgorithmName").getValue().iterator().next();
            }
        }
        ret.setAlgorithmName(algorithm);
        
        if(relation == null){
            if((props.get("Relation")!=null)&&(props.get("Relation").getValue().size()>0)&&(props.get("Relation").getParameterClass().getName().contentEquals(String.class.getName()))){
                relation = (String)props.get("Relation").getValue().iterator().next();
            }else if((properties.get("Relation").getValue().size()>0)&&(properties.get("Relation").getParameterClass().getName().contentEquals(String.class.getName()))){
                relation = (String)properties.get("Relation").getValue().iterator().next();
            }
        }
        ret.setRelation(relation);
        
        if(query == null){
            if((props.get("Query")!=null)&&(props.get("Query").getValue().size()>0)&&(props.get("Query").getParameterClass().getName().contentEquals(String.class.getName()))){
                query = (Query)props.get("Query").getValue().iterator().next();
            }else if((properties.get("Query").getValue().size()>0)&&(properties.get("Query").getParameterClass().getName().contentEquals(String.class.getName()))){
                query = (Query)properties.get("Query").getValue().iterator().next();
            }
        }
        ret.setQuery(query);
        
         if(property == null){
            if((props.get("Property")!=null)&&(props.get("Property").getValue().size()>0)&&(props.get("Property").getParameterClass().getName().contentEquals(String.class.getName()))){
                property = (String)props.get("Property").getValue().iterator().next();
            }else if((properties.get("Query").getValue().size()>0)&&(properties.get("Query").getParameterClass().getName().contentEquals(String.class.getName()))){
                property = (String)properties.get("Query").getValue().iterator().next();
            }
        }
        ret.setProperty(property);


         if(description == null){
            if((props.get("Property")!=null)&&(props.get("Property").getValue().size()>0)&&(props.get("Property").getParameterClass().getName().contentEquals(String.class.getName()))){
                description = (String)props.get("Property").getValue().iterator().next();
            }else if((properties.get("Query").getValue().size()>0)&&(properties.get("Query").getParameterClass().getName().contentEquals(String.class.getName()))){
                description = (String)properties.get("Query").getValue().iterator().next();
            }
        }
        ret.setDescription(description);
        
        return ret;
    }    
    
    @Override
    public IODescriptorInternal create(Properties props) {
        return create(null,null,null,null,null,null,props);
    }

    public Parameter getClassParameter(){
        return properties.get("IODescriptorClass");
    }
    

}

