/*
 * Copyright Daniel McEnnis, see license.txt
 */

package nz.ac.waikato.mcennis.rat.util;


/**
 *  Class for ordering pairs of objects so that they stay associated with each other
 * after sorting.
 * 
 * @author Daniel McEnnis
 */
public class Duples<L,R> implements Comparable{
    L left = null;
    R right = null;
    
    public Duples(){}
    
    public Duples(L l, R r){
        left = l;
        right = r;
    }

    /**
     * Returns -1 if class L is not comparable, otherwise returns the output of 
     * left.compareTo(o.left)
     * @param o Duples to be compared to
     * @return
     */
    public int compareTo(Object o) {
        if(o==null){
            return 1;
        }
        Duples param = (Duples)o;
        if((param.left==null)&&(left==null)){
            if(this.right != null){
                return ((Comparable)this.right).compareTo(param.right);
            }else if(param.right != null){
                return -1;
            }else{
                return 0;
            }
        }
        
        if((left == null)&&(param.left!=null)){
            return -1;
        }
        if(((Comparable)left).compareTo(param.left) == 0){
            if((right==null)&&(param.right==null)){
                return 0;
            }else if((right==null)&&(param.right!=null)){
                return -1;
            }else{
                return ((Comparable)right).compareTo(param.right);
            }
        }else{
            return ((Comparable)left).compareTo(param.left);
        }
        
    }
    
    /**
     * get left object in the duple
     * @return left item
     */
    public L getLeft(){
        return left;
    }
    
    /**
     * get right object in the duple
     * @return right object
     */
    public R getRight(){
        return right;
    }
    
    /**
     * set the left object with this object
     * @param l object to be set
     */
    public void setLeft(L l){
        left = l;
    }
    
    /**
     * set the right object with this object
     * @param r object to be set
     */
    public void setRight(R r){
        right = r;
    }
}
