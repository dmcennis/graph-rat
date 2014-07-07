/*

 * OptimizedBetweeness.java

 *

 * Created on 22 October 2007, 16:27

 *

 * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt)

 */

package nz.ac.waikato.mcennis.rat.graph.algorithm.prestige;



import java.util.logging.Level;

import java.util.logging.Logger;

import nz.ac.waikato.mcennis.rat.graph.Graph;
import nz.ac.waikato.mcennis.rat.graph.algorithm.Algorithm;
import nz.ac.waikato.mcennis.rat.graph.algorithm.AlgorithmMacros;
import nz.ac.waikato.mcennis.rat.graph.algorithm.OptimizedPathBase;

import nz.ac.waikato.mcennis.rat.graph.descriptors.IODescriptorFactory;
import nz.ac.waikato.mcennis.rat.graph.descriptors.IODescriptorInternal;
import org.dynamicfactory.descriptors.*;
import nz.ac.waikato.mcennis.rat.graph.descriptors.IODescriptor.Type;

import nz.ac.waikato.mcennis.rat.graph.path.PathNode;
import org.dynamicfactory.property.InvalidObjectTypeException;
import org.dynamicfactory.property.Property;
import org.dynamicfactory.property.PropertyFactory;


/**

 *


 * 

 * Class built upon OptimizedPathBase that claculates Betweeness.  OptimizedPathBase

 * only records one geodesic path for each actor pair, otherwise same as the 

 * betweeness metric deefined in Freeman79.

 * <br>

 * <br>

 * Freeman, L. "Centrality in social networks: I. Conceptual clarification."

 * <i>Social Networks</i>. 1:215--239.

 * @author Daniel McEnnis
 */

public class Betweeness extends OptimizedPathBase implements Algorithm {



    double[] betweeness = null;

    double maxBetweeness = 0.0;



    /** Creates a new instance of OptimizedBetweeness */

    public Betweeness() {
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



    protected void doCleanup(PathNode[] path, Graph g) {

        try {

            if (((Boolean) (super.getParameter()).get("Normalize").get()).booleanValue()) {

                Logger.getLogger(Betweeness.class.getName()).log(Level.INFO, "Normalizing Betweeness values");

                double norm = 0.0;

                for (int i = 0; i < betweeness.length; ++i) {

                    norm += betweeness[i] * betweeness[i];

                }

                for (int i = 0; i < betweeness.length; ++i) {

                    betweeness[i] /= Math.sqrt(norm);

                }

            }



            for (int i = 0; i < betweeness.length; ++i) {
                Property prop = PropertyFactory.newInstance().create("BasicProperty",AlgorithmMacros.getDestID(super.parameter, g, (String)parameter.get("DestinationProperty").get()),Double.class);

                prop.add(new Double(betweeness[i]));

                path[i].getActor().add(prop);

                if (betweeness[i] > maxBetweeness) {

                    maxBetweeness = betweeness[i];

                }

            }

            calculateGraphBetweeness(g);

            calculateBetweenessSD(g);

        } catch (InvalidObjectTypeException ex) {

            Logger.getLogger(Betweeness.class.getName()).log(Level.SEVERE, "Property class of Betweeness does not match java.lang.Double", ex);

        }

    }



    protected void doAnalysis(PathNode[] path, PathNode source) {

        for (int i = 0; i < path.length; ++i) {

            PathNode traversal = path[i].getPrevious();

            while ((traversal != null) && (traversal.compareTo(source) != 0)) {

                betweeness[traversal.getId()] += 1.0;

                traversal = traversal.getPrevious();

            }

        }

    }



    protected void setSize(int size) {

        betweeness = new double[size];

        java.util.Arrays.fill(betweeness, 0.0);

    }



    protected void calculateGraphBetweeness(Graph g) {

        try {

            double between = 0.0;

            for (int i = 0; i < betweeness.length; ++i) {

                between += maxBetweeness - betweeness[i];

            }

            between *= 2;

            between /= (betweeness.length - 1) * (betweeness.length - 1) * (betweeness.length - 2);

            Property prop = PropertyFactory.newInstance().create("BasicProperty",AlgorithmMacros.getDestID(super.parameter, g, (String)parameter.get("DestinationProperty").get()+" Mean"),Double.class);

            prop.add(new Double(between));

            g.add(prop);

        } catch (InvalidObjectTypeException ex) {

            Logger.getLogger(Betweeness.class.getName()).log(Level.SEVERE, "Property class of Betweeness does not match java.lang.Double", ex);

        }

    }



    protected void calculateBetweenessSD(Graph g) {

        try {

            double squareSum = 0.0;

            double sum = 0.0;

            double sd = 0.0;

            for (int i = 0; i < betweeness.length; ++i) {

                squareSum += betweeness[i] * betweeness[i];

                sum += betweeness[i];

            }

            sd = betweeness.length * squareSum - sum * sum;

            sd /= betweeness.length;

            Property prop = PropertyFactory.newInstance().create("BasicProperty",AlgorithmMacros.getDestID(super.parameter, g, (String)parameter.get("DestinationProperty").get()+" Standard Deviation"),Double.class);

            prop.add(new Double(sd));

            g.add(prop);

        } catch (InvalidObjectTypeException ex) {

            Logger.getLogger(Betweeness.class.getName()).log(Level.SEVERE, "Property class of BetweenessSD does not match java.lang.Double", ex);

        }



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
                    (String)parameter.get("DestinationProperty").get(),
                    "",
                    (Boolean)parameter.get("DestinationAppendGraphID").get());
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.GRAPH_PROPERTY,
                    (String)parameter.get("Name").get(),
                    null,
                    null,
                    (String)parameter.get("DestinationProperty").get()+" Mean",
                    "",
                    (Boolean)parameter.get("DestinationAppendGraphID").get());
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.GRAPH_PROPERTY,
                    (String)parameter.get("Name").get(),
                    null,
                    null,
                    (String)parameter.get("DestinationProperty").get()+" Standard Deviation",
                    "",
                    (Boolean)parameter.get("DestinationAppendGraphID").get());
            output.add(desc);

        }
    }

    @Override
    public Betweeness prototype() {
        return new Betweeness();
    }

}

