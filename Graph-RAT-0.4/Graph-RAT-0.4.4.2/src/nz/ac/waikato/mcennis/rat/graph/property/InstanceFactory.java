/*
 * Created 27-3-08
 * Copyright Daniel McEnnis, see license.txt
 */

package nz.ac.waikato.mcennis.rat.graph.property;

import nz.ac.waikato.mcennis.rat.graph.Graph;
import weka.core.Instance;

/**
 * Factory object for serializing and deserializing weka Instance objects
 * @author Daniel McEnnis
 */
public class InstanceFactory implements PropertyValueFactory<Instance>{

    @Override
    public Instance importFromString(String data, Graph g) {
        String[] split = data.split(",");
        double weight = Double.parseDouble(split[0]);
        double[] values = new double[split.length-1];
        for(int i=1;i<split.length;++i){
            values[i-1] = Double.parseDouble(split[i]);
        }
        Instance ret = new Instance(weight,values);
        return ret;
    }

    @Override
    public String exportToString(Instance type, Graph g) {
        double weight = type.weight();
        double[] values = type.toDoubleArray();
        StringBuffer buffer = new StringBuffer();
        buffer.append(Double.toString(weight));
        for(int i=0;i<values.length;++i){
            buffer.append(",").append(Double.toString(values[i]));
        }
        return buffer.toString();
    }

}
