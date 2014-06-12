/**
 * Jul 23, 2008-10:06:20 PM
 * Copyright Daniel McEnnis, see license.txt
 */

package nz.ac.waikato.mcennis.rat.graph.algorithm.reusablecores.aggregator;

import java.util.logging.Level;
import java.util.logging.Logger;
import nz.ac.waikato.mcennis.rat.graph.algorithm.reusablecores.InstanceManipulation;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author Daniel McEnnis
 */
public class ConcatenationAggregator implements AggregatorFunction{

    @Override
    public Instance[] aggregate(Instance[] data, double[] weights) {
        Instance[] ret = new Instance[0];
        if((data != null)&&(data.length>0)&&(weights != null)&&(weights.length == data.length)){
            for(int i=0;i<data.length;++i){
                if(data[i].dataset()==null){
                    Logger.getLogger(ConcatenationAggregator.class.getName()).log(Level.SEVERE, "Instances array entry "+i+" is missing a dataset");
                    return ret;
                }
            }
            Instances[] meta = new Instances[data.length];
            for(int i=0;i<meta.length;++i){
                meta[i] = data[i].dataset();
            }
            ret = new Instance[1];
            ret[0] = InstanceManipulation.instanceConcatenation(data, meta);
        }else{
            if(data==null){
                Logger.getLogger(FirstItemAggregatorFunction.class.getName()).log(Level.WARNING, "Instance array is null");
            }else if(weights==null){
                Logger.getLogger(FirstItemAggregatorFunction.class.getName()).log(Level.WARNING, "weights array is null");
            }else if(data.length != weights.length){
                Logger.getLogger(FirstItemAggregatorFunction.class.getName()).log(Level.SEVERE, "Array lengths differ - "+data.length+":"+weights.length);
            }
        }
        return ret;
    }

}
