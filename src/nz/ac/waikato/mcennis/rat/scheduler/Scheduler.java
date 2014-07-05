/*
 * Scheduler.java
 *
 * Created on 14 September 2007, 13:33
 *
 * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt)
 */
package nz.ac.waikato.mcennis.rat.scheduler;

import java.io.IOException;
import java.io.InputStream;
import nz.ac.waikato.mcennis.rat.dataAquisition.DataAquisition;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import nz.ac.waikato.mcennis.rat.graph.algorithm.Algorithm;
import nz.ac.waikato.mcennis.rat.graph.model.Listener;
import nz.ac.waikato.mcennis.rat.graph.model.Model;
import nz.ac.waikato.mcennis.rat.graph.query.GraphQuery;
import nz.ac.waikato.mcennis.rat.parser.ParsedObject;

/**
 * Interface for a blackboard-style scheduler. It is created by a XML configuration
 * language and executes the dataaquisition layer first, followed by each algorithm in order.
 * Scheudler implementations that differ from this assumption must explicitly state so in their
 * documentation.
 * 
 * @author Daniel McEnnis
 * 
 */
public interface Scheduler extends ParsedObject, Model, Listener{

    /**
     * Set of message types for listeners of this scheduler
     */
    public static final int START = 0;
    public static final int INITIALIZE = 1;
    public static final int SET_ALGORITHM=2;
    public static final int SET_GRAPH_COUNT=3;
    public static final int SET_ALGORITHM_COUNT=4;
    public static final int SET_ALGORITHM_PROGRESS=5;
    public static final int SET_GRAPH_PROGRESS=6;
    public static final int ALGORITHM_DONE=7;
    public static final int DONE = 8;

    /**
     * set the data-aquisition component to be executed
     * @param loader acquisition method.
     */
    public void load(DataAquisition loader);

    /**
     * set an algorithm to be executed.
     * 
     * @param algorithm array of algorithms to execute against the graph
     */
    public void load(Algorithm algorithm, GraphQuery graphQuery);

    /**
     * Set the graph that will serve as the data structure to be executed against
     * 
     * @param g graph that will be the data structure
     */
    public void set(Graph g);

    /**
     * Return the graph produced and altered by the data aquisition and algorithms.
     * 
     * @return graph associated with this scheduler
     */
    public Graph get();

    /**
     * load this scheduler using the given XML file.
     * 
     * @param config 
     * @throws java.io.IOException 
     * @throws Exception
     */
    public void load(InputStream config) throws IOException, Exception;

    /**
     * Begin execution
     */
    public void start();

    /**
     * Cancel execution and return immediately
     */
    public void cancel();

    /**
     * Initiliaize this scheduler with the following parameters. Called by SchedulerFactory.
     * 
     * @param props 
     */
    public void init(Properties props);
    
    /**
     * Return a machine-readable description of the parameters this scheduler has.
     * @return parameters for this scheduler.
     */
    public Properties getParameter();
    
    public Scheduler prototype();
}

