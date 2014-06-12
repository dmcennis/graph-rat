/*
 * Page.java
 *
 * Created on 1 May 2007, 14:16
 *
 * copyright Daniel McEnnis - published under Aferro GPL (see license.txt)
 */

package nz.ac.waikato.mcennis.rat.graph.page;

/**
 * Class for describing a text document.  Pending application of generic properties
 * on graphs and actors, this represents text-based documents.
 *
 * @author Daniel McEnnis
 * 
 */
public interface Page extends java.io.Serializable, Comparable {
    /**
     * Compute a similarity between 2 pages.
     * 
     * @param p page to compare to
     * @return similarity
     */
    public double compare(Page p);
    
    /**
     * Create a page document fromthe given string.
     * 
     * @param content contents of the text document
     */
    public void load(String content);
    
    /**
     * Create a page given the bag-of-words description.
     * 
     * @param histogram bag-of-words description of a text document
     */
    public void load(java.util.HashMap<String,Double> histogram);
    
    /**
     * Retrieve the histogram describing this document.
     * @return 
     */
    public java.util.HashMap<String,Double> getHistogram();
    
    /**
     * get the unique id identifying this page
     * @return unique id
     */
    public String getID();
}
