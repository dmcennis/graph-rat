/*

 * Created 7-5-08

 * Copyright Daniel McEnnis, see license.txt

 */



package org.mcennis.graphrat.algorithm.similarity;



import java.util.HashMap;

import java.util.HashSet;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;

import java.util.logging.Logger;
import org.dynamicfactory.descriptors.*;

import org.mcennis.graphrat.algorithm.Algorithm;
import org.mcennis.graphrat.algorithm.AlgorithmMacros;
import org.mcennis.graphrat.graph.Graph;


import org.mcennis.graphrat.actor.Actor;

import org.mcennis.graphrat.reusablecores.datavector.DataVector;

import org.mcennis.graphrat.reusablecores.datavector.MapDataVector;


import org.mcennis.graphrat.reusablecores.distance.DistanceFunction;
import org.mcennis.graphrat.descriptors.IODescriptor;


import org.mcennis.graphrat.descriptors.IODescriptor.Type;
import org.mcennis.graphrat.descriptors.IODescriptorFactory;
import org.mcennis.graphrat.descriptors.IODescriptorInternal;

import org.mcennis.graphrat.link.Link;

import org.mcennis.graphrat.link.LinkFactory;

import org.dynamicfactory.model.ModelShell;
import org.mcennis.graphrat.query.ActorQuery;
import org.mcennis.graphrat.query.ActorQueryFactory;
import org.mcennis.graphrat.query.LinkQuery;
import org.mcennis.graphrat.query.LinkQuery.LinkEnd;
import org.mcennis.graphrat.query.LinkQueryFactory;
import org.mcennis.graphrat.query.actor.ActorByMode;
import org.mcennis.graphrat.query.link.LinkByRelation;



/**

 * Determine the similarity between two actors by the data vector implied by the 

 * given relation.  Also can be described as a percentage of role similarity between

 * two actors (Social Network Analysis).

 * 

 * @author Daniel McEnnis

 */

public class SimilarityByLink extends ModelShell implements Algorithm {

    PropertiesInternal parameter = PropertiesFactory.newInstance().create();
    LinkedList<IODescriptor> input = new LinkedList<IODescriptor>();
    LinkedList<IODescriptor> output = new LinkedList<IODescriptor>();

        

    /**

     * Create a new algorithm utilizing default parameters.

     */

    public SimilarityByLink(){
        ParameterInternal name = ParameterFactory.newInstance().create("AlgorithmClass",String.class);
        SyntaxObject syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("Similarity By Link");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Name",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,Integer.MAX_VALUE,null,String.class);
        name.setRestrictions(syntax);
        name.add("Similarity By Link");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Category",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("Similarity");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("LinkFilter",LinkQuery.class);
        syntax = SyntaxCheckerFactory.newInstance().create(0,1,null,LinkQuery.class);
        name.setRestrictions(syntax);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("ActorFilter",ActorQuery.class);
        syntax = SyntaxCheckerFactory.newInstance().create(0,1,null,ActorQuery.class);
        name.setRestrictions(syntax);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("SimilarityFunction",DistanceFunction.class);
        syntax = SyntaxCheckerFactory.newInstance().create(0,1,null,DistanceFunction.class);
        name.setRestrictions(syntax);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("SourceRelation",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("Property");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("DestinationRelation",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("Property");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Threshold",Double.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,Double.class);
        name.setRestrictions(syntax);
        name.add(0.75);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("SourceAppendGraphID",Boolean.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,Boolean.class);
        name.setRestrictions(syntax);
        name.add(false);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("DestinationAppendGraphID",Boolean.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,Boolean.class);
        name.setRestrictions(syntax);
        name.add(false);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("LinkEnd",LinkEnd.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,LinkEnd.class);
        name.setRestrictions(syntax);
        name.add(LinkEnd.SOURCE);
        parameter.add(name);
    }

    

    @Override

    public void execute(Graph g){
        ActorByMode mode = (ActorByMode)ActorQueryFactory.newInstance().create("ActorByMode");
        mode.buildQuery((String)parameter.get("Mode").get(),".*",false);
        Vector<Actor> tags = new Vector<Actor>();
        tags.addAll(AlgorithmMacros.filterActor(parameter, g, mode.execute(g, null, null)));

        DistanceFunction similarity = (DistanceFunction)parameter.get("SimilarityFunction").get();

        if(!tags.isEmpty()){

            HashMap<Actor,Double>[] actorMap = new HashMap[tags.size()];

            HashSet<Actor> actorCount = new HashSet<Actor>();

            DataVector[] dataVectorArray = new DataVector[actorMap.length];
            for(int i=0;i<tags.size();++i){

                actorMap[i] = getMap(tags.get(i),actorCount,g);

//                actorCount.add(e);

                dataVectorArray[i] = new MapDataVector(actorMap[i],tags.size());

            }
            for(int i=0;i<dataVectorArray.length;++i){
                dataVectorArray[i].setSize(actorCount.size());
            }
            for(int i=0;i<dataVectorArray.length;++i){
                for(int j=0;j<dataVectorArray.length;++j){
                    if(i != j){

                        double sim =  similarity.distance(dataVectorArray[i], dataVectorArray[j]); //similarity(actorMap,i,j,actorCount.size(),g);

                        if(sim >= ((Double)parameter.get("Threshold").get()).doubleValue()){

                            Link simLink = LinkFactory.newInstance().create(AlgorithmMacros.getDestID(parameter,g, (String)parameter.get("DestinationRelation").get()));

                            simLink.set(tags.get(i), sim, tags.get(j));

                            g.add(simLink);

                        }

                    }
                }
            }

        }else{

            Logger.getLogger(SimilarityByLink.class.getName()).log(Level.WARNING,"No actors of mode '"+(String)parameter.get("Mode").get()+"' are present");

        }

    }

    

    protected HashMap<Actor,Double> getMap(Actor tag, HashSet<Actor> total, Graph g){
        HashMap<Actor, Double> ret = new HashMap<Actor, Double>();
            LinkedList<Actor> actor = new LinkedList<Actor>();
            actor.add(tag);
            LinkByRelation query = (LinkByRelation)LinkQueryFactory.newInstance().create("LinkByRelation");
            query.buildQuery(AlgorithmMacros.getSourceID(parameter,g, (String)parameter.get("SourceRelation").get()),false);

        if ( (((LinkEnd)parameter.get("ActorEnd").get())==LinkEnd.SOURCE) || (((LinkEnd)parameter.get("ActorEnd").get())==LinkEnd.ALL)) {
            Iterator<Link> link = AlgorithmMacros.filterLink(parameter, g, query, actor, null, null);
                while (link.hasNext()) {
                    Link l = link.next();
                    ret.put(l.getDestination(), l.getStrength());
                    total.add(l.getDestination());
                }
        }
        if ( (((LinkEnd)parameter.get("ActorEnd").get())==LinkEnd.DESTINATION) || (((LinkEnd)parameter.get("ActorEnd").get())==LinkEnd.ALL)) {
            Iterator<Link> link = AlgorithmMacros.filterLink(parameter, g, query, null, actor, null);
                while (link.hasNext()) {
                    Link l = link.next();
                    total.add(l.getSource());
                    if (!ret.containsKey(l.getSource())) {
                        ret.put(l.getSource(), l.getStrength());
                    } else {
                        ret.put(l.getSource(), l.getStrength() + ret.get(l.getSource()));
                    }
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

     * <li/><b>name</b>: Name of the algorithm. Default is 'Simlarity By Link'

     * <li/><b>actorType</b>: Mode of the actor to compare. Default is 'tag'

     * <li/><b>relation</b>: Relation to calculate similarity from. Default is 'Tags'

     * <li/><b>linkDirection</b>: type of link to use- Incoming, Ougoing, or All.

     * Default is 'Outgoing'

     * <li/><b>linkName</b>: Name of generated similarity links.  Default is 'Tag Similarity'

     * <li/><b>threshold</b>: threshold of similarity before a new link is created.

     * Deafult is '0.75'

     * <li/><b>distanceFunction</b>: which distance function to use. Deafult is 

     * 'EuclideanDistance'

     * </ul>

     * @param map parameters to be loaded - may be null

     */

    public void init(Properties map) {
        if(parameter.check(map)){
            parameter.merge(map);
            
            IODescriptorInternal desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR,
                    (String)parameter.get("Name").get(),
                    (String)parameter.get("Mode").get(),
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
                    (Boolean)parameter.get("SourceAppendGraphID").get());
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
        }
    }

    public SimilarityByLink prototype(){
        return new SimilarityByLink();
    }

}

