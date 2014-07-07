/**


 * copyright Daniel McEnnis, published under Aferro GPL (see license.txt)


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

package org.mcennis.graphrat.crawler;





import org.mcennis.graphrat.parser.Parser;


import java.net.MalformedURLException;


import java.io.IOException;





/**


 * Interface for accessing files via io.  Designed to abstract away the 


 * difference between file and web access.  Utilizes parsing objects to parse 


 * the documents collected.  Multiple parser can be utilized. It is crawler 


 * dependant whether all parsers are used against all documents or only a 


 * subset.


 *


 * @author Daniel McEnnis


 *


 *


 */


public interface Crawler {


        /**


     * fetches the site designated by site.  The meaning and interpretation 


     * of 'site' is dependant on the cralwer used.  This can spark new


     * secondary crawls depending on the crawler.


     * @param site Name of the document to be fetched


     * @throws java.net.MalformedURLException If the site to crawl is not a valid document.  Only thrown if the 


     * underlying crawler is retrieving documents via a http or similar 


     * protocol.


     * @throws java.io.IOException Thrown if their is a problem retrieving the document to be processed.


     */


	public void crawl(String site) throws MalformedURLException,IOException;


        


        /**


        * 


     * @param site Name of the document to be fetched


     * @param parsers index of parsers to parse this site


     * @throws java.net.MalformedURLException If the site to crawl is not a valid document.  Only thrown if the 


     * underlying crawler is retrieving documents via a http or similar 


     * protocol.


     * @throws java.io.IOException Thrown if their is a problem retrieving the document to be processed.


         */


        public void crawl(String site,String[] parsers) throws MalformedURLException,IOException;





     


//        public void crawl(String site, String[] parsers, Crawler crawler)


    /**


     * Set the parsers that are to be utilized by this crawler to interpret the


     * documents that are parsed.


     * @param parser Array of parsing objects to be utilized by the crawler to process documents fetched


     */


	public void set(Parser[] parser);


    /**


     * Returns an array of Parser objects used by this crawler to parse pages.


     * @return Array of parsers utilized to parse fetched documents.


     */


        public Parser[] getParser();


    /**


     * Establishes whether a proxy is needed to access documents


     * @param proxy Whether or not a proxy is needed for accessing documents


     */


        public void setProxy(boolean proxy);


        


        /**


         * Set whether or notthe crawler should cache the web page or reload for each individual parser.


         * This is a tradeof between memory needed for loading potentially large files and the


         * cost of continually reloading the web page


         * 


         * @param b should caching occur


         */


        public void setCaching(boolean b);


        


        /**


         * Is the crawler caching the page or is it re-acquiring the page for each parser.


         * 


         * @return is caching enabled


         */


        public boolean isCaching();


        


        /**


         * Should links to new documents discovered also be read


         * 


         * @param spider


         */


        public void setSpidering(boolean spider);


        


        /**


         * Is this crawler following links


         * @return follows links or not


         */


        public boolean isSpidering();


}


