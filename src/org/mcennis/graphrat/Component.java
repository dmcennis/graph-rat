/*

 * Component.java

 *

 * Created on October 4, 2007, 6:08 AM

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



package org.mcennis.graphrat;



import java.util.List;

import org.mcennis.graphrat.descriptors.IODescriptor;

import org.dynamicfactory.model.Model;
import org.dynamicfactory.descriptors.Parameter;
import org.dynamicfactory.descriptors.Properties;


/**

 * This class describes the self-documenting features of the different components

 * that make up the toolkit.  Every component describes what it creates, what it

 * generates, and what kinds of parameters it accepts

 *

 * @author Daniel McEnnis

 * 

 */

public interface Component extends Model{



    /**

     * The input type describes all the different kinds of graph objects that 

     * are utilized (and hence required) by this object.  This result is only 

     * guaranteed to be fixed if structural parameters are not modified.  This

     * is an empty array if there is no input.

     * 

     * @see org.mcennis.graphrat.descriptors.IODescriptor

     *

     * @return IODescriptor array for this component

     */

    public List<IODescriptor> getInputType();

    

    /**

     * The output type describes all the different kinds of graph objects that

     * are created during the execution of this algorithm.  The result is only

     * guaranteed to be fixed if structural parameters are not modified. This is

     * an empty array if there is no output.

     * 

     * @see org.mcennis.graphrat.descriptors.IODescriptor

     * 

     * @return IODescriptor array for this component

     */

    public List<IODescriptor> getOutputType();

    

    /**

     * List of all parameters this component accepts.  Each parameter also has a

     * distinct key-name used when initializing the object using the init method.

     * If there are no parameters, null is returned.

     * 

     * @return read-only array of Parameters

     */

    public Properties getParameter();

    

    /**

     * Returns the specific parameter identified by its key-name.  If no 

     * parameter is found with this key-name, null is returned.

     * 

     * @param param key-name of the parameter

     * @return named parameter

     */

    public Parameter getParameter(String param);

    

    /**

     * Returns settable (i.e. editable while running) parameters. If none exist,

     * null is returned.

     * 

     * @return array of settable parameters

     */

//    public SettableParameter[] getSettableParameter();

    

    /**

     * Return the settable parameter namede by this key-name.  If this parameter is not found

     * or is not settable, null is returned.

     * 

     * @param param key-name of the parameter

     * @return named settable parameter

     */

//    public SettableParameter getSettableParameter(String param);

    

    

    /**

     * Initialize the object by setting parameters using the given properties map

     * @param map map of the given properties naming parameters and their values in a string

     */

    public void init(Properties map);

    

    public Component prototype();

}

