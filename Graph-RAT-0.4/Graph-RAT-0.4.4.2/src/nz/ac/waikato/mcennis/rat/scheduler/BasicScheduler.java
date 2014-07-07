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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import nz.ac.waikato.mcennis.rat.dataAquisition.DataAquisition;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import nz.ac.waikato.mcennis.rat.graph.algorithm.Algorithm;
import org.dynamicfactory.model.Listener;
import org.dynamicfactory.model.Model;
import org.dynamicfactory.model.ModelShell;

/**
 * Stripped down scheduler that directly executes each component without performing 
 * any sanity checks on the input.
 *
 * @author Daniel McEnnis
 * 
 */
public class BasicScheduler extends ModelShell implements Scheduler, Listener {
    
    Graph graph;
    
    LinkedList<DataAquisition> aquisition = new LinkedList<DataAquisition>();
    
    LinkedList<Algorithm> algorithm = new LinkedList<Algorithm>();
    
    LinkedList<Pattern> graphIDPattern = new LinkedList<Pattern>();
    
    boolean stop = false;
    
    /** Creates a new instance of BasicScheduler */
    public BasicScheduler() {
    }

    @Override
    public void load(DataAquisition loader) {
        aquisition.add(loader);
    }

    @Override
    public void load(Algorithm algorithms, Pattern pattern) {
        algorithm.add(algorithms);
        graphIDPattern.add(pattern);
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
        Iterator<Pattern> pattern = graphIDPattern.iterator();
        while(algs.hasNext() && !stop){
            fireChange(Scheduler.SET_ALGORITHM,algorithmCount);
            Algorithm a = algs.next();
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Algorithm "+a.getParameter("name").getValue()+" Starting");
            Graph[] graphArray = graph.getGraphs(pattern.next());
            for(int i=0;i<graphArray.length;++i){
                if(stop){
                    break;
                }
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Algorithm "+a.getParameter("name").getValue()+" Starting on graphID "+graphArray[i].getID());
                a.execute(graphArray[i]);
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
    public void init(java.util.Properties props){
        
    }

    public Parameter[] getParameters() {
        return new Parameter[]{};
    }

    public void publishChange(Model m, int type, int argument) {
        if((type==Scheduler.SET_ALGORITHM_COUNT)||
                (type==Scheduler.SET_ALGORITHM_PROGRESS)||
                (type==Scheduler.SET_GRAPH_COUNT)||
                (type==Scheduler.SET_GRAPH_PROGRESS)){
            fireChange(type,argument);
        }
    }
}
