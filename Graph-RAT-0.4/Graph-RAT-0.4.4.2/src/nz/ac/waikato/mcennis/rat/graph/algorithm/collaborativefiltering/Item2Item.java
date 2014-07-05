/*
 * Created 2-2-08
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.graph.algorithm.collaborativefiltering;

import cern.colt.list.IntArrayList;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix1D;
import cern.colt.matrix.impl.SparseDoubleMatrix2D;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import nz.ac.waikato.mcennis.rat.graph.actor.Actor;
import nz.ac.waikato.mcennis.rat.graph.algorithm.Algorithm;
import org.dynamicfactory.descriptors.DescriptorFactory;
import org.dynamicfactory.descriptors.InputDescriptor;
import org.dynamicfactory.descriptors.OutputDescriptor;
import org.dynamicfactory.descriptors.SettableParameter;
import nz.ac.waikato.mcennis.rat.graph.link.Link;
import nz.ac.waikato.mcennis.rat.graph.link.LinkFactory;
import nz.ac.waikato.mcennis.rat.graph.model.ModelShell;
import nz.ac.waikato.mcennis.rat.scheduler.Scheduler;

/**
 * Performs classic Item to Item collaborative filtering.  Modified from the
 * orginal version submitted (under GPL) to audioscrobbler.net in 2003 by adding
 * code for using RAT data structures instead of JDBC calls.
 * 
 * @author Daniel McEnnis
 */
public class Item2Item extends ModelShell implements Algorithm {

    ParameterInternal[] parameter = new ParameterInternal[9];
    InputDescriptor[] input = new InputDescriptor[3];
    OutputDescriptor[] output = new OutputDescriptor[2];

    /**
     * 
     * Create a new generic Item2Item algorithm object with default parameters.
     */
    public Item2Item() {
        init(null);
    }

    @Override
    public void execute(Graph g) {
        Actor[] users = g.getActor((String) parameter[2].getValue());
        Actor[] artist = g.getActor((String) parameter[3].getValue());
        HashMap<Actor, Integer> user2index = new HashMap<Actor, Integer>();
        HashMap<Actor, Integer> artist2Index = new HashMap<Actor, Integer>();
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
                user2index.put(users[i], i);
            }
            if (users.length == 0) {
                Logger.getLogger(Item2Item.class.getName()).log(Level.WARNING,"No users of type '" + (String) parameter[2].getValue() + "' have links of type '" + (String) parameter[1].getValue() + "' in graph '" + g.getID() + "'");
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

            // Now that artist and users have been purged of empty sets, create the 
            // matrix describing who listens to what and how much.

            /**
             * provides an index into userArtistMatrix artist->user->(time in seconds)
             */
            IntArrayList[] indexArtist = new IntArrayList[artist.length];
            for (int i = 0; i < indexArtist.length; ++i) {
                indexArtist[i] = new IntArrayList();
            }

            // creates an array indexing all users that passed criteria specified at [user specification] above, allowing cheap user-ordered access.
            // each vector is a sorted list of artists that have non-zero entries for a particular user.
            // duplicate index of userArtistMatrix, but, when combined with indexArtist above, represents the matrix effeciently in both row order and column order.
            /**
             * provides an index into userArtistMatrix user->artist->(time in seconds)
             */
            IntArrayList[] indexUser = new IntArrayList[users.length];

            for (int i = 0; i < indexUser.length; ++i) {
                indexUser[i] = new IntArrayList();
            }


            SparseDoubleMatrix2D userArtist = new SparseDoubleMatrix2D(users.length, artist.length);
            for (int i = 0; i < users.length; ++i) {
                Link[] given = g.getLinkBySource((String) parameter[1].getValue(), users[i]);
                for (int j = 0; j < given.length; ++j) {
                    indexUser[i].add(artist2Index.get(given[j].getDestination()));
                    indexArtist[artist2Index.get(given[j].getDestination())].add(i);
                    userArtist.set(i, artist2Index.get(given[j].getDestination()), given[j].getStrength());
                }
            }


            // create total artist length strength across a user - used to compare recommendations across users.
            DenseDoubleMatrix1D userTime = new DenseDoubleMatrix1D(users.length);
            for (int i = 0; i < users.length; ++i) {
                double total = 0.0;
                for (int j = 0; j < artist.length; ++j) {
                    total += userArtist.get(i, j);
                }
                userTime.set(i, total);
            }

            // calculate total of link strength for each artist
            DenseDoubleMatrix1D time = new DenseDoubleMatrix1D(artist.length);
            DenseDoubleMatrix1D time2 = new DenseDoubleMatrix1D(artist.length);
            for (int i = 0; i < artist.length; ++i) {
                time.set(i, 0.0);
                time2.set(i, 0.0);
                Link[] given = g.getLinkByDestination((String) parameter[1].getValue(), artist[i]);
                for (int j = 0; j < given.length; ++j) {
                    time.set(i, time.get(i) + given[j].getStrength());
                    time2.set(i, time2.get(i) + given[j].getStrength() * given[j].getStrength());
                }
            }

            // calculate recommendations for every artist sequentially
            fireChange(Scheduler.SET_ALGORITHM_COUNT,artist.length);
            for (int a = 0; a < artist.length; ++a) {
                fireChange(Scheduler.SET_ALGORITHM_PROGRESS,a);
                DenseDoubleMatrix1D correlationMatrix = new DenseDoubleMatrix1D(artist.length);
                // calculate the correlation with every other artist using traditional CF methods, then...
                for (int i = 0; i < artist.length; ++i) {
                    double[] size = new double[1];
                    correlationMatrix.set(i, correlation(userArtist, indexArtist, time, time2, i, a, size));
                    if ((((Boolean) parameter[7].getValue()).booleanValue() == true) && (i != a)) {
                        createCorrelation(g, artist[a], correlationMatrix.get(i), artist[i]);
                    }
                }
                for (int u = 0; u < users.length; ++u) {
                    if ((((Boolean) parameter[6].getValue()).booleanValue() == false) && (indexUser[u].contains(a))) {
                        ;// intentionally a no-op.  Not evaluating and person already listens to artist a.
                    } else {
                        // Now that we have verified that the user has not listened to this artist, predict the number of seconds of this artist that the person
                        // would have listened to if they had known about this music before....
                        double suggestion = 0.0;
                        for (int i = 0; i < indexUser[u].size(); ++i) {
                            suggestion += userArtist.getQuick(u, indexUser[u].getQuick(i)) *
                                    correlationMatrix.getQuick(indexUser[u].getQuick(i)) / userTime.getQuick(u);
                        }
                        // Sanity check for the predictions - debugging code that can be later removed once the algorithm is declared stable.
                        if (Double.isNaN(suggestion) || Double.isInfinite(suggestion)) {
                            Logger.getLogger(Item2Item.class.getName()).log(Level.WARNING,"User " + u + " Artist " + a + " Suggestion: " + suggestion);
                        } else {
                            // Add this prediction to each user's total.
                            createRecommendation(g, users[u], suggestion, artist[a]);
                        }
                    }
                }
            }


        } else {
            if (users == null) {
                Logger.getLogger(Item2Item.class.getName()).log(Level.WARNING,"No users of type '" + (String) parameter[2].getValue() + "' are present in this graph '" + g.getID() + "'");
            }
            if (artist == null) {
                Logger.getLogger(Item2Item.class.getName()).log(Level.WARNING,"No artists of type '" + (String) parameter[3].getValue() + "' are present in this graph '" + g.getID() + "'");
            }
        }

    }

    /**
     * function to caclualate the digrams needed for calculating relationships between artists.
     * 
     * @param a - artist to be crossed
     * @param b - artist to be crossed
     * @param size - double passed by reference using array hack/workaround used to pass back the total number of users listening to either artist a or b
     * @return - sum over all users of product of time in seconds a user listened to artist a and time listened to artist b. 
     */
    protected double digram(IntArrayList[] indexArtist, DoubleMatrix2D userArtistMatrix, int a, int b, double[] size) {
        double total = 0.0;
        int i = 0;
        int j = 0;
        double add = 0.0;
        while (i < indexArtist[a].size() && j < indexArtist[b].size()) {
            if (indexArtist[a].getQuick(i) < indexArtist[b].getQuick(j)) {
                ++i;
            } else if (indexArtist[a].getQuick(i) > indexArtist[b].getQuick(j)) {
                ++j;
            } else {
                total += userArtistMatrix.getQuick(indexArtist[a].getQuick(i), a) * userArtistMatrix.getQuick(indexArtist[b].getQuick(j), b);
                ++i;
                ++j;
            }
            add += 1.0;
        }
        size[0] =
                add + (indexArtist[a].size() - i) + (indexArtist[b].size() - j);
        return total;
    }

    /**
     * calculates the correlation between 2 artists using standard statistical definition of correlation.
     * 
     * @param a - artist in Matrix
     * @param b - artist in Matrix
     * @param size - number of users listening to either artists
     * @return - correlation between listening to artist a and listening to artist b.
     */
    protected double correlation(DoubleMatrix2D userArtist, IntArrayList[] indexArtist, DoubleMatrix1D time, DoubleMatrix1D time2, int a, int b, double[] size) {
        double d = digram(indexArtist, userArtist, a, b, size);

        if ((indexArtist[a].size() > 1) && (indexArtist[b].size() > 1) && (d != Double.NaN)) {
            d =
                    (d - (time.getQuick(a) * time.getQuick(b) / size[0])) / (Math.sqrt(
                    (time2.getQuick(a) - time.getQuick(a) * time.getQuick(a) / size[0]) * (time2.getQuick(b) - time.getQuick(b) * time.getQuick(b) / size[0])));
            if (d > 0) {
                return d;
            } else {
                return 0.0;
            }
        } else if (d == Double.NaN) {
            System.err.println(
                    "Problems with digram " + a + "," + b + ": registers NaN");
        }
        return 0.0;
    }

    protected void createRecommendation(Graph g, Actor u, double strength, Actor a) {
        if (strength > 0) {
            Properties props = new Properties();
            props.setProperty("LinkType", (String) parameter[4].getValue());
            Link link = LinkFactory.newInstance().create(props);
            link.set(u, strength, a);
            g.add(link);
        }
    }

    protected void createCorrelation(Graph g, Actor u, double strength, Actor a) {
        if (strength > ((Double) parameter[8].getValue()).doubleValue()) {
            Properties props = new Properties();
            props.setProperty("LinkType", (String) parameter[5].getValue());
            Link link = LinkFactory.newInstance().create(props);
            link.set(u, strength, a);
            g.add(link);
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
     * <li/><b>name</b>: name of this algorithm. Default is 'Basic Item to Item Collaborative Filtering'
     * <li/><b>relation</b>: name of the link relation to execute over. Default is 'Given'
     * <li/><b>userActor</b>: name of the source actors for whom predictions are made. Default is 'User'
     * <li/><b>artistActor</b>: name of the destination actors for whom correlations 
     * are calculated. Deafult is 'Artist'
     * <li/><b>userActorLink</b>: relation used for predictions. Default is 'Derived'
     * <li/><b>artistArtistLink</b>: relation used for describing correlations.
     * Default is 'Correlated Artist'
     * <li/><b>evaluation</b>: Should predicted links be suppressed if they match
     * an existing link between source and destination.  Used when pre-existing 
     * links between source and destination are the ground truth. Default is 'false'
     * <li/><b>createArtistSimilarity</b>: Should correlations between destination
     * actors be stored. Deafult is 'false'
     * <li/><b>artistSimilarityThreshold</b>: threshold between destination
     * actors before a correlation link is created. Default is '0.0'
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
            parameter[0].setValue("Basic Item to Item Collaborative Filtering");
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
        props.setProperty("Name", "artistArtistLink");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[5] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("artistProperty") != null)) {
            parameter[5].setValue(map.getProperty("artistProperty"));
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
            parameter[6].setValue(new Boolean("false"));
        }

        props.setProperty("Type", "java.lang.Boolean");
        props.setProperty("Name", "createArtistSimilarity");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[7] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("createArtistSimilarity") != null)) {
            parameter[7].setValue(Boolean.parseBoolean(map.getProperty("createArtistSimilarity")));
        } else {
            parameter[7].setValue(new Boolean("false"));
        }

        props.setProperty("Type", "java.lang.Double");
        props.setProperty("Name", "artistSimilarityThreshold");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[8] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("artistSimilarityThreshold") != null)) {
            parameter[8].setValue(Double.parseDouble(map.getProperty("artistSimilarityThreshold")));
        } else {
            parameter[8].setValue(new Double(0.0));
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
