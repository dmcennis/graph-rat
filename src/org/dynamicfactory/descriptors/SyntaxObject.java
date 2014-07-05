/**
 * SyntaxObject
 * Created Jan 25, 2009 - 7:38:30 PM
 * Copyright Daniel McEnnis, see license.txt
 */
package org.dynamicfactory.descriptors;

import nz.ac.waikato.mcennis.rat.graph.query.property.PropertyQuery;

/**
 *
 * @author Daniel McEnnis
 */
public interface SyntaxObject extends SyntaxChecker{

    public void setRestriction(String type, PropertyRestriction restriction);
    
    public void setDefaultRestriction(PropertyRestriction restriction);
    
    public void setMinCount(int minCount);
    
    public void setMaxCount(int maxCount);
 
    public void setTest(PropertyQuery test);
    
    public PropertyQuery getTest();
    
    public void setClassType(Class classType);    
    
    public SyntaxObject duplicate();
}
