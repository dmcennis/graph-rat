/**
 * QueryFactory
 * Created Jan 5, 2009 - 11:13:55 PM
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.graph.query.property;

import nz.ac.waikato.mcennis.rat.graph.property.Property;
import nz.ac.waikato.mcennis.rat.XMLParserObject.State;
        
/**
 *
 * @author Daniel McEnnis
 */
public interface PropertyQuery extends Comparable{

    public boolean execute(Property property);
    
    public void exportQuery(java.io.Writer writer);
    
    public void importQuery(java.io.Reader reader);
        
    public State buildingStatus();
    
    public PropertyQuery prototype();
    
}
