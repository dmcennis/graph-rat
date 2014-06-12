/*

 * Created 21/05/2008-16:26:38

 * Copyright Daniel McEnnis, see license.txt

 */

package nz.ac.waikato.mcennis.rat.reusablecores.datavector;



import java.util.Collections;

import nz.ac.waikato.mcennis.rat.reusablecores.datavector.DataVector;

import java.util.Iterator;

import java.util.List;

import java.util.Set;

import java.util.TreeSet;



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



        Iterator<? extends Comparable> it = (Iterator<? extends Comparable>) leftSource.iterator();

        Comparable current = null;

        boolean sorted = false;

        int size = leftSource.size();

        List<? extends Comparable> source = (List<? extends Comparable>) leftSource;



        @Override

        public int size() {

            return size;

        }



        @Override

        public void setSize(int s) {

            size = s;

        }



        @Override

        public double getValue(Comparable index) {

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

            it = (Iterator<? extends Comparable>) leftSource.iterator();

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

        public Comparable getCurrentIndex() {

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



        public int compareTo(Object o) {

            if (this.getClass().getName().compareTo(o.getClass().getName()) == 0) {

                LeftVector right = (LeftVector) o;

                if (size - right.size != 0) {

                    return size - right.size;

                }

                if (source.size() - right.source.size() != 0) {

                    return source.size() - right.source.size();

                }

                Iterator leftIt = source.iterator();

                Iterator rightIt = right.source.iterator();

                while (leftIt.hasNext()) {

                    Comparable leftO = (Comparable) leftIt.next();

                    Comparable rightO = (Comparable) rightIt.next();

                    if (leftO.compareTo(rightO) != 0) {

                        return leftO.compareTo(rightO);

                    }

                }

                return 0;

            } else {

                return this.getClass().getName().compareTo(o.getClass().getName());

            }

        }

    }



    /**

     * Data vector backed by a Set

     */

    public class RightVector implements DataVector {



        Iterator<? extends Comparable> it = (Iterator<? extends Comparable>) rightSource.iterator();

        Comparable value = null;

        int size = rightSource.size();

        Set<? extends Comparable> source = (Set<? extends Comparable>) rightSource;



        @Override

        public int size() {

            return size;

        }



        @Override

        public void setSize(int s) {

            size = s;

        }



        @Override

        public double getValue(Comparable index) {

            if (rightSource.contains(index)) {

                return 1.0;

            } else {

                return 0.0;

            }

        }



        @Override

        public void reset() {

            it = (Iterator<? extends Comparable>) rightSource.iterator();

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

        public Comparable getCurrentIndex() {

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



        public int compareTo(Object o) {

            if (this.getClass().getName().compareTo(o.getClass().getName()) == 0) {

                RightVector right = (RightVector) o;

                if (size - right.size != 0) {

                    return size - right.size;

                }

                if (source.size() - right.source.size() != 0) {

                    return source.size() - right.source.size();

                }

                TreeSet<? extends Comparable> leftTree = new TreeSet(source);

                TreeSet<? extends Comparable> rightTree = new TreeSet(right.source);

                Iterator leftIt = leftTree.iterator();

                Iterator rightIt = rightTree.iterator();

                while (leftIt.hasNext()) {

                    Comparable leftO = (Comparable) leftIt.next();

                    Comparable rightO = (Comparable) rightIt.next();

                    if (leftO.compareTo(rightO) != 0) {

                        return leftO.compareTo(rightO);

                    }

                }

                return 0;

            } else {

                return this.getClass().getName().compareTo(o.getClass().getName());

            }

        }

    }

}

