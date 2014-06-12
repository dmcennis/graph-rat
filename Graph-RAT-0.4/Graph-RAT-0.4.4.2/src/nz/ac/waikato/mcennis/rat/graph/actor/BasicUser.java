/*
 * BasicUser.java
 *
 * Created on 1 May 2007, 15:12
 *
 * copyright Daniel McEnnis - published under Aferro GPL (see license.txt)
 */

package nz.ac.waikato.mcennis.rat.graph.actor;

import nz.ac.waikato.mcennis.rat.graph.model.ModelShell;
import nz.ac.waikato.mcennis.rat.graph.page.Page;
import nz.ac.waikato.mcennis.rat.graph.property.Property;

/**
 *
 * @author Daniel McEnnis
 */
public class BasicUser extends ModelShell implements Actor {
    
   static final long  serialVersionUID = 1;

    String type = "User";
    String ID;
    java.util.HashMap<String,Property> properties;
    java.util.HashMap<String,Page> pages;
    
    /** Creates a new instance of BasicUser */
    public BasicUser(String ID) {
        this.ID = ID;
        properties = new java.util.HashMap<String, Property>();
         pages = new java.util.HashMap<String,Page>();
    }


    @Override
    public String getID() {
        return ID;
    }

    @Override
    public Page[] getPage() {
        if(pages.size()>0){
        return pages.values().toArray(new Page[]{});
        }else{
            return null;
        }
    }
    
    @Override
    public void add(Page p){
        pages.put(p.getID(),p);
    }

    @Override
    public Property[] getProperty() {
        if(properties.size()>0){
            return properties.values().toArray(new Property[]{});
        }else{
            return null;
        }
    }

    @Override
    public Page getPage(String ID) {
        return pages.get(ID);
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
    public void setType(String type){
        this.type = type;
    }
    
    @Override
    public String getType(){
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
        if(this.type.compareTo(right.getType())==0){
            if(this.getID().compareTo(right.getID())==0){
                int ret = compareProperties(right);
                if(ret==0){
                    return comparePages(right);
                    
                }else
                    return ret;
            }else{
                return this.getID().compareTo(right.getID());
            }
        }else{
            return this.type.compareTo(right.getType());
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
        Property[] l = this.getProperty();
        Property[] r = right.getProperty();
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
    }
    
    /**
     * Compare pages of this actor with the given actor.  Sort the page arrays
     * and compare sequentially, returning the first non-zero value or returning 
     * zero if all pages are equal.
     * 
     * @param right actor to be compared against
     * @return compareTo over all pages
     */
    protected int comparePages(Actor right){
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
    }

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
        hash = 37 * hash + (this.pages != null ? this.pages.hashCode() : 0);
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
    public Actor duplicate(){
        BasicUser a = new BasicUser(ID);
        a.setType(this.getType());
        java.util.Iterator<Property> it = properties.values().iterator();
        while(it.hasNext()){
            a.add(it.next().duplicate());
        }
        return a;
    }
    
}
