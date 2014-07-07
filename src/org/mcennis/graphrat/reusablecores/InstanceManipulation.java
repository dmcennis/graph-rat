/**
 * Jul 23, 2008-6:06:57 PM
 *
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

package org.mcennis.graphrat.reusablecores;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeMap;
import org.mcennis.graphrat.reusablecores.instancefactory.InstanceFactory;
import org.mcennis.graphrat.reusablecores.instancefactory.InstanceFactoryFactory;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import org.dynamicfactory.property.Property;

/**
 * Helper class for performing manipulations of weka Instance and Instances objects.
 * 
 * @author Daniel McEnnis
 */
public class InstanceManipulation {
    /**
     * Transforms a property into an Instance object.  Useful for providing
     * raw Instances for use in aggreator algorithms.  The work is farmed out 
     * to InstanceFactory objects that perform the transformation based on the
     * class of the values in the property.  The InstanceFactoryRegistry object
     * performs the lookup for the right registry.  In the event of a null property,
     * empty value list, or a class of object without a factory for transforming 
     * into an Instance, an empty LinkedList is returned.
     * 
     * @param property property object to be transformed into a Instance.
     * 
     * @return LinkedList of Instance objects representing this property
     */
    static public LinkedList<Instance> propertyToInstance(Property property){
        LinkedList<Instance> ret = new LinkedList<Instance>();
        if(property != null){
            Class type = property.getPropertyClass();
            InstanceFactory factory = InstanceFactoryFactory.newInstance().create(type);
            Iterator values = property.getValue().iterator();
            while(values.hasNext()){
                ret.add(factory.transform(values.next(), property.getType()));
            }
        }
        return ret;
    }
    
    /**
     * Takes the contents of the Instance array and creates a new Instance object
     * whose attributes are the attributes of the Instance objects in sequence
     * backed by a new Dataset relfecting the new set of attributes.
     *   
     * If there is any conflict of attribute names between the Instance objects,
     * they are duplicated in the Instance object (which may cause difficulties
     * for some machine learning algorithms.)  If this a problem, utilize 
     * normalizeFieldNames instead of concatenation.
     * 
     * If either array is null or if the length of the data and meta array do not
     * match, a new Instance object without attributes is created and returned.
     * 
     * @param data array of Instance objects 
     * @param meta array of Instances backing the data array
     * @return new Instance containing all the given data
     */
    static public Instance instanceConcatenation(Instance[] data,Instances[] meta){
        FastVector attributeVector = new FastVector();
        LinkedList<Double> values = new LinkedList<Double>();
        Instance ret = new Instance(0);
        ret.setDataset(new Instances("Default",new FastVector(),0));
        if((data !=null)&&(meta != null)&&(data.length == meta.length)&&(data.length>0)){
            for(int i=0;i<data.length;++i){
                for(int j=0;j<meta[i].numAttributes();++j){
                    attributeVector.addElement(meta[i].attribute(j));
                    values.add(data[i].value(j));
                }
            }
            ret = new Instance(values.size());
            Iterator<Double> it = values.iterator();
            for(int i=0;i<ret.numAttributes();++i){
                ret.setValue(i,it.next());
            }
            ret.setDataset(new Instances(meta[0].relationName()+" Concatenated",attributeVector,1));
        }
        return ret;
    }
    
    /**
     * Takes an array of Instance objects and create a new array of Instance objects
     * backed by a single Dataset.  When there is no data for a given attribute in
     * the new Dataset, the Instance is assigned the 'missing' value for that attribute.
     * 
     * If either array is null or if the length of the data and meta arrays are 
     * not the same, an empty array is returned.  Otherwise, the length of the returned
     * array is equal to the length of the data array.
     * 
     * @param data array of Instance objects to normalize
     * @param meta array of Instances objects backing the objects in the data array
     * @return new array of Instance objects backed by a single dataset.
     */
    static public Instance[] normalizeFieldNames(Instance[] data, Instances[] meta){
        Instance[] ret = new Instance[]{};
        TreeMap<String,Attribute> attributes = new TreeMap<String,Attribute>();
        if((meta != null )&&(data != null)&&(data.length==meta.length)){
            //collect a list of all elements
            for(int i=0;i<meta.length;++i){
                for(int j=0;j<meta[i].numAttributes();++j){
                    if(!attributes.containsKey(meta[i].attribute(j).name())){
                        attributes.put(meta[i].attribute(j).name(), meta[i].attribute(j));
                    }else if((meta[i].attribute(j).isNominal())&&(attributes.get(meta[i].attribute(j).name()).indexOfValue(data[i].stringValue(j))<0)){
                        attributes.get(meta[i].attribute(j).name()).addStringValue(data[i].stringValue(j));
                    }
                }
            }
            
            // populate a fastvector using these attributes in ascending order by attribute name
            // assumes that all attributes of the same name are of the same type and info.
            Iterator<String> it = attributes.keySet().iterator();
            FastVector attributeVector = new FastVector();
            int count = 0;
            HashMap<Attribute,Integer> index = new HashMap<Attribute,Integer>();
            while(it.hasNext()){
                Attribute item = attributes.get(it.next());
                attributeVector.addElement(item);
                index.put(item, count++);
            }
            Instances retMeta = new Instances(meta[0].relationName()+" Concatenated",attributeVector,data.length);
            
            // create instance array using the new instances object. 
            // initially, all entries are missing
            ret = new Instance[data.length];
            for(int i=0;i<ret.length;++i){
                ret[i] = new Instance(attributes.size());
                for(int j=0;j<ret[i].numAttributes();++j){
                    ret[i].setMissing(j);
                }
            }
            
            // for all entries that are present, record a value
            // if multiple attributes have the same name, the attribute closest 
            // to the end of the array is saved, the remainder are discarded.
            for(int i=0;i<data.length;++i){
                for(int j=0;j<data[i].numAttributes();++j){
                    ret[i].setValue(meta[i].attribute(j),data[i].value(j));
                }
                ret[i].setDataset(retMeta);
            }
            
        }
        return ret;
    }
}
