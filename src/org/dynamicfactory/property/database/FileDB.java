/*
 * FileDB - created 14/03/2009 - 6:34:21 PM
 * Copyright Daniel McEnnis, see license.txt
 */

package org.dynamicfactory.property.database;

import java.io.File;
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
public class FileDB extends AbstractPropertyDB<File>{

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
                stat.executeUpdate("CREATE TABLE File ("
                        + "id integer not null generated ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
                        + "file varchar(1024) not null, "
                        + "primary key(id))");
            } catch (SQLException ex) {
                Logger.getLogger(URLDB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void setConnection(Connection con) {
        try {
            conn = con;
            get = conn.prepareStatement("SELECT file FROM File WHERE id=?");
            put = conn.prepareStatement("ISNERT INTO File(file) VALUES (?)");
            putGet = conn.prepareStatement("SELECT MAX(id) FROM File");
            delete = conn.prepareStatement("DELETE FROM File WHERE id=?");
        } catch (SQLException ex) {
            Logger.getLogger(StringDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void clearDatabase(Connection conn) {
        try {
            conn.createStatement().executeUpdate("DELETE FROM File");
        } catch (SQLException ex) {
            Logger.getLogger(StringDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public File get(int i) {
            File ret=null;
            ResultSet rs = null;
        try {
            get.clearParameters();
            get.setInt(1, i);
            rs = get.executeQuery();
            if (rs.next()) {
                ret = new File(rs.getString("file"));
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

    public int put(File object) {
        int ret = -1;
        ResultSet rs = null;
        try {
            String url = object.getAbsolutePath();
            put.clearParameters();
            putGet.clearParameters();
            put.setString(1, url);
            put.executeUpdate();
            rs = putGet.executeQuery();
            if(rs.next()){
                ret = rs.getInt("id");
            }else{
                Logger.getLogger(FileDB.class.getName()).log(Level.SEVERE, "Put method for File '"+object.toString()+"' silently failed");
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
            Logger.getLogger(FileDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Class getValueClass() {
        return File.class;
    }

}
