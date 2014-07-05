/*

 * RunScheduler.java

 *

 * Created on 7 October 2007, 16:24

 *

 * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt)

 */

package nz.ac.waikato.mcennis.rat;



import java.io.File;

import nz.ac.waikato.mcennis.rat.parser.ParserFactory;

import nz.ac.waikato.mcennis.rat.parser.Parser;

import nz.ac.waikato.mcennis.rat.scheduler.Scheduler;
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

