/*
 * GraphFactory.java
 *
 * Created on 1 May 2007, 17:05
 *
 * copyright Daniel McEnnis - published under Aferro GPL (see license.txt)
 */

package org.mcennis.graphrat.graph;

import java.io.File;
import java.io.Writer;
import java.util.LinkedList;
import java.util.logging.Logger;
import java.util.logging.Level;
import org.dynamicfactory.AbstractFactory;
import org.dynamicfactory.descriptors.*;

/**
 * Create a Graph object of the given type with the given arguments
 *
 * @author Daniel McEnnis
 *
 */
public class GraphFactory extends AbstractFactory<Graph>{
    
    /**
     *Singleton of graph factory
     */
    static GraphFactory instance = null;
    
    /**
     * return a singleton of this type, creating it if necessary
     *
     * @return signelton instance of GraphFactory
     */
    public static GraphFactory newInstance(){
        if(instance==null){
            instance = new GraphFactory();
        }
        return instance;
    }
    
    /** Creates a new instance of GraphFactory */
    protected GraphFactory() {
        ParameterInternal name = ParameterFactory.newInstance().create("GraphID",String.class);
        SyntaxObject restrictionPart = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(restrictionPart);
        name.add("Default");
        properties.add(name);

        name = ParameterFactory.newInstance().create("GraphClass",String.class);
        restrictionPart = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(restrictionPart);
        name.add("MemGraph");
        properties.add(name);

        name = ParameterFactory.newInstance().create("GraphOutput",Writer.class);
        restrictionPart = SyntaxCheckerFactory.newInstance().create(1, 1, null, Writer.class);
        name.setRestrictions(restrictionPart);
        properties.add(name);

        name = ParameterFactory.newInstance().create("Compression",Boolean.class);
        restrictionPart = SyntaxCheckerFactory.newInstance().create(1, 1, null, Boolean.class);
        name.setRestrictions(restrictionPart);
        name.add(true);
        properties.add(name);
 
        name = ParameterFactory.newInstance().create("DerbyDirectory",File.class);
        restrictionPart = SyntaxCheckerFactory.newInstance().create(1, 1, null, File.class);
        name.setRestrictions(restrictionPart);
        name.add(new File("/tmp/"));
        properties.add(name);

        name = ParameterFactory.newInstance().create("DatabaseName",String.class);
        restrictionPart = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(restrictionPart);
        name.add("LiveJournal");
        properties.add(name);

        name = ParameterFactory.newInstance().create("DatabaseInitialization",Boolean.class);
        restrictionPart = SyntaxCheckerFactory.newInstance().create(1, 1, null, Boolean.class);
        name.setRestrictions(restrictionPart);
        name.add(false);
        properties.add(name);

        name = ParameterFactory.newInstance().create("DatabaseUser",String.class);
        restrictionPart = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(restrictionPart);
        name.add("Graph-RAT");
        properties.add(name);

        name = ParameterFactory.newInstance().create("DatabasePassword",String.class);
        restrictionPart = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(restrictionPart);
        name.add("");
        properties.add(name);

        name = ParameterFactory.newInstance().create("DatabaseClear",Boolean.class);
        restrictionPart = SyntaxCheckerFactory.newInstance().create(1, 1, null, Boolean.class);
        name.setRestrictions(restrictionPart);
        name.add(false);
        properties.add(name);


        map.put("MemGraph", new MemGraph());
        map.put("DerbyGraph",new DerbyGraph());
        map.put("Clique",new Clique());
        map.put("NullGraph",new NullGraph());
        map.put("PostgresqlGraph",new PostgresqlGraph());
        map.put("Tree",new Tree());
        map.put("UserIDList",new UserIDList());
        map.put("UserList",new UserList());
    }
    
    /**
     * Create a graph object without directly exposing the underlying types
     * 
     * Types of graphs are pulled from the "Graph" key with "GraphID" for their unique ID.
     * <ul>
     * <li>'Null'  - Creates a NullGraph object (no parameters)
     * <li>'User'  - Creates a UserList object (no parameters)
     * <li>'Derby' - Creates a DerbyGraph Object 
     *      <ul><li>'Directory' - location of the Derby DB
     *      <li>'GraphID' - id string of the Derby Graph
     *      <li>'GraphInitialize' - if present, the graph is initialized
     *      <li>'GraphClear' - if present, the graph is cleared
     *      </ul>
     * <li>'Postgresql' - Creates a PostgresqlGraph
     *      <ul><li>'GraphID' - graph ID, also name of database
     *      <li>'GraphUser' - user to connect to datbase with
     *      <li>'Initialize' - if present, the graph is initialized
     *      <li>'GraphClear' - if equal to true, the graph is reset to an initial default state
     *      </ul>
     * <li>'UserIDList' - Creates a UserIDList object (no parameters)
     * <li>any other value - Creates a MemGraph object
     *      <ul><li>'GraphOutput' - Location to store XML output.  No output created on close if this parameter is not set.
     *      <li>'Compression' - if present, GZip compression is used.
     *      </ul>
     * </ul>
     *
     * @param props parameters for the new graph
     *
     * @return newly created Graph object
     */
    public Graph create(Properties props){
        return create(null,props);
    }


    public Graph create(String id){
        return create(id, properties);
    }
    
    public Graph create(String id, Properties parameters){
        String linkClass = "";
        if ((parameters.get("GraphClass") != null) && (parameters.get("GraphClass").getParameterClass().getName().contentEquals(String.class.getName()))) {
            linkClass = (String) parameters.get("GraphClass").getValue().iterator().next();
        } else {
            linkClass = (String) properties.get("GraphClass").getValue().iterator().next();
        }
        
        Graph ret = null;
        if(map.containsKey(linkClass)){
            ret = map.get(linkClass).prototype();
        }else{
            ret = new MemGraph();
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Graph class '"+linkClass+"' not found - assuming MemGraph");
        }
        
        if (id == null) {
            if ((parameters.get("GraphID") != null) && (parameters.get("GraphID").getParameterClass().getName().contentEquals(String.class.getName()))) {
                id = (String) parameters.get("GraphID").getValue().iterator().next();
            } else {
                id = (String) properties.get("GraphID").getValue().iterator().next();
            }
        }
        
        ret.init(parameters);
        
        return ret;
    }
    
    public String[] getKnownGraphs(){
        LinkedList<String> items = new LinkedList<String>();
        items.addAll(map.keySet());
        return items.toArray(new String[]{});
    }
    
    public Parameter getClassParameter(){
        return properties.get("GraphClass");
    }
}

