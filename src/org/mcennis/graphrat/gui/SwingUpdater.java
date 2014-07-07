/*

 * Created 21-02-08

 * Copyright Daniel McEnnis, see license.txt

 */

/*
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


package org.mcennis.graphrat.gui;



import javax.swing.SwingUtilities;

import org.dynamicfactory.model.Listener;

import org.dynamicfactory.model.Model;

import org.mcennis.graphrat.scheduler.Scheduler;



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

