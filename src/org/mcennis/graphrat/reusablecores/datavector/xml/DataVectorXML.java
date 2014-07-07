/**
 * DataVectorXML
 * Created Jan 16, 2009 - 11:20:17 PM
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
package org.mcennis.graphrat.reusablecores.datavector.xml;

import org.mcennis.graphrat.reusablecores.datavector.DataVector;
import org.dynamicfactory.propertyQuery.XMLParserObject;

/**
 *
 * @author Daniel McEnnis
 */
public interface DataVectorXML extends XMLParserObject{

    public DataVector getDataVector();

}
