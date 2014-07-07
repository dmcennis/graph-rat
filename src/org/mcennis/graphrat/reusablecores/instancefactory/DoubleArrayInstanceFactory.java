/**
 * Jul 23, 2008-9:42:03 PM
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
package org.mcennis.graphrat.reusablecores.instancefactory;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * Class for transforming double[] property values into an Instance.  If the array
 * is null or of zero length, an Instance is returned with no attributes.  If the 
 * array has length greater than zero, each element of the array becomes an attribute
 * named as 'name':'index' with the appropriate array value as its value.
 * 
 * @author Daniel McEnnis
 */
public class DoubleArrayInstanceFactory implements InstanceFactory {

    @Override
    public Instance transform(Object value, String name) {
        Instance ret = new Instance(0);
        ret.setDataset(new Instances(name, new FastVector(0), 1));
        double[] data = (double[]) value;
        if ((data != null) && (data.length > 0)) {
            FastVector attributes = new FastVector(data.length);
            ret = new Instance(data.length);
            for (int i = 0; i < attributes.size(); ++i) {
                attributes.addElement(new Attribute(name + ":" + i));
                ret.setValue(i, data[i]);
            }
            ret.setDataset(new Instances(name, attributes, 1));
        }
        return ret;
    }

    public DoubleArrayInstanceFactory prototype(){
        return new DoubleArrayInstanceFactory();
    }
}
