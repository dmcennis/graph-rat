/*

 * ParameterInternal.java

 *

 * Created on 14 September 2007, 13:26

 *

 * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt)

 */



package org.dynamicfactory.descriptors;


/**

 *


 * 

 * Extension of the SettableParameter and Parameter interface that allows parameter

 * owners to set all parts of the parameter without knowing what specific implementation

 * of parameter is used.

 * @author Daniel McEnnis
 */

public interface ParameterInternal extends Parameter{

    /**
     * Sets the name that this parameter will be accessed by
     * 
     * @param name name of the parameter
     */

    public void setType(String name);

    

    /**
     * Sets the Class that this parameter will hold.  This restriction can be a
     * hard restriction or a soft contract depending on the implementation.
     *
     * @param c Class expected for the parameter's value
     */

    public void setParameterClass(Class c);

    

    /**
     * Set this as a parameter whose changes means the application structure needs to 
     * be rebuilt.  This is of primary interest for GUI development knowing whether 
     * a parameter modification means that dependancy checks need to be performed 
     * again.
     * 
     * @param b is a structural parameter or not.
     */
    public void setStructural(boolean b);
    
    public void setDescription(String b);

    public void setRestrictions(SyntaxObject syntax);
    
    public SyntaxObject getRestrictions();
    
    public ParameterInternal duplicate();

}

