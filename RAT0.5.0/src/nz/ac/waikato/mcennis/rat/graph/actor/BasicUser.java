/*

 * BasicUser.java

 *

 * Created on 1 May 2007, 15:12

 *

 * copyright Daniel McEnnis - published under Aferro GPL (see license.txt)

 */



package nz.ac.waikato.mcennis.rat.graph.actor;



import java.util.List;
import java.util.Iterator;
import java.util.LinkedList;
import nz.ac.waikato.mcennis.rat.graph.model.ModelShell;

import nz.ac.waikato.mcennis.rat.graph.property.Property;
import nz.ac.waikato.mcennis.rat.graph.descriptors.Properties;


/**

 *

 * @author Daniel McEnnis

 */

public class BasicUser extends ModelShell implements Actor {

    

   static final long  serialVersionUID = 2;



    String type = "User";

    String ID;

    java.util.HashMap<String,Property> properties;


    /** Creates a new instance of BasicUser */

    public BasicUser(String ID) {

        this.ID = ID;

        properties = new java.util.HashMap<String, Property>();

    }





    @Override

    public String getID() {

        return ID;

    }





    @Override

    public List<Property> getProperty() {
        LinkedList<Property> ret = new LinkedList<Property>();
        ret.addAll(properties.values());
        return ret;
    }



    @Override

    public Property getProperty(String ID) {

        return properties.get(ID);

    }



    @Override

    public void removeProperty(String ID) {

        if(properties.containsKey(ID)){

            properties.remove(ID);

        }

    }



    @Override

    public void add(Property prop) {

        properties.put(prop.getType(),prop);

   }

    

    @Override

    public void setMode(String type){

        this.type = type;

    }

    

    @Override

    public String getMode(){

        return type;

    }



    /**

     * Throws ClassCastException when the parameter is not an Actor.  Comparisons

     * are:

     * <br>

     * <ol><li>String compareTo on type (mode)

     * <li>String compareTo on ID

     * <li>Property comparison: return compareTo on the first pair of properties 

     * that do not return 0

     * <li>Page comparison: return compareTo on the first pair of pages that do not

     * return 0

     * <li>return 0

     * </ol>

     */

    public int compareTo(Object o) {

        Actor right = (Actor)o;

        if(this.type.compareTo(right.getMode())==0){

            if(this.getID().compareTo(right.getID())==0){

                return compareProperties(right);


            }else{

                return this.getID().compareTo(right.getID());

            }

        }else{

            return this.type.compareTo(right.getMode());

        }

    }

    

    /**

     * Compare properties of this actor with the given actor.  Sort the property

     * arrays and compare sequentially, returning the first non-zero value or 

     * returning zero if all properties are equal.

     * 

     * @param right actor to be compared against

     * @return compareTo over all properties

     */

    protected int compareProperties(Actor right){

        List<Property> leftProp = this.getProperty();

        List<Property> rightProp = right.getProperty();

        if(leftProp.size() != rightProp.size()){
            return leftProp.size() - rightProp.size();
        }

        LinkedList<Property> l = new LinkedList<Property>();
        l.addAll(leftProp);
        
        LinkedList<Property> r = new LinkedList<Property>();
        r.addAll(rightProp);
        
        java.util.Collections.sort(l);

        java.util.Collections.sort(r);
        
        Iterator<Property> lIt = l.iterator();
        Iterator<Property> rIt = r.iterator();

        int ret = 0;

        while((lIt.hasNext())&&(rIt.hasNext())){

            ret = lIt.next().compareTo(rIt.next());

            if(ret != 0){

                return ret;

            }

        }


            return 0;


    }

    

    /**

     * Compare pages of this actor with the given actor.  Sort the page arrays

     * and compare sequentially, returning the first non-zero value or returning 

     * zero if all pages are equal.

     * 

     * @param right actor to be compared against

     * @return compareTo over all pages

     */

/*    protected int comparePages(Actor right){

        Page[] l = this.getPage();

        Page[] r = right.getPage();

        if((l==null)&&(r==null)){

            return 0;

        }else if(l==null){

            return -1;

        }else if(r==null){

            return 1;

        }

        java.util.Arrays.sort(l);

        java.util.Arrays.sort(r);

        int i=0;

        int ret = 0;

        while((i<l.length)&&(i<r.length)){

            ret = l[i].compareTo(r[i]);

            if(ret != 0){

                return ret;

            }else{

                ++i;

            }

        }

        if(i<l.length){

           return 1;

        }else if(i<r.length){

            return -1;

        }else{

            return 0;

        }

    }*/



    /**

     * implements equals in terms of CompareTo

     */

    @Override

    public boolean equals(Object obj) {

        if(!(obj instanceof BasicUser)){

            return false;

        }else if(this.compareTo(obj)==0){

            return true;

        }else{

            return false;

        }

    }



    @Override

    public int hashCode() {

        int hash = 3;

        hash = 37 * hash + (this.type != null ? this.type.hashCode() : 0);

        hash = 37 * hash + (this.ID != null ? this.ID.hashCode() : 0);

        hash = 37 * hash + (this.properties != null ? this.properties.hashCode() : 0);

        return hash;

    }



    @Override

    public void setID(String id) {

        this.ID = id;

    }

    

    public String toString(){

        return ID;

    }

    

    @Override

    public Actor prototype(){

        BasicUser a = new BasicUser("");
        return a;

    }

    public void init(Properties properties) {
        ;//deliberate no-op
    }

    

}

