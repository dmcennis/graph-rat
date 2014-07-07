/**
 * CrossValidationOfLinks
 * Created Dec 11, 2008 - 1:12:04 PM
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
package org.mcennis.graphrat.algorithm.evaluation;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mcennis.graphrat.graph.Graph;
import org.mcennis.graphrat.algorithm.Algorithm;
import org.mcennis.graphrat.algorithm.AlgorithmMacros;
import org.dynamicfactory.model.ModelShell;
import org.mcennis.graphrat.descriptors.IODescriptorFactory;
import org.mcennis.graphrat.descriptors.IODescriptor;
import org.mcennis.graphrat.descriptors.IODescriptor.Type;
import org.mcennis.graphrat.link.Link;
import org.mcennis.graphrat.query.LinkQuery;
import org.mcennis.graphrat.query.LinkQueryFactory;
import org.mcennis.graphrat.query.link.LinkByRelation;
import org.dynamicfactory.descriptors.*;
import org.dynamicfactory.property.Property;
import org.dynamicfactory.property.PropertyFactory;
import org.dynamicfactory.property.InvalidObjectTypeException;

/**
 * Creates cross-validation splits across a link mode. 
 * 
 * @author Daniel McEnnis
 */
public class CrossValidationByLinks extends ModelShell implements Algorithm {

    PropertiesInternal parameter = PropertiesFactory.newInstance().create();
    LinkedList<IODescriptor> input = new LinkedList<IODescriptor>();
    LinkedList<IODescriptor> output = new LinkedList<IODescriptor>();

    public CrossValidationByLinks(){
        ParameterInternal name = ParameterFactory.newInstance().create("AlgorithmClass",String.class);
        SyntaxObject syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("Cross Validation By Link");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Name",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,Integer.MAX_VALUE,null,String.class);
        name.setRestrictions(syntax);
        name.add("Cross Validation By Link");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Category",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("Evaluation");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("LinkFilter",LinkQuery.class);
        syntax = SyntaxCheckerFactory.newInstance().create(0,1,null,LinkQuery.class);
        name.setRestrictions(syntax);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("DestinationProperty",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("Evaluation");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("DestinationAppendGraphID",Boolean.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,Boolean.class);
        name.setRestrictions(syntax);
        name.add(false);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Relation",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("tag");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("NumberOfFolds",Integer.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,Integer.class);
        name.setRestrictions(syntax);
        name.add("tag");
        parameter.add(name);
    }

    public void execute(Graph g) {
        LinkByRelation relation = (LinkByRelation)LinkQueryFactory.newInstance().create("LinkByRelation");
        relation.buildQuery((String)parameter.get("Relation").get(),false);
        Iterator<Link> original = AlgorithmMacros.filterLink(parameter, g, relation, null,null, null);
        if(original.hasNext()){
            int split = (Integer)parameter.get("NumberOfFolds").get();
            while(original.hasNext()){
                int assignment = (int) Math.floor(split*Math.random());
                Property linkAssignment = PropertyFactory.newInstance().create("BasicProperty",AlgorithmMacros.getDestID(parameter, g,(String)parameter.get("DestinationProperty").get()),Integer.class);
                try {
                    linkAssignment.add(assignment);
                } catch (InvalidObjectTypeException ex) {
                    Logger.getLogger(CrossValidationByLinks.class.getName()).log(Level.SEVERE, "Expected Integer, but was not compatible:"+ex.getMessage(), ex);
                }
                original.next().add(linkAssignment);
            }
        }else{
            Logger.getLogger(CrossValidationByLinks.class.getName()).log(Level.WARNING, "No links of relation "+(String)parameter.get("Relation").get()+" found");
        }
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

    public CrossValidationByLinks prototype(){
        return new CrossValidationByLinks();
    }
}
