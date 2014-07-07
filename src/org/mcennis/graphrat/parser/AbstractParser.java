/**
 * AbstractParser
 * Created Jan 29, 2009 - 8:44:37 PM
 * Copyright Daniel McEnnis, see license.txt
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
import org.dynamicfactory.descriptors.*;

/**
 *
 * @author Daniel McEnnis
 */
public abstract class AbstractParser implements Parser{
    
    PropertiesInternal properties = PropertiesFactory.newInstance().create();
    
    public AbstractParser(){
        ParameterInternal name = ParameterFactory.newInstance().create("ParserClass",String.class);
        SyntaxObject syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        properties.add(name);

        name = ParameterFactory.newInstance().create("Name",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        properties.add(name);
    }
    
    @Override
    public void setName(String name) {
        properties.get("Name").clear();
        properties.get("Name").add(name);
    }

    @Override
    public String getName() {
        return (String)properties.get("Name").getValue().iterator().next();
    }

    public void init(Properties parameters) {
        if(properties.check(parameters)){
            
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

    public abstract void parse(InputStream data, String site) throws Exception;

    public abstract void parse(InputStream data, Crawler crawler, String site) throws Exception;

    public abstract Parser duplicate();

    public abstract ParsedObject get();

    public abstract void set(ParsedObject o);

}
