/**
 * Jul 25, 2008-8:01:11 PM
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

package org.mcennis.graphrat.algorithm.reusablecores.aggregator;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author Daniel McEnnis
 */
public class MeanAggregator implements AggregatorFunction{

    @Override
    public Instance[] aggregate(Instance[] data, double[] weights) {
        Instance[] ret = new Instance[0];
        if((data != null)&&(data.length>0)&&(weights != null)&&(weights.length == data.length)){
            Instances meta = data[0].dataset();
            if(meta == null){
               Logger.getLogger(MeanAggregator.class.getName()).log(Level.SEVERE, "Instances array entry 0 is missing a dataset");
               return ret;
            }
            for(int i=1;i<data.length;++i){
                if(data[i].dataset() == null){
                    Logger.getLogger(MeanAggregator.class.getName()).log(Level.SEVERE, "Instances array entry "+i+" is missing a dataset");
                }
                if(!data[i].dataset().equalHeaders(meta)){
                    Logger.getLogger(MeanAggregator.class.getName()).log(Level.SEVERE, "Instances array entry "+i+" is not compatible with the previous entries");
                    return ret;
                }
            }
            ret = new Instance[1];
            double[] values = new double[data[0].numAttributes()];
            Arrays.fill(values, 0.0);
            for(int i=0;i<values.length;++i){
                if(meta.attribute(i).isNumeric()){
                    for(int j=0;j<data.length;++j){
                        values[i] += data[j].value(i)*weights[j];
                    }
                    values[i] /= data.length;
                }else{
                    values[i]=data[0].value(i);
                }
            }
            ret[0] = new Instance(values.length,values);
            ret[0].setDataset(meta);
        }else{
            if(data==null){
                Logger.getLogger(MeanAggregator.class.getName()).log(Level.WARNING, "Instance array is null");
            }else if(weights==null){
                Logger.getLogger(MeanAggregator.class.getName()).log(Level.WARNING, "weights array is null");
            }else if(data.length != weights.length){
                Logger.getLogger(MeanAggregator.class.getName()).log(Level.SEVERE, "Array lengths differ - "+data.length+":"+weights.length);
            }
        }
        return ret;
    }

    public MeanAggregator prototype(){
        return new MeanAggregator();
    }
}
