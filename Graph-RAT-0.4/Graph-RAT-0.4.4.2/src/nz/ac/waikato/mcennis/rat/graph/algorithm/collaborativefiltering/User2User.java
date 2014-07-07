/*
 * Created 3-2-02
 * Copyright Daniel McEnnis, see license.txt
 */
package org.mcennis.graphrat.algorithm.collaborativefiltering;

import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.impl.SparseDoubleMatrix1D;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mcennis.graphrat.graph.Graph;
import org.mcennis.graphrat.actor.Actor;
import org.mcennis.graphrat.algorithm.Algorithm;
import org.dynamicfactory.descriptors.DescriptorFactory;
import org.dynamicfactory.descriptors.InputDescriptor;
import org.dynamicfactory.descriptors.OutputDescriptor;
import org.dynamicfactory.descriptors.SettableParameter;
import org.mcennis.graphrat.link.Link;
import org.mcennis.graphrat.link.LinkFactory;
import org.dynamicfactory.model.ModelShell;
import org.mcennis.graphrat.scheduler.Scheduler;

/**
 * Performs User to user collaborative filtering where similar users' links to 
 * destination actors (items) are used to suggest items for the given user.
 * @author Daniel McEnnis
 */
public class User2User extends ModelShell implements Algorithm {

    ParameterInternal[] parameter = new ParameterInternal[9];
    InputDescriptor[] input = new InputDescriptor[3];
    OutputDescriptor[] output = new OutputDescriptor[2];
    HashMap<Actor, Integer> user2Index = new HashMap<Actor, Integer>();
    HashMap<Actor, Integer> artist2Index = new HashMap<Actor, Integer>();
    HashMap<Actor, Link> artist2Link = new HashMap<Actor, Link>();
    Actor[] artist;
    
    /**
     * Create a generic user to user algorithm with default parameters.
     */
    public User2User(){
        init(null);
    }

    @Override
    public void execute(Graph g) {
        Actor[] users = g.getActor((String) parameter[2].getValue());
        artist = g.getActor((String) parameter[3].getValue());
        // list of all users that have listened to artists
        LinkedList<Actor> userBase = new LinkedList<Actor>();
        // list of all artists that someone in the user list has listened to
        LinkedList<Actor> artistBase = new LinkedList<Actor>();


        if ((users != null) && (artist != null)) {
            // eliminate from calculations any artist or user that has either
            // not listened to a song or is not listened to by a user.  This can
            // occur if this is a sub-graph of the root graph.
            for (int i = 0; i < users.length; ++i) {
                Link[] given = g.getLinkBySource((String) parameter[1].getValue(), users[i]);
                if (given != null) {
                    userBase.add(users[i]);
                }
            }
            users = userBase.toArray(new Actor[]{});
            for (int i = 0; i < users.length; ++i) {
                user2Index.put(users[i], i);
            }
            if (users.length == 0) {
                Logger.getLogger(User2User.class.getName()).log(Level.WARNING,"No users of type '" + (String) parameter[2].getValue() + "' have links of type '" + (String) parameter[1].getValue() + "' in graph '" + g.getID() + "'");
            }
            for (int i = 0; i < artist.length; ++i) {
                Link[] given = g.getLinkByDestination((String) parameter[1].getValue(), artist[i]);
                if (given != null) {
                    artistBase.add(artist[i]);
                }
            }
            artist = artistBase.toArray(new Actor[]{});
            for (int i = 0; i < artist.length; ++i) {
                artist2Index.put(artist[i], i);
            }
            if (artist.length == 0) {
                Logger.getLogger(Item2Item.class.getName()).log(Level.WARNING,"No users of type '" + (String) parameter[3].getValue() + "' have links of type '" + (String) parameter[1].getValue() + "' in graph '" + g.getID() + "'");
            }

            // iterate over all users
            fireChange(Scheduler.SET_ALGORITHM_COUNT,users.length);
            for (int i = 0; i < users.length; ++i) {
                fireChange(Scheduler.SET_ALGORITHM_PROGRESS,i);
                // get the base vector to compare against
                DoubleMatrix1D baseUser = getUserVector(g, users[i], artist.length);
                // initialize the actor-link hashmap
                artist2Link.clear();
                for (int j = 0; j < users.length; ++j) {
                    if (i != j) {
                        DoubleMatrix1D otherUser = getUserVector(g, users[j], artist.length);
                        double sim = compare(baseUser, otherUser);
                        if (sim > ((Double) parameter[8].getValue()).doubleValue()) {
                            if (((Boolean) parameter[7].getValue()).booleanValue() == true) {
                                Properties props = new Properties();
                                props.setProperty("LinkType", (String) parameter[5].getValue());
                                Link userLink = LinkFactory.newInstance().create(props);
                                userLink.set(users[i], sim, users[j]);
                                g.add(userLink);
                            }
                            doRecommendation(g,users[i],baseUser,users[j],otherUser,sim);   
                        }
                    }
                }
                Iterator<Actor> key = artist2Link.keySet().iterator();
                while (key.hasNext()) {
                    g.add(artist2Link.get(key.next()));
                }
            }
            Logger.getLogger(Item2Item.class.getName()).log(Level.FINE,"Done");

        } else {
            if (users == null) {
                Logger.getLogger(Item2Item.class.getName()).log(Level.WARNING,"No users of type '" + (String) parameter[2].getValue() + "' are present in this graph '" + g.getID() + "'");
            }
            if (artist == null) {
                Logger.getLogger(Item2Item.class.getName()).log(Level.WARNING,"No artists of type '" + (String) parameter[3].getValue() + "' are present in this graph '" + g.getID() + "'");
            }
        }
    }

    protected DoubleMatrix1D getUserVector(Graph g, Actor u, int numArtists) {
        Link[] links = g.getLinkBySource((String) parameter[1].getValue(), u);
        SparseDoubleMatrix1D ret = new SparseDoubleMatrix1D(numArtists);
        ret.assign(0.0);
        if (links != null) {
            for (int i = 0; i < links.length; ++i) {
                ret.set(artist2Index.get(links[i].getDestination()), links[i].getStrength());
            }
        }
        
        
        return ret;
    }

    protected void doRecommendation(Graph g, Actor left, DoubleMatrix1D leftArray, Actor right, DoubleMatrix1D rightArray, double sim) {
        for(int i=0;i<rightArray.size();++i){
            if(rightArray.get(i) != 0.0){
                if((((Boolean)parameter[6].getValue()).booleanValue()==false)&&(leftArray.get(i)!=0.0)){
                    ;// deliberate no-op - this artist is already recommended
                }else{
                    if(!artist2Link.containsKey(right)){
                            Properties props = new Properties();
                                props.setProperty("LinkType", (String) parameter[4].getValue());
                                Link artistLink = LinkFactory.newInstance().create(props);
                                artistLink.set(left,0.0,artist[i]);
                                artist2Link.put(artist[i], artistLink);
                        }
                    Link artistLink = artist2Link.get(artist[i]);
                    artistLink.set(artistLink.getStrength()+rightArray.get(i)*sim);
                }
            }
        }
    }

    protected double compare(DoubleMatrix1D left, DoubleMatrix1D right) {
        return left.zDotProduct(right)/(Math.sqrt(left.zDotProduct(left))*Math.sqrt(right.zDotProduct(right)));
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
     * <li/><b>name</b>: name of this algorithm. Default is 'Basic Item to Item Collaborative Filtering'
     * <li/><b>relation</b>: name of the link relation to execute over. Default is 'Given'
     * <li/><b>userActor</b>: name of the source actors for whom predictions are made. Default is 'User'
     * <li/><b>artistActor</b>: name of the destination actors for whom correlations 
     * are calculated. Deafult is 'Artist'
     * <li/><b>userActorLink</b>: relation used for predictions. Default is 'Derived'
     * <li/><b>userUserLink</b>: relation used for describing correlations.
     * Default is 'Nearest Neighbor'
     * <li/><b>evaluation</b>: Should predicted links be suppressed if they match
     * an existing link between source and destination.  Used when pre-existing 
     * links between source and destination are the ground truth. Default is 'false'
     * <li/><b>createUserSimilarity</b>: Should similarities between source
     * actors be stored. Deafult is 'false'
     * <li/><b>threshold</b>: threshold between sources
     * actors before a correlation link is created. Default is '0.5'
     * </ul>
     * 
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
            parameter[0].setValue("Basic User to User Collaborative Filtering");
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
        if ((map != null) && (map.getProperty("userActorLink") != null)) {
            parameter[4].setValue(map.getProperty("userActorLink"));
        } else {
            parameter[4].setValue("Derived");
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "userUserLink");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[5] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("userUserLink") != null)) {
            parameter[5].setValue(map.getProperty("userUserLink"));
        } else {
            parameter[5].setValue("Nearest Neighbor");
        }

        props.setProperty("Type", "java.lang.Boolean");
        props.setProperty("Name", "evaluation");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[6] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("evaluation") != null)) {
            parameter[6].setValue(Boolean.parseBoolean(map.getProperty("evaluation")));
        } else {
            parameter[6].setValue(new Boolean("false"));
        }

        props.setProperty("Type", "java.lang.Boolean");
        props.setProperty("Name", "createUserSimilarity");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[7] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("createUserSimilarity") != null)) {
            parameter[7].setValue(Boolean.parseBoolean(map.getProperty("createUserSimilarity")));
        } else {
            parameter[7].setValue(new Boolean("false"));
        }

        props.setProperty("Type", "java.lang.Double");
        props.setProperty("Name", "threshold");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[8] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("threshold") != null)) {
            parameter[8].setValue(Double.parseDouble(map.getProperty("threshold")));
        } else {
            parameter[8].setValue(new Double(0.5));
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

        props.setProperty("Type", "Link");
        props.setProperty("Relation", (String) parameter[5].getValue());
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.remove("Property");
        output[1] = DescriptorFactory.newInstance().createOutputDescriptor(props);
    }
}
