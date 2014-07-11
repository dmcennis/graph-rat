/*
 * WekaClassifierMultiAttribute - created 7/02/2009 - 11:46:52 PM
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
package org.mcennis.graphrat.algorithm.machinelearning;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.dynamicfactory.descriptors.Properties;
import org.mcennis.graphrat.graph.Graph;
import org.mcennis.graphrat.actor.Actor;
import org.mcennis.graphrat.algorithm.Algorithm;
import org.mcennis.graphrat.algorithm.AlgorithmMacros;
import org.mcennis.graphrat.descriptors.IODescriptor;
import org.mcennis.graphrat.descriptors.IODescriptor.Type;
import org.mcennis.graphrat.descriptors.IODescriptorFactory;
import org.mcennis.graphrat.link.Link;
import org.dynamicfactory.model.ModelShell;
import org.mcennis.graphrat.query.ActorQuery;
import org.mcennis.graphrat.query.ActorQueryFactory;
import org.mcennis.graphrat.query.LinkQuery;
import org.mcennis.graphrat.query.LinkQuery.LinkEnd;
import org.mcennis.graphrat.query.LinkQueryFactory;
import org.mcennis.graphrat.query.actor.ActorByMode;
import org.mcennis.graphrat.query.link.LinkByRelation;
import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import org.dynamicfactory.descriptors.*;
import org.dynamicfactory.property.Property;
import org.dynamicfactory.property.PropertyFactory;

/**
 *
 * @author Daniel McEnnis
 */
public class BuildClassifierSingleAttribute extends ModelShell implements Algorithm {

    PropertiesInternal parameter = PropertiesFactory.newInstance().create();
    LinkedList<IODescriptor> input = new LinkedList<IODescriptor>();
    LinkedList<IODescriptor> output = new LinkedList<IODescriptor>();

    public BuildClassifierSingleAttribute() {
        ParameterInternal name = ParameterFactory.newInstance().create("AlgorithmClass", String.class);
        SyntaxObject syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("Weka Classifier Multi-Attribute");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Name", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, Integer.MAX_VALUE, null, String.class);
        name.setRestrictions(syntax);
        name.add("Weka Classifier Multi-Attribute");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Category", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("Machine Learning");
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

        name = ParameterFactory.newInstance().create("GroundMode", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("tag");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("TargetMode", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("tag");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Relation", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("tag");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("SourceProperty", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("Property");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("ClassifierProperty", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("Property");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Classifer", Class.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, Class.class);
        name.setRestrictions(syntax);
        name.add(weka.classifiers.meta.AdaBoostM1.class);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("LinkEnd", LinkEnd.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, LinkEnd.class);
        name.setRestrictions(syntax);
        name.add(LinkEnd.SOURCE);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Options", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("AbsenceMeansMissing", Boolean.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, Boolean.class);
        name.setRestrictions(syntax);
        name.add(true);
        parameter.add(name);
    }

    public void execute(Graph g) {
        // construct the queries to be used

        ActorByMode groundMode = (ActorByMode) ActorQueryFactory.newInstance().create("ActorByMode");
        groundMode.buildQuery((String) parameter.get("GroundMode").get(),".*",false);

        ActorByMode targetMode = (ActorByMode) ActorQueryFactory.newInstance().create("ActorByMode");
        targetMode.buildQuery((String) parameter.get("TargetMode").get(),".*",false);

        LinkByRelation groundTruth = (LinkByRelation) LinkQueryFactory.newInstance().create("LinkByRelation");
        groundTruth.buildQuery((String) parameter.get("Relation").get(), false);

        // build a list of new artists
        TreeSet<Actor> artists = new TreeSet<Actor>();
        artists.addAll(AlgorithmMacros.filterActor(parameter, g, targetMode.execute(g, artists, null)));

        // collect the instance variables from the properties to be the 

            Classifier classifier = createClassifier();
            Iterator<Actor> users = AlgorithmMacros.filterActor(parameter, g, groundMode, null, null);
            Instances dataSet = null;
            boolean firstEntry = true;
            while (users.hasNext()) {
                TreeSet<Actor> user = new TreeSet<Actor>();
                user.add(users.next());
                Property property = user.first().getProperty(AlgorithmMacros.getSourceID(parameter, g, (String) parameter.get("SourceProperty").get()));
                if (property.getPropertyClass().getName().contentEquals(Instance.class.getName())) {
                    List values = property.getValue();
                    if (!values.isEmpty()) {
                        // get the existing instance
                        Instance object = (Instance) values.get(0);
                        if(firstEntry){
                            firstEntry = false;
                            Instances current = object.dataset();
                            FastVector attributes = new FastVector();
                            for(int j=0;j<current.numAttributes();++j){
                                attributes.addElement(current.attribute(j));
                            }
                            FastVector targetNames = new FastVector();
                            Iterator<Actor> artistIt = targetMode.executeIterator(g, null, null);
                            while(artistIt.hasNext()){
                                targetNames.addElement(artistIt.next().getID());
                            }
                            Attribute classValue = new Attribute("TargetID",targetNames);
                            attributes.addElement(classValue);
                            dataSet = new Instances("Training",attributes,1000);
                            dataSet.setClassIndex(dataSet.numAttributes()-1);
                        }

                        // for every artist, create a temporary artist classifer
                        double[] content = new double[object.numAttributes()+1];
                        for(int j=0;j<object.numAttributes()+1;++j){
                            content[j] = object.value(j);
                        }

                        Iterator<Link> link = null;
                        if((LinkEnd)parameter.get("LinkEnd").get()==LinkEnd.SOURCE){
                            link = AlgorithmMacros.filterLink(parameter, g, groundTruth, user, null, null);
                        }else{
                            link = AlgorithmMacros.filterLink(parameter, g, groundTruth, null, user, null);
                        }
                        if(link.hasNext()){
                                double strength = Double.NEGATIVE_INFINITY;
                                Actor target = null;
                                while(link.hasNext()){
                                    Link l =link.next();
                                    if(l.getStrength()>strength){
                                        strength = l.getStrength();
                                        if((LinkEnd)parameter.get("LinkEnd").get()==LinkEnd.SOURCE){
                                            target = l.getDestination();
                                        }else{
                                            target = l.getSource();
                                        }
                                    }
                                }
                            content[content.length-1]=dataSet.attribute(dataSet.numAttributes()-1).indexOfValue(target.getID());
                        }else{
                            content[content.length-1]=Double.NaN;
                        }
                        Instance base = new Instance(1.0,content);
                        base.setDataset(dataSet);
                        dataSet.add(base);
                    }
                }
            }
            try {
                classifier.buildClassifier(dataSet);
                Property classifierProperty = PropertyFactory.newInstance().create(
                        "BasicProperty",
                        AlgorithmMacros.getDestID(parameter, g, (String)parameter.get("ClassifierProperty").get()),weka.classifiers.Classifier.class);
                classifierProperty.add(classifier);
                g.add(classifierProperty);

                Property instancesProperty = PropertyFactory.newInstance().create(
                        "BasicProperty",
                        AlgorithmMacros.getDestID(parameter, g, (String)parameter.get("InstancesProperty").get()),weka.core.Instances.class);
                instancesProperty.add(classifier);
                g.add(instancesProperty);
            } catch (Exception ex) {
                Logger.getLogger(BuildClassifierSingleAttribute.class.getName()).log(Level.SEVERE, null, ex);
            }
    }

    public BuildClassifierSingleAttribute prototype() {
        return new BuildClassifierSingleAttribute();
    }

    public List<IODescriptor> getInputType() {
        return input;
    }

    public List<IODescriptor> getOutputType() {
        return output;
    }

    public Properties getParameter() {
        return parameter;
    }

    public Parameter getParameter(String param) {
        return parameter.get(param);
    }

    public void init(Properties map) {
        if(parameter.check(map)){
            parameter.merge(map);
            
            IODescriptor desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR, 
                    (String)parameter.get("Name").get(), 
                    (String)parameter.get("GroundMode").get(), 
                    null, 
                    null, 
                    "", 
                    false);
            input.add(desc);
            
            desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR, 
                    (String)parameter.get("Name").get(), 
                    (String)parameter.get("TargetMode").get(), 
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
                    Type.ACTOR_PROPERTY,
                    (String)parameter.get("Name").get(),
                    (String)parameter.get("GroundMode").get(),
                    null,
                    (String)parameter.get("SourceProperty").get(),
                    "",
                    (Boolean)parameter.get("SourceAppendGraphID").get());
            input.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.GRAPH_PROPERTY,
                    (String)parameter.get("Name").get(),
                    (String)parameter.get("TargetMode").get(),
                    null,
                    (String)parameter.get("ClassifierProperty").get(),
                    "",
                    (Boolean)parameter.get("DestinationAppendGraphID").get());
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.GRAPH_PROPERTY,
                    (String)parameter.get("Name").get(),
                    (String)parameter.get("TargetMode").get(),
                    null,
                    (String)parameter.get("InstancesProperty").get(),
                    "",
                    (Boolean)parameter.get("DestinationAppendGraphID").get());
            output.add(desc);
        }
    }

    private Classifier createClassifier() {
        try {
            Classifier ret = (Classifier) ((Class)parameter.get("Classifier").get()).newInstance();
            String[] options = ((String) parameter.get("Options").get()).split("\\s");
            ret.setOptions(options);
            return ret;
        } catch (InstantiationException ex) {
            Logger.getLogger(BuildClassifierSingleAttribute.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(BuildClassifierSingleAttribute.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(BuildClassifierSingleAttribute.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new weka.classifiers.meta.AdaBoostM1();
    }
}
