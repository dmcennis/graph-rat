/*

 * Created 3-2-02

 * Copyright Daniel McEnnis, see license.txt

 */
package nz.ac.waikato.mcennis.rat.graph.algorithm.collaborativefiltering;

import cern.colt.matrix.DoubleMatrix1D;

import cern.colt.matrix.impl.SparseDoubleMatrix1D;

import java.util.HashMap;

import java.util.Iterator;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import java.util.logging.Logger;

import nz.ac.waikato.mcennis.rat.graph.Graph;
import org.dynamicfactory.descriptors.*;
import org.dynamicfactory.property.Property;
import org.dynamicfactory.property.PropertyFactory;
import org.dynamicfactory.property.InvalidObjectTypeException;

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

 * Performs User to user collaborative filtering where similar users' links to 

 * destination actors (items) are used to suggest items for the given user.

 * @author Daniel McEnnis

 */
public class User2User extends ModelShell implements Algorithm {

    PropertiesInternal parameter = PropertiesFactory.newInstance().create();
    LinkedList<IODescriptor> input = new LinkedList<IODescriptor>();
    LinkedList<IODescriptor> output = new LinkedList<IODescriptor>();
    HashMap<Actor, Integer> user2Index = new HashMap<Actor, Integer>();
    HashMap<Actor, Integer> artist2Index = new HashMap<Actor, Integer>();
    HashMap<Actor, Link> artist2Link = new HashMap<Actor, Link>();
    Actor[] artist;

    /**

     * Create a generic user to user algorithm with default parameters.

     */
    public User2User() {
        ParameterInternal name = ParameterFactory.newInstance().create("AlgorithmClass", String.class);
        SyntaxObject syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("User to User");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Name", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, Integer.MAX_VALUE, null, String.class);
        name.setRestrictions(syntax);
        name.add("User to User");
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

                user2Index.put(users[i], i);

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



            // iterate over all users

            fireChange(Scheduler.SET_ALGORITHM_COUNT, users.length);

            for (int i = 0; i < users.length; ++i) {

                fireChange(Scheduler.SET_ALGORITHM_PROGRESS, i);

                // get the base vector to compare against

                singleActorList.add(users[i]);
                DoubleMatrix1D baseUser = getUserVector(g, relation, singleActorList, artist.length);
                singleActorList.clear();
                // initialize the actor-link hashmap

                artist2Link.clear();

                for (int j = 0; j < users.length; ++j) {

                    if (i != j) {

                        singleActorList.add(users[j]);
                        DoubleMatrix1D otherUser = getUserVector(g, relation, singleActorList, artist.length);
                        singleActorList.clear();

                        double sim = compare(baseUser, otherUser);

                        if (sim > ((Double) parameter.get("Threshold").get()).doubleValue()) {

                            if (((Boolean) parameter.get("StoreSimilarity").get()).booleanValue() == true) {
                                Link userLink = LinkFactory.newInstance().create((String) parameter.get("SimilarityRelation").get());

                                userLink.set(users[i], sim, users[j]);

                                g.add(userLink);

                            }

                            doRecommendation(g, users[i], baseUser, users[j], otherUser, sim);

                        }

                    }

                }

                Iterator<Actor> key = artist2Link.keySet().iterator();

                while (key.hasNext()) {

                    g.add(artist2Link.get(key.next()));

                }

            }

            Logger.getLogger(Item2Item.class.getName()).log(Level.FINE, "Done");



        } else {
            if (!user.hasNext()) {

                Logger.getLogger(User2User.class.getName()).log(Level.WARNING, "No users of type '" + (String) parameter.get("ReferenceMode").get() + "' are present in this graph '" + g.getID() + "'");

            }

            if (!artists.hasNext()) {

                Logger.getLogger(User2User.class.getName()).log(Level.WARNING, "No artists of type '" + (String) parameter.get("SourceMode").get() + "' are present in this graph '" + g.getID() + "'");

            }

        }



    }

    protected DoubleMatrix1D getUserVector(Graph g, LinkQuery relation, LinkedList<Actor> u, int numArtists) {

        Iterator<Link> links = null;
        if (((LinkEnd) parameter.get("LinkEnd").get()) == LinkEnd.DESTINATION) {
            links = relation.executeIterator(g, u, null, null);
        } else {
            links = relation.executeIterator(g, null, u, null);
        }

        SparseDoubleMatrix1D ret = new SparseDoubleMatrix1D(numArtists);

        ret.assign(0.0);

        while (links.hasNext()) {
            Link l = links.next();
            if (((LinkEnd) parameter.get("LinkEnd").get()) == LinkEnd.DESTINATION) {
                ret.set(artist2Index.get(l.getSource()), l.getStrength());
            } else {
                ret.set(artist2Index.get(l.getDestination()), l.getStrength());
            }
        }
        return ret;

    }

    protected void doRecommendation(Graph g, Actor left, DoubleMatrix1D leftArray, Actor right, DoubleMatrix1D rightArray, double sim) {

        for (int i = 0; i < rightArray.size(); ++i) {

            if (rightArray.get(i) != 0.0) {

                if ((((Boolean) parameter.get("NewRecommendationsOnly").get()).booleanValue() == true) && (leftArray.get(i) != 0.0)) {

                    ;// deliberate no-op - this artist is already recommended

                } else {

                    if (!artist2Link.containsKey(right)) {
                        Link artistLink = LinkFactory.newInstance().create((String) parameter.get("DestinationRelation").get());

                        artistLink.set(left, 0.0, artist[i]);

                        artist2Link.put(artist[i], artistLink);

                    }

                    Link artistLink = artist2Link.get(artist[i]);

                    artistLink.set(artistLink.getStrength() + rightArray.get(i) * sim);

                }

            }

        }

    }

    protected double compare(DoubleMatrix1D left, DoubleMatrix1D right) {

        return left.zDotProduct(right) / (Math.sqrt(left.zDotProduct(left)) * Math.sqrt(right.zDotProduct(right)));

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
        if (parameter.check(map)) {
            parameter.merge(map);

            IODescriptor desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR,
                    (String) parameter.get("Name").get(),
                    (String) parameter.get("SourceMode").get(),
                    null,
                    null,
                    "",
                    false);
            input.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR,
                    (String) parameter.get("Name").get(),
                    (String) parameter.get("ReferenceMode").get(),
                    null,
                    null,
                    "",
                    false);
            input.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.LINK,
                    (String) parameter.get("Name").get(),
                    (String) parameter.get("SourceRelation").get(),
                    null,
                    null,
                    "",
                    false);
            input.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.LINK,
                    (String) parameter.get("Name").get(),
                    (String) parameter.get("DestinationRelation").get(),
                    null,
                    null,
                    "",
                    false);
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.LINK,
                    (String) parameter.get("Name").get(),
                    (String) parameter.get("SimilarityRelation").get(),
                    null,
                    null,
                    "",
                    false);
            output.add(desc);
        }
    }

    public User2User prototype() {
        return new User2User();
    }
}

