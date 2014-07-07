/**
 * Jul 23, 2008-7:18:06 PM
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

import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * This class is used when an approprite Object specific factory does not exist.
 * It always returns an Instance wihout attributes.
 * 
 * @author Daniel McEnnis
 */
public class DefaultInstanceFactory implements InstanceFactory{

    @Override
    public Instance transform(Object value, String name) {
        FastVector attributes = new FastVector(0);
        Instances meta = new Instances(name,attributes,1);
        Instance data = new Instance(0);
        data.setDataset(meta);
        return data;
    }
    
    public DefaultInstanceFactory prototype(){
        return new DefaultInstanceFactory();
    }

}
