/**
 * Sep 12, 2008-5:36:56 PM
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.graph.algorithm.aggregators;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import nz.ac.waikato.mcennis.rat.graph.actor.Actor;
import nz.ac.waikato.mcennis.rat.graph.algorithm.Algorithm;
import nz.ac.waikato.mcennis.rat.graph.algorithm.AlgorithmMacros;
import org.dynamicfactory.descriptors.IODescriptorFactory;
import org.dynamicfactory.descriptors.IODescriptor;
import org.dynamicfactory.descriptors.IODescriptor.Type;
import org.dynamicfactory.descriptors.IODescriptorInternal;
import nz.ac.waikato.mcennis.rat.graph.link.Link;
import nz.ac.waikato.mcennis.rat.graph.link.LinkFactory;
import nz.ac.waikato.mcennis.rat.graph.model.ModelShell;
import nz.ac.waikato.mcennis.rat.graph.query.ActorQuery;
import nz.ac.waikato.mcennis.rat.graph.query.ActorQueryFactory;
import nz.ac.waikato.mcennis.rat.graph.query.LinkQuery;
import nz.ac.waikato.mcennis.rat.graph.query.actor.ActorByMode;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author Daniel McEnnis
 */
public class PropertyToLink extends ModelShell implements Algorithm {

    PropertiesInternal parameter = PropertiesFactory.newInstance().create();
    LinkedList<IODescriptor> input = new LinkedList<IODescriptor>();
    LinkedList<IODescriptor> output = new LinkedList<IODescriptor>();

    /**
     * Create a new algorithm utilizing default parameters.
     */
    public PropertyToLink() {
        ParameterInternal name = ParameterFactory.newInstance().create("AlgorithmClass", String.class);
        SyntaxObject syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("Property To Link");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Name", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, Integer.MAX_VALUE, null, String.class);
        name.setRestrictions(syntax);
        name.add("Property To Link");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Category", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("Aggregator");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("LinkFilter", LinkQuery.class);
        syntax = SyntaxCheckerFactory.newInstance().create(0, 1, null, LinkQuery.class);
        name.setRestrictions(syntax);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("ActorFilter", ActorQuery.class);
        syntax = SyntaxCheckerFactory.newInstance().create(0, 1, null, ActorQuery.class);
        name.setRestrictions(syntax);
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

        name = ParameterFactory.newInstance().create("SourceMode", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("tag");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("DestinationMode", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("tag");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Property", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("Property");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Relation", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("Property");
        parameter.add(name);
    }

    @Override
    public void execute(Graph g) {
        ActorByMode mode = (ActorByMode) ActorQueryFactory.newInstance().create("ActorByMode");
        mode.buildQuery((String) parameter.get("SourceMode").get(),".*",false);
        Iterator<Actor> actorIt = AlgorithmMacros.filterActor(parameter, g, mode, null, null);

        if (actorIt.hasNext()) {
            while (actorIt.hasNext()) {
                Actor actor = actorIt.next();
                Property linkSource = actor.getProperty(AlgorithmMacros.getDestID(parameter,g, (String)parameter.get("Property").get()));
                if (linkSource != null) {
                    List values = linkSource.getValue();
                    if (values.size() > 0) {
                        Instance data = (Instance) values.get(0);
                        Instances meta = data.dataset();
                        LinkedList<Actor> base = new LinkedList<Actor>();
                        for (int j = 0; j < meta.numAttributes(); ++j) {
                            if (!Double.isNaN(data.value(j))) {
                                Actor destination = g.getActor((String) parameter.get("DestinationMode").get(), meta.attribute(j).name());
                                if (destination != null) {
                                    base.add(destination);
                                    Collection<Actor> ret = AlgorithmMacros.filterActor(parameter, g, base);
                                    if (ret.size() > 0) {
                                        Link link = LinkFactory.newInstance().create((String) parameter.get("Relation").get());
                                        link.set(actor, data.value(j), ret.iterator().next());
                                        g.add(link);
                                    }
                                    base.clear();
                                }
                            }
                        }
                    } else {
                        Logger.getLogger(PropertyToLink.class.getName()).log(Level.WARNING, "Property '" + linkSource.getType() + "' on actor '" + actor.getID() + "' has no value");
                    }
                } else {
                    Logger.getLogger(PropertyToLink.class.getName()).log(Level.WARNING, "Property '" + (String) parameter.get("Property").get() + "' on actor '" + actor.getID() + "' does not exist");
                }
            }
        } else {
            Logger.getLogger(PropertyToLink.class.getName()).log(Level.WARNING, "No actors of mode '" + (String) parameter.get("SourceMode").get() + "' were found");
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
    public Parameter getParameter(
            String param) {
        return parameter.get(param);
    }

    public void init(Properties map) {
        if (parameter.check(map)) {
            parameter.merge(map);

            IODescriptorInternal desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR_PROPERTY,
                    (String) parameter.get("Name").get(),
                    (String) parameter.get("SourceMode").get(),
                    null,
                    (String) parameter.get("Property").get(),"");
                if((Boolean)parameter.get("SourceAppendGraphID").get()){
                    desc.setAppendGraphID(true);
                }
            input.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.LINK,
                    (String) parameter.get("Name").get(),
                    (String) parameter.get("Relation").get(),
                    null,
                    null,"");
                if((Boolean)parameter.get("DestinationAppendGraphID").get()){
                    desc.setAppendGraphID(true);
                }
            output.add(desc);
        }
    }

    public PropertyToLink prototype() {
        return new PropertyToLink();
    }
}
