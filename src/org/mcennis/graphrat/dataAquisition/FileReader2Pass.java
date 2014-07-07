/*

 * MakeRecommendation2Pass.java

 *

 * Created on 4 October 2007, 08:10

 *

 * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt)

 */

package org.mcennis.graphrat.dataAquisition;



import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import java.util.logging.Logger;

import org.mcennis.graphrat.crawler.FileListCrawler;

import org.mcennis.graphrat.graph.Graph;

import org.mcennis.graphrat.graph.GraphFactory;
import org.mcennis.graphrat.graph.UserIDList;

import org.mcennis.graphrat.descriptors.IODescriptorFactory;
import org.dynamicfactory.descriptors.*;


import org.mcennis.graphrat.descriptors.IODescriptor;

import org.mcennis.graphrat.descriptors.IODescriptor.Type;

import org.dynamicfactory.model.ModelShell;

import org.mcennis.graphrat.parser.Parser;

import org.mcennis.graphrat.parser.ParserFactory;

import org.mcennis.graphrat.scheduler.Scheduler;



/**

 * Reads a directory of FOAF descriptions as defined by the FOAF parser (the 

 * LiveJournal specific dialect) in two passes.  The first pass determines

 * which users are present in the directory.  The second pass only creates an actor

 * if the users are present, not just references.<br/>

 * <br/>

 * See the output descriptors for the description of the graph created.

 * 

 * @author Daniel McEnnis

 */

public class FileReader2Pass extends ModelShell implements DataAquisition {



    Graph graph = null;

    PropertiesInternal properties = PropertiesFactory.newInstance().create();

    LinkedList<IODescriptor> input = new LinkedList<IODescriptor>();
    LinkedList<IODescriptor> output = new LinkedList<IODescriptor>();



    /** Creates a new instance of MakeRecommendation2Pass */

    public FileReader2Pass() {
        ParameterInternal name = ParameterFactory.newInstance().create("AlgorithmClass",String.class);
        SyntaxObject syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("File Reader 2 Pass");
        properties.add(name);

        name = ParameterFactory.newInstance().create("Name",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,Integer.MAX_VALUE,null,String.class);
        name.setRestrictions(syntax);
        name.add("File Reader 2 Pass");
        properties.add(name);

        name = ParameterFactory.newInstance().create("Category",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("File Crawler");
        properties.add(name);

        name = ParameterFactory.newInstance().create("Directory",File.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,File.class);
        name.setRestrictions(syntax);
        properties.add(name);

        name = ParameterFactory.newInstance().create("Anonymizing",Boolean.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,Boolean.class);
        name.setRestrictions(syntax);
        name.add(true);
        properties.add(name);
    }



    @Override

    public void start() {

        properties.set("ParserClass", "FOAF");

        Graph userIDList = GraphFactory.newInstance().create("UserIDList");
        Parser[] parser = new Parser[]{ParserFactory.newInstance().create(properties)};
        parser[0].set(userIDList);
        FileListCrawler crawler = new FileListCrawler();
        crawler.set(parser);

        parser = null;



//        java.io.File directory = new java.io.File("/research/data/backup-21-06-07");

        File directory = (File)properties.get("Directory").get();

        File[] files = directory.listFiles();

        Logger.getLogger(FileReader2Pass.class.getName()).log(Level.INFO, "First Pass...");

        fireChange(Scheduler.SET_GRAPH_COUNT, 2);

        fireChange(Scheduler.SET_ALGORITHM_COUNT, files.length);

        for (int i = 0; i < files.length; ++i) {

            try {

                if (i % 100 == 0) {

                    Logger.getLogger(FileReader2Pass.class.getName()).log(Level.FINE, i + " of " + files.length);

                    ((Graph) crawler.getParser()[0].get()).commit();

                    fireChange(Scheduler.SET_ALGORITHM_PROGRESS, i);

                }

                crawler.crawl(files[i].getAbsolutePath());

            } catch (Exception e) {

                e.printStackTrace();

            }

        }

        ((Graph) crawler.getParser()[0].get()).commit();



        UserIDList uil = (UserIDList) (crawler.getParser()[0].get());

        parser = new Parser[]{ParserFactory.newInstance().create(properties)};

        parser[0].set(graph);

        ((Graph) parser[0].get()).add(uil);

        crawler.set(parser);

        parser = null;



        Logger.getLogger(FileReader2Pass.class.getName()).log(Level.INFO, "Second pass...");

        fireChange(Scheduler.SET_GRAPH_PROGRESS, 1);

        fireChange(Scheduler.SET_ALGORITHM_COUNT, files.length);

        for (int i = 0; i < files.length; ++i) {

            try {

                if (i % 100 == 0) {

                    Logger.getLogger(FileReader2Pass.class.getName()).log(Level.FINE, i + " of " + files.length);

                    ((Graph) crawler.getParser()[0].get()).commit();

                }

                crawler.crawl(files[i].getAbsolutePath());

                fireChange(Scheduler.SET_ALGORITHM_PROGRESS, i);

            } catch (Exception e) {

                e.printStackTrace();

            }

        }

        ((Graph) crawler.getParser()[0].get()).commit();

        uil = null;

        graph = ((Graph) crawler.getParser()[0].get());

        graph.add(uil);

        if ((Boolean)properties.get("Anonymizing").get()) {

            Logger.getLogger(FileReader2Pass.class.getName()).log(Level.INFO, "Anonymizing");

            graph.anonymize();

        }

        crawler = null;

//        graph.add((UserIDList)null);

    }



    @Override

    public void set(Graph g) {

        graph = g;

    }



    @Override

    public Graph get() {

        return graph;

    }



    /**

     * FIXME: write cancel operation

     */

    public void cancel() {

    }



    @Override

    public List<IODescriptor> getInputType() {

        return input;

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

     * Initializes this object using the parameters. Also creates the input and 

     * output descriptors

     * 

     * <br><b>parameter[0]</b>

     * <br><i>Key-name:</i>'name'

     * <br><i>Type:</i>java.lang.String

     * <br><i>Structural:</i> true

     * <br><i>Description:</i> Name of this component.

     * <br>

     * <br><b>parameter[1]</b>

     * <br><i>Key-name:</i>'foafDirectory'

     * <br><i>Type:</i>java.lang.String

     * <br><i>Structural:</i> true

     * <br><i>Description:</i> Directory where the FOAF xml files are located

     * <br>

     * <br><b>parameter[2]</b>

     * <br><i>Key-name:</i>'pageDirectory'

     * <br><i>Type:</i>java.lang.String

     * <br><i>Structural:</i> true

     * <br><i>Description:</i> Directory where the web-page files are located 

     * <br>FIXME: page parsing not currently implemented

     * <br>

     * <br><b>parameter[3]</b>

     * <br><i>Key-name:</i>'anonymizing'

     * <br><i>Type:</i>java.lang.Boolean

     * <br><i>Structural:</i> true

     * <br><i>Description:</i> Should users be anonymized.

     * <br>

     * <br><b>parameter[4]</b>

     * <br><i>Key-name:</i>'actorClass'

     * <br><i>Type:</i>java.lang.String

     * <br><i>Structural:</i> true

     * <br><i>Description:</i> Class of the actor that should be used to produce

     * users described by the FOAF files.

     * <br>

     * <br><b>parameter[5]</b>

     * <br><i>Key-name:</i>'actorType'

     * <br><i>Type:</i>java.lang.String

     * <br><i>Structural:</i> true

     * <br><i>Description:</i> The name of the 'mode' (type name) used to describe

     * users described by the FOAF files.

     * <br>

     * <br><b>parameter[6]</b>

     * <br><i>Key-name:</i>'linkClass'

     * <br><i>Type:</i>java.lang.String

     * <br><i>Structural:</i> true

     * <br><i>Description:</i> class of links created by FOAF 'Knows' links.

     * <br>

     * <br><b>parameter[7]</b>

     * <br><i>Key-name:</i>'linkType'

     * <br><i>Type:</i>java.lang.String

     * <br><i>Structural:</i> true

     * <br><i>Description:</i> Relation (arc type, edge type) created by FOAF 'Knows' links

     * <br>

     * <br><b>parameter[8]</b>

     * <br><i>Key-name:</i>'propertyClass'

     * <br><i>Type:</i>java.lang.String

     * <br><i>Structural:</i> true

     * <br><i>Description:</i> Class for the property objects created

     * <br>

     * 

     * @param map the key-value pairs to initialize this component

     */

    public void init(Properties map) {
        if(properties.check(map)){
            properties.merge(map);

            IODescriptor desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR,
                    (String)properties.get("Name").get(),
                    "User",
                    null,
                    null,"");
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.LINK,
                    (String)properties.get("Name").get(),
                    "Knows",
                    null,
                    null,"");
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR_PROPERTY,
                    (String)properties.get("Name").get(),
                    "User",
                    null,
                    "foaf:title","");
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR_PROPERTY,
                    (String)properties.get("Name").get(),
                    "User",
                    null,
                    "foaf:phone","");
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR_PROPERTY,
                    (String)properties.get("Name").get(),
                    "User",
                    null,
                    "foaf:gender","");
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR_PROPERTY,
                    (String)properties.get("Name").get(),
                    "User",
                    null,
                    "ya:country","");
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR_PROPERTY,
                    (String)properties.get("Name").get(),
                    "User",
                    null,
                    "ya:city","");
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR_PROPERTY,
                    (String)properties.get("Name").get(),
                    "User",
                    null,
                    "foaf:dateOfBirth","");
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR_PROPERTY,
                    (String)properties.get("Name").get(),
                    "User",
                    null,
                    "foaf:aimChatID","");
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR_PROPERTY,
                    (String)properties.get("Name").get(),
                    "User",
                    null,
                    "foaf:msnChatID","");
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR_PROPERTY,
                    (String)properties.get("Name").get(),
                    "User",
                    null,
                    "ya:bio","");
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR_PROPERTY,
                    (String)properties.get("Name").get(),
                    "User",
                    null,
                    "interest","");
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR_PROPERTY,
                    (String)properties.get("Name").get(),
                    "User",
                    null,
                    "foaf:phone","");
            output.add(desc);

        }

    }

    public FileReader2Pass prototype(){
        return new FileReader2Pass();
    }
}

