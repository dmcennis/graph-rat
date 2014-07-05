/*
 * Copyright Daniel McEnnis, see license.txt
 */

package org.dynamicfactory.property;

import java.util.HashMap;
import java.util.Iterator;
import nz.ac.waikato.mcennis.rat.graph.Graph;

/**
 * Factory for serlizing and deserializing HashMap&lt;String,Double&gt; objects
 * @author Daniel McEnnis
 */
public class HashMapStringDoubleFactory implements PropertyValueFactory<HashMap<String,Double>>{

    @Override
    public HashMap<String, Double> importFromString(String data, Graph g) {
        String[] pieces = data.split(":");
        HashMap<String,Double> ret = new HashMap<String,Double>();
        int i=0;
        while(i<pieces.length){
            String left = pieces[i++];
            if(i<pieces.length){
                ret.put(left,Double.parseDouble(pieces[i++]));
            }
        }
        return ret;
    }

    @Override
    public String exportToString(HashMap<String, Double> type, Graph g) {
        StringBuffer ret = new StringBuffer();
        Iterator<String> key = type.keySet().iterator();
        boolean first = true;
        while(key.hasNext()){
            if(!first){
                ret.append(":");
            }else{
                first = false;
            }
            String left = key.next();
            ret.append(left).append(":");
            ret.append(Double.toString(type.get(left)));
        }
        return  ret.toString();
    }

    
}
