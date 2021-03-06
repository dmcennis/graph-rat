/**
 * OrQuery
 * Created Jan 5, 2009 - 8:32:02 PM
 * Copyright Daniel McEnnis, see license.txt
 */
/*
 *   This file is part of GraphRAT.
 *
 *   GraphRAT is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   GraphRAT is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with GraphRAT.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.mcennis.graphrat.query;

import java.io.IOException;
import java.io.Writer;
import java.util.*;

import org.dynamicfactory.propertyQuery.Query.State;

/**
 *
 * @author Daniel McEnnis
 */
public abstract class OrQuery<Type extends Comparable> implements Comparable {

    transient State state = State.UNINITIALIZED;
    LinkedList entries = new LinkedList();
    String type = "Graph";

    protected SortedSet<Type> execute(SortedSet<Type> restriction) {
        TreeSet<Type> base = new TreeSet<Type>();
        if (entries.size() == 0) {
            return base;
        }
        Iterator it = entries.iterator();
        base.addAll(executeComponent(it.next()));
        while (it.hasNext()) {
            base.addAll(executeComponent(it.next()));
        }
        base.retainAll(restriction);
        return base;
    }

    protected Iterator<Type> executeIterator(SortedSet<Type> r) {
        return new OrIterator<Type>(r);
    }

    protected abstract SortedSet<Type> executeComponent(Object query);

    protected abstract Iterator<Type> executeIterator(Object query);

    protected abstract void exportQuery(Object object, Writer writer) throws IOException;

    public void exportQuery(Writer writer) throws IOException {
        writer.append("<Or Class=\"" + type + "\">\n");
        Iterator it = entries.iterator();
        while (it.hasNext()) {
            exportQuery(it.next(), writer);
        }
        writer.append("</OR>\n");
    }

    protected void buildQuery(Collection source, String type) {
        state = State.LOADING;
        entries.addAll(source);
        this.type = type;
        state = State.READY;
    }

    public int compareTo(Object o) {
        if (o.getClass().getName().contentEquals(this.getClass().getName())) {
            OrQuery query = (OrQuery) o;
            Iterator left = this.entries.iterator();
            Iterator right = query.entries.iterator();
            while (left.hasNext() && right.hasNext()) {
                int compare = ((Comparable) left.next()).compareTo((Comparable) right.next());
                if (compare != 0) {
                    return compare;
                }
            }
            if (left.hasNext()) {
                return -1;
            } else if (right.hasNext()) {
                return 1;
            } else {
                return 0;
            }
        } else {
            return this.getClass().getName().compareTo(o.getClass().getName());
        }
    }

    public State buildingStatus() {
        return state;
    }

    public class OrIterator<Type extends Comparable> implements Iterator<Type> {

        Vector<Iterator<Type>> iterators = new Vector<Iterator<Type>>();
        Vector<Type> nextEntries = new Vector<Type>();
        Type next = null;
        boolean remaining = true;
        SortedSet<Type> restriction;

        public OrIterator(SortedSet<Type> r) {
            Iterator it = entries.iterator();
            while (it.hasNext()) {
                iterators.add((Iterator<Type>) executeIterator(it.next()));
            }
            for (int i = 0; i < iterators.size(); ++i) {
                if (iterators.get(i).hasNext()) {
                    nextEntries.set(i, iterators.get(i).next());
                    if ((next == null) || (nextEntries.get(i).compareTo(i) < 0)) {
                        next = nextEntries.get(i);
                    }
                }
            }
            if (next == null) {
                remaining = false;
            }
            restriction = r;
        }

        public boolean hasNext() {
            if (remaining) {
                if (next != null) {
                    return true;
                } else {
                    boolean done = false;
                    while (!done) {
                        done = true;
                        for (int i = 0; i < nextEntries.size(); ++i) {
                            if (nextEntries.get(i) != null) {
                                if (next == null) {
                                    next = nextEntries.get(i);
                                } else if (nextEntries.get(i).compareTo(next) < 0) {
                                    next = nextEntries.get(i);
                                    done = false;
                                }
                            }
                        }
                        if (done && (restriction!=null) && !restriction.contains(next)) {
                            done = false;
                            next = null;
                        }
                    }
                    if (next != null) {
                        for (int i = 0; i < nextEntries.size(); ++i) {
                            if (nextEntries.get(i).compareTo(next) == 0) {
                                if (iterators.get(i).hasNext()) {
                                    nextEntries.set(i, iterators.get(i).next());
                                } else {
                                    nextEntries.set(i, null);
                                }
                            }
                        }
                        return true;
                    } else {
                        remaining = false;
                        return false;
                    }
                }
            } else {
                return false;
            }
        }

        public Type next() {
            if (next != null) {
                Type ret = next;
                next = null;
                hasNext();
                return ret;
            }
            return null;
        }

        public void remove() {
            ;
        }
    }
}
