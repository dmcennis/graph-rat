/*
 * GoogleArtistDecider.java
 *
 * Created on 8 June 2007, 14:02
 *
 * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt)
 */

package nz.ac.waikato.mcennis.rat.graph.artist.decider;

import org.mcennis.graphrat.parser.ParsedObject;

/**
 * Common base class for deciding whether or not something belongs to a class
 * or not.  Provides the abstract lookup method to implement class specific lookup
 * code.
 *
 * @author Daniel McEnnis
 * 
 */
public abstract class ArtistDeciderBase implements ArtistDecider, ParsedObject{
    
    java.util.HashSet<String> checked = new java.util.HashSet<String>();
    java.util.HashSet<String> artist = new java.util.HashSet<String>();
 
    /**
     * Only mechanism for getting access to this singleton object.
     * @return static reference to the underlying class
     */
    public static ArtistDeciderBase newInstance(){
        return null;
    }
    
    /** Creates a new instance of GoogleArtistDecider */
    protected ArtistDeciderBase() {
        checked = new java.util.HashSet<String>();
        artist = new java.util.HashSet<String>();
    }
    
    /**
     * 
     * Basic outline of a checked name.
     * @param name string to be checked
     * @return is it an artist or not, potentially after a lookup (if it is an 
     * unchecked string.
     */
    public boolean isArtist(String name){
        if(!isChecked(name.toLowerCase())){
            lookup(name.toLowerCase());
            checked.add(name.toLowerCase());
        }
        return artist.contains(name.toLowerCase());
    }
    
    /**
     * Overridden to provide a means of determining an artist from a non-artist
     * 
     * @param name string to be checked
     */
    protected abstract void lookup(String name);
    
    /**
     * Has this decider seen this particular string before?
     * 
     * @param name name of string
     * @return has this string been seen before.
     */
    protected boolean isChecked(String name){
        return checked.contains(name.toLowerCase());
    }
    
    /**
     * Write this decider to XML.
     * 
     * @param outs place to write the data
     * @throws java.io.IOException
     */
    public void output(java.io.Writer outs) throws java.io.IOException{
        outs.write("<?xml version=\"1.0\">\n");
        outs.write("<!DOCTYPE artistList (string*,artist*)[\n");
        outs.write("\t<!ELEMENT string (#PCDATA)>\n");
        outs.write("\t<!ELEMENT artist (#PCDATA)>\n");
        outs.write("]>\n\n");
        outs.write("<artistList>\n");
        java.util.Iterator<String> i = checked.iterator();
        while(i.hasNext()){
            outs.write("\t<string>");
            outs.write(i.next());
            outs.write("</string>\n");
        }
        i = artist.iterator();
        while(i.hasNext()){
            outs.write("\t<artist>");
            outs.write(i.next());
            outs.write("</artist>\n");
        }
        outs.write("</artistList>");
    }
    
    /**
     * Manually adds an artist to the list of known artists
     * @param a
     */
    public void addArtist(String a){
        artist.add(a.toLowerCase());
        checked.add(a.toLowerCase());
    }
    
    /**
     * Manually adds to the list of seen artists, but not known artists, 
     * effectively labeling it non-artist.
     * @param c non-artist string to be added.
     */
    public void addChecked(String c){
        checked.add(c.toLowerCase());
    }
}
