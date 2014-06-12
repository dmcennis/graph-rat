/**
 * Jul 26, 2008-10:28:51 PM
 * Copyright Daniel McEnnis, see license.txt
 */

package nz.ac.waikato.mcennis.rat.graph.algorithm.reusablecores.aggregator;

import java.util.logging.Level;
import java.util.logging.Logger;
import weka.core.Instance;

/**
 *
 * @author Daniel McEnnis
 */
public class NullAggregator implements AggregatorFunction{

    @Override
    public Instance[] aggregate(Instance[] data, double[] weights) {
        Instance[] ret = new Instance[0];
        if((data != null)&&(data.length>0)&&(weights != null)&&(weights.length == data.length)){
            for(int i=0;i<data.length;++i){
                if(data[i].dataset()==null){
                    Logger.getLogger(MaxAggregator.class.getName()).log(Level.SEVERE, "Instances array entry "+i+" is missing a dataset");
                    return ret;
                }
            }
            return data;
        }else{
            if(data==null){
                Logger.getLogger(MaxAggregator.class.getName()).log(Level.WARNING, "Instance array is null");
            }else if(weights==null){
                Logger.getLogger(MaxAggregator.class.getName()).log(Level.WARNING, "weights array is null");
            }else if(data.length != weights.length){
                Logger.getLogger(MaxAggregator.class.getName()).log(Level.SEVERE, "Array lengths differ - "+data.length+":"+weights.length);
            }
            return ret;
        }
    }

}
