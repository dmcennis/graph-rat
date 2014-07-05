/*

 * OptimizedBetweeness.java

 *

 * Created on 22 October 2007, 16:27

 *

 * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt)

 */

package nz.ac.waikato.mcennis.rat.graph.algorithm.prestige;




import java.util.Iterator;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import java.util.logging.Logger;

import nz.ac.waikato.mcennis.rat.graph.Graph;
import nz.ac.waikato.mcennis.rat.graph.algorithm.Algorithm;
import nz.ac.waikato.mcennis.rat.graph.algorithm.AlgorithmMacros;
import org.dynamicfactory.descriptors.IODescriptor;

import org.dynamicfactory.descriptors.IODescriptor.Type;
import org.dynamicfactory.descriptors.IODescriptorFactory;
import org.dynamicfactory.descriptors.IODescriptorInternal;

import nz.ac.waikato.mcennis.rat.graph.link.Link;

import nz.ac.waikato.mcennis.rat.graph.model.Listener;

import nz.ac.waikato.mcennis.rat.graph.model.Model;

import nz.ac.waikato.mcennis.rat.graph.model.ModelShell;

import nz.ac.waikato.mcennis.rat.graph.path.PathNode;

import nz.ac.waikato.mcennis.rat.graph.query.ActorQuery;
import nz.ac.waikato.mcennis.rat.graph.query.LinkQuery;
import nz.ac.waikato.mcennis.rat.graph.query.LinkQueryFactory;
import nz.ac.waikato.mcennis.rat.graph.query.link.LinkByRelation;
import nz.ac.waikato.mcennis.rat.reusablecores.OptimizedLinkBetweenessCore;



/**

 * Class built upon OptimizedPathBase that calculates Betweeness for links.  OptimizedPathBase

 * only records one geodesic path for each actor pair, otherwise same as the 

 * betweeness metric deefined in Freeman79. 

 * <br>

 * <br>

 * Freeman, L. "Centrality in social networks: I. Conceptual clarification."

 * <i>Social Networks</i>. 1:215--239.

 *

 * @author Daniel McEnnis

 * 

 */

public class LinkBetweeness extends ModelShell implements Algorithm, Listener {



    PropertiesInternal parameter = PropertiesFactory.newInstance().create();
    LinkedList<IODescriptor> input = new LinkedList<IODescriptor>();
    LinkedList<IODescriptor> output = new LinkedList<IODescriptor>();

    protected double maxBetweeness = 0.0;

    protected OptimizedLinkBetweenessCore core = new OptimizedLinkBetweenessCore();



    /** Creates a new instance of OptimizedBetweeness */

    public LinkBetweeness() {

        super();

        ParameterInternal name = ParameterFactory.newInstance().create("AlgorithmClass", String.class);
        SyntaxObject syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("HITS Prestige");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Name", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, Integer.MAX_VALUE, null, String.class);
        name.setRestrictions(syntax);
        name.add("HITS Prestige");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Category", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("Prestige");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("LinkFilter", LinkQuery.class);
        syntax = SyntaxCheckerFactory.newInstance().create(0, 1, null, LinkQuery.class);
        name.setRestrictions(syntax);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("ActorFilter", ActorQuery.class);
        syntax = SyntaxCheckerFactory.newInstance().create(0, 1, null, ActorQuery.class);
        name.setRestrictions(syntax);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("DestinationAppendGraphID", Boolean.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, Boolean.class);
        name.setRestrictions(syntax);
        name.add(false);
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

        name = ParameterFactory.newInstance().create("DestinationProperty", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("Property");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("TeleportFactor", Double.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, Double.class);
        name.setRestrictions(syntax);
        name.add(0.15);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("StoreMatrix", Boolean.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, Boolean.class);
        name.setRestrictions(syntax);
        name.add(false);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Normalize", Boolean.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, Boolean.class);
        name.setRestrictions(syntax);
        name.add(true);
        parameter.add(name);

        core.setProperties(parameter);

    }



    public void execute(Graph g){

        LinkByRelation relation = (LinkByRelation)LinkQueryFactory.newInstance().create("LinkByRelation");
        relation.buildQuery((String)parameter.get("Relation").get(),false);

        try{

        core.execute(g);

        Map<Link,Double> linkMap = core.getLinkMap();

        if (((Boolean) parameter.get("Normalize").get()).booleanValue()) {

            Logger.getLogger(LinkBetweeness.class.getName()).log(Level.INFO, "Normalizing Betweeness values");

            double norm = 0.0;

            Iterator<Link> it = linkMap.keySet().iterator();

            while (it.hasNext()) {

                Link l = it.next();

                norm += linkMap.get(l) * linkMap.get(l);



            }

            it = linkMap.keySet().iterator();

            while (it.hasNext()) {

                Link l = it.next();

                linkMap.put(l, linkMap.get(l) / Math.sqrt(norm));

            }

        }

        Link[] links = (AlgorithmMacros.filterLink(parameter, g, relation.execute(g, null, null, null))).toArray(new Link[]{});

        if(links != null){

        for(int i=0;i<links.length;++i) {
            Property prop = PropertyFactory.newInstance().create(AlgorithmMacros.getDestID(parameter, g, (String)parameter.get("DesitnationProperty").get()),Double.class);

            double betweeness = 0.0;

            if(linkMap.containsKey(links[i])){

                betweeness = linkMap.get(links[i]);

            }

            prop.add(new Double(betweeness));

            

            links[i].add(prop);

            if (betweeness > maxBetweeness) {

                maxBetweeness = betweeness;

            }

        }

        }

       } catch (InvalidObjectTypeException ex) {

            Logger.getLogger(LinkBetweeness.class.getName()).log(Level.SEVERE, "Property class of Betweeness does not match java.lang.Double", ex);

        }

    }

    

    protected void doCleanup(PathNode[] path, Graph g) {

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

     * Parameters to be initialized.  Subclasses should override if they provide

     * any additional parameters or require additional inputs.

     * 

     * <ol>

     * <li>'name' - Name of this instance of the algorithm.  Default is ''.

     * <li>'relation' - type (relation) of link to calculate over. Default 'Knows'.

     * <li>'actorType' - type (mode) of actor to calculate over. Deafult 'User'.

     * <li>'normalize' - boolean for whether or not to normalize prestige vectors. 

     * Default 'false'.

     * </ol>

     * <br>

     * <br>Input 0 - Link

     * <br>Output 0 - LinkProperty

     */

    public void init(Properties map) {
        if(parameter.check(map)){
            parameter.merge(map);
            IODescriptorInternal desc = IODescriptorFactory.newInstance().create(
                    Type.LINK,
                    (String)parameter.get("Name").get(),
                    (String)parameter.get("Relation").get(),
                    null,
                    null,
                    "",
                    false);
            input.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.LINK_PROPERTY,
                    (String)parameter.get("Name").get(),
                    (String)parameter.get("Relation").get(),
                    null,
                    (String)parameter.get("DestinationProperty").get(),
                    "",
                    (Boolean)parameter.get("DestinationAppendGraphID").get());
            output.add(desc);
        }
    }



    public void publishChange(Model m, int type, int argument) {

        fireChange(type,argument);

    }

    public LinkBetweeness prototype(){
        return new LinkBetweeness();
    }

}

