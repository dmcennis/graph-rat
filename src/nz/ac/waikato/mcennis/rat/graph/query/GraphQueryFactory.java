/**
 * GraphQueryFactory
 * Created Jan 26, 2009 - 8:57:14 PM
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.graph.query;

import java.util.logging.Level;
import java.util.logging.Logger;
import nz.ac.waikato.mcennis.rat.AbstractFactory;
import nz.ac.waikato.mcennis.rat.graph.query.graph.GraphByLink;
import nz.ac.waikato.mcennis.rat.graph.query.graph.GraphByActor;
import nz.ac.waikato.mcennis.rat.graph.query.graph.GraphByProperty;
import nz.ac.waikato.mcennis.rat.graph.query.graph.GraphByID;
import nz.ac.waikato.mcennis.rat.graph.query.graph.GraphByLevel;
import nz.ac.waikato.mcennis.rat.graph.query.graph.AllGraphs;
import nz.ac.waikato.mcennis.rat.graph.query.graph.AndGraphQuery;
import nz.ac.waikato.mcennis.rat.graph.query.graph.NullGraphQuery;
import nz.ac.waikato.mcennis.rat.graph.query.graph.OrGraphQuery;
import nz.ac.waikato.mcennis.rat.graph.query.graph.XorGraphQuery;
/**
 *
 * @author Daniel McEnnis
 */
public class GraphQueryFactory extends AbstractFactory<GraphQuery>{

    static GraphQueryFactory instance = null;
    
    public static GraphQueryFactory newInstance(){
        if(instance == null){
            instance = new GraphQueryFactory();
        }
        return instance;
    }
    
    private GraphQueryFactory(){
        ParameterInternal name = ParameterFactory.newInstance().create("QueryClass", String.class);
        SyntaxObject check = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(check);
        name.add("NullQuery");
        properties.add(name);
        
        map.put("AndQuery",new AndGraphQuery());
        map.put("NullQuery",new NullGraphQuery());
        map.put("OrQuery",new OrGraphQuery());
        map.put("XorQuery",new XorGraphQuery());
        map.put("GraphByLink",new GraphByLink());
        map.put("GraphByActor",new GraphByActor());
        map.put("GraphByProperty",new GraphByProperty());
        map.put("GraphByID",new GraphByID());
        map.put("GraphByLevel",new GraphByLevel());
	map.put("AllGraphs",new AllGraphs());
    }
    
    @Override
    public GraphQuery create(Properties props) {
        return create(null,props);
    }
    
    public GraphQuery create(String id){
        return create(id,properties);
    }
    
    public GraphQuery create(String id, Properties parameters){
        String classType = "";
        if ((parameters.get("QueryClass") != null) && (parameters.get("QueryClass").getParameterClass().getName().contentEquals(String.class.getName()))) {
            classType = (String) parameters.get("QueryClass").getValue().iterator().next();
        } else {
            classType = (String) properties.get("QueryClass").getValue().iterator().next();
        }

        if(map.containsKey(classType)){
            return map.get(classType).prototype();
        }else{
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE,"GraphQuery class '"+classType+"' not found - assuming NullQuery<Graph>");
            return new NullGraphQuery();
        }
    }

    public Parameter getClassParameter(){
        return properties.get("QueryClass");
    }
}
