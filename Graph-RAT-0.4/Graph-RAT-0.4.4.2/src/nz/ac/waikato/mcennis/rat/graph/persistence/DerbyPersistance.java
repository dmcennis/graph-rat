/*
 * DerbyPersistance.java
 *
 * Created on 21 August 2007, 13:51
 *
 * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt)
 */

package nz.ac.waikato.mcennis.rat.graph.persistence;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.io.File;
import java.util.HashMap;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import nz.ac.waikato.mcennis.rat.graph.actor.Actor;
import nz.ac.waikato.mcennis.rat.graph.link.Link;
import nz.ac.waikato.mcennis.rat.graph.property.Property;

/**
 *
 * @author Daniel McEnnis
 */
public class DerbyPersistance implements GraphPersistance{
    
    HashMap<Actor,Integer> actor2integer = new HashMap<Actor,Integer>();

    HashMap<Integer,Actor> integer2actor = new HashMap<Integer,Actor>();
    
    File directory = null;
    
    String database = null;
    
    java.sql.Connection connection = null;
    
    int graphID = -1;
    
    Graph dataSource = null;
    
    
    
    /** Creates a new instance of DerbyPersistance */
    public DerbyPersistance() {
    }

    public void storeGraph(Graph g) throws SQLException{
        dataSource = g;
        storeActors();
        storeActorProperties();
        storeLinks();
        storeGraphProperties();
        connection.commit();
    }

    public void loadGraph(Graph g) {
    }

    public void start() throws SQLException {
        try {
            System.setProperty("derby.system.home",directory.getAbsolutePath());
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            connection = java.sql.DriverManager.getConnection("jdbc:derby:"+database);
            
            PreparedStatement stat = connection.prepareStatement("SELECT id FROM Graph ORDER BY id ASC");
            stat.setString(1,database);
            ResultSet rs = stat.executeQuery();
            if(rs.next()){
                graphID = rs.getInt("id");
            }
            connection.setAutoCommit(false);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public void stop() throws SQLException {
        connection.close();
        System.gc();
    }
    
    public void initializeDatabase(){
        
    }
    
    protected void storeActors() throws SQLException{
        PreparedStatement stat = connection.prepareStatement("INSERT INTO Actor (type,name) VALUES ( ? , ? )");
        Actor[] allActors = dataSource.getActor();
        int count = 0;
        for(int i=0;i<allActors.length;++i){
            stat.clearParameters();
            stat.setString(1,allActors[i].getType());
            stat.setString(2,allActors[i].getID());
            stat.executeUpdate();
            if(i%100==0){
                System.out.println("Uploading Actor "+i+" of "+allActors.length);
            }
        }
        connection.commit();
        stat.close();
        allActors = null;
        java.sql.Statement get = connection.createStatement();
        ResultSet rs = get.executeQuery("SELECT (id , type, name) FROM Actor");
        count = 0;
        while(rs.next()){
            int id = rs.getInt("id");
            Actor a = dataSource.getActor(rs.getString("type"),rs.getString("name"));
            actor2integer.put(a,id);
            if(count%1000==0){
                System.out.println("Loading actor hashmap "+count+" of "+allActors.length);
            }
            count++;
        }
        rs.close();
        get.close();
   }
    
    protected void storeActorProperties() throws SQLException{
        PreparedStatement stat = connection.prepareStatement("INSERT (actor,type,value) INTO ActorProperties VALUES ( ? , ? , ? )");
        java.util.Iterator<Actor> it = actor2integer.keySet().iterator();
        int count = 0;
        while(it.hasNext()){
            Actor a = it.next();
            Property[] propList = a.getProperty();
            if(propList != null){
                for(int j=0;j<propList.length;++j){
                    String[] value = null;//propList[j].getValue();
                    if(value != null){
                        for(int k=0;k<value.length;++k){
                            stat.clearParameters();
                            stat.setInt(1,actor2integer.get(a));
                            stat.setString(2,propList[j].getType());
                            stat.setString(3,value[k]);
                            stat.executeUpdate();
                        }
                    }
                }
            }
            if(count%100==0){
                System.out.println("Uploading Actor Properties "+count+" of "+actor2integer.size());
            }
            count++;
        }
        connection.commit();
        stat.close();
    }
    
    protected void storeLinks() throws SQLException{
        Link[] allLinks = dataSource.getLink();
        PreparedStatement stat = connection.prepareStatement("INSERT (type,strength,start,finish) INTO Link VALUES ( ? , ? , ? , ? )");
        for(int i=0;i<allLinks.length;++i){
            stat.clearParameters();
            stat.setString(1,allLinks[i].getType());
            stat.setDouble(2,allLinks[i].getStrength());
            stat.setInt(3,actor2integer.get(allLinks[i].getSource()));
            stat.setInt(4,actor2integer.get(allLinks[i].getDestination()));
            stat.executeUpdate();
            if(i%100==0){
                System.out.println("Uploading Link "+i+" of "+allLinks.length);
            }
        }
        connection.commit();
    }
    
    protected void storeGraphProperties() throws SQLException{
        
    }
    
}
