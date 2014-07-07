/*

 * CrawlLiveJournal.java

 *

 * Created on 3 October 2007, 13:53

 *

 * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt)

 */



package org.mcennis.graphrat.dataAquisition;



import java.io.File;
import java.io.IOException;

import java.io.UnsupportedEncodingException;

import java.net.MalformedURLException;


import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import java.util.logging.Logger;


import org.mcennis.graphrat.crawler.Authenticator;
import org.mcennis.graphrat.crawler.FileListCrawler;

import org.mcennis.graphrat.crawler.WebCrawler;

import org.mcennis.graphrat.graph.Graph;

import org.mcennis.graphrat.graph.GraphFactory;
import org.mcennis.graphrat.actor.Actor;

import org.mcennis.graphrat.descriptors.IODescriptorFactory;


import org.mcennis.graphrat.descriptors.IODescriptor;


import org.mcennis.graphrat.descriptors.IODescriptor.Type;
import org.dynamicfactory.descriptors.*;

import org.mcennis.graphrat.link.Link;

import org.mcennis.graphrat.parser.Parser;

import org.mcennis.graphrat.parser.ParserFactory;

import org.mcennis.graphrat.parser.ToFileParser;

import org.dynamicfactory.model.ModelShell;

import org.mcennis.graphrat.scheduler.Scheduler;



/**

 * This class enables parsing and spidering of the LiveJournal site.  

 * 

 * FIXME: There are still some basic components that are needed to be 

 * generalized - the class will need to be modified before it can be used in a

 * new situation.

 *

 * @author Daniel McEnnis

 * 

 */

public class CrawlLiveJournal extends ModelShell implements DataAquisition{

    PropertiesInternal properties = PropertiesFactory.newInstance().create();

    LinkedList<IODescriptor> input = new LinkedList<IODescriptor>();
    LinkedList<IODescriptor> output = new LinkedList<IODescriptor>();
        

    /**

     * Graph object that parsed LiveJournal pages are loaded into

     */

    private Graph graph=null;

    

    /** Creates a new instance of CrawlLiveJournal */

    public CrawlLiveJournal() {
        ParameterInternal name = ParameterFactory.newInstance().create("AlgorithmClass",String.class);
        SyntaxObject syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("Crawl LiveJournal");
        properties.add(name);

        name = ParameterFactory.newInstance().create("Name",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,Integer.MAX_VALUE,null,String.class);
        name.setRestrictions(syntax);
        name.add("Crawl LiveJournal");
        properties.add(name);

        name = ParameterFactory.newInstance().create("Category",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("Web Crawler");
        properties.add(name);

        name = ParameterFactory.newInstance().create("CrawlerClass",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("WebCrawler");
        properties.add(name);

        name = ParameterFactory.newInstance().create("URLPrefix",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("http://");
        properties.add(name);

        name = ParameterFactory.newInstance().create("URLSuffix",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        name.add(".livejournal.com/data/foaf");
        properties.add(name);

        name = ParameterFactory.newInstance().create("DownloadDirectory",File.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,File.class);
        name.setRestrictions(syntax);
        properties.add(name);

        name = ParameterFactory.newInstance().create("StopCount",Integer.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,Integer.class);
        name.setRestrictions(syntax);
        name.add(10000);
        properties.add(name);

        name = ParameterFactory.newInstance().create("UseProxy",Boolean.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,Boolean.class);
        name.setRestrictions(syntax);
        name.add(false);
        properties.add(name);

        name = ParameterFactory.newInstance().create("ProxyUser",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(0,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("dm75");
        properties.add(name);

        name = ParameterFactory.newInstance().create("ProxyPassword",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(0,1,null,String.class);
        name.setRestrictions(syntax);
        properties.add(name);

        name = ParameterFactory.newInstance().create("ProxyLocation",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(0,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("proxy.cs.waikato.ac.nz");
        properties.add(name);

        name = ParameterFactory.newInstance().create("ProxyType",Integer.class);
        syntax = SyntaxCheckerFactory.newInstance().create(0,1,null,Integer.class);
        name.setRestrictions(syntax);
        name.add(4);
        properties.add(name);

        name = ParameterFactory.newInstance().create("ProxyPort",Integer.class);
        syntax = SyntaxCheckerFactory.newInstance().create(0,1,null,Integer.class);
        name.setRestrictions(syntax);
        name.add(80);
        properties.add(name);
}



    /**

     * Creates the crawler and proxies and starts spidering FOAF descriptions.

     * 

     * FIXME: Not using initialization parameters

     * FIXME: Not using a cusomizable Graph type and class

     */

    public void start() {

        WebCrawler crawler = new WebCrawler();

        crawler.createThreads(1);

        if((Boolean)properties.get("Proxy").get()){
            crawler.setProxy(true);
            crawler.setProxyHost((String)properties.get("ProxyLocation").get());
            crawler.setProxyPort(((Integer)properties.get("ProxyPort").get()).toString());
            crawler.setProxyType(((Integer)properties.get("ProxyType").get()).toString());
            Authenticator auth = new Authenticator();
            auth.setUser((String)properties.get("ProxyUser").get());
            auth.setPassword((String)properties.get("ProxyPassword").get());
            java.net.Authenticator.setDefault(auth);
        }else{
            crawler.setProxy(false);
        }
        // read the lists

        Graph userGraph = GraphFactory.newInstance().create("User");

        properties.set("ParserClass", "FOAF");
        Parser[] parser = new Parser[]{ParserFactory.newInstance().create(properties)};
        parser[0].set(userGraph);
        FileListCrawler fileCrawler  = new FileListCrawler();
        fileCrawler.set(parser);
        

        File directory = (File)properties.get("DestinatonDirectory").get();

        File[] files = directory.listFiles();

            fireChange(Scheduler.SET_GRAPH_COUNT, 3);

        fireChange(Scheduler.SET_ALGORITHM_COUNT,files.length);

        

        for(int i=0;i<files.length;++i){

            try {

                System.out.println(files[i].getAbsolutePath());

                fileCrawler.crawl(files[i].getAbsolutePath());

                fireChange(Scheduler.SET_ALGORITHM_PROGRESS,i);

            } catch(Exception e){

                e.printStackTrace();

            }

        }

        

            fireChange(Scheduler.SET_GRAPH_PROGRESS,1);

        // set the crawler

        int userCount = ((Graph)fileCrawler.getParser()[0].get()).getActorCount("User");
        Iterator<Actor> userIt = ((Graph)fileCrawler.getParser()[0].get()).getActorIterator("User");

        fireChange(Scheduler.SET_ALGORITHM_COUNT,userCount);
        int i=0;
        while(userIt.hasNext()){
            Actor user = userIt.next();
            Logger.getLogger(CrawlLastFM.class.getName()).log(Level.FINE, Integer.toString(i));

            Iterator<Link> l = ((Graph)fileCrawler.getParser()[0].get()).getLinkBySourceIterator("Knows",user);

            if(l.hasNext()){

                try {

                    crawler.block((String)properties.get("URLPrefix").get()+java.net.URLEncoder.encode(user.getID(),"UTF-8")+(String)properties.get("URLSuffix").get());

                } catch (UnsupportedEncodingException ex) {

                    ex.printStackTrace();

                }

            }else{

                try {

                    crawler.crawl((String)properties.get("URLPrefix").get()+java.net.URLEncoder.encode(user.getID(),"UTF-8")+(String)properties.get("URLSuffix").get());

                } catch (MalformedURLException ex) {

                    Logger.getLogger(CrawlLiveJournal.class.getName()).log(Level.SEVERE, null, ex);

                } catch (IOException ex) {

                    Logger.getLogger(CrawlLiveJournal.class.getName()).log(Level.SEVERE, null, ex);

                }

            }

            fireChange(Scheduler.SET_ALGORITHM_PROGRESS,++i);

        }

//        for(int i=0;i<user.length;++i){

//            UserLink[] l = ((Graph)fileCrawler.getParser()[0].get()).getUserLink("Basic",user[i].getID());

//            for(int j=0;j<user.length;++j){

//                System.out.println(" "+j);

//                crawler.crawl("http://"+java.net.URLEncoder.encode(l[j].getDestination().getID(),"UTF-8")+".livejournal.com/data/foaf");

//            }

//        }

        

        

        Properties props1 = properties.duplicate();
        props1.set("ParserClass","File");
        props1.set("Directory",((File)properties.get("DestinationDirectory").get()).getAbsolutePath());
        Properties props2 = properties.duplicate();
        props2.set("ParserClass","FOAF");
        props2.set("GraphClass","Null");

        

        parser = new Parser[]{ParserFactory.newInstance().create(props1),ParserFactory.newInstance().create(props2)};

        parser[1].set(graph);

        crawler.set(parser);

        ((ToFileParser)crawler.getParser()[0]).setCount(files.length);

        crawler.setMaxCount((Integer)properties.get("StopCount").get());

        

        fireChange(Scheduler.SET_GRAPH_PROGRESS,2);

        fireChange(Scheduler.SET_ALGORITHM_COUNT,Short.MAX_VALUE);

        crawler.startThreads();

        java.io.BufferedReader input = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));

        try {

            input.readLine();

        } catch (IOException ex) {

            ex.printStackTrace();

        }

        crawler.stopThreads();

        fireChange(Scheduler.SET_ALGORITHM_PROGRESS,Short.MAX_VALUE-1);

    }

    



    /**

     * Cancel the run at the end of the next user.

     * 

     * FIXME: currently unimplemented

     */

    public void cancel() {

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

     * 

     * <br/><b>parameter[0]</b>

     * <br/><i>Key-name:</i>'name'

     * <br/><i>Type:</i>java.lang.String

     * <br/><i>Structural:</i> true

     * <br/><i>Description:</i> Name of this component.

     * <br/>

     * <br/><b>parameter[1]</b>

     * <br/><i>Key-name:</i>'pageCrawl'

     * <br/><i>Type:</i>java.lang.Boolean

     * <br/><i>Structural:</i> true

     * <br/><i>Description:</i> Should blogs and other web pages be parsed.

     * <br/>

     * <br/><b>parameter[2]</b>

     * <br/><i>Key-name:</i>'urlPrefix'

     * <br/><i>Type:</i>java.lang.String

     * <br/><i>Structural:</i> true

     * <br/><i>Description:</i> Prefix used with usernames for parsing FOAF pages.

     * <br/>

     * <br/><b>parameter[3]</b>

     * <br/><i>Key-name:</i>'urlSuffix'

     * <br/><i>Type:</i>java.lang.String

     * <br/><i>Structural:</i> true

     * <br/><i>Description:</i> Suffix used with usernames for parsing FOAF pages.

     * <br/>

     * <br/><b>parameter[4]</b>

     * <br/><i>Key-name:</i>'foafDirectory'

     * <br/><i>Type:</i>java.lang.String

     * <br/><i>Structural:</i> true

     * <br/><i>Description:</i> Directory where FOAF pages should be stored.

     * <br/>

     * <br/><b>parameter[5]</b>

     * <br/><i>Key-name:</i>'pageDirectory'

     * <br/><i>Type:</i>java.lang.String

     * <br/><i>Structural:</i> true

     * <br/><i>Description:</i> Directory where web pages should be stored.

     * <br/>

     * <br/><b>parameter[6]</b>

     * <br/><i>Key-name:</i>'stopCount'

     * <br/><i>Type:</i>java.lang.Integer

     * <br/><i>Structural:</i> true

     * <br/><i>Description:</i> number of pages to crawl before spidering should cease.

     * <br/>

     * <br/><b>parameter[7]</b>

     * <br/><i>Key-name:</i>'proxyUser'

     * <br/><i>Type:</i>java.lang.String

     * <br/><i>Structural:</i> true

     * <br/><i>Description:</i> username to use with a proxy.

     * <br/>

     * <br/><b>parameter[8]</b>

     * <br/><i>Key-name:</i>'proxyPassword'

     * <br/><i>Type:</i>java.lang.String

     * <br/><i>Structural:</i> true

     * <br/><i>Description:</i> password to use with a proxy.

     * <br/>

     * 

     * @param map key-value pairs used to initialize this component

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



    public void set(Graph g) {

        graph = g;

    }



    public Graph get() {

        return graph;

    }

    public CrawlLiveJournal prototype(){
        return new CrawlLiveJournal();
    }

}

