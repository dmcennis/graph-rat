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

import java.util.logging.Level;
import java.util.logging.Logger;
import org.dynamicfactory.descriptors.*;
import org.dynamicfactory.AbstractFactory;

/**
 * Class for creating Parsers
 * @author Daniel McEnnis
 * 
 */
public class ParserFactory extends AbstractFactory<Parser>{
    
    private static ParserFactory instance = null;
    
    /**
     * Create a new reference to the ParserFactory singelton
     * @return new reference
     */
    static public ParserFactory newInstance(){
        if(instance == null){
            instance = new ParserFactory();
        }
        return instance;
    }
    
    protected ParserFactory(){
        ParameterInternal name = ParameterFactory.newInstance().create("ParserClass",String.class);
        SyntaxObject syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("MemGraph");
        properties.add(name);
        
        map.put("BaseHTMLParser",new BaseHTMLParser());
        map.put("ToFileParser",new ToFileParser());
        map.put("YahooArtistDecider",new XMLParser());
        map.put("BasicScheduler",new XMLParser());
        map.put("FOAF",new XMLParser());
        map.put("LastFMArtistTag",new XMLParser());
        map.put("MemGraph",new XMLParser());
        map.put("ParseClassLabel",new XMLParser());
        map.put("QueryReader",new XMLParser());
    }
    
    @Override
    public Parser create(Properties props) {
        return create(null,props);
    }
    
    public Parser create(String name){
        return create(name,properties);
    }
    
    
    public Parser create(String classType, Properties parameters){
        if(classType != null){
        if ((parameters.get("AlgorithmClass") != null) && (parameters.get("AlgorithmClass").getParameterClass().getName().contentEquals(String.class.getName()))) {
            classType = (String) parameters.get("AlgorithmClass").getValue().iterator().next();
        } else {
            classType = (String) properties.get("AlgorithmClass").getValue().iterator().next();
        }
        }
        Parser ret = null;
        if(map.containsKey(classType)){
            ret = (Parser)map.get(classType).duplicate();
        }else{
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE,"Algorithm class '"+classType+"' nbot found - assuming AggregateLinks");
            ret = new XMLParser();
        }
        ret.init(parameters);
        return ret;
    }

    @Override
    public boolean check(Properties parameters) {
        String classType = "";
        if ((parameters.get("ParserClass") != null) && (parameters.get("ParserClass").getParameterClass().getName().contentEquals(String.class.getName()))) {
            classType = (String) parameters.get("ParserClass").getValue().iterator().next();
        } else {
            classType = (String) properties.get("ParserClass").getValue().iterator().next();
        }

        if(map.containsKey(classType)){
            return map.get(classType).getParameter().check(parameters);
        }else{
            Logger.getLogger(this.getClass().getName()).log(Level.INFO,"Parser '"+classType+"' does not exist");
            return false;
        }
    }
    
    @Override
    public Properties getParameter(){
        String classType = (String) properties.get("ParserClass").getValue().iterator().next();
        if(map.containsKey(classType)){
            return map.get(classType).getParameter();
        }else{
            return properties;
        }
    }

    @Override
    public Parameter getClassParameter() {
        return properties.get("ParserClass");
    }
    /**
     * Create Parsers using the given properties file as parameters
     * 
     * Parser Class is determined by the 'ParserType' key
     * 
     * <ul>
     * <li>HTML</li>
     * <li>File<ul><li>'ToFileDirectory' - directory where files should be stored</li></ul></li>
     * <li>Scheduler - see HandlerFactory-RATExecution for more parameters</li>
     * <li>FOAF - see HandlerFactory-FOAF for more parameters</li>
     * <li>Graph - see HandlerFactory-Graph for more parameters</li>
     * <li>GoogleArtist</li>
     * <li>LastFmArtist <ul>
     *  <li>'LastFMType' - LastFM xml parser if XML, Basic bio parser otherwise</li>
     * </ul></li>
     * <li>BaseArtistDecider - ArtistDecider created - see ArtistDeciderFactory</li>
     * <li>ArtistDeciderXML</li>
     * <li>YahooArtistDecider</li>
     * <li>LastFMArtistTag - see HandlerFactory-LastFMArtistTag for parameters</li>
     * </ul>
     * @param props
     * @return
     */
}