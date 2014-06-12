/*
 * Listener.java
 *
 * Created on 8 October 2007, 13:24
 *
 * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt)
 */

package nz.ac.waikato.mcennis.rat.graph.model;

/**
 * Class for implementing a lister interface for View part of MVC.  To be 
 * replaced by standard JavaBeans interface
 *
 * @author Daniel McEnnis
 * 
 */
public interface Listener {
    /**
     * Recieve and update of the given type.
     * @param m model that changed
     * @param type type of change that has occured
     */
    public void publishChange(Model m, int type, int argument);
}
