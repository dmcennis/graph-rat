/*
 * AssociativeMiningItemsDB - created 14/03/2009 - 6:33:53 PM
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.graph.property.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import nz.ac.waikato.mcennis.rat.graph.actor.Actor;
import nz.ac.waikato.mcennis.rat.graph.actor.ActorFactory;
import nz.ac.waikato.mcennis.rat.graph.algorithm.collaborativefiltering.AssociativeMiningItems;

/**
 *
 * @author Daniel McEnnis
 */
public class AssociativeMiningItemsDB extends AbstractPropertyDB<AssociativeMiningItems> {

    PreparedStatement get = null;
    PreparedStatement getActorID = null;
    PreparedStatement getActorByIDStatement = null;
    PreparedStatement getKey = null;
    PreparedStatement getActorKey = null;
    PreparedStatement put = null;
    PreparedStatement putActor = null;
    PreparedStatement deleteMining = null;
    PreparedStatement deleteMiningActor = null;

    public StringDB newCopy() {
        return new StringDB();
    }

    public void initializeDatabase(Connection conn) {
        if (conn != null) {
            try {
                Statement stat = conn.createStatement();
                stat.executeUpdate("CREATE TABLE AssociativeMiningItems (" + "id integer not null generated ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)," + "is_positive boolean not null, " + "significance double not null, " + "primary key(id))");
                stat.executeUpdate("CREATE TABLE AssociativeMiningItems_Actor (" + "associativemining integer not null )," + "actor int not null, " + "primary key(associativemining,actor))");
            } catch (SQLException ex) {
                Logger.getLogger(URLDB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void setConnection(Connection con) {
        try {
            conn = con;
            get = conn.prepareStatement("SELECT is_positive, significance FROM AssociativeMiningItems WHERE id=?");
            getActorID = conn.prepareStatement("SELECT actor FROM AssociativeMiningItems_Actor WHERE associativemining=?");
            getActorByIDStatement = conn.prepareStatement("SELECT * FROM Actor WHERE id=?");
            getKey = conn.prepareStatement("SELECT MAX(id) FROM AssociativeMiningItems WHERE is_positive=? and significance=?");
            getActorKey = conn.prepareStatement("SELECT id FROM Actor WHERE type=? and name=?");
            put = conn.prepareStatement("INSERT INTO AssociativeMiningItems (is_positive,significance) VALUES (?,?)");
            putActor = conn.prepareStatement("INSERT INTO AssociativeMiningItems_Actor (associativemining,actor) VALUES (?,?)");
            deleteMining = conn.prepareStatement("DELETE FROM AssociativeMiningItems WHERE id=?");
            deleteMiningActor = conn.prepareStatement("DELETE FROM AssociativeMiningItems_Actor WHERE associativemining=?");
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

    public AssociativeMiningItems get(int i) {
        AssociativeMiningItems ret = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        ResultSet rs3 = null;
        try {
            get.clearParameters();
            get.setInt(1, i);
            rs = get.executeQuery();
            if (rs.next()) {
                boolean positive = rs.getBoolean("is_positive");
                double significance = rs.getDouble("significance");
                LinkedList<Actor> contents = new LinkedList<Actor>();
                getActorID.clearParameters();
                getActorID.setInt(1, i);
                rs2 = getActorID.executeQuery();
                while (rs2.next()) {
                    getActorByIDStatement.clearParameters();

                    getActorByIDStatement.setInt(1, rs2.getInt("actor"));

                    rs3 = getActorByIDStatement.executeQuery();
                    if (rs3.next()) {
                        contents.add(ActorFactory.newInstance().create(rs3.getString("type"), rs3.getString("name")));
                    }
                    rs3.close();
                }
                rs2.close();
                ret = new AssociativeMiningItems(contents, null, null, significance);
                ret.setPositive(positive);
            }
        } catch (SQLException ex) {
            Logger.getLogger(StringDB.class.getName()).log(Level.SEVERE, null, ex);
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
            if (rs3 != null) {
                try {
                    rs3.close();
                } catch (SQLException ex) {
                }
                rs3 = null;
            }
        }
        return ret;
    }

    public int put(AssociativeMiningItems object) {
        int ret = -1;
        ResultSet rs = null;
        ResultSet rs2 = null;
        try {
            put.clearParameters();
            getKey.clearParameters();
            put.setBoolean(1, object.isPositive());
            getKey.setBoolean(1, object.isPositive());
            put.setDouble(2, object.getSignificance());
            getKey.setDouble(2, object.getSignificance());
            put.executeUpdate();
            rs = getKey.executeQuery();
            if (rs.next()) {
                ret = rs.getInt("id");
                Actor[] array = object.getActors();
                if (array != null) {
                    for (int i = 0; i < array.length; ++i) {
                        getActorKey.clearParameters();
                        getActorKey.setString(1, array[i].getMode());
                        getActorKey.setString(2, array[i].getID());
                        rs2 = getActorKey.executeQuery();
                        if (rs2.next()) {
                            putActor.clearParameters();
                            putActor.setInt(1, ret);
                            putActor.setInt(2, rs2.getInt("id"));
                            putActor.executeUpdate();
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(URLDB.class.getName()).log(Level.SEVERE, null, ex);
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

    public void remove(int id) {
        try {
            deleteMiningActor.clearParameters();
            deleteMiningActor.setInt(1, id);
            deleteMiningActor.executeUpdate();
            deleteMining.clearParameters();
            deleteMining.setInt(1, id);
            deleteMining.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(AssociativeMiningItemsDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Class getValueClass() {
        return AssociativeMiningItems.class;
    }
}
