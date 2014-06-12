/**
 * PropertiesInternal
 * Created Jan 25, 2009 - 7:48:09 PM
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.graph.descriptors;

import java.util.Collection;
import nz.ac.waikato.mcennis.rat.graph.descriptors.ParameterInternal;

/**
 *
 * @author Daniel McEnnis
 */
public interface PropertiesInternal extends Properties{

    void add(ParameterInternal parameter);

    void add(String type,Object value);
        
    void remove(String type);
    
    void replace(Parameter type);

    void setDefaultRestriction(SyntaxObject restriction);

    SyntaxObject getDefaultRestriction();
    
    PropertiesInternal duplicate();

    ParameterInternal get(String string);

    PropertiesInternal merge(Properties right);
}
