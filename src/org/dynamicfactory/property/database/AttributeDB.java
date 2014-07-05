/*
 * AttributeDB - created 14/03/2009 - 6:34:01 PM
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
import weka.core.Attribute;
import weka.core.FastVector;

/**
 *
 * @author Daniel McEnnis
 */
public class AttributeDB extends AbstractPropertyDB<Attribute>{
    PreparedStatement get = null;
    PreparedStatement getValue = null;
    PreparedStatement put = null;
    PreparedStatement getKey = null;
    PreparedStatement putValue = null;
    PreparedStatement deleteKey = null;
    PreparedStatement deleteValue = null;

    public StringDB newCopy() {
        return new StringDB();
    }

    public void initializeDatabase(Connection conn) {
        if(conn != null){
            try {
                Statement stat = conn.createStatement();
                stat.executeUpdate("CREATE TABLE AttributeKey ("
                        + "id integer not null generated ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
                        + "key varchar(1024) not null, "
                        + "primary key(id))");
                stat.executeUpdate("CREATE TABLE AttributeValue ("
                        + "id integer not null generated ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
                        + "key integer not null, "
                        + "string varchar(1024) not null, "
                        + "primary key(id))");
            } catch (SQLException ex) {
                Logger.getLogger(URLDB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void setConnection(Connection con) {
        try {
            conn = con;
            get = conn.prepareStatement("SELECT key FROM AttributeKey WHERE id=?");
            getValue = conn.prepareStatement("SELECT string FROM AttributeValue WHERE key=?");
            put = conn.prepareStatement("INSERT INTO AttributeKey (key) VALUES (?)");
            getKey = conn.prepareStatement("SELECT MAX(id) FROM AttributeKey WHERE key=?");
            putValue = conn.prepareStatement("INSERT INTO AttributeValues (key,string) VALUES (?,?)");
        } catch (SQLException ex) {
            Logger.getLogger(StringDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void clearDatabase(Connection conn) {
        try {
            conn.createStatement().executeUpdate("DELETE FROM AttributeKey");
            conn.createStatement().executeUpdate("DELETE FROM AttributeValue");
        } catch (SQLException ex) {
            Logger.getLogger(StringDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Attribute get(int i) {
            Attribute ret=null;
            ResultSet rs = null;
        try {
            String key = "";
            get.clearParameters();
            get.setInt(1, i);
            rs = get.executeQuery();
            if (rs.next()) {
                key = rs.getString("key");
            }
            rs.close();
            getValue.clearParameters();
            getValue.setInt(1, i);
            rs = getValue.executeQuery();
            if(rs.next()){
                FastVector names = new FastVector();
                names.addElement(rs.getString("string"));
                while(rs.next()){
                    names.addElement(rs.getString("string"));
                }
                ret = new Attribute(key,names);
            }else{
                ret = new Attribute(key);
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

    public int put(Attribute object) {
        int ret= -1;
        ResultSet rs = null;
        try {
            put.clearParameters();
            getKey.clearParameters();
            put.setString(1, object.name());
            getKey.setString(1, object.name());
            put.executeUpdate();
            rs = getKey.executeQuery();
            if(rs.next()){
                ret = rs.getInt("id");
                for(int i=0;i<object.numValues();++i){
                    putValue.clearParameters();
                    putValue.setInt(1, ret);
                    putValue.setString(2, object.value(i));
                    putValue.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(URLDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }

    public void remove(int id) {
        try {
            deleteValue.clearParameters();
            deleteValue.setInt(1, id);
            deleteValue.executeUpdate();
            deleteKey.clearParameters();
            deleteKey.setInt(1, id);
            deleteKey.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(AttributeDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Class getValueClass() {
        return Attribute.class;
    }

}
