/*
 * PostgresqlGraph.java
 *
 * Created on 31 July 2007, 11:27
 *
 * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt)
 */
package nz.ac.waikato.mcennis.rat.graph;

import java.sql.SQLException;


import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import nz.ac.waikato.mcennis.rat.graph.descriptors.IODescriptorFactory;
import nz.ac.waikato.mcennis.rat.graph.descriptors.Parameter;
import nz.ac.waikato.mcennis.rat.graph.descriptors.ParameterFactory;
import nz.ac.waikato.mcennis.rat.graph.descriptors.ParameterInternal;
import nz.ac.waikato.mcennis.rat.graph.descriptors.SyntaxObject;
import nz.ac.waikato.mcennis.rat.graph.descriptors.SyntaxCheckerFactory;

/**
 * Sub-class of the DerbyGraph Object to work with postgresql databases
 *
 * @author Daniel McEnnis
 * 
 */
public class PostgresqlGraph extends DerbyGraph {

    
    
    String user = "posgres";

    /** Creates a new instance of PostgresqlGraph */
    public PostgresqlGraph() {
        ParameterInternal param = ParameterFactory.newInstance().create("GraphID",String.class);
        SyntaxObject restrictionPart = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        param.setRestrictions(restrictionPart);
        param.add("Root");
        parameters.add(param);

        param = ParameterFactory.newInstance().create("DatabaseUser",String.class);
        restrictionPart = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        param.setRestrictions(restrictionPart);
        parameters.add(param);

        param = ParameterFactory.newInstance().create("DatabasePassword",String.class);
        restrictionPart = SyntaxCheckerFactory.newInstance().create(0, 1, null, String.class);
        param.setRestrictions(restrictionPart);
        parameters.add(param);

        param = ParameterFactory.newInstance().create("DatabaseString",String.class);
        restrictionPart = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        param.setRestrictions(restrictionPart);
        parameters.add(param);
        
        param = ParameterFactory.newInstance().create("DatabaseClear",Boolean.class);
        restrictionPart = SyntaxCheckerFactory.newInstance().create(1, 1, null, Boolean.class);
        param.setRestrictions(restrictionPart);
        param.add(false);
        parameters.add(param);
        
        param = ParameterFactory.newInstance().create("DatabaseInitialization",Boolean.class);
        restrictionPart = SyntaxCheckerFactory.newInstance().create(1, 1, null, Boolean.class);
        param.setRestrictions(restrictionPart);
        param.add(false);
        parameters.add(param);
        
    }

    /**
     * Performs postgresql initialization rather than DerbyGraph
     * FIXME: initActor for postgresql is needed
     */
    @Override
    public void startup() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
            String password = "";
            if(parameters.get("DatabasePassword").getValue().size()>0){
                password = "&password="+(String)(parameters.get("DatabasePassword").getValue().iterator().next());
            }
            String startup = "jdbc:postgresql:" + (String)(parameters.get("DatabaseName").getValue().iterator().next()) 
                    + "?user=" + (String)(parameters.get("DatabaseUser").getValue().iterator().next())+password;
            System.out.println(startup);
            connection = java.sql.DriverManager.getConnection(startup);

            Statement stat = connection.createStatement();

            java.sql.ResultSet id = stat.executeQuery("SELECT id FROM Graph");
            if (id.next()) {
                graphID = id.getInt("id");
            }

            super.prepareStatement();
            connection.setAutoCommit(false);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Performs Postgresql specific database initialization
     */
    @Override
    public void initializeDatabase() throws SQLException {


        try {


            Class.forName("org.postgresql.Driver");


            String password = "";
            if(parameters.get("DatabasePassword").getValue().size()>0){
                password = "&password="+(String)(parameters.get("DatabasePassword").getValue().iterator().next());
            }
            connection = java.sql.DriverManager.getConnection("jdbc:postgresql:" + (String)(parameters.get("DatabaseName").getValue().iterator().next()) 
                    + "?user=" + (String)(parameters.get("DatabaseUser").getValue().iterator().next())+password);





            Statement stat = connection.createStatement();


            try {


                stat.executeUpdate("CREATE TABLE Graph (" +
                        "id SERIAL," +
                        "name varchar(256) not null, " +
                        "primary key(id))");


            } catch (SQLException ex) {


                System.err.println("GRAPH: " + ex.getMessage());


            }


            try {


                stat.executeUpdate("CREATE TABLE SubGraph (" +
                        "parent integer not null, " +
                        "child integer not null, " +
                        //                        "FOREIGN KEY (parent) REFERENCES Graph(id)," +

                        //                        "FOREIGN KEY (child) REFERENCES Graph(id)," +

                        "PRIMARY KEY(parent,child)" +
                        ")");


            } catch (SQLException ex) {


                System.err.println("SUBGRAPH: " + ex.getMessage());


            }


            try {


                stat.executeUpdate("CREATE TABLE Actor (" +
                        "id SERIAL," +
                        "type VARCHAR(64) not null, " +
                        "name VARCHAR(256) not null, " +
                        "PRIMARY KEY(id)" +
                        ")");


                stat.executeUpdate("CREATE INDEX actor_type_index ON Actor(type)");


                stat.executeUpdate("CREATE INDEX actor_name_index ON Actor(name)");


                stat.executeUpdate("CREATE INDEX actor_index ON Actor(type,name)");


            } catch (SQLException ex) {


                System.err.println("ACTOR: " + ex.getMessage());


            }


            try {


                stat.executeUpdate("CREATE TABLE ActorGraph (" +
                        "graph integer not null," +
                        "actor integer not null," +
                        //                        "FOREIGN KEY (graph) REFERENCES Graph(id)," +

                        //                        "FOREIGN KEY (actor) REFERENCES Actor(id)," +

                        "PRIMARY KEY (graph,actor)" +
                        ")");


            } catch (SQLException ex) {


                System.err.println("ACTORGRAPH: " + ex.getMessage());


            }


            try {





                stat.executeUpdate("CREATE TABLE ActorProperties(" +
                        "id integer not null, " +
                        "type VARCHAR(256) , " +
                        "value VARCHAR(256) , " +
                        //                        "FOREIGN KEY(id) REFERENCES Actor(id), " +

                        "PRIMARY KEY(id,type,value)" +
                        ")");


                stat.executeUpdate("CREATE INDEX ActorProperty_idtype_index ON ActorProperties(id,type)");


            } catch (SQLException ex) {


                System.err.println("ACTORPROPERTIES: " + ex.getMessage());


            }


            try {


                stat.executeUpdate("CREATE TABLE GraphProperties (" +
                        "id integer not null, " +
                        "type VARCHAR(256) , " +
                        "value VARCHAR(256) , " +
                        //                        "FOREIGN KEY(id) REFERENCES Graph(id), " +

                        "PRIMARY KEY(id,type,value)" +
                        ")");


            } catch (SQLException ex) {


                System.err.println("GRAPHPROPERTIES: " + ex.getMessage());


            }


            try {


                stat.executeUpdate("CREATE TABLE Link (" +
                        "id SERIAL, " +
                        "type VARchar(256)," +
                        "start integer, " +
                        "finish integer, " +
                        "cost double precision, " +
                        //                        "FOREIGN KEY (start) REFERENCES Actor(id)," +

                        //                        "FOREIGN KEY (finish) REFERENCES Actor(id)," +

                        "PRIMARY KEY (id)" +
                        ")");


                stat.executeUpdate("CREATE INDEX Link_type ON Link(type)");


                stat.executeUpdate("CREATE INDEX Link_type_start ON Link(type,start)");


                stat.executeUpdate("CREATE INDEX Link_type_finish ON Link(type,finish)");


                stat.executeUpdate("CREATE INDEX Link_all ON Link(type,start,finish)");


                stat.executeUpdate("CREATE INDEX Link_start_finish ON Link(start,finish)");


            } catch (SQLException ex) {


                System.err.println("LINK: " + ex.getMessage());


            }


            try {


                stat.executeUpdate("CREATE Table LinkGraph (" +
                        "link integer not null, " +
                        "graph integer not null, " +
                        //                        "FOREIGN KEY (link) REFERENCES Link(id), " +

                        //                        "FOREIGN KEY (graph) REFERENCES Graph(id), " +

                        "PRIMARY KEY (link,graph)" +
                        ")");


            } catch (SQLException ex) {


                System.err.println("LINKGRAPH: " + ex.getMessage());


            }


            try {


                stat.executeUpdate("CREATE TABLE PathSet (" +
                        "id SERIAL, " +
                        "name VARchar(256) not null unique, " +
                        "PRIMARY KEY (id)" +
                        ")");


            } catch (SQLException ex) {


                System.err.println("PATHSET: " + ex.getMessage());


            }


            try {


                stat.executeUpdate("CREATE TABLE Path (" +
                        "id SERIAL, " +
                        "name VARchar(256) not null, " +
                        "cost double precision, " +
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
                        //                        "FOREIGN KEY(path) REFERENCES Path(id), " +

                        //                        "FOREIGN KEY(actor) REFERENCES Actor(id), " +

                        "PRIMARY KEY (path,actor)" +
                        ")");


            } catch (SQLException ex) {


                System.err.println("PATHACTOR: " + ex.getMessage());


            }


            try {


                stat.executeUpdate("CREATE TABLE PathSetPath (" +
                        "pathSet integer not null, " +
                        "path integer not null, " +
                        //                        "FOREIGN KEY (path) REFERENCES Path(id), " +

                        //                        "FOREIGN KEY (pathSet) REFERENCES PathSet(id), " +

                        "PRIMARY KEY (pathSet,path)" +
                        ")");


            } catch (SQLException ex) {


                System.err.println("PATHSETPATH: " + ex.getMessage());


            }


            try {


                stat.executeUpdate("INSERT INTO Graph (name) VALUES ( '" + (String)(parameters.get("DatabaseName").getValue().iterator().next()) + "' )");


            } catch (SQLException ex) {


                System.err.println("INSERT: " + ex.getMessage());


            }


            connection.close();





        } catch (ClassNotFoundException ex) {


            ex.printStackTrace();


        }


    }

    
}


