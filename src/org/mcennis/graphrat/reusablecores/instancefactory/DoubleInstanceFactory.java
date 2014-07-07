/**
 * Jul 23, 2008-9:40:24 PM
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
 * Class for transforming Double property values into an Instance.  Cretaes a single
 * numeric attribute with the double value as its value.
 * 
 * @author Daniel McEnnis
 */
public class DoubleInstanceFactory implements InstanceFactory{

    @Override
    public Instance transform(Object value, String name) {
        Instance ret = new Instance(1);
        FastVector attributes = new FastVector();
        attributes.addElement(new Attribute(name));
        Instances meta = new Instances(name,attributes,1);
        ret.setDataset(meta);
        ret.setValue(0,((Double)value).doubleValue());
        return ret;
    }

    public DoubleInstanceFactory prototype(){
        return new DoubleInstanceFactory();
    }
}
