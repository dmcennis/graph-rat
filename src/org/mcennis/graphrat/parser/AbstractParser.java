/**
 * AbstractParser
 * Created Jan 29, 2009 - 8:44:37 PM
 * Copyright Daniel McEnnis, see license.txt
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
