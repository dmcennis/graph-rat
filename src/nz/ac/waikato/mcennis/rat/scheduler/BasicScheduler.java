/*

 * BasicScheduler.java

 *

 * Created on 3 October 2007, 13:45

 *

 * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt)

 */



package nz.ac.waikato.mcennis.rat.scheduler;



import java.io.IOException;

import java.io.InputStream;

import java.util.Collection;

import java.util.Iterator;

import java.util.LinkedList;

import java.util.logging.Level;

import java.util.logging.Logger;

import nz.ac.waikato.mcennis.rat.dataAquisition.DataAquisition;

import nz.ac.waikato.mcennis.rat.graph.Graph;

import nz.ac.waikato.mcennis.rat.graph.algorithm.Algorithm;

import nz.ac.waikato.mcennis.rat.graph.model.Listener;

import nz.ac.waikato.mcennis.rat.graph.model.Model;

import nz.ac.waikato.mcennis.rat.graph.model.ModelShell;

import nz.ac.waikato.mcennis.rat.graph.query.GraphQuery;
import org.dynamicfactory.descriptors.*;


/**

 * Stripped down scheduler that directly executes each component without performing 

 * any sanity checks on the input.

 *

 * @author Daniel McEnnis

 * 

 */

public class BasicScheduler extends ModelShell implements Scheduler, Listener {

    

    PropertiesInternal properties = PropertiesFactory.newInstance().create();

    

    Graph graph;

    

    LinkedList<DataAquisition> aquisition = new LinkedList<DataAquisition>();

    

    LinkedList<Algorithm> algorithm = new LinkedList<Algorithm>();

    

    LinkedList<GraphQuery> graphQuery = new LinkedList<GraphQuery>();

    

    boolean stop = false;

    

    /** Creates a new instance of BasicScheduler */

    public BasicScheduler() {

        ParameterInternal name = ParameterFactory.newInstance().create("SchedulerClass",String.class);

        SyntaxObject syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);

        name.setRestrictions(syntax);

        name.add("BasicScheduler");

        properties.add(name);



    }



    @Override

    public void load(DataAquisition loader) {

        aquisition.add(loader);

    }



    @Override

    public void load(Algorithm algorithms, GraphQuery pattern) {

        algorithm.add(algorithms);

        graphQuery.add(pattern);

    }



    @Override

    public void set(Graph g) {

        graph = g;

    }

    

    @Override

    public Graph get(){

        return graph;

    }



    /**

     * FIXME: Not yet implemented.

     */

    public void load(InputStream config) throws IOException, Exception {

    }



    @Override

    public void start() {

        Logger.getLogger(this.getClass().getName()).log(Level.INFO,"Starting Aquisition");

        Iterator<DataAquisition> iterator = aquisition.iterator();

        int algorithmCount=1;

        while(iterator.hasNext() && !stop){

            DataAquisition data = iterator.next();

            Logger.getLogger(this.getClass().getName()).log(Level.INFO,"Aquisition: "+data.getParameter("name").getValue());

            fireChange(Scheduler.SET_ALGORITHM,algorithmCount++);

            data.set(graph);

            data.start();

            graph.commit();

        }

        aquisition = null;

        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Algorithms Starting");

        Iterator<Algorithm> algs = algorithm.iterator();

        Iterator<GraphQuery> query = graphQuery.iterator();

        while(algs.hasNext() && !stop){

            fireChange(Scheduler.SET_ALGORITHM,algorithmCount);

            Algorithm a = algs.next();

            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Algorithm "+a.getParameter("name").getValue()+" Starting");

            Collection<Graph> graphArray = (query.next()).execute(graph,null,null);

            Iterator<Graph> graphArrayIt = graphArray.iterator();

            while(graphArrayIt.hasNext()){

                Graph g = graphArrayIt.next();

                if(stop){

                    break;

                }

                Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Algorithm "+a.getParameter("name").getValue()+" Starting on graphID "+g.getID());

                a.execute(g);

                graph.commit();

            }

            a = null;

            algorithmCount++;

        }

        Logger.getLogger(this.getClass().getName()).log(Level.INFO,"Algorithms Completed");

        if(!stop){

            graph.commit();

            graph.close();

        }

        if(!stop){

            Logger.getLogger(this.getClass().getName()).log(Level.INFO,"Execution Completed");

        }else{

            Logger.getLogger(this.getClass().getName()).log(Level.INFO,"Cancellation Completed");

        }

    }



    /**

     * FIXME: Not yet implemented.

     */

    public void cancel() {

        stop = true;

    }

    

    /**

     * FIXME: Not yet implemented

     */

    public void init(Properties props){

        

    }



    public Properties getParameter() {

        return properties;

    }



    public void publishChange(Model m, int type, int argument) {

        if((type==Scheduler.SET_ALGORITHM_COUNT)||

                (type==Scheduler.SET_ALGORITHM_PROGRESS)||

                (type==Scheduler.SET_GRAPH_COUNT)||

                (type==Scheduler.SET_GRAPH_PROGRESS)){

            fireChange(type,argument);

        }

    }

    

    public Scheduler prototype(){

        return new BasicScheduler();

    }



}

