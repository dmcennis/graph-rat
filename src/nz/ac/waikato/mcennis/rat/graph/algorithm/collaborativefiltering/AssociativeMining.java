/*

 * Created 3-2-08

 * Copyright Daniel McEnnis, see license.txt

 */
package nz.ac.waikato.mcennis.rat.graph.algorithm.collaborativefiltering;

import java.util.HashSet;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import java.util.logging.Logger;

import nz.ac.waikato.mcennis.rat.graph.Graph;

import nz.ac.waikato.mcennis.rat.graph.actor.Actor;

import nz.ac.waikato.mcennis.rat.graph.algorithm.Algorithm;
import org.dynamicfactory.descriptors.*;
import org.dynamicfactory.property.Property;
import org.dynamicfactory.property.PropertyFactory;
import org.dynamicfactory.property.InvalidObjectTypeException;

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

    PropertiesInternal parameter = PropertiesFactory.newInstance().create();
    LinkedList<IODescriptor> input = new LinkedList<IODescriptor>();
    LinkedList<IODescriptor> output = new LinkedList<IODescriptor>();

    public AssociativeMining() {
        ParameterInternal name = ParameterFactory.newInstance().create("AlgorithmClass", String.class);
        SyntaxObject syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("Associative Mining");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Name", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, Integer.MAX_VALUE, null, String.class);
        name.setRestrictions(syntax);
        name.add("Associative Mining");
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

        name = ParameterFactory.newInstance().create("DestinationAppendGraphID", Boolean.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, Boolean.class);
        name.setRestrictions(syntax);
        name.add(false);
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

        name = ParameterFactory.newInstance().create("DestinationProperty", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("Property");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("LinkDirection", LinkEnd.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, LinkEnd.class);
        name.setRestrictions(syntax);
        name.add(LinkEnd.SOURCE);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("PositiveSignificance", Double.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, Double.class);
        name.setRestrictions(syntax);
        name.add(0.05);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("NegativeSignificance", Double.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, Double.class);
        name.setRestrictions(syntax);
        name.add(0.000001);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("MaxDepth", Integer.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, Integer.class);
        name.setRestrictions(syntax);
        name.add(5);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("StoreAssociations", Boolean.class);
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

        // create actor list

        ActorByMode artistQuery = (ActorByMode) ActorQueryFactory.newInstance().create("ActorByMode");
        artistQuery.buildQuery((String) parameter.get("SourceMode").get(),".*",false);
        Iterator<Actor> outerArtist = AlgorithmMacros.filterActor(parameter, g, artistQuery, null, null);
        Iterator<Actor> innerArtist = null;

//        Actor[] artists = g.getActor((String) parameter[3].getValue());

        // create user list

        ActorByMode userQuery = (ActorByMode) ActorQueryFactory.newInstance().create("ActorByMode");
        userQuery.buildQuery((String) parameter.get("ReferenceMode").get(),".*",false);

//        Actor[] users = g.getActor((String) parameter[2].getValue());
        AndLinkQuery and = (AndLinkQuery)LinkQueryFactory.newInstance().create("AndActorQuery");
        LinkedList<LinkQuery> source = new LinkedList<LinkQuery>();

        if(parameter.get("LinkFilter").getValue().size()>0){
            source.add((LinkQuery)parameter.get("LinkFilter").get());
        }

        LinkByRelation relation = (LinkByRelation)LinkQueryFactory.newInstance().create("LinkByRelation");
        relation.buildQuery((String)parameter.get("SourceRelation").get(), false);
        source.add(relation);

        LinkByActor actorLink = (LinkByActor)LinkQueryFactory.newInstance().create("LinkByActor");
        if(((LinkEnd)parameter.get("LinkEnd").get()).equals(LinkEnd.SOURCE)){
            actorLink.buildQuery(false, artistQuery, userQuery, SetOperation.AND);
        }else{
            actorLink.buildQuery(false, userQuery, artistQuery, SetOperation.AND);
        }
        source.add(actorLink);
        and.buildQuery(source);


        int artistCount = g.getActorCount((String) parameter.get("SourceMode").get());
        int userCount = g.getActorCount((String) parameter.get("TargetMode").get());

        LinkByRelation linkQuery = (LinkByRelation) LinkQueryFactory.newInstance().create("LinkByRelation");
        linkQuery.buildQuery((String) parameter.get("SourceRelation").get(), false);

        if ((artistCount > 0) && (userCount > 0)) {

            fireChange(Scheduler.SET_ALGORITHM_COUNT, g.getActorCount((String) parameter.get("SourceMode").get()));

            // for every artist, calculate all significant correlations
            int count = 0;
            while (outerArtist.hasNext()) {
                Actor outer = outerArtist.next();
                Logger.getLogger(AssociativeMining.class.getName()).log(Level.FINE, "Tag '" + outer.getID() + "' - " + count + "/" + artistCount);

                // set up with first order correlations

                AssociativeMiningItemSetGroup group = new AssociativeMiningItemSetGroup(g, linkQuery,(LinkEnd) parameter.get("SourceLinkEnd").get());
                innerArtist = AlgorithmMacros.filterActor(parameter, g, artistQuery, null, null);
                while (innerArtist.hasNext()) {
                    Actor inner = innerArtist.next();
                    if (outer != inner) {

                        AssociativeMiningItems base = new AssociativeMiningItems(g, and, (LinkEnd)parameter.get("LinkEnd").get(),inner);

                        base.setPositiveSignificance(((Double) parameter.get("PositiveSignificance").get()).doubleValue());

                        base.setNegativeSignificance(((Double) parameter.get("NegativeSignificance").get()).doubleValue());

                        int result = base.significanceTest(outer);

                        if (result > 0) {

                            group.addPositiveBase(inner, base);

                        } else if (result < 0) {

                            base.setPositive(false);

                            group.addNegativeBase(inner, base);

                        }

                    }

                }



                int depth = 1;

                // set up the rest of the system

                while ((depth < ((Integer) parameter.get("MaxDepth").get()).intValue()) && (group.expandSet(outer) > 0)) {

                    depth++;

                }

                if (((Boolean) parameter.get("StoreAssociations").get()).booleanValue() == true) {
                    Property property = PropertyFactory.newInstance().create("BasicProperty",AlgorithmMacros.getDestID(parameter, g, (String) parameter.get("DestinationProperty").get()), AssociativeMiningItems.class);

                    AssociativeMiningItems[] items = group.exportAssociations();

                    if (items.length > 0) {

                        try {

                            for (int j = 0; j < items.length; ++j) {

                                property.add(items[j]);

                            }

                            outer.add(property);

                        } catch (InvalidObjectTypeException ex) {

                            Logger.getLogger(AssociativeMining.class.getName()).log(Level.SEVERE, null, ex);

                        }

                    }

                }

                AssociativeMiningItems[] set = group.exportAssociations();
                Iterator<Actor> usersIt = AlgorithmMacros.filterActor(parameter, g, userQuery, null, null);
                while (usersIt.hasNext()) {
                    Actor user = usersIt.next();
                    LinkedList<Actor> sourceRestriction = new LinkedList<Actor>();
                    sourceRestriction.add(outer);
                    LinkedList<Actor> destinationRestriction = new LinkedList<Actor>();
                    destinationRestriction.add(user);
                    double strength = 0.0;

                    HashSet<Actor> given = getGiven(g, destinationRestriction,linkQuery);

                    Iterator<Link> l = null;
                    if (((LinkEnd) parameter.get("SourceLinkEnd").get()).equals(LinkEnd.SOURCE)) {
                        l = AlgorithmMacros.filterLink(parameter, g, linkQuery, sourceRestriction, destinationRestriction, null);
                    } else {
                        l = AlgorithmMacros.filterLink(parameter, g, linkQuery, destinationRestriction, sourceRestriction, null);
                    }
                    if ((!l.hasNext()) || (((Boolean) parameter.get("NewRecommendationsOnly").get()).booleanValue() == false)) {

                        for (int s = 0; s < set.length; ++s) {

                            if (set[s].applies(given)) {

                                strength += 1.0;

                            }

                        }

                    }

                    if (strength > 0.0) {

                        Link link = LinkFactory.newInstance().create(AlgorithmMacros.getDestID(parameter,g,(String) parameter.get("DestinationRelation").get()));
                        if(((LinkEnd)parameter.get("SourceLinkEnd").get()).equals(LinkEnd.SOURCE)){
                            link.set(outer, strength, user);
                        }else{
                            link.set(user, strength, outer);
                        }
                        g.add(link);

                    }

                }

                fireChange(Scheduler.SET_ALGORITHM_PROGRESS, count++);

            }

        } else {

            if (artistCount == 0) {

                Logger.getLogger(AssociativeMining.class.getName()).log(Level.WARNING, "No actors of mode '" + (String) parameter.get("SourceMode").get() + "' are found in graph '" + g.getID() + "'");

            }

            if (userCount == 0) {

                Logger.getLogger(AssociativeMining.class.getName()).log(Level.WARNING, "No actors of mode '" + (String) parameter.get("TargetMode").get() + "' are found in graph '" + g.getID() + "'");

            }

        }

    }

    protected HashSet<Actor> getGiven(Graph g, LinkedList<Actor> u, LinkQuery linkQuery) {

        HashSet<Actor> ret = new HashSet<Actor>();
        Iterator<Link> l = null;
        if (((LinkEnd) parameter.get("SourceLinkEnd").get()).equals(LinkEnd.SOURCE)) {
            l = AlgorithmMacros.filterLink(parameter, g, linkQuery, null, u, null);
            while (l.hasNext()) {
                ret.add(l.next().getSource());
            }
        } else {
            l = AlgorithmMacros.filterLink(parameter, g, linkQuery, u, null, null);
            while (l.hasNext()) {
                ret.add(l.next().getDestination());
            }
        }

        return ret;

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
                    (String)parameter.get("TargetMode").get(),
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
                    (Boolean)parameter.get("DestinationAppendGraphID").get());
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR_PROPERTY,
                    (String)parameter.get("Name").get(),
                    (String)parameter.get("SourceMode").get(),
                    null,
                    (String)parameter.get("DestinationProperty").get(),
                    "",
                    (Boolean)parameter.get("DestinationAppendGraphID").get());
            output.add(desc);
        }
    }

    public AssociativeMining prototype(){
        return new AssociativeMining();
    }
}



