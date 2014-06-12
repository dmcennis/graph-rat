/*
 * NullParser.java
 *
 * Created on 3 May 2007, 15:25
 *
 * copyright Daniel McEnnis - published under Aferro GPL (see license.txt)
 */

package nz.ac.waikato.mcennis.rat.parser;

import java.io.IOException;
import java.io.InputStream;
import nz.ac.waikato.mcennis.rat.crawler.Crawler;
import nz.ac.waikato.mcennis.rat.crawler.WebCrawler;

/**
 * Intentionally a do-nothing parser.  
 *
 * @author Daniel McEnnis
 * 
 */
public class NullParser implements Parser{
    
    String id = "NullParser";
    public String content = null;
    /** Creates a new instance of NullParser */
    public NullParser() {
    }

    /**
     * Reads the content into a String, otherwise a no-op
     * @param data read into a string
     */
    public void parse(InputStream data) {
        java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(data));
        StringBuffer source = new StringBuffer();
        try {
            String buffer = null;
            buffer = reader.readLine();
            while((buffer != null)&&(!buffer.contentEquals(""))){
                source.append(buffer).append("\r\n");
                buffer = reader.readLine();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        this.content = source.toString();
    }

    @Override
    public Parser duplicate() {
        return new NullParser();
    }

    /**
     * Identical to parse
     * @param data read into string 
     * @param crawler ignored
     */
    public void parse(InputStream data, Crawler crawler) {
        parse(data);
    }
    
    /**
     * Intentionally a no-op
     * @return null
     */
    public ParsedObject get(){
        return null;
    }

    /**
     * Intentionally a no-op
     * @param o ignored
     */
    public void set(ParsedObject o) {
        ;//intentionally null
    }

    public void setName(String name) {
        id = name;
    }

    public String getName() {
        return id;
    }
}
