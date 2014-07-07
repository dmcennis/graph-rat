/**
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

import java.io.InputStream;
import org.mcennis.graphrat.crawler.Crawler;
import org.dynamicfactory.descriptors.Parameter;
import org.dynamicfactory.descriptors.Properties;

/**
 * This interface decsribes classes that transforms a data stream into objects.
 * @author Daniel McEnnis
 *
 */
public interface Parser {
    
        /**
         * Parse an input stream into its components
         * @param data data stream to be read
         * @throws Exception
         */
	public void parse(InputStream data, String site) throws Exception;
        
        /**
         * Parse an input stream into its components, spidering using the given crawler
         * @param data
         * @param crawler
         * @throws Exception
         */
	public void parse(InputStream data, Crawler crawler, String site) throws Exception;

        /**
         * Create an exact copy of this object
         * @return new Parser object
         */
        public Parser duplicate();
        
        /**
         * Get the object created by this parser
         * @return parsed object
         */
        public ParsedObject get();
        
        /**
         * Set the parsed object to be loaded
         * @param o object to be loaded
         */
        public void set(ParsedObject o);
        
        /**
         * Give this parser an id that should be globally unique
         * @param name id for this parser
         */
        public void setName(String name);
        
        /*
         * get the unique id associated with this parser
         * @return parsers unique id
         */
        public String getName();
        
        public void  init(Properties parameters);
        
        public Properties getParameter();
        
        public Parameter getParameter(String parameter);
        
        public boolean check(Properties parameters);
        
        public boolean check(Parameter parameter);
}



