/**
 * Copyright Daniel McEnnis, Anna Huang, see license.txt
 */
package org.mcennis.graphrat.algorithm.reusablecores.distance;

import org.mcennis.graphrat.algorithm.reusablecores.datavector.DataVector;

/**
 * Weighted KLDistance function
 * Adapted from Anna Huang's Weka implementation.
 * <br/>
 * Note: SparseListMapDataVector and ListSetVector are slow on this metric
 * 
 * @author Anna Huang
 */
public class WeightedKLDDistance implements DistanceFunction {

    @Override
    public double distance(DataVector inst1, DataVector inst2) {

        double dist = 0.0, sumA = 0.0, sumB = 0.0;

//		for(int v = 0; v < inst1.numValues(); v++) {
        inst1.reset();
        while (inst1.hasNext()) {
            inst1.next();
            sumA += inst1.getCurrentValue();
        }

//		for(int v = 0; v < inst2.numValues(); v++) {
        inst2.reset();
        while (inst2.hasNext()) {
            inst2.next();
            sumB += inst2.getCurrentValue();
        }

        double Pnew = sumA + sumB;
        double pi1 = sumA / Pnew, pi2 = sumB / Pnew;

        if (Math.min(pi1, pi2) <= 0) {
            System.err.format("Warning: zero or negative weights in JS calculation! (pi1 %s, pi2 %s)\n", pi1, pi2);
            return Double.NaN;
        }

        double kl1 = 0.0, kl2 = 0.0, tmp = 0.0;
        inst1.reset();
//		for (int i = 0; i < inst1.numValues(); i++) {
        while (inst1.hasNext()) {
            inst1.next();

            tmp = inst1.getCurrentValue();
            if (tmp != 0) {
                kl1 += tmp * Math.log(tmp / (tmp * pi1 + pi2 * inst2.getValue(inst1.getCurrentIndex())));
            }
        }
//		for (int i = 0; i < inst2.numValues(); i++) {
        inst2.reset();
        while (inst2.hasNext()) {
            inst2.next();

            if ((tmp = inst2.getCurrentValue()) != 0) {
                kl2 += tmp * Math.log(tmp / (inst1.getValue(inst2.getCurrentIndex()) * pi1 + pi2 * tmp));
            }
        }
        dist = Pnew * (pi1 * kl1 + pi2 * kl2);
        return dist;
    }
}
