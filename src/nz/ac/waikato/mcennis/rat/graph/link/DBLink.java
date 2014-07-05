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
import org.dynamicfactory.descriptors.*;
import org.dynamicfactory.property.Property;
import org.dynamicfactory.property.PropertyFactory;
import org.dynamicfactory.property.InvalidObjectTypeException;

import java.util.List;

import java.util.Iterator;

import java.util.LinkedList;

import java.util.logging.Level;
import java.util.logging.Logger;

import nz.ac.waikato.mcennis.rat.graph.model.Listener;

import nz.ac.waikato.mcennis.rat.graph.model.Model;

import nz.ac.waikato.mcennis.rat.graph.model.ModelShell;

import org.dynamicfactory.property.PropertyValueDatabaseFactory;
import org.dynamicfactory.property.database.IntegerDB;
import org.dynamicfactory.property.database.PropertyValueDB;



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

    static PreparedStatement statGetRelation = null;

    static PreparedStatement statGetNumID = null;

    static PreparedStatement statSetNumID = null;

    static PreparedStatement statGetActorNumID = null;

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

            statGetNumID = connection.prepareStatement("SELECT id FROM Link WHERE type=? AND start=? AND strength=? AND finish=?");

            statSetNumID = connection.prepareStatement("INSERT INTO Link (type,start,strength,finish) VALUES (?,?,?,?)");

            statGetPropertyIDArray = connection.prepareStatement("SELECT Property.id, Property.type, Property.class FROM LinkProperties, Property WHERE LinkProperties.property=Property.id AND LinkProperties.id=?");

            statGetPropertyID = connection.prepareStatement("SELECT Property.id, Property.type, Property.class FROM LinkProperties, Property WHERE LinkProperties.property=Property.id AND LinkProperties.id=? AND Property.type=?");

            statGetPropertyValues = connection.prepareStatement("SEECT value FROM PropertyValue WHERE property=?");

            statRemoveActorProperty = connection.prepareStatement("DELETE FROM LinkProperties WHERE property=?");

            statRemoveProperty = connection.prepareStatement("DELETE FROM Property WHERE id=?");

            statRemovePropertyValues = connection.prepareStatement("DELETE FROM Property_Value WHERE property=?");

            statAddProperty = connection.prepareStatement("INSERT  INTO LinkProperties (id,property) VALUES (?,?)");

            statAddGetPropertyID = connection.prepareStatement("SELECT MAX(Property.id) FROM Property WHERE type=? AND class=? ");

            statDeleteActorProperty = connection.prepareStatement("DELETE FROM LinkProperty WHERE property=?");

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
                    Property item = PropertyFactory.newInstance().create(propertyType, Integer.toString(propertyID), factory.getValueClass());
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
                    Property item = PropertyFactory.newInstance().create(propertyType, Integer.toString(propertyID), propertyClass);
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



//    @Override

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

    public void setRelation(String type) {

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

    public String getRelation() {

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

        if (type.compareTo(a.getRelation()) == 0) {

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

            return type.compareTo(a.getRelation());

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

        List<Property> leftProp = this.getProperty();



        List<Property> rightProp = right.getProperty();



        if(leftProp.size() != rightProp.size()){

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



        while((lIt.hasNext())&&(rIt.hasNext())){



            ret = lIt.next().compareTo(rIt.next());



            if(ret != 0){



                return ret;



            }



        }

            return 0;

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
            ResultSet rs = null;
            try {

               Property p = (Property) m;

                statGetPropertyID.clearParameters();

                statGetPropertyID.setInt(1, id);

                statGetPropertyID.setString(2, p.getType());

                rs = statGetProperty.executeQuery();

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

            }finally{
                if(rs != null){
                    try {
                        rs.close();
                    } catch (SQLException ex) {}
                    rs = null;
                }
            }

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

                    System.err.println("Destination actor (" + u.getMode() + "," + u.getID() + ") is not in the database");

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

                    System.err.println("Destination actor (" + u.getMode() + "," + u.getID() + ") is not in the database");

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

            statGetNumID.setString(2, u.getMode());

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

    

    public DBLink prototype(){

        return new DBLink();

    }



    public void init(Properties properties) {

        if(!isInitialized()){

        if((properties.get("DerbyDirectory")!=null)&&(properties.get("DerbyDirectory").getParameterClass().getName().contentEquals(String.class.getName()))){

            directory = (String)properties.get("DerbyDirectory").getValue().iterator().next();

        }

        if((properties.get("Database")!=null)&&(properties.get("Database").getParameterClass().getName().contentEquals(String.class.getName()))){

            database = (String)properties.get("Database").getValue().iterator().next();

        }

            DBLink.init();

        }

    }

}

