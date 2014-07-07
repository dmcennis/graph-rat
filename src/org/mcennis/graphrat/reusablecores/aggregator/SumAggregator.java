/**
 * Jul 23, 2008-9:58:49 PM
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
package org.mcennis.graphrat.reusablecores.aggregator;

import java.util.logging.Level;
import java.util.logging.Logger;

import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author Daniel McEnnis
 */
public class SumAggregator implements AggregatorFunction {

    @Override
    public Instance[] aggregate(Instance[] data, double[] weights) {
        Instance[] ret = new Instance[0];
        if((data != null)&&(data.length>0)&&(weights != null)&&(weights.length == data.length)){
            Instances meta = data[0].dataset();
            if(meta == null){
               Logger.getLogger(SumAggregator.class.getName()).log(Level.SEVERE, "Instances array entry 0 is missing a dataset");
               return ret;
            }
            for(int i=1;i<data.length;++i){
                if(data[i].dataset() == null){
                    Logger.getLogger(SumAggregator.class.getName()).log(Level.SEVERE, "Instances array entry "+i+" is missing a dataset");
                }
                if(!data[i].dataset().equalHeaders(meta)){
                    Logger.getLogger(SumAggregator.class.getName()).log(Level.SEVERE, "Instances array entry "+i+" is not compatible with the previous entries");
                    return ret;
                }
            }
            ret = new Instance[1];
            ret[0] = new Instance(data[0].numAttributes());
            ret[0].setDataset(meta);
            for (int i = 0; i < ret[0].numAttributes(); ++i) {
                if (meta.attribute(i).isNumeric()) {
                    double val = 0.0;
                    for (int j = 0; j < data.length; ++j) {
                        val += data[j].value(i) * weights[j];
                    }
                    ret[0].setValue(i, val);
                } else {
                    ret[0].setValue(i, data[0].value(i));
                }
            }
        }else{
            if(data==null){
                Logger.getLogger(SumAggregator.class.getName()).log(Level.WARNING, "Instance array is null");
            }else if(weights==null){
                Logger.getLogger(SumAggregator.class.getName()).log(Level.WARNING, "weights array is null");
            }else if(data.length != weights.length){
                Logger.getLogger(SumAggregator.class.getName()).log(Level.SEVERE, "Array lengths differ - "+data.length+":"+weights.length);
            }
        }
        return ret;
    }
    
    public SumAggregator prototype(){
        return new SumAggregator();
    }
}
