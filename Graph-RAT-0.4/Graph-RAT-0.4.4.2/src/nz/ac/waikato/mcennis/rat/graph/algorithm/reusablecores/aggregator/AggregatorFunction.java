/**
 * Created Jul 23, 2008-5:40:37 PM
 * Copyright Daniel McEnnis, see license.txt
 */

package org.mcennis.graphrat.algorithm.reusablecores.aggregator;

import weka.core.Instance;

/**
 *
 * @author Daniel McEnnis
 */
public interface AggregatorFunction {
    
    /**
     * <p>AggregatorFunction aggregates the given data, adjusted by the weights given,
     * into one or more Instances.</p><br/>
     * <br/>
     * <p>The data array may be null, but if it is not null, every Instance in the array must be backed 
     * by an Instances object.  Some aggregators require that all elements in the
     * data array be backed by the same or equivelant Instances object.  Other 
     * aggregators require that every element in the data array e backed by an Instances
     * object that has disjoint attribute names compared to other elements' Instances object.
     * Others have no restrictions at all.  See the documentation of each aggregator
     * function to determine what additional restrictions on backing dataset are necessary.</p><br/>
     * <br/>
     * <p>If the data array is null, the weights array should be either null or of length 0.
     * In all other cases, this array must match the length of the data array.</p><br/>
     * <br/>
     * <p>In the event of null input, the aggregator should return an empty Instance array.
     * In general, when meaningful results can not be returned, an empty Instance array is 
     * appropriate return value.<br/>
     * <br/>
     * <p>In the event of an error, the aggregator should log a meaningful error (with stack trace
     * if it involves an exception or error) with the logger identified by the aggregator
     * class name.  Logging of debugging info should be at a lower priority than the INFO
     * level (the default logging level.) No exceptions should be propogated back to the caller.</p>
     *  
     * @param data array of Instance objects to aggregate
     * @param weights array of relative weights of the Instance objects
     * @return array containing the aggregated results
     * 
     */
    public Instance[] aggregate(Instance[] data,double[] weights);
}
