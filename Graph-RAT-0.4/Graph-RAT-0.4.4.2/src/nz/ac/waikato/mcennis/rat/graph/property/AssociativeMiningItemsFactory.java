/*
 * Created 12-5-08
 * Copyright Daniel McEnnis, see license.txt
 */

package org.dynamicfactory.property;

import java.util.LinkedList;
import java.util.Properties;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import nz.ac.waikato.mcennis.rat.graph.actor.Actor;
import nz.ac.waikato.mcennis.rat.graph.actor.ActorFactory;
import nz.ac.waikato.mcennis.rat.graph.algorithm.collaborativefiltering.AssociativeMiningItems;

/**
 * Factory for serializing and deserializing AssociativeMiningItems.
 * @author Daniel McEnnis
 */
public class AssociativeMiningItemsFactory implements PropertyValueFactory<AssociativeMiningItems>{

    @Override
    public AssociativeMiningItems importFromString(String data, Graph g) {
        String[] pieces = data.split(",");
        String[] actors = pieces[0].split(";");
        LinkedList<Actor> col = new LinkedList<Actor>();
        for(int i=0;i<actors.length;i+=2){
            Properties props = new Properties();
            props.setProperty("ActorClass", "Basic");
            props.setProperty("ActorType", actors[i]);
            props.setProperty("ActorID", actors[i+1]);
            Actor a = ActorFactory.newInstance().create(props);
            col.add(a);
        }
        AssociativeMiningItems ret = new AssociativeMiningItems(col,pieces[1],Double.parseDouble(pieces[2]));
        ret.setPositive(Boolean.parseBoolean(pieces[3]));
        ret.setPositiveSignificance(Double.parseDouble(pieces[4]));
        ret.setNegativeSignificance(Double.parseDouble(pieces[5]));
        return ret;
    }

    @Override
    public String exportToString(AssociativeMiningItems type, Graph g) {
        return type.toString();
    }

}
