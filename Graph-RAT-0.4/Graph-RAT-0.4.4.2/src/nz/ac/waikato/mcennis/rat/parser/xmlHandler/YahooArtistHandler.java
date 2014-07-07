/*
 * YahooArtistHandler.java
 * 
 * Created on 11/12/2007, 12:50:58
 * 
 * Copyright Daniel McEnnis, see license.txt
 */

package org.mcennis.graphrat.parser.xmlHandler;

import nz.ac.waikato.mcennis.rat.graph.artist.decider.YahooArtistDecider;
import org.mcennis.graphrat.parser.ParsedObject;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Class designed to use parse the XML from the Yahoo! REST service for artist 
 * detection.  The given id is limited to 5000 queries / 24 hour period / IP address.
 *
 * @author Daniel McEnnis
 * 
 */
public class YahooArtistHandler extends Handler{

    YahooArtistDecider yaDecider = null;
    
    enum State  {START,RESULT_SET,RESULT,NAME,THUMBNAIL,RELATED_ARTISTS,POPULAR_SONGS,YAHOO_MUSIC_PAGE,ARTIST};
    
    State state = State.START;
    
    public YahooArtistHandler() {
    }

    public ParsedObject get() {
        return yaDecider;
    }

    public void set(ParsedObject o) {
        yaDecider = (YahooArtistDecider)o;
    }

    public Handler duplicate() {
       YahooArtistHandler ret = new YahooArtistHandler();
       ret.yaDecider = this.yaDecider;
       return ret;
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        state = State.START;
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if(localName.equalsIgnoreCase("resultset")||(qName.equalsIgnoreCase("resultset"))){
            state = State.RESULT_SET;
        }else if (localName.equalsIgnoreCase("result")||(qName.equalsIgnoreCase("result"))){
            state = State.RESULT;
        }else if (localName.equalsIgnoreCase("name")||(qName.equalsIgnoreCase("name"))){
            state = State.NAME;
        }else if (localName.equalsIgnoreCase("relatedartists")||(qName.equalsIgnoreCase("relatedartists"))){
            state = State.RELATED_ARTISTS;
        }else if (localName.equalsIgnoreCase("artist")||(qName.equalsIgnoreCase("artist"))){
            state = State.ARTIST;
        }
        
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
       if(localName.equalsIgnoreCase("resultset")||(qName.equalsIgnoreCase("resultset"))){
            state = State.START;
        }else if (localName.equalsIgnoreCase("result")||(qName.equalsIgnoreCase("result"))){
            state = State.RESULT_SET;
        }else if (localName.equalsIgnoreCase("name")||(qName.equalsIgnoreCase("name"))){
            state = State.RESULT;
        }else if (localName.equalsIgnoreCase("relatedartists")||(qName.equalsIgnoreCase("relatedartists"))){
            state = State.RESULT;
        }else if (localName.equalsIgnoreCase("artist")||(qName.equalsIgnoreCase("artist"))){
            state = State.RELATED_ARTISTS;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if(state == State.ARTIST){
            yaDecider.addChecked((new String(ch,start,length)).toLowerCase());
            yaDecider.addArtist((new String(ch,start,length)).toLowerCase());
        }else if(state == State.NAME){
            yaDecider.addChecked((new String(ch,start,length)).toLowerCase());
            yaDecider.addArtist((new String(ch,start,length)).toLowerCase());           
        }
    }
    
    

}
