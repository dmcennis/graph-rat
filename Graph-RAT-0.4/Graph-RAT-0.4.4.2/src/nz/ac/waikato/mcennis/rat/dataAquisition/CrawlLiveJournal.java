/*
 * CrawlLiveJournal.java
 *
 * Created on 3 October 2007, 13:53
 *
 * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt)
 */

package nz.ac.waikato.mcennis.rat.dataAquisition;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import nz.ac.waikato.mcennis.rat.crawler.FileListCrawler;
import nz.ac.waikato.mcennis.rat.crawler.WebCrawler;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import nz.ac.waikato.mcennis.rat.graph.actor.Actor;
import org.dynamicfactory.descriptors.DescriptorFactory;
import org.dynamicfactory.descriptors.InputDescriptor;
import org.dynamicfactory.descriptors.OutputDescriptor;
import org.dynamicfactory.descriptors.OutputDescriptorInternal;
import org.dynamicfactory.descriptors.SettableParameter;
import nz.ac.waikato.mcennis.rat.graph.link.Link;
import nz.ac.waikato.mcennis.rat.parser.Parser;
import nz.ac.waikato.mcennis.rat.parser.ParserFactory;
import nz.ac.waikato.mcennis.rat.parser.ToFileParser;
import nz.ac.waikato.mcennis.rat.graph.model.ModelShell;
import nz.ac.waikato.mcennis.rat.scheduler.Scheduler;

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
    
    private ParameterInternal[] parameter = new ParameterInternal[9];
    private OutputDescriptor[] output = new OutputDescriptor[12];
    
    /**
     * Graph object that parsed LiveJournal pages are loaded into
     */
    private Graph graph=null;
    
    /** Creates a new instance of CrawlLiveJournal */
    public CrawlLiveJournal() {
        init(null);
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
        crawler.setProxy(false);
        
        // read the lists
        java.util.Properties props = new java.util.Properties();
        props.setProperty("ParserType","FOAF");
        props.setProperty("GraphType","User");
        
        Parser[] parser = new Parser[]{ParserFactory.newInstance().create(props)};
        FileListCrawler fileCrawler  = new FileListCrawler();
        fileCrawler.set(parser);
        
        java.io.File directory = new java.io.File((String)parameter[4].getValue());
        java.io.File[] files = directory.listFiles();
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
        Actor[] user = ((Graph)fileCrawler.getParser()[0].get()).getActor("User");
        fireChange(Scheduler.SET_ALGORITHM_COUNT,user.length);
        for(int i=0;i<user.length;++i){
            Logger.getLogger(CrawlLastFM.class.getName()).log(Level.FINE, Integer.toString(i));
            Link[] l = ((Graph)fileCrawler.getParser()[0].get()).getLinkBySource("Knows",user[i]);
            if(l != null){
                try {
                    crawler.block((String)parameter[2].getValue()+java.net.URLEncoder.encode(user[i].getID(),"UTF-8")+(String)parameter[3].getValue());
                } catch (UnsupportedEncodingException ex) {
                    ex.printStackTrace();
                }
            }else{
                try {
                    crawler.crawl((String)parameter[2].getValue()+java.net.URLEncoder.encode(user[i].getID(),"UTF-8")+(String)parameter[3].getValue());
                } catch (MalformedURLException ex) {
                    Logger.getLogger(CrawlLiveJournal.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(CrawlLiveJournal.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            fireChange(Scheduler.SET_ALGORITHM_PROGRESS,i);
        }
//        for(int i=0;i<user.length;++i){
//            UserLink[] l = ((Graph)fileCrawler.getParser()[0].get()).getUserLink("Basic",user[i].getID());
//            for(int j=0;j<user.length;++j){
//                System.out.println(" "+j);
//                crawler.crawl("http://"+java.net.URLEncoder.encode(l[j].getDestination().getID(),"UTF-8")+".livejournal.com/data/foaf");
//            }
//        }
        
        
        java.util.Properties props1 = new java.util.Properties();
        props1.setProperty("ParserType","File");
        props1.setProperty("Directory",(String)parameter[4].getValue());
        java.util.Properties props2 = new java.util.Properties();
        props2.setProperty("ParserType","FOAF");
        props2.setProperty("GraphType","Null");
        
        parser = new Parser[]{ParserFactory.newInstance().create(props1),ParserFactory.newInstance().create(props2)};
        parser[1].set(graph);
        crawler.set(parser);
        ((ToFileParser)crawler.getParser()[0]).setCount(files.length);
        crawler.setMaxCount((Integer)parameter[6].getValue());
        
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
    public InputDescriptor[] getInputType() {
        return new InputDescriptor[]{};
    }

    @Override
    public OutputDescriptor[] getOutputType() {
        return output;
    }

    @Override
    public Parameter[] getParameter() {
        return parameter;
    }

    @Override
    public Parameter getParameter(String param) {
        for(int i=0;i<parameter.length;++i){
            if(parameter[i].getName().equals(param)){
                return parameter[i];
            }
        }
        return null;
    }

     @Override
   public SettableParameter[] getSettableParameter() {
        return null;
    }

    @Override
    public SettableParameter getSettableParameter(String param) {
        return null;
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
        
        // Construct Properties
            Properties props = new Properties();
            props.setProperty("Type","java.lang.String");
            props.setProperty("Name","name");
            props.setProperty("Class","Basic");
            props.setProperty("Structural","true");
            parameter[0] = DescriptorFactory.newInstance().createParameter(props);
            if((map != null)&&(map.getProperty("name") != null)){
                parameter[0].setValue(map.getProperty("name"));
            }else{
                parameter[0].setValue("Crawl FOAF");
            }
        
            props.setProperty("Type","java.lang.Boolean");
            props.setProperty("Name","pageCrawl");
            props.setProperty("Class","Basic");
            props.setProperty("Structural","true");
            parameter[1] = DescriptorFactory.newInstance().createParameter(props);
            if((map != null)&&(map.getProperty("pageCrawl") != null)){
                parameter[1].setValue(new Boolean(Boolean.parseBoolean(map.getProperty("pageCrawl"))));
            }else{
                parameter[1].setValue("false");
            }
        
            props.setProperty("Type","java.lang.String");
            props.setProperty("Name","urlPrefix");
            props.setProperty("Class","Basic");
            props.setProperty("Structural","true");
            parameter[2] = DescriptorFactory.newInstance().createParameter(props);
            if((map != null)&&(map.getProperty("urlPrefix") != null)){
                parameter[2].setValue(map.getProperty("urlPrefix"));
            }else{
                parameter[2].setValue("http://");
            }
        
            props.setProperty("Type","java.lang.String");
            props.setProperty("Name","urlSuffix");
            props.setProperty("Class","Basic");
            props.setProperty("Structural","true");
            parameter[3] = DescriptorFactory.newInstance().createParameter(props);
            if((map != null)&&(map.getProperty("urlSuffix") != null)){
                parameter[3].setValue(map.getProperty("urlSuffix"));
            }else{
                parameter[3].setValue(".livejournal.com/data/foaf");
            }
        
            props.setProperty("Type","java.lang.String");
            props.setProperty("Name","foafDirectory");
            props.setProperty("Class","Basic");
            props.setProperty("Structural","true");
            parameter[4] = DescriptorFactory.newInstance().createParameter(props);
            if((map != null)&&(map.getProperty("foafDirectory") != null)){
                parameter[4].setValue(map.getProperty("foafDirectory"));
            }else{
                parameter[4].setValue("");
            }
        
            props.setProperty("Type","java.lang.String");
            props.setProperty("Name","pageDirectory");
            props.setProperty("Class","Basic");
            props.setProperty("Structural","true");
            parameter[5] = DescriptorFactory.newInstance().createParameter(props);
            if((map != null)&&(map.getProperty("pageDirectory") != null)){
                parameter[5].setValue(map.getProperty("pageDirectory"));
            }else{
                parameter[5].setValue("");
            }
        
            props.setProperty("Type","java.lang.String");
            props.setProperty("Name","stopCount");
            props.setProperty("Class","Basic");
            props.setProperty("Structural","true");
            parameter[6] = DescriptorFactory.newInstance().createParameter(props);
            if((map != null)&&(map.getProperty("stopCount") != null)){
                parameter[6].setValue(map.getProperty("stopCount"));
            }else{
                parameter[6].setValue("10000");
            }
        
            props.setProperty("Type","java.lang.String");
            props.setProperty("Name","proxyUser");
            props.setProperty("Class","Basic");
            props.setProperty("Structural","true");
            parameter[7] = DescriptorFactory.newInstance().createParameter(props);
            if((map != null)&&(map.getProperty("proxyUser") != null)){
                parameter[7].setValue(map.getProperty("proxyUser"));
            }else{
                parameter[7].setValue("");
            }
        
            props.setProperty("Type","java.lang.String");
            props.setProperty("Name","proxyPassword");
            props.setProperty("Class","Basic");
            props.setProperty("Structural","true");
            parameter[8] = DescriptorFactory.newInstance().createParameter(props);
            if((map != null)&&(map.getProperty("proxyPassword") != null)){
                parameter[8].setValue(map.getProperty("proxyPassword"));
            }else{
                parameter[8].setValue("");
            }
        
        props.setProperty("Type", "Actor");
        props.setProperty("Relation", "User");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.remove("Property");
        output[0] = DescriptorFactory.newInstance().createOutputDescriptor(props);
       
        props.setProperty("Type", "Link");
        props.setProperty("Relation", "Knows");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.remove("Property");
        output[1] = DescriptorFactory.newInstance().createOutputDescriptor(props);
       
        props.setProperty("Type", "ActorProperty");
        props.setProperty("Relation", "User");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property","foaf:title");
        output[2] = DescriptorFactory.newInstance().createOutputDescriptor(props);
       
        props.setProperty("Type", "ActorProperty");
        props.setProperty("Relation", "User");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property","foaf:phone");
        output[3] = DescriptorFactory.newInstance().createOutputDescriptor(props);
       
        props.setProperty("Type", "ActorProperty");
        props.setProperty("Relation", "User");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property","foaf:gender");
        output[4] = DescriptorFactory.newInstance().createOutputDescriptor(props);
       
        props.setProperty("Type", "ActorProperty");
        props.setProperty("Relation", "User");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property","ya:country");
        output[5] = DescriptorFactory.newInstance().createOutputDescriptor(props);
       
        props.setProperty("Type", "ActorProperty");
        props.setProperty("Relation", "User");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property","ya:city");
        output[6] = DescriptorFactory.newInstance().createOutputDescriptor(props);
       
        props.setProperty("Type", "ActorProperty");
        props.setProperty("Relation", "User");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property","foaf:dateOfBirth");
        output[7] = DescriptorFactory.newInstance().createOutputDescriptor(props);
       
        props.setProperty("Type", "ActorProperty");
        props.setProperty("Relation", "User");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property","foaf:aimChatID");
        output[8] = DescriptorFactory.newInstance().createOutputDescriptor(props);
       
        props.setProperty("Type", "ActorProperty");
        props.setProperty("Relation", "User");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property","foaf:msnChatID");
        output[9] = DescriptorFactory.newInstance().createOutputDescriptor(props);
       
        props.setProperty("Type", "ActorProperty");
        props.setProperty("Relation", "User");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property","ya:bio");
        output[10] = DescriptorFactory.newInstance().createOutputDescriptor(props);
       
        props.setProperty("Type", "ActorProperty");
        props.setProperty("Relation", "User");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property","interest");
        output[11] = DescriptorFactory.newInstance().createOutputDescriptor(props);
    }

    public void set(Graph g) {
        graph = g;
    }

    public Graph get() {
        return graph;
    }
    
}
