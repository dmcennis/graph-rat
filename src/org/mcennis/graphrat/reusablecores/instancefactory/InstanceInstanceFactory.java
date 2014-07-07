/**
 * Jul 31, 2008-12:34:51 PM
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

import weka.core.Instance;

/**
 *
 * Returns the underlying instance without any modification.
 * 
 * @author Daniel McEnnis
 */
public class InstanceInstanceFactory implements InstanceFactory{

    @Override
    public Instance transform(Object value, String name) {
        return (Instance)value;
    }

    public InstanceInstanceFactory prototype(){
        return new InstanceInstanceFactory();
    }
}
