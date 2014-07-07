/*
 * DerbyGraph.java
 *
 * Created on 24 July 2007, 15:16
 *
 * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt)
 */
package org.mcennis.graphrat.graph;

import java.io.File;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mcennis.graphrat.actor.Actor;
import org.mcennis.graphrat.actor.ActorFactory;
import org.mcennis.graphrat.actor.DBActor;
import org.mcennis.graphrat.link.Link;
import org.mcennis.graphrat.link.DBLink;
import org.mcennis.graphrat.link.LinkFactory;
import org.dynamicfactory.model.Listener;
import org.dynamicfactory.model.Model;
import org.dynamicfactory.model.ModelShell;
import org.mcennis.graphrat.path.NotConstructedError;
import org.mcennis.graphrat.path.Path;
import org.mcennis.graphrat.path.PathFactory;
import org.mcennis.graphrat.path.PathSet;
import org.mcennis.graphrat.path.PathSetFactory;
import org.dynamicfactory.descriptors.*;
import org.dynamicfactory.property.InvalidObjectTypeException;
import org.dynamicfactory.property.Property;
import org.dynamicfactory.property.PropertyFactory;
import org.dynamicfactory.property.PropertyValueDatabaseFactory;
import org.dynamicfactory.property.database.PropertyValueDB;

/**
 * Class that implements all the JDBC code for a Derby backed graph model.  This
 * model differs from persistance in that all loading of actors, links, and properties
 * is done using lazy techniques (they are not createde until requested).  Provided
 * that the appropriate actor class (DBActor by default) is used, persistance is
 * maintained with generated references in a manner similar to hibernate.
 * <br><br>
 * The first time this system is utilized, it requires initialization to create
 * the appropriate tables in the database.  This database must be the same for the
 * corresponding actor class, but is handled by default (unless the class is changed
 * between startup and actor request.)
 * <br><br>
 * The system requires two paramters - the directory of the database and the database
 * name.  All JDBC calls are compiled and have auto-commit disabled.  Periodic calls
 * to commit() are required to maintain consistancy between the database and
 * memory-resident components.
 *
 * @author Daniel McEnnis
 *
 */
public class DerbyGraph extends ModelShell implements Graph, Listener, Comparable {

    public static final long serialVersionUID = 2;
    UserIDList userList = null;
    static Connection connection = null;
    String actorClass = "DBActor";
    PreparedStatement addActorStatement = null;
    PreparedStatement addActorGraphStatement = null;
    PreparedStatement addActorPropertyStatement = null;
    PreparedStatement addLinkStatement = null;
    PreparedStatement addLinkPropertyStatement = null;
    PreparedStatement addLinkGraphStatement = null;
    PreparedStatement addGraphStatement = null;
    PreparedStatement addGraphPropertyStatement = null;
    PreparedStatement addGraphChildStatement = null;
    PreparedStatement addPathSetStatement = null;
    PreparedStatement addPathStatement = null;
    PreparedStatement addPathSetPathStatement = null;
    PreparedStatement addPathActorStatement = null;
    PreparedStatement addPropertyStatement = null;
    PreparedStatement addPropertyValueStatement = null;
    PreparedStatement graphGetIDStatement = null;
    PreparedStatement getActorStatement = null;
    PreparedStatement getActorByTypeStatement = null;
    PreparedStatement getActorByIDStatement = null;
    PreparedStatement getActorByIDTypeStatement = null;
    PreparedStatement getActorIteratorStatement = null;
    PreparedStatement getActorCountStatement = null;
    PreparedStatement getActorPropertiesStatement = null;
    PreparedStatement getActorTypesStatement = null;
    PreparedStatement actorGetIDStatement = null;
    PreparedStatement getActorByIndexStatement = null;
    PreparedStatement getLinkStatement = null;
    PreparedStatement getLinkByTypeStatement = null;
    PreparedStatement getLinkBySourceStatement = null;
    PreparedStatement getLinkByDestStatement = null;
    PreparedStatement getLinkByBothStatement = null;
    PreparedStatement getLinkByActorStatement = null;
    PreparedStatement getLinkTypesStatement = null;
    PreparedStatement getLinkByAllStatement = null;
    PreparedStatement getLinkPropertiesStatement = null;
    PreparedStatement getGraphID = null;
    PreparedStatement getGraphName = null;
    PreparedStatement getGraphChildrenStatement = null;
    PreparedStatement getGraphParentStatement = null;
    PreparedStatement getGraphByLinkGraphStatement = null;
    PreparedStatement getGraphByActorGraphStatement = null;
    PreparedStatement getAddGraphPropertiesStatement = null;
    PreparedStatement getGraphPropertiesStatement = null;
    PreparedStatement getGraphPropertiesByTypeStatement = null;
    PreparedStatement getPathSetStatement = null;
    PreparedStatement getPathSetByNameStatement = null;
    PreparedStatement getPathSetIDStatement = null;
    PreparedStatement getPathIDStatement = null;
    PreparedStatement getPathByPathSetStatement = null;
    PreparedStatement getPathActorStatement = null;
    PreparedStatement getPropertyStatement = null;
    PreparedStatement getPropertyValuesStatement = null;
    PreparedStatement deleteActorStatement = null;
    PreparedStatement deleteActorGraphStatement = null;
    PreparedStatement deleteActorPropertiesStatement = null;
    PreparedStatement deleteLinkStatement = null;
    PreparedStatement deleteLinkByActorStatement = null;
    PreparedStatement deleteLinkGraphStatement = null;
    PreparedStatement deleteLinkPropertiesStatement = null;
    PreparedStatement deletePropertyStatement = null;
    PreparedStatement deletePropertyValuesStatement = null;
    PreparedStatement deleteGraphPropertiesStatement = null;
    PreparedStatement anonymize = null;
    PreparedStatement updateGraphProperty = null;
    int graphID = -1;
    PropertiesInternal parameters = PropertiesFactory.newInstance().create();

    /** Creates a new instance of DerbyGraph */
    public DerbyGraph() {
        ParameterInternal param = ParameterFactory.newInstance().create("GraphID", String.class);
        SyntaxObject restrictionPart = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        param.setRestrictions(restrictionPart);
        param.add("Root");
        parameters.add(param);

        param = ParameterFactory.newInstance().create("DerbyDirectory", File.class);
        restrictionPart = SyntaxCheckerFactory.newInstance().create(1, 1, null, File.class);
        param.setRestrictions(restrictionPart);
        parameters.add(param);

        param = ParameterFactory.newInstance().create("DatabaseString", String.class);
        restrictionPart = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        param.setRestrictions(restrictionPart);
        parameters.add(param);

        param = ParameterFactory.newInstance().create("DatabaseClear", Boolean.class);
        restrictionPart = SyntaxCheckerFactory.newInstance().create(1, 1, null, Boolean.class);
        param.setRestrictions(restrictionPart);
        param.add(false);
        parameters.add(param);

        param = ParameterFactory.newInstance().create("DatabaseInitialization", Boolean.class);
        restrictionPart = SyntaxCheckerFactory.newInstance().create(1, 1, null, Boolean.class);
        param.setRestrictions(restrictionPart);
        param.add(false);
        parameters.add(param);

    }

    public void init(Properties props) {
        if (parameters.check(props)) {

            Iterator<Parameter> it = props.get().iterator();
            while (it.hasNext()) {
                Parameter param = it.next();
                if (parameters.get(param.getType()) != null) {
                    parameters.replace(param);
                } else {
                    parameters.add((ParameterInternal) param);
                }
            }



            if ((Boolean) props.get("DatabaseInitialize").getValue().iterator().next()) {
                try {
                    this.initializeDatabase();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

            try {
                this.startup();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            if ((Boolean) props.get("DatabaseClear").getValue().iterator().next()) {
                this.clear();
            }
        }
    }

    /**
     * Starts the database
     * <ul>
     * <li>Creates a database connection at the directory 'directory' with database
     * name 'database'
     * <li>aquires the graph id of the root graph
     * <li>compiles all the prepared statements used in the class
     * <ul>
     *
     * @throws java.sql.SQLException
     */
    public void startup() throws SQLException {
        if (connection == null) {
            try {
                System.setProperty("derby.system.home", ((File) parameters.get("DerbyDirectory").getValue().iterator().next()).getAbsolutePath());
                Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
                connection = java.sql.DriverManager.getConnection("jdbc:derby:" + (String) parameters.get("DatabaseName").getValue().iterator().next());
                connection.setAutoCommit(false);
                getGraphID.clearParameters();
                getGraphID.setString(1, (String) parameters.get("DatabaseName").getValue().iterator().next());
                ResultSet rs = getGraphID.executeQuery();
                if (rs.next()) {
                    graphID = rs.getInt("id");
                } else {
                    rs.close();
                    addGraphStatement.clearParameters();
                    addGraphStatement.setString(1, (String) parameters.get("DatabaseName").getValue().iterator().next());
                    addGraphStatement.executeUpdate();
                    getGraphID.setString(1, (String) parameters.get("DatabaseName").getValue().iterator().next());
                    rs = getGraphID.executeQuery();
                    rs.next();
                    graphID = rs.getInt("id");
                }
                rs.close();
                prepareStatement();
                connection.setAutoCommit(false);
                initActor();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        } else {
            getGraphID.clearParameters();
            getGraphID.setString(1, (String) parameters.get("DatabaseName").getValue().iterator().next());
            ResultSet rs = getGraphID.executeQuery();
            if (rs.next()) {
                graphID = rs.getInt("id");
            }
        }
    }

    /**
     * Performs initialization of the DBActor class - a static call that
     * prepares all subsequent calls for actor creation.
     *
     * @see org.mcennis.graphrat.actor.DBActor
     *
     */
    protected void initActor() {
        if (!DBActor.isInitialized()) {
            DBActor.setDatabase((String) parameters.get("DatabaseName").getValue().iterator().next());
            DBActor.setDirectory(((File) parameters.get("DerbyDirectory").getValue().iterator().next()).getAbsolutePath());
            DBActor.init();
        }
        ParameterInternal actorType = ParameterFactory.newInstance().create("ActorClass", String.class);
        actorType.add("DBActor");
        ActorFactory.newInstance().setDefaultProperty(actorType);
    }

    protected void initLink() {
        if (!DBLink.isInitialized()) {
            DBLink.setDatabase((String) parameters.get("DatabaseName").getValue().iterator().next());
            DBLink.setDirectory(((File) parameters.get("DerbyDirectory").getValue().iterator().next()).getAbsolutePath());
            DBLink.init();
        }
        ParameterInternal actorType = ParameterFactory.newInstance().create("LinkClass", String.class);
        actorType.add("DBLink");
        LinkFactory.newInstance().setDefaultProperty(actorType);
    }

    /**
     * Prepares all statements utilized by DerbyGraph.
     * @throws java.sql.SQLException
     */
    protected void prepareStatement() throws SQLException {
        try {
            addActorStatement = connection.prepareStatement("INSERT INTO Actor (type,name) VALUES ( ?, ?)");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try {
            actorGetIDStatement = connection.prepareStatement("SELECT id FROM Actor WHERE type=? AND name=?");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try {
            addActorGraphStatement = connection.prepareStatement("INSERT INTO ActorGraph (graph,actor) VALUES (?,?)");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try {
            addActorPropertyStatement = connection.prepareStatement("INSERT INTO ActorProperties (id,property) VALUES ( ?, ? )");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try {
            addGraphStatement = connection.prepareStatement("INSERT INTO Graph (name) VALUES ( ? )");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try {
            graphGetIDStatement = connection.prepareStatement("SELECT id FROM Graph WHERE name=?");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try {
            addGraphPropertyStatement = connection.prepareStatement("INSERT INTO GraphProperties (id,property) VALUES ( ? , ? )");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try {
            addGraphChildStatement = connection.prepareStatement("INSERT INTO SubGraph (parent,child) VALUES ( ?, ?)");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try {
            addLinkStatement = connection.prepareStatement("INSERT INTO Link (type,start,finish,cost) VALUES ( ?, ?, ?, ?)");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try {
            addLinkPropertyStatement = connection.prepareStatement("INSERT INTO LinkProperty (id,property) VALUES (?,?)");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try {
            addLinkGraphStatement = connection.prepareStatement("INSERT INTO LinkGraph (graph,link) VALUES (?,?)");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try {
            addPathStatement = connection.prepareStatement("INSERT INTO Path (name,cost) VALUES ( ? , ?)");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try {
            addPathActorStatement = connection.prepareStatement("INSERT INTO PathActor ( path, actor, index ) VALUES ( ? , ? , ? )");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try {
            addPathSetStatement = connection.prepareStatement("INSERT INTO PathSet (name) VALUES (?)");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try {
            addPathSetPathStatement = connection.prepareStatement("INSERT INTO PathSetPath (pathSet,path) VALUES ( ? , ? )");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try {
            addPropertyStatement = connection.prepareStatement("INSERT INTO Property (type,class) VALUES ( ? , ? )");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try {
            addPropertyValueStatement = connection.prepareStatement("INSERT INTO Property_Value (property,value) VALUES ( ? , ? )");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try {
            getActorStatement = connection.prepareStatement("SELECT * FROM Actor");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try {
            getActorByIDStatement = connection.prepareStatement("SELECT * FROM Actor WHERE id=?");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try {
            getActorByIDTypeStatement = connection.prepareStatement("SELECT * FROM Actor WHERE type=? AND name=?");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try {
            getActorCountStatement = connection.prepareStatement("SELECT COUNT(*) FROM Actor WHERE type=? ");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try {
            getActorByTypeStatement = connection.prepareStatement("SELECT * FROM Actor WHERE type=?");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try {
            getActorPropertiesStatement = connection.prepareStatement("SELECT property FROM ActorProperties WHERE id=?");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try {
            getActorTypesStatement = connection.prepareStatement("SELECT DISTINCT type FROM Actor");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try {
            getActorByIndexStatement = connection.prepareStatement("SELECT * FROM Actor WHERE id=?");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try {
            getGraphID = connection.prepareStatement("SELECT id FROM Graph WHERE name=?");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try {
            getGraphName = connection.prepareStatement("SELECT name FROM Graph WHERE id=?");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try {
            getAddGraphPropertiesStatement = connection.prepareStatement("SELECT MAX(id) FROM Property WHERE type=? AND class=?");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try {
            getGraphPropertiesStatement = connection.prepareStatement("SELECT Property.id, Property.class, Property.type FROM GraphProperties WHERE id=?");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try {
            getGraphPropertiesByTypeStatement = connection.prepareStatement("SELECT Property.id, Property.class  FROM GraphProperties, Property WHERE GraphProperties.property=Property.id AND GraphProperties.id=? AND Property.type=?");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try {
            getGraphByLinkGraphStatement = connection.prepareStatement("SELECT graph FROM LinkGraph WHERE link=? ");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try {
            getGraphByActorGraphStatement = connection.prepareStatement("SELECT graph FROM ActorGraph WHERE actor=? ");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try {
            getGraphChildrenStatement = connection.prepareStatement("SELECT child FROM SubGraph  WHERE parent=?");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try {
            getGraphParentStatement = connection.prepareStatement("SELECT parent FROM SubGraph  WHERE child=?");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try {
            getLinkStatement = connection.prepareStatement("SELECT * FROM Link");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try {
            getLinkByTypeStatement = connection.prepareStatement("SELECT * FROM Link WHERE type=?");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try {
            getLinkBySourceStatement = connection.prepareStatement("SELECT * FROM Link WHERE type=? AND start=?");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try {
            getLinkByDestStatement = connection.prepareStatement("SELECT * FROM Link WHERE type=? AND finish=?");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try {
            getLinkByBothStatement = connection.prepareStatement("SELECT * FROM Link WHERE type=? AND start=? AND finish=?");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try {
            getLinkByActorStatement = connection.prepareStatement("SELECT id FROM Link WHERE start=? OR finish=?");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try {
            getLinkTypesStatement = connection.prepareStatement("SELECT DISTINCT type FROM LINK");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try {
            getLinkByAllStatement = connection.prepareStatement("SELECT id FROM Link WHERE cost=? AND start=? AND finish=?");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try {
            getLinkPropertiesStatement = connection.prepareStatement("SELECT property FROM LinkProperty WHERE id=?");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try {
            getPathSetStatement = connection.prepareStatement("SELECT * FROM PathSet");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try {
            getPathSetByNameStatement = connection.prepareStatement("SELECT * FROM PathSet WHERE name=?");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try {
            getPathSetIDStatement = connection.prepareStatement("SELECT id FROM PathSet WHERE name=?");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try {
            getPathByPathSetStatement = connection.prepareStatement("SELECT * FROM Path, PathSetPath WHERE pathSet=? AND PathSetPath.path=Path.id");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try {
            getPathActorStatement = connection.prepareStatement("SELECT * FROM Actor,PathActor WHERE PathActor.path=? AND PathActor.actor = Actor.id ORDER BY PathActor.index");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try {
            getPathIDStatement = connection.prepareStatement("SELECT max(id) FROM Path");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try {
            getPropertyStatement = connection.prepareStatement("SELECT type , class FROM Property WHERE id=?");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try {
            getPropertyValuesStatement = connection.prepareStatement("SELECT value FROM Property_Value WHERE property = ?");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try {
            deleteActorStatement = connection.prepareStatement("DELETE FROM Actor WHERE id=?");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try {
            deleteActorGraphStatement = connection.prepareStatement("DELETE FROM ActorGraph WHERE actor=? AND graph=?");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try {
            deleteActorPropertiesStatement = connection.prepareStatement("DELETE FROM ActorProperties WHERE id=?");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try {
            deleteLinkByActorStatement = connection.prepareStatement("DELETE FROM Link WHERE start=? OR finish=?");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try {
            deleteLinkStatement = connection.prepareStatement("DELETE FROM Link WHERE id=?");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try {
            deleteLinkGraphStatement = connection.prepareStatement("DELETE FROM LinkGraph WHERE link=?");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try {
            deleteLinkPropertiesStatement = connection.prepareStatement("DELETE FROM LinkProperties WHERE id=?");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try {
            deletePropertyValuesStatement = connection.prepareStatement("DELETE FROM Property_Value WHERE property=?");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try {
            deletePropertyStatement = connection.prepareStatement("DELETE FROM Property WHERE id=?");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try {
            deleteGraphPropertiesStatement = connection.prepareStatement("DELETE FROM GraphProperties WHERE property=?");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        try {
            anonymize = connection.prepareStatement("UPDATE Actor SET name=? WHERE id=?");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try {
            updateGraphProperty = connection.prepareStatement("UPDATE GraphProperties SET value=? WHERE id=? AND type=?");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        PropertyValueDatabaseFactory.newInstance().init(connection);
    }

    /**
     * The first call to be made if the database has never been used before.  It
     * creates its own connection, so startup is not needed before.  If called on
     * an already generated table, the system will report errors, but not damage
     * the table. Also closes its own connection, so close is not needed either.
     * @throws java.sql.SQLException
     */
    public void initializeDatabase() throws SQLException {
        System.setProperty("derby.system.home", ((File) parameters.get("DerbyDirectory").getValue().iterator().next()).getAbsolutePath());
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            try {
                connection = java.sql.DriverManager.getConnection("jdbc:derby:" + (String) parameters.get("DatabaseName").getValue().iterator().next() + ";create=TRUE");
            } catch (SQLException ex) {
                System.err.println("WARNING: " + ex.getMessage());
                connection = java.sql.DriverManager.getConnection("jdbc:derby:" + (String) parameters.get("DatabaseName").getValue().iterator().next());
            }
            Statement stat = connection.createStatement();
            try {
                stat.executeUpdate("CREATE TABLE Graph (" +
                        "id integer not null generated ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)," +
                        "name varchar(256) not null, " +
                        "primary key(id))");
            } catch (SQLException ex) {
                System.err.println("GRAPH: " + ex.getMessage());
            }
            try {
                stat.executeUpdate("CREATE TABLE SubGraph (" +
                        "parent integer not null, " +
                        "child integer not null, " +
                        //                    "FOREIGN KEY (parent) REFERENCES Graph(id)," +
                        //                    "FOREIGN KEY (child) REFERENCES Graph(id)," +
                        "PRIMARY KEY(parent,child)" +
                        ")");
            } catch (SQLException ex) {
                System.err.println("SUBGRAPH: " + ex.getMessage());
            }
            try {
                stat.executeUpdate("CREATE TABLE Actor (" +
                        "id integer not null generated ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)," +
                        "type VARCHAR(64) not null, " +
                        "name VARCHAR(256) not null, " +
                        "PRIMARY KEY(id)" +
                        ")");
                stat.executeUpdate("CREATE INDEX actor_type ON Actor(type)");
                stat.executeUpdate("CREATE INDEX actor_name ON Actor(name)");
                stat.executeUpdate("CREATE INDEX actor_type_name ON Actor(type,name)");
            } catch (SQLException ex) {
                System.err.println("ACTOR: " + ex.getMessage());
            }
            try {
                stat.executeUpdate("CREATE TABLE ActorGraph (" +
                        "graph integer not null," +
                        "actor integer not null," +
                        //                    "FOREIGN KEY (graph) REFERENCES Graph(id)," +
                        //                    "FOREIGN KEY (actor) REFERENCES Actor(id)," +
                        "PRIMARY KEY (graph,actor)" +
                        ")");
            } catch (SQLException ex) {
                System.err.println("ACTORGRAPH: " + ex.getMessage());
            }
            try {
                stat.executeUpdate("CREATE TABLE ActorProperties(" +
                        "id integer not null, " +
                        "property integer not null , " +
                        //                    "FOREIGN KEY(id) REFERENCES Actor(id), " +
                        "PRIMARY KEY(id,property)" +
                        ")");
            } catch (SQLException ex) {
                System.err.println("ACTORPROPERTIES: " + ex.getMessage());
            }
            try {
                stat.executeUpdate("CREATE TABLE GraphProperties (" +
                        "id integer not null, " +
                        "property integer not null , " +
                        //                    "FOREIGN KEY(id) REFERENCES Graph(id), " +
                        "PRIMARY KEY(id,property)" +
                        ")");
            } catch (SQLException ex) {
                System.err.println("GRAPHPROPERTIES: " + ex.getMessage());
            }
            try {
                stat.executeUpdate("CREATE TABLE Property (" +
                        "id integer not null, " +
                        "type VARCHAR(256) not null , " +
                        "class VARCHAR(256) not null , " +
                        //                    "FOREIGN KEY(id) REFERENCES Graph(id), " +
                        "PRIMARY KEY(id)" +
                        ")");
                stat.executeUpdate("CREATE INDEX Property_Type ON Property(type)");
            } catch (SQLException ex) {
                System.err.println("PROPERTY: " + ex.getMessage());
            }
            try {
                stat.executeUpdate("CREATE TABLE Property_Value (" +
                        "property integer not null, " +
                        "value integer not null , " +
                        //                    "FOREIGN KEY(id) REFERENCES Graph(id), " +
                        "PRIMARY KEY(property,value)" +
                        ")");
                stat.executeUpdate("CREATE INDEX Property_Type ON Property(type)");
            } catch (SQLException ex) {
                System.err.println("PROPERTYVALUE: " + ex.getMessage());
            }
            try {
                stat.executeUpdate("CREATE TABLE Link (" +
                        "id integer not null generated ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
                        "type VARchar(256)," +
                        "start integer, " +
                        "finish integer, " +
                        "cost double, " +
                        //                    "FOREIGN KEY (start) REFERENCES Actor(id)," +
                        //                    "FOREIGN KEY (finish) REFERENCES Actor(id)," +
                        "PRIMARY KEY (id)" +
                        ")");
                stat.executeUpdate("CREATE INDEX Link_type ON Link(type)");
                stat.executeUpdate("CREATE INDEX Link_type_source ON Link(type,start)");
                stat.executeUpdate("CREATE INDEX Link_type_dest ON Link(type,finish)");
                stat.executeUpdate("CREATE INDEX Link_start_finish ON Link(type,start,finish)");
            } catch (SQLException ex) {
                System.err.println("LINK: " + ex.getMessage());
            }
            try {
                stat.executeUpdate("CREATE TABLE LinkProperties (" +
                        "id integer not null, " +
                        "property integer not null , " +
                        //                    "FOREIGN KEY(id) REFERENCES Graph(id), " +
                        "PRIMARY KEY(id,property)" +
                        ")");
            } catch (SQLException ex) {
                System.err.println("LINKPROPERTIES: " + ex.getMessage());
            }
            try {
                stat.executeUpdate("CREATE Table LinkGraph (" +
                        "graph integer not null, " +
                        "link integer not null, " +
                        //                    "FOREIGN KEY (link) REFERENCES Link(id), " +
                        //                    "FOREIGN KEY (graph) REFERENCES Graph(id), " +
                        "PRIMARY KEY (link,graph)" +
                        ")");
            } catch (SQLException ex) {
                System.err.println("LINKGRAPH: " + ex.getMessage());
            }
            try {
                stat.executeUpdate("CREATE TABLE PathSet (" +
                        "id integer not null generated ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
                        "name VARchar(256) not null unique, " +
                        "PRIMARY KEY (id)" +
                        ")");
            } catch (SQLException ex) {
                System.err.println("PATHSET: " + ex.getMessage());
            }
            try {
                stat.executeUpdate("CREATE TABLE Path (" +
                        "id integer not null generated ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
                        "name VARchar(256) not null, " +
                        "cost double, " +
                        "PRIMARY KEY (id) " +
                        ")");
            } catch (SQLException ex) {
                System.err.println("PATH: " + ex.getMessage());
            }
            try {
                stat.executeUpdate("CREATE TABLE PathActor (" +
                        "path integer not null, " +
                        "actor integer not null, " +
                        "index integer not null, " +
                        //                    "FOREIGN KEY(path) REFERENCES Path(id), " +
                        //                    "FOREIGN KEY(actor) REFERENCES Actor(id), " +
                        "PRIMARY KEY (path,actor)" +
                        ")");
            } catch (SQLException ex) {
                System.err.println("PATHACTOR: " + ex.getMessage());
            }
            try {
                stat.executeUpdate("CREATE TABLE PathSetPath (" +
                        "pathSet integer not null, " +
                        "path integer not null, " +
                        //                    "FOREIGN KEY (path) REFERENCES Path(id), " +
                        //                    "FOREIGN KEY (pathSet) REFERENCES PathSet(id), " +
                        "PRIMARY KEY (pathSet,path)" +
                        ")");
            } catch (SQLException ex) {
                System.err.println("PATHSETPATH: " + ex.getMessage());
            }
            try {
                stat.executeUpdate("INSERT INTO Graph (name) VALUES ( '" + (String) parameters.get("DatabaseName").getValue().iterator().next() + "' )");
            } catch (SQLException ex) {
                System.err.println("INSERT: " + ex.getMessage());
            }
            PropertyValueDatabaseFactory.newInstance().initializeDatabase(connection);
            connection.close();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    protected void finalize() throws Throwable {

        this.close();

    }

    /**
     * Attempts to release all resources created by the startup method
     */
    public void close() {
        try {
            connection.commit();
            if (addActorStatement != null) {
                addActorStatement.close();
                addActorStatement = null;
            }
            if (addActorGraphStatement != null) {
                addActorGraphStatement.close();
                addActorGraphStatement = null;
            }
            if (addActorPropertyStatement != null) {
                addActorPropertyStatement.close();
                addActorPropertyStatement = null;
            }
            if (addLinkStatement != null) {
                addLinkStatement.close();
                addLinkStatement = null;
            }
            if (addLinkPropertyStatement != null) {
                addLinkPropertyStatement.close();
                addLinkPropertyStatement = null;
            }
            if (addGraphStatement != null) {
                addGraphStatement.close();
                addGraphStatement = null;
            }
            if (addGraphPropertyStatement != null) {
                addGraphPropertyStatement.close();
                addGraphPropertyStatement = null;
            }
            if (addGraphChildStatement != null) {
                addGraphChildStatement.close();
                addGraphChildStatement = null;
            }
            if (addPathSetStatement != null) {
                addPathSetStatement.close();
                addPathSetStatement = null;
            }
            if (addPathStatement != null) {
                addPathStatement.close();
                addPathStatement = null;
            }
            if (addPathSetPathStatement != null) {
                addPathSetPathStatement.close();
                addPathSetPathStatement = null;
            }
            if (addPathActorStatement != null) {
                addPathActorStatement.close();
                addPathActorStatement = null;
            }
            if (addLinkGraphStatement != null) {
                addLinkGraphStatement.close();
                addLinkGraphStatement = null;
            }
            if (addPropertyStatement != null) {
                addPropertyStatement.close();
                addPropertyStatement = null;
            }
            if (addPropertyValueStatement != null) {
                addPropertyValueStatement.close();
                addPropertyValueStatement = null;
            }
            if (graphGetIDStatement != null) {
                graphGetIDStatement.close();
                graphGetIDStatement = null;
            }
            if (getGraphID != null) {
                getGraphID.close();
                getGraphID = null;
            }
            if (getGraphName != null) {
                getGraphName.close();
                getGraphName = null;
            }
            if (getActorStatement != null) {
                getActorStatement.close();
                getActorStatement = null;
            }
            if (getActorByTypeStatement != null) {
                getActorByTypeStatement.close();
                getActorByTypeStatement = null;
            }
            if (getActorByIDStatement != null) {
                getActorByIDStatement.close();
                getActorByIDStatement = null;
            }
            if (getActorByIDTypeStatement != null) {
                getActorByIDTypeStatement.close();
                getActorByIDTypeStatement = null;
            }
            if (getActorCountStatement != null) {
                getActorCountStatement.close();
                getActorCountStatement = null;
            }
            if (getActorIteratorStatement != null) {
                getActorIteratorStatement.close();
                getActorIteratorStatement = null;
            }
            if (getActorPropertiesStatement != null) {
                getActorPropertiesStatement.close();
                getActorPropertiesStatement = null;
            }
            if (getActorTypesStatement != null) {
                getActorTypesStatement.close();
                getActorTypesStatement = null;
            }
            if (actorGetIDStatement != null) {
                actorGetIDStatement.close();
                actorGetIDStatement = null;
            }
            if (getActorByIndexStatement != null) {
                getActorByIndexStatement.close();
                getActorByIndexStatement = null;
            }
            if (getLinkStatement != null) {
                getLinkStatement.close();
                getLinkStatement = null;
            }
            if (getLinkByTypeStatement != null) {
                getLinkByTypeStatement.close();
                getLinkByTypeStatement = null;
            }
            if (getLinkBySourceStatement != null) {
                getLinkBySourceStatement.close();
                getLinkBySourceStatement = null;
            }
            if (getLinkByDestStatement != null) {
                getLinkByDestStatement.close();
                getLinkByDestStatement = null;
            }
            if (getLinkByBothStatement != null) {
                getLinkByBothStatement.close();
                getLinkByBothStatement = null;
            }
            if (getLinkByActorStatement != null) {
                getLinkByActorStatement.close();
                getLinkByActorStatement = null;
            }
            if (getLinkTypesStatement != null) {
                getLinkTypesStatement.close();
                getLinkTypesStatement = null;
            }
            if (getLinkByAllStatement != null) {
                getLinkByAllStatement.close();
                getLinkByAllStatement = null;
            }
            if (getLinkPropertiesStatement != null) {
                getLinkPropertiesStatement.close();
                getLinkPropertiesStatement = null;
            }
            if (getAddGraphPropertiesStatement != null) {
                getAddGraphPropertiesStatement.close();
                getAddGraphPropertiesStatement = null;
            }
            if (getGraphPropertiesStatement != null) {
                getGraphPropertiesStatement.close();
                getGraphPropertiesStatement = null;
            }
            if (getGraphPropertiesByTypeStatement != null) {
                getGraphPropertiesByTypeStatement.close();
                getGraphPropertiesByTypeStatement = null;
            }
            if (getGraphChildrenStatement != null) {
                getGraphChildrenStatement.close();
                getGraphChildrenStatement = null;
            }
            if (getGraphParentStatement != null) {
                getGraphParentStatement.close();
                getGraphParentStatement = null;
            }
            if (getPathSetStatement != null) {
                getPathSetStatement.close();
                getPathSetStatement = null;
            }
            if (getPathSetByNameStatement != null) {
                getPathSetByNameStatement.close();
                getPathSetByNameStatement = null;
            }
            if (getPathSetIDStatement != null) {
                getPathSetIDStatement.close();
                getPathSetIDStatement = null;
            }
            if (getPathIDStatement != null) {
                getPathIDStatement.close();
                getPathIDStatement = null;
            }
            if (getPathByPathSetStatement != null) {
                getPathByPathSetStatement.close();
                getPathByPathSetStatement = null;
            }
            if (getPathActorStatement != null) {
                getPathActorStatement.close();
                getPathActorStatement = null;
            }
            if (getGraphByLinkGraphStatement != null) {
                getGraphByLinkGraphStatement.close();
                getGraphByLinkGraphStatement = null;
            }
            if (getGraphByActorGraphStatement != null) {
                getGraphByActorGraphStatement.close();
                getGraphByActorGraphStatement = null;
            }
            if (getPropertyStatement != null) {
                getPropertyStatement.close();
                getPropertyStatement = null;
            }
            if (getPropertyValuesStatement != null) {
                getPropertyValuesStatement.close();
                getPropertyValuesStatement = null;
            }
            if (deleteActorStatement != null) {
                deleteActorStatement.close();
                deleteActorStatement = null;
            }
            if (deleteActorGraphStatement != null) {
                deleteActorGraphStatement.close();
                deleteActorGraphStatement = null;
            }
            if (deleteActorPropertiesStatement != null) {
                deleteActorPropertiesStatement.close();
                deleteActorPropertiesStatement = null;
            }
            if (deleteLinkStatement != null) {
                deleteLinkStatement.close();
                deleteLinkStatement = null;
            }
            if (deleteLinkByActorStatement != null) {
                deleteLinkByActorStatement.close();
                deleteLinkByActorStatement = null;
            }
            if (deleteLinkGraphStatement != null) {
                deleteLinkGraphStatement.close();
                deleteLinkGraphStatement = null;
            }
            if (deleteLinkPropertiesStatement != null) {
                deleteLinkPropertiesStatement.close();
                deleteLinkPropertiesStatement = null;
            }
            if (deletePropertyStatement != null) {
                deletePropertyStatement.close();
                deletePropertyStatement = null;
            }
            if (deletePropertyValuesStatement != null) {
                deletePropertyValuesStatement.close();
                deletePropertyValuesStatement = null;
            }
            if (deleteGraphPropertiesStatement != null) {
                deleteGraphPropertiesStatement.close();
                deleteGraphPropertiesStatement = null;
            }
        } catch (SQLException ex) {
        }
        try {
            if (connection != null) {
                connection.close();
                connection = null;
            }
        } catch (SQLException ex) {
        }
    }

    /**
     * Determine if 'startup' has been called for this graph.
     * @return whether or not this graph has been initialized
     */
    public boolean isInitialized() {
        if (connection != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Removes all data from the database.  Assumes that startup has been called.
     */
    public void clear() {
        Statement stat = null;
        try {
            stat = connection.createStatement();
            stat.executeUpdate("DELETE FROM PathActor");
            stat.executeUpdate("DELETE FROM PathSetPath");
            stat.executeUpdate("DELETE FROM Path");
            stat.executeUpdate("DELETE FROM PathSet");
            stat.executeUpdate("DELETE FROM Link");
            stat.executeUpdate("DELETE FROM ActorGraph");
            stat.executeUpdate("DELETE FROM ActorProperties");
            stat.executeUpdate("DELETE FROM GraphProperties");
            stat.executeUpdate("DELETE FROM LinkProperties");
            stat.executeUpdate("DELETE FROM Actor");
            stat.executeUpdate("DELETE FROM Subgraph");
            stat.executeUpdate("DELETE FROM Graph");
            PropertyValueDatabaseFactory.newInstance().clearDatabase(connection);
            stat.executeUpdate("INSERT INTO Graph (name) VALUES ( '" + (String) parameters.get("DatabaseName").getValue().iterator().next() + "' )");
        } catch (SQLException ex) {
            if (stat != null) {
                try {
                    stat.close();
                } catch (SQLException ex2) {
                }
                stat = null;
            }
            ex.printStackTrace();
        }
    }

    /**
     * Attempts to remove the database tables so that initializeDatabase will be needed
     * to utilize the graph again.
     */
    public void deleteDatabase() {
        try {
            System.setProperty("derby.system.home", ((File) parameters.get("DerbyDirectory").getValue().iterator().next()).getAbsolutePath());
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            Statement stat = null;
            try {
                connection = java.sql.DriverManager.getConnection("jdbc:derby:" + (String) parameters.get("DatabaseName").getValue().iterator().next());
                stat = connection.createStatement();
                stat.executeUpdate("DROP TABLE PathActor");
                stat.executeUpdate("DROP TABLE PathSetPath");
                stat.executeUpdate("DROP TABLE Path");
                stat.executeUpdate("DROP TABLE PathSet");
                stat.executeUpdate("DROP TABLE Link");
                stat.executeUpdate("DROP TABLE ActorGraph");
                stat.executeUpdate("DROP TABLE ActorProperties");
                stat.executeUpdate("DROP TABLE GraphProperties");
                stat.executeUpdate("DROP TABLE Actor");
                stat.executeUpdate("DROP TABLE Subgraph");
                stat.executeUpdate("DROP TABLE Graph");
            //           stat.executeUpdate("DELETE DATABASE "+database);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            if (stat != null) {
                try {
                    stat.close();
                } catch (SQLException ex) {
                }
                stat = null;
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                }
                connection = null;
            }
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void add(Actor u) {
        if ((userList == null) || (userList.getActor(u.getMode(), u.getID()) != null)) {
            try {
                int id = -1;
                ResultSet idSet = actorGetIDStatement.executeQuery();
                if (idSet.next()) {
                    id = idSet.getInt("id");
                }
                idSet.close();
                if (id == -1) {
                    addActorStatement.clearParameters();
                    addActorStatement.setString(1, u.getMode());
                    addActorStatement.setString(2, u.getID());
                    addActorStatement.executeUpdate();

                    actorGetIDStatement.clearParameters();
                    actorGetIDStatement.setString(1, u.getMode());
                    actorGetIDStatement.setString(2, u.getID());
                    idSet = actorGetIDStatement.executeQuery();
                    if (idSet.next()) {
                        id = idSet.getInt("id");
                    }
                    idSet.close();

                    //               ResultSet rs = getGraphID.executeQuery();
                    //               int gID = -1;
                    //               if (rs.next()) {
                    //                   gID = rs.getInt("id");
                    //               }
                    //               rs.close();
                    Iterator<Property> propertyIt = u.getProperty().iterator();
                    while (propertyIt.hasNext()) {
                        Property props = propertyIt.next();
                        Iterator values = props.getValue().iterator();
                        if (!values.hasNext()) {
                            addActorPropertyStatement.clearParameters();
                            addActorPropertyStatement.setInt(1, id);
                            addActorPropertyStatement.setString(2, props.getClass().getName());
                            addActorPropertyStatement.setString(3, props.getType());
                            addActorPropertyStatement.setString(4, "");
                            addActorPropertyStatement.executeUpdate();
                        } else {
                            while (values.hasNext()) {
                                addActorPropertyStatement.clearParameters();
                                addActorPropertyStatement.setInt(1, id);
                                addActorPropertyStatement.setString(2, props.getClass().getName());
                                addActorPropertyStatement.setString(3, props.getType());
                                addActorPropertyStatement.setString(4, values.next().toString());
                                addActorPropertyStatement.executeUpdate();
                            }
                        }
                    }
                }
                addActorGraphStatement.clearParameters();
                addActorGraphStatement.setInt(1, graphID);
                addActorGraphStatement.setInt(2, id);
                addActorGraphStatement.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void add(Link link) {
        if ((userList == null) || (userList.getActor(link.getDestination().getMode(), link.getDestination().getID()) != null)) {
            try {
                int source = -1;
                Actor a = link.getSource();
                actorGetIDStatement.clearParameters();
                actorGetIDStatement.setString(1, a.getMode());
                actorGetIDStatement.setString(2, a.getID());
                ResultSet idSet = actorGetIDStatement.executeQuery();
                if (idSet.next()) {
                    source = idSet.getInt("id");
                }
                idSet.close();

                int dest = -1;
                a = link.getDestination();
                actorGetIDStatement.clearParameters();
                actorGetIDStatement.setString(1, a.getMode());
                actorGetIDStatement.setString(2, a.getID());
                idSet = actorGetIDStatement.executeQuery();
                if (idSet.next()) {
                    dest = idSet.getInt("id");
                }
                idSet.close();

                int linkID = -1;
                getLinkByAllStatement.clearParameters();
                getLinkByAllStatement.setDouble(1, link.getStrength());
                getLinkByAllStatement.setInt(2, source);
                getLinkByAllStatement.setInt(3, dest);
                ResultSet id = getLinkByAllStatement.executeQuery();
                if (id.next()) {
                    linkID = id.getInt("id");
                }
                id.close();
                if (linkID == -1) {
                    addLinkStatement.clearParameters();
                    addLinkStatement.setString(1, link.getRelation());
                    addLinkStatement.setInt(2, source);
                    addLinkStatement.setInt(3, dest);
                    addLinkStatement.setDouble(4, link.getStrength());
                    addLinkStatement.executeUpdate();

                    getLinkByAllStatement.clearParameters();
                    getLinkByAllStatement.setDouble(1, link.getStrength());
                    getLinkByAllStatement.setInt(2, source);
                    getLinkByAllStatement.setInt(3, dest);
                    id = getLinkByAllStatement.executeQuery();
                    if (id.next()) {
                        linkID = id.getInt("id");
                    }
                    id.close();
                    Iterator<Property> linkPropsIt = link.getProperty().iterator();
                    while (linkPropsIt.hasNext()) {
                        Property linkProps = linkPropsIt.next();
                        Iterator values = linkProps.getValue().iterator();
                        if (!values.hasNext()) {
                            addLinkPropertyStatement.clearParameters();
                            addLinkPropertyStatement.setInt(1, linkID);
                            addLinkPropertyStatement.setString(2, linkProps.getClass().getName());
                            addLinkPropertyStatement.setString(3, linkProps.getType());
                            addLinkPropertyStatement.setString(4, "");
                            addLinkPropertyStatement.executeUpdate();
                        } else {
                            while (values.hasNext()) {
                                addLinkPropertyStatement.clearParameters();
                                addLinkPropertyStatement.setInt(1, linkID);
                                addLinkPropertyStatement.setString(2, linkProps.getClass().getName());
                                addLinkPropertyStatement.setString(3, linkProps.getType());
                                addLinkPropertyStatement.setString(4, values.next().toString());
                                addLinkPropertyStatement.executeUpdate();
                            }
                        }

                    }
                }
                addLinkGraphStatement.clearParameters();
                addLinkGraphStatement.setInt(1, graphID);
                addLinkGraphStatement.setInt(2, linkID);
                addLinkGraphStatement.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void remove(Actor u) {
        try {
            // Get Actor ID
            getActorByIDTypeStatement.clearParameters();
            getActorByIDTypeStatement.setString(1, u.getMode());
            getActorByIDTypeStatement.setString(2, u.getID());
            ResultSet actorRS = getActorByIDTypeStatement.executeQuery();
            if (actorRS.next()) {
                int actorID = actorRS.getInt("id");
                // remove ActorGraph
                deleteActorGraphStatement.clearParameters();
                deleteActorGraphStatement.setInt(1, actorID);
                deleteActorGraphStatement.setInt(2, graphID);
                deleteActorGraphStatement.executeUpdate();
                getLinkByActorStatement.clearParameters();
                getLinkByActorStatement.setInt(1, actorID);
                getLinkByActorStatement.setInt(2, actorID);
                ResultSet linkID = getLinkByActorStatement.executeQuery();
                while (linkID.next()) {
                    int link = linkID.getInt("ID");
                    removeLink(link);
                }
                linkID.close();
                getGraphByActorGraphStatement.clearParameters();
                getGraphByActorGraphStatement.setInt(1, actorID);
                ResultSet test = getGraphByActorGraphStatement.executeQuery();
                if (!test.next()) {
                    deleteActorPropertiesStatement.clearParameters();
                    deleteActorPropertiesStatement.setInt(1, actorID);
                    deleteActorPropertiesStatement.executeUpdate();
                    deleteActorStatement.clearParameters();
                    deleteActorStatement.setInt(1, actorID);
                    deleteActorStatement.executeUpdate();
                }
                test.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void remove(Link ul) {
        int source = -1;
        int dest = -1;
        try {
            getActorByIDTypeStatement.clearParameters();
            getActorByIDTypeStatement.setString(1, ul.getSource().getMode());
            getActorByIDTypeStatement.setString(2, ul.getSource().getID());
            ResultSet actor = getActorByIDTypeStatement.executeQuery();
            if (actor.next()) {
                source = actor.getInt("id");
                actor.close();
                getActorByIDTypeStatement.clearParameters();
                getActorByIDTypeStatement.setString(1, ul.getDestination().getMode());
                getActorByIDTypeStatement.setString(2, ul.getDestination().getID());
                actor = getActorByIDTypeStatement.executeQuery();
                if (actor.next()) {
                    dest = actor.getInt("id");
                    actor.close();
                    getLinkByAllStatement.clearParameters();
                    getLinkByAllStatement.setDouble(1, ul.getStrength());
                    getLinkByAllStatement.setInt(2, source);
                    getLinkByAllStatement.setInt(3, dest);
                    ResultSet linkID = getLinkByAllStatement.executeQuery();
                    if (linkID.next()) {
                        int id = linkID.getInt("id");
                        removeLink(id);
                    }
                    linkID.close();
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public Actor getActor(String type, String ID) {
        Actor ret = null;
        try {
            getActorByIDTypeStatement.clearParameters();
            getActorByIDTypeStatement.setString(1, type);
            getActorByIDTypeStatement.setString(2, ID);
            ResultSet rs = getActorByIDTypeStatement.executeQuery();
            if (rs.next()) {
                java.util.Properties properties = new java.util.Properties();
                properties.setProperty("ActorClass", actorClass);
                properties.setProperty("ActorType", rs.getString("type"));
                properties.setProperty("ActorID", rs.getString("name"));
                ret = ActorFactory.newInstance().create(rs.getString("name"), rs.getString("type"));
//                Property[] props = setActorProperties(rs.getInt("id"));
//                for (int i = 0; i < props.length; ++i) {
//                    ret.add(props[i]);
//                }
            }
            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return ret;
    }

    @Override
    public List<Actor> getActor() {
        Vector<Actor> ret = new Vector<Actor>();
        try {
            ResultSet rs = getActorStatement.executeQuery();
            while (rs.next()) {
                Actor a = ActorFactory.newInstance().create(rs.getString("type"), rs.getString("name"));
//                Property[] props = setActorProperties(rs.getInt("id"));
//                for (int i = 0; i < props.length; ++i) {
//                    a.add(props[i]);
//                }
                ret.add(a);
            }
            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return ret;
    }

    @Override
    public List<Actor> getActor(String type) {
        Vector<Actor> ret = new Vector<Actor>();
        try {
            getActorByTypeStatement.clearParameters();
            getActorByTypeStatement.setString(1, type);
            ResultSet rs = getActorByTypeStatement.executeQuery();
            while (rs.next()) {
                Actor a = ActorFactory.newInstance().create(rs.getString("name"), rs.getString("type"));
//                Property[] props = setActorProperties(rs.getInt("id"));
//                for (int i = 0; i < props.length; ++i) {
//                    a.add(props[i]);
//                }
                ret.add(a);
            }
            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return ret;
    }

    @Override
    public Iterator<Actor> getActorIterator(String type) {
        try {
            ResultSet rs = getActorStatement.executeQuery();
            Vector<Integer> intVect = new Vector<Integer>();
            while (rs.next()) {
                intVect.add(rs.getInt("id"));
            }
            int[] list = new int[intVect.size()];
            for (int i = 0; i < list.length; ++i) {
                list[i] = intVect.get(i).intValue();
            }
            ActorIterator it = new ActorIterator();
            it.setList(list);
            return it;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public List<String> getActorTypes() {
        LinkedList<String> ret = new LinkedList<String>();
        try {
            getActorTypesStatement.clearParameters();
            ResultSet rs = getActorTypesStatement.executeQuery();
            while (rs.next()) {
                ret.add(rs.getString("type"));
            }
            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return ret;
    }

    @Override
    public List<Link> getLink() {
        Vector<Link> ret = new Vector<Link>();
        try {
            ResultSet rs = getLinkStatement.executeQuery();
            while (rs.next()) {
                Link l = LinkFactory.newInstance().create(rs.getString("type"));
                Actor s = retrieveActor(rs.getInt("start"));
                Actor d = retrieveActor(rs.getInt("finish"));
                l.set(s, rs.getDouble("cost"), d);
//                Property[] linkProperties = getLinkProperties(rs.getInt("id"));
//                for (int i = 0; i < linkProperties.length; ++i) {
//                    l.add(linkProperties[i]);
//                }
                ret.add(l);
            }
            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return ret;
    }

    /**
     * Retrieve an actor object from the database with its database's numeric
     * id. Returns null if no such actor exists.
     *
     * @param id numeric id of this object
     * @return Actor object this id refers to
     */
    protected Actor retrieveActor(int id) {

        try {

            getActorByIDStatement.clearParameters();

            getActorByIDStatement.setInt(1, id);

            ResultSet rs = getActorByIDStatement.executeQuery();
            if (rs.next()) {
                return ActorFactory.newInstance().create(rs.getString("type"), rs.getString("name"));
            } else {
                return null;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Link> getLink(String type) {
        Vector<Link> ret = new Vector<Link>();
        try {
            getLinkByTypeStatement.clearParameters();
            getLinkByTypeStatement.setString(1, type);
            ResultSet rs = getLinkByTypeStatement.executeQuery();
            while (rs.next()) {
                Link l = LinkFactory.newInstance().create(rs.getString("type"));
                Actor s = retrieveActor(rs.getInt("start"));
                Actor d = retrieveActor(rs.getInt("finish"));
                l.set(s, rs.getDouble("cost"), d);
//                Property[] linkProperties = getLinkProperties(rs.getInt("id"));
//                for (int i = 0; i < linkProperties.length; ++i) {
//                    l.add(linkProperties[i]);
//                }
                ret.add(l);
            }
            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return ret;
    }

    @Override
    public List<Link> getLinkBySource(String type, Actor sourceActor) {
        Vector<Link> ret = new Vector<Link>();
        try {
            actorGetIDStatement.clearParameters();
            actorGetIDStatement.setString(1, sourceActor.getMode());
            actorGetIDStatement.setString(2, sourceActor.getID());
            ResultSet actor = actorGetIDStatement.executeQuery();
            if (actor.next()) {
                getLinkBySourceStatement.clearParameters();
                getLinkBySourceStatement.setString(1, type);
                getLinkBySourceStatement.setInt(2, actor.getInt("id"));
                ResultSet rs = getLinkBySourceStatement.executeQuery();
                while (rs.next()) {
                    Link l = LinkFactory.newInstance().create(rs.getString("type"));
                    Actor s = retrieveActor(rs.getInt("start"));
                    Actor d = retrieveActor(rs.getInt("finish"));
                    l.set(s, rs.getDouble("cost"), d);
//                    Property[] linkProperties = getLinkProperties(rs.getInt("id"));
//                    for (int i = 0; i < linkProperties.length; ++i) {
//                        l.add(linkProperties[i]);
//                    }
                    ret.add(l);
                }
                rs.close();
            }
            actor.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return ret;
    }

    @Override
    public List<Link> getLinkByDestination(String type, Actor destActor) {
        Vector<Link> ret = new Vector<Link>();
        try {
            actorGetIDStatement.clearParameters();
            actorGetIDStatement.setString(1, destActor.getMode());
            actorGetIDStatement.setString(2, destActor.getID());
            ResultSet actor = actorGetIDStatement.executeQuery();
            if (actor.next()) {
                getLinkByDestStatement.clearParameters();
                getLinkByDestStatement.setString(1, type);
                getLinkByDestStatement.setInt(2, actor.getInt("id"));
                ResultSet rs = getLinkByDestStatement.executeQuery();
                while (rs.next()) {
                    Link l = LinkFactory.newInstance().create(rs.getString("type"));
                    Actor s = retrieveActor(rs.getInt("start"));
                    Actor d = retrieveActor(rs.getInt("finish"));
                    l.set(s, rs.getDouble("cost"), d);
//                    Property[] linkProperties = getLinkProperties(rs.getInt("id"));
//                    for (int i = 0; i < linkProperties.length; ++i) {
//                        l.add(linkProperties[i]);
//                    }
                    ret.add(l);
                }
                rs.close();
            }
            actor.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return ret;
    }

    @Override
    public List<Link> getLink(String type, Actor sourceActor, Actor destActor) {
        Vector<Link> ret = new Vector<Link>();
        try {
            actorGetIDStatement.clearParameters();
            actorGetIDStatement.setString(1, sourceActor.getMode());
            actorGetIDStatement.setString(2, sourceActor.getID());
            ResultSet actor = actorGetIDStatement.executeQuery();
            if (actor.next()) {
                int source = actor.getInt("id");
                actorGetIDStatement.clearParameters();
                actorGetIDStatement.setString(1, destActor.getMode());
                actorGetIDStatement.setString(2, destActor.getID());
                ResultSet actor2 = actorGetIDStatement.executeQuery();
                if (actor2.next()) {
                    int dest = actor2.getInt("id");
                    getLinkByBothStatement.clearParameters();
                    getLinkByBothStatement.setString(1, type);
                    getLinkByBothStatement.setInt(2, source);
                    getLinkByBothStatement.setInt(3, dest);
                    ResultSet rs = getLinkByBothStatement.executeQuery();
                    while (rs.next()) {
                        Link l = LinkFactory.newInstance().create(rs.getString("type"));
                        Actor s = retrieveActor(rs.getInt("start"));
                        Actor d = retrieveActor(rs.getInt("finish"));
                        l.set(s, rs.getDouble("cost"), d);
//                        Property[] linkProperties = getLinkProperties(rs.getInt("id"));
//                        for (int i = 0; i < linkProperties.length; ++i) {
//                            l.add(linkProperties[i]);
//                        }
                        ret.add(l);
                    }
                    rs.close();
                    actor2.close();
                }
                actor.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return ret;
    }

    @Override
    public List<String> getLinkTypes() {
        Vector<String> ret = new Vector<String>();
        try {
            ResultSet rs = getLinkTypesStatement.executeQuery();
            while (rs.next()) {
                ret.add(rs.getString("type"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return ret;
    }

    @Override
    public List<Property> getProperty() {
        LinkedList<Property> ret = new LinkedList<Property>();
        try {
            getGraphPropertiesStatement.clearParameters();
            getGraphPropertiesStatement.setInt(1, graphID);
            ResultSet rs = getGraphPropertiesStatement.executeQuery();
            String className = "";
            int propertyID = -1;
            String type = "";
            while (rs.next()) {
                className = rs.getString("Property.class");
                propertyID = rs.getInt("Property.id");
                type = rs.getString("Property.type");
                PropertyValueDB factory = PropertyValueDatabaseFactory.newInstance().create(className);
                Property prop = PropertyFactory.newInstance().create("BasicPropertry",type, factory.getValueClass());
                rs.close();
                getPropertyValuesStatement.clearParameters();
                getPropertyValuesStatement.setInt(1, propertyID);
                rs = getPropertyValuesStatement.executeQuery();
                while (rs.next()) {
                    int valueID = rs.getInt("value");
                    prop.add(factory.get(valueID));
                }
                ret.add(prop);
            }
        } catch (InvalidObjectTypeException ex) {
            Logger.getLogger(DerbyGraph.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return ret;
    }

    @Override
    public Property getProperty(String type) {
        boolean isNull = true;
        Property ret = null;
        try {
            getGraphPropertiesByTypeStatement.clearParameters();
            getGraphPropertiesByTypeStatement.setInt(1, graphID);
            getGraphPropertiesByTypeStatement.setString(2, type);
            ResultSet rs = getGraphPropertiesByTypeStatement.executeQuery();
            String className = "";
            int propertyID = -1;

            if (rs.next()) {
                className = rs.getString("Property.class");
                propertyID = rs.getInt("Property.id");
                PropertyValueDB factory = PropertyValueDatabaseFactory.newInstance().create(className);
                ret = PropertyFactory.newInstance().create(type, Integer.toString(propertyID), factory.getValueClass());
                rs.close();
                getPropertyValuesStatement.clearParameters();
                getPropertyValuesStatement.setInt(1, propertyID);
                rs = getPropertyValuesStatement.executeQuery();
                while (rs.next()) {
                    int valueID = rs.getInt("value");
                    ret.add(factory.get(valueID));
                }
                isNull = false;
            }
        } catch (InvalidObjectTypeException ex) {
            Logger.getLogger(DerbyGraph.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        if (isNull) {
            return null;
        } else {
            ret.addListener(this);
            return ret;
        }
    }

    @Override
    public void add(Property prop) throws NullPointerException {
        prop.addListener(this);
        String type = prop.getType();
        String className = prop.getPropertyClass().getSimpleName();

        Iterator values = prop.getValue().iterator();
        ResultSet rs = null;
        try {
            addPropertyStatement.clearParameters();
            addPropertyStatement.setString(1, type);
            addPropertyStatement.setString(2, className);
            getAddGraphPropertiesStatement.clearParameters();
            getAddGraphPropertiesStatement.setString(1,type);
            getAddGraphPropertiesStatement.setString(2,className);
            addPropertyStatement.executeUpdate();
            rs = getAddGraphPropertiesStatement.executeQuery();
            if(rs.next()){
                int propertyID = rs.getInt("id");
                PropertyValueDB factory = PropertyValueDatabaseFactory.newInstance().create(prop.getPropertyClass());
                while (values.hasNext()) {
                    int valueID = factory.put(values.next());
                    addPropertyValueStatement.clearParameters();
                    addPropertyValueStatement.setInt(1, propertyID);
                    addPropertyValueStatement.setInt(2, valueID);
                    addPropertyValueStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public List<PathSet> getPathSet() {
        Vector<PathSet> ret = new Vector<PathSet>();
        try {
            // Get set of all path sets
            ResultSet rs = getPathSetStatement.executeQuery();
            while (rs.next()) {
                java.util.Properties props = new java.util.Properties();
                props.setProperty("PathSetID", rs.getString("name"));
                PathSet p = PathSetFactory.newInstance().create(props);
                int id = rs.getInt("id");
                getPathByPathSetStatement.clearParameters();
                getPathByPathSetStatement.setInt(1, id);
                ResultSet paths = getPathByPathSetStatement.executeQuery();
                while (paths.next()) {
                    Path path = PathFactory.newInstance().create(null);
                    getPathActorStatement.clearParameters();
                    getPathActorStatement.setInt(1, paths.getInt("path"));
                    ResultSet actor = getPathActorStatement.executeQuery();
                    double cost = 0.0;
                    Vector<Actor> pathActor = new Vector<Actor>();
                    while (actor.next()) {
                        Actor a = ActorFactory.newInstance().create(actor.getString("type"), actor.getString("name"));
                        pathActor.add(a);
                    }
                    actor.close();
                    path.setPath(pathActor.toArray(new Actor[]{}), paths.getDouble("cost"), paths.getString("type"));
                    p.addPath(path);
                }
                paths.close();
            }
            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return ret;
    }

    @Override
    public PathSet getPathSet(String id) {
        PathSet ret = null;
        try {
            // Get set of all path sets
            getPathSetByNameStatement.clearParameters();
            getPathSetByNameStatement.setString(1, id);
            ResultSet rs = getPathSetByNameStatement.executeQuery();
            if (rs.next()) {
                PathSet p = PathSetFactory.newInstance().create(null);
                p.setType(rs.getString("name"));
                int numericID = rs.getInt("id");
                getPathByPathSetStatement.clearParameters();
                getPathByPathSetStatement.setInt(1, numericID);
                ResultSet paths = getPathByPathSetStatement.executeQuery();
                while (paths.next()) {
                    Path path = PathFactory.newInstance().create(null);
                    getPathActorStatement.clearParameters();
                    getPathActorStatement.setInt(1, paths.getInt("path"));
                    ResultSet actor = getPathActorStatement.executeQuery();
                    double cost = 0.0;
                    Vector<Actor> pathActor = new Vector<Actor>();
                    while (actor.next()) {
                        java.util.Properties props = new java.util.Properties();
                        props.setProperty("ActorType", actor.getString("type"));
                        props.setProperty("ActorID", actor.getString("name"));
                        Actor a = ActorFactory.newInstance().create(actor.getString("type"), actor.getString("name"));
                        pathActor.add(a);
                    }
                    actor.close();
                    path.setPath(pathActor.toArray(new Actor[]{}), paths.getDouble("cost"), paths.getString("type"));
                    p.addPath(path);
                }
                paths.close();
            }
            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return ret;
    }

    @Override
    public void add(PathSet pathSet) {
        try {
            // TODO: Implement addPathSet
            addPathSetStatement.clearParameters();
            addPathSetStatement.setString(1, pathSet.getType());
            addPathSetStatement.executeUpdate();
            getPathSetIDStatement.clearParameters();
            getPathSetIDStatement.setString(1, pathSet.getType());
            ResultSet id = getPathSetIDStatement.executeQuery();
            int pathSetID = -1;
            if (id.next()) {
                pathSetID = id.getInt("id");
            }
            id.close();

            id = getPathIDStatement.executeQuery();
            int pathID = -1;
            if (id.next()) {
                pathID = id.getInt(1) + 1;
            }
            Path[] paths = pathSet.getPath();
            for (int i = 0; i < paths.length; ++i) {
                addPathStatement.clearParameters();
                addPathStatement.setString(1, paths[i].getType());
                addPathStatement.setDouble(2, paths[i].getCost());
                addPathStatement.executeUpdate();
                Actor[] actorList = paths[i].getPath();
                for (int j = 0; j < actorList.length; ++j) {
                    actorGetIDStatement.clearParameters();
                    actorGetIDStatement.setString(1, actorList[j].getMode());
                    actorGetIDStatement.setString(2, actorList[j].getID());
                    ResultSet actor = actorGetIDStatement.executeQuery();
                    if (actor.next()) {
                        addPathActorStatement.clearParameters();
                        addPathActorStatement.setInt(1, pathID);
                        addPathActorStatement.setInt(2, actor.getInt("id"));
                        addPathActorStatement.executeUpdate();
                    }
                    actor.close();
                }
                ++pathID;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (NotConstructedError ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void setID(String id) {
        parameters.get("DatabaseName").clear();
        parameters.get("DatabaseName").add(id);
    }

    @Override
    public String getID() {
        return (String) parameters.get("DatabaseName").getValue().iterator().next();
    }

    /**
     * Set the directory that DerbyGraph will look for its database
     * @param f file representing the target directory.
     */
    public void setDirectory(File f) {
        parameters.get("DerbyDirectory").clear();
        parameters.get("DerbyDirectory").add(f);
    }

    /**
     * Return the directory that this object will or is using.
     * @return directory object
     */
    public File getDirectory() {
        return ((File) parameters.get("DerbyDirectory").getValue().iterator().next());
    }

    /**
     * Aquire a set of properties from the database attached to a given actor.
     * Returns a zero length array if no properties exist for this actor.
     * 
     * @param a unique id of the actor whose properties are to be retrieved
     * @return set of properties attached to given actor
     * @throws java.sql.SQLException 
     */
    /*    protected Property[] setActorProperties(int a) throws SQLException {
    getActorPropertiesStatement.clearParameters();
    getActorPropertiesStatement.setInt(1, a);
    ResultSet rs = getActorPropertiesStatement.executeQuery();
    java.util.HashMap<String, Property> props = new java.util.HashMap<String, Property>();
    while (rs.next()) {
    String type = rs.getString("type");
    String className = rs.getString("class");
    Property p = null;
    if (!props.containsKey(type)) {
    try {
    props.put(type, PropertyFactory.newInstance().create(type, Class.forName(className)));
    } catch (ClassNotFoundException ex) {
    Logger.getLogger(DerbyGraph.class.getName()).log(Level.SEVERE, "Class '"+className+"' does not exist in constructing property '"+type+"' - using String class instead", ex);
    props.put(type, PropertyFactory.newInstance().create(type, String.class));
    }
    }
    String value = rs.getString("value");
    if (!value.contentEquals("")) {
    addValue(props.get(type), value);
    }
    }
    return props.values().toArray(new Property[]{});
    }*/
    @Override
    public void commit() {
        try {
            connection.commit();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void add(Graph g) {
        if (g instanceof UserIDList) {
            userList = (UserIDList) g;
        }
    }

    @Override
    public void anonymize() {
        try {
            ResultSet rs = connection.createStatement().executeQuery("SELECT id FROM Actor");
            while (rs.next()) {
                int id = rs.getInt("id");
                anonymize.clearParameters();
                anonymize.setString(1, Integer.toString(rs.getInt("id")));
                anonymize.setInt(2, id);
                anonymize.executeUpdate();
            }
            connection.commit();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Records changes in properties of this graph, updating the database as needed.
     * @param m property that is modified
     * @param type currently only records adding a property
     */
    public void publishChange(Model m, int type, int argument) {
        if (m instanceof Property) {
            try {
                Property p = (Property) m;
                getGraphPropertiesByTypeStatement.clearParameters();
                getGraphPropertiesByTypeStatement.setInt(1, graphID);
                getGraphPropertiesByTypeStatement.setString(2, p.getType());
                ResultSet rs = getGraphPropertiesByTypeStatement.executeQuery();
                if (rs.next()) {
                    if (rs.getString("value") == null) {
                        updateGraphProperty.clearParameters();
                        updateGraphProperty.setInt(2, graphID);
                        updateGraphProperty.setString(3, p.getType());
                        updateGraphProperty.setString(1, p.getValue().get(0).toString());
                        updateGraphProperty.executeUpdate();
                    } else {
                        addGraphPropertyStatement.setString(1, p.getType());
                        addGraphPropertyStatement.setString(2, p.getValue().get(p.getValue().size() - 1).toString());
                        addGraphPropertyStatement.executeUpdate();
                    }
                } else {
                    // intentionally null - property removed from the graph already
                }
                rs.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * FIXME: Does not properly access database
     */
    public Graph getParent() {
        try {
            getGraphParentStatement.clearParameters();
            getGraphParentStatement.setInt(1, graphID);
            ResultSet rs = getGraphParentStatement.executeQuery();
            if (rs.next()) {
                int parent = rs.getInt("parent");
                getGraphName.clearParameters();
                getGraphName.setInt(1, parent);
                ResultSet nameSet = getGraphName.executeQuery();
                if (nameSet.next()) {
                    DerbyGraph ret = new DerbyGraph();
                    ret.setID(nameSet.getString("name"));
                    ret.startup();
                    nameSet.close();
                    rs.close();
                    return ret;
                }
                nameSet.close();
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(DerbyGraph.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * FIXME: Does not properly access database
     */
    public List<Graph> getChildren() {
        Vector<Graph> children = new Vector<Graph>();
        try {
            getGraphChildrenStatement.clearParameters();
            getGraphChildrenStatement.setInt(1, graphID);
            ResultSet rs = getGraphChildrenStatement.executeQuery();
            while (rs.next()) {
                int childID = rs.getInt("child");
                getGraphName.clearParameters();
                getGraphName.setInt(1, childID);
                ResultSet nameSet = getGraphName.executeQuery();
                while (nameSet.next()) {
                    String name = nameSet.getString("name");
                    DerbyGraph child = new DerbyGraph();
                    child.setID(name);
                    child.startup();
                    children.add(child);
                }
                nameSet.close();
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(DerbyGraph.class.getName()).log(Level.SEVERE, null, ex);
        }
        return children;
    }

    /**
     * FIXME: Does not properly access database
     */
    public Graph getChildren(String id) {
        try {
            getGraphChildrenStatement.clearParameters();
            getGraphChildrenStatement.setInt(1, graphID);
            ResultSet rs = getGraphChildrenStatement.executeQuery();
            while (rs.next()) {
                int childID = rs.getInt("child");
                getGraphName.clearParameters();
                getGraphName.setInt(1, childID);
                ResultSet nameSet = getGraphName.executeQuery();
                while (nameSet.next()) {
                    String name = nameSet.getString("name");
                    if (name.contentEquals(id)) {
                        nameSet.close();
                        rs.close();
                        DerbyGraph child = new DerbyGraph();
                        child.setID(name);
                        child.startup();
                        return child;
                    }
                }
                nameSet.close();
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(DerbyGraph.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * FIXME: Does not save to database
     */
    public void addChild(Graph g) {
        if (g instanceof DerbyGraph) {
            try {
                DerbyGraph graph = (DerbyGraph) g;
                addGraphChildStatement.clearParameters();
                addGraphChildStatement.setInt(1, graphID);
                addGraphChildStatement.setInt(2, graph.graphID);
                addGraphChildStatement.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(DerbyGraph.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            // shouldn't be mixing graph types - stick to DerbyDB
        }
    }

//    private Property[] getLinkProperties(int linkID) throws SQLException {
//        getLinkPropertiesStatement.clearParameters();
//        getLinkPropertiesStatement.setInt(1, linkID);
//        ResultSet rs = getLinkPropertiesStatement.executeQuery();
//        java.util.HashMap<String, Property> props = new java.util.HashMap<String, Property>();
//        while (rs.next()) {
//            /*
//            String type = rs.getString("type");
//            String className = rs.getString("class");
//            Property p = null;
//            if (!props.containsKey(type)) {
//                    try {
//                        props.put(type, PropertyFactory.newInstance().create(type, Class.forName(className)));
//                    } catch (ClassNotFoundException ex) {
//                        Logger.getLogger(DerbyGraph.class.getName()).log(Level.SEVERE, "Class '"+className+"' does not exist in constructing property '"+type+"' - using String class instead", ex);
//                        props.put(type, PropertyFactory.newInstance().create(type, String.class));
//                    }
//            }
//            String value = rs.getString("value");
//            if (!value.contentEquals("")) {
//                addValue(props.get(type), value);
//            }*/
//        }
//        return props.values().toArray(new Property[]{});
//    }
    private void removeLink(int link) throws SQLException {
        deleteLinkGraphStatement.clearParameters();
        deleteLinkGraphStatement.setInt(1, link);
        deleteLinkGraphStatement.setInt(2, graphID);
        deleteLinkGraphStatement.executeUpdate();
        getGraphByLinkGraphStatement.clearParameters();
        getGraphByLinkGraphStatement.setInt(1, link);
        ResultSet test = getGraphByLinkGraphStatement.executeQuery();
        if (!test.next()) {
            deleteLinkPropertiesStatement.clearParameters();
            deleteLinkPropertiesStatement.setInt(1, link);
            deleteLinkPropertiesStatement.executeUpdate();
            deleteLinkStatement.clearParameters();
            deleteLinkStatement.setInt(1, link);
            deleteLinkStatement.executeUpdate();
        }
    }

    public void removeProperty(String ID) {
        try {
            getGraphPropertiesByTypeStatement.clearParameters();
            getGraphPropertiesByTypeStatement.setInt(1, graphID);
            getGraphPropertiesByTypeStatement.setString(2, ID);
            ResultSet rs = getGraphPropertiesByTypeStatement.executeQuery();
            if (rs.next()) {
                int propertyID = rs.getInt("Property.id");
                String className = rs.getString("Property.class");
                rs.close();
                getPropertyValuesStatement.clearParameters();
                getPropertyValuesStatement.setInt(1, propertyID);
                rs = getPropertyValuesStatement.executeQuery();
                while (rs.next()) {
                    int valueID = rs.getInt("value");
                    PropertyValueDB factory = PropertyValueDatabaseFactory.newInstance().create(className);
                    factory.remove(valueID);
                }
                rs.close();
                deletePropertyValuesStatement.clearParameters();
                deletePropertyValuesStatement.setInt(1, propertyID);
                deletePropertyValuesStatement.executeUpdate();
                deletePropertyStatement.clearParameters();
                deletePropertyStatement.setInt(1, propertyID);
                deletePropertyStatement.executeUpdate();
                deleteGraphPropertiesStatement.clearParameters();
                deleteGraphPropertiesStatement.setInt(1, propertyID);
                deleteGraphPropertiesStatement.executeUpdate();
            } else {
                Logger.getLogger(DerbyGraph.class.getName()).log(Level.WARNING, "Property '"+ID+"' on graph '"+graphID+"' does not exist");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DerbyGraph.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @author Daniel McEnnis
     * 
     * Inner class of DerbyGraph that implements the iterator interface.  If
     * actors are inserted or removed during the lifetime ofthis iterator, the 
     * resulting iterations will not reflect the changes, resulting in accessing
     * non-existant actors.  However, changes to actors during the lifetime of 
     * the iterator will be reflected when the actor is returned.
     *
     */
    public class ActorIterator implements Iterator {

        ResultSet resultSet;
        int[] results;
        int count;

        /**
         * Allows the DerbyGraph object to set the list of actor ids this object
         * will return.
         * 
         * @param rs 
         */
        protected void setList(int[] rs) {
            results = rs;
            count = 0;
        }

        @Override
        public boolean hasNext() {
            if (count < results.length) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        public Object next() throws java.util.NoSuchElementException {
            if (count >= results.length) {
                throw new java.util.NoSuchElementException();
            }
            try {
                getActorByIDStatement.clearParameters();
                getActorByIDStatement.setInt(1, results[count]);
                ResultSet resultSet = getActorByIDStatement.executeQuery();
                if (resultSet.next()) {
                    Actor a = ActorFactory.newInstance().create(resultSet.getString("type"), resultSet.getString("name"));
//                    Property[] props = setActorProperties(resultSet.getInt("id"));
//                    for (int i = 0; i < props.length; ++i) {
//                        a.add(props[i]);
//                    }
                    count++;
                    resultSet.close();
                    return a;
                } else {
                    count++;
                    resultSet.close();
                    return null;
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                try {
                    resultSet.close();
                } catch (SQLException ex2) {
                }
                count++;
                return null;
            }
        }

        /**
         * FIXME: Implement remove code
         */
        public void remove() {
        }
    }

    /**
     * Determine what class will be used for the creation of actor objects.
     * @see org.mcennis.graphrat.actor.ActorFactory
     * 
     * @param actorClass name of class of actor 
     */
    protected void setActorClass(String actorClass) {
        this.actorClass = actorClass;
    }

    /**
     * Determine what class will be used for the creation of actor objects.
     * @see org.mcennis.graphrat.actor.ActorFactory
     * 
     * @return String used to create actors in ActorFactory
     */
    protected String getActorClass() {
        return actorClass;
    }

    @Override
    public Properties getParameter() {
        return parameters;
    }

    @Override
    public int getActorCount(String mode) {
        try {
            getActorCountStatement.clearParameters();
            getActorCountStatement.setString(1, mode);
            ResultSet rs = getActorCountStatement.executeQuery();
            int ret = 0;
            if (rs.next()) {
                ret = rs.getInt(1);
            }
            rs.close();
            return ret;
        } catch (SQLException ex) {
            Logger.getLogger(DerbyGraph.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }

    public int compareTo(Object o) {
        return GraphCompare.compareTo(this, o);
    }

    public DerbyGraph prototype() {
        return new DerbyGraph();
    }

    public Iterator<Actor> getActorIterator() {
        return getActor().iterator();
    }

    public Iterator<Link> getLinkIterator() {
        return getLink().iterator();
    }

    public Iterator<Link> getLinkIterator(String type) {
        return getLink(type).iterator();
    }

    public Iterator<Link> getLinkBySourceIterator(String type, Actor sourceActor) {
        return getLinkBySource(type, sourceActor).iterator();
    }

    public Iterator<Link> getLinkByDesinationIterator(String type, Actor destActor) {
        return getLinkByDestination(type, destActor).iterator();
    }

    public Iterator<Link> getLinkIterator(String type, Actor sourceActor, Actor destActor) {
        return getLink(type, sourceActor, destActor).iterator();
    }

    public Iterator<Property> getPropertyIterator() {
        return getProperty().iterator();
    }

    public Iterator<PathSet> getPathSetIterator() {
        return getPathSet().iterator();
    }

    public Iterator<Graph> getChildrenIterator() {
        return getChildren().iterator();
    }

    public Parameter getParameter(String name) {
        return parameters.get(name);
    }
}

