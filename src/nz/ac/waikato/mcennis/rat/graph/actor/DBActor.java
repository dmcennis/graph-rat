/*

 * DerbyActor.java

 *

 * Created on 8 October 2007, 11:49

 *

 * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt)

 */
package nz.ac.waikato.mcennis.rat.graph.actor;

import java.sql.Connection;

import java.sql.PreparedStatement;

import java.sql.ResultSet;

import java.sql.SQLException;

import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import nz.ac.waikato.mcennis.rat.graph.model.Listener;

import nz.ac.waikato.mcennis.rat.graph.model.Model;

import nz.ac.waikato.mcennis.rat.graph.model.ModelShell;

import org.dynamicfactory.descriptors.Properties;
import org.dynamicfactory.property.InvalidObjectTypeException;
import org.dynamicfactory.property.Property;
import org.dynamicfactory.property.PropertyFactory;
import org.dynamicfactory.property.PropertyValueDatabaseFactory;
import org.dynamicfactory.property.database.PropertyValueDB;

/**

 *


 * 

 * Class that implemnts an actor backed by a DerbyDB database.  Fixes problems

 * of inconsistencies between the database and the in-memory data when using

 * BasicUser class.

 * @author Daniel McEnnis
 */
public class DBActor extends ModelShell implements Actor, Listener {

    int id = -1;
    boolean nameSet = false;
    boolean typeSet = false;
    String name = "";
    String type = "";
    static String directory = "/tmp/";
    static String database = "LiveJournal";
    static Connection connection = null;
    static PreparedStatement statSetID = null;
    static PreparedStatement statGetPropertyIDArray = null;
    static PreparedStatement statGetProperty = null;
    static PreparedStatement statGetPropertyValues = null;
    static PreparedStatement statGetPropertyID = null;
    static PreparedStatement statRemoveActorProperty = null;
    static PreparedStatement statRemoveProperty = null;
    static PreparedStatement statRemovePropertyValues = null;
    static PreparedStatement statAddGetPropertyID = null;
    static PreparedStatement statAddActorProperty = null;
    static PreparedStatement statAddProperty = null;
    static PreparedStatement statAddPropertyValue = null;
    static PreparedStatement statDeleteActorProperty =null;
    static PreparedStatement statDeleteProperty = null;
    static PreparedStatement statDeletePropertyValue = null;
    static PreparedStatement statSetType = null;
    static PreparedStatement statGetNumID = null;
    static PreparedStatement statSetNumID = null;

    /** Creates a new instance of DerbyActor */
    public DBActor() {
    }

    /**

     * static initialization method that sets up the global connection objects.  This

     * static connection should ultimately be replaced by a database connection pool.

     */
    public static void init() {

        try {

            System.setProperty("derby.system.home", directory);

            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");

            connection = java.sql.DriverManager.getConnection("jdbc:derby:" + database);

            connection.setAutoCommit(false);

            statSetID = connection.prepareStatement("UPDATE Actor SET name=? WHERE id=?");

            statGetPropertyIDArray = connection.prepareStatement("SELECT Property.id, Property.type, Property.class FROM ActorProperties, Property WHERE ActorProperties.property=Property.id AND ActorProperties.id=?");

            statGetPropertyID = connection.prepareStatement("SELECT Property.id, Property.type, Property.class FROM ActorProperties, Property WHERE ActorProperties.property=Property.id AND ActorProperties.id=? AND Property.type=?");

            statGetPropertyValues = connection.prepareStatement("SEECT value FROM PropertyValue WHERE property=?");

            statRemoveActorProperty = connection.prepareStatement("DELETE FROM ActorProperties WHERE property=?");

            statRemoveProperty = connection.prepareStatement("DELETE FROM Property WHERE id=?");

            statRemovePropertyValues = connection.prepareStatement("DELETE FROM Property_Value WHERE property=?");

            statAddProperty = connection.prepareStatement("INSERT  INTO ActorProperties (id,property) VALUES (?,?)");

            statAddGetPropertyID = connection.prepareStatement("SELECT MAX(Property.id) FROM Property WHERE type=? AND class=? ");

            statSetType = connection.prepareStatement("UPDATE Actor SET type=? WHERE id=?");

            statGetNumID = connection.prepareStatement("SELECT id FROM Actor WHERE name=? AND type=?");

            statSetNumID = connection.prepareStatement("INSERT INTO Actor (name,type) VALUES (?,?)");

            statDeleteActorProperty = connection.prepareStatement("DELETE FROM ActorProperty WHERE property=?");

            statDeleteProperty = connection.prepareStatement("DELETE FROM Property WHERE id=?");

            statDeletePropertyValue = connection.prepareStatement("DELETE FROM PropertyValue WHERE property=?");

        } catch (ClassNotFoundException ex) {

            ex.printStackTrace();

        } catch (SQLException ex) {

            ex.printStackTrace();

        }

    }

    /**

     * Has this object been Initialized yet.

     * @return is initialized or not

     */
    public static boolean isInitialized() {

        return !(connection == null);

    }

    @Override
    public String getID() {

        return name;

    }

    @Override
    public void setID(String id) {

        if (this.id > 0) {

            //            synchronized(connection){

            try {

                statSetID.clearParameters();

                statSetID.setString(1, id);

                statSetID.setInt(2, this.id);

                statSetID.executeUpdate();

                name = id;

            } catch (SQLException ex) {

                ex.printStackTrace();

            }

        //            }

        } else {

            name = id;

            nameSet = true;

            if (nameSet && typeSet) {

                getNumericalID();

            }

        }

    }

    @Override
    public List<Property> getProperty() {
        LinkedList<Property> ret = new LinkedList<Property>();
        ResultSet rs = null;
        ResultSet rs2 = null;
        try {
            statGetPropertyIDArray.clearParameters();
            statGetPropertyIDArray.setInt(1, id);
            rs = statGetPropertyIDArray.executeQuery();
            while (rs.next()) {
                int propertyID = rs.getInt("Property.id");
                String propertyType = rs.getString("Property.type");
                String propertyClassString = rs.getString("Property.class");
//                if (PropertyValueDatabaseFactory.newInstance().getClassParameter().getValue().contains(propertyClassString)) {
                    PropertyValueDB factory = PropertyValueDatabaseFactory.newInstance().create(propertyClassString);
                    Property item = PropertyFactory.newInstance().create(propertyType, Integer.toString(propertyID),factory.getValueClass());
                    statGetPropertyValues.clearParameters();
                    statGetPropertyValues.setInt(1, propertyID);
                    rs2 = statGetPropertyValues.executeQuery();
                    while (rs2.next()) {
                        item.add(factory.get(rs2.getInt("value")));
                    }
                    ret.add(item);
//                }
            }
        } catch (InvalidObjectTypeException ex) {
            Logger.getLogger(DBActor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(DBActor.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                }
                rs = null;
            }
            if (rs2 != null) {
                try {
                    rs2.close();
                } catch (SQLException ex) {
                }
                rs2 = null;
            }
        }
        return ret;
    }

    @Override
    public Property getProperty(String ID) {

        ResultSet rs = null;
        ResultSet rs2 = null;
        try {
            statGetPropertyID.clearParameters();
            statGetPropertyID.setInt(1, id);
            statGetPropertyID.setString(2, ID);
            rs = statGetPropertyIDArray.executeQuery();
            if (rs.next()) {
                int propertyID = rs.getInt("Property.id");
                String propertyType = rs.getString("Property.type");
                String propertyClassString = rs.getString("Property.class");
                Class propertyClass;
                propertyClass = Class.forName(propertyClassString);
//                if (PropertyValueDatabaseFactory.newInstance().getClassParameter().getValue().contains(propertyClass.getSimpleName())) {
                    PropertyValueDB factory = PropertyValueDatabaseFactory.newInstance().create(propertyClass);
                    Property item = PropertyFactory.newInstance().create(propertyType, Integer.toString(propertyID),propertyClass);
                    statGetPropertyValues.clearParameters();
                    statGetPropertyValues.setInt(1, propertyID);
                    rs2 = statGetPropertyValues.executeQuery();
                    while (rs2.next()) {
                        item.add(factory.get(rs2.getInt("value")));
                    }
                    return item;
//                }
            }
        } catch (InvalidObjectTypeException ex) {
            Logger.getLogger(DBActor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBActor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(DBActor.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                }
                rs = null;
            }
            if (rs2 != null) {
                try {
                    rs2.close();
                } catch (SQLException ex) {
                }
                rs2 = null;
            }
        }
        return null;
    }

    @Override
    public void add(Property prop) {
        ResultSet rs = null;
        try {
            statAddProperty.clearParameters();
            statAddProperty.setString(1, prop.getType());
            statAddProperty.setString(2, prop.getPropertyClass().getSimpleName());
            statAddGetPropertyID.clearParameters();
            statAddGetPropertyID.setString(1, prop.getType());
            statAddGetPropertyID.setString(2, prop.getPropertyClass().getSimpleName());
            statAddProperty.executeUpdate();
            rs = statAddGetPropertyID.executeQuery();
            if (rs.next()) {
                int propertyId = rs.getInt("Property.id");
                statAddActorProperty.clearParameters();
                statAddActorProperty.setInt(1, id);
                statAddActorProperty.setInt(2, propertyId);
                statAddActorProperty.executeUpdate();
                PropertyValueDB factory = PropertyValueDatabaseFactory.newInstance().create(prop.getPropertyClass());
                Iterator it = prop.getValue().iterator();
                while (it.hasNext()) {
                    int value = factory.put(it.next());
                    statAddPropertyValue.clearParameters();
                    statAddPropertyValue.setInt(1, propertyId);
                    statAddPropertyValue.setInt(2, value);
                    statAddPropertyValue.executeUpdate();
                }
            } else {
                Logger.getLogger(DBActor.class.getName()).log(Level.SEVERE, "Adding of Property " + prop.getType() + " on actor " + id + " failed");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBActor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void setMode(String type) {

        if (id > 0) {

            try {

                statSetType.clearParameters();

                statSetType.setString(1, type);

                statSetType.setInt(2, id);

                statSetType.executeUpdate();

                this.type = type;

            } catch (SQLException ex) {

                ex.printStackTrace();

            }

        } else {

            this.type = type;

            typeSet = true;

            if (nameSet && typeSet) {

                getNumericalID();

            }

        }

    }

    @Override
    public String getMode() {

        return type;

    }

    @Override
    public DBActor prototype() {

        DBActor ret = new DBActor();

        return ret;

    }

    /**

     * Throws ClassCastException when the parameter is not an Actor.  Comparisons

     * are:

     * <br>

     * <ol><li>String compareTo on type (mode)

     * <li>String compareTo on ID

     * <li>Property comparison: return compareTo on the first pair of properties 

     * that do not return 0

     * <li>Page comparison: return compareTo on the first pair of pages that do not

     * return 0

     * <li>return 0

     * </ol>

     */
    public int compareTo(Object o) {

        Actor a = (Actor) o;

        if (type.compareTo(a.getMode()) == 0) {

            if (name.compareTo(a.getID()) == 0) {

                return compareProperties(a);
            } else {

                return name.compareTo(a.getID());

            }

        } else {

            return type.compareTo(a.getMode());

        }

    }

    /**

     * Compare properties of this actor with the given actor.  Sort the property

     * arrays and compare sequentially, returning the first non-zero value or 

     * returning zero if all properties are equal.

     * 

     * @param right actor to be compared against

     * @return compareTo over all properties

     */
    protected int compareProperties(Actor right) {

        List<Property> leftProp = this.getProperty();

        List<Property> rightProp = right.getProperty();

        if (leftProp.size() != rightProp.size()) {
            return leftProp.size() - rightProp.size();
        }

        LinkedList<Property> l = new LinkedList<Property>();
        l.addAll(leftProp);

        LinkedList<Property> r = new LinkedList<Property>();
        r.addAll(rightProp);

        java.util.Collections.sort(l);

        java.util.Collections.sort(r);

        Iterator<Property> lIt = l.iterator();
        Iterator<Property> rIt = r.iterator();

        int ret = 0;

        while ((lIt.hasNext()) && (rIt.hasNext())) {

            ret = lIt.next().compareTo(rIt.next());

            if (ret != 0) {

                return ret;

            }

        }


        return 0;

    }

    /**

     * Get thye numerical ID that uniquely describes this Actor in the database

     */
    protected void getNumericalID() {

        try {

            statGetNumID.clearParameters();

            statGetNumID.setString(1, name);

            statGetNumID.setString(2, type);

            ResultSet rs = statGetNumID.executeQuery();

            if (rs.next()) {

                id = rs.getInt("id");

            } else {

                statSetNumID.clearParameters();

                statSetNumID.setString(1, name);

                statSetNumID.setString(2, type);

                statSetNumID.executeUpdate();

                statGetNumID.clearParameters();

                statGetNumID.setString(1, name);

                statGetNumID.setString(2, type);

                ResultSet rs2 = statGetNumID.executeQuery();

                if (rs2.next()) {

                    id = rs2.getInt("id");

                } else {

                    id = -2;

                }

                rs2.close();

            }

            rs.close();

        } catch (SQLException ex) {

            ex.printStackTrace();

        }

    }

    /**

     * Set the location of the Derby database directory 

     * @param dir directory where thye datbases are stored

     */
    public static void setDirectory(String dir) {

        directory = dir;

    }

    /**

     * Set which database to access

     * @param db name of the datbase to open

     */
    public static void setDatabase(String db) {

        database = db;

    }

    /**

     * Saves to database the changes in a property.

     * @param m Property that changed

     * @param type currently only 0 (Changed) is fired

     */
    public void publishChange(Model m, int type, int argument) {

        if (m instanceof Property) {

            try {

                Property p = (Property) m;

                statGetPropertyID.clearParameters();

                statGetPropertyID.setInt(1, id);

                statGetPropertyID.setString(2, p.getType());

                ResultSet rs = statGetProperty.executeQuery();

                if (rs.next()) {

                    int propertyId = rs.getInt("Property.id");
                    PropertyValueDB factory = PropertyValueDatabaseFactory.newInstance().create(p.getPropertyClass());
                    int value = factory.put(p.getValue().get(p.getValue().size() - 1));
                    statAddPropertyValue.clearParameters();

                    statAddPropertyValue.setInt(1, id);

                    statAddPropertyValue.setInt(2, value);

                    statAddProperty.executeUpdate();

                }
            } catch (SQLException ex) {

                ex.printStackTrace();

            }

        }

    }

    @Override
    public void init(Properties properties) {
        if (!isInitialized()) {
            if ((properties.get("DerbyDirectory") != null) && (properties.get("DerbyDirectory").getParameterClass().getName().contentEquals(String.class.getName()))) {
                directory = (String) properties.get("DerbyDirectory").getValue().iterator().next();
            }
            if ((properties.get("Database") != null) && (properties.get("Database").getParameterClass().getName().contentEquals(String.class.getName()))) {
                database = (String) properties.get("Database").getValue().iterator().next();
            }
            DBActor.init();
        }
    }

    public void removeProperty(String ID) {
            ResultSet rs = null;
        try {
            statGetPropertyID.clearParameters();
            statGetPropertyID.setInt(1, id);
            statGetPropertyID.setString(2, ID);
            rs = statGetPropertyID.executeQuery();
            if (rs.next()) {
                int propertyID = rs.getInt("Property.id");
                statDeleteActorProperty.clearParameters();
                statDeleteActorProperty.setInt(1, propertyID);
                statDeleteActorProperty.executeUpdate();
                statDeleteProperty.clearParameters();
                statDeleteProperty.setInt(1, propertyID);
                statDeleteProperty.executeUpdate();
                statDeletePropertyValue.clearParameters();
                statDeletePropertyValue.setInt(1, propertyID);
                statDeletePropertyValue.executeUpdate();
            } else {
                Logger.getLogger(DBActor.class.getName()).log(Level.SEVERE, "Property" + ID + "  does not exist in actor " + id);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBActor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

