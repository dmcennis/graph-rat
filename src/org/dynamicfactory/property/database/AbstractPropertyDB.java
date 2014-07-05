/*
 * AbstractPropertyDB - created 15/03/2009 - 11:20:49 AM
 * Copyright Daniel McEnnis, see license.txt
 */

package org.dynamicfactory.property.database;

import java.sql.Connection;
import nz.ac.waikato.mcennis.rat.XMLParserObject.State;

/**
 *
 * @author Daniel McEnnis
 */
public abstract class AbstractPropertyDB<Type> implements PropertyValueDB<Type>{
    State state = State.UNINITIALIZED;

    Connection conn = null;

    public State buildingStatus(){
        return state;
    }

}
