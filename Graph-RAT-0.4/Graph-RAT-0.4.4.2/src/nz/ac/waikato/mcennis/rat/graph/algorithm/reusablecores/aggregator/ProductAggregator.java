/**
 * Jul 26, 2008-10:31:27 PM
 * Copyright Daniel McEnnis, see license.txt
 */

package nz.ac.waikato.mcennis.rat.graph.algorithm.reusablecores.aggregator;

import java.util.logging.Level;
import java.util.logging.Logger;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author Daniel McEnnis
 */
public class ProductAggregator implements AggregatorFunction{

    @Override
    public Instance[] aggregate(Instance[] data, double[] weights) {
        Instance[] ret = new Instance[0];
        if((data != null)&&(data.length>0)&&(weights != null)&&(weights.length == data.length)){
            Instances meta = data[0].dataset();
            if(meta == null){
               Logger.getLogger(ProductAggregator.class.getName()).log(Level.SEVERE, "Instances array entry 0 is missing a dataset");
               return ret;
            }
            for(int i=1;i<data.length;++i){
                if(data[i].dataset() == null){
                    Logger.getLogger(ProductAggregator.class.getName()).log(Level.SEVERE, "Instances array entry "+i+" is missing a dataset");
                }
                if(!data[i].dataset().equalHeaders(meta)){
                    Logger.getLogger(ProductAggregator.class.getName()).log(Level.SEVERE, "Instances array entry "+i+" is not compatible with the previous entries");
                    return ret;
                }
            }
            ret = new Instance[1];
            ret[0] = new Instance(meta.numAttributes());
            ret[0].setDataset(meta);
            for (int i = 0; i < ret[0].numAttributes(); ++i) {
                if (meta.attribute(i).isNumeric()) {
                    double val = 1.0;
                    for (int j = 0; j < data.length; ++j) {
                        val *= (data[j].value(i) * weights[j]);
                    }
                    ret[0].setValue(i, val);
                } else {
                    ret[0].setValue(i, data[0].value(i));
                }
            }
        }else{
            if(data==null){
                Logger.getLogger(ProductAggregator.class.getName()).log(Level.WARNING, "Instance array is null");
            }else if(weights==null){
                Logger.getLogger(ProductAggregator.class.getName()).log(Level.WARNING, "weights array is null");
            }else if(data.length != weights.length){
                Logger.getLogger(ProductAggregator.class.getName()).log(Level.SEVERE, "Array lengths differ - "+data.length+":"+weights.length);
            }
        }
        return ret;
    }

}
