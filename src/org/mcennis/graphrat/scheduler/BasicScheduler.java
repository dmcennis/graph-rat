/*

 * BasicScheduler.java

 *

 * Created on 3 October 2007, 13:45

 *

 *
 *   This file is part of GraphRAT.
 *
 *   GraphRAT is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   GraphRAT is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with GraphRAT.  If not, see <http://www.gnu.org/licenses/>.

 */



package org.mcennis.graphrat.scheduler;



import java.io.IOException;

import java.io.InputStream;

import java.util.Collection;

import java.util.Iterator;

import java.util.LinkedList;

import java.util.logging.Level;

import java.util.logging.Logger;

import org.mcennis.graphrat.dataAquisition.DataAquisition;

import org.mcennis.graphrat.graph.Graph;

import org.mcennis.graphrat.algorithm.Algorithm;

import org.dynamicfactory.model.Listener;

import org.dynamicfactory.model.Model;

import org.dynamicfactory.model.ModelShell;

import org.mcennis.graphrat.query.GraphQuery;
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

