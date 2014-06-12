/*
 * GoogleArtistHandler.java
 *
 * Created on 8 June 2007, 16:59
 *
 * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt)
 */

package nz.ac.waikato.mcennis.rat.parser;

import java.io.IOException;
import java.io.InputStream;
import nz.ac.waikato.mcennis.rat.crawler.WebCrawler;
import java.util.regex.Matcher;
import nz.ac.waikato.mcennis.rat.crawler.Crawler;
import nz.ac.waikato.mcennis.rat.graph.actor.Actor;
import nz.ac.waikato.mcennis.rat.graph.actor.ActorFactory;

/**
 * Class for parsing the Google music artist search engine results.  Returns null 
 * or not null Actor.
 *
 * @author Daniel McEnnis
 * 
 */
public class GoogleArtistParser implements Parser{
    
    Actor artist;
    String id = "GoogleArtistParser";
    
    /** Creates a new instance of GoogleArtistHandler */
    public GoogleArtistParser() {
    }
    
    @Override
    public void parse(InputStream data) {
        try {
            java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(data));
            StringBuffer content = new StringBuffer();
            String buffer = reader.readLine();
            while((buffer != null)&&(buffer.contentEquals(""))){
                content.append(buffer);
                buffer  = reader.readLine();
            }
            java.util.regex.Pattern number = java.util.regex.Pattern.compile("[0-9] artist");
            java.util.regex.Pattern all = java.util.regex.Pattern.compile("All artist");
            Matcher n = number.matcher(content.toString());
            Matcher a = all.matcher(content.toString());
            if((n.matches())||(a.matches())){
                java.util.Properties props = new java.util.Properties();
                props.setProperty("ActorType","Artist");
                props.setProperty("ActorID","");
                artist = ActorFactory.newInstance().create(props);
            }else{
                artist = null;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    @Override
    public ParsedObject get() {
        return artist;
    }
    
    @Override
    public Parser duplicate() {
        GoogleArtistParser ret = new GoogleArtistParser();
        ret.artist = this.artist;
        return ret;
    }
    
    /**
     * Identical to normal parse - no spidering occuring
     * 
     * @param data data stream to be read
     * @param crawler ignored
     */
    public void parse(InputStream data, Crawler crawler) {
        parse(data);
    }

    @Override
    public void set(ParsedObject o) {
        if(o instanceof Actor){
            this.artist = (Actor)o;
        }
    }

    @Override
    public void setName(String name) {
        id = name;
    }

    @Override
    public String getName() {
        return id;
    }
    
}
