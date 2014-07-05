/*

 * ArtistParser.java

 *

 * Created on 3 May 2007, 22:08

 *

 * Copyright Daniel McEnnis - published under Aferro GPL (see license.txt)

 */



package nz.ac.waikato.mcennis.rat.parser;



import java.io.IOException;

import java.io.InputStream;

import nz.ac.waikato.mcennis.rat.crawler.Crawler;

import nz.ac.waikato.mcennis.rat.graph.actor.Actor;

import nz.ac.waikato.mcennis.rat.graph.actor.ActorFactory;


/**

 * Class for parsing actor biographies on LastFM.  NOTE: Use with caution.  Contact

 * the LastFM webiste owners for permission first.

 *

 * @author Daniel McEnnis

 * 

 */

public class LastFmBasicArtistParser extends AbstractParser{

    

    Actor artist;

    String id = "LastFmBasicArtistParser";

    

    /** Creates a new instance of ArtistParser */

    public LastFmBasicArtistParser() {

        super();

        properties.get("ParserClass").add("LastFmBasicArtistParser");

        properties.get("Name").add("LastFmBasicArtistParser");

    }

    

    @Override

    public void parse(InputStream data, String site) {

        java.io.BufferedReader inp = new java.io.BufferedReader(new java.io.InputStreamReader(data));

        try {

            String id = inp.readLine();

            if(id != null){

                java.util.regex.Pattern patt = java.util.regex.Pattern.compile(".*No artist exists with this name.*");

                java.util.regex.Matcher match = patt.matcher(id);

                if(match.matches()){

                    artist = null;

                }else{

                    artist = ActorFactory.newInstance().create("Artist","");

                }

            }else{

                artist = null;

            }

        } catch (IOException ex) {

            artist=null;

        }

    }

    

    @Override

    public Parser duplicate() {

        LastFmBasicArtistParser ret = new LastFmBasicArtistParser();

        ret.properties = this.properties.duplicate();

        return ret;

    }

    

    /**

     * Identical to parse without crawler

     * @param data data to be parsed

     * @param crawler ignored

     */

    public void parse(InputStream data, Crawler crawler, String site) {

        parse(data,site);

    }

    

    @Override

    public ParsedObject get(){

        return artist;

    }



    @Override

    public void set(ParsedObject o) {

        if(o instanceof Actor){

            this.artist = (Actor)o;

        }

    }



}

