/*
 * DerbyActor.java
 *
 * Created on 8 October 2007, 11:49
 *
 * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt)
 */
package nz.ac.waikato.mcennis.rat.graph.link;

import nz.ac.waikato.mcennis.rat.graph.actor.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.dynamicfactory.model.Listener;
import org.dynamicfactory.model.Model;
import org.dynamicfactory.model.ModelShell;
import nz.ac.waikato.mcennis.rat.graph.page.Page;

/**
 * Class that implemnts a link backed by a DerbyDB database.  Fixes problems
 * of inconsistencies between the database and the in-memory data when using
 * BasicUserLink class.
 *
 * @author Daniel McEnnis
 * 
 */
public class DBLink extends ModelShell implements Link, Listener {

    int id = -1;
    Actor start = null;
    double strength = 1.0;
    Actor finish = null;
    String type = "";
    static String directory = "/tmp/";
    static String database = "LiveJournal";
    static Connection connection = null;
    static PreparedStatement statSetRelation = null;
    static PreparedStatement statSetSource = null;
    static PreparedStatement statSetDestination = null;
    static PreparedStatement statSetStrength = null;
    static PreparedStatement statSetAll = null;
    static PreparedStatement statGetSource = null;
    static PreparedStatement statGetDestination = null;
    static PreparedStatement statGetStrength = null;
    static PreparedStatement statGetPropertyArray = null;
    static PreparedStatement statRemoveProperty = null;
    static PreparedStatement statAddProperty = null;
    static PreparedStatement statGetProperty = null;
    static PreparedStatement statAlterProperty = null;
    static PreparedStatement statGetRelation = null;
    static PreparedStatement statGetNumID = null;
    static PreparedStatement statSetNumID = null;
    static PreparedStatement statGetActorNumID = null;

    /** Creates a new instance of DerbyActor */
    public DBLink() {
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
            statSetRelation = connection.prepareStatement("UPDATE Link SET type=? WHERE id=?");
            statSetSource = connection.prepareStatement("UPDATE Link SET start=? WHERE id=?");
            statSetDestination = connection.prepareStatement("UPDATE Link SET finish=? WHERE id=?");
            statSetStrength = connection.prepareStatement("UPDATE Link SET strength=? WHERE id=?");
            statSetAll = connection.prepareStatement("UPDATE Link SET start=?,strength=?,finish=? WHERE id=?");
            statGetSource = connection.prepareStatement("SELECT start FROM Link WHERE id=?");
            statGetDestination = connection.prepareStatement("SELECT finish FROM LINK WHERE id=?");
            statGetStrength = connection.prepareStatement("SELECT strength FROM LINK WHERE id=?");
            statGetPropertyArray = connection.prepareStatement("SELECT type, value FROM LinkProperties WHERE id=?");
            statRemoveProperty = connection.prepareStatement("DELETE FROM LinkProperties WHERE id=? AND type=?");
            statAlterProperty = connection.prepareStatement("UPDATE LinkProperties SET value=? WHERE id=? AND type=?");
            statAddProperty = connection.prepareStatement("INSERT  INTO LinkProperties (id,type,value) VALUES (?,?,?)");
            statGetProperty = connection.prepareStatement("SELECT value FROM LinkProperties WHERE id=? AND type=?");
            //            statGetPage = connection.prepareStatement("SELECT * FROM Page WHERE actor=?");
            //            statAddPage = connection.prepareStatement("INSERT INTO Page VALUES (?)");
            statGetNumID = connection.prepareStatement("SELECT id FROM Link WHERE type=? AND start=? AND strength=? AND finish=?");
            statSetNumID = connection.prepareStatement("INSERT INTO Link (type,start,strength,finish) VALUES (?,?,?,?)");
            statGetActorNumID = connection.prepareStatement("SELECT id FROM Actor WHERE name=? AND type=?");
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
    public Property[] getProperty() {
        java.util.HashMap<String, Property> ret = new java.util.HashMap<String, Property>();
        try {
            //            synchronized(connection){
            statGetPropertyArray.setInt(1, id);
            ResultSet rs = statGetPropertyArray.executeQuery();
            while (rs.next()) {
                String type = rs.getString("type");
                if (!ret.containsKey(type)) {
                    java.util.Properties properties = new java.util.Properties();
                    properties.setProperty("PropertyID", type);
                    ret.put(type, PropertyFactory.newInstance().create(properties));
                }
                String value = rs.getString("value");
                if (!value.contentEquals("")) {
                    ret.get(type).add(rs.getString("value"));
                }
            }
            java.util.Iterator<Property> it = ret.values().iterator();
            while (it.hasNext()) {
                it.next().addListener(this);
            }
        //            }
        } catch (java.sql.SQLException ex) {
            ex.printStackTrace();
        } catch (InvalidObjectTypeException ex) {
            ex.printStackTrace();
        }
        if (ret.size() > 0) {
            return ret.values().toArray(new Property[]{});
        } else {
            return null;
        }
    }

    @Override
    public Property getProperty(String ID) {
        Property ret = null;
        try {
            //            synchronized(connection){
            statGetProperty.setInt(1, id);
            statGetProperty.setString(2, ID);
            ResultSet rs = statGetProperty.executeQuery();
            if (rs.next()) {

                java.util.Properties properties = new java.util.Properties();
                properties.setProperty("PropertyID", ID);
                ret = PropertyFactory.newInstance().create(properties);
                String value = rs.getString("value");
                if (!value.contentEquals("")) {
                    addValue(ret, value);
                }

                while (rs.next()) {
                    addValue(ret, value);
                }
                ret.addListener(this);
            }
        //            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return ret;
    }

//    @Override
    public void removeProperty(String ID) {
        try {
            //            synchronized(connection){
            statRemoveProperty.clearParameters();
            statRemoveProperty.setInt(1, id);
            statRemoveProperty.setString(2, ID);
            statRemoveProperty.executeUpdate();
        //            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void add(Property prop) {
        try {
            //            synchronized(connection){
            prop.addListener(this);
            statAddProperty.clearParameters();
            statAddProperty.setInt(1, id);
            statAddProperty.setString(2, prop.getType());
            Object[] value = prop.getValue();
            if (value == null) {
                statAddProperty.setString(3, "");
                statAddProperty.executeUpdate();
            } else {
                for (int i = 0; i < value.length; ++i) {
                    statAddProperty.setString(3, value[i].toString());
                    statAddProperty.executeUpdate();
                }
            }
        //            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void setType(String type) {
        this.type = type;
        if (id > 0) {
            try {
                statSetRelation.clearParameters();
                statSetRelation.setString(1, type);
                statSetRelation.setInt(2, id);
                statSetRelation.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public String getType() {
        return type;
    }

    /**
     * Throws ClassCastException when the parameter is not an Link.  Comparisons
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
        Link a = (Link) o;
        if (type.compareTo(a.getType()) == 0) {
            if (strength == a.getStrength()) {
                if ((start != null) && (a.getSource() == null)) {
                    return 1;
                } else if ((start == null) && (a.getSource() != null)) {
                    return -1;
                } else if (((start == null) && (a.getSource() == null)) || (start.compareTo(a.getSource()) == 0)) {
                    if ((finish != null) && (a.getDestination() == null)) {
                        return 1;
                    } else if ((finish == null) && (a.getDestination() != null)) {
                        return -1;
                    } else if (((finish == null) && (a.getDestination() == null)) || (finish.compareTo(a.getDestination()) == 0)) {
                        return compareProperties(a);
                    }else{
                        return finish.compareTo(a.getDestination());
                    }
                }else{
                    return start.compareTo(a.getSource());
                }
            } else if (strength < a.getStrength()) {
                return -1;
            } else {
                return 1;
            }
        } else {
            return type.compareTo(a.getType());
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
    protected int compareProperties(Link right) {
        Property[] l = this.getProperty();
        Property[] r = right.getProperty();
        if ((l == null) && (r == null)) {
            return 0;
        } else if (l == null) {
            return -1;
        } else if (r == null) {
            return 1;
        }
        java.util.Arrays.sort(l);
        java.util.Arrays.sort(r);
        int i = 0;
        int ret = 0;
        while ((i < l.length) && (i < r.length)) {
            ret = l[i].compareTo(r[i]);
            if (ret != 0) {
                return ret;
            } else {
                ++i;
            }
        }
        if (i < l.length) {
            return 1;
        } else if (i < r.length) {
            return -1;
        } else {
            return 0;
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
    public void publishChange(Model m, int type,int argument) {
        if (m instanceof Property) {
            try {
                Property p = (Property) m;
                statGetProperty.clearParameters();
                statGetProperty.setInt(1, id);
                statGetProperty.setString(2, p.getType());
                ResultSet rs = statGetProperty.executeQuery();
                if (rs.next()) {
                    if (rs.getString("value").contentEquals("")) {
                        statAlterProperty.clearParameters();
                        statAlterProperty.setInt(2, id);
                        statAlterProperty.setString(3, p.getType());
                        statAlterProperty.setString(1, p.getValue()[0].toString());
                        statAlterProperty.executeUpdate();
                    } else {
                        statAddProperty.clearParameters();
                        statAddProperty.setInt(1, id);
                        statAddProperty.setString(2, p.getType());
                        statAddProperty.setString(3, p.getValue()[p.getValue().length - 1].toString());
                        statAddProperty.executeUpdate();
                    }
                } else {
                    statAddProperty.clearParameters();
                    statAddProperty.setInt(1, id);
                    statAddProperty.setString(2, p.getType());
                    statAddProperty.setString(3, p.getValue()[p.getValue().length - 1].toString());
                    statAddProperty.executeUpdate();
                }
                rs.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void addValue(Property property, String string) {
        try {
            if (property.getPropertyClass().getName().contentEquals("java.lang.Double")) {
                property.add(Double.valueOf(string));
            } else if (property.getPropertyClass().getName().contentEquals("java.lang.String")) {
                property.add(string);
            }
        } catch (InvalidObjectTypeException ex) {
            ex.printStackTrace();
        }
    }

    public double getStrength() {
        return strength;
    }

    public Actor getSource() {
        return start;
    }

    public Actor getDestination() {
        return finish;
    }

    public void set(Actor l, double strength, Actor r) {
        try {
            if (id < 0) {
                start = l;
                this.strength = strength;
                finish = r;
                insertLink();
            } else {
                int sourceID = getActorID(l);
                int destID = getActorID(r);
                if ((sourceID > 0) && (destID > 0)) {
                    statSetAll.clearParameters();
                    statSetAll.setInt(1, sourceID);
                    statSetAll.setDouble(2, strength);
                    statSetAll.setInt(3, destID);
                    statSetAll.executeUpdate();
                } else {
                    System.err.println("At least one of the actors set is not present");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void setSource(Actor u) {
        try {
            start = u;
            if ((finish != null) && (id < 0)) {
                insertLink();
            } else if (id >= 0) {
                int actorID = getActorID(u);
                if (actorID >= 0) {
                    statSetSource.clearParameters();
                    statSetSource.setInt(1, actorID);
                    statSetSource.setInt(1, id);
                    statSetSource.executeUpdate();
                } else {
                    System.err.println("Destination actor (" + u.getType() + "," + u.getID() + ") is not in the database");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    public void setDestination(Actor u) {
        try {
            finish = u;
            if ((start != null) && (id < 0)) {
                insertLink();
            } else if (id >= 0) {
                int actorID = getActorID(u);
                if (actorID >= 0) {
                    statSetDestination.clearParameters();
                    statSetDestination.setInt(1, actorID);
                    statSetDestination.setInt(1, id);
                    statSetDestination.executeUpdate();
                } else {
                    System.err.println("Destination actor (" + u.getType() + "," + u.getID() + ") is not in the database");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    public void set(double str) {
        try {
            strength = str;
            if (id >= 0) {
                statSetStrength.clearParameters();
                statSetStrength.setDouble(1, str);
                statSetStrength.setInt(1, id);
                statSetStrength.executeUpdate();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    private void insertLink() {
        try {
            int s = getActorID(start);
            int f = getActorID(finish);
            if ((s > 0) && (f > 0)) {
                statSetNumID.clearParameters();
                statSetNumID.setString(1, type);
                statSetNumID.setInt(2, s);
                statSetNumID.setDouble(3, strength);
                statSetNumID.setInt(4, f);
                statSetNumID.executeUpdate();

                statGetNumID.clearParameters();
                statGetNumID.setString(1, type);
                statGetNumID.setInt(2, s);
                statGetNumID.setDouble(3, strength);
                statGetNumID.setInt(4, f);
                ResultSet rs = statGetNumID.executeQuery();
                if (rs.next()) {
                    id = rs.getInt("id");
                } else {
                    System.err.println("Added link to database but then couldn't find it");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    /**
     * Get thye numerical ID that uniquely describes this Actor in the database
     */
    protected int getActorID(Actor u) {
        int ret = -1;
        try {
            statGetNumID.clearParameters();
            statGetNumID.setString(1, u.getID());
            statGetNumID.setString(2, u.getType());
            ResultSet rs = statGetNumID.executeQuery();
            if (rs.next()) {
                ret = rs.getInt("id");
            }
            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return ret;
    }
}
