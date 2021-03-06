/*

 * Created 21/05/2008-16:16:29

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



import java.util.Iterator;

import java.util.Map;

import java.util.TreeMap;



/**

 * Data vector backed by a maps.

 * 

 * @author Daniel McEnnis

 */

public class MapDataVector implements DataVector {



    Map<? extends Comparable, Double> source;

    Iterator<? extends Comparable> it;

    Comparable current = null;

    int size;



    /**

     * Create a new data vector from a map.  Note that ordering or consistant

     * oprder of iteration are not guaranteed.

     * @param source source data

     * @param size number of dimensions

     */

    public MapDataVector(Map<? extends Comparable, Double> source, int size) {

        this.source = source;

        it = source.keySet().iterator();

        size = source.size();

    }



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

        if (source.containsKey(index)) {

            return source.get(index).doubleValue();

        } else {

            return 0.0;

        }

    }



    @Override

    public void reset() {

        it = source.keySet().iterator();

        current = null;



    }



    @Override

    public double getCurrentValue() {

        if (current == null) {

            return Double.NaN;

        } else {

            return source.get(current).doubleValue();

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

            MapDataVector right = (MapDataVector) o;

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

                if(leftTree.get(leftO).compareTo(rightTree.get(rightO))!=0){

                    return leftTree.get(leftO).compareTo(rightTree.get(rightO));

                }

            }

            return 0;



        } else {

            return this.getClass().getName().compareTo(o.getClass().getName());

        }

    }

}

