/*
 * InstancesDB - created 14/03/2009 - 6:34:39 PM
 * Copyright Daniel McEnnis, see license.txt
 */

package org.mcennis.graphrat.property.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.dynamicfactory.property.database.AbstractPropertyDB;import org.dynamicfactory.property.database.StringDB;import org.dynamicfactory.property.database.URLDB;import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instances;

/**
 *
 * @author Daniel McEnnis
 */
public class InstancesDB extends AbstractPropertyDB<Instances>{
    AttributeDB attributeFactory = new AttributeDB();
    PreparedStatement get = null;
    PreparedStatement getAttribute = null;
    PreparedStatement getID = null;
    PreparedStatement getAttributeID = null;
    PreparedStatement put = null;
    PreparedStatement putAttribute = null;
    PreparedStatement delete = null;
    PreparedStatement deleteAttribute = null;

    public StringDB newCopy() {
        return new StringDB();
    }

    public void initializeDatabase(Connection conn) {
        if(conn != null){
            try {
                Statement stat = conn.createStatement();
                stat.executeUpdate("CREATE TABLE Instances ("
                        + "id integer not null generated ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
                        + "name varchar(1024) not null, "
                        + "primary key(id))");
                stat.executeUpdate("CREATE TABLE Instances_Attribute ("
                        + "instances integer not null, "
                        + "index integer not null, "
                        + "attribute integer not null, "
                        + "primary key(instances,index))");
            } catch (SQLException ex) {
                Logger.getLogger(URLDB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void setConnection(Connection con) {
        try {
            conn = con;
            get = conn.prepareStatement("SELECT name FROM Instances WHERE id=?");
            getAttribute = conn.prepareStatement("SELECT attribute FROM Instances_Attribute WHERE id=? ORDER BY index ASC");
            put = conn.prepareStatement("INSERT INTO Instances(name) VALUES (?)");
            getID = conn.prepareStatement("SELECT MAX(id) FROM Instances WHERE name=?");
            getAttributeID = conn.prepareStatement("SELECT MAX(id) FROM Attribute WHERE ");
            putAttribute = conn.prepareStatement("INSERT INTO Instances_Attribute (instances,index,attribute) VALUES (?,?,?)");
            delete = conn.prepareStatement("DELETE FROM Instances WHERE id=?");
            deleteAttribute = conn.prepareStatement("DELETE FROM Instances_Data WHERE instances=?");
        } catch (SQLException ex) {
            Logger.getLogger(StringDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void clearDatabase(Connection conn) {
        try {
            conn.createStatement().executeUpdate("DELETE FROM Instances");
            conn.createStatement().executeUpdate("DELETE FROM Instances_Attribute");
        } catch (SQLException ex) {
            Logger.getLogger(StringDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Instances get(int i) {
            Instances ret=null;
            ResultSet rs = null;
        try {
            get.clearParameters();
            get.setInt(1, i);
            rs = get.executeQuery();
            String name = "";
            if (rs.next()) {
                name = rs.getString("name");
                rs.close();
                getAttribute.clearParameters();
                getAttribute.setInt(1, i);
                rs = getAttribute.executeQuery();
                FastVector attributes = new FastVector();
                while(rs.next()){
                    Attribute a = attributeFactory.get(rs.getInt("attribute"));
                    attributes.addElement(a);
                }
                rs.close();
                ret = new Instances(name,attributes,100);
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

    public int put(Instances object) {
        int ret = -1;
        ResultSet rs = null;
        ResultSet rs2 = null;
        try {
            put.clearParameters();
            getID.clearParameters();
            put.setString(1, object.relationName());
            getID.setString(1, object.relationName());
            put.executeUpdate();
            rs = getID.executeQuery();
            if(rs.next()){
                ret = rs.getInt("id");
                for(int i=0;i<object.numAttributes();++i){
                    getAttributeID.clearParameters();
                    getAttributeID.setString(1,object.attribute(i).name());
                    attributeFactory.put(object.attribute(i));
                    rs2 = getAttributeID.executeQuery();
                    if(rs2.next()){
                        putAttribute.clearParameters();
                        putAttribute.setInt(1, ret);
                        putAttribute.setInt(2, i);
                        putAttribute.setInt(3, rs2.getInt("id"));
                        putAttribute.executeUpdate();
                    }
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
            if(rs2 != null){
                try {
                    rs2.close();
                } catch (SQLException ex) {}
                rs2 = null;
            }
        }
        return ret;
    }

    public void remove(int id) {
        try {
            deleteAttribute.clearParameters();
            deleteAttribute.setInt(1, id);
            deleteAttribute.executeUpdate();
            delete.clearParameters();
            delete.setInt(1, id);
            delete.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(InstancesDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Class getValueClass() {
        return Instances.class;
    }
}
