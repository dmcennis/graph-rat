/*

 * Created 21-02-08

 * Copyright Daniel McEnnis, see license.txt

 */



package nz.ac.waikato.mcennis.rat.gui;



import javax.swing.SwingUtilities;

import org.dynamicfactory.model.Listener;

import org.dynamicfactory.model.Model;

import nz.ac.waikato.mcennis.rat.scheduler.Scheduler;



/**

 * Controller for recieving updates during execution and

 * then scheduling them on the GUI thread for updating the GUI.

 * @author Daniel McEnnis

 */

public class SwingUpdater implements Listener{



    RatGui gui;

    DoChange status = new DoChange();

    

    /**

     * Create a new updater linked to the GUI

     * @param gui GUI to publishn to.

     */

    public SwingUpdater(RatGui gui){

        this.gui = gui;

    }

    

    @Override

    public void publishChange(Model m, int type, int argument) {

        switch(type){

            case Scheduler.SET_ALGORITHM:

                status.algorithmValue=argument;

                status.algorithmProgressValue=0;

                status.graphProgressValue=0;

                SwingUtilities.invokeLater(status);

                break;

            case Scheduler.SET_ALGORITHM_COUNT:

                status.algorithmProgressSize=argument;

                status.algorithmProgressValue=0;

                SwingUtilities.invokeLater(status);

                break;

            case Scheduler.SET_ALGORITHM_PROGRESS:

                status.algorithmProgressValue=argument;

                SwingUtilities.invokeLater(status);

                break;

            case Scheduler.SET_GRAPH_COUNT:

                status.graphProgressSize=argument;

                status.graphProgressValue=0;

                SwingUtilities.invokeLater(status);

                break;

            case Scheduler.SET_GRAPH_PROGRESS:

                status.graphProgressValue=argument;

                SwingUtilities.invokeLater(status);

                break;

            default:

        }

            



    }



    /**

     * Class object representing a message containing info about the current

     * state of the application's execution.

     */

    public class DoChange implements Runnable{

        int algorithmValue=0;

        int algorithmProgressSize=0;

        int algorithmProgressValue=0;

        int graphProgressSize=0;

        int graphProgressValue=0;

        

        /**

         * Create an object that has initial state of zero progress on the data aquisition object

         */

        public DoChange(){}



        /**

         * when executed, load the state into the gui.

         */

        public void run() {

            

            gui.doUpdate(this);

        }

    }

}

