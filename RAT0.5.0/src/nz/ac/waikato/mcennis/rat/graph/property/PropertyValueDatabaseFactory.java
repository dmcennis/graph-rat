/*
 * PropertyValueDatabaseFactory - created 14/03/2009 - 6:27:22 PM
 * Copyright Daniel McEnnis, see license.txt
 */

package nz.ac.waikato.mcennis.rat.graph.property;

import java.sql.Connection;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import nz.ac.waikato.mcennis.rat.AbstractFactory;
import nz.ac.waikato.mcennis.rat.graph.descriptors.Parameter;
import nz.ac.waikato.mcennis.rat.graph.descriptors.ParameterFactory;
import nz.ac.waikato.mcennis.rat.graph.descriptors.ParameterInternal;
import nz.ac.waikato.mcennis.rat.graph.descriptors.Properties;
import nz.ac.waikato.mcennis.rat.graph.descriptors.SyntaxCheckerFactory;
import nz.ac.waikato.mcennis.rat.graph.descriptors.SyntaxObject;
import nz.ac.waikato.mcennis.rat.graph.property.database.AssociativeMiningItemsDB;
import nz.ac.waikato.mcennis.rat.graph.property.database.AttributeDB;
import nz.ac.waikato.mcennis.rat.graph.property.database.DoubleDB;
import nz.ac.waikato.mcennis.rat.graph.property.database.FileDB;
import nz.ac.waikato.mcennis.rat.graph.property.database.InstanceDB;
import nz.ac.waikato.mcennis.rat.graph.property.database.InstancesDB;
import nz.ac.waikato.mcennis.rat.graph.property.database.IntegerDB;
import nz.ac.waikato.mcennis.rat.graph.property.database.LongDB;
import nz.ac.waikato.mcennis.rat.graph.property.database.PropertyValueDB;
import nz.ac.waikato.mcennis.rat.graph.property.database.StringDB;
import nz.ac.waikato.mcennis.rat.graph.property.database.URLDB;

/**
 *
 * @author Daniel McEnnis
 */
public class PropertyValueDatabaseFactory extends AbstractFactory<PropertyValueDB>{

    Connection connection = null;
    static PropertyValueDatabaseFactory instance = null;

    public static PropertyValueDatabaseFactory newInstance(){
        if(instance == null){
            instance = new PropertyValueDatabaseFactory();
        }
        return instance;
    }

    private PropertyValueDatabaseFactory(){
        ParameterInternal name = ParameterFactory.newInstance().create("ValueClass", String.class);
        SyntaxObject check = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(check);
        name.add("String");
        properties.add(name);

        map.put("AssociativeMiningItems",new AssociativeMiningItemsDB());
        map.put("Attribute",new AttributeDB());
        map.put("Double",new DoubleDB());
        map.put("File",new FileDB());
        map.put("Instance",new InstanceDB());
        map.put("Instances",new InstancesDB());
        map.put("Integer",new IntegerDB());
        map.put("Long",new LongDB());
        map.put("String",new StringDB());
        map.put("URL",new URLDB());
    }

    @Override
    public PropertyValueDB create(Properties props) {
        return create(null,props);
    }

    public PropertyValueDB create(Class classType){
        return create(classType.getSimpleName(),properties);
    }

    public PropertyValueDB create(String classType){
        return create(classType,properties);
    }

    public PropertyValueDB create(String classType, Properties props){
        if(classType==null){
        if ((props.get("ValueClass") != null) && (props.get("QueryClass").getParameterClass().getName().contentEquals(String.class.getName()))) {
            classType = (String) props.get("ValueClass").getValue().iterator().next();
        } else {
            classType = (String) properties.get("ValueClass").getValue().iterator().next();
        }
        }

        if(map.containsKey(classType)){
            return map.get(classType).newCopy();
        }else{
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE,"PropertyXML class '"+classType+"' not found - assuming StringXML");
            return new StringDB();
        }
    }
    
    @Override
    public Parameter getClassParameter() {
        return properties.get("ValueClass");
    }

    public void init(Connection db){
        Iterator<PropertyValueDB> it = map.values().iterator();
        while(it.hasNext()){
            it.next().setConnection(db);
        }
    }

    public void clear(){

    }

    public void initializeDatabase(Connection conn){
        Iterator<PropertyValueDB> it = map.values().iterator();
        while(it.hasNext()){
            it.next().initializeDatabase(conn);
        }
    }

    public void clearDatabase(Connection conn){
        Iterator<PropertyValueDB> it = map.values().iterator();
        while(it.hasNext()){
            it.next().clearDatabase(conn);
        }
    }
}
