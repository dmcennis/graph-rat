/**
 * AssociativeMiningItemsXMLFactory
 * Created Jan 17, 2009 - 12:21:04 AM
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.graph.property.xml;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import nz.ac.waikato.mcennis.rat.XMLParserObject.State;
import nz.ac.waikato.mcennis.rat.graph.algorithm.collaborativefiltering.AssociativeMiningItems;
import nz.ac.waikato.mcennis.rat.graph.actor.Actor;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import nz.ac.waikato.mcennis.rat.graph.property.PropertyFactory;
import nz.ac.waikato.mcennis.rat.graph.query.LinkQuery.LinkEnd;
import nz.ac.waikato.mcennis.rat.graph.query.LinkQueryFactory;
import nz.ac.waikato.mcennis.rat.graph.query.link.LinkByRelation;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author Daniel McEnnis
 */
public class AssociativeMiningItemsXML implements PropertyValueXML<AssociativeMiningItems> {

    AssociativeMiningItems item = null;
    LinkedList<Actor> actor = new LinkedList<Actor>();
    boolean not = false;
    String mode = "";
    String id = "";
    String relation = "";
    double significance = Double.NaN;
    LinkEnd end = null;

    enum InternalState {

        START, NOT, ACTOR, MODE, ID, RELATION, LINK_END, SIGNIFICANCE
    };
    InternalState internalState = InternalState.START;
    State state = State.UNINITIALIZED;

    public AssociativeMiningItems getProperty() {
        return item;
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (state == State.UNINITIALIZED) {
            state = State.LOADING;
        } else if (localName.equalsIgnoreCase("Negative") || qName.equalsIgnoreCase("Negative")) {
            internalState = InternalState.NOT;
            not = true;
        } else if (localName.equalsIgnoreCase("Actor") || qName.equalsIgnoreCase("Actor")) {
            internalState = InternalState.ACTOR;
        } else if (localName.equalsIgnoreCase("Mode") || qName.equalsIgnoreCase("Mode")) {
            internalState = InternalState.MODE;
        } else if (localName.equalsIgnoreCase("ID") || qName.equalsIgnoreCase("ID")) {
            internalState = InternalState.ID;
        } else if (localName.equalsIgnoreCase("Relation") || qName.equalsIgnoreCase("Relation")) {
            internalState = InternalState.RELATION;
        } else if (localName.equalsIgnoreCase("Significance") || qName.equalsIgnoreCase("Significance")) {
            internalState = InternalState.SIGNIFICANCE;
        } else if (localName.equalsIgnoreCase("LinkEnd") || qName.equalsIgnoreCase("LinkEnd")) {
            internalState = InternalState.LINK_END;
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        if (internalState == InternalState.MODE) {
            mode = new String(ch, start, length);
        } else if (internalState == InternalState.ID) {
            id = new String(ch, start, length);
        } else if (internalState == InternalState.RELATION) {
            relation = new String(ch, start, length);
        } else if (internalState == InternalState.SIGNIFICANCE) {
            significance = Double.parseDouble(new String(ch, start, length));
        }else if(internalState == InternalState.LINK_END){
            end = parseLinkEnd(new String(ch,start,length));
        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (internalState == InternalState.NOT) {
            internalState = InternalState.START;
        } else if (internalState == InternalState.ACTOR) {
            internalState = InternalState.START;
            Iterator it = PropertyFactory.newInstance().getParameter("GraphRoot").getValue().iterator();
            Actor a = null;
            if(it.hasNext()){
                a = ((Graph)it.next()).getActor(mode, id);
            }else{
                // a = ActorFactory.create(mode,id);
            }
            if (a != null) {
                actor.add(a);
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Actor reference with mode '" + mode + "' and id '" + id + "' does not exist");
            }
        } else if (internalState == InternalState.MODE) {
            internalState = InternalState.ACTOR;
        } else if (internalState == InternalState.ID) {
            internalState = InternalState.ACTOR;
        } else if (internalState == InternalState.RELATION) {
            internalState = InternalState.START;
        } else if (internalState == InternalState.SIGNIFICANCE) {
            internalState = InternalState.START;
        } else if (internalState == InternalState.LINK_END) {
            internalState = InternalState.START;
        } else if (internalState == InternalState.START) {
            LinkByRelation linkQuery = (LinkByRelation)LinkQueryFactory.newInstance().create("LinkByRelation");
            linkQuery.buildQuery(relation,false);
            item = new AssociativeMiningItems(actor, linkQuery, end,significance);
            item.setPositive(!not);
            state = State.READY;
        }
    }

    public String getClassName() {
        return "AssociativeMiningItems";
    }

    public State buildingStatus() {
        return state;
    }

    public AssociativeMiningItemsXML newCopy() {
        return new AssociativeMiningItemsXML();
    }

    public void setProperty(AssociativeMiningItems type) {
        item = type;
    }

    public void export(Writer writer) throws IOException {
        writer.append("<AssociativeMiningItems>\n");
        if (!item.isPositive()) {
            writer.append("\t</Not>\n");
        }
        writer.append("\t<Significance>").append(Double.toString(significance)).append("</Significance>\n");
        Actor[] actors = item.getActors();
        if (actors != null) {
            for (int i = 0; i < actors.length; ++i) {
                writer.append("\t<Actor>\n");
                writer.append("\t\t<Mode>").append(actors[i].getMode()).append("</Mode>\n");
                writer.append("\t\t<ID>").append(actors[i].getID()).append("</ID>\n");
                writer.append("\t</Actor>\n");
            }
        }
        writer.append("</AssociativeMiningItems>\n");
    }

    LinkEnd parseLinkEnd(String type){
       if(type.equalsIgnoreCase("ALL")){
           return LinkEnd.ALL;
       } else if(type.equalsIgnoreCase("SOURCE")){
           return LinkEnd.SOURCE;
       } else if(type.equalsIgnoreCase("DESTINATION")){
           return LinkEnd.DESTINATION;
       }else{
           Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Unknown LinkQuery.LinkEnd type '"+type+"'");
           return LinkEnd.ALL;
       }
    }
}
