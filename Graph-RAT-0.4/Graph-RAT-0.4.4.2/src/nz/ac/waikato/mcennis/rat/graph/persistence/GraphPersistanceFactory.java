/*
 * GraphPersistanceFactory.java
 *
 * Created on 21 August 2007, 13:53
 *
 * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt)
 */

package nz.ac.waikato.mcennis.rat.graph.persistence;

/**
 *
 * @author Daniel McEnnis
 */
public class GraphPersistanceFactory {
    
    static GraphPersistanceFactory instance = null;
    
    static GraphPersistanceFactory newInstance(){
        if(instance==null){
            instance = new GraphPersistanceFactory();
        }
        return instance;
    }
    
    /** Creates a new instance of GraphPersistanceFactory */
    private GraphPersistanceFactory() {
    }
    
    public GraphPersistance create(java.util.Properties props){
        return new DerbyPersistance();
    }
    
}
