/*
 * BuildGraph.java
 *
 * Created on 7 October 2007, 13:09
 *
 * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt)
 */

package nz.ac.waikato.mcennis.rat;

import java.util.Properties;
import nz.ac.waikato.mcennis.rat.dataAquisition.FileReader2Pass;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import nz.ac.waikato.mcennis.rat.graph.GraphFactory;
import nz.ac.waikato.mcennis.rat.graph.algorithm.Algorithm;
import nz.ac.waikato.mcennis.rat.graph.algorithm.AlgorithmFactory;

/**
 *
 * @author Daniel McEnnis
 */
public class BuildGraph {
    
    /** Creates a new instance of BuildGraph */
    public BuildGraph() {
    }
    
    
    public static void main(String[] args) throws Exception{
        Properties props = new Properties();
        props.setProperty("Graph","MemGraph");
        props.setProperty("GraphOutput","/research/test.xml");
//        props.setProperty("Compression","true");
        Graph graph = GraphFactory.newInstance().create(props);
        
        FileReader2Pass reader = new FileReader2Pass();
        props.setProperty("name","File Reader 2 Pass");
        props.setProperty("foafDirectory","/research/data/backup-21-06-07");
        props.setProperty("anonymizing","true");
        reader.init(props);
        reader.set(graph);
        reader.start();
        graph = reader.get();
        Algorithm[] algorithms = new Algorithm[4];
        
        props.clear();
        props.setProperty("algorithm","AddBasicInterestLink");
        props.setProperty("adjustorPropertyType","");
        algorithms[0] = AlgorithmFactory.newInstance().create(props);
        
        props.clear();
        props.setProperty("algorithm","AddMusicReferences");
        props.setProperty("decider","basic");
        props.setProperty("artistListLocation","/research/artist.txt");
        algorithms[1] = AlgorithmFactory.newInstance().create(props);
        
        props.clear();
        props.setProperty("algorithm","AddMusicLinks");
        props.setProperty("adjustorPropertyType","");
        algorithms[2] = AlgorithmFactory.newInstance().create(props);
        
        props.clear();
        props.setProperty("algorithm","AddMusicRecommendations");
        props.setProperty("adjustorPropertyType","");
        props.setProperty("evaluation","true");
        algorithms[3] = AlgorithmFactory.newInstance().create(props);
        
        for(int i=0;i<algorithms.length;++i){
            System.out.println("Executing Algorithm '"+algorithms[i].getParameter("name").getValue()+"'");
            algorithms[i].execute(graph);
        }
       
        graph.commit();
        graph.close();
    }
}
