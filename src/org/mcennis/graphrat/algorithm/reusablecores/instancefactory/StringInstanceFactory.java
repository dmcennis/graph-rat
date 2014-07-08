/**
 * Jul 23, 2008-9:39:37 PM
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

package org.mcennis.graphrat.algorithm.reusablecores.instancefactory;

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

    public StringInstanceFactory prototype(){
        return new StringInstanceFactory();
    }
}
