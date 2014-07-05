/*
 * LongDB - created 14/03/2009 - 6:34:54 PM
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
public class LongDB extends AbstractPropertyDB<Long>{

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
                stat.executeUpdate("CREATE TABLE Long ("
                        + "id integer not null generated ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
                        + "long long not null, "
                        + "primary key(id))");
            } catch (SQLException ex) {
                Logger.getLogger(URLDB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void setConnection(Connection con) {
        try {
            conn = con;
            get = conn.prepareStatement("SELECT long FROM Long WHERE id=?");
            put = conn.prepareStatement("INSERT INTO Long (long) VALUES (?)");
            putGet = conn.prepareStatement("SELECT MAX(id) FROM Long ");
            delete = conn.prepareStatement("DELETE FROM Long WHERE id=?");
        } catch (SQLException ex) {
            Logger.getLogger(StringDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void clearDatabase(Connection conn) {
        try {
            conn.createStatement().executeUpdate("DELETE FROM Long");
        } catch (SQLException ex) {
            Logger.getLogger(StringDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Long get(int i) {
            Long ret=(long)0;
            ResultSet rs = null;
        try {
            get.clearParameters();
            get.setInt(1, i);
            rs = get.executeQuery();
            if (rs.next()) {
                ret = rs.getLong("long");
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

    public int put(Long object) {
        int ret = -1;
        ResultSet rs = null;
        try {
            long url = object.longValue();
            put.clearParameters();
            put.setLong(1, url);
            putGet.clearParameters();
            put.executeUpdate();
            rs = putGet.executeQuery();
            if(rs.next()){
                ret = rs.getInt("id");
            }else{
                Logger.getLogger(URLDB.class.getName()).log(Level.SEVERE, "Insert of Long '"+object+"' silently failed");
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
            Logger.getLogger(LongDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Class getValueClass() {
        return Long.class;
    }

}
