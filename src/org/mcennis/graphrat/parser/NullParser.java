/*

 * NullParser.java

 *

 * Created on 3 May 2007, 15:25

 *

 * copyright Daniel McEnnis - published under Aferro GPL (see license.txt)

 */

/*
 *   This file is part of GraphRAT.
 *
 *   GraphRAT is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   GraphRAT is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with GraphRAT.  If not, see <http://www.gnu.org/licenses/>.
 */


package org.mcennis.graphrat.parser;



import java.io.IOException;

import java.io.InputStream;

import org.mcennis.graphrat.crawler.Crawler;



/**

 * Intentionally a do-nothing parser.  

 *

 * @author Daniel McEnnis

 * 

 */

public class NullParser extends AbstractParser{

    

    String id = "NullParser";

    public String content = null;

    /** Creates a new instance of NullParser */

    public NullParser() {

        super();

        properties.get("Name").add("NullParser");

        properties.get("ParserClass").add("NullParser");

    }



    /**

     * Reads the content into a String, otherwise a no-op

     * @param data read into a string

     */

    public void parse(InputStream data, String site) {

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

        NullParser ret = new NullParser();

        ret.properties = this.properties.duplicate();

        return ret;

    }



    /**

     * Identical to parse

     * @param data read into string 

     * @param crawler ignored

     */

    public void parse(InputStream data, Crawler crawler, String site) {

        parse(data, site);

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



}

