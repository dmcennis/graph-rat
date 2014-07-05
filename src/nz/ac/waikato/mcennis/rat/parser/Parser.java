/**
 * copyright Daniel McEnnis - published under Aferro GPL (see license.txt)
*/
package nz.ac.waikato.mcennis.rat.parser;

import java.io.InputStream;
import nz.ac.waikato.mcennis.rat.crawler.Crawler;

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



