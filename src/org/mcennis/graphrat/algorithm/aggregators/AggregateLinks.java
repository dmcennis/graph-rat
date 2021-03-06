/**
 * Jul 23, 2008-5:34:26 PM
 * Copyright Daniel McEnnis, see license.txt
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
package org.mcennis.graphrat.algorithm.aggregators;

import java.util.HashMap;
import java.util.TreeSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mcennis.graphrat.graph.Graph;
import org.mcennis.graphrat.actor.Actor;
import org.mcennis.graphrat.algorithm.Algorithm;
import org.mcennis.graphrat.algorithm.AlgorithmMacros;
import org.dynamicfactory.descriptors.*;
import org.dynamicfactory.property.Property;
import org.dynamicfactory.property.PropertyFactory;
import org.dynamicfactory.property.InvalidObjectTypeException;
import org.mcennis.graphrat.descriptors.IODescriptorFactory;
import org.mcennis.graphrat.descriptors.IODescriptor;
import org.mcennis.graphrat.descriptors.IODescriptor.Type;
import org.mcennis.graphrat.descriptors.IODescriptorInternal;
import org.mcennis.graphrat.link.Link;
import org.dynamicfactory.model.ModelShell;
import org.mcennis.graphrat.query.ActorQuery;
import org.mcennis.graphrat.query.ActorQueryFactory;
import org.mcennis.graphrat.query.LinkQuery;
import org.mcennis.graphrat.query.LinkQuery.LinkEnd;
import org.mcennis.graphrat.query.LinkQueryFactory;
import org.mcennis.graphrat.query.actor.ActorByLink;
import org.mcennis.graphrat.query.actor.ActorByMode;
import org.mcennis.graphrat.query.link.LinkByRelation;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

/**
 * 
 * Aggregates a single property across links.  This property is converted into 
 * a Weka Instance object if it is not already.  Aggregation can be across incoming links, outgoing
 * links, or both. This aggregation is performed across all actors of a given mode.  
 * Two aggregators functions are used.  The first aggregates values 
 * on a single actor on a single property.  The second aggregates values across 
 * actors.  
 * 
 * @author Daniel McEnnis
 */
public class AggregateLinks extends ModelShell implements Algorithm {

    PropertiesInternal parameter = PropertiesFactory.newInstance().create();
    LinkedList<IODescriptor> input = new LinkedList<IODescriptor>();
    LinkedList<IODescriptor> output = new LinkedList<IODescriptor>();

    /**
     * Create a new algorithm utilizing default parameters.
     */
    public AggregateLinks() {
        ParameterInternal name = ParameterFactory.newInstance().create("AlgorithmClass",String.class);
        SyntaxObject syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("Aggregate By Link");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Name",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,Integer.MAX_VALUE,null,String.class);
        name.setRestrictions(syntax);
        name.add("Aggregate By Link");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Category",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("Aggregator");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("LinkFilter",LinkQuery.class);
        syntax = SyntaxCheckerFactory.newInstance().create(0,1,null,LinkQuery.class);
        name.setRestrictions(syntax);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("ActorFilter",ActorQuery.class);
        syntax = SyntaxCheckerFactory.newInstance().create(0,1,null,ActorQuery.class);
        name.setRestrictions(syntax);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("SourceAppendGraphID",Boolean.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,Boolean.class);
        name.setRestrictions(syntax);
        name.add(false);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("DestinationAppendGraphID",Boolean.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,Boolean.class);
        name.setRestrictions(syntax);
        name.add(false);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Mode",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("tag");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Relation",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("tag");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("DestinationProperty",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("Property");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("MakeSingleValueInstance",Boolean.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,Boolean.class);
        name.setRestrictions(syntax);
        name.add(true);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("AppendGraphID",Boolean.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,Boolean.class);
        name.setRestrictions(syntax);
        name.add(false);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("ActorEnd",LinkEnd.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,LinkEnd.class);
        name.setRestrictions(syntax);
        name.add(LinkEnd.SOURCE);
        parameter.add(name);
    }

    @Override
    public void execute(Graph g) {
        ActorByMode mode = (ActorByMode)ActorQueryFactory.newInstance().create("ActorByMode");
        mode.buildQuery((String)parameter.get("Mode").get(),".*",false);
        Iterator<Actor> actorIt = AlgorithmMacros.filterActor(parameter, g, mode, null, null);

        if (actorIt.hasNext()) {
            int count = 0;

            // create the Instances metadata object
            Instances meta = new Instances("AggregateLinks", new FastVector(), 1);
            FastVector attributeList = new FastVector();
            FastVector attributeItems = new FastVector();
            LinkByRelation linkQuery = (LinkByRelation)LinkQueryFactory.newInstance().create("LinkByRelation");
            linkQuery.buildQuery((String)parameter.get("Relation").get(),false);
            Iterator<Link> link = AlgorithmMacros.filterLink(parameter, g, linkQuery, null, null, null);
            Set<Actor> list = null;
            if ((Boolean) parameter.get("MakeSingleInstance").get()) {
                list = new TreeSet<Actor>();
                if (link != null) {
                    while(link.hasNext()) {
        if(((LinkEnd)parameter.get("ActorEnd").get())==LinkEnd.ALL){
            Link l = link.next();
            list.add(l.getSource());
            list.add(l.getDestination());
        }else if(((LinkEnd)parameter.get("ActorEnd").get())==LinkEnd.SOURCE){
            list.add(link.next().getDestination());
        }else{
            list.add(link.next().getSource());
        }
                    }
                    for (Actor name : list) {
                        attributeItems.addElement(name.getID());
                    }
                    attributeItems.addElement("None");
                    attributeList.addElement(new Attribute("ActorList", attributeItems));
                    meta = new Instances("AggregateLinks", attributeList, 1);
                } else {
                    Logger.getLogger(AggregateLinks.class.getName()).log(Level.WARNING, "No links of type '" + (String) parameter.get("Relation").get() + "' are found");
                }
            } else {
                list = new TreeSet<Actor>();
                if (link != null) {
                    while (link.hasNext()) {
        if(((LinkEnd)parameter.get("ActorEnd").get())==LinkEnd.ALL){
            Link l = link.next();
            list.add(l.getSource());
            list.add(l.getDestination());
        }else if(((LinkEnd)parameter.get("ActorEnd").get())==LinkEnd.SOURCE){
            list.add(link.next().getDestination());
        }else{
            list.add(link.next().getSource());
        }
                    }
                    for (Actor name : list) {
                        attributeList.addElement(new Attribute(name.getID()));
                    }
                    meta = new Instances("AggregateLinks", attributeList, 1);
                } else {
                    Logger.getLogger(AggregateLinks.class.getName()).log(Level.WARNING, "No links of type '" + (String) parameter.get("Relation").get() + "' are found");
                }
            }
            while (actorIt.hasNext()) {
                Actor actor = actorIt.next();
                double[] values = new double[meta.numAttributes()];
                TreeSet<Actor> total = new TreeSet<Actor>();
                HashMap<Actor, Double> links = getMap(actor, total, g);
                Instance instance = null;
                if ((Boolean) parameter.get("MakeSingleInstance").get()) {
                    if (links.size() > 0) {
                        String name = links.keySet().iterator().next().getID();
                        values[0] = meta.attribute("ActorList").indexOfValue(name);
                    } else {
                        values[0] = Double.NaN;
                    }
                    instance = new Instance(values.length, values);
                    instance.setDataset(meta);
                } else {
                    Iterator<Actor> it = list.iterator();
                    for (int j = 0; j < list.size(); ++j) {
                        Actor artist = it.next();
                        if (links.containsKey(artist)) {
                            values[j] = links.get(artist);
                        } else {
                            values[j] = Double.NaN;
                        }
                    }
                    instance = new Instance(values.length, values);
                    instance.setDataset(meta);
                }


                Property aggregator = PropertyFactory.newInstance().create("BasicProperty",AlgorithmMacros.getDestID(parameter,g, (String)parameter.get("DestinationProperty").get()),weka.core.Instance.class);
                try {
                    aggregator.add(instance);
                } catch (InvalidObjectTypeException ex) {
                    Logger.getLogger(AggregateLinks.class.getName()).log(Level.SEVERE, null, ex);
                }
                actor.add(aggregator);
            }

        } else {
            Logger.getLogger(AggregateLinks.class.getName()).log(Level.WARNING, "No tags of mode '" + (String) parameter.get("Mode").get() + "' are present");
        }
    }

    protected HashMap<Actor, Double> getMap(Actor tag, TreeSet<Actor> total, Graph g) {
        HashMap<Actor, Double> ret = new HashMap<Actor, Double>();
            TreeSet<Actor> actor = new TreeSet<Actor>();
            actor.add(tag);
            LinkByRelation query = (LinkByRelation)LinkQueryFactory.newInstance().create("LinkByRelation");
            query.buildQuery((String)parameter.get("Relation").get(),false);

        if ( (((LinkEnd)parameter.get("ActorEnd").get())==LinkEnd.SOURCE) || (((LinkEnd)parameter.get("ActorEnd").get())==LinkEnd.ALL)) {
            Iterator<Link> link = AlgorithmMacros.filterLink(parameter, g, query, actor, null, null);
                while (link.hasNext()) {
                    Link l = link.next();
                    ret.put(l.getDestination(), l.getStrength());
                    total.add(l.getDestination());
                }
        }
        if ( (((LinkEnd)parameter.get("ActorEnd").get())==LinkEnd.DESTINATION) || (((LinkEnd)parameter.get("ActorEnd").get())==LinkEnd.ALL)) {
            Iterator<Link> link = AlgorithmMacros.filterLink(parameter, g, query, null, actor, null);
                while (link.hasNext()) {
                    Link l = link.next();
                    total.add(l.getSource());
                    if (!ret.containsKey(l.getSource())) {
                        ret.put(l.getSource(), l.getStrength());
                    } else {
                        ret.put(l.getSource(), l.getStrength() + ret.get(l.getSource()));
                    }
                }
        }
        return ret;
    }

    public Instances buildInstances(Graph g) {
        Instances ret = new Instances("AggregateLinks", new FastVector(), 1);
        FastVector attributeList = new FastVector();
        FastVector attributeItems = new FastVector();
            LinkByRelation linkQuery = (LinkByRelation)LinkQueryFactory.newInstance().create("LinkByRelation");
            linkQuery.buildQuery((String)parameter.get("Relation").get(),false);
            Iterator<Link> link = AlgorithmMacros.filterLink(parameter, g, linkQuery, null, null, null);
        if ((Boolean) parameter.get("MakeSingleInstance").get()) {
            TreeSet<String> list = new TreeSet<String>();
            if (link.hasNext()) {
                while (link.hasNext()) {
        if(((LinkEnd)parameter.get("ActorEnd").get())==LinkEnd.ALL){
            Link l = link.next();
            list.add(l.getSource().getID());
            list.add(l.getDestination().getID());
        }else if(((LinkEnd)parameter.get("ActorEnd").get())==LinkEnd.SOURCE){
            list.add(link.next().getDestination().getID());
        }else{
            list.add(link.next().getSource().getID());
        }
                }
                for (String name : list) {
                    attributeItems.addElement(name);
                }
                attributeList.addElement(attributeItems);
                ret = new Instances("AggregateLinks", attributeList, 1);
            } else {
                Logger.getLogger(AggregateLinks.class.getName()).log(Level.WARNING, "No links of type '" + (String) parameter.get("Relation").get() + "' are found");
            }
            return ret;
        } else {
            TreeSet<String> list = new TreeSet<String>();
            if (link.hasNext()) {
                while (link.hasNext()) {
        if(((LinkEnd)parameter.get("ActorEnd").get())==LinkEnd.ALL){
            Link l = link.next();
            list.add(l.getSource().getID());
            list.add(l.getDestination().getID());
        }else if(((LinkEnd)parameter.get("ActorEnd").get())==LinkEnd.SOURCE){
            list.add(link.next().getDestination().getID());
        }else{
            list.add(link.next().getSource().getID());
        }
                }
                for (String name : list) {
                    attributeList.addElement(new Attribute(name));
                }
                ret = new Instances("AggregateLinks", attributeList, 1);
            } else {
                Logger.getLogger(AggregateLinks.class.getName()).log(Level.WARNING, "No links of type '" + (String) parameter.get("Relation").get() + "' are found");
            }
            return ret;
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
     * Parameters:<br/>
     * <br/>
     * <ul>
     * <li/><b>name</b>: Name of the algorithm. Default is 'Aggregate By Link'
     * <li/><b>actorType</b>: Mode of the actor to compare. Default is 'tag'
     * <li/><b>relation</b>: Relation to calculate similarity from. Default is 'Tags'
     * <li/><b>innerAggregator</b>: Function for aggregating values inside a property on an actor.
     * Deafult value is 'org.mcennis.graphrat.algorithm.reusablecores.aggregator.FirstItemAggregatorFunction'
     * <li/><b>outerAggregator</b>: Function for aggreagting across actors. Deafult
     * is 'org.mcennis.graphrat.algorithm.reusablecores.aggregator.SumAggregator'
     * <li/><b>sourceProperty</b>: Property to aggregate across links. Default 
     * is 'actor profile'
     * <li/><b>destinationProperty</b>: Property to store result of aggreation in.
     * Default is 'actorProperty'
     * <li/><b>linkDirection</b>: type of link to use- Incoming, Ougoing, or All.
     * Default is 'Outgoing'
     * </ul>
     * @param map parameters to be loaded - may be null
     */
    public void init(Properties map) {
        if(parameter.check(map)){
            parameter.merge(map);

            IODescriptorInternal desc = IODescriptorFactory.newInstance().create(
                    Type.LINK,
                    (String)parameter.get("Name").get(),
                    (String)parameter.get("Relation").get(),
                    null,
                    null,"");
            input.add(desc);

            if(((LinkEnd)parameter.get("ActorEnd").get())==LinkEnd.ALL){
                desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR,
                    (String)parameter.get("Name").get(),
                    (String)parameter.get("Mode").get(),
                    null,
                    null,"");
                input.add(desc);
            }else{
                ActorByLink actorQuery = (ActorByLink)ActorQueryFactory.newInstance().create("ActorByLink");
                LinkByRelation linkQuery = (LinkByRelation)LinkQueryFactory.newInstance().create("LinkByRelation");
                linkQuery.buildQuery((String)parameter.get("Relation").get(), false);
                actorQuery.buildQuery((LinkEnd)parameter.get("ActorEnd").get(), false, linkQuery);
                desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR,
                    (String)parameter.get("Name").get(),
                    null,
                    actorQuery,
                    null,"");
                input.add(desc);

            }
            desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR_PROPERTY,
                    (String)parameter.get("Name").get(),
                    (String)parameter.get("Mode").get(),
                    null,
                    (String)parameter.get("DestinationProperty").get(),"");
                if((Boolean)parameter.get("DestinationAppendGraphID").get()){
                    desc.setAppendGraphID(true);
                }
            output.add(desc);
        }
    }

    public AggregateLinks prototype(){
        return new AggregateLinks();
    }
}
