/*
 * Created 21/05/2008-16:16:29
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.graph.algorithm.reusablecores.datavector;

import nz.ac.waikato.mcennis.rat.graph.algorithm.reusablecores.datavector.DataVector;
import java.util.Iterator;
import java.util.Map;

/**
 * Data vector backed by a maps.
 * 
 * @author Daniel McEnnis
 */
public class MapDataVector implements DataVector {

    Map<? extends Object, Double> source;
    Iterator<? extends Object> it;
    Object current = null;
    int size;

    /**
     * Create a new data vector from a map.  Note that ordering or consistant
     * oprder of iteration are not guaranteed.
     * @param source source data
     * @param size number of dimensions
     */
    public MapDataVector(Map<? extends Object, Double> source, int size) {
        this.source = source;
        it = source.keySet().iterator();
        size = source.size();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void setSize(int s) {
        size = s;
    }

    @Override
    public double getValue(Object index) {
        if (source.containsKey(index)) {
            return source.get(index).doubleValue();
        } else {
            return 0.0;
        }
    }

    @Override
    public void reset() {
        it = source.keySet().iterator();
        current = null;

    }

    @Override
    public double getCurrentValue() {
        if (current == null) {
            return Double.NaN;
        } else {
            return source.get(current).doubleValue();
        }
    }

    @Override
    public Object getCurrentIndex() {
        return current;
    }

    @Override
    public void next() {
        current = it.next();
    }

    @Override
    public boolean hasNext() {
        return it.hasNext();
    }
}
