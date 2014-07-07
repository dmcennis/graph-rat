/*

 * GraphPersistance.java

 *

 * Created on 20 August 2007, 16:56

 *

 * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt)

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


package org.mcennis.graphrat.database;



/**

 *

 * @author Daniel McEnnis

 * 

 * TODO: Not implemented yet - eventually could provide a way for loading and saving 

 * MemGraph objects to a database.

 */

public interface GraphPersistance {

    public void initialize() throws java.sql.SQLException;

    public void save() throws java.sql.SQLException;

    public void load() throws java.sql.SQLException;

    public void close() throws java.sql.SQLException;

}

