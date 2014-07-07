/*
 * ArtistDecider.java
 *
 * Created on 8 June 2007, 14:01
 *
 * Copright Daniel McEnnis, published under Aferro GPL (see license.txt)
 */

package nz.ac.waikato.mcennis.rat.graph.artist.decider;

import org.mcennis.graphrat.parser.ParsedObject;

/**
 * Interface for providing a binary responce to whether a given String belongs
 * to the class this class differentiates.  It is called ArtistDecider because 
 * its first implementation is in deciding whether or not a String based property
 * is an artist's name or not.
 *
 * @author Daniel McEnnis
 * 
 */
public interface ArtistDecider extends ParsedObject{
    
    /**
     * Does this String belong to this class
     * @param name string to be checked
     * @return does it belong (is an artist's name)
     */
    public boolean isArtist(String name);
    
    /**
     * Write this object's data on artist or not to an XML file.
     * 
     * @param outs output writer to write to
     * @throws java.io.IOException
     */
    public void output(java.io.Writer outs) throws java.io.IOException;
    
    /**
     * Should output be done using a proxy or not.
     * 
     * @param p
     */
    public void setProxy(boolean p);
    
    /**
     * Add this string as not belonging to the class (not an artist)
     * @param name non-artist to be added.
     */
    public void addChecked(String name);
    
    /**
     * Add this string to the list of those belonging (is an artist name)
     * 
     * @param name string to be added
     */
    public void addArtist(String name);
}
