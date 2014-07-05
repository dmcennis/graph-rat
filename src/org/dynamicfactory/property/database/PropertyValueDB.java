/*
 * PropertyValueDB - Created 14/03/2009 - 6:28:24 PM
 * Copyright Daniel McEnnis, see license.txt
 */

package org.dynamicfactory.property.database;

import java.sql.Connection;
import nz.ac.waikato.mcennis.rat.XMLParserObject.State;

/**
 *
 * @author Daniel McEnnis
 */
public interface PropertyValueDB<Type> {

    public enum ObjectType {GRAPH,LINK,ACTOR};

    public abstract State buildingStatus();
    
    public abstract PropertyValueDB newCopy();

    public abstract void initializeDatabase(Connection con);

    public abstract void setConnection(Connection con);

    public abstract void clearDatabase(Connection con);

    public abstract Type get(int id);

    public abstract void remove(int id);

    public int put(Type object);

    public Class getValueClass();
}
