/*
 * CheckDBDerby.java
 *
 * Created on 22 August 2007, 11:46
 *
 * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt)
 */

package nz.ac.waikato.mcennis.rat;

import nz.ac.waikato.mcennis.rat.graph.Graph;
import nz.ac.waikato.mcennis.rat.graph.GraphFactory;
import nz.ac.waikato.mcennis.rat.graph.actor.Actor;
import org.dynamicfactory.property.Property;

/**
 *
 * @author Daniel McEnnis
 */
public class CheckDBDerby {
    
    /** Creates a new instance of CheckDBDerby */
    public CheckDBDerby() {
    }

    public static void main(String[] args) throws Exception{
        java.util.Properties props = new java.util.Properties();
        props.setProperty("Graph","Derby");
        props.setProperty("Directory","/Users/mcennis/database");
        props.setProperty("GraphID","LiveJournalTest");
        Graph graph = GraphFactory.newInstance().create(props);
        
        Actor[] actor = graph.getActor("User");
        for(int i=0;i<actor.length;++i){
            Property[] p =  actor[i].getProperty();
            if( p!= null){
                System.out.println(p.length);
            }else{
                System.out.println("0");
            }
        }
    }
}
