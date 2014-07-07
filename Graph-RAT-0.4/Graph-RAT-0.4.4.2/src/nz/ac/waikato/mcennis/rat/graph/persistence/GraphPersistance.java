/*
 * GraphPersistance.java
 *
 * Created on 21 August 2007, 13:48
 *
 * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt)
 */

package nz.ac.waikato.mcennis.rat.graph.persistence;

import org.mcennis.graphrat.graph.Graph;

/**
 *
 * @author Daniel McEnnis
 */
public interface GraphPersistance {
    
    public void storeGraph(Graph g) throws java.sql.SQLException;
    
    public void loadGraph(Graph g) throws java.sql.SQLException;
    
    public void start() throws java.sql.SQLException;
    
    public void stop() throws java.sql.SQLException;
}
