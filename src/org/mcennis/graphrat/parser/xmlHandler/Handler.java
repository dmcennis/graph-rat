/*
 * GetParsedObject.java
 *
 * Created on 9 June 2007, 11:08
 *
 * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt)
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

package org.mcennis.graphrat.parser.xmlHandler;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.mcennis.graphrat.crawler.Crawler;
import org.mcennis.graphrat.parser.ParsedObject;
import org.dynamicfactory.descriptors.*;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 *
 * Extends SAX default handler for infrastructure needed to be called by the 
 * XMLParser parser class.
 * @author Daniel McEnnis
 */
public abstract class Handler extends org.xml.sax.helpers.DefaultHandler{

    protected PropertiesInternal properties = PropertiesFactory.newInstance().create();

    protected String site = "";
    
    public Handler(){
        ParameterInternal name = ParameterFactory.newInstance().create("ParserClass",String.class);
        SyntaxObject syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        properties.add(name);

        name = ParameterFactory.newInstance().create("Name",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        properties.add(name);
    }

    public void setSite(String site){
        this.site = site;
    }
    
    /**
     * Return the underlying parsed object
     * @return parsed object
     */
    public abstract ParsedObject get();

    /**
     * Set the underlying parsed object for parsing
     * @param o underlying parsed object
     */
    public abstract void set(ParsedObject o);

    /**
     * Set the crawler to be used for spidering new pages
     * @param crawler crawler to be parsing.
     */
    public void setCrawler(Crawler crawler){}

    /**
     * Create a complete duplicate of this class
     * @return deep copy of the original.
     */
    public abstract Handler duplicate();


    /**
     * Error method for XML parsers
     * @param e error thrown
     * @throws org.xml.sax.SAXException
     */
    @Override
    public void error(SAXParseException e) throws SAXException {

        Logger.getLogger(Handler.class.getName()).log(Level.SEVERE,"Error: "+e.getMessage()+" at line "+e.getLineNumber());

    }

    /**
     * Error method for XML parsers 
     * @param e error thrown
     * @throws org.xml.sax.SAXException
     */
    @Override
    public void fatalError(SAXParseException e) throws SAXException {
        Logger.getLogger(Handler.class.getName()).log(Level.SEVERE,"Fatal Error: "+e.getMessage()+" at line "+e.getLineNumber());
    }

    /**
     * Warning method for XML Parsers
     * @param e warning thrown
     * @throws org.xml.sax.SAXException
     */
    @Override
    public void warning(SAXParseException e) throws SAXException {
        Logger.getLogger(Handler.class.getName()).log(Level.SEVERE,"Warning: "+e.getMessage()+" at line "+e.getLineNumber());
    }
        
    public void setName(String name) {
        properties.get("Name").clear();
        properties.get("Name").add(name);
    }

    public String getName() {
        return (String)properties.get("Name").getValue().iterator().next();
    }

    public void init(Properties parameters) {
        if(properties.check(parameters)){
            properties = properties.merge(parameters);
        }
    }

    public Properties getParameter() {
        return properties;
    }

    public Parameter getParameter(String parameter) {
        return properties.get(parameter);
    }

    public boolean check(Properties parameters) {
        return properties.check(parameters);
    }

    public boolean check(Parameter parameter) {
        return properties.check(parameter);
    }

}

