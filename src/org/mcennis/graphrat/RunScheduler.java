/*

 * RunScheduler.java

 *

 * Created on 7 October 2007, 16:24

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
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with GraphRAT.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.mcennis.graphrat;



import java.io.File;

import org.mcennis.graphrat.parser.ParserFactory;

import org.mcennis.graphrat.parser.Parser;

import org.mcennis.graphrat.scheduler.Scheduler;
import org.dynamicfactory.descriptors.Properties;
import org.dynamicfactory.descriptors.PropertiesFactory;


/**

 * Executes RAT by reading in an XML file - scheduler.xml by default.  This

 * file is then parsed to determine which components should be put together, and

 * then executes the resulting application.  Parameters are included in a 

 * parameter followed by a name-value pair.

 *

 * @author Daniel McEnnis

 * 

 */

public class RunScheduler {

    

    /** Creates a new instance of RunScheduler */

    public RunScheduler() {

    }

    

    /**

     * Loads the scheduler specified as the first argument or 'scheduler.xml' if

     * nor arguments are provided. 

     * 

     * @param args array containing the application arguments

     * @throws Exception

     */

    public static void main(String[] args) throws Exception{

        Scheduler scheduler = null;

        File config = null;

        if(args.length == 0){

            config = new File("scheduler.xml");

        }else{

            config = new File(args[0]);

            System.out.println("Using Configuration File '"+args[0]+"'");

        }

        if(config.exists()){

            

        

        java.io.FileInputStream input = new java.io.FileInputStream(config);

        if(input == null){

            System.out.println("WARNING input stream is returning null");

        }

        Properties props = PropertiesFactory.newInstance().create();

        props.set("ParserClass","Scheduler");

        Parser parser = ParserFactory.newInstance().create(props);

        parser.parse(input,config.getAbsolutePath());

        

        scheduler = (Scheduler)parser.get();

        scheduler.start();

        }else{

            System.err.println("File '"+config.getAbsolutePath()+"' does not exist");

        }

    }

}

