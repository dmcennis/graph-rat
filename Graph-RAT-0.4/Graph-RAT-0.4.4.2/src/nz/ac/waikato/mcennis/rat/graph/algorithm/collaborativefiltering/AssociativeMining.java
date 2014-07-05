/*
 * Created 3-2-08
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.graph.algorithm.collaborativefiltering;

import java.util.HashSet;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import nz.ac.waikato.mcennis.rat.graph.actor.Actor;
import nz.ac.waikato.mcennis.rat.graph.algorithm.Algorithm;
import org.dynamicfactory.descriptors.DescriptorFactory;
import org.dynamicfactory.descriptors.InputDescriptor;
import org.dynamicfactory.descriptors.OutputDescriptor;
import org.dynamicfactory.descriptors.Parameter;
import org.dynamicfactory.descriptors.ParameterInternal;
import org.dynamicfactory.descriptors.SettableParameter;
import nz.ac.waikato.mcennis.rat.graph.link.Link;
import nz.ac.waikato.mcennis.rat.graph.link.LinkFactory;
import nz.ac.waikato.mcennis.rat.graph.model.ModelShell;
import org.dynamicfactory.property.InvalidObjectTypeException;
import org.dynamicfactory.property.Property;
import org.dynamicfactory.property.PropertyFactory;
import nz.ac.waikato.mcennis.rat.scheduler.Scheduler;

/**
 * Class that performs collaborative filtering on a set of actors via their links
 * to another mode of actors.  The algorithm has two stages: the calculation of the
 * relationships between the target actors via whether or not there is a significant
 * change in the frequency of occurance in the destination given the source versus the 
 * occurances of the destination overall.  This is extended until the combination
 * of existing significant actors is no longer significant (up to the depth parameter
 * value). O(n2) for the first level, but worst case is O(n!) for infinite depth.
 * 
 * @author Daniel McEnnis
 */
public class AssociativeMining extends ModelShell implements Algorithm {

    ParameterInternal[] parameter = new ParameterInternal[11];
    InputDescriptor[] input = new InputDescriptor[3];
    OutputDescriptor[] output = new OutputDescriptor[2];

    public AssociativeMining(){
        init(null);
    }
    
    @Override
    public void execute(Graph g) {
        // create actor list
        Actor[] artists = g.getActor((String) parameter[3].getValue());
        // create user list
        Actor[] users = g.getActor((String) parameter[2].getValue());
        if ((artists != null) && (users != null)) {
            fireChange(Scheduler.SET_ALGORITHM_COUNT,artists.length);
            // for every artist, calculate all significant correlations
            for (int i = 0; i < artists.length; ++i) {
                Logger.getLogger(AssociativeMining.class.getName()).log(Level.FINE, "Tag '"+artists[i].getID()+"' - "+i+"/"+artists.length);
                // set up with first order correlations
                AssociativeMiningItemSetGroup group = new AssociativeMiningItemSetGroup(g,(String) parameter[1].getValue());
                for (int j = 0; j < artists.length; ++j) {
                    if (i != j) {
                        AssociativeMiningItems base = new AssociativeMiningItems(g, (String) parameter[1].getValue(), artists[j]);
                        base.setPositiveSignificance(((Double)parameter[8].getValue()).doubleValue());
                        base.setNegativeSignificance(((Double)parameter[9].getValue()).doubleValue());
                        int result = base.significanceTest(artists[i]);
                        if (result > 0) {
                            group.addPositiveBase(artists[j], base);
                        } else if (result < 0) {
                            base.setPositive(false);
                            group.addNegativeBase(artists[j], base);
                        }
                    }
                }

                int depth = 1;
                // set up the rest of the system
                while ((depth < ((Integer)parameter[10].getValue()).intValue())&&(group.expandSet(artists[i]) > 0)) {
                    depth++;
                }
                if (((Boolean) parameter[7].getValue()).booleanValue() == true) {
                    Properties props = new Properties();
                    props.setProperty("PropertyID", (String) parameter[5].getValue());
                    props.setProperty("PropertyClass", "nz.ac.waikato.mcennis.rat.graph.algorithm.collaborativefiltering.AssociativeMiningItems");
                    Property property = PropertyFactory.newInstance().create(props);
                    AssociativeMiningItems[] items = group.exportAssociations();
                    if (items.length > 0) {
                        try {
                            for (int j = 0; j < items.length; ++j) {
                                property.add(items[j]);
                            }
                            artists[i].add(property);
                        } catch (InvalidObjectTypeException ex) {
                            Logger.getLogger(AssociativeMining.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                AssociativeMiningItems[] set = group.exportAssociations();
                for(int u=0;u<users.length;++u){
                    double strength=0.0;
                    HashSet<Actor> given = getGiven(g,users[u]);
                    Link[] l = g.getLink((String)parameter[1].getValue(),users[u] , artists[i]);
                    if((l==null)||(((Boolean)parameter[6].getValue()).booleanValue()==true)){
                        for(int s=0;s<set.length;++s){
                            if(set[s].applies(given)){
                                strength += 1.0;
                            }
                        }
                    }
                    if(strength > 0.0){
                        Properties props = new Properties();
                        props.setProperty("LinkType",(String)parameter[4].getValue());
                        Link link = LinkFactory.newInstance().create(props);
                        link.set(users[u], strength, artists[i]);
                        g.add(link);
                    }
                }
                fireChange(Scheduler.SET_ALGORITHM_PROGRESS,i);
            }
        } else {
            if (artists == null) {
                Logger.getLogger(AssociativeMining.class.getName()).log(Level.WARNING,"No actors of mode '" + (String) parameter[3].getValue() + "' are found in graph '" + g.getID() + "'");
            }
            if (users == null) {
                Logger.getLogger(AssociativeMining.class.getName()).log(Level.WARNING,"No actors of mode '" + (String) parameter[2].getValue() + "' are found in graph '" + g.getID() + "'");
            }
        }
    }
    
    protected HashSet<Actor> getGiven(Graph g, Actor u){
        HashSet<Actor> ret = new HashSet<Actor>();
        Link[] links = g.getLinkBySource((String)parameter[1].getValue(), u);
        if(links != null){
            for(int i=0;i<links.length;++i){
                ret.add(links[i].getDestination());
            }
        }
        return ret;
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
     * <li/><b>name</b>: name of this algorithm. Deafult is 'Dispersion Based Collaborative Filtering'
     * <li/><b>relation</b>: relation between users and actors. Default is 'Given'
     * <li/><b>userActor</b>: mode of source actors. Default is 'User'
     * <li/><b>artistActor</b>: mode of destination actors. Default is 'Artist'
     * <li/><b>userActorLink</b>: relation used for predictions. Default is 'Derived'
     * <li/><b>artistCorrelationProperty</b>: property ID for storing artist correlation
     * data. Default is 'Correlated Artist'
     * <li/><b>evaluation</b>: should the algorithm reproduce existing given links.
     * Deafult is 'false'
     * <li/><b>createArtistCorrelationProperty</b>: are the correlations found
     * to be stored. Default is 'true'
     * <li/><b>positiveSignificance</b>: significance threshold for storing positive 
     * correlations as a decimal. Default is '0.10'
     * <li/><b>negativeSignificance</b>: significance threshold for storing negative 
     * correlations as a decimal. Default is '0.10'
     * <li/><b>expansionDepth</b>: maximum number of items to group into a single
     * correlation. Deafult is '5'
     * </ul>
     * @param map parameters to be loaded - may be null
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
            parameter[0].setValue("Dispersion Based Collaborative Filtering");
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "relation");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[1] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("relation") != null)) {
            parameter[1].setValue(map.getProperty("relation"));
        } else {
            parameter[1].setValue("Given");
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "userActor");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[2] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("userActor") != null)) {
            parameter[2].setValue(map.getProperty("userActor"));
        } else {
            parameter[2].setValue("User");
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "artistActor");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[3] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("artistActor") != null)) {
            parameter[3].setValue(map.getProperty("artistActor"));
        } else {
            parameter[3].setValue("Artist");
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "userActorLink");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[4] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("userProperty") != null)) {
            parameter[4].setValue(map.getProperty("userProperty"));
        } else {
            parameter[4].setValue("Derived");
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "artistCorrelationProperty");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[5] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("artistCorrelationProperty") != null)) {
            parameter[5].setValue(map.getProperty("artistCorrelationProperty"));
        } else {
            parameter[5].setValue("Correlated Artist");
        }

        props.setProperty("Type", "java.lang.Boolean");
        props.setProperty("Name", "evaluation");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[6] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("evaluation") != null)) {
            parameter[6].setValue(Boolean.parseBoolean(map.getProperty("evaluation")));
        } else {
            parameter[6].setValue(new Boolean(false));
        }

        props.setProperty("Type", "java.lang.Boolean");
        props.setProperty("Name", "createArtistCorrelationProperty");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[7] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("createArtistCorrelationProperty") != null)) {
            parameter[7].setValue(Boolean.parseBoolean(map.getProperty("createArtistCorrelationProperty")));
        } else {
            parameter[7].setValue(new Boolean(true));
        }

        props.setProperty("Type", "java.lang.Double");
        props.setProperty("Name", "positiveSignificance");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "false");
        parameter[8] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("positiveSignificance") != null)) {
            parameter[8].setValue(Double.parseDouble(map.getProperty("positiveSignificance")));
        } else {
            parameter[8].setValue(new Double(0.10));
        }
        
        props.setProperty("Type", "java.lang.Double");
        props.setProperty("Name", "negativeSignificance");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "false");
        parameter[9] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("negativeSignificance") != null)) {
            parameter[9].setValue(Double.parseDouble(map.getProperty("negativeSignificance")));
        } else {
            parameter[9].setValue(new Double(0.10));
        }
        
        props.setProperty("Type", "java.lang.Integer");
        props.setProperty("Name", "expansionDepth");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "false");
        parameter[10] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("expansionDepth") != null)) {
            parameter[10].setValue(Integer.parseInt(map.getProperty("expansionDepth")));
        } else {
            parameter[10].setValue(new Integer(5));
        }
        

        props.setProperty("Type", "Actor");
        props.setProperty("Relation", (String) parameter[2].getValue());
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.remove("Property");
        input[0] = DescriptorFactory.newInstance().createInputDescriptor(props);

        props.setProperty("Type", "Actor");
        props.setProperty("Relation", (String) parameter[3].getValue());
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.remove("Property");
        input[1] = DescriptorFactory.newInstance().createInputDescriptor(props);

        props.setProperty("Type", "Link");
        props.setProperty("Relation", (String) parameter[1].getValue());
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.remove("Property");
        input[2] = DescriptorFactory.newInstance().createInputDescriptor(props);

        props.setProperty("Type", "Link");
        props.setProperty("Relation", (String) parameter[4].getValue());
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.remove("Property");
        output[0] = DescriptorFactory.newInstance().createOutputDescriptor(props);

        props.setProperty("Type", "ActorProperty");
        props.setProperty("Relation", (String) parameter[3].getValue());
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property", (String) parameter[5].getValue());
        output[1] = DescriptorFactory.newInstance().createOutputDescriptor(props);
    }
}

