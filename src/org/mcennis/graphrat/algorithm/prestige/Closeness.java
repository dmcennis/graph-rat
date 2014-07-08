/*

 * OptimizedCloseness.java

 *

 * Created on 22 October 2007, 15:13

 *

 * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt)

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


package org.mcennis.graphrat.algorithm.prestige;



import java.util.logging.Level;

import java.util.logging.Logger;

import org.mcennis.graphrat.algorithm.Algorithm;
import org.mcennis.graphrat.algorithm.AlgorithmMacros;
import org.mcennis.graphrat.algorithm.OptimizedPathBase;
import org.mcennis.graphrat.graph.Graph;

import org.mcennis.graphrat.descriptors.IODescriptor.Type;
import org.mcennis.graphrat.descriptors.IODescriptorFactory;
import org.mcennis.graphrat.descriptors.IODescriptorInternal;
import org.dynamicfactory.descriptors.*;
import org.dynamicfactory.property.Property;
import org.dynamicfactory.property.PropertyFactory;
import org.dynamicfactory.property.InvalidObjectTypeException;

import org.mcennis.graphrat.path.PathNode;


/**

 *


 * 

 * Class that calculates Closeness in O(n) space using OptimizedPathBase.  This 

 * implements Closeness as described in Freeman79.

 * <br>

 * <br>

 * Freeman, L. "Centrality in social networks: I. Conceptual clarification."

 * <i>Social Networks</i>. 1:215--239.

 * @author Daniel McEnnis
 */

public class Closeness extends OptimizedPathBase implements Algorithm {

    

    double[] prestigeValue = null;

    double[] centralityValue = null;

    double maxPrestige = 0.0;

    double maxCentrality = 0.0;

    

    /** Creates a new instance of OptimizedCloseness */

    public Closeness() {

        super();

        ParameterInternal name = ParameterFactory.newInstance().create("AlgorithmClass", String.class);
        SyntaxObject syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("Betweeness");
        super.parameter.add(name);

        name = ParameterFactory.newInstance().create("Name", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, Integer.MAX_VALUE, null, String.class);
        name.setRestrictions(syntax);
        name.add("Betweeness");
        super.parameter.add(name);

        name = ParameterFactory.newInstance().create("Category", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("Prestige");
        super.parameter.add(name);

        name = ParameterFactory.newInstance().create("DestinationProperty", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("Property");
        super.parameter.add(name);

        name = ParameterFactory.newInstance().create("DestinationAppendGraphID", Boolean.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, Boolean.class);
        name.setRestrictions(syntax);
        name.add(false);
        super.parameter.add(name);
    }

    

    protected void setSize(int size){

        prestigeValue = new double[size];

        centralityValue = new double[size];

    }



    protected void doAnalysis(PathNode[] path,PathNode source) {

        for(int i=0;i<path.length;++i){

            double cost = path[i].getCost();

            if(Double.isInfinite(cost)){

                cost = 0.0;

            }

            prestigeValue[path[i].getId()] += cost;

            centralityValue[source.getId()] += cost;

        }

    }

    

    protected void doCleanup(PathNode path[], Graph g){

        try{

        for(int i=0;i<prestigeValue.length;++i){

            if(prestigeValue[i]!=0){

                prestigeValue[i] = 1/prestigeValue[i];

                if(prestigeValue[i]>maxPrestige){

                    maxPrestige = prestigeValue[i];

                }

            }else{

                prestigeValue[i]=0.0;

            }

            if(centralityValue[i]!=0){

                centralityValue[i] = 1/centralityValue[i];

                if(centralityValue[i]>maxCentrality){

                    maxCentrality = centralityValue[i];

                }

            }else{

                centralityValue[i] = 0.0;

            }

            

            if(maxPrestige < prestigeValue[i]){

                maxPrestige = prestigeValue[i];

            }

            if(maxCentrality < centralityValue[i]){

                maxCentrality = centralityValue[i];

            }

        }

        // Normalize if necessary

        if(((Boolean)(super.getParameter()).get("Normalize").get()).booleanValue()){

            double norm = 0.0;

            for(int i=0;i<centralityValue.length;++i){

                norm += centralityValue[i]*centralityValue[i];

            }

            for(int i=0;i<centralityValue.length;++i){

                centralityValue[i] /= Math.sqrt(norm);

            }

        }

        

        if(((Boolean)(super.getParameter()).get("Normalize").get()).booleanValue()){

            double norm = 0.0;

            for(int i=0;i<prestigeValue.length;++i){

                norm += prestigeValue[i]*prestigeValue[i];

            }

            for(int i=0;i<prestigeValue.length;++i){

                prestigeValue[i] /= Math.sqrt(norm);

            }

        }

        

        //create and set the properties defined

        for(int i=0;i<prestigeValue.length;++i){
            Property prop = PropertyFactory.newInstance().create("BasicProperty",AlgorithmMacros.getDestID(super.parameter, g, (String)parameter.get("DestinationProperty").get()+" Centrality"),Double.class);

            if(centralityValue[i]!=0.0){

                prop.add(new Double(centralityValue[i]));

            }else{

                prop.add(new Double(Double.NaN));

            }

            path[i].getActor().add(prop);

            

            prop = PropertyFactory.newInstance().create("BasicProperty",AlgorithmMacros.getDestID(super.parameter, g, (String)parameter.get("DestinationProperty").get()+" Prestige"),Double.class);

            if(prestigeValue[i] != 0.0){

                prop.add(new Double(prestigeValue[i]));

            }else{

                prop.add(new Double(Double.NaN));

            }

            path[i].getActor().add(prop);

        }

        calculateGraphCentrality(g);

        calculateGraphCentralitySD(g);

        calculateGraphPrestige(g);

        calculateGraphPrestigeSD(g);

       } catch (InvalidObjectTypeException ex) {

            Logger.getLogger(Closeness.class.getName()).log(Level.SEVERE, "Property class does not match java.lang.Double", ex);

        }

    }

    protected void calculateGraphCentrality(Graph g) throws InvalidObjectTypeException{

        double centrality = 0.0;

        for(int i=0;i<centralityValue.length;++i){

            centrality += maxCentrality - centralityValue[i];

        }

        centrality *= 2;

        centrality /= (centralityValue.length-1)*(centralityValue.length-1)*(centralityValue.length-2);
            Property prop = PropertyFactory.newInstance().create("BasicProperty",AlgorithmMacros.getDestID(super.parameter, g, (String)parameter.get("DestinationProperty").get()+" Centrality Mean"),Double.class);

        prop.add(new Double(centrality));

        g.add(prop);

    }

    

    protected void calculateGraphPrestige(Graph g) throws InvalidObjectTypeException{

        double prestige = 0.0;

        for(int i=0;i<prestigeValue.length;++i){

            prestige += maxPrestige - prestigeValue[i];

        }

        prestige *= 2;

        prestige /= (prestigeValue.length-1)*(prestigeValue.length-1)*(prestigeValue.length-2);

            Property prop = PropertyFactory.newInstance().create("BasicProperty",AlgorithmMacros.getDestID(super.parameter, g, (String)parameter.get("DestinationProperty").get()+" Prestige Mean"),Double.class);

        prop.add(new Double(prestige));

        g.add(prop);

    }

    

    protected void calculateGraphCentralitySD(Graph g) throws InvalidObjectTypeException{

        double sd = 0.0;

        double centralitySquare = 0.0;

        double centralitySum = 0.0;

        for(int i=0;i<centralityValue.length;++i){

            centralitySquare += centralityValue[i]*centralityValue[i];

            centralitySum += centralityValue[i];

        }

        sd = centralityValue.length*centralitySquare-centralitySum*centralitySum;

        sd /= centralityValue.length;
            Property prop = PropertyFactory.newInstance().create("BasicProperty", AlgorithmMacros.getDestID(super.parameter, g, (String) parameter.get("DestinationProperty").get() + " Centrality Standard Deviation"),Double.class);

        prop.add(new Double(sd));

        g.add(prop);

    }

    

    protected void calculateGraphPrestigeSD(Graph g) throws InvalidObjectTypeException{

        double sd = 0.0;

        double prestigeSquare = 0.0;

        double prestigeSum = 0.0;

        for(int i=0;i<centralityValue.length;++i){

            prestigeSquare += prestigeValue[i]*prestigeValue[i];

            prestigeSum += prestigeValue[i];

        }

        double squareSum = prestigeSum*prestigeSum;

        sd = (prestigeValue.length*prestigeSquare)-squareSum;

        sd /= prestigeValue.length;
            Property prop = PropertyFactory.newInstance().create("BasicProperty",AlgorithmMacros.getDestID(super.parameter, g, (String)parameter.get("DestinationProperty").get()+" Prestige Standard Deviation"),Double.class);
        prop.add(new Double(sd));

        g.add(prop);

    }

    

    @Override
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
                    Type.ACTOR_PROPERTY,
                    (String)parameter.get("Name").get(),
                    (String)parameter.get("Mode").get(),
                    null,
                    (String)parameter.get("DestinationProperty").get()+" Prestige",
                    "",
                    (Boolean)parameter.get("DestinationAppendGraphID").get());
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.GRAPH_PROPERTY,
                    (String)parameter.get("Name").get(),
                    null,
                    null,
                    (String)parameter.get("DestinationProperty").get()+" Prestige Mean",
                    "",
                    (Boolean)parameter.get("DestinationAppendGraphID").get());
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.GRAPH_PROPERTY,
                    (String)parameter.get("Name").get(),
                    null,
                    null,
                    (String)parameter.get("DestinationProperty").get()+" Prestige Standard Deviation",
                    "",
                    (Boolean)parameter.get("DestinationAppendGraphID").get());
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR_PROPERTY,
                    (String)parameter.get("Name").get(),
                    (String)parameter.get("Mode").get(),
                    null,
                    (String)parameter.get("DestinationProperty").get()+" Centrality",
                    "",
                    (Boolean)parameter.get("DestinationAppendGraphID").get());
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.GRAPH_PROPERTY,
                    (String)parameter.get("Name").get(),
                    null,
                    null,
                    (String)parameter.get("DestinationProperty").get()+" Centrality Mean ",
                    "",
                    (Boolean)parameter.get("DestinationAppendGraphID").get());
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.GRAPH_PROPERTY,
                    (String)parameter.get("Name").get(),
                    null,
                    null,
                    (String)parameter.get("DestinationProperty").get()+" Centrality Standard Deviation",
                    "",
                    (Boolean)parameter.get("DestinationAppendGraphID").get());
            output.add(desc);

        }
    }

    @Override
    public Closeness prototype() {
        return new Closeness();
    }

    

}

