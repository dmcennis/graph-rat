/*

 * SchedulerFactory.java

 *

 * Created on 7 October 2007, 15:54

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



import java.util.logging.Level;

import java.util.logging.Logger;

import org.dynamicfactory.AbstractFactory;
import org.dynamicfactory.descriptors.*;

/**

 * Class for creating a scheduler.

 *

 * @author Daniel McEnnis

 * 

 */

public class SchedulerFactory extends AbstractFactory<Scheduler>{

    

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

        ParameterInternal name = ParameterFactory.newInstance().create("SchedulerClass",String.class);

        SyntaxObject syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);

        name.setRestrictions(syntax);

        name.add("BasicScheduler");

        properties.add(name);

        

        map.put("BasicScheduler",new BasicScheduler());

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
    @Override

    public Scheduler create(Properties props){

        return create(null,props);

    }

    public String[] getKnownSchedulers(){

        return new String[]{"Basic Scheduler"};

    }

    


    

    public Scheduler create(String name){

        return create(name,properties);

    }

    

    public Scheduler create(String classType, Properties parameters){

        if(classType != null){

        if ((parameters.get("SchedulerClass") != null) && (parameters.get("SchedulerClass").getParameterClass().getName().contentEquals(String.class.getName()))) {

            classType = (String) parameters.get("SchedulerClass").getValue().iterator().next();

        } else {

            classType = (String) properties.get("SchedulerClass").getValue().iterator().next();

        }

        }

        Scheduler ret = null;

        if(map.containsKey(classType)){

            ret = (Scheduler)map.get(classType).prototype();

        }else{

            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE,"Scheduler class '"+classType+"' not found - assuming BasicScheduler");

            ret = new BasicScheduler();

        }

        ret.init(parameters);

        return ret;

    }

    

    @Override

    public boolean check(Properties parameters) {

        String classType = "";

        if ((parameters.get("SchedulerClass") != null) && (parameters.get("SchedulerClass").getParameterClass().getName().contentEquals(String.class.getName()))) {

            classType = (String) parameters.get("SchedulerClass").getValue().iterator().next();

        } else {

            classType = (String) properties.get("SchedulerClass").getValue().iterator().next();

        }



        if(map.containsKey(classType)){

            return map.get(classType).getParameter().check(parameters);

        }else{

            Logger.getLogger(this.getClass().getName()).log(Level.INFO,"Scheduler '"+classType+"' does not exist");

            return false;

        }

    }

    

    @Override

    public Properties getParameter(){

        String classType = (String) properties.get("SchedulerClass").getValue().iterator().next();

        if(map.containsKey(classType)){

            return map.get(classType).getParameter();

        }else{

            return properties;

        }

    }



    @Override

    public Parameter getClassParameter() {

        return properties.get("SchedulerClass");

    }

    

}

