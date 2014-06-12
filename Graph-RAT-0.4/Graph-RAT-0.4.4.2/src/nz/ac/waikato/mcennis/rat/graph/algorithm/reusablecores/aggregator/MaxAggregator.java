/**
 * Jul 25, 2008-7:51:15 PM
 * Copyright Daniel McEnnis, see license.txt
 */

package nz.ac.waikato.mcennis.rat.graph.algorithm.reusablecores.aggregator;

import java.util.logging.Level;
import java.util.logging.Logger;
import nz.ac.waikato.mcennis.rat.graph.algorithm.reusablecores.datavector.DataVector;
import nz.ac.waikato.mcennis.rat.graph.algorithm.reusablecores.datavector.InstanceDataVector;
import nz.ac.waikato.mcennis.rat.graph.algorithm.reusablecores.distance.DistanceFunction;
import nz.ac.waikato.mcennis.rat.graph.algorithm.reusablecores.distance.DotProductDistance;
import weka.core.Instance;

/**
 *
 * @author Daniel McEnnis
 */
public class MaxAggregator implements AggregatorFunction{

    DistanceFunction distance =  new DotProductDistance();
    
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
            ret = new Instance[1];
            double maxLength = Double.NEGATIVE_INFINITY;
            double length=0.0;
            for(int i=0;i<data.length;++i){
                DataVector d = new InstanceDataVector(data[i]);
                length = distance.distance(d, d);
                if(length > maxLength){
                    ret[0] = data[i];
                    maxLength = length;
                }
            }
        }else{
            if(data==null){
                Logger.getLogger(MaxAggregator.class.getName()).log(Level.WARNING, "Instance array is null");
            }else if(weights==null){
                Logger.getLogger(MaxAggregator.class.getName()).log(Level.WARNING, "weights array is null");
            }else if(data.length != weights.length){
                Logger.getLogger(MaxAggregator.class.getName()).log(Level.SEVERE, "Array lengths differ - "+data.length+":"+weights.length);
            }
        }
        return ret;
    }
    
    public void setDistanceFunction(DistanceFunction d){
        distance = d;
    }
}
