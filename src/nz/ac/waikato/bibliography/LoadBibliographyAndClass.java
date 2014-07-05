/*

 * LoadBibliographyXML.java

 * 

 * Created on 8/01/2008, 14:04:48

 * Copyright Daniel McEnnis, see license.txt

 */



package nz.ac.waikato.bibliography;



import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import java.util.logging.Logger;

import nz.ac.waikato.mcennis.rat.crawler.FileListCrawler;

import nz.ac.waikato.mcennis.rat.dataAquisition.DataAquisition;

import nz.ac.waikato.mcennis.rat.graph.Graph;

import org.dynamicfactory.descriptors.IODescriptorFactory;

import org.dynamicfactory.descriptors.IODescriptor;

import org.dynamicfactory.descriptors.IODescriptor.Type;
import org.dynamicfactory.descriptors.Parameter;

import org.dynamicfactory.descriptors.ParameterFactory;
import org.dynamicfactory.descriptors.ParameterInternal;

import org.dynamicfactory.descriptors.Properties;
import org.dynamicfactory.descriptors.PropertiesFactory;
import org.dynamicfactory.descriptors.PropertiesInternal;
import org.dynamicfactory.descriptors.SyntaxCheckerFactory;
import org.dynamicfactory.descriptors.SyntaxObject;
import nz.ac.waikato.mcennis.rat.graph.model.ModelShell;

import nz.ac.waikato.mcennis.rat.graph.query.ActorQuery;
import nz.ac.waikato.mcennis.rat.graph.query.LinkQuery;
import nz.ac.waikato.mcennis.rat.graph.query.Query;
import nz.ac.waikato.mcennis.rat.parser.Parser;

import nz.ac.waikato.mcennis.rat.parser.ParserFactory;

import nz.ac.waikato.mcennis.rat.scheduler.Scheduler;



/**

 * Loads two XML files describing a set of papers and their authors.  See ParseBibliographyXML 

 * for the structure of the base XML file and ParseClassLabel for the structure 

 * of the ground truth XML file.<br/>

 * <br/>

 * Once the two files are read into the graph, the graph will contain two actor 

 * types representing papers and the authors that write them.  See init() for a 

 * detailed description of the constructed graph.<br/>

 * 

 * @author Daniel McEnnis

 */

public class LoadBibliographyAndClass extends ModelShell implements DataAquisition {

    PropertiesInternal properties = PropertiesFactory.newInstance().create();

    Graph graph = null;
    LinkedList<IODescriptor> input = new LinkedList<IODescriptor>();
    LinkedList<IODescriptor> output = new LinkedList<IODescriptor>();

    

    public LoadBibliographyAndClass() {
        ParameterInternal name = ParameterFactory.newInstance().create("AlgorithmClass",String.class);
        SyntaxObject syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("Load Bibliography And Class");
        properties.add(name);

        name = ParameterFactory.newInstance().create("Name",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,Integer.MAX_VALUE,null,String.class);
        name.setRestrictions(syntax);
        name.add("Load Bibliography And Class");
        properties.add(name);

        name = ParameterFactory.newInstance().create("Category",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("Bibliography");
        properties.add(name);

        name = ParameterFactory.newInstance().create("LinkFilter",LinkQuery.class);
        syntax = SyntaxCheckerFactory.newInstance().create(0,1,null,LinkQuery.class);
        name.setRestrictions(syntax);
        properties.add(name);


        name = ParameterFactory.newInstance().create("ActorFilter",ActorQuery.class);
        syntax = SyntaxCheckerFactory.newInstance().create(0,1,null,Query.class);
        name.setRestrictions(syntax);
        properties.add(name);

        name = ParameterFactory.newInstance().create("PaperFileLocation",File.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,File.class);
        name.setRestrictions(syntax);
        properties.add(name);

        name = ParameterFactory.newInstance().create("ClassFileLocation",File.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,File.class);
        name.setRestrictions(syntax);
        properties.add(name);

        name = ParameterFactory.newInstance().create("AuthorMode",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("Author");
        properties.add(name);

        name = ParameterFactory.newInstance().create("PaperMode",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("Paper");
        properties.add(name);

        name = ParameterFactory.newInstance().create("WroteRelation",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("Authored");
        properties.add(name);

        name = ParameterFactory.newInstance().create("ReferencesRelation",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("References");
        properties.add(name);

        name = ParameterFactory.newInstance().create("BidirectionalReferences",Boolean.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,Boolean.class);
        name.setRestrictions(syntax);
        name.add(false);
        properties.add(name);
    }



    @Override

    public void start() {

        PropertiesInternal parserProperties = PropertiesFactory.newInstance().create();
        ParserFactory.newInstance().getParameter("ParserClass").clear();
        ParserFactory.newInstance().getParameter("ParserClass").add("ParseBibliographyXML");

        parserProperties.merge(ParserFactory.newInstance().getParameter());
        parserProperties.merge(properties);

        Parser[] parser = new Parser[]{ParserFactory.newInstance().create(parserProperties)};

        parser[0].set(graph);

        fireChange(Scheduler.SET_ALGORITHM_COUNT,2);

        FileListCrawler crawler  = new FileListCrawler();

        crawler.set(parser);

        Logger.getLogger(LoadBibliographyAndClass.class.getName()).log(Level.INFO,"Analyzing "+((File)(properties.get("PaperFileLocation").get())).getAbsolutePath());

        crawler.crawl(((File)(properties.get("PaperFileLocation").get())).getAbsolutePath());

        graph = ((Graph)crawler.getParser()[0].get());

        fireChange(Scheduler.SET_ALGORITHM_PROGRESS,1);

        parserProperties = PropertiesFactory.newInstance().create();
        ParserFactory.newInstance().getParameter("ParserClass").clear();
        ParserFactory.newInstance().getParameter("ParserClass").add("ParseClassLabel");
        parserProperties.merge(ParserFactory.newInstance().getParameter());
        parserProperties.merge(properties);

        parser[0] = ParserFactory.newInstance().create(parserProperties);

        parser[0].set(graph);

        Logger.getLogger(LoadBibliographyAndClass.class.getName()).log(Level.INFO,"Analyzing "+((File)(properties.get("ClassFileLocation").get())).getAbsolutePath());

        crawler.set(parser);

        crawler.crawl(((File)(properties.get("ClassFileLocation").get())).getAbsolutePath());

        graph = ((Graph)crawler.getParser()[0].get());

        fireChange(Scheduler.SET_ALGORITHM_PROGRESS,2);

    }



    @Override

    public void set(Graph g) {

        graph = g;

    }



    @Override

    public Graph get() {

        return graph;

    }



    @Override

    public void cancel() {

        throw new UnsupportedOperationException("Not supported yet.");

    }



    @Override

    public List<IODescriptor> getInputType() {

        return new LinkedList<IODescriptor>();

    }

    

    @Override

    public List<IODescriptor> getOutputType() {

        return output;

    }

    

    @Override

    public Properties getParameter() {

        return properties;

    }

    

    @Override

    public Parameter getParameter(String param) {
        return properties.get(param);
    }

    

    /**

     * Parameters for this object:

     * <ul>

     * <li/><b>name</b>: Name of this algorithm.  Default is 'Bibliography and Class XML Reader'

     * <li/><b>fileLocation</b>: file name of the base bibliography XML file. Default

     *  is '/tmp/bibliography.xml'

     * <li/><b>actorMode</b>: actor type of authors. Default is 'Author'

     * <li/><b>paperMode</b>: actor type of papers. Default is 'Paper'

     * <li/><b>wroteRelation</b>: link relation between authors and papers. Default 

     * is 'Authored'

     * <li/><b>referencesRelation</b>: relation for citations between papers. Deafult

     * is 'Authored'

     * <li/><b>classLabelFile</b>: file name of the ground truth XML file. Default

     * is '/tmp/idLabel.xml'

     * <li/><b>bidirectionalReferences</b>: boolean determining the direction of the 

     * links between papers. Default is 'false'

     * </ul><br/>

     * @param map map of all parameters for this object

     */

    public void init(Properties map) {
        if(properties.check(map)){
            properties.merge(map);
            IODescriptor desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR,
                    (String)properties.get("Name").get(),
                    (String)properties.get("ActorMode").get(),
                    null,
                    null,
                    "");
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR_PROPERTY,
                    (String)properties.get("Name").get(),
                    (String)properties.get("AuthorMode").get(),
                    null,
                    "name",
                    "");
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR,
                    (String)properties.get("Name").get(),
                    (String)properties.get("PaperMode").get(),
                    null,
                    null,
                    "");
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR_PROPERTY,
                    (String)properties.get("Name").get(),
                    (String)properties.get("PaperMode").get(),
                    null,
                    "title","");
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR_PROPERTY,
                    (String)properties.get("Name").get(),
                    (String)properties.get("PaperMode").get(),
                    null,
                    "abstract","");
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR_PROPERTY,
                    (String)properties.get("Name").get(),
                    (String)properties.get("PaperMode").get(),
                    null,
                    "year","");
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR_PROPERTY,
                    (String)properties.get("Name").get(),
                    (String)properties.get("PaperMode").get(),
                    null,
                    "file","");
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR_PROPERTY,
                    (String)properties.get("Name").get(),
                    (String)properties.get("PaperMode").get(),
                    null,
                    "type","");
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR_PROPERTY,
                    (String)properties.get("Name").get(),
                    (String)properties.get("PaperMode").get(),
                    null,
                    "journal","");
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR_PROPERTY,
                    (String)properties.get("Name").get(),
                    (String)properties.get("PaperMode").get(),
                    null,
                    "cluster","");
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.LINK,
                    (String)properties.get("Name").get(),
                    (String)properties.get("AuthoredRelation").get(),
                    null,
                    null,"");
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.LINK,
                    (String)properties.get("Name").get(),
                    (String)properties.get("ReferencesRelation").get(),
                    null,
                    null,"");
            output.add(desc);
        }else{
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING,"Initialization skipped as the parameters provided were not within restrictions.");
        }
    }

    public LoadBibliographyAndClass prototype() {
        return new LoadBibliographyAndClass();
    }



}

