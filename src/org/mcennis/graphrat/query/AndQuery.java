/**
 * GraphCompositeQuery
 * Created Jan 5, 2009 - 7:46:57 PM
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
public abstract class AndQuery<Type extends Comparable> implements Comparable {

    LinkedList entries = new LinkedList();
    transient State state = State.UNINITIALIZED;
    String type = "Graph";

    protected SortedSet<Type> execute(SortedSet<Type> restriction) {
        SortedSet<Type> base = new TreeSet<Type>();
        if (entries.size() == 0) {
            return base;
        }
        Iterator it = entries.iterator();
        base.addAll(executeComponent(it.next()));
        while (it.hasNext()) {
            base.retainAll(executeComponent(it.next()));
        }
        base.retainAll(restriction);
        return base;
    }

    protected Iterator<Type> executeIterator(SortedSet<Type> restriction) {
        return new AndIterator<Type>(restriction);
    }

    protected abstract SortedSet<Type> executeComponent(Object query);

    protected abstract Iterator<Type> executeIterator(Object query);

    protected abstract void exportQuery(Object object, Writer writer) throws IOException;

    public void exportQuery(Writer writer) throws IOException {
        writer.append("<And Class=\"" + type + "\">\n");
        Iterator it = entries.iterator();
        while (it.hasNext()) {
            exportQuery(it.next(), writer);
        }
        writer.append("</And>\n");
    }


    protected void buildQuery(Collection source, String type) {
        state = State.LOADING;
        entries.addAll(source);
        this.type = type;
        state = State.READY;
    }

    public int compareTo(Object o) {
        if (o.getClass().getName().contentEquals(this.getClass().getName())) {
            AndQuery query = (AndQuery) o;
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

    public class AndIterator<Type extends Comparable> implements Iterator<Type> {

        Vector<Iterator<Type>> iterators = new Vector<Iterator<Type>>();
        Vector<Type> nextEntries = new Vector<Type>();
        Type next = null;
        boolean remaining = true;
        SortedSet<Type> restriction;

        public AndIterator(SortedSet<Type> r) {
            Iterator it = entries.iterator();
            restriction = r;
            while (it.hasNext()) {
                iterators.add((Iterator<Type>) executeIterator(it.next()));
            }
            for (int i = 0; i < iterators.size(); ++i) {
                if (iterators.get(i).hasNext()) {
                    nextEntries.set(i, iterators.get(i).next());
                } else {
                    remaining = false;
                }
            }
        }

        public boolean hasNext() {
            if (remaining) {
                boolean done = false;
                next = nextEntries.get(0);
                while (!done) {
                    done = true;
                    for (int i = 0; i < nextEntries.size(); ++i) {
                        if (nextEntries.get(i).compareTo(next) < 0) {
                            while (nextEntries.get(i).compareTo(next) < 0) {
                                if (iterators.get(i).hasNext()) {
                                    nextEntries.set(i, iterators.get(i).next());
                                    done = false;
                                } else {
                                    remaining = false;
                                    return false;
                                }
                            }
                        } else if (nextEntries.get(i).compareTo(next) > 0) {
                            next = nextEntries.get(i);
                            done = false;
                        }
                    }
                    if (done && (restriction!=null) && !restriction.contains(next)) {
                        done = false;
                        if (iterators.get(0).hasNext()) {
                            nextEntries.set(0, iterators.get(0).next());
                            next = nextEntries.get(0);
                        } else {
                            remaining = false;
                            return false;
                        }
                    }
                }
                return done;
            } else {
                return false;
            }
        }

        public Type next() {
            if (this.hasNext()) {
                if (this.hasNext()) {
                    Type ret = next;
                    if (iterators.get(0).hasNext()) {
                        nextEntries.set(0, iterators.get(0).next());
                    } else {
                        remaining = false;
                    }
                    return ret;
                }
            }
            return null;
        }

        public void remove() {
            ;
        }
    }
}
