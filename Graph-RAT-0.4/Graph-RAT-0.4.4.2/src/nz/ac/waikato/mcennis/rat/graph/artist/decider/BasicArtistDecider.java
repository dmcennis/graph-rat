/* * BasicArtistDecider.java * * Created on 12 June 2007, 13:26 * * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt) */package nz.ac.waikato.mcennis.rat.graph.artist.decider;/** * Class for manually assigned artist labels. * * @author Daniel McEnnis *  */public class BasicArtistDecider extends ArtistDeciderBase{        /** Creates a new instance of BasicArtistDecider */    public BasicArtistDecider() {    }        /**     * Intentionally a no-op - if it is lont already known, it isn't an artist     * @param name ignored     */    public void lookup(String name){    }        /**     * Intentionally a no-op - no network access is done, so proxy configurations     * are unneeded.     * @param proxy ignored     */    public void setProxy(boolean proxy){    }}