/*
 * SchedulerFactory.java
 *
 * Created on 7 October 2007, 15:54
 *
 * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt)
 */

package org.mcennis.graphrat.scheduler;

/**
 * Class for creating a scheduler.
 *
 * @author Daniel McEnnis
 * 
 */
public class SchedulerFactory {
    
    private static SchedulerFactory instance = null;
    
    /**
     * return a reference to a singleton SchedulerFactory object.
     * 
     * @return new reference to a singleton SchedulerFactory.
     */
    public static SchedulerFactory newInstance(){
        if(instance == null){
            instance = new SchedulerFactory();
        }
        return instance;
    }
    
    /** Creates a new instance of SchedulerFactory */
    private SchedulerFactory() {
    }
    
    /**
     * Create a new Scheduler
     * 
     * The class is created using the 'scheduler' key.  Currently, only the basic
     * scheduler is implemented and is returned regardless of the value.
     * 
     * @param props parameters for constructing a scheduler
     * @return newly constructed Scheduler
     */
    public Scheduler create(java.util.Properties props){
        Scheduler ret = null;
//        if(props.getProperty("scheduler").compareToIgnoreCase("input checked")==0){
//            ret = new InputCheckedScheduler();
//            ret.init(props);
//        }else{
            ret = new BasicScheduler();
            ret.init(props);
//        }
        return ret;
    }
    
    public String[] getKnownSchedulers(){
        return new String[]{"Basic Scheduler"};
    }
}
