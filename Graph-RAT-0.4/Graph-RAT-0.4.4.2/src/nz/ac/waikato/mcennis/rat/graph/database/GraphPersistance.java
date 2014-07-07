/*
 * GraphPersistance.java
 *
 * Created on 20 August 2007, 16:56
 *
 * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt)
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
