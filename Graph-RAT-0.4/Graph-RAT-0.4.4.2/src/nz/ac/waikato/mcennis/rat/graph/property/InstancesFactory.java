/*
 * Created 27-3-08
 * Copyright Daniel McEnnis, see license.txt
 */

package org.dynamicfactory.property;

import java.util.Enumeration;

import org.mcennis.graphrat.graph.Graph;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instances;

/**
 * Factory for serializing and deserializing Instances objects.
 * @author Daniel McEnnis
 */
public class InstancesFactory implements PropertyValueFactory<Instances>{

    @Override
    public Instances importFromString(String data, Graph g) {
        String[] attributes = data.split(",");
        String title = attributes[0];
        FastVector attributeList = new FastVector(attributes.length-1);
        for(int i=1;i<attributes.length;++i){
            String[] attributeParts = attributes[i].split(":");
            if(attributeParts.length<=1){
                attributeList.addElement(new Attribute(attributes[i]));
            }else{
                String name = attributeParts[0];
                FastVector attValues = new FastVector(attributeParts.length-1);
                for(int j=1;j<attributeParts.length;++j){
                    attValues.addElement(attributeParts[j]);
                }
                attributeList.addElement(new Attribute(name,attValues));
            }
        }
        Instances ret = new Instances(title,attributeList,100);
        ret.setClassIndex(attributeList.size()-1);
        return ret;
    }

    @Override
    public String exportToString(Instances type, Graph g) {
        StringBuffer ret = new StringBuffer();
        ret.append(type.relationName());
        Enumeration attributes = type.enumerateAttributes();
        while(attributes.hasMoreElements()){
            ret.append(",");
            outputAttribute(ret,(Attribute)attributes.nextElement());
        }
        return ret.toString();
    }

    protected void outputAttribute(StringBuffer ret,Attribute value){
        ret.append(value.name());
        Enumeration nominalValues = value.enumerateValues();
        while((nominalValues != null) && nominalValues.hasMoreElements()){
            ret.append(":");
            ret.append(nominalValues.nextElement().toString());
        }
    }
}
