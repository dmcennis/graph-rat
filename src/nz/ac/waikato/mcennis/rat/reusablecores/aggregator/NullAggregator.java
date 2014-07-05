/**
 * Jul 26, 2008-10:28:51 PM
 * Copyright Daniel McEnnis, see license.txt
 */

package nz.ac.waikato.mcennis.rat.reusablecores.aggregator;

import weka.core.Instance;

/**
 *
 * @author Daniel McEnnis
 */
public class NullAggregator implements AggregatorFunction{

    @Override
    public Instance[] aggregate(Instance[] data, double[] weights) {
        return new Instance[0];
    }

    public NullAggregator prototype(){
        return new NullAggregator();
    }
}
