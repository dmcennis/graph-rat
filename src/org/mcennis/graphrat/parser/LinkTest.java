/*

 * LinkTest.java

 *

 * Created on 3 May 2007, 15:30

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

import java.net.MalformedURLException;

import java.util.logging.Level;

import java.util.logging.Logger;

import org.mcennis.graphrat.crawler.Crawler;



/**

 * Dummy class designed purely to test spidering capabilities

 *

 * @author Daniel McEnnis

 * 

 */

public class LinkTest extends AbstractParser{

    

    String id = "LinkTest";

    /** Creates a new instance of LinkTest */

    public LinkTest() {

        super();

        properties.get("Name").add("LinkTest");

        properties.get("ParserClass").add("LinkTest");

    }



    /**

     * Intentionally a no-op

     * @param data ignored

     */

    public void parse(InputStream data,String site) {

        //deliberately empty

        ;

    }



    @Override

    public Parser duplicate() {

        LinkTest ret = new LinkTest();

        ret.properties = this.properties.duplicate();

        return ret;

    }



    /**

     * Parses CCL website - ignores data

     * @param data ignores

     * @param crawler crawler to be tested

     */

    public void parse(InputStream data, Crawler crawler, String site) {

        try {

            crawler.crawl("http://www.ccl.net");

        } catch (MalformedURLException ex) {

            Logger.getLogger(LinkTest.class.getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {

            Logger.getLogger(LinkTest.class.getName()).log(Level.SEVERE, null, ex);

        }

    }



    /**

     * Returns null

     * @return null

     */

    public ParsedObject get(){

        return null;

    }



    /**

     * Deliberately a no-op

     * @param o ignored

     */

    public void set(ParsedObject o) {

        ;//intentionally null

    }



}

