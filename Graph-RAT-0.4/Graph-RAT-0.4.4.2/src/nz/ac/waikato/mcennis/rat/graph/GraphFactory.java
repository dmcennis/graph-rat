/* * GraphFactory.java * * Created on 1 May 2007, 17:05 * * copyright Daniel McEnnis - published under Aferro GPL (see license.txt) */package nz.ac.waikato.mcennis.rat.graph;import java.io.IOException;import java.sql.SQLException;import java.util.Collections;import java.util.LinkedList;/** * Create a Graph object of the given type with the given arguments * * @author Daniel McEnnis * */public class GraphFactory {        /**     *Singleton of graph factory     */    static GraphFactory instance = null;        /**     * return a singleton of this type, creating it if necessary     *     * @return signelton instance of GraphFactory     */    public static GraphFactory newInstance(){        if(instance==null){            instance = new GraphFactory();        }        return instance;    }        /** Creates a new instance of GraphFactory */    protected GraphFactory() {    }        /**     * Create a graph object without directly exposing the underlying types     *      * Types of graphs are pulled from the "Graph" key with "GraphID" for their unique ID.     * <ul>     * <li>'Null'  - Creates a NullGraph object (no parameters)     * <li>'User'  - Creates a UserList object (no parameters)     * <li>'Derby' - Creates a DerbyGraph Object      *      <ul><li>'Directory' - location of the Derby DB     *      <li>'GraphID' - id string of the Derby Graph     *      <li>'GraphInitialize' - if present, the graph is initialized     *      <li>'GraphClear' - if present, the graph is cleared     *      </ul>     * <li>'Postgresql' - Creates a PostgresqlGraph     *      <ul><li>'GraphID' - graph ID, also name of database     *      <li>'GraphUser' - user to connect to datbase with     *      <li>'Initialize' - if present, the graph is initialized     *      <li>'GraphClear' - if equal to true, the graph is initialized     *      </ul>     * <li>'UserIDList' - Creates a UserIDList object (no parameters)     * <li>any other value - Creates a MemGraph object     *      <ul><li>'GraphOutput' - Location to store XML output.  No output created on close if this parameter is not set.     *      <li>'Compression' - if present, GZip compression is used.     *      </ul>     * </ul>     *     * @param args parameters for the new graph     *     * @return newly created Graph object     */    public Graph create(java.util.Properties args){        if((args==null)||(args.getProperty("Graph")==null)){            return new MemGraph();        }else if(args.getProperty("Graph").equals("Null")){            return new NullGraph();        }else if(args.getProperty("Graph").equals("User")){            return new UserList();        }else if(args.getProperty("Graph").equals("Derby")){            DerbyGraph ret = new DerbyGraph();            if(args.getProperty("Directory")!=null){                ret.setDirectory(new java.io.File(args.getProperty("Directory")));            }            if(args.getProperty("GraphID")!=null){                ret.setID(args.getProperty("GraphID"));            }            if(args.getProperty("GraphInitialize")!=null){                try {                    ret.initializeDatabase();                } catch (SQLException ex) {                    ex.printStackTrace();                }            }                        try {                ret.startup();            } catch (SQLException ex) {                ex.printStackTrace();            }            if(args.getProperty("GraphClear")!=null){                ret.clear();            }            return ret;        }else if(args.getProperty("Graph").equals("Postgresql")){            PostgresqlGraph ret = new PostgresqlGraph();            if(args.getProperty("GraphID")!=null){                ret.setID(args.getProperty("GraphID"));            }            if(args.getProperty("GraphUser")!=null){                ret.setUser(args.getProperty("GraphUser"));            }            if(args.getProperty("Initialize")!=null){                try {                    ret.initializeDatabase();                } catch (SQLException ex) {                    ex.printStackTrace();                }            }            try {                ret.startup();                if((args.getProperty("GraphClear") != null)&&(Boolean.parseBoolean(args.getProperty("GraphClear")))){                    ret.clear();                }            } catch (SQLException ex) {                ex.printStackTrace();            }            return ret;        }else if(args.getProperty("Graph").equals("UserIDList")){            Graph ret = new UserIDList();            return ret;        }else{            MemGraph ret = new MemGraph();            try {                if(args.getProperty("GraphOutput")!=null){                    java.io.Writer fw = null;                    if((args.getProperty("Compression")!=null)&&(args.getProperty("Compression").compareToIgnoreCase("false")!=0)){                        fw = new java.io.OutputStreamWriter(new java.util.zip.GZIPOutputStream(new java.io.FileOutputStream(args.getProperty("GraphOutput"))));                    }else{                        fw = new java.io.FileWriter(args.getProperty("GraphOutput"));                    }                    ret.setWriter(fw);                }                if(args.getProperty("GraphID")!=null){                    ret.setID(args.getProperty("GraphID"));                }            } catch (IOException ex) {                ex.printStackTrace();            }            return ret;        }    }        public String[] getKnownGraphs(){        LinkedList<String> ret = new LinkedList<String>();        ret.add("MemGraph");        ret.add("UserIDList");        ret.add("Derby");        ret.add("Postgresql");        ret.add("User");        ret.add("Null");        Collections.sort(ret);        return ret.toArray(new String[]{});    }}