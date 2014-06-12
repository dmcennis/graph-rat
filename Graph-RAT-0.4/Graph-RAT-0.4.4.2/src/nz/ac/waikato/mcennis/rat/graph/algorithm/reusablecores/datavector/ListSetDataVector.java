/*
 * Created 21/05/2008-16:26:38
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.graph.algorithm.reusablecores.datavector;

import java.util.Collections;
import nz.ac.waikato.mcennis.rat.graph.algorithm.reusablecores.datavector.DataVector;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Create a pair of data vectors backed by a list and set combination. Note that some
 * functions will be very slow as random access to lists are linear operations.
 * 
 * @author Daniel McEnnis
 */
public class ListSetDataVector {

    List<? extends Object> leftSource;
    Set<? extends Object> rightSource;
    LeftVector left;
    RightVector right;

    /**
     * Get the left side of this data vector pair
     * @return left data vector
     */
    public DataVector left() {
        return left;
    }

    /**
     * Get the right side of this data vector pair
     * @return right data vector
     */
    public DataVector right() {
        return right;
    }

    /**
     * Creates two new data vectors from a list and a set. Assumes binary exists 
     * or not exists as the values for each entry.
     * @param left
     * @param right
     */
    public ListSetDataVector(List<? extends Object> left, Set<? extends Object> right, int size) {
        this.leftSource = left;
        this.rightSource = right;
        this.left = new LeftVector();
        this.right = new RightVector();
        this.left.setSize(size);
        this.right.setSize(size);
    }

    /**
     * data vector backed by a list
     */
    public class LeftVector implements DataVector {

        Iterator<? extends Object> it = leftSource.iterator();
        Object current = null;        
        boolean sorted = false;
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
            Iterator<? extends Object> iterator = leftSource.iterator();
            while (iterator.hasNext()) {
                Object item = iterator.next();
                if (item.equals(index)) {
                    return 1.0;
                }
            }
            return 0.0;
        }

        @Override
        public void reset() {
            it = leftSource.iterator();
            current = null;
        }

        @Override
        public double getCurrentValue() {
            if (current != null) {
                return 1.0;
            } else {
                return 0.0;
            }
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

    /**
     * Data vector backed by a Set
     */
    public class RightVector implements DataVector {

        Iterator<? extends Object> it = rightSource.iterator();
        Object value = null;
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
            if (rightSource.contains(index)) {
                return 1.0;
            } else {
                return 0.0;
            }
        }

        @Override
        public void reset() {
            it = rightSource.iterator();
            value = null;
        }

        @Override
        public double getCurrentValue() {
            if (value != null) {
                return 1.0;
            } else {
                return 0.0;
            }
        }

        @Override
        public Object getCurrentIndex() {
            return value;
        }

        @Override
        public void next() {
            value = it.next();
        }

        @Override
        public boolean hasNext() {
            return it.hasNext();
        }
    }
}
