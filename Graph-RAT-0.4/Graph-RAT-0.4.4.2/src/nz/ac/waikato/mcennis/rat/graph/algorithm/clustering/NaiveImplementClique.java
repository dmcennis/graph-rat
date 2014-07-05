/* * NaiveImplementClique.java * * Created on 18 October 2007, 13:07 * * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt) */package nz.ac.waikato.mcennis.rat.graph.algorithm.clustering;import nz.ac.waikato.mcennis.rat.graph.algorithm.*;import java.util.Properties;import java.util.logging.Level;import java.util.logging.Logger;import nz.ac.waikato.mcennis.rat.graph.Clique;import nz.ac.waikato.mcennis.rat.graph.Graph;import nz.ac.waikato.mcennis.rat.graph.actor.Actor;import org.dynamicfactory.descriptors.DescriptorFactory;import org.dynamicfactory.descriptors.InputDescriptor;import org.dynamicfactory.descriptors.InputDescriptorInternal;import org.dynamicfactory.descriptors.OutputDescriptor;import org.dynamicfactory.descriptors.OutputDescriptorInternal;import org.dynamicfactory.descriptors.Parameter;import org.dynamicfactory.descriptors.ParameterInternal;import org.dynamicfactory.descriptors.SettableParameter;import nz.ac.waikato.mcennis.rat.graph.model.ModelShell;import nz.ac.waikato.mcennis.rat.scheduler.Scheduler;/** * Implements the traditional naive implementation of finidng cliques.  This  * version finds cliques by examining all possible subgraphs. * * @author Daniel McEnnis *  */public class NaiveImplementClique extends ModelShell implements Algorithm {    ParameterInternal[] parameter = new ParameterInternal[5];    InputDescriptorInternal[] input = new InputDescriptorInternal[1];    OutputDescriptorInternal[] output = new OutputDescriptorInternal[1];    /** Creates a new instance of NaiveImplementClique */    public NaiveImplementClique() {    }    /**     * A naive implementation of the clique algorithm for runtime comparison to      * the optimized version EnumerateMaximalCliques     */    public void execute(Graph g) {        // enumerate all subgraphs        Actor[] a = g.getActor((String) parameter[2].getValue());        int k;        int limit = (Integer) parameter[4].getValue();        if (limit > a.length) {            limit = a.length;        }        java.util.LinkedList<SubGroup> newGroups = new java.util.LinkedList<SubGroup>();        java.util.LinkedList<SubGroup> oldGroups = new java.util.LinkedList<SubGroup>();        java.util.LinkedList<SubGroup> cliques = new java.util.LinkedList<SubGroup>();        java.util.LinkedList<SubGroup> maximalCliques = new java.util.LinkedList<SubGroup>();        for (int i = 0; i < a.length; ++i) {            SubGroup subGroup = new SubGroup();            subGroup.g = g;            subGroup.nodes.add(a[i]);            subGroup.max = a[i];            oldGroups.add(subGroup);        }        fireChange(Scheduler.SET_ALGORITHM_COUNT,limit*a.length);        // for every clique level        for (int i = 0; i < limit; ++i) {            Logger.getLogger(NaiveImplementClique.class.getName()).log(Level.FINE, "Finding cliques of level " + i);            // find all subsets            for (int j = i; j < a.length; ++j) {                Logger.getLogger(NaiveImplementClique.class.getName()).log(Level.FINE, "Expanding on actor " + j);                java.util.Iterator<SubGroup> it = oldGroups.iterator();                int count = newGroups.size();                while (it.hasNext()) {                    SubGroup n = it.next();                    if (a[j].compareTo(n.max) > 0) {                        newGroups.add(n.expand(a[j]));                    } else {                        ;                    }                }                Logger.getLogger(NaiveImplementClique.class.getName()).log(Level.FINE, "Actor " + j + " added " + (newGroups.size() - count) + " subgroups");                fireChange(Scheduler.SET_ALGORITHM_PROGRESS,i*a.length+j);            }            Logger.getLogger(NaiveImplementClique.class.getName()).log(Level.FINE, "Determining Cliques of " + newGroups.size() + " subgroups");            // determine cliques of all subsets of this type            java.util.Iterator<SubGroup> it = newGroups.iterator();            while (it.hasNext()) {                SubGroup current = it.next();                if (current.isClique()) {                    cliques.add(current);                }            }            oldGroups = newGroups;            newGroups = new java.util.LinkedList<SubGroup>();        }        java.util.Iterator<SubGroup> it = cliques.iterator();        while (it.hasNext()) {            SubGroup clique = it.next();            boolean maximal = true;            for (int i = 0; i < a.length; ++i) {                if (!clique.isPresent(a[i])) {                    if (clique.expand(a[i]).isClique()) {                        maximal = false;                    }                }            }            if (maximal) {                maximalCliques.add(clique);            }        }        it = maximalCliques.iterator();        int count = 0;        while (it.hasNext()) {            Clique c = it.next().makeClique();            c.setID((String) parameter[2].getValue() + count);            count++;            g.addChild(c);        }    }    @Override    public InputDescriptor[] getInputType() {        return input;    }    @Override    public OutputDescriptor[] getOutputType() {        return output;    }    @Override    public Parameter[] getParameter() {        return parameter;    }    @Override    public Parameter getParameter(String param) {        for (int i = 0; i < parameter.length; ++i) {            if (parameter[i].getName().contentEquals(param)) {                return parameter[i];            }        }        return null;    }    @Override    public SettableParameter[] getSettableParameter() {        return null;    }    @Override    public SettableParameter getSettableParameter(String param) {        return null;    }    /**     * Parameters to initialize     * <ol>     * <li>'name' - name used for this instance of the algorithm. Default is 'Naive Enumerate Maximal Cliques'.     * <li>'relation' - type (relation) of link to calculate over. Default 'Knows'.     * <li>'actorType' - type (mode) of actor to calculate over. Deafult 'User'     * <li>'graphIDPrefix' - prefix used when generating graph IDs for cliques.  Default 'clique'.     * </ol>     * <br>     * <br>Input 0 - Link     * <br>Output 0 - Graph     */    public void init(Properties map) {        // set all parameters        Properties props = new Properties();        props.setProperty("Type", "java.lang.String");        props.setProperty("Name", "name");        props.setProperty("Class", "Basic");        props.setProperty("Structural", "true");        parameter[0] = DescriptorFactory.newInstance().createParameter(props);        if ((map != null) && (map.getProperty("name") != null)) {            parameter[0].setValue(map.getProperty("name"));        } else {            parameter[0].setValue("Naive Enumerate Maximal Cliques");        }        // Parameter 1 - relation        props.setProperty("Type", "java.lang.String");        props.setProperty("Name", "relation");        props.setProperty("Class", "Basic");        props.setProperty("Structural", "true");        parameter[1] = DescriptorFactory.newInstance().createParameter(props);        if ((map != null) && (map.getProperty("relation") != null)) {            parameter[1].setValue(map.getProperty("relation"));        } else {            parameter[1].setValue("Knows");        }        // Parameter 2 - actor type        props.setProperty("Type", "java.lang.String");        props.setProperty("Name", "actorType");        props.setProperty("Class", "Basic");        props.setProperty("Structural", "true");        parameter[2] = DescriptorFactory.newInstance().createParameter(props);        if ((map != null) && (map.getProperty("actorType") != null)) {            parameter[2].setValue(map.getProperty("actorType"));        } else {            parameter[2].setValue("User");        }        // Parameter 3 - graph ID prefix        props.setProperty("Type", "java.lang.String");        props.setProperty("Name", "graphIDPrefix");        props.setProperty("Class", "Basic");        props.setProperty("Structural", "true");        parameter[3] = DescriptorFactory.newInstance().createParameter(props);        if ((map != null) && (map.getProperty("graphIDPrefix") != null)) {            parameter[3].setValue(map.getProperty("graphIDPrefix"));        } else {            parameter[3].setValue("clique");        }        // Parameter 4 - max degree to check        props.setProperty("Type", "java.lang.Integer");        props.setProperty("Name", "cliqueSizeLimit");        props.setProperty("Class", "Basic");        props.setProperty("Structural", "true");        parameter[4] = DescriptorFactory.newInstance().createParameter(props);        if ((map != null) && (map.getProperty("cliqueSizeLimit") != null)) {            parameter[4].setValue(Integer.parseInt(map.getProperty("cliqueSizeLimit")));        } else {            parameter[4].setValue(new Integer(50));        }        // init input 0        props.setProperty("Type", "Link");        props.setProperty("Relation", (String) parameter[1].getValue());        props.setProperty("AlgorithmName", (String) parameter[0].getValue());        props.remove("Property");        input[0] = DescriptorFactory.newInstance().createInputDescriptor(props);        // init output 0        // Construct Output Descriptors        props.setProperty("Type", "Graph");        props.remove("Relation");        props.setProperty("AlgorithmName", (String) parameter[0].getValue());        props.remove("Property");        output[0] = DescriptorFactory.newInstance().createOutputDescriptor(props);    }    /**     * Helper class for describing sub-groups.     */    public class SubGroup {        public Graph g = null;        public Actor max = null;        public java.util.HashSet<Actor> nodes = new java.util.HashSet<Actor>();        public SubGroup() {        }        /**         * Evaluates whether or not this subgroup is a clique or not.         * @return is this subgroup a clique         */        public boolean isClique() {            java.util.Iterator<Actor> it = nodes.iterator();            while (it.hasNext()) {                Actor left = it.next();                java.util.Iterator<Actor> it2 = nodes.iterator();                while (it2.hasNext()) {                    Actor right = it2.next();                    if (left.compareTo(right) != 0) {                        if (g.getLink((String) parameter[2].getValue(), left, right) == null) {                            return false;                        }                    }                }            }            return true;        }        /**         * Generate the next subgroup from this subgroup.         * @param a actor to add to this subgroup.         * @return next subgroup         */        public SubGroup expand(Actor a) {            SubGroup ret = new SubGroup();            ret.g = this.g;            if (max == null) {                ret.max = a;            } else if (a.compareTo(max) > 0) {                ret.max = a;            } else {                ret.max = max;            }            ret.nodes.add(a);            return ret;        }        /**         * If this is a clique, create it from this subgroup         * @return clique representation of this subgroup         */        public Clique makeClique() {            Clique ret = new Clique();            java.util.Iterator<Actor> it = nodes.iterator();            while (it.hasNext()) {                ret.add(it.next());            }            return ret;        }        /**         * Does this subgroup contain the actor or not         * @param a actor to check existance of         * @return is the actor present         */        public boolean isPresent(Actor a) {            if (nodes.contains(a)) {                return true;            } else {                return false;            }        }    }}