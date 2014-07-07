/**
 * Jul 25, 2008-8:09:30 PM
 * Copyright Daniel McEnnis, see license.txt
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
public class StandardDeviationAggregator implements AggregatorFunction {

    @Override
    public Instance[] aggregate(Instance[] data, double[] weights) {
        Instance[] ret = new Instance[0];
        if ((data != null) && (data.length > 0) && (weights != null) && (weights.length == data.length)) {
            Instances meta = data[0].dataset();
            if (meta == null) {
                Logger.getLogger(MeanAggregator.class.getName()).log(Level.SEVERE, "Instances array entry 0 is missing a dataset");
                return ret;
            }
            for (int i = 1; i < data.length; ++i) {
                if (data[i].dataset() == null) {
                    Logger.getLogger(MeanAggregator.class.getName()).log(Level.SEVERE, "Instances array entry " + i + " is missing a dataset");
                }
                if (!data[i].dataset().equalHeaders(meta)) {
                    Logger.getLogger(MeanAggregator.class.getName()).log(Level.SEVERE, "Instances array entry " + i + " is not compatible with the previous entries");
                    return ret;
                }
            }
            ret = new Instance[1];
            double[] sum = new double[data[0].numAttributes()];
            double[] squaredSum = new double[data[0].numAttributes()];
            Arrays.fill(sum, 0.0);
            Arrays.fill(squaredSum, 0.0);
            for (int i = 0; i < sum.length; ++i) {
                if (meta.attribute(i).isNumeric()) {
                    for (int j = 0; j < data.length; ++j) {
                        sum[i] += data[j].value(i)*weights[j];
                        squaredSum[i] += data[j].value(i)*weights[j] * data[j].value(i)*weights[j];
                    }
                    sum[i] = ((squaredSum[i] * data.length) - sum[i] * sum[i]) / ((double) (data.length - 1));
                } else {
                    sum[i] = data[0].value(i);
                }
            }
            ret[0] = new Instance(sum.length, sum);
            ret[0].setDataset(meta);
        } else {
            if (data == null) {
                Logger.getLogger(MeanAggregator.class.getName()).log(Level.WARNING, "Instance array is null");
            } else if (weights == null) {
                Logger.getLogger(MeanAggregator.class.getName()).log(Level.WARNING, "weights array is null");
            } else if (data.length != weights.length) {
                Logger.getLogger(MeanAggregator.class.getName()).log(Level.SEVERE, "Array lengths differ - " + data.length + ":" + weights.length);
            }
        }
        return ret;
    }
}
