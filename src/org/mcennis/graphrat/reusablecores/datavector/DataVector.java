/*

 * Created 21/05/2008-14:13:29

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


package org.mcennis.graphrat.reusablecores.datavector;



/**

 * Class describing an abstraction of a vector for data.  This is designed so

 * data does not need to be transformed, mearly wrapped, in order to use a single implementation

 * of standard functions such as distance functions and aggregators.  Note that 

 * this interface does not guarantee ordering or the consistant order of items.

 * 

 * @author Daniel McEnnis

 */

public interface DataVector extends Comparable{

   

    /**

     * Returns the number of dimensions of this object.  This is frequently not the number of non-zero entries.

     * @return dimension of the vector.

     */

    public int size();

    

    /**

     * Get the value at the given index.  Note that this need not be an integer.

     * Some classes require integer values and will throw an exception if the object

     * does not convert into an integer.

     * @param index index of the desired value

     * @return value at the given index

     */

    public double getValue(Comparable index);

    

    /**

     * Reset the iterator to the first index of the function, potentially creating 

     * a new ordering of objects on some data vectors (i.e. HashMap objects).

     */

    public void reset();

    

    /**

     * return the value at the current position of the iterator.

     * @return current value

     */

    public double getCurrentValue();

    

    /**

     * return the index at the current position of the iterator

     * @return

     */

    public Comparable getCurrentIndex();

    

    /**

     * Move the iterator to the next non-zero index-value pair

     */

    public void next();

    

    /**

     * Does the vector contain any additional non-zero values?

     * @return are there values left.

     */

    public boolean hasNext();

    

    /**

     * Set the dimensionality of this vector

     * @param s dimensionality of the vector

     */

    public void setSize(int s);

}

