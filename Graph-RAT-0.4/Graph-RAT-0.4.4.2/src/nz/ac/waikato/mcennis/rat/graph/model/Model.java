/*
 * Model.java
 *
 * Created on 8 October 2007, 13:23
 *
 * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt)
 */

package nz.ac.waikato.mcennis.rat.graph.model;

/**
 * Interface for implementing MVC.  TO be replaced by the standard JavaBeans
 *
 * @author Daniel McEnnis
 * 
 */
public interface Model {
    
    /**
     * Add a listener to recieve changes from this object
     * 
     * @param l object that wants to listens to changes
     */
    public void addListener(Listener l);
    
}
