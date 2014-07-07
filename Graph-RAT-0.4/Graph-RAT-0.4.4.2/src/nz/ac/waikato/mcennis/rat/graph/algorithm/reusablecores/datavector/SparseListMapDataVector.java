/*
 * Created 21/05/2008-15:55:16
 * Copyright Daniel McEnnis, see license.txt
 */
package org.mcennis.graphrat.algorithm.reusablecores.datavector;

import org.mcennis.graphrat.algorithm.reusablecores.datavector.DataVector;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.mcennis.graphrat.util.Duples;

/**
 * Class that creates a dual data vector class fromm a duples-list and a map.  Note
 * that the list portion has linear random access times making some metrics very slow.
 * 
 * @author Daniel McEnnis
 */
public class SparseListMapDataVector {

    List<Duples<? extends Object, Double>> leftSource;
    Map<? extends Object, Double> rightSource;
    LeftVector left;
    RightVector right;

    /**
     * Return the list portion of this object.
     * @return left vector
     */
    public DataVector left() {
        return left;
    }

    /**
     * Return the map portion of this object.
     * @return right vector
     */
    public DataVector right() {
        return right;
    }

    /**
     * Create a pair of data vectors from a list and a map with the given dimensionality.
     * @param left source list of duples
     * @param right map of objects to doubles
     * @param size dimensionality of the set
     */
    public SparseListMapDataVector(List<Duples<? extends Object, Double>> left, Map<? extends Object, Double> right, int size) {
        this.leftSource = left;
        this.rightSource = right;
        this.left = new LeftVector();
        this.left.setSize(size);
        this.right = new RightVector();
        this.right.setSize(size);
    }

    /**
     * Class implementing a list data vector.  Extremely slow (linear) random access times.
     */
    public class LeftVector implements DataVector {

        Iterator<Duples<? extends Object, Double>> it = leftSource.iterator();
        Duples<? extends Object, Double> current = null;
        int size = leftSource.size();

        @Override
        public int size() {
            return size;
        }

        @Override
        public void setSize(int s) {
            size = s;
        }

        @Override
        public double getValue(Object index) {
            Iterator<Duples<? extends Object, Double>> iterator = leftSource.iterator();
            while (iterator.hasNext()) {
                Duples<? extends Object, Double> object = iterator.next();
                if (object.getLeft().equals(index)) {
                    return object.getRight().doubleValue();
                }
            }
            return 0.0;
        }

        @Override
        public void reset() {
            current = null;
            it = leftSource.iterator();
        }

        @Override
        public double getCurrentValue() {
            return current.getRight().doubleValue();
        }

        @Override
        public Object getCurrentIndex() {
            return current.getLeft();
        }

        @Override
        public void next() {
            current = it.next();
        }

        @Override
        public boolean hasNext() {
            return it.hasNext();
        }
    }

    /**
     * Class representing a map data vector.
     */
    public class RightVector implements DataVector {

        Iterator<? extends Object> it = rightSource.keySet().iterator();
        Object current = null;
        int size = rightSource.size();

        @Override
        public int size() {
            return size;
        }

        @Override
        public void setSize(int s) {
            size = s;
        }

        @Override
        public double getValue(Object index) {
            return rightSource.get(index).doubleValue();
        }

        @Override
        public void reset() {
            it = rightSource.keySet().iterator();
            current = null;
        }

        @Override
        public double getCurrentValue() {
            return rightSource.get(current).doubleValue();
        }

        @Override
        public Object getCurrentIndex() {
            return current;
        }

        @Override
        public void next() {
            current = it.next();
        }

        @Override
        public boolean hasNext() {
            return it.hasNext();
        }
    }
}
