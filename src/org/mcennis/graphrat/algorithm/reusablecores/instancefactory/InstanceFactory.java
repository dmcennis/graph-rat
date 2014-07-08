/**
 * Created Jul 23, 2008-7:11:39 PM
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

package org.mcennis.graphrat.algorithm.reusablecores.instancefactory;

import weka.core.Instance;

/**
 *
 * Interface for transforming Properties into Instance Objects.
 * 
 * @author Daniel McEnnis
 */
public interface InstanceFactory {
    /**
     * Transform an object into an Instance where the name of the dataset and the
     * names of the attributes are derived from the name provided.
     * 
     * @param value Property value to be transformed
     * @param name string utilized to derive a dataset name and attribute names.
     * @return Instance representing this object
     */
    public Instance transform(Object value, String name);
    
    public InstanceFactory prototype();
}
