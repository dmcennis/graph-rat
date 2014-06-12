/*

 * BasicProperty.java

 *

 * Created on 1 May 2007, 16:27

 *

 * copyright Daniel McEnnis - published under Aferro GPL (see license.txt)

*/



package nz.ac.waikato.mcennis.rat.graph.property;



import nz.ac.waikato.mcennis.rat.graph.model.ModelShell;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;



/**
 *
 * 
 * Class describing properties on graphs and actors
 * @author Daniel McEnnis
 */

public class BasicProperty extends ModelShell implements Property{

    static final long serialVersionUID = 2;

    

    java.util.Vector values;

    Class objectType;

    String type;

    

    /** Creates a new instance of BasicProperty 

     * @param name id for this property

     */

    public BasicProperty(String name, Class type) {

        values = new java.util.Vector();

        this.type = name;

        objectType = type;

    }
    
    public void setType(String name){
        this.type = name;
    }
    
    public void setClass(Class classType){
        objectType = classType;
    }


    @Override
    public List getValue() {

        return values;

    }



    @Override

    public String getType() {

        return type;

    }



    @Override

    public void add(Object value) {

        if(objectType.isInstance(value)){

            values.add(value);

            this.fireChange(Property.ADD_VALUE,0);

        }

    }



    @Override

    public int compareTo(Object o) throws ClassCastException{

        Property right = (Property)o;

        if(this.getType().contentEquals(right.getType())){

            if(this.objectType.getName().compareTo(right.getPropertyClass().getName())!=0){
                return this.objectType.getName().compareTo(right.getPropertyClass().getName());
            }            
            List leftValue = this.getValue();
            List rightValue = right.getValue();

            if(leftValue.size() != rightValue.size()){
                return leftValue.size() - rightValue.size();
            }else{
                if(leftValue.size() == 0){
                    return 0;
                }else if(!(leftValue.iterator().next() instanceof Comparable)){
                    return 0;
                }else if(!(rightValue.iterator().next() instanceof Comparable)){
                    return 0;
                }
                java.util.Collections.sort(leftValue);
                java.util.Collections.sort(rightValue);
                Iterator leftIt = leftValue.iterator();               
                Iterator rightIt = rightValue.iterator();
                while(leftIt.hasNext()){
                    Comparable l = (Comparable)leftIt.next();
                    Comparable r = (Comparable)rightIt.next(); 
                    if(!(l.compareTo(r)!=0)){
                        return l.compareTo(r);
                    }
                }
                return 0;
            }
        }else{
            return this.getType().compareTo(right.getType());
        }

    }    



    public boolean equals(Object obj) {

        if(obj instanceof Property){

            if(this.compareTo(obj)==0){

                return true;

            }else{

                return false;

            }

        }else{

            return false;

        }

    }

    

    @Override

    public Property duplicate(){

        BasicProperty props = new BasicProperty(type,objectType);

        for(int i=0;i<values.size();++i){

            props.add(values.get(i));

        }

        return props;

    }



    public Class getPropertyClass() {

        return objectType;

    }

    

}

