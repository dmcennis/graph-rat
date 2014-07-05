/*
 * IntegerDB - created 14/03/2009 - 6:34:48 PM
 * Copyright Daniel McEnnis, see license.txt
 */

package org.dynamicfactory.property.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Daniel McEnnis
 */
public class IntegerDB extends AbstractPropertyDB<Integer>{

    PreparedStatement get = null;
    PreparedStatement put = null;
    PreparedStatement putGet = null;
    PreparedStatement delete = null;

    public StringDB newCopy() {
        return new StringDB();
    }

    public void initializeDatabase(Connection conn) {
        if(conn != null){
            try {
                Statement stat = conn.createStatement();
                stat.executeUpdate("CREATE TABLE Integer ("
                        + "id integer not null generated ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
                        + "integer integer not null, "
                        + "primary key(id))");
            } catch (SQLException ex) {
                Logger.getLogger(URLDB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void setConnection(Connection con) {
        try {
            conn = con;
            get = conn.prepareStatement("SELECT integer FROM Integer WHERE id=?");
            put = conn.prepareStatement("ISNERT INTO Integer(integer) VALUES (?)");
            putGet = conn.prepareStatement("SELECT MAX(ID) FROM Integer");
            delete = conn.prepareStatement("DELETE FROM Integer WHERE id=?");
        } catch (SQLException ex) {
            Logger.getLogger(StringDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void clearDatabase(Connection conn) {
        try {
            conn.createStatement().executeUpdate("DELETE FROM Integer");
        } catch (SQLException ex) {
            Logger.getLogger(StringDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Integer get(int i) {
            Integer ret=(int)0;
            ResultSet rs = null;
        try {
            get.clearParameters();
            get.setInt(1, i);
            rs = get.executeQuery();
            if (rs.next()) {
                ret = rs.getInt("int");
            }
        } catch (SQLException ex) {
            Logger.getLogger(StringDB.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            if(rs != null){
                try {
                    rs.close();
                } catch (SQLException ex) {}
                rs = null;
            }
        }
            return ret;
    }

    public int put(Integer object) {
        int ret = -1;
        ResultSet rs = null;
        try {
            int url = object.intValue();
            put.clearParameters();
            putGet.clearParameters();
            put.setInt(1, url);
            put.executeUpdate();
            rs = putGet.executeQuery();
            if(rs.next()){
                ret = rs.getInt("id");
            }else{
                Logger.getLogger(URLDB.class.getName()).log(Level.SEVERE, "Put method for Integer '"+object.toString()+"' silently failed");
            }
        } catch (SQLException ex) {
            Logger.getLogger(URLDB.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            if(rs != null){
                try {
                    rs.close();
                } catch (SQLException ex) {}
                rs = null;
            }
        }
        return ret;
    }

    public void remove(int id) {
        try {
            delete.clearParameters();
            delete.setInt(1, id);
            delete.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(IntegerDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Class getValueClass() {
        return Integer.class;
    }

}
