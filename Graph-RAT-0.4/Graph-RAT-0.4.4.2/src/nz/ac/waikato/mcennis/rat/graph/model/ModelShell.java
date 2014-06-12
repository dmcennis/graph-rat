/*
 * ModelShell.java
 *
 * Created on 8 October 2007, 13:28
 *
 * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt)
 */

package nz.ac.waikato.mcennis.rat.graph.model;

/**
 * Shell class providing basic MVC support to models.  Models must define what the 
 * enums for each type of change and manually fire the change method, but this 
 * class handles all other bookkeeping.
 *
 * @author Daniel McEnnis
 * 
 */
public class ModelShell implements Model{
    
    protected java.util.LinkedList<Listener> listener = new java.util.LinkedList<Listener>();
    
    /** Creates a new instance of ModelShell */
    public ModelShell() {
    }
    
    /**
     * notifies all listeners of changes of the given type.
     * 
     * @param type kind of change that has occured, defined by each subclass.
     */
    protected void fireChange(int type, int argument){
        java.util.Iterator<Listener> it = listener.iterator();
        while(it.hasNext()){
            it.next().publishChange(this,type,argument);
        }
    }

    /**
     * Add a listener to this model
     * 
     */
    public void addListener(Listener l) {
        listener.add(l);
    }
}
