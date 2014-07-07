/*

 * Created 24-3-08

 * Copyyright Daniel McEnnis, see license.txt

 */
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
package org.mcennis.graphrat.algorithm.evaluation;

import java.util.Collections;
import java.util.logging.Level;

import java.util.logging.Logger;

import org.mcennis.graphrat.graph.Graph;

import org.dynamicfactory.descriptors.*;
import org.dynamicfactory.property.Property;
import org.dynamicfactory.property.PropertyFactory;
import org.dynamicfactory.property.InvalidObjectTypeException;


import org.mcennis.graphrat.actor.Actor;
import org.mcennis.graphrat.descriptors.IODescriptor;


import org.mcennis.graphrat.link.Link;


import java.util.Iterator;




import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import org.mcennis.graphrat.algorithm.Algorithm;

import org.mcennis.graphrat.algorithm.AlgorithmMacros;
import org.mcennis.graphrat.descriptors.IODescriptor.Type;
import org.mcennis.graphrat.descriptors.IODescriptorFactory;
import org.dynamicfactory.model.ModelShell;

import org.mcennis.graphrat.query.ActorQuery;
import org.mcennis.graphrat.query.ActorQueryFactory;
import org.mcennis.graphrat.query.LinkQuery;
import org.mcennis.graphrat.query.LinkQuery.LinkEnd;
import org.mcennis.graphrat.query.LinkQueryFactory;
import org.mcennis.graphrat.query.actor.ActorByMode;
import org.mcennis.graphrat.query.link.AndLinkQuery;
import org.mcennis.graphrat.query.link.LinkByActor;
import org.mcennis.graphrat.query.link.LinkByRelation;
import org.mcennis.graphrat.scheduler.Scheduler;

/**

 * Calculates the integral of the average percent of ground truth present in a 

 * ranked list iterating over the list.

 * 

 * Herlocker, J., J. Konstan, L. Terveen, J. Reidl. 2004. Evaluating collaborative 

 * filtering recommender systems. ACM Transactions on Information Systems 22(1):5-53

 *

 * @author Daniel McEnnis

 * 

 */
public class ROCAreaEvaluation extends ModelShell implements Algorithm {

    PropertiesInternal parameter = PropertiesFactory.newInstance().create();
    LinkedList<IODescriptor> input = new LinkedList<IODescriptor>();
    LinkedList<IODescriptor> output = new LinkedList<IODescriptor>();

    /** Creates a new instance of Evaluation */
    public ROCAreaEvaluation() {
        ParameterInternal name = ParameterFactory.newInstance().create("AlgorithmClass", String.class);
        SyntaxObject syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("HalfLife");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Name", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, Integer.MAX_VALUE, null, String.class);
        name.setRestrictions(syntax);
        name.add("HalfLife");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Category", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("Evaluation");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("ActorFilter", ActorQuery.class);
        syntax = SyntaxCheckerFactory.newInstance().create(0, 1, null, ActorQuery.class);
        name.setRestrictions(syntax);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("LinkFilter", LinkQuery.class);
        syntax = SyntaxCheckerFactory.newInstance().create(0, 1, null, LinkQuery.class);
        name.setRestrictions(syntax);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("DestinationProperty", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("Evaluation");
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

        name = ParameterFactory.newInstance().create("Mode", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("tag");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Direction", LinkEnd.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, LinkEnd.class);
        name.setRestrictions(syntax);
        name.add(LinkEnd.SOURCE);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("SourceRelation", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("tag");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("DerivedRelation", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("tag");
        parameter.add(name);
    }

    /**

     * Calculate all the evaluation metrics against the given graph.

     * 

     * TODO: add F-measure stastic

     * 

     */
    public void execute(Graph g) {



        fireChange(Scheduler.SET_ALGORITHM_COUNT, 1);

        ROCArea(g);

        fireChange(Scheduler.SET_ALGORITHM_PROGRESS, 1);



    }

    protected void ROCArea(Graph g) {
        ActorByMode mode = (ActorByMode) ActorQueryFactory.newInstance().create("ActorByMode");
        mode.buildQuery((String) parameter.get("Mode").get(),".*",false);
        Iterator<Actor> it = AlgorithmMacros.filterActor(parameter, g, mode, null, null);

        LinkedList<LinkQuery> derived = new LinkedList<LinkQuery>();
        LinkByRelation derivedRelation = (LinkByRelation) LinkQueryFactory.newInstance().create("LinkByRelation");
        derivedRelation.buildQuery(AlgorithmMacros.getSourceID(parameter, g, (String) parameter.get("DerivedRelation").get()), false);
        derived.add(derivedRelation);
        LinkByActor derivedActor = (LinkByActor) LinkQueryFactory.newInstance().create("LinkByActor");
        LinkEnd end = (LinkEnd) parameter.get("LinkEnd").get();
        if (end == LinkEnd.SOURCE) {
            derivedActor.buildQuery(false, mode, null, null);
        } else {
            derivedActor.buildQuery(false, null, mode, null);
        }
        derived.add(derivedActor);
        if (!parameter.get("LinkFilter").getValue().isEmpty()) {
            derived.add((LinkQuery) parameter.get("LinkFilter").get());
        }
        AndLinkQuery derivedAnd = (AndLinkQuery) LinkQueryFactory.newInstance().create("AndLinkQuery");
        derivedAnd.buildQuery(derived);

        LinkedList<LinkQuery> source = new LinkedList<LinkQuery>();
        LinkByRelation sourceRelation = (LinkByRelation) LinkQueryFactory.newInstance().create("LinkByRelation");
        sourceRelation.buildQuery((String) parameter.get("SourceRelation").get(), false);
        source.add(sourceRelation);
        LinkByActor sourceActor = (LinkByActor) LinkQueryFactory.newInstance().create("LinkByActor");
        if (end == LinkEnd.SOURCE) {
            sourceActor.buildQuery(false, mode, null, null);
        } else {
            sourceActor.buildQuery(false, null, mode, null);
        }
        source.add(sourceActor);
        if (!parameter.get("LinkFilter").getValue().isEmpty()) {
            source.add((LinkQuery) parameter.get("LinkFilter").get());
        }
        AndLinkQuery sourceAnd = (AndLinkQuery) LinkQueryFactory.newInstance().create("AndLinkQuery");
        sourceAnd.buildQuery(source);

        try {
            double rocSquaredSum = 0.0;



            double rocSum = 0.0;


            int n = 0;
            while (it.hasNext()) {
                ++n;
                LinkedList<Actor> s = new LinkedList<Actor>();
                LinkedList<Actor> r = new LinkedList<Actor>();
                s.add(it.next());

                // determine given (ground truth) links

                Vector<Link> dLink = new Vector<Link>();
                if ((LinkEnd) parameter.get("LinkEnd").get() == LinkEnd.SOURCE) {
                    dLink.addAll(derivedAnd.execute(g, s, null, null));
                } else {
                    dLink.addAll(derivedAnd.execute(g, null, s, null));
                }
                Collections.sort(dLink);

                double givenMean = 0.0;

                double givenSD = 0.0;

                double derivedMean = 0.0;

                double derivedSD = 0.0;

                int givenSize = sourceAnd.execute(g, s, null, null).size();

                // acquire derived links


                double nextLevel = 0.0;

                double total = 0.0;

                int count = 0;

                for (int i = 0; i < dLink.size(); ++i) {
                    Link l = dLink.get(i);
                    Iterator<Link> given = null;
                    if (end == LinkEnd.SOURCE) {
                        r.add(l.getDestination());
                        given = sourceAnd.executeIterator(g, s, r, null);
                    } else {
                        r.add(l.getSource());
                        given = sourceAnd.executeIterator(g, s, r, null);
                    }
                    if (given.hasNext()) {

                        nextLevel += 1.0 / (double) givenSize;

                    }

                    total += nextLevel;

                    count++;


                    r.clear();
                }

                double roc = 0.0;

                if (givenSize == 0) {

                    roc = 1.0;

                } else {

                    roc = total / count;

                }

                Property precisionProperty = PropertyFactory.newInstance().create("BasicProperty",AlgorithmMacros.getDestID(parameter, g, (String) parameter.get("DestinationProperty").get()), Double.class);

                precisionProperty.add(new Double(roc));

                s.get(0).add(precisionProperty);

                rocSum += roc;

                rocSquaredSum += roc * roc;
                s.clear();
            }

            // calculate mean and SD of precison for graph

            double sd = ((n * rocSquaredSum) - rocSum * rocSum) / n;

            double mean = rocSum / n;
            Property property = PropertyFactory.newInstance().create("BasicProperty",AlgorithmMacros.getDestID(parameter, g, (String) parameter.get("DestinationProperty").get() + " Standard Deviation"), Double.class);
            property.add(new Double(sd));
            g.add(property);
            property = PropertyFactory.newInstance().create("BasicProperty",AlgorithmMacros.getDestID(parameter, g, (String) parameter.get("DestinationProperty").get() + " Mean"), Double.class);



            property.add(new Double(mean));



            g.add(property);



            Logger.getLogger(ROCAreaEvaluation.class.getName()).log(Level.INFO,"ROC Area\t" + mean);



            Logger.getLogger(ROCAreaEvaluation.class.getName()).log(Level.INFO,"ROC Area SD\t" + sd);

        } catch (InvalidObjectTypeException ex) {

            Logger.getLogger(ROCAreaEvaluation.class.getName()).log(Level.SEVERE, null, ex);

        }



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

     * Parameters to be initialized

     * 

     * <ol>

     * <li>'name' - name of this instance of this algorithm. Deafult 'Evaluation'

     * <li>'relationGroundTruth' - type (relation) of link that describes ground

     * truth relations between two actor types (modes). Default 'Given'

     * <li>'relationDerived' - type (relation) of link that describes calculated

     * references to be evaluated between two modes. Deafult 'Derived'.

     * <li>'propertyPrefix' - prefix for the property names generated. Default 

     * 'Evaluation '.

     * </ol>

     * <br>

     * <br>Input 1 - Link

     * <br>Input 2 - Link

     * <br>Output 1-12 - Graph Property

     * 

     * 

     */
    public void init(Properties map) {
        if (parameter.check(map)) {
            parameter.merge(map);

            IODescriptor desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR,
                    (String) parameter.get("Name").get(),
                    (String) parameter.get("Mode").get(),
                    null,
                    null,
                    "",
                    false);
            input.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.LINK,
                    (String) parameter.get("Name").get(),
                    (String) parameter.get("SourceRelation").get(),
                    null,
                    null,
                    "",
                    false);
            input.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.LINK,
                    (String) parameter.get("Name").get(),
                    (String) parameter.get("DerivedRelation").get(),
                    null,
                    null,
                    "",
                    (Boolean) parameter.get("SourceAppendGraphID").get());
            input.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR_PROPERTY,
                    (String) parameter.get("Name").get(),
                    (String) parameter.get("Mode").get(),
                    null,
                    (String) parameter.get("DestinationProperty").get(),
                    "",
                    (Boolean) parameter.get("SourceAppendGraphID").get());
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.GRAPH_PROPERTY,
                    (String) parameter.get("Name").get(),
                    null,
                    null,
                    (String) parameter.get("DestinationProperty").get() + " StandardDeviation",
                    "",
                    (Boolean) parameter.get("SourceAppendGraphID").get());
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.GRAPH_PROPERTY,
                    (String) parameter.get("Name").get(),
                    null,
                    null,
                    (String) parameter.get("DestinationProperty").get() + " Mean",
                    "",
                    (Boolean) parameter.get("SourceAppendGraphID").get());
            output.add(desc);
        }

    }

    public ROCAreaEvaluation prototype() {
        return new ROCAreaEvaluation();
    }
}

