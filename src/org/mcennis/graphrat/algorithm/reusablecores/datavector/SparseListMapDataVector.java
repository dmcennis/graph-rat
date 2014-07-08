/*

 * Created 21/05/2008-15:55:16

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

package org.mcennis.graphrat.algorithm.reusablecores.datavector;



import java.util.Collections;

import java.util.Iterator;

import java.util.LinkedList;

import java.util.List;

import java.util.Map;

import java.util.TreeMap;

import org.mcennis.graphrat.util.Duples;



/**

 * Class that creates a dual data vector class fromm a duples-list and a map.  Note

 * that the list portion has linear random access times making some metrics very slow.

 * 

 * @author Daniel McEnnis

 */

public class SparseListMapDataVector {



    List<Duples<? extends Comparable, Double>> leftSource;

    Map<? extends Comparable, Double> rightSource;

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

    public SparseListMapDataVector(List<Duples<? extends Comparable, Double>> left, Map<? extends Comparable, Double> right, int size) {

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



        Iterator<Duples<? extends Comparable, Double>> it = leftSource.iterator();

        Duples<? extends Comparable, Double> current = null;

        int size = leftSource.size();

        List<Duples<? extends Comparable, Double>> source = leftSource;



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

            Iterator<Duples<? extends Comparable, Double>> iterator = leftSource.iterator();

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

        public Comparable getCurrentIndex() {

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



        public int compareTo(Object o) {

            if (this.getClass().getName().compareTo(o.getClass().getName()) == 0) {

                LeftVector right = (LeftVector) o;

                if (this.size - right.size != 0) {

                    return this.size - right.size;

                }

                if (source.size() - right.source.size() != 0) {

                    return source.size() - right.source.size();

                }

                LinkedList<Duples<Comparable, Double>> leftTree = new LinkedList<Duples<Comparable, Double>>();

                Iterator<Duples<? extends Comparable, Double>> itLeftTemp = source.iterator();

                while (itLeftTemp.hasNext()) {

                    leftTree.add((Duples<Comparable, Double>) itLeftTemp.next());

                }

                Collections.sort(leftTree);

                LinkedList<Duples<Comparable, Double>> rightTree = new LinkedList<Duples<Comparable, Double>>();

                Iterator<Duples<? extends Comparable, Double>> itRightTemp = right.source.iterator();

                while (itRightTemp.hasNext()) {

                    rightTree.add((Duples<Comparable, Double>) itRightTemp.next());

                }

                Collections.sort(rightTree);

                Iterator<Duples<Comparable, Double>> leftIt = leftTree.iterator();

                Iterator<Duples<Comparable, Double>> rightIt = rightTree.iterator();

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

     * Class representing a map data vector.

     */

    public class RightVector implements DataVector {



        Iterator<? extends Comparable> it = rightSource.keySet().iterator();

        Comparable current = null;

        int size = rightSource.size();

        Map<? extends Comparable, Double> source = rightSource;



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

                RightVector right = (RightVector) o;

                if (this.size - right.size != 0) {

                    return this.size - right.size;

                }

                if (source.size() - right.source.size() != 0) {

                    return source.size() - right.source.size();

                }

                TreeMap<Comparable, Double> leftTree = new TreeMap<Comparable, Double>(source);

                TreeMap<Comparable, Double> rightTree = new TreeMap<Comparable, Double>(right.source);

                Iterator<Comparable> leftIt = leftTree.keySet().iterator();

                Iterator<Comparable> rightIt = rightTree.keySet().iterator();

                while (leftIt.hasNext()) {

                    Comparable leftO = (Comparable) leftIt.next();

                    Comparable rightO = (Comparable) rightIt.next();

                    if (leftO.compareTo(rightO) != 0) {

                        return leftO.compareTo(rightO);

                    }

                    if (leftTree.get(leftO).compareTo(rightTree.get(rightO)) != 0) {

                        return leftTree.get(leftO).compareTo(rightTree.get(rightO));

                    }

                }

                return 0;



            } else {

                return this.getClass().getName().compareTo(o.getClass().getName());

            }

        }

    }

}