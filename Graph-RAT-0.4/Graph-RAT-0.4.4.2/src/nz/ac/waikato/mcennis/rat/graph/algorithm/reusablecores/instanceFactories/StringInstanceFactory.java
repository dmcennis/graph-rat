/**
 * Jul 23, 2008-9:39:37 PM
 * Copyright Daniel McEnnis, see license.txt
 */

package nz.ac.waikato.mcennis.rat.graph.algorithm.reusablecores.instanceFactories;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * Class for transforming a String into an Instance from a property of name 'name'.
 * This transformation creates a single nominal attribute with only the given string
 * as an option.  It is assumed that concatentation or normalization operations
 * will combine attributes into a more useful nominal attribute.
 * 
 * @author Daniel McEnnis
 */
public class StringInstanceFactory implements InstanceFactory{

    @Override
    public Instance transform(Object value, String name) {
        Instance ret = new Instance(1);
        FastVector string = new FastVector(1);
        string.addElement(value);
        FastVector attributes = new FastVector(1);
        attributes.addElement(new Attribute(name,string));
        Instances meta = new Instances(name, attributes,1);
        ret.setDataset(meta);
        ret.setValue(0, (String)value);
        return ret;
    }

}
