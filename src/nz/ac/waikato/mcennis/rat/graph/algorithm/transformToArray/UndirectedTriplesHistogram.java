/*

 * Created 29/05/2008-14:15:01

 * Copyright Daniel McEnnis, see license.txt

 */

package nz.ac.waikato.mcennis.rat.graph.algorithm.transformToArray;



import java.util.Arrays;


import java.util.Iterator;

import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import java.util.logging.Level;

import java.util.logging.Logger;

import nz.ac.waikato.mcennis.rat.graph.Graph;

import nz.ac.waikato.mcennis.rat.graph.actor.Actor;

import nz.ac.waikato.mcennis.rat.graph.algorithm.Algorithm;

import nz.ac.waikato.mcennis.rat.graph.algorithm.AlgorithmMacros;
import nz.ac.waikato.mcennis.rat.graph.descriptors.IODescriptor;
import nz.ac.waikato.mcennis.rat.graph.descriptors.IODescriptorFactory;
import nz.ac.waikato.mcennis.rat.graph.descriptors.IODescriptorInternal;
import org.dynamicfactory.descriptors.*;


import nz.ac.waikato.mcennis.rat.graph.descriptors.IODescriptor.Type;

import nz.ac.waikato.mcennis.rat.graph.link.Link;

import org.dynamicfactory.model.ModelShell;

import nz.ac.waikato.mcennis.rat.graph.query.ActorQuery;
import nz.ac.waikato.mcennis.rat.graph.query.ActorQueryFactory;
import nz.ac.waikato.mcennis.rat.graph.query.LinkQuery;
import nz.ac.waikato.mcennis.rat.graph.query.LinkQueryFactory;
import nz.ac.waikato.mcennis.rat.graph.query.actor.ActorByMode;
import nz.ac.waikato.mcennis.rat.graph.query.link.LinkByRelation;
import org.dynamicfactory.property.InvalidObjectTypeException;
import org.dynamicfactory.property.Property;
import org.dynamicfactory.property.PropertyFactory;
import weka.core.Attribute;

import weka.core.FastVector;

import weka.core.Instance;

import weka.core.Instances;



/**

 * Rediscovery of Batagel-Mrvar algorithm

 * 

 * Batagel, V., A. Mrvar. 2001. A subquadratic triad census algorithm for large

 * sparse networks with small maximum degree. <i>Social Networks</i> 23:237-43.

 * 

 * Transforms a graph object (one mode and relation) into 4 dimensional double

 * array describing a histogram of all triples of actors in the graph.  

 * <br/>

 * Links are classified in the following way:

 * <ul>

 * <li/>0: no link exists

 * <li/>1: smaller->greater link exists

 * <li/>2: greater->smaller link exists

 * <li/>3: bi-directional link exists

 * </ul>

 * 

 * 

 * 

 * @author Daniel McEnnis

 */

public class UndirectedTriplesHistogram extends ModelShell implements Algorithm {



    PropertiesInternal parameter = PropertiesFactory.newInstance().create();
    LinkedList<IODescriptor> input = new LinkedList<IODescriptor>();
    LinkedList<IODescriptor> output = new LinkedList<IODescriptor>();

    LinkByRelation relation;
    

    public UndirectedTriplesHistogram(){
        ParameterInternal name = ParameterFactory.newInstance().create("AlgorithmClass", String.class);
        SyntaxObject syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("Directed Triples Histogram");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Name", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, Integer.MAX_VALUE, null, String.class);
        name.setRestrictions(syntax);
        name.add("Directed Triples Histogram");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Category", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("Transform To Array");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("LinkFilter", LinkQuery.class);
        syntax = SyntaxCheckerFactory.newInstance().create(0, 1, null, LinkQuery.class);
        name.setRestrictions(syntax);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("ActorFilter", ActorQuery.class);
        syntax = SyntaxCheckerFactory.newInstance().create(0, 1, null, ActorQuery.class);
        name.setRestrictions(syntax);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("SourceAppendGraphID", Boolean.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, Boolean.class);
        name.setRestrictions(syntax);
        name.add(false);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("DestinationAppendGraphID", Boolean.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, Boolean.class);
        name.setRestrictions(syntax);
        name.add(false);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("DestinationProperty", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("Transform To Array");
        parameter.add(name);
    }



    @Override

    public void execute(Graph g) {

        ActorByMode mode = (ActorByMode) ActorQueryFactory.newInstance().create("ActorByMode");
        mode.buildQuery((String) parameter.get("Mode").get(),".*",false);
//        TreeSet<Actor> triplesBase = new TreeSet<Actor>();

        relation = (LinkByRelation)LinkQueryFactory.newInstance().create("LinkByRelation");
        relation.buildQuery((String)parameter.get("Relation").get(),false);

        Iterator<Actor> iterator = AlgorithmMacros.filterActor(parameter, g, mode, null, null);
        int nonZeroCount = 0;
        int actorCount = 0;
        TreeSet<Actor> triplesBase = new TreeSet<Actor>();
        while (iterator.hasNext()) {
            iterator.next();
                triplesBase.add(iterator.next());
            actorCount++;
        }
        iterator = triplesBase.iterator();
        if (iterator.hasNext()) {

            double[] tripleCount = new double[4];

            Arrays.fill(tripleCount, 0.0);

            while (iterator.hasNext()) {
                Actor first = iterator.next();
           TreeSet<Actor> firstNeighbors = new TreeSet<Actor>();
                LinkedList<Actor> a = new LinkedList<Actor>();
                a.add(first);
                LinkedList<Actor> b = new LinkedList<Actor>();

                TreeSet<Actor> secondNeighbors = new TreeSet<Actor>();

                TreeSet<Actor> firstActorList = getLinks(g, a, a,firstNeighbors);


                for (Actor second : firstActorList) {

                    if (second.compareTo(first) > 0) {
                        b.add(second);


                        // handle all fully linked triples

                        TreeSet<Actor> secondActorList = getLinks(g,b,b,null);

                        TreeSet<Actor> fullyLinked = new TreeSet<Actor>();

                        fullyLinked.addAll(firstActorList);

                        fullyLinked.retainAll(secondActorList);

                        tripleCount[3] += fullyLinked.size();

                        

                        // handle all 2 sided triples

                        secondActorList = getLinks(g, b, a,secondNeighbors);

                        TreeSet<Actor> twoSided = new TreeSet<Actor>();

                        twoSided.addAll(secondActorList);

                        twoSided.removeAll(firstActorList);

                        tripleCount[2] += twoSided.size();

                        

                        // handle all 1 sided triples

                        firstNeighbors.addAll(secondNeighbors);

                        tripleCount[1] += actorCount - firstNeighbors.size();
                        b.clear();
                    }
                    
                }
                a.clear();
            }

            tripleCount[0] = (actorCount*(actorCount-1)*(actorCount-2))/6-tripleCount[1]-tripleCount[2]-tripleCount[3];

            FastVector base = new FastVector();

            for(int i=0;i<tripleCount.length;++i){

                base.addElement(new Attribute(AlgorithmMacros.getDestID(parameter,g,(String)parameter.get("DestinationProperty").get())+":"+i));

            }

            Instances meta = new Instances("Undirected Triples",base,1);

            Instance value = new Instance(tripleCount.length,tripleCount);

            value.setDataset(meta);
            Property property = PropertyFactory.newInstance().create("BasicProperty",AlgorithmMacros.getDestID(parameter,g,(String)parameter.get("DestinationProperty").get()),Instance.class);

            try{

                property.add(value);

                g.add(property);

            } catch (InvalidObjectTypeException ex) {

                Logger.getLogger(UndirectedTriplesHistogram.class.getName()).log(Level.SEVERE, "Property class does not match double[]", ex);

            }

        }else{

            Logger.getLogger(UndirectedTriplesHistogram.class.getName()).log(Level.SEVERE, "No actors of mode '"+(String)parameter.get("Mode").get()+"' were found in graph '"+g.getID()+"'");

        }

    }

    

        protected TreeSet<Actor> getLinks(Graph g, LinkedList<Actor> v, LinkedList<Actor> comparator, TreeSet<Actor> allNeighbors) {

    TreeSet<Actor> actors = new TreeSet<Actor>();

    if(allNeighbors != null){

        allNeighbors.clear();

    }


        Iterator<Link> out = AlgorithmMacros.filterLink(parameter, g, relation, v, null, null);

        while (out.hasNext()) {
            Link link = out.next();
                if(link.getDestination().compareTo(comparator)>0){

                actors.add(link.getDestination());

                }

                if(allNeighbors != null){

                    allNeighbors.add(link.getDestination());

                }


        }

        Iterator<Link> in = AlgorithmMacros.filterLink(parameter, g, relation, null, v, null);

        while (in.hasNext()) {
            Link link = in.next();
                if (!actors.contains(link.getSource())&&link.getSource().compareTo(comparator)>0) {

                    actors.add(link.getSource());

                }

                if(allNeighbors != null){

                    allNeighbors.add(link.getSource());

                }

        }

        return actors;

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
//
    /**

     * Parameters: <br/>

     * <br/>

     * <ul>

     * <li/><b>name</b>: name of this algorithm. Default is 'Graph Undirected Triples Histogram'

     * <li/><b>actorType</b>: mode of actor to execute over. Default is 'tag'

     * <li/><b>relation</b>: link relation to execute over. Default is 'Tags'

     * <li/><b>propertyNameSuffix</b>: Property ID suffix for the graph property.

     * Deafult is ' Undirected Triples Histogram'

     * </ul>

     * 

     * @param map parameters to be loaded - may be null

     */

    public void init(Properties map) {
        if(parameter.check(map)){
            parameter.merge(map);

            IODescriptorInternal desc = IODescriptorFactory.newInstance().create(
                    Type.GRAPH_PROPERTY,
                    (String)parameter.get("Name").get(),
                    (String)parameter.get("Mode").get(),
                    null,
                    (String)parameter.get("DestinationProperty").get(),"",
                    (Boolean)parameter.get("DestinationAppendGraphID").get());
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.LINK,
                    (String)parameter.get("Name").get(),
                    (String)parameter.get("Relation").get(),
                    null,
                    null,"");
            input.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR,
                    (String)parameter.get("Name").get(),
                    (String)parameter.get("Mode").get(),
                    null,
                    null,"");
            input.add(desc);

        }
    }

    public UndirectedTriplesHistogram prototype(){
        return new UndirectedTriplesHistogram();
    }
}

