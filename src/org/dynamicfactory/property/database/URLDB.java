/*
 * URLDB - created 14/03/2009 - 6:35:12 PM
 * Copyright Daniel McEnnis, see license.txt
 */

package org.dynamicfactory.property.database;

import java.net.MalformedURLException;
import java.net.URL;
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
public class URLDB extends AbstractPropertyDB<URL>{

    PreparedStatement get = null;
    PreparedStatement put = null;
    PreparedStatement putGet = null;
    PreparedStatement delete = null;

    public PropertyValueDB newCopy() {
        return new URLDB();
    }

    public void initializeDatabase(Connection conn) {
        if(conn != null){
            try {
                Statement stat = conn.createStatement();
                stat.executeUpdate("CREATE TABLE URL ("
                        + "id integer not null generated ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
                        + "url varchar(1024) not null, "
                        + "primary key(id))");
            } catch (SQLException ex) {
                Logger.getLogger(URLDB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void clearDatabase(Connection conn) {
        try {
            Statement stat = conn.createStatement();
            stat.executeUpdate("DELETE FROM URLProperty");
        } catch (SQLException ex) {
            Logger.getLogger(URLDB.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public URL get(int id) {
        URL ret = null;
            ResultSet rs = null;
        try {
            get.clearParameters();
            get.setInt(1, id);
            rs = get.executeQuery();
            if (rs.next()) {
                String url = rs.getString("url");
                ret = new URL(url);
            }
        } catch (MalformedURLException ex) {
            Logger.getLogger(URLDB.class.getName()).log(Level.SEVERE, null, ex);
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

    public void setConnection(Connection c){
        try {
            conn = c;
            get = conn.prepareStatement("SELECT url FROM URL WHERE id=?");
            put = conn.prepareStatement("INSERT INTO URL (url) VALUES (?)");
            putGet = conn.prepareStatement("SELECT MAX(id) FROM url");
            delete = conn.prepareStatement("DELETE FROM url WHERE id=?");
        } catch (SQLException ex) {
            Logger.getLogger(URLDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int put(URL object) {
        int ret = -1;
        ResultSet rs = null;
        try {
            String url = object.toString();
            put.clearParameters();
            put.setString(1, url);
            putGet.clearParameters();
            rs = putGet.executeQuery();
            if(rs.next()){
                ret = rs.getInt("id");
            }else{
                Logger.getLogger(URLDB.class.getName()).log(Level.SEVERE, "Put method for URL '"+object.toString()+"' silently failed");
            }
            put.executeUpdate();
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
            Logger.getLogger(URLDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Class getValueClass() {
        return URL.class;
    }

}
