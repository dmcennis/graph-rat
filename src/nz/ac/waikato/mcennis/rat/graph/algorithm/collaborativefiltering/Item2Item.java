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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import java.util.logging.Logger;

import nz.ac.waikato.mcennis.rat.graph.Graph;

import nz.ac.waikato.mcennis.rat.graph.actor.Actor;

import nz.ac.waikato.mcennis.rat.graph.algorithm.Algorithm;
import nz.ac.waikato.mcennis.rat.graph.algorithm.AlgorithmMacros;
import org.dynamicfactory.descriptors.IODescriptor;

import org.dynamicfactory.descriptors.IODescriptor.Type;
import org.dynamicfactory.descriptors.IODescriptorFactory;

import nz.ac.waikato.mcennis.rat.graph.link.Link;

import nz.ac.waikato.mcennis.rat.graph.link.LinkFactory;

import nz.ac.waikato.mcennis.rat.graph.model.ModelShell;

import nz.ac.waikato.mcennis.rat.graph.query.ActorQuery;
import nz.ac.waikato.mcennis.rat.graph.query.ActorQueryFactory;
import nz.ac.waikato.mcennis.rat.graph.query.LinkQuery;
import nz.ac.waikato.mcennis.rat.graph.query.LinkQuery.LinkEnd;
import nz.ac.waikato.mcennis.rat.graph.query.LinkQuery.SetOperation;
import nz.ac.waikato.mcennis.rat.graph.query.LinkQueryFactory;
import nz.ac.waikato.mcennis.rat.graph.query.actor.ActorByMode;
import nz.ac.waikato.mcennis.rat.graph.query.link.AndLinkQuery;
import nz.ac.waikato.mcennis.rat.graph.query.link.LinkByActor;
import nz.ac.waikato.mcennis.rat.graph.query.link.LinkByRelation;
import nz.ac.waikato.mcennis.rat.scheduler.Scheduler;

/**

 * Performs classic Item to Item collaborative filtering.  Modified from the

 * orginal version submitted (under GPL) to audioscrobbler.net in 2003 by adding

 * code for using RAT data structures instead of JDBC calls.

 * Jan 5, 2009 - modified to use new interface
 *
 * Note: Deprecated:  Use the following to replace this algorithm with a more
 * configurable algorithm
 * &lt;SimilarityByLink&gt;
 *  &lt;Property&gt;&lt;Name&gt;Mode&lt;/Name&gt;&lt;Value&gt;Artist&lt;/Value&gt;&lt;/Property&gt;
 *  &lt;Property&gt;&lt;Name&gt;Relation&lt;/Name&gt;&lt;Value&gt;ListensTo&lt;/Value&gt;&lt;/Property&gt;
 * &lt;/SimilarityByLink&gt;
 * &lt;AggregateByLink&gt;
 *  &lt;Property&gt;&lt;Name&gt;Mode&lt;/Name&gt;&lt;Value&gt;Artist&lt;/Value&gt;&lt;/Property&gt;
 *  &lt;Property&gt;&lt;Name&gt;Relation&lt;/Name&gt;&lt;Value&gt;Similarity&lt;/Value&gt;&lt;/Property&gt;
 *  &lt;Property&gt;&lt;Name&gt;DestinationProperty&lt;/Name&gt;&lt;Value&gt;Property&lt;/Value&gt;&lt;/Property&gt;
 * &lt;/AggregateByLink&gt;
 * &lt;AggregateByLinkProperty&gt;
 *  &lt;Property&gt;&lt;Name&gt;Mode&lt;/Name&gt;&lt;Value&gt;User&lt;/Value&gt;&lt;/Property&gt;
 *  &lt;Property&gt;&lt;Name&gt;SourceProperty&lt;/Name&gt;&lt;Value&gt;Property&lt;/Value&gt;&lt;/Property&gt;
 *  &lt;Property&gt;&lt;Name&gt;DestinationProperty&lt;/Name&gt;&lt;Value&gt;UserProperty&lt;/Value&gt;&lt;/Property&gt;
 * &lt;/AggregateByLinkProperty&gt;
 * &lt;PropertyToLink&gt;
 *  &lt;Property&gt;&lt;Name&gt;SourceProperty&lt;/Name&gt;&lt;Value&gt;UserProperty&lt;/Value&gt;&lt;/Property&gt;
 *  &lt;Property&gt;&lt;Name&gt;SourceMode&lt;/Name&gt;&lt;Value&gt;User&lt;/Value&gt;&lt;/Property&gt;
 *  &lt;Property&gt;&lt;Name&gt;DestinationMode&lt;/Name&gt;&lt;Value&gt;Artist&lt;/Value&gt;&lt;/Property&gt;
 *  &lt;Property&gt;&lt;Name&gt;Relation&lt;/Name&gt;&lt;Value&gt;Derived&lt;/Value&gt;&lt;/Property&gt;
 * &lt;/PropertyToLink&gt;

 * @author Daniel McEnnis

 */
public class Item2Item extends ModelShell implements Algorithm {

    PropertiesInternal parameter = PropertiesFactory.newInstance().create();
    LinkedList<IODescriptor> input = new LinkedList<IODescriptor>();
    LinkedList<IODescriptor> output = new LinkedList<IODescriptor>();

    /**

     * 

     * Create a new generic Item2Item algorithm object with default parameters.

     */
    public Item2Item() {
        ParameterInternal name = ParameterFactory.newInstance().create("AlgorithmClass", String.class);
        SyntaxObject syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("Item to Item");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Name", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, Integer.MAX_VALUE, null, String.class);
        name.setRestrictions(syntax);
        name.add("Item to Item");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Category", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("Collaborative Filtering");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("LinkFilter", LinkQuery.class);
        syntax = SyntaxCheckerFactory.newInstance().create(0, 1, null, LinkQuery.class);
        name.setRestrictions(syntax);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("ActorFilter", ActorQuery.class);
        syntax = SyntaxCheckerFactory.newInstance().create(0, 1, null, ActorQuery.class);
        name.setRestrictions(syntax);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("SourceMode", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("tag");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("ReferenceMode", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("tag");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("SourceRelation", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("tag");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("DestinationRelation", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("tag");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("SimilarityRelation", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("tag");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("LinkDirection", LinkEnd.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, LinkEnd.class);
        name.setRestrictions(syntax);
        name.add(LinkEnd.SOURCE);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Threshold", Double.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, Double.class);
        name.setRestrictions(syntax);
        name.add(0.05);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("StoreSimilarity", Boolean.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, Boolean.class);
        name.setRestrictions(syntax);
        name.add(true);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("NewRecommendationsOnly", Boolean.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, Boolean.class);
        name.setRestrictions(syntax);
        name.add(false);
        parameter.add(name);
    }

    @Override
    public void execute(Graph g) {
        ActorByMode userQuery = (ActorByMode) ActorQueryFactory.newInstance().create("ActorByMode");
        userQuery.buildQuery((String) parameter.get("ReferenceMode").get(),".*",false);
        Iterator<Actor> user = AlgorithmMacros.filterActor(parameter, g, userQuery, null, null);

        ActorByMode artistQuery = (ActorByMode) ActorQueryFactory.newInstance().create("ActorByMode");
        artistQuery.buildQuery((String) parameter.get("SourceMode").get(),".*",false);
        Iterator<Actor> artists = AlgorithmMacros.filterActor(parameter, g, artistQuery, null, null);

        AndLinkQuery and = (AndLinkQuery) LinkQueryFactory.newInstance().create("AndActorQuery");
        LinkedList<LinkQuery> source = new LinkedList<LinkQuery>();

        if (parameter.get("LinkFilter").getValue().size() > 0) {
            source.add((LinkQuery) parameter.get("LinkFilter").get());
        }

        LinkByRelation relation = (LinkByRelation) LinkQueryFactory.newInstance().create("LinkByRelation");
        relation.buildQuery((String) parameter.get("SourceRelation").get(), false);
        source.add(relation);

        LinkByActor actorLink = (LinkByActor) LinkQueryFactory.newInstance().create("LinkByActor");
        if (((LinkEnd) parameter.get("LinkEnd").get()).equals(LinkEnd.SOURCE)) {
            actorLink.buildQuery(false, artistQuery, userQuery, SetOperation.AND);
        } else {
            actorLink.buildQuery(false, userQuery, artistQuery, SetOperation.AND);
        }
        source.add(actorLink);
        and.buildQuery(source);

        LinkEnd end = (LinkEnd) parameter.get("LinkEnd").get();

        HashMap<Actor, Integer> user2index = new HashMap<Actor, Integer>();

        HashMap<Actor, Integer> artist2Index = new HashMap<Actor, Integer>();

        // list of all users that have listened to artists

        LinkedList<Actor> userBase = new LinkedList<Actor>();

        // list of all artists that someone in the user list has listened to

        LinkedList<Actor> artistBase = new LinkedList<Actor>();





        if ((user.hasNext()) && (artists.hasNext())) {
            Actor[] users = null;
            Actor[] artist = null;
            // eliminate from calculations any artist or user that has either

            // not listened to a song or is not listened to by a user.  This can

            // occur if this is a sub-graph of the root graph.
            LinkedList<Actor> singleActorList = new LinkedList<Actor>();
            while (user.hasNext()) {
                singleActorList.add(user.next());
                Iterator<Link> given = null;
                if (end == LinkEnd.DESTINATION) {
                    given = relation.executeIterator(g, singleActorList, null, null);
                } else {
                    given = relation.executeIterator(g, null, singleActorList, null);
                }

                if (given.hasNext()) {

                    userBase.addAll(singleActorList);

                }
                singleActorList.clear();
            }

            users = userBase.toArray(new Actor[]{});

            for (int i = 0; i < users.length; ++i) {

                user2index.put(users[i], i);

            }

            if (users.length == 0) {

                Logger.getLogger(Item2Item.class.getName()).log(Level.WARNING, "No users of type '" + (String) parameter.get("ReferenceMode").get() + "' have links of type '" + (String) parameter.get("Relation").get() + "' in graph '" + g.getID() + "'");

            }

            while (artists.hasNext()) {
                singleActorList.add(artists.next());
                Iterator<Link> given = null;
                if (end == LinkEnd.SOURCE) {
                    given = relation.executeIterator(g, singleActorList, null, null);
                } else {
                    given = relation.executeIterator(g, null, singleActorList, null);
                }

                if (given.hasNext()) {

                    artistBase.addAll(singleActorList);

                }
                singleActorList.clear();
            }

            artist = artistBase.toArray(new Actor[]{});

            for (int i = 0; i < artist.length; ++i) {

                artist2Index.put(artist[i], i);

            }

            if (artist.length == 0) {

                Logger.getLogger(Item2Item.class.getName()).log(Level.WARNING, "No users of type '" + (String) parameter.get("SourceMode").get() + "' have links of type '" + (String) parameter.get("SourceRelation").get() + "' in graph '" + g.getID() + "'");

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

                singleActorList.add(users[i]);
                Iterator<Link> given = null;
                if (end == LinkEnd.DESTINATION) {
                    given = relation.executeIterator(g, singleActorList, null, null);
                    while (given.hasNext()) {
                        Link l = given.next();
                        indexUser[i].add(artist2Index.get(l.getDestination()));

                        indexArtist[artist2Index.get(l.getDestination())].add(i);

                        userArtist.set(i, artist2Index.get(l.getDestination()), l.getStrength());

                    }
                } else {
                    given = relation.executeIterator(g, null, singleActorList, null);
                    while (given.hasNext()) {
                        Link l = given.next();
                        indexUser[i].add(artist2Index.get(l.getSource()));

                        indexArtist[artist2Index.get(l.getSource())].add(i);

                        userArtist.set(i, artist2Index.get(l.getSource()), l.getStrength());

                    }
                }
                singleActorList.clear();


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

                singleActorList.add(artist[i]);
                Iterator<Link> given = null;
                if (end == LinkEnd.DESTINATION) {
                    given = relation.executeIterator(g, null, singleActorList, null);
                }else{
                    given = relation.executeIterator(g, singleActorList, null, null);
                }
                while (given.hasNext()) {
                    Link l = given.next();
                    time.set(i, time.get(i) + l.getStrength());

                    time2.set(i, time2.get(i) + l.getStrength() * l.getStrength());

                }
                singleActorList.clear();
            }



            // calculate recommendations for every artist sequentially

            fireChange(Scheduler.SET_ALGORITHM_COUNT, artist.length);

            for (int a = 0; a < artist.length; ++a) {

                fireChange(Scheduler.SET_ALGORITHM_PROGRESS, a);

                DenseDoubleMatrix1D correlationMatrix = new DenseDoubleMatrix1D(artist.length);

                // calculate the correlation with every other artist using traditional CF methods, then...

                for (int i = 0; i < artist.length; ++i) {

                    double[] size = new double[1];

                    correlationMatrix.set(i, correlation(userArtist, indexArtist, time, time2, i, a, size));

                    if ((((Boolean) parameter.get("StoreSimilarity").get()).booleanValue() == true) && (i != a)) {

                        createCorrelation(g, artist[a], correlationMatrix.get(i), artist[i]);

                    }

                }

                for (int u = 0; u < users.length; ++u) {

                    if ((((Boolean) parameter.get("NewRecommendationsOnly").get()).booleanValue() == true) && (indexUser[u].contains(a))) {

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

                            Logger.getLogger(Item2Item.class.getName()).log(Level.WARNING, "User " + u + " Artist " + a + " Suggestion: " + suggestion);

                        } else {

                            // Add this prediction to each user's total.

                            createRecommendation(g, users[u], suggestion, artist[a]);

                        }

                    }

                }

            }





        } else {

            if (!user.hasNext()) {

                Logger.getLogger(Item2Item.class.getName()).log(Level.WARNING, "No users of type '" + (String) parameter.get("ReferenceMode").get() + "' are present in this graph '" + g.getID() + "'");

            }

            if (!artists.hasNext()) {

                Logger.getLogger(Item2Item.class.getName()).log(Level.WARNING, "No artists of type '" + (String) parameter.get("SourceMode").get() + "' are present in this graph '" + g.getID() + "'");

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
            Link link = LinkFactory.newInstance().create((String)parameter.get("DestinationRelation").get());

            link.set(u, strength, a);

            g.add(link);

        }

    }

    protected void createCorrelation(Graph g, Actor u, double strength, Actor a) {

        if (strength > ((Double) parameter.get("Threshold").get()).doubleValue()) {

            Link link = LinkFactory.newInstance().create((String)parameter.get("SimilarityRelation").get());

            link.set(u, strength, a);

            g.add(link);

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
        if(parameter.check(map)){
            parameter.merge(map);

            IODescriptor desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR,
                    (String)parameter.get("Name").get(),
                    (String)parameter.get("SourceMode").get(),
                    null,
                    null,
                    "",
                    false);
            input.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR,
                    (String)parameter.get("Name").get(),
                    (String)parameter.get("ReferenceMode").get(),
                    null,
                    null,
                    "",
                    false);
            input.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.LINK,
                    (String)parameter.get("Name").get(),
                    (String)parameter.get("SourceRelation").get(),
                    null,
                    null,
                    "",
                    false);
            input.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.LINK,
                    (String)parameter.get("Name").get(),
                    (String)parameter.get("DestinationRelation").get(),
                    null,
                    null,
                    "",
                    false);
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.LINK,
                    (String)parameter.get("Name").get(),
                    (String)parameter.get("SimilarityRelation").get(),
                    null,
                    null,
                    "",
                    false);
            output.add(desc);
        }
    }

    public Item2Item prototype(){
        return new Item2Item();
    }
}

