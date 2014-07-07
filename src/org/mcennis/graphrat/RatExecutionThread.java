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
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with GraphRAT.  If not, see <http://www.gnu.org/licenses/>.
 */



package org.mcennis.graphrat;



import org.mcennis.graphrat.scheduler.Scheduler;



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





