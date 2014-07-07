/*
 * Created 30/05/2008-14:58:51
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.graph.algorithm;

import cern.colt.matrix.DoubleFactory2D;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.doublealgo.Statistic;
import cern.colt.matrix.linalg.EigenvalueDecomposition;
import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.mcennis.graphrat.algorithm.Algorithm;
import org.mcennis.graphrat.graph.Graph;
import org.mcennis.graphrat.actor.Actor;
import org.dynamicfactory.descriptors.DescriptorFactory;
import org.dynamicfactory.descriptors.InputDescriptor;
import org.dynamicfactory.descriptors.OutputDescriptor;
import org.dynamicfactory.descriptors.SettableParameter;
import org.dynamicfactory.model.ModelShell;
import org.mcennis.graphrat.util.Duples;

/**
 * Performs principal component analysis on a colt 2D matrix describing a distance matrix. Calculates a matrix
 * for transforming a distance vector into a (usually lower) space where the basis
 * vectors describing this space are oriented for maximum variance in the original
 * distance matrix. Also calculates a set of new vectors in this new space for each actor
 * in the matrix. These vecors are also stored as a 2D matrix on the graph object.
 * Any one of these three outputs can be turned off. 
 * 
 * @author Daniel McEnnis
 */
public class PrincipalComponentsAnalysis extends ModelShell implements Algorithm {

    ParameterInternal[] parameter = new ParameterInternal[9];
    InputDescriptor[] input = new InputDescriptor[1];
    OutputDescriptor[] output = new OutputDescriptor[3];

    @Override
    public void execute(Graph g) {
        Property distanceProperty = g.getProperty((String) parameter[1].getValue());
        if (distanceProperty != null) {
            Object[] value = distanceProperty.getValue();
            if ((value != null) && (value.length > 0)) {
                DoubleMatrix2D matrix = (DoubleMatrix2D) value[0];
                DoubleMatrix2D covariance = Statistic.covariance(matrix);
                EigenvalueDecomposition decomp = new EigenvalueDecomposition(covariance);
                covariance = null;
                DoubleMatrix1D values = decomp.getRealEigenvalues();
                double totalEnergy = 0.0;
                for (int i = 0; i < values.size(); ++i) {
                    if (values.get(i) > 0) {
                        totalEnergy += values.get(i);
                    }
                }
                DoubleMatrix2D eigenMatrix = decomp.getV();

                // get indeci for sorting the columns by descing eigenvalue
                Vector<Duples<Double, Integer>> valueVector = new Vector<Duples<Double, Integer>>();
                for (int i = 0; i < values.size(); ++i) {
                    Duples duple = new Duples();
                    duple.setLeft(Math.max(values.get(i), 0.0));
                    duple.setRight(i);
                    valueVector.add(duple);
                }
                Collections.sort(valueVector);
                double foundEnergy = 0.0;
                int count = 0;
                for (int i = valueVector.size() - 1; i >= 0; --i) {
                    foundEnergy += valueVector.get(i).getLeft();
                    count++;
                    if ((foundEnergy / totalEnergy) > (Double) parameter[2].getValue()) {
                        break;
                    }
                }
                int[] columns = new int[count];
                count = 0;
                for (int i = valueVector.size() - 1; i >= 0; --i) {
                    columns[count++] = valueVector.get(i).getRight();
                }
                int[] rows = new int[eigenMatrix.rows()];
                for (int i = 0; i < rows.length; ++i) {
                    rows[i] = i;
                }

                valueVector = null;
                DoubleMatrix2D result = eigenMatrix.viewSelection(rows, columns);
                DoubleMatrix2D actorMatrix = DoubleFactory2D.dense.make(matrix.rows(), result.columns());
                matrix.zMult(result, actorMatrix);

                // save transposition matrici
                if ((Boolean) parameter[3].getValue()) {
                    Properties props = new Properties();
                    props.setProperty("PropertyClass", "cern.colt.matrix.DoubleMatrix2D");
                    props.setProperty("PropertyType", "Basic");
                    props.setProperty("PropertyID", (String) parameter[4].getValue());
                    Property transposition = PropertyFactory.newInstance().create(props);
                    try {
                        transposition.add(result);
                        g.add(transposition);
                    } catch (InvalidObjectTypeException ex) {
                        Logger.getLogger(PrincipalComponentsAnalysis.class.getName()).log(Level.SEVERE, "actual class DoubleMatrix2D does not match property class '"+transposition.getPropertyClass().getName()+"'", ex);
                    }
                }

                // calculate actor-vector matrici
                if ((Boolean) parameter[5].getValue()) {
                    Properties props = new Properties();
                    props.setProperty("PropertyClass", "cern.colt.matrix.DoubleMatrix1D");
                    props.setProperty("PropertyType", "Basic");
                    props.setProperty("PropertyID", (String) parameter[6].getValue());
                    Actor[] actors = g.getActor((String) parameter[2].getValue());
                    if (actors != null) {
                        Arrays.sort(actors);
                        for (int i = 0; i < actors.length; ++i) {


                            Property actorMatrixProperty = PropertyFactory.newInstance().create(props);
                            try {
                                actorMatrixProperty.add(actorMatrix.viewRow(i));
                                actors[i].add(actorMatrixProperty);
                            } catch (InvalidObjectTypeException ex) {
                                Logger.getLogger(PrincipalComponentsAnalysis.class.getName()).log(Level.SEVERE, "actual class DoubleMatrix1D does not match property class '"+actorMatrixProperty.getPropertyClass().getName()+"'", ex);
                            }
                        }
                    }
                }

                if ((Boolean) parameter[7].getValue()) {
                    Properties props = new Properties();
                    props.setProperty("PropertyClass", "cern.colt.matrix.DoubleMatrix2D");
                    props.setProperty("PropertyType", "Basic");
                    props.setProperty("PropertyID", (String) parameter[8].getValue());
                    Property actorMatrixProperty = PropertyFactory.newInstance().create(props);
                    try {
                        actorMatrixProperty.add(actorMatrix);
                        g.add(actorMatrixProperty);
                    } catch (InvalidObjectTypeException ex) {
                        Logger.getLogger(PrincipalComponentsAnalysis.class.getName()).log(Level.SEVERE, "actual class DoubleMatrix2D does not match property class '"+actorMatrixProperty.getPropertyClass().getName()+"'", ex);
                    }
                }
            }
        }
    }

    @Override
    public InputDescriptor[] getInputType() {
        return input;
    }

    @Override
    public OutputDescriptor[] getOutputType() {
        return output;
    }

    @Override
    public Parameter[] getParameter() {
        return parameter;
    }

    @Override
    public Parameter getParameter(String param) {
        for (int i = 0; i < parameter.length; ++i) {
            if (parameter[i].getName().contentEquals(param)) {
                return parameter[i];
            }
        }
        return null;
    }

    @Override
    public SettableParameter[] getSettableParameter() {
        return null;
    }

    @Override
    public SettableParameter getSettableParameter(String param) {
        return null;
    }

    /**
     * Parameters:<br/>
     * <br/>
     * <ul>
     * <li/><b>name</b>: Name of this algorithm. Deafult is 'Principal Components Analysis'
     * <li/><b>distanceMatrix</b>: name of the property containing the distance matrix.
     * Default is 'Actor Distance Matrix'
     * <li/><b>actorType</b>: mode of actor from which the distance matrix is derived.
     * Default is 'Artist'
     * <li/><b>storeTransformMatrix</b>: is the transform matrix to be stored. 
     * Default is 'true'
     * <li/><b>transformMatrix</b>: property name for the transform matrix. Default
     * is 'Actor Transform Matrix'
     * <li/><b>storeActorProperty</b>: are the new vectors to be stored on each actor
     * Default is 'false'
     * <li/><b>actorProperty</b>: property name for storing vector descriptions.
     * Default is 'Actor Distance Vector'
     * <li/><b>storeVectorMatrix</b>: is the matrix representing collectively all
     * actor's vectors to be stored. Default is 'false'
     * <li/><b>vectorMatrixProperty</b>: property name for storing the new actor 
     * matrix. Deafult is 'Actor Distance Matrix'
     * </ul>
     * @param map
     */
    public void init(Properties map) {
        Properties props = new Properties();
        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "name");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[0] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("name") != null)) {
            parameter[0].setValue(map.getProperty("name"));
        } else {
            parameter[0].setValue("Principal Components Analysis");
        }

        props = new Properties();
        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "distanceMatrix");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[1] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("distanceMatrix") != null)) {
            parameter[1].setValue(map.getProperty("distanceMatrix"));
        } else {
            parameter[1].setValue("Actor Distance Matrix");
        }

        props = new Properties();
        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "actorType");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[2] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("actorType") != null)) {
            parameter[2].setValue(map.getProperty("actorType"));
        } else {
            parameter[2].setValue("Artist");
        }

        props = new Properties();
        props.setProperty("Type", "java.lang.Boolean");
        props.setProperty("Name", "storeTransformMatrix");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[3] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("storeTransformMatrix") != null)) {
            parameter[3].setValue(new Boolean(Boolean.parseBoolean(map.getProperty("storeTransformMatrix"))));
        } else {
            parameter[3].setValue(new Boolean(true));
        }

        props = new Properties();
        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "transformMatrix");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[4] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("transformMatrix") != null)) {
            parameter[4].setValue(map.getProperty("transformMatrix"));
        } else {
            parameter[4].setValue("Actor Transform Matrix");
        }

        props = new Properties();
        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "storeActorProperty");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[5] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("makeActorProperty") != null)) {
            parameter[5].setValue(new Boolean(Boolean.parseBoolean(map.getProperty("makeActorProperty"))));
        } else {
            parameter[5].setValue(new Boolean(false));
        }

        props = new Properties();
        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "actorProperty");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[6] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("actorProperty") != null)) {
            parameter[6].setValue(map.getProperty("actorProperty"));
        } else {
            parameter[6].setValue("Actor Distance Vector");
        }

        props = new Properties();
        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "storeVectorMatrix");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[7] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("storeVectorMatrix") != null)) {
            parameter[7].setValue(new Boolean(Boolean.parseBoolean(map.getProperty("storeVectorMatrix"))));
        } else {
            parameter[7].setValue(new Boolean(false));
        }

        props = new Properties();
        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "vectorMatrixProperty");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[8] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("vectorMatrixProperty") != null)) {
            parameter[8].setValue(map.getProperty("vectorMatrixProperty"));
        } else {
            parameter[8].setValue("Actor Distance Matrix");
        }

        props.setProperty("Type", "GraphProperty");
        props.remove("Relation");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property", (String) parameter[1].getValue());
        input[0] = DescriptorFactory.newInstance().createInputDescriptor(props);

        props.setProperty("Type", "GraphProperty");
        props.remove("Relation");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property", (String) parameter[4].getValue());
        output[0] = DescriptorFactory.newInstance().createOutputDescriptor(props);

        props.setProperty("Type", "ActorProperty");
        props.setProperty("Relation", (String) parameter[1].getValue());
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property", (String) parameter[6].getValue());
        output[1] = DescriptorFactory.newInstance().createOutputDescriptor(props);

        props.setProperty("Type", "GraphProperty");
        props.setProperty("Relation", (String) parameter[1].getValue());
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property", (String) parameter[8].getValue());
        output[2] = DescriptorFactory.newInstance().createOutputDescriptor(props);

    }
}
