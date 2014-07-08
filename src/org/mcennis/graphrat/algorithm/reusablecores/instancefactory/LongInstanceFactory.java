/**
 * Jul 23, 2008-9:42:32 PM
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
 * Class for transforming Long property values into an Instance.  Cretaes a single
 * numeric attribute with the long value transformed into a double as its value.
 * 
 * @author Daniel McEnnis
 */
public class LongInstanceFactory implements InstanceFactory{

    @Override
    public Instance transform(Object value, String name) {
        Instance ret = new Instance(1);
        FastVector attributes = new FastVector();
        attributes.addElement(new Attribute(name));
        Instances meta = new Instances(name,attributes,1);
        ret.setDataset(meta);
        ret.setValue(0,((Long)value).longValue());
        return ret;
    }

    public LongInstanceFactory prototype(){
        return new LongInstanceFactory();
    }
}
