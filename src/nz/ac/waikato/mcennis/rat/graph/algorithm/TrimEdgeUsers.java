/*
 * TrimEdgeUsers.java
 *
 * Created on 12 June 2007, 16:55
 *
 * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt)
 */
package nz.ac.waikato.mcennis.rat.graph.algorithm;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import nz.ac.waikato.mcennis.rat.graph.Graph;


import nz.ac.waikato.mcennis.rat.graph.actor.Actor;


import org.dynamicfactory.descriptors.IODescriptorFactory;
import org.dynamicfactory.descriptors.IODescriptor;




import org.dynamicfactory.descriptors.IODescriptor.Type;
import org.dynamicfactory.descriptors.IODescriptorInternal;
import org.dynamicfactory.descriptors.Parameter;


import org.dynamicfactory.descriptors.ParameterFactory;
import org.dynamicfactory.descriptors.ParameterInternal;
import org.dynamicfactory.descriptors.Properties;
import org.dynamicfactory.descriptors.PropertiesFactory;
import org.dynamicfactory.descriptors.PropertiesInternal;
import org.dynamicfactory.descriptors.SyntaxCheckerFactory;
import org.dynamicfactory.descriptors.SyntaxObject;
import nz.ac.waikato.mcennis.rat.graph.link.Link;
import nz.ac.waikato.mcennis.rat.graph.model.ModelShell;
import nz.ac.waikato.mcennis.rat.graph.query.ActorQueryFactory;
import nz.ac.waikato.mcennis.rat.graph.query.LinkQuery.LinkEnd;
import nz.ac.waikato.mcennis.rat.graph.query.LinkQueryFactory;
import nz.ac.waikato.mcennis.rat.graph.query.actor.ActorByMode;
import nz.ac.waikato.mcennis.rat.graph.query.link.LinkByRelation;
import nz.ac.waikato.mcennis.rat.scheduler.Scheduler;

/**
 * Class for removing all actors of a given type (mode) that have no outgoing links
 *
 * @author Daniel McEnnis
 *
 */
public class TrimEdgeUsers extends ModelShell implements Algorithm {

    public static final long serialVersionUID = 3;
    PropertiesInternal parameter = PropertiesFactory.newInstance().create();
    LinkedList<IODescriptor> input = new LinkedList<IODescriptor>();
    LinkedList<IODescriptor> output = new LinkedList<IODescriptor>();


    /** Creates a new instance of TrimEdgeUsers */
    public TrimEdgeUsers() {
        ParameterInternal name = ParameterFactory.newInstance().create("AlgorithmClass", String.class);
        SyntaxObject syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("Degree Centrality");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Name", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, Integer.MAX_VALUE, null, String.class);
        name.setRestrictions(syntax);
        name.add("Degree Centrality");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Category", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("Prestige");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Relation", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("tag");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Mode", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("tag");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Direction", LinkEnd.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, LinkEnd.class);
        name.setRestrictions(syntax);
        name.add(LinkEnd.SOURCE);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("TrimByNullLink", Boolean.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, Boolean.class);
        name.setRestrictions(syntax);
        name.add(true);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("TrimByNullProperty", Boolean.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, Boolean.class);
        name.setRestrictions(syntax);
        name.add(true);
        parameter.add(name);
    }

    /**
     * Remove actors without outgoing links.
     */
    public void execute(Graph g) {
        ActorByMode mode = (ActorByMode)ActorQueryFactory.newInstance().create("ActorByMode");
        mode.buildQuery((String)parameter.get("Mode").get(),".*",false);

        LinkByRelation relation = (LinkByRelation)LinkQueryFactory.newInstance().create("LinkByRelation");
        relation.buildQuery((String)parameter.get("Relation").get(),false);


        Iterator<Actor> masterList = mode.executeIterator(g, null, null);
        fireChange(Scheduler.SET_ALGORITHM_COUNT,g.getActorCount((String) parameter.get("Mode").get()));
        int actorCount=0;
        while (masterList.hasNext()) {

            LinkedList<Actor> source = new LinkedList<Actor>();

            Actor actor = masterList.next();
            source.add(actor);
            boolean remove = true;
            if(((Boolean)parameter.get("TrimByNullLink").get())&&(((LinkEnd)parameter.get("Direction").get() == LinkEnd.SOURCE)||((LinkEnd)parameter.get("Direction").get() == LinkEnd.ALL))){
                Iterator<Link> test = relation.executeIterator(g, source, null, null);
                if(test.hasNext()){
                   remove = false;
                }
            }else if(((Boolean)parameter.get("TrimByNullLink").get())&&(((LinkEnd)parameter.get("Direction").get() == LinkEnd.DESTINATION)||((LinkEnd)parameter.get("Direction").get() == LinkEnd.ALL))){
                Iterator<Link> test = relation.executeIterator(g, null,source, null);
                if(test.hasNext()){
                   remove = false;
                }
            }

            if(((Boolean)parameter.get("TrimByNullProperty").get())&&(!actor.getProperty().isEmpty())){
                remove = false;
            }
            if(remove){
                g.remove(actor);
            }
 
            fireChange(Scheduler.SET_ALGORITHM_PROGRESS,actorCount++);
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
     * Parameters to be initialized. 
     * 
     * <ol>
     * <li>'name' - name of this instance of the algorithm. Default 'Trim Edge Actors'
     * <li>'relation' - type (relation) of link to compute over. Default 'Knows'.
     * <li>'actorType' - type (mode) of actor to compute over. Default 'User'.
     * </ol>
     * <br>
     * <br>Input 0 - Actor
     * <br>Output 0 - Actor
     * 
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
                    (String)parameter.get("Relation").get(),
                    null,
                    null,
                    "",
                    false);
            input.add(desc);

        }
    }

    public TrimEdgeUsers prototype(){
        return new TrimEdgeUsers();
    }
}


