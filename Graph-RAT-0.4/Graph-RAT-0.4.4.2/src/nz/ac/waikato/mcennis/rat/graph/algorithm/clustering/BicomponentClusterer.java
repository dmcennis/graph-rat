/* * Copyright (c) 2003, the JUNG Project and the Regents of the University  * of California * All rights reserved. * * This software is open-source under the BSD license; see either * "license.txt" or * http://jung.sourceforge.net/license.txt for a description. *  * Modified for RAT by Daniel McEnnis Dec 2 2007 */package nz.ac.waikato.mcennis.rat.graph.algorithm.clustering;import java.util.HashMap;import java.util.HashSet;import java.util.LinkedHashSet;import java.util.LinkedList;import java.util.Map;import java.util.Properties;import java.util.Set;import java.util.Stack;import java.util.logging.Level;import java.util.logging.Logger;import nz.ac.waikato.mcennis.rat.graph.Graph;import nz.ac.waikato.mcennis.rat.graph.actor.Actor;import nz.ac.waikato.mcennis.rat.graph.algorithm.Algorithm;import nz.ac.waikato.mcennis.rat.graph.descriptors.DescriptorFactory;import nz.ac.waikato.mcennis.rat.graph.descriptors.InputDescriptor;import nz.ac.waikato.mcennis.rat.graph.descriptors.OutputDescriptor;import nz.ac.waikato.mcennis.rat.graph.descriptors.Parameter;import nz.ac.waikato.mcennis.rat.graph.descriptors.ParameterInternal;import nz.ac.waikato.mcennis.rat.graph.descriptors.SettableParameter;import nz.ac.waikato.mcennis.rat.graph.link.Link;import nz.ac.waikato.mcennis.rat.graph.model.ModelShell;import nz.ac.waikato.mcennis.rat.scheduler.Scheduler;/** * Finds all biconnected components (bicomponents) of an undirected graph.   * A graph is a biconnected component if  * at least 2 vertices must be removed in order to disconnect the graph.  (Graphs  * consisting of one vertex, or of two connected vertices, are also biconnected.)  Biconnected * components of three or more vertices have the property that every pair of vertices in the component * are connected by two or more vertex-disjoint paths. * <p> * Running time: O(|V| + |E|) where |V| is the number of vertices and |E| is the number of edges * @see "Depth first search and linear graph algorithms by R. E. Tarjan (1972), SIAM J. Comp." *  * @author Joshua O'Madadhain *  * Modified for RAT by Daniel McEnnis */public class BicomponentClusterer extends ModelShell implements Algorithm {    private ParameterInternal[] parameter = new ParameterInternal[5];    private InputDescriptor[] input = new InputDescriptor[1];    private OutputDescriptor[] output = new OutputDescriptor[1];    protected Map<Actor, Number> dfs_num;    protected Map<Actor, Number> high;    protected Map<Actor, Actor> parents;    protected Stack<Link> stack;    protected int converse_depth;    protected int graphCount = 0;    public BicomponentClusterer() {        init(null);    }    @Override    public void execute(Graph g) {        Set<Set<Actor>> bicomponents = new LinkedHashSet<Set<Actor>>();        if (g.getActor() != null) {            // initialize DFS number for each vertex to 0            dfs_num = new HashMap<Actor, Number>();            Actor[] actorList = g.getActor((String) parameter[2].getValue());            if (actorList != null) {                fireChange(Scheduler.SET_ALGORITHM_COUNT,actorList.length);                for (int i = 0; i < actorList.length; ++i) {                    dfs_num.put(actorList[i], 0);                }                for (int i = 0; i < actorList.length; ++i) {                    if (dfs_num.get(actorList[i]).intValue() == 0) // if we haven't hit this vertex yet...                    {                        high = new HashMap<Actor, Number>();                        stack = new Stack<Link>();                        parents = new HashMap<Actor, Actor>();                        converse_depth = actorList.length;                        // find the biconnected components for this subgraph, starting from v                        findBiconnectedComponents(g, actorList[i], bicomponents);                        // if we only visited one vertex, this method won't have                        // ID'd it as a biconnected component, so mark it as one                        if (actorList.length - converse_depth == 1) {                            Set<Actor> s = new HashSet<Actor>();                            s.add(actorList[i]);                            bicomponents.add(s);                        }                    }                    fireChange(Scheduler.SET_ALGORITHM_PROGRESS,i);                }            }        }        for (Set<Actor> set : bicomponents) {            try {                Properties props = new Properties();                props.setProperty("GraphClass", (String) parameter[3].getValue());                props.setProperty("GraphID", ((String) parameter[4].getValue()) + graphCount);                graphCount++;                g.addChild(g.getSubGraph(props, set));            } catch (Exception ex) {                Logger.getLogger(BicomponentClusterer.class.getName()).log(Level.SEVERE, "Actors or properties are null", ex);            }        }    }    @Override    public InputDescriptor[] getInputType() {        return input;    }    @Override    public OutputDescriptor[] getOutputType() {        return output;    }    @Override    public Parameter[] getParameter() {        return parameter;    }    @Override    public Parameter getParameter(String param) {        for (int i = 0; i < parameter.length; ++i) {            if (parameter[i].getName().contentEquals(param)) {                return parameter[i];            }        }        return null;    }    @Override    public SettableParameter[] getSettableParameter() {        return null;    }    @Override    public SettableParameter getSettableParameter(String param) {        return null;    }    /**     * Parameters to be initialized.  Subclasses should override if they provide     * any additional parameters or require additional inputs.     *      * <ol>     * <li>'name' - Name of this instance of the algorithm.  Default is ''.     * <li>'relation' - type (relation) of link to calculate over. Default 'Knows'.     * <li>'actorType' - type (mode) of actor to calculate over. Deafult 'User'.     * <li>'graphClass' - graph class used to create subgraphs     * <li>'graphIDprefix' - prefix used for graphIDs.     * </ol>     * <br>     * <br>Input 0 - Link     * <br>Output 0 - Graph     */    public void init(Properties map) {        Properties props = new Properties();        props.setProperty("Type", "java.lang.String");        props.setProperty("Name", "name");        props.setProperty("Class", "Basic");        props.setProperty("Structural", "true");        parameter[0] = DescriptorFactory.newInstance().createParameter(props);        if ((map != null) && (map.getProperty("name") != null)) {            parameter[0].setValue(map.getProperty("name"));        } else {            parameter[0].setValue("Bicomponent Clusterer");        }        props.setProperty("Type", "java.lang.String");        props.setProperty("Name", "relation");        props.setProperty("Class", "Basic");        props.setProperty("Structural", "true");        parameter[1] = DescriptorFactory.newInstance().createParameter(props);        if ((map != null) && (map.getProperty("relation") != null)) {            parameter[1].setValue(map.getProperty("relation"));        } else {            parameter[1].setValue("Knows");        }        props.setProperty("Type", "java.lang.String");        props.setProperty("Name", "actorType");        props.setProperty("Class", "Basic");        props.setProperty("Structural", "true");        parameter[2] = DescriptorFactory.newInstance().createParameter(props);        if ((map != null) && (map.getProperty("actorType") != null)) {            parameter[2].setValue(map.getProperty("actorType"));        } else {            parameter[2].setValue("User");        }        // Parameter 3 - graphClass        props.setProperty("Type", "java.lang.String");        props.setProperty("Name", "graphClass");        props.setProperty("Class", "Basic");        props.setProperty("Structural", "true");        parameter[3] = DescriptorFactory.newInstance().createParameter(props);        if ((map != null) && (map.getProperty("graphClass") != null)) {            parameter[3].setValue(map.getProperty("graphClass"));        } else {            parameter[3].setValue("MemGraph");        }        // Parameter 4 - graphID        props.setProperty("Type", "java.lang.String");        props.setProperty("Name", "graphIDprefix");        props.setProperty("Class", "Basic");        props.setProperty("Structural", "true");        parameter[4] = DescriptorFactory.newInstance().createParameter(props);        if ((map != null) && (map.getProperty("graphIDprefix") != null)) {            parameter[4].setValue(map.getProperty("graphIDprefix"));        } else {            parameter[4].setValue("Bicomponent ");        }        // Create Input Descriptors        // Construct input descriptors        props.setProperty("Type", "Link");        props.setProperty("Relation", (String) parameter[1].getValue());        props.setProperty("AlgorithmName", (String) parameter[0].getValue());        props.remove("Property");        input[0] = DescriptorFactory.newInstance().createInputDescriptor(props);        // Construct Output Descriptors        output = new OutputDescriptor[1];        // Construct Output Descriptors        props.setProperty("Type", "Link");        props.setProperty("Relation", (String) parameter[1].getValue());        props.setProperty("AlgorithmName", (String) parameter[0].getValue());        props.setProperty("Property", (String) parameter[1].getValue() + " Link Betweeness");        output[0] = DescriptorFactory.newInstance().createOutputDescriptor(props);    }    /**     * <p>Stores, in <code>bicomponents</code>, all the biconnected     * components that are reachable from <code>v</code>.</p>     *      * <p>The algorithm basically proceeds as follows: do a depth-first     * traversal starting from <code>v</code>, marking each vertex with     * a value that indicates the order in which it was encountered (dfs_num),      * and with     * a value that indicates the highest point in the DFS tree that is known     * to be reachable from this vertex using non-DFS edges (high).  (Since it     * is measured on non-DFS edges, "high" tells you how far back in the DFS     * tree you can reach by two distinct paths, hence biconnectivity.)      * Each time a new vertex w is encountered, push the edge just traversed     * on a stack, and call this method recursively.  If w.high is no greater than     * v.dfs_num, then the contents of the stack down to (v,w) is a      * biconnected component (and v is an articulation point, that is, a      * component boundary).  In either case, set v.high to max(v.high, w.high),      * and continue.  If w has already been encountered but is      * not v's parent, set v.high max(v.high, w.dfs_num) and continue.      *      * <p>(In case anyone cares, the version of this algorithm on p. 224 of      * Udi Manber's "Introduction to Algorithms: A Creative Approach" seems to be     * wrong: the stack should be initialized outside this method,      * (v,w) should only be put on the stack if w hasn't been seen already,     * and there's no real benefit to putting v on the stack separately: just     * check for (v,w) on the stack rather than v.  Had I known this, I could     * have saved myself a few days.  JRTOM)</p>     *      */    protected void findBiconnectedComponents(Graph g, Actor v, Set<Set<Actor>> bicomponents) {        int v_dfs_num = converse_depth;        dfs_num.put(v, v_dfs_num);        converse_depth--;        high.put(v, v_dfs_num);        Link[] link = getLinks(g, v);        if (link != null) {            for (int i = 0; i < link.length; ++i) {                int w_dfs_num = dfs_num.get(getOther(link[i], v)).intValue();//get(w, dfs_num);//            E vw = g.findEdge(v, w);                if (w_dfs_num == 0) // w hasn't yet been visited                {                    parents.put(getOther(link[i], v), v); // v is w's parent in the DFS tree                    stack.push(link[i]);                    findBiconnectedComponents(g, getOther(link[i], v), bicomponents);                    int w_high = high.get(getOther(link[i], v)).intValue();//get(w, high);                    if (w_high <= v_dfs_num) {                        // v disconnects w from the rest of the graph,                        // i.e., v is an articulation point                        // thus, everything between the top of the stack and                        // v is part of a single biconnected component                        Set<Actor> bicomponent = new HashSet<Actor>();                        Link e;                        do {                            e = stack.pop();                            bicomponent.add(e.getSource());                            bicomponent.add(e.getDestination());                        } while (e != link[i]);                        bicomponents.add(bicomponent);                    }                    high.put(v, Math.max(w_high, high.get(v).intValue()));                } else if (getOther(link[i], v) != parents.get(v)) // (v,w) is a back or a forward edge                {                    high.put(v, Math.max(w_dfs_num, high.get(v).intValue()));                }            }        }    }    protected Link[] getLinks(Graph g, Actor v) {        HashSet<Actor> actors = new HashSet<Actor>();        LinkedList<Link> list = new LinkedList<Link>();        Link[] out = g.getLinkBySource((String) parameter[1].getValue(), v);        if (out != null) {            for (int i = 0; i < out.length; ++i) {                actors.add(out[i].getDestination());                list.add(out[i]);            }        }        Link[] in = g.getLinkByDestination((String) parameter[1].getValue(), v);        if (in != null) {            for (int i = 0; i < in.length; ++i) {                if (!actors.contains(in[i].getSource())) {                    list.add(in[i]);                }            }        }        if (list.size() > 0) {            return list.toArray(new Link[]{});        } else {            return null;        }    }    protected Actor getOther(Link l, Actor a) {        if (l.getSource().equals(a)) {            return l.getDestination();        } else {            return l.getSource();        }    }}