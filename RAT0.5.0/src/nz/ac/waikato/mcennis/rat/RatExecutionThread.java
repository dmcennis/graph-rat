/*

 * Copyright Daniel McEnnis, see license.txt

 */



package nz.ac.waikato.mcennis.rat;



import nz.ac.waikato.mcennis.rat.scheduler.Scheduler;



/**

 * Thread class for executing RAT schedulers outside of the main thread.

 * 

 * @author Daniel McEnnis

 */

public class RatExecutionThread extends Thread{



    Scheduler scheduler;

    

    public RatExecutionThread(Scheduler s){

        scheduler = s;

    }



    @Override

    public void run() {

        scheduler.start();

    }

    

   

}





