/*
 * InstanceDB - created 14/03/2009 - 6:34:30 PM
 * Copyright Daniel McEnnis, see license.txt
 */

package org.dynamicfactory.property.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.core.Instance;

/**
 *
 * @author Daniel McEnnis
 */
public class InstanceDB extends AbstractPropertyDB<Instance>{
    PreparedStatement get = null;
    PreparedStatement getValue = null;
    PreparedStatement getID = null;
    PreparedStatement put = null;
    PreparedStatement putValue = null;
    PreparedStatement deleteInstance = null;
    PreparedStatement deleteInstanceData = null;

    public StringDB newCopy() {
        return new StringDB();
    }

    public void initializeDatabase(Connection conn) {
        if(conn != null){
            try {
                Statement stat = conn.createStatement();
                stat.executeUpdate("CREATE TABLE Instance ("
                        + "id integer not null generated ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
                        + "instances integer, "
                        + "weight double not null, "
                        + "primary key(id))");
                stat.executeUpdate("CREATE TABLE Instance_Data ("
                        + "instance integer not null),"
                        + "index integer not null, "
                        + "data double not null, "
                        + "primary key(instance, index))");
            } catch (SQLException ex) {
                Logger.getLogger(URLDB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void setConnection(Connection con) {
        try {
            conn = con;
            get = conn.prepareStatement("SELECT instances, weight FROM Instance WHERE id=?");
            getValue = conn.prepareStatement("SELECT data FROM Instance_Data WHERE instance=? ORDER BY index ASC");
            getID = conn.prepareStatement("SELECT MAX(id) FROM Instance WHERE weight=?");
            put = conn.prepareStatement("INSERT INTO Instance(weight) VALUES (?)");
            putValue = conn.prepareStatement("INSERT INTO Instance_Data (instance,index,data) VALUES (?,?,?)");
            deleteInstance = conn.prepareStatement("DELETE FROM Instance WHERE id=?");
            deleteInstanceData = conn.prepareStatement("DELETE FROM Instance_Data WHERE instance=?");
        } catch (SQLException ex) {
            Logger.getLogger(StringDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void clearDatabase(Connection conn) {
        try {
            conn.createStatement().executeUpdate("DELETE FROM Instance");
            conn.createStatement().executeUpdate("DELETE FROM Instance_Data");
        } catch (SQLException ex) {
            Logger.getLogger(StringDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Instance get(int i) {
            Instance ret=null;
            ResultSet rs = null;
        try {
            get.clearParameters();
            get.setInt(1, i);
            rs = get.executeQuery();
            String name = "";
            if (rs.next()) {
                name = rs.getString("name");
                rs.close();
                getValue.clearParameters();
                getValue.setInt(1, i);
                rs = getValue.executeQuery();
                LinkedList<Double> values = new LinkedList<Double>();
                while(rs.next()){
                    values.add(rs.getDouble("data"));
                }
                rs.close();
                double[] data = new double[values.size()];
                int count =0;
                Iterator<Double> it = values.iterator();
                while(it.hasNext()){
                    data[count++] = it.next();
                }
                ret = new Instance(data.length,data);
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

    public int put(Instance object) {
        int ret = -1;
        ResultSet rs = null;
        try {
            put.clearParameters();
            getID.clearParameters();
            put.setDouble(1, object.weight());
            getID.setDouble(1, object.weight());
            put.executeUpdate();
            rs = getID.executeQuery();
            if(rs.next()){
                ret = rs.getInt("id");
                for(int i=0;i<object.numValues();++i){
                    putValue.clearParameters();
                    putValue.setInt(1, ret);
                    putValue.setInt(2, i);
                    putValue.setDouble(3, object.value(i));
                    putValue.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(URLDB.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
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
            deleteInstanceData.clearParameters();
            deleteInstanceData.setInt(1, id);
            deleteInstanceData.executeUpdate();
            deleteInstance.clearParameters();
            deleteInstance.setInt(1, id);
            deleteInstance.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(InstanceDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Class getValueClass() {
        return Instance.class;
    }
}
