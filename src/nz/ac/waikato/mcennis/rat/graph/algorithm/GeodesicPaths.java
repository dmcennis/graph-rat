/*
 * AddGeodesicPaths.java
 *
 * Created on 5 July 2007, 19:25
 *
 * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt)
 */
package nz.ac.waikato.mcennis.rat.graph.algorithm;

import nz.ac.waikato.mcennis.rat.graph.Graph;


import nz.ac.waikato.mcennis.rat.graph.descriptors.IODescriptorFactory;


import nz.ac.waikato.mcennis.rat.graph.descriptors.IODescriptor;


import nz.ac.waikato.mcennis.rat.graph.path.Path;


import nz.ac.waikato.mcennis.rat.graph.path.PathFactory;


import nz.ac.waikato.mcennis.rat.graph.path.PathSet;

import org.dynamicfactory.descriptors.*;

import nz.ac.waikato.mcennis.rat.graph.path.PathSetFactory;


import nz.ac.waikato.mcennis.rat.graph.actor.Actor;


import nz.ac.waikato.mcennis.rat.graph.link.Link;


import java.util.HashSet;


import java.util.HashMap;


import java.util.Vector;


import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import nz.ac.waikato.mcennis.rat.graph.descriptors.IODescriptor.Type;
import nz.ac.waikato.mcennis.rat.graph.descriptors.IODescriptorInternal;
import org.dynamicfactory.model.ModelShell;
import nz.ac.waikato.mcennis.rat.graph.query.ActorQuery;
import nz.ac.waikato.mcennis.rat.graph.query.ActorQueryFactory;
import nz.ac.waikato.mcennis.rat.graph.query.LinkQuery;
import nz.ac.waikato.mcennis.rat.graph.query.actor.ActorByMode;
import nz.ac.waikato.mcennis.rat.scheduler.Scheduler;

/**
 * Class for calculating all geodesic paths over a set of links and set of actors
 * using a variation of Djikstra's algorithm.
 *
 * @author Daniel McEnnis
 * 
 */
public class GeodesicPaths extends ModelShell implements Algorithm {

    public static final long serialVersionUID = 2;
    HashSet<String> usedCount;
    HashMap<String, Integer> map;
    HashMap<String, Vector<String>> nodeExpansion;
    Actor[] userList;
    PathSet pathSet;
    PathSet lastPath;
    PathSet nextPath;
    HashSet<String> seedNodes;
    PropertiesInternal parameter = PropertiesFactory.newInstance().create();
    LinkedList<IODescriptor> input = new LinkedList<IODescriptor>();
    LinkedList<IODescriptor> output = new LinkedList<IODescriptor>();

    /** Creates a new instance of AddGeodesicPaths */
    public GeodesicPaths() {
        ParameterInternal name = ParameterFactory.newInstance().create("AlgorithmClass", String.class);
        SyntaxObject syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("Basic Geodesic Paths");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Name", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, Integer.MAX_VALUE, null, String.class);
        name.setRestrictions(syntax);
        name.add("Basic Geodesic Paths");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Category", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("LinkFilter",LinkQuery.class);
        syntax = SyntaxCheckerFactory.newInstance().create(0,1,null,LinkQuery.class);
        name.setRestrictions(syntax);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("ActorFilter", ActorQuery.class);
        syntax = SyntaxCheckerFactory.newInstance().create(0, 1, null, ActorQuery.class);
        name.setRestrictions(syntax);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Mode", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("tag");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Relation", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("tag");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("PathSetName", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("tag");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("DestinationAppendGraphID",Boolean.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,Boolean.class);
        name.setRestrictions(syntax);
        name.add(false);
        parameter.add(name);
    }

    /**
     * Calculate each path using a variation of Djikstra's algorithm for spanning
     * trees with modification for recording multiple same-cost minimum paths.
     * 
     */
    public void execute(Graph g) {
        ActorByMode mode = (ActorByMode)ActorQueryFactory.newInstance().create("ActorByMode");
        mode.buildQuery((String)parameter.get("Mode").get(),".*",false);
        java.util.Properties props = new java.util.Properties();
        props.setProperty("PathSetType", "Basic");
        pathSet = PathSetFactory.newInstance().create(props);
        pathSet.setType(AlgorithmMacros.getDestID(parameter, g, (String)parameter.get("PathSetName").get()));
        seedNodes = new HashSet<String>();
        usedCount = new HashSet<String>();
        nodeExpansion = new HashMap<String, Vector<String>>();
        userList = (AlgorithmMacros.filterActor(parameter, g, mode.execute(g, null, null))).toArray(new Actor[]{});
        int count = 0;
        fireChange(Scheduler.SET_ALGORITHM_COUNT,userList.length);

        //
        // For every user, calculate all geodesics.
        //
        // NOTE: iff path p is a geodesic of length n+1 iff the first n nodes
        // form a geodesic and node n+1 does not occur in any shorter path.
        //
        // NOTE: this method enumerates all geodesics in order of length.  It
        // ceases searching after consecutive expansions with no new nodes -
        // sign of a disconnected graph.
        //
        // For every user
        //    For each length geodesics
        //
        for (int i = 0; i < userList.length; ++i) {
            
            if (i % 100 == 0) {
                Logger.getLogger(GeodesicPaths.class.getName()).log(Level.FINE,"Geodesic set " + i + " of " + userList.length);
                fireChange(Scheduler.SET_ALGORITHM_PROGRESS,i);
            }
            seedNodes.clear();
            seedNodes.add(userList[i].getID());
            usedCount.clear();
            usedCount.add(userList[i].getID());
            nodeExpansion.clear();
            java.util.Properties properties = new java.util.Properties();
            properties.setProperty("PathType", "Basic");
            properties.setProperty("PathID", "Base");
            properties.setProperty("PathSetType", "Basic");
            properties.setProperty("PathSetID", "Base");
            Path base = PathFactory.newInstance().create(properties);
            lastPath = PathSetFactory.newInstance().create(properties);
            base.setPath(new Actor[]{userList[i]}, 0.0, "Base");
            lastPath.addPath(base);
            // While there exists nodes to be found or new nodes are added
            while ((usedCount.size() < userList.length) && (lastPath.size() > 0)) {
                // For every last node in list
                pathSet.setType(AlgorithmMacros.getDestID(parameter, g, (String)parameter.get("PathSetName").get()));
                nextPath = PathSetFactory.newInstance().create(properties);
                nextStepNodes(g);
                processVectors(g);
                lastPath = nextPath;
                count++;
            }
        }// for every user
        g.add(pathSet);
    }

    /**
     * Place node expansion into node expansion map.  This must be done every
     * user becuase this filters out all nodes that would not create a new
     * geodesic.
     *
     * @param g graph containing the nodes to be parsed.
     */
    protected void nextStepNodes(Graph g) {
        nodeExpansion.clear();
        Iterator<String> seed = seedNodes.iterator();
        HashSet<String> nextSeed = new HashSet<String>();
        while (seed.hasNext()) {
            String key = seed.next();
            Actor source = g.getActor((String)parameter.get("Mode").get(), key);
            Iterator<Link> linkedNodes = AlgorithmMacros.filterLink(parameter, g, g.getLinkBySource((String)parameter.get("Relation").get(), source)).iterator();
            if (linkedNodes.hasNext()) {
                nodeExpansion.put(key, new Vector<String>());
                while (linkedNodes.hasNext()) {
                    String dest = linkedNodes.next().getDestination().getID();
                    if (!usedCount.contains(dest)) {
                        nodeExpansion.get(key).add(dest);
                        nextSeed.add(dest);
                    }
                }
            }
        }
        usedCount.addAll(nextSeed);
        seedNodes = nextSeed;
    }

    /**
     * for each vector in the list
     *   process end node for each geodesic
     *
     * @param g graph for betweeness calculations
     */
    protected void processVectors(Graph g) {
        Iterator<String> seed = nodeExpansion.keySet().iterator();
        while (seed.hasNext()) {
            String seedString = seed.next();
            Path[] base = lastPath.getPathByDestination(seedString);
            Vector<String> mapping = nodeExpansion.get(seedString);
            if ((base != null) && (mapping != null) && (mapping.size() > 0)) {
                for (int i = 0; i < base.length; ++i) {
                    for (int j = 0; j < mapping.size(); ++j) {
                        Path item = base[i].addActor(g.getActor((String) parameter.get("Mode").get(), mapping.get(j)), 1.0);
                        nextPath.addPath(item);
                        pathSet.addPath(item);
                    }
                }
            }
            usedCount.add(seedString);
        }
        lastPath = nextPath;
    }

    @Override
    public List<IODescriptor> getInputType() {
        return input;
    }

    @Override
    public List<IODescriptor> getOutputType() {
        return output;
    }

    @Override
    public Properties getParameter() {
        return parameter;
    }

    @Override
    public Parameter getParameter(String param) {
        return parameter.get(param);
    }

    /**
     * Initialize a geodesic path object
     * 
     * Parameters:
     * <br><ol>
     * <li>'name' - the name for this algorithm component. Default is 'Basic Geodesic Paths'.
     * <li>'relation' - what type (relation) of link (arc, edge) to calcualte paths over. Default is 'Knows'
     * <li>'actorType' - what type (mode) of actor to calculate paths over. Default is 'User'.
     * </ol>
     * <br>
     * <br><b>input 1</b> Link
     * <br><b>output 1</b> Path
     */
    public void init(Properties map) {
        if(parameter.check(map)){
            parameter.merge(map);
            IODescriptorInternal desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR,
                    (String)parameter.get("Name").get(),
                    (String)parameter.get("Mode").get(),
                    null,
                    null,
                    "",
                    false);
            input.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.LINK,
                    (String)parameter.get("Name").get(),
                    (String)parameter.get("Relation").get(),
                    null,
                    null,
                    "",
                    false);
            input.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.PATHSET,
                    (String)parameter.get("Name").get(),
                    (String)parameter.get("PathSetName").get(),
                    null,
                    null,
                    "",
                    (Boolean)parameter.get("DestinationAppendGraphID").get());
            output.add(desc);
        }
    }

    public GeodesicPaths prototype(){
        return new GeodesicPaths();
    }
}