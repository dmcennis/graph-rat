/**
 * copyright Daniel McEnnis - published under Aferro GPL (see license.txt)
 */
package nz.ac.waikato.mcennis.rat.crawler;

import java.io.IOException;
import java.net.MalformedURLException;

import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import nz.ac.waikato.mcennis.rat.parser.Parser;

/**
 * Multithreaded web crawler designed to pull web pages.  Each thread utilizes
 * CrawlerBase as the base classs.  The threads themselves are a interior class.
 * 
 * FIXME: Does not handle sites parsed using a subset of given parsers
 *
 * @author Daniel McEnnis
 *
 */
public class WebCrawler implements Crawler {
    
    /**
     * Array of runnable objects for multithreaded parsing.  Initially set to
     * null.
     */
    public Spider[] spiders;
    
    /**
     * Array of threads.  The number of threads equals the number of spiders.  
     * Also intially set to null.
     */
    java.lang.Thread[] threads;
    
    /**
     * Index of the next spider to recieve a new site to parse
     */
    int next_spider;
    
    /**
     * A hashtable that holds all web sites that have already been scheduled to 
     * be parsed in order to prevent duplicate accesses of the same web page.
     */
    java.util.HashSet<SiteReference> webSites = new java.util.HashSet<SiteReference>();
    
    int count = 0;
    
    int maxCount = Integer.MAX_VALUE;
    
    boolean siteCheck = true;
    
    /**
     * Length of the delay between finishing one site and requesting the next in 
     * milliseconds
     */
    long threadDelay = 500;
    
    /**
     * Base constructor that initializes threads to null.
     */
    public WebCrawler(){
        spiders = null;
        next_spider = -1;
    }
    
    /**
     * Creates threads to be utilized by this web crawler.  Must be called before
     * parsing or a NullPointerException is thrown
     *
     * @param numThreads number of threads to be created
     *
     * @throws java.lang.NullPointerException if no threads have been created yet
     */
    public void createThreads(int numThreads){
        if(spiders != null){
            for(int i=0;i<spiders.length;++i){
                if(spiders[i] != null){
                    spiders[i].stop = true;
                }
            }
        }
        spiders = new Spider[numThreads];
        threads = new java.lang.Thread[numThreads];
        for(int i=0;i<spiders.length;++i){
            spiders[i] = new Spider(this);
            threads[i] = new java.lang.Thread(spiders[i]);
        }
        next_spider=0;
    }
    
    /**
     * Sets the threads running.  A null operation if no threads exist yet.
     *
     */
    public void startThreads(){
        if(spiders != null){
            for(int i=0;i<spiders.length;++i){
                if(spiders[i]!=null){
                    threads[i].start();
                }
            }
        }
    }
    
    /**
     * Tells each thread to stop execution after parsing the current document.
     */
    public void stopThreads(){
        if(spiders != null){
            for(int i=0;i<spiders.length;++i){
                if(spiders[i]!=null){
                    spiders[i].stop = true;
                }
            }
        }
    }
    
    /**
     * Helper class that is used for threads. Crawls sites in order.  Each crawler
     * recieves an equal number f sites to crawl, not based on curent load.
     */
    public class Spider extends CrawlerBase implements Runnable {
        
        /**
         * Syncronized queue of web sites to be crawled
         */
        java.util.concurrent.LinkedBlockingQueue<SiteReference> URLList = new java.util.concurrent.LinkedBlockingQueue<SiteReference>();
        
        /**
         * reference to enclosing object.
         */
        WebCrawler parent;
        
        /**
         * message boolean for signaling that the crawling should cease.
         */
        boolean stop = false;
        
        /**
         * message boolean indicating whether or not the thread still have sites to parse
         */
        boolean running=false;
        
        /**
         * redundant set of hash sites to verify no redundant accesses to a web
         * site are made.
         */
        java.util.HashSet<SiteReference> sites = new java.util.HashSet<SiteReference>();
        
        boolean siteCheck = true;
        
        /**
         * Base constructor that stores a reference to the parent in each thread
         * 
         * @param p parent which allows communication between thread and the 
         * parent object.
         */
        public Spider(WebCrawler p) {
            super();
            parent = p;
        }
        
        /**
         * add a new site to be crawled by this thread
         *
         * @param entry Site to be crawled
         */
        protected void add(SiteReference site) {
            URLList.add(site);
        }
        
        /**
         * is this thread idle and waiting for more sites to crawl
         */
        protected boolean isEmpty(){
            return URLList.isEmpty();
        }
        
        /**
         * starts the thread executing, parsing web sites in its queue until it
         * recieves a stop request.
         */
        public void run() {
            int count = 0;
            running=true;
            Logger.getLogger(WebCrawler.class.getName()).log(Level.INFO,"Starting WebCrawl");
            while (!stop) {
                while (!URLList.isEmpty() && !stop) {
                    try {
                        SiteReference site = URLList.take();
                        if(!sites.contains(site)){
                            Logger.getLogger(WebCrawler.class.getName()).log(Level.INFO,site.siteName+":"+count);
                            if(siteCheck){
                                sites.add(site);
                            }
                            count++;
                            Thread.sleep(threadDelay);
                            try {
                                crawl(site.getSiteName(),site.getParserNames());
                            } catch (MalformedURLException ex) {
                                ex.printStackTrace();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        stop = true;
                        break;
                    }
                }
                if (!stop) {
                    try {
                        Logger.getLogger(WebCrawler.class.getName()).log(Level.INFO,"Sleeping");
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        stop = true;
                        e.printStackTrace();
                    }
                }
//                if(count >=10000){
//                    stop = true;
//                }
            }
        }
        
        public boolean isRunning(){
            return running;
        }
        
//        @Override
//        protected void doParse(byte[] raw_data,String[] parsers) throws IOException {
//            for (int i = 0; i < parser.length; ++i) {
//                java.io.ByteArrayInputStream data = new java.io.ByteArrayInputStream(
//                        raw_data);
//                try {
//                    parser[i].parse(data, parent);
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                }
//                data.close();
//            }
//        }
        @Override
    protected void doParse(byte[] raw_data, String[] parsers) throws IOException, Exception {
        LinkedList<Parser> parserList = new LinkedList<Parser>();
        for(int i=0;i<parsers.length;++i){
            for(int j=0;j<parser.length;++j){
                if(parser[j].getName().contentEquals(parsers[i])){
                    parserList.add(parser[j]);
                    break;
                }
            }
        }
        for (Parser p : parserList) {
            java.io.ByteArrayInputStream data = new java.io.ByteArrayInputStream(
                    raw_data);
            if(spider){
                p.parse(data,parent);
            }else{
                p.parse(data);
            }
            data.close();
        }
    }
    }
    
    /**
     * Identical to crawl except all parsers are used
     * @param site site to be crawled
     * @throws java.net.MalformedURLException id site URL is invalid
     * @throws java.io.IOException error occurs during retrieval
     */
    public synchronized void crawl(String site) throws MalformedURLException,IOException{
        String[] parsers = new String[spiders[0].parser.length];
        for(int i=0;i<spiders[0].parser.length;++i){
            parsers[i] = spiders[0].parser[i].getName();
        }
        crawl(site,parsers);
    }
    
        /**
         * If this site has not been crawled before, adds a new site to the 
         * set of all sites parsed and adds it to the queue of the next spider,
         * incrementing the spider index to the next spider.
         *
         * @param site web site to be retrieved and parsed
         *
         * @see nz.ac.waikato.mcennis.arm.crawler.Crawler#crawl(java.lang.String)
         */
    public synchronized void crawl(String site, String[] parsers) {
        if((count < maxCount)&&(!webSites.contains(site))&&(spiders[next_spider]!=null)){
            SiteReference value = new SiteReference();
            value.setSiteName(site);
            value.setParserNames(parsers);
            if(siteCheck){
                webSites.add(value);
            }
            spiders[next_spider].add(value);
            if(count % 100 == 0){
                Logger.getLogger(WebCrawler.class.getName()).log(Level.INFO,"NEW: "+ count);
            }
            count++;
            next_spider = (next_spider + 1)%spiders.length;
        }
    }
    
    
        /**
         * Set the parser for all spiders in this object.  This is a null 
         * operation if no spider threads have been created yet.
         *
         * @param parser set of parsers to be duplicated accross all spiders
         *
         * @see nz.ac.waikato.mcennis.arm.crawler.Crawler#set(nz.ac.waikato.mcennis.arm.parser.Parser[])
         */
    public void set(Parser[] parser) {
        if(spiders != null){
            for(int i=0;i<spiders.length;++i){
                if(spiders[i] != null){
                    spiders[i].set(parser);
                }
            }
        }
    }
    
    /**
     * returns if this object has finished parsing all sites added to be crawled
     *
     * @return if all sites added have been parsed
     */
    public boolean isDone(){
        boolean done = true;
        if(spiders != null){
            for(int i=0;i<spiders.length;++i){
                if((spiders[i]!=null)&&(!spiders[i].isEmpty())){
                    done=false;
                }
            }
        }
        return done;
    }
    
    /**
     * Returns the parsers of the first spider.  Parsers should use static 
     * storage in the parser so this will access all data parsed.  This is a null
     * operation if threads have not been constructed yet
     *
     * @return array of parsers stored on the first thread.  
     */
    public Parser[] getParser(){
        if((spiders != null)&&(spiders.length>0)){
            return spiders[0].getParser();
        }else{
            return null;
        }
    }
    
    /**
     * Add a site to the list of parsed sites without parsing it.  Useful for 
     * restarting a site crawl without reparsing all the data it contained.
     *
     * @param site web site to be added to the set of sites already parsed
     */
    public void block(String site){
        String[] parsers = new String[spiders[0].parser.length];
        for(int i=0;i<parsers.length;++i){
            parsers[i] = spiders[0].parser[i].getName();
        }
        block(site,parsers);
    }
    
    public void block(String site,String[] parsers){
        SiteReference value = new SiteReference();
        value.setSiteName(site);
        value.setParserNames(parsers);
        webSites.add(value);
        for(int i=0;i<spiders.length;++i){
            spiders[i].sites.add(value);
        }
    }
    
    /**
     * indicates whether or not this crawler's threads should use a proxy to connect
     * to the internet. This is a null operation if threads have not been 
     *constructed yet.
     *
     * @param proxy should a proxy be used.
     */
    public void setProxy(boolean proxy){
        if(spiders != null){
        for(int i=0;i<spiders.length;++i){
            spiders[i].setProxy(proxy);
        }
        }
    }

    @Override
    public void setCaching(boolean b) {
        // FIXME: Does nothing
    }

    @Override
    public boolean isCaching() {
        //FIXME: always true
        return true;
    }

    public class SiteReference {
            String siteName = "";
            String[] parserNames = new String[]{};
            
            public String[] getParserNames() {
                return parserNames;
            }

            public void setParserNames(String[] parserNames) {
                if(parserNames != null){
                    this.parserNames = parserNames;
                }
            }

            public String getSiteName() {
                return siteName;
            }

            public void setSiteName(String siteName) {
                if(siteName != null){
                    this.siteName = siteName;
                }
            }           
        }
    @Override
    public void setSpidering(boolean s){
        if(spiders != null){
            for( int i=0;i<spiders.length;++i){
                spiders[i].spider=s;
            }
        }
    }
    
    @Override
    public boolean isSpidering(){
        if(spiders == null){
            return false;
        }else{
            return spiders[0].spider;
        }
    }
    
    public void setSiteCheck(boolean check){
        siteCheck = check;
        for(int i=0;i<spiders.length;++i){
            spiders[i].siteCheck=check;
        }
    }
    
    public boolean getSiteCheck(){
        return siteCheck;
    }

    public long getThreadDelay() {
        return threadDelay;
    }

    public void setThreadDelay(long threadDelay) {
        this.threadDelay = threadDelay;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    public String getProxyHost() {
        if((spiders != null)&&(spiders.length > 0)){
            return spiders[0].proxyHost;
        }else{
            return null;
        }
    }

    public void setProxyHost(String proxyHost) {
        if(spiders != null){
            for(int i=0;i<spiders.length;++i){
                spiders[i].setProxyHost(proxyHost);
            }
        }
    }

    public String getProxyPort() {
        if((spiders != null)&&(spiders.length > 0)){
            return spiders[0].proxyPort;
        }else{
            return null;
        }
    }

    public void setProxyPort(String proxyPort) {
        if(spiders != null){
            for(int i=0;i<spiders.length;++i){
                spiders[i].setProxyPort(proxyPort);
            }
        }
    }

    public String getProxyType() {
        if((spiders != null)&&(spiders.length > 0)){
            return spiders[0].proxyType;
        }else{
            return null;
        }
    }

    public void setProxyType(String proxyType) {
        if(spiders != null){
            for(int i=0;i<spiders.length;++i){
                spiders[i].setProxyType(proxyType);
            }
        }
    }
    

}
