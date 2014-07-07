/**
 * GraphCompare
 * Created Jan 5, 2009 - 9:46:14 PM
 * Copyright Daniel McEnnis, see license.txt
 */
package org.mcennis.graphrat.graph;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author Daniel McEnnis
 */
public class GraphCompare {
    static public int compareTo(Graph left, Object rightO){
        if (left.getClass().getName().compareTo(rightO.getClass().getName())==0) {
            Graph right = (Graph)rightO;
            LinkedList<String>  leftTypes = new LinkedList<String>();
            LinkedList<String> rightTypes = new LinkedList<String>();
            
            leftTypes.addAll(left.getActorTypes());
            Collections.sort(leftTypes);
            rightTypes.addAll(right.getActorTypes());
            Collections.sort(rightTypes);
            int result = compareStrings(leftTypes, rightTypes);
            if (result != 0) {
                return result;
            }
            
            LinkedList<Comparable> leftObjects = new LinkedList<Comparable>();
            LinkedList<Comparable> rightObjects = new  LinkedList<Comparable>();

            Iterator<String> types = leftTypes.iterator();
            while(types.hasNext()){
                String type = types.next();
                leftObjects.addAll(left.getActor(type));
                Collections.sort(leftObjects);
                rightObjects.addAll(right.getActor(type));
                Collections.sort(rightObjects);
                result = compareComparables(leftObjects,rightObjects);
                if(result!=0){
                    return result;
                }
                leftObjects.clear();
                rightObjects.clear();
            }
            leftTypes.clear();
            rightTypes.clear();
            
            
            leftTypes.addAll(left.getLinkTypes());
            Collections.sort(leftTypes);
            rightTypes.addAll(right.getLinkTypes());
            Collections.sort(rightTypes);
            result = compareStrings(leftTypes, rightTypes);
            if (result != 0) {
                return result;
            }
            
            types = leftTypes.iterator();
            while(types.hasNext()){
                String type = types.next();
                leftObjects.addAll(left.getLink(type));
                Collections.sort(leftObjects);
                rightObjects.addAll(right.getLink(type));
                Collections.sort(rightObjects);
                result = compareComparables(leftObjects,rightObjects);
                if(result!=0){
                    return result;
                }
                leftObjects.clear();
                rightObjects.clear();
            }            
            return 0;
        } else {
            return left.getClass().getName().compareTo(rightO.getClass().getName());
        }
    }
    
    static int compareComparables(LinkedList<Comparable> left, LinkedList<Comparable> right) {
        if(left.size() != right.size()){
            return left.size() - right.size();
        }else{
            Iterator<Comparable> lIt = left.iterator();
            Iterator<Comparable> rIt = right.iterator();
            while(lIt.hasNext()){
                Comparable l = lIt.next();
                Comparable r = rIt.next();
                if(l.compareTo(r)!=0){
                    return l.compareTo(r);
                }
            }
            return 0;
        }
   }
    
    static int compareStrings(LinkedList<String> left, LinkedList<String> right) {
        if(left.size() != right.size()){
            return left.size() - right.size();
        }else{
            Iterator<String> lIt = left.iterator();
            Iterator<String> rIt = right.iterator();
            while(lIt.hasNext()){
                String l = lIt.next();
                String r = rIt.next();
                if(l.compareTo(r)!=0){
                    return l.compareTo(r);
                }
            }
            return 0;
        }
   }
    

}
