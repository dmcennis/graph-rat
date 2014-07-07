/*

 * Created 13/06/2008-14:23:45

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



import java.util.logging.Handler;

import java.util.logging.LogRecord;

import java.util.logging.SimpleFormatter;

import javax.swing.JTextArea;



/**

 * Logging handler for logging messages to a JTextArea object 

 * as if it were standard out.

 * @author Daniel McEnnis

 */

public class ScreenHandler extends Handler{



    JTextArea outputScreen = null;

    

    /**

     * Create a new handler with a default SimpleFormatter object

     */

    public ScreenHandler(){

        super.setFormatter(new SimpleFormatter());

    }

    

    @Override

    public void publish(LogRecord record) {

        if(outputScreen != null){

            if(record.getThrown() == null){

                outputScreen.append(record.getMessage());

                outputScreen.append("\n");

            }else{

                outputScreen.append(this.getFormatter().format(record));

            }

        }

    }



    @Override

    public void flush() {

        ; // deliberate no-op.

    }



    @Override

    public void close() throws SecurityException {

        if(outputScreen != null){

            outputScreen.setText("");

        }

    }

    

    /**

     * Link this object to a JTextArea

     * @param o JTextArea object to log to

     */

    public void setOutputScreen(JTextArea o){

        outputScreen = o;

    }



}

