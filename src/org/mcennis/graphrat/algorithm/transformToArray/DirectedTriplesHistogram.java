/*

 * Created 29/05/2008-14:15:01

 * Copyright Daniel McEnnis, see license.txt

 */
package org.mcennis.graphrat.algorithm.transformToArray;
/*
 *   This file is part of GraphRAT.
 *
 *   GraphRAT is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   GraphRAT is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with GraphRAT.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.util.Arrays;


import java.util.Iterator;

import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import java.util.logging.Level;

import java.util.logging.Logger;

import org.mcennis.graphrat.graph.Graph;

import org.mcennis.graphrat.actor.Actor;

import org.mcennis.graphrat.algorithm.Algorithm;

import org.mcennis.graphrat.algorithm.AlgorithmMacros;
import org.mcennis.graphrat.descriptors.IODescriptor;
import org.mcennis.graphrat.descriptors.IODescriptorFactory;
import org.mcennis.graphrat.descriptors.IODescriptorInternal;
import org.dynamicfactory.descriptors.*;


import org.mcennis.graphrat.descriptors.IODescriptor.Type;

import org.mcennis.graphrat.link.Link;

import org.dynamicfactory.model.ModelShell;

import org.mcennis.graphrat.query.ActorQuery;
import org.mcennis.graphrat.query.ActorQueryFactory;
import org.mcennis.graphrat.query.LinkQuery;
import org.mcennis.graphrat.query.LinkQueryFactory;
import org.mcennis.graphrat.query.actor.ActorByMode;
import org.mcennis.graphrat.query.link.LinkByRelation;
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

 * Transforms a graph object (one mode and relation) into 16 dimensional double

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
public class DirectedTriplesHistogram extends ModelShell implements Algorithm {

    PropertiesInternal parameter = PropertiesFactory.newInstance().create();
    LinkedList<IODescriptor> input = new LinkedList<IODescriptor>();
    LinkedList<IODescriptor> output = new LinkedList<IODescriptor>();

    LinkByRelation relation;

    public void DirectedTriplesHistogram() {
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
        double[] tripleCount = new double[16];

        Arrays.fill(tripleCount, 0.0);
        if (actorCount > 0) {
//            while (Actor first : triplesBase) {
            while (iterator.hasNext()) {
                Actor first = iterator.next();
                TreeSet<Actor> firstNeighbors = new TreeSet<Actor>();

                TreeSet<Actor> secondNeighbors = new TreeSet<Actor>();

                TreeSet<Actor> firstActorList = getLinks(g, first, first, firstNeighbors);

                TreeSet<Actor> a = new TreeSet<Actor>();
                a.add(first);
                TreeSet<Actor> b = new TreeSet<Actor>();

                TreeSet<Actor> c = new TreeSet<Actor>();

                for (Actor second : firstActorList) {

                    if (second.compareTo(first) > 0) {
                        b.add(second);


                        // handle all fully linked triples

                        TreeSet<Actor> secondActorList = getLinks(g, second, second, null);

                        secondActorList.retainAll(firstActorList);

                        int firstSecond = getLinkType(g, a, b);

                        for (Actor third : secondActorList) {
                            c.add(third);
                            int firstThird = getLinkType(g, a, c);

                            int secondThird = getLinkType(g, b, c);



                            tripleCount[mapIndex(firstSecond, firstThird, secondThird)]++;

                            nonZeroCount++;
                            c.clear();
                        }



                        // handle all 2 sided triples

                        secondActorList = getLinks(g, second, first, secondNeighbors);

                        secondActorList.removeAll(firstActorList);

                        for (Actor third : secondActorList) {
                            c.add(third);
                            int firstThird = getLinkType(g, a, c);

                            int secondThird = getLinkType(g, b, c);

                            tripleCount[mapIndex(firstSecond, firstThird, secondThird)]++;

                            nonZeroCount++;
                            c.clear();
                        }



                        // handle all 1 sided triples

                        firstNeighbors.addAll(secondNeighbors);

                        tripleCount[mapIndex(firstSecond, 0, 0)] += actorCount - firstNeighbors.size();

                        nonZeroCount += firstNeighbors.size();
                        b.clear();
                    }
                    a.clear();
                }
            }



            tripleCount[0] = (actorCount * (actorCount - 1) * (actorCount - 2)) / 6 - nonZeroCount;

            FastVector base = new FastVector();

            for (int i = 0; i < tripleCount.length; ++i) {

                base.addElement(new Attribute(g.getID() + (String) parameter.get("DestinationProperty").get() + ":" + i));

            }

            Instances meta = new Instances("Directed Triples", base, 1);

            Instance value = new Instance(tripleCount.length, tripleCount);

            value.setDataset(meta);

            Property property = PropertyFactory.newInstance().create(AlgorithmMacros.getDestID(parameter, g, (String) parameter.get("DestinationProperty").get()),(String)parameter.get("DestinationProperty").getType(), Instance.class);

            try {

                property.add(value);

                g.add(property);

            } catch (InvalidObjectTypeException ex) {

                Logger.getLogger(DirectedTriplesHistogram.class.getName()).log(Level.SEVERE, "Property class doesn't match double[]", ex);

            }

        } else {

            Logger.getLogger(DirectedTriplesHistogram.class.getName()).log(Level.WARNING, "No actors of mode '" + (String) parameter.get("Mode").get() + "' were found in graph '" + g.getID() + "'");

        }
    }

    protected int mapIndex(int first, int second, int third) {

        boolean bad = false;

        if ((first > 3) || (first < 0)) {

            Logger.getLogger(DirectedTriplesHistogram.class.getName()).log(Level.SEVERE, "First link is not 0-4:" + first);

            bad = true;
        }

        if ((second > 3) || (second < 0)) {

            Logger.getLogger(DirectedTriplesHistogram.class.getName()).log(Level.SEVERE, "Second link is not 0-4:" + second);

            bad = true;
        }

        if ((third > 3) || (third < 0)) {

            Logger.getLogger(DirectedTriplesHistogram.class.getName()).log(Level.SEVERE, "Third link is not 0-4:" + third);

            bad = true;
        }

        if (bad) {

            return -1;

        }

        switch (first) {

            case 0:

                switch (second) {

                    case 0:

                        switch (third) {

                            case 0:

                                return 0;

                            case 1:

                                return 1;

                            case 2:

                                return 1;

                            case 3:

                                return 2;

                        }

                    case 1:

                        switch (third) {

                            case 0:

                                return 1;

                            case 1:

                                return 4;

                            case 2:

                                return 5;

                            case 3:

                                return 6;

                        }

                    case 2:

                        switch (third) {

                            case 0:

                                return 1;

                            case 1:

                                return 5;

                            case 2:

                                return 3;

                            case 3:

                                return 6;

                        }

                    case 3:

                        switch (third) {

                            case 0:

                                return 2;

                            case 1:

                                return 6;

                            case 2:

                                return 6;

                            case 3:

                                return 10;

                        }

                }

            case 1:

                switch (second) {

                    case 0:

                        switch (third) {

                            case 0:

                                return 1;

                            case 1:

                                return 2;

                            case 2:

                                return 5;

                            case 3:

                                return 6;

                        }

                    case 1:

                        switch (third) {

                            case 0:

                                return 5;

                            case 1:

                                return 9;

                            case 2:

                                return 8;

                            case 3:

                                return 13;

                        }

                    case 2:

                        switch (third) {

                            case 0:

                                return 4;

                            case 1:

                                return 8;

                            case 2:

                                return 8;

                            case 3:

                                return 12;

                        }

                    case 3:

                        switch (third) {

                            case 0:

                                return 6;

                            case 1:

                                return 11;

                            case 2:

                                return 13;

                            case 3:

                                return 14;

                        }

                }

            case 2:

                switch (second) {

                    case 0:

                        switch (third) {

                            case 0:

                                return 1;

                            case 1:

                                return 5;

                            case 2:

                                return 4;

                            case 3:

                                return 6;

                        }

                    case 1:

                        switch (third) {

                            case 0:

                                return 2;

                            case 1:

                                return 8;

                            case 2:

                                return 8;

                            case 3:

                                return 11;

                        }

                    case 2:

                        switch (third) {

                            case 0:

                                return 5;

                            case 1:

                                return 9;

                            case 2:

                                return 8;

                            case 3:

                                return 13;

                        }

                    case 3:

                        switch (third) {

                            case 0:

                                return 6;

                            case 1:

                                return 13;

                            case 2:

                                return 12;

                            case 3:

                                return 14;

                        }

                }

            case 3:

                switch (second) {

                    case 0:

                        switch (third) {

                            case 0:

                                return 2;

                            case 1:

                                return 6;

                            case 2:

                                return 6;

                            case 3:

                                return 10;

                        }

                    case 1:

                        switch (third) {

                            case 0:

                                return 6;

                            case 1:

                                return 12;

                            case 2:

                                return 13;

                            case 3:

                                return 14;

                        }

                    case 2:

                        switch (third) {

                            case 0:

                                return 6;

                            case 1:

                                return 13;

                            case 2:

                                return 11;

                            case 3:

                                return 14;

                        }

                    case 3:

                        switch (third) {

                            case 0:

                                return 10;

                            case 1:

                                return 14;

                            case 2:

                                return 14;

                            case 3:

                                return 15;

                        }

                }

        }

        return -1;

    }

    protected int twoSidesMap(int twoSide, int oneSide) {

        if (twoSide == 0) {

            switch (oneSide) {

                case 1:

                    return 5;

                case 2:

                    return 6;

                case 3:

                    return 7;

                default:

                    Logger.getLogger(DirectedTriplesHistogram.class.getName()).log(Level.SEVERE, "INTERNAL ERROR - invalid oneSide link type " + oneSide + " in DirectedTriplesHistogram");







                    return -1;

            }

        } else if (twoSide == 1) {

            switch (oneSide) {

                case 0:

                    return 8;

                case 2:

                    return 9;

                case 3:

                    return 10;

                default:

                    Logger.getLogger(DirectedTriplesHistogram.class.getName()).log(Level.SEVERE, "INTERNAL ERROR - invalid oneSide link type " + oneSide + " in DirectedTriplesHistogram");







                    return -1;

            }

        } else if (twoSide == 2) {

            switch (oneSide) {

                case 0:

                    return 11;

                case 1:

                    return 12;

                case 3:

                    return 13;

                default:

                    Logger.getLogger(DirectedTriplesHistogram.class.getName()).log(Level.SEVERE, "INTERNAL ERROR - invalid oneSide link type " + oneSide + " in DirectedTriplesHistogram");







                    return -1;

            }

        } else if (twoSide == 3) {

            switch (oneSide) {

                case 0:

                    return 14;

                case 1:

                    return 15;

                case 2:

                    return 16;

                default:

                    Logger.getLogger(DirectedTriplesHistogram.class.getName()).log(Level.SEVERE, "INTERNAL ERROR - invalid oneSide link type " + oneSide + " in DirectedTriplesHistogram");







                    return -1;

            }

        } else {

            Logger.getLogger(DirectedTriplesHistogram.class.getName()).log(Level.SEVERE, "INTERNAL ERROR - invalid twoSided link type " + twoSide + " in DirectedTriplesHistogram");







            return -1;

        }

    }

    protected int getLinkType(Graph g, TreeSet<Actor> left, TreeSet<Actor> right) {


        Iterator<Link> leftToRight = AlgorithmMacros.filterLink(parameter, g, relation, left, right, null);
        //g.getLink((String) parameter[2].getValue(), left, right);

        Iterator<Link> rightToLeft = AlgorithmMacros.filterLink(parameter, g, relation, left, right, null);
        //g.getLink((String) parameter[2].getValue(), right, left);

        if ((!leftToRight.hasNext()) && (!rightToLeft.hasNext())) {

            return 0;

        } else if ((leftToRight.hasNext()) && (!rightToLeft.hasNext())) {

            return 1;

        } else if ((!leftToRight.hasNext()) && (rightToLeft.hasNext())) {

            return 2;

        } else {

            return 3;

        }

    }

    protected TreeSet<Actor> getLinks(Graph g, Actor v, Actor comparator, TreeSet<Actor> allNeighbors) {

        TreeSet<Actor> actors = new TreeSet<Actor>();
        TreeSet<Actor> source = new TreeSet<Actor>();
        source.add(v);
        TreeSet<Actor> compare = new TreeSet<Actor>();
        compare.add(comparator);
        if (allNeighbors != null) {

            allNeighbors.clear();

        }

//        Link[] out = g.getLinkBySource((String) parameter[1].getValue(), v);

        Iterator<Link> out = AlgorithmMacros.filterLink(parameter, g, relation, source, null, null);

        while (out.hasNext()){
            Link link = out.next();
                if (link.getDestination().compareTo(comparator) > 0) {

                    actors.add(link.getDestination());

                }

                if (allNeighbors != null) {

                    allNeighbors.add(link.getDestination());

                }

        }

        Iterator<Link> in = AlgorithmMacros.filterLink(parameter, g, relation, null, source, null);
        //g.getLinkByDestination((String) parameter[1].getValue(), v);

        while (in.hasNext()) {

            Link link = in.next();
                if (!actors.contains(link.getSource()) && link.getSource().compareTo(comparator) > 0) {

                    actors.add(link.getSource());

                }

                if (allNeighbors != null) {

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
    public Parameter getParameter(
            String param) {

        return parameter.get(param);

    }

    /**

     * Parameters:<br/>

     * <ul>

     * <li/><b>name</b>: name of this algorithm. Default is 'Graph Triples Histogram'

     * <li/><b>actorType</b>: mode to execute over.  Default is 'tag'

     * <li/><b>relation</b>: realtion to execute over. Default is 'Tags'

     * <li/><b>propertyNameSuffix</b>: Suffix for the property name. Default is ' Directed Triples Histogram'

     * </ul>

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

    public DirectedTriplesHistogram prototype(){
        return new DirectedTriplesHistogram();
    }
}

