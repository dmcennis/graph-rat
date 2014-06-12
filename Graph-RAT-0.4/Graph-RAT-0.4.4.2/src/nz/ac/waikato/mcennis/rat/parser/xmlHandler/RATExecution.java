/*
 * RATExecution.java
 *
 * Created on 7 October 2007, 15:26
 *
 * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt)
 */

package nz.ac.waikato.mcennis.rat.parser.xmlHandler;

import java.util.regex.Pattern;
import nz.ac.waikato.mcennis.rat.dataAquisition.DataAquisitionFactory;
import nz.ac.waikato.mcennis.rat.graph.GraphFactory;
import nz.ac.waikato.mcennis.rat.graph.algorithm.Algorithm;
import nz.ac.waikato.mcennis.rat.graph.algorithm.AlgorithmFactory;
import nz.ac.waikato.mcennis.rat.parser.ParsedObject;
import nz.ac.waikato.mcennis.rat.scheduler.Scheduler;
import nz.ac.waikato.mcennis.rat.scheduler.SchedulerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Class for parsing a scheduler XML configuration file.  The file is in three parts.
 * The first describes the graph representation, the second describes the data 
 * aquisition modules, and the final section describes the algorithms used.
 *
 * @author Daniel McEnnis
 * 
 */
public class RATExecution extends Handler{
    
    Scheduler scheduler = null;
    
    String className = "";
    
    String propertyName = "";
    
    java.util.Properties props = new java.util.Properties();
    
    State now = State.START;
    
    State type = State.START;
    
    java.util.Vector<Algorithm> algs = new java.util.Vector<Algorithm>();
    
    Pattern pattern = null;
    
    enum State {CONFIG, START, SCHEDULER, PROPERTY, NAME, VALUE, CLASS, PATTERN, GRAPH, DATA, ALGORITHM};
    
    /** Creates a new instance of RATExecution */
    public RATExecution() {
    }
    
    @Override
    public ParsedObject get() {
        return scheduler;
    }
    
    @Override
    public void set(ParsedObject o) {
        scheduler = (Scheduler)o;
    }
    
    @Override
    public Handler duplicate() {
        RATExecution ret = new RATExecution();
        ret.set(scheduler);
        return ret;
    }
    
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if(localName.contentEquals("Config")||qName.contentEquals("Config")){
            now = State.CONFIG;
            type = State.CONFIG;
        }else if(localName.contentEquals("Scheduler")||qName.contentEquals("Scheduler")){
            now = State.SCHEDULER;
            type = State.SCHEDULER;
        }else if(localName.contentEquals("class")||qName.contentEquals("class")){
            now = State.CLASS;
        }else if(localName.contentEquals("property")||qName.contentEquals("property")){
            now = State.PROPERTY;
        }else if(localName.contentEquals("name")||qName.contentEquals("name")){
            now = State.NAME;
        }else if(localName.contentEquals("value")||qName.contentEquals("value")){
            now = State.VALUE;
        }else if(localName.contentEquals("Graph")||qName.contentEquals("Graph")){
            type = State.GRAPH;
            now = State.GRAPH;
        }else if(localName.contentEquals("DataAquisition")||qName.contentEquals("DataAquisition")){
            type = State.DATA;
            now = State.DATA;
        }else if(localName.contentEquals("Algorithm")||qName.contentEquals("Algorithm")){
            type = State.ALGORITHM;
            now = State.ALGORITHM;
        }else if(localName.equalsIgnoreCase("Pattern")||qName.equalsIgnoreCase("Pattern")){
            type = State.ALGORITHM;
            now = State.PATTERN;
        }
    }
    
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if(now == State.CLASS){
            className = new String(ch,start,length);
        } else if(now == State.NAME){
            propertyName = new String(ch,start,length);
        }else if(now == State.VALUE){
            props.setProperty(propertyName,new String(ch,start,length));
        }else if (now == State.PATTERN){
            pattern = Pattern.compile(new String(ch,start,length));
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if(("Scheduler".compareToIgnoreCase(localName)==0)||("Scheduler".compareToIgnoreCase(qName)==0)){
            now = State.CONFIG;
            type = State.CONFIG;
            props.setProperty("scheduler",className);
            scheduler = SchedulerFactory.newInstance().create(props);
            props.clear();
        }else if(("Config".compareToIgnoreCase(localName)==0)||("Config".compareToIgnoreCase(qName)==0)){
            now = State.START;
            type = State.START;
            props.clear();
        }else if(("Graph".compareToIgnoreCase(localName)==0)||("Graph".compareToIgnoreCase(qName)==0)){
            now = State.CONFIG;
            type = State.CONFIG;
            props.setProperty("Graph",className);
            scheduler.set(GraphFactory.newInstance().create(props));
            props.clear();
        }else if(("DataAquisition".compareToIgnoreCase(localName)==0)||("DataAquisition".compareToIgnoreCase(qName)==0)){
            now = State.CONFIG;
            type = State.CONFIG;
            props.setProperty("aquisition",className);
            scheduler.load(DataAquisitionFactory.newInstance().create(props));
            props.clear();
        }else if(("Algorithm".compareToIgnoreCase(localName)==0)||("Algorithm".compareToIgnoreCase(qName)==0)){
            now = State.CONFIG;
            type = State.CONFIG;
            props.setProperty("algorithm",className);
            Algorithm alg = AlgorithmFactory.newInstance().create(props);
            scheduler.load(alg, pattern);
            props.clear();
        }else if(("Property".compareToIgnoreCase(localName)==0)||("Property".compareToIgnoreCase(qName)==0)){
            now = type;
        }else if(("Name".compareToIgnoreCase(localName)==0)||("Name".compareToIgnoreCase(qName)==0)){
            now = State.PROPERTY;
        }else if(("Value".compareToIgnoreCase(localName)==0)||("Value".compareToIgnoreCase(qName)==0)){
            now = State.PROPERTY;
        }else if(("Pattern".compareToIgnoreCase(localName)==0)||("Pattern".compareToIgnoreCase(qName)==0)){
            now = State.PROPERTY;
        }else if(("Class".compareToIgnoreCase(localName)==0)||("Class".compareToIgnoreCase(qName)==0)){
            now = type;
        }
    }


}
