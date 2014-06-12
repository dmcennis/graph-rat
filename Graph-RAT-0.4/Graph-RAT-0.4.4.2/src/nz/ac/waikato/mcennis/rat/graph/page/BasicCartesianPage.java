/*
 * BasicCartesianPage.java
 *
 * Created on 1 May 2007, 16:18
 *
 * copyright Daniel McEnnis - published under Aferro GPL (see license.txt)
 */

package nz.ac.waikato.mcennis.rat.graph.page;

import java.util.HashMap;

/**
 * Class representing a bag-of-words representation of a page.
 *
 * @author Daniel McEnnis
 */
public class BasicCartesianPage implements Page{
    static final long serialVersionUID = 1;
    
    private java.util.HashMap<String,Double> histogram;
    private String ID;
    
    /** Creates a new instance of BasicCartesianPage 
     * @param ID 
     */
    public BasicCartesianPage(String ID) {
        histogram = new java.util.HashMap<String,Double>();
        this.ID = ID;
    }
    
    @Override
    public String getID(){
        return ID;
    }

    @Override
    public java.util.HashMap<String,Double> getHistogram(){
        return histogram;
    }
    
    /**
     * Only does comparisons of other objects of identical class.
     */
    public double compare(Page p) {
        if(p instanceof BasicCartesianPage){
            BasicCartesianPage page = (BasicCartesianPage)p;
            return this.compare(page);
        }
        return Double.NaN;
    }
    
    /**
     * Implements an internal compare.
     * @param p 
     * @return 
     */
    public double compare(BasicCartesianPage p){
        double ret = 0.0;
        java.util.HashMap<String,Double> right = p.getHistogram();
        
        java.util.Iterator<String> it = histogram.keySet().iterator();
        while(it.hasNext()){
            String key = it.next();
            if(right.containsKey(key)){
                ret += (double)histogram.get(key) * (double)right.get(key);
            }
        }
        
        return ret;
    }

    @Override
    public void load(String content) {
        		java.util.regex.Pattern tag = java.util.regex.Pattern.compile("<[^>]*>");
		java.util.regex.Pattern whitespace = java.util.regex.Pattern.compile("[^a-zA-Z]+");
		String[] non_tagged = tag.split(content);
		for(int i=0;i<non_tagged.length;++i){
			String[] temp = whitespace.split(non_tagged[i]);
			for(int j=0;j<temp.length;++j){
				if(temp[j].length() > 0){
					if(histogram.containsKey(temp[j])){
						histogram.put(temp[j], new Double(histogram.get(temp[j])+1.0));
					}else{
						histogram.put(temp[j], 1.0);
					}
				}
			}
		}

    }

    @Override
    public void load(HashMap<String, Double> histogram) {
        histogram.putAll(histogram);
    }
    
    /**
     * FIXME: Not implemented yet
     */
    public int compareTo(Object o) {
        //TODO: write compareTo
        return 0;
    }
    
}
