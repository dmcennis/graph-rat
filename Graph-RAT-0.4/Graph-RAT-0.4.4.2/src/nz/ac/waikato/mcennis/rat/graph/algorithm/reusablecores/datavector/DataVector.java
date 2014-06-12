/*
 * Created 21/05/2008-14:13:29
 * Copyright Daniel McEnnis, see license.txt
 */

package nz.ac.waikato.mcennis.rat.graph.algorithm.reusablecores.datavector;

/**
 * Class describing an abstraction of a vector for data.  This is designed so
 * data does not need to be transformed, mearly wrapped, in order to use a single implementation
 * of standard functions such as distance functions and aggregators.  Note that 
 * this interface does not guarantee ordering or the consistant order of items.
 * 
 * @author Daniel McEnnis
 */
public interface DataVector {
   
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
    public double getValue(Object index);
    
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
    public Object getCurrentIndex();
    
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
