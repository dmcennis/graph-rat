/**
 * XorQuery
 * Created Jan 5, 2009 - 8:32:19 PM
 * Copyright Daniel McEnnis, see license.txt
 */
package org.mcennis.graphrat.query;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.dynamicfactory.propertyQuery.Query.State;
import org.mcennis.graphrat.query.actor.NullActorQuery;
import org.mcennis.graphrat.query.graph.NullGraphQuery;
import org.mcennis.graphrat.query.link.NullLinkQuery;

/**
 *
 * @author Daniel McEnnis
 */
public abstract class XorQuery<Type extends Comparable> implements Comparable {

    Object left = new NullLinkQuery();
    Object right = new NullLinkQuery();
    
    transient State state = State.UNINITIALIZED;
    
    String type = "Graph";
    
    protected Collection<Type> execute(Collection<Type> r) {
        Collection<Type> leftSet = new HashSet<Type>();
        Collection<Type> rightSet = new LinkedList<Type>();
        leftSet.addAll(executeComponent(left));
        
        Collection<Type> common = new LinkedList<Type>();
        common.addAll(leftSet);
        
        rightSet.addAll(executeComponent(right));
        
        common.retainAll(common);
        
        leftSet.addAll(rightSet);
        leftSet.removeAll(common);
        leftSet.retainAll(r);
        return leftSet;
    }

    protected  Iterator<Type> executeIterator(Collection<Type> r){
	return new XorIterator(r);
    }


    protected abstract Collection<Type> executeComponent(Object query);

    protected abstract Iterator<Type> executeIterator(Object query);

    protected abstract void exportQuery(Object object,Writer writer) throws IOException;

    public void exportQuery(Writer writer) throws IOException{
        writer.append("<Xor Class=\""+type+"\">\n");
        exportQuery(left,writer);
        exportQuery(right,writer);
        writer.append("</Xor>\n");
    }

    public void buildQuery(Collection source,String type){
        state = State.LOADING;
        if(source.size()==2){
            Iterator it_source = source.iterator();
            left = it_source.next();
            right = it_source.next();
        }else if(source.size() >2){
            Iterator it = source.iterator();
            left = it.next();
            right = it.next();
            if(it.hasNext()){
                Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "query list has "+source.size()+" entries, but only first two were utilized");
            }
        }else{
            if(type.contains("Graph")){
                left = new NullGraphQuery();
                ((NullGraphQuery)left).buildQuery();
                right = new NullGraphQuery();
                ((NullGraphQuery)right).buildQuery();
            }else if(type.contains("Actor")){
                left = new NullActorQuery();
                ((NullActorQuery)left).buildQuery();
                right = new NullActorQuery();
                ((NullActorQuery)right).buildQuery();
            }else{
                left = new NullLinkQuery();
                ((NullLinkQuery)left).buildQuery();
                right = new NullLinkQuery();
                ((NullLinkQuery)right).buildQuery();
            }
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "query list has "+source.size()+" entries when two are required.  NullQuery's inserted.");
        }
        this.type = type;
        state = State.READY;
    }

    public int compareTo(Object o) {
        if(o.getClass().getName().contentEquals(this.getClass().getName())){
            XorQuery query = (XorQuery)o;
            if(((Comparable)this.left).compareTo(query.left)!=0){
                return ((Comparable)this.left).compareTo(query.left);
            }else {
                return ((Comparable)this.right).compareTo(query.right);
            }
        }else{
            return this.getClass().getName().compareTo(o.getClass().getName());
        }
    }

    public State buildingStatus() {
        return state;
    }

    public class XorIterator<Type extends Comparable> implements Iterator<Type> {

        Iterator<Type> leftIterator = (Iterator<Type>)executeIterator(left);
        Iterator<Type> rightIterator = (Iterator<Type>)executeIterator(right);
        Type leftItem = null;
        Type rightItem = null;
        Type next = null;
        boolean remaining = true;
        Collection<Type> restriction;

        public XorIterator(Collection<Type> r) {
            if(leftIterator.hasNext()){
                leftItem = leftIterator.next();
            }
            if(rightIterator.hasNext()){
                rightItem = rightIterator.next();
            }
            restriction = r;
        }

        public boolean hasNext() {
            if (remaining){
                while(next==null) {
                   if((leftItem==null)&&(rightItem != null)){
                        next = rightItem;
                        rightItem = advance(rightIterator);
                        return true;
                   }else if((rightItem == null)&&(leftItem != null)){
                       next = leftItem;
                       leftItem = advance(leftIterator);
                       return true;
                   }else if((leftItem==null)&&(rightItem==null)){
                        remaining = false;
                        return false;
                   }else{
                       if(leftItem.compareTo(rightItem)==0){
                            leftItem = advance(leftIterator);
                            rightItem = advance(rightIterator);
                            next = null;
                       }else if(leftItem.compareTo(rightItem)<0){
                           next = leftItem;
                           leftItem = advance(leftIterator);
                       }else{
                           next = rightItem;
                           rightItem = advance(rightIterator);
                       }
                   }
                   if((next != null)&& (restriction!=null)&&(!restriction.contains(next))){
                        next = null;
                   }
                }
                return (next != null);
            } else {
                return false;
            }
        }

        public Type next() {
            if(hasNext()){
                Type ret = next;
                next = null;
                return ret;
            }else{
                return null;
            }
        }

        public void remove() {
            ;
        }

        protected Type advance(Iterator<Type> it){
            if(it.hasNext()){
                return it.next();
            }else{
                return null;
            }
        }
    }
}
