/*
 * Created 19/05/2008-10:51:51
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.dataAquisition;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import nz.ac.waikato.mcennis.rat.crawler.Authenticator;
import nz.ac.waikato.mcennis.rat.crawler.FileListCrawler;
import nz.ac.waikato.mcennis.rat.crawler.WebCrawler;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import org.dynamicfactory.descriptors.DescriptorFactory;
import org.dynamicfactory.descriptors.InputDescriptor;
import org.dynamicfactory.descriptors.OutputDescriptor;
import org.dynamicfactory.descriptors.OutputDescriptor;
import org.dynamicfactory.descriptors.SettableParameter;
import nz.ac.waikato.mcennis.rat.graph.model.ModelShell;
import nz.ac.waikato.mcennis.rat.parser.Parser;
import nz.ac.waikato.mcennis.rat.parser.ToFileParser;
import nz.ac.waikato.mcennis.rat.parser.XMLParser;
import nz.ac.waikato.mcennis.rat.parser.xmlHandler.LastFMArtistTag;
import nz.ac.waikato.mcennis.rat.parser.xmlHandler.LastFMFriends;
import nz.ac.waikato.mcennis.rat.parser.xmlHandler.LastFMProfile;
import nz.ac.waikato.mcennis.rat.parser.xmlHandler.LastFMSimilarUsers;
import nz.ac.waikato.mcennis.rat.parser.xmlHandler.LastFMUserArtistTags;
import nz.ac.waikato.mcennis.rat.parser.xmlHandler.LastFMUserExpansion;
import nz.ac.waikato.mcennis.rat.parser.xmlHandler.LastFMUserTags;
import nz.ac.waikato.mcennis.rat.parser.xmlHandler.LastFMUserTopArtists;

/**
 * Class for parsing the LastFM web services with a multi-threaded parser.  Files are stored to the directory 
 * structure as follows:<br/> 
 * <br/>
 * &lt;directory&gt;/&lt;UserName&gt;/Profile.xml<br/>
 * &lt;directory&gt;/&lt;UserName&gt;/Tags.xml<br/>
 * &lt;directory&gt;/&lt;UserName&gt;/Friends.xml<br/>
 * &lt;directory&gt;/&lt;UserName&gt;/Neighbours.xml<br/>
 * &lt;directory&gt;/&lt;UserName&gt;/topArtists.xml<br/>
 * &lt;directory&gt;/ArtistDirectory/&lt;ArtistName&gt;.xml<br/>
 * &lt;directory&gt;/&lt;UserName&gt;/&lt;ArtistName&gt;.xml<br/>
 * <br/>
 * Profile.xml contains demographic information about a user.<br/>
 * <br/>
 * Tags.xml contains the set of the top 50 tags this user has used<br/>
 * <br/>
 * Friends.xml contains all users this user has declared as a friend<br/>
 * <br/>
 * Neighbors.xml contains the top 50 users suffeciently similar to this user<br/>
 * <br/>
 * topArtists.xml contains the top 50 artists this user has listened to by playcount
 * <br/>
 * &lt;ArtistName&gt;.xml are the tags used to describe this artist.  In a user 
 * directory, these are the tags applied to this artist by that user only. <br/>
 * <br/>
 * 
 * @author Daniel McEnnis
 */
public class CrawlLastFM extends ModelShell implements DataAquisition {

    Graph graph = null;
    ParameterInternal[] parameter = new ParameterInternal[13];
    OutputDescriptor[] output = new OutputDescriptor[22];

    public CrawlLastFM() {
        init(null);
    }

    public void start() {
        WebCrawler crawler = new WebCrawler();
        crawler.createThreads(((Integer)parameter[9].getValue()).intValue());
        crawler.setThreadDelay(((Long)parameter[8].getValue()).longValue());
        crawler.setProxy((Boolean) parameter[4].getValue());
        crawler.setSpidering(true);
        crawler.setSiteCheck(false);
        crawler.setMaxCount(((Integer)parameter[3].getValue()).intValue());
        if ((Boolean) parameter[4].getValue()) {
            Authenticator.setUser((String) parameter[5].getValue());
            crawler.setProxyHost((String)parameter[10].getValue());
            crawler.setProxyPort((String)parameter[11].getValue());
            crawler.setProxyType((String)parameter[12].getValue());
            if (parameter[6].getValue() != null) {
                Authenticator.setPassword((String) parameter[6].getValue());
            } else {
                Logger.getLogger(CrawlLastFM.class.getName()).log(Level.INFO, "Enter proxy password for " + (String) parameter[5].getValue());
                Authenticator.getPassword();
            }
        }

        Properties props = new Properties();
        LastFMUserExpansion expansion = new LastFMUserExpansion();
        Parser[] parsers = expansion.setUpParsers((File) parameter[1].getValue(), graph);

        crawler.set(parsers);
//        parsers = crawler.getParser();
        for(int i=0;i<crawler.spiders.length;++i){
            parsers = crawler.spiders[i].getParser();
        ((LastFMProfile) ((XMLParser) parsers[0]).getHandler()).setParser((ToFileParser) parsers[7]);
        ((LastFMUserTopArtists) ((XMLParser) parsers[1]).getHandler()).setParser((ToFileParser) parsers[7]);
        ((LastFMUserArtistTags) ((XMLParser) parsers[2]).getHandler()).setParser((ToFileParser) parsers[7]);
        ((LastFMUserTags) ((XMLParser) parsers[3]).getHandler()).setParser((ToFileParser) parsers[7]);
        ((LastFMArtistTag) ((XMLParser) parsers[4]).getHandler()).setParser((ToFileParser) parsers[7]);
        ((LastFMFriends) ((XMLParser) parsers[5]).getHandler()).setParser((ToFileParser) parsers[7]);
        ((LastFMSimilarUsers) ((XMLParser) parsers[6]).getHandler()).setParser((ToFileParser) parsers[7]);
        }
        if ((Boolean) parameter[7].getValue()) {
            File[] userDirectory = ((File) (parameter[1].getValue())).listFiles();
            if (userDirectory != null) {
                // block all artists and users already parsed
                for (int i = 0; i < userDirectory.length; ++i) {
                    if (userDirectory[i].getName().contentEquals("ArtistDirectory")) {
                        parseArtists(userDirectory[i]);
                    } else {
                        Logger.getLogger(CrawlLastFM.class.getName()).log(Level.FINE, "Blocking user '" + userDirectory[i].getName() + "'");
                        LastFMUserExpansion.blockUsername(userDirectory[i].getName());
                    }
                }
                FileListCrawler fileCrawler = new FileListCrawler();
                fileCrawler.set(new Parser[]{parsers[1],parsers[5],parsers[6]});
                fileCrawler.setCrawler(crawler);
                fileCrawler.setSpidering(true);
                // schedule for crawling all users that have base info but not artist info
                for (int i = 0; i < userDirectory.length; ++i) {
                    if ((!userDirectory[i].getName().contentEquals("ArtistDirectory")) && (!isArtistParsed(userDirectory[i]))) {
                            fileCrawler.crawl(userDirectory[i].getAbsolutePath() + File.separator+"topArtists.xml", new String[]{parsers[1].getName()});
                    }
                }
                crawler.startThreads();
                // schedule for crawling additional users
                for (int i = 0; i < userDirectory.length; ++i) {
                    if (!userDirectory[i].getName().contentEquals("ArtistDirectory")) {
                            fileCrawler.crawl(userDirectory[i].getAbsolutePath() + File.separator+ "friends.xml", new String[]{parsers[5].getName()});
                            fileCrawler.crawl(userDirectory[i].getAbsolutePath() + File.separator+ "neighbours.xml", new String[]{parsers[6].getName()});
                    }
                }
            }
        } else {
            LineNumberReader reader = null;
            try {
                reader = new LineNumberReader(new java.io.FileReader((File) parameter[2].getValue()));
                String line = reader.readLine();
//            while ((line != null) && (!line.contentEquals(""))) {
                expansion.expandUser(crawler, line);
//                line = reader.readLine();
//            }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(CrawlLastFM.class.getName()).log(Level.SEVERE, null, ex);
                ex.printStackTrace();
            } catch (IOException ex) {
                Logger.getLogger(CrawlLastFM.class.getName()).log(Level.SEVERE, null, ex);
                ex.printStackTrace();
            } finally {
                try {
                    reader.close();
                } catch (IOException ex) {
                    Logger.getLogger(CrawlLastFM.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            crawler.startThreads();
        }
        try {
            while (System.in.read() != ';') {
                ;
            }
        } catch (IOException ex) {
            Logger.getLogger(CrawlLastFM.class.getName()).log(Level.SEVERE, null, ex);
        }
        crawler.stopThreads();
    }

    protected boolean isArtistParsed(File userDirectory) {
        File[] files = userDirectory.listFiles();
        if(files != null){
        for (int i = 0; i < files.length; ++i) {
            if (files[i].getName().contentEquals("profile.xml")) {

            } else if (files[i].getName().contentEquals("topArtists.xml")) {

            } else if (files[i].getName().contentEquals("tags.xml")) {

            } else if (files[i].getName().contentEquals("friends.xml")) {

            } else if (files[i].getName().contentEquals("neighbours.xml")) {

            } else {
                return true;
            }
        }
        return false;
        }else{
            return false;
        }
    }

    protected void parseArtists(File artistDirectory) {
        File[] artistNames = artistDirectory.listFiles();
        if (artistNames != null) {
            for (int i = 0; i < artistNames.length; ++i) {
                String name = artistNames[i].getName();
                name = name.substring(0, name.lastIndexOf("."));
                Logger.getLogger(CrawlLastFM.class.getName()).log(Level.FINE, "Blocking artist '" + name + "'");
                LastFMUserTopArtists.blockArtist(name);
            }
        }
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
    public OutputDescriptor[] getOutputType() {
        return output;
    }

    @Override
    public InputDescriptor[] getInputType() {
        return new InputDescriptor[]{};
    }

    @Override
    public Parameter[] getParameter() {
        return parameter;
    }

    @Override
    public Parameter getParameter(String param) {
        for (int i = 0; i < parameter.length; ++i) {
            if (parameter[i].getName().contentEquals(param)) {
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
     * @param map properties to load - null is permitted.
     */
    public void init(Properties map) {
        Properties props = new Properties();
        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "name");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[0] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("name") != null)) {
            parameter[0].setValue(map.getProperty("name"));
        } else {
            parameter[0].setValue("Crawl LastFM");
        }

        props.setProperty("Type", "java.io.File");
        props.setProperty("Name", "destinationDirectory");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[1] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("destinationDirectory") != null)) {
            parameter[1].setValue(new File(map.getProperty("destinationDirectory")));
        } else {
            parameter[1].setValue(new File("/tmp/LastFM/"));
        }

        props.setProperty("Type", "java.io.File");
        props.setProperty("Name", "usernameFile");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[2] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("usernameFile") != null)) {
            parameter[2].setValue(new File(map.getProperty("usernameFile")));
        } else {
            parameter[2].setValue(new File("/tmp/usernames.txt"));
        }

        props.setProperty("Type", "java.lang.Integer");
        props.setProperty("Name", "stopCount");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[3] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("stopCount") != null)) {
            parameter[3].setValue(new Integer(Integer.parseInt(map.getProperty("stopCount"))));
        } else {
            parameter[3].setValue(new Integer(2000000));
        }

        props.setProperty("Type", "java.lang.Boolean");
        props.setProperty("Name", "proxy");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[4] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("proxy") != null)) {
            parameter[4].setValue(new Boolean(Boolean.parseBoolean(map.getProperty("proxy"))));
        } else {
            parameter[4].setValue(new Boolean(true));
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "proxyUser");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[5] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("proxyUser") != null)) {
            parameter[5].setValue(map.getProperty("proxyUser"));
        } else {
            parameter[5].setValue("dm75");
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "proxyPassword");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[6] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("proxyPassword") != null)) {
            parameter[6].setValue(map.getProperty("proxyPassword"));
        } else {
            parameter[6].setValue(null);
        }

        props.setProperty("Type", "java.lang.Boolean");
        props.setProperty("Name", "restart");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[7] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("restart") != null)) {
            parameter[7].setValue(new Boolean(Boolean.parseBoolean(map.getProperty("restart"))));
        } else {
            parameter[7].setValue(new Boolean(true));
        }

        props.setProperty("Type", "java.lang.Long");
        props.setProperty("Name", "threadDelay");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[8] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("threadDelay") != null)) {
            parameter[8].setValue(new Long(Long.parseLong(map.getProperty("threadDelay"))));
        } else {
            parameter[8].setValue(new Long(500));
        }

        props.setProperty("Type", "java.lang.Integer");
        props.setProperty("Name", "threadCount");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[9] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("threadCount") != null)) {
            parameter[9].setValue(new Integer(Integer.parseInt(map.getProperty("threadCount"))));
        } else {
            parameter[9].setValue(new Integer(1));
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "proxyHost");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[10] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("proxyHost") != null)) {
            parameter[10].setValue(map.getProperty("proxyHost"));
        } else {
            parameter[10].setValue("proxy.scms.waikato.ac.nz");
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "proxyPort");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[11] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("proxyPort") != null)) {
            parameter[11].setValue(map.getProperty("proxyPort"));
        } else {
            parameter[11].setValue("80");
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "proxyType");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[12] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("proxyType") != null)) {
            parameter[12].setValue(map.getProperty("proxyType"));
        } else {
            parameter[12].setValue("4");
        }

        props.setProperty("Type", "Actor");
        props.setProperty("Relation", "user");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.remove("Property");
        output[0] = DescriptorFactory.newInstance().createOutputDescriptor(props);
       
        props.setProperty("Type", "ActorProperty");
        props.setProperty("Relation", "user");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property","LastFM Homepage");
        output[1] = DescriptorFactory.newInstance().createOutputDescriptor(props);
       
        props.setProperty("Type", "ActorProperty");
        props.setProperty("Relation", "user");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property","Age");
        output[2] = DescriptorFactory.newInstance().createOutputDescriptor(props);
       
        props.setProperty("Type", "ActorProperty");
        props.setProperty("Relation", "user");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property","Avatar");
        output[3] = DescriptorFactory.newInstance().createOutputDescriptor(props);
       
        props.setProperty("Type", "ActorProperty");
        props.setProperty("Relation", "user");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property","Country");
        output[4] = DescriptorFactory.newInstance().createOutputDescriptor(props);
       
        props.setProperty("Type", "ActorProperty");
        props.setProperty("Relation", "user");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property","Gender");
        output[5] = DescriptorFactory.newInstance().createOutputDescriptor(props);
       
        props.setProperty("Type", "ActorProperty");
        props.setProperty("Relation", "user");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property","Icon");
        output[6] = DescriptorFactory.newInstance().createOutputDescriptor(props);
       
        props.setProperty("Type", "ActorProperty");
        props.setProperty("Relation", "user");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property","MBox");
        output[7] = DescriptorFactory.newInstance().createOutputDescriptor(props);
       
        props.setProperty("Type", "ActorProperty");
        props.setProperty("Relation", "user");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property","PlayCount");
        output[8] = DescriptorFactory.newInstance().createOutputDescriptor(props);
       
        props.setProperty("Type", "ActorProperty");
        props.setProperty("Relation", "user");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property","name");
        output[9] = DescriptorFactory.newInstance().createOutputDescriptor(props);
       
        props.setProperty("Type", "ActorProperty");
        props.setProperty("Relation", "user");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property","Cluster ID");
        output[10] = DescriptorFactory.newInstance().createOutputDescriptor(props);
       
        props.setProperty("Type", "ActorProperty");
        props.setProperty("Relation", "user");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property","User ID");
        output[11] = DescriptorFactory.newInstance().createOutputDescriptor(props);
       
        props.setProperty("Type", "Actor");
        props.setProperty("Relation", "artist");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.remove("Property");
        output[12] = DescriptorFactory.newInstance().createOutputDescriptor(props);
       
        props.setProperty("Type", "Actor");
        props.setProperty("Relation", "tag");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.remove("Property");
        output[13] = DescriptorFactory.newInstance().createOutputDescriptor(props);

        props.setProperty("Type", "ActorProperty");
        props.setProperty("Relation", "tag");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property","URL");
        output[14] = DescriptorFactory.newInstance().createOutputDescriptor(props);
       
        props.setProperty("Type", "Link");
        props.setProperty("Relation", "ArtistTag");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.remove("Property");
        output[15] = DescriptorFactory.newInstance().createOutputDescriptor(props);
       
        props.setProperty("Type", "Link");
        props.setProperty("Relation", "UserTag");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.remove("Property");
        output[16] = DescriptorFactory.newInstance().createOutputDescriptor(props);
       
        props.setProperty("Type", "Link");
        props.setProperty("Relation", "UserArtistTag");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.remove("Property");
        output[17] = DescriptorFactory.newInstance().createOutputDescriptor(props);
       
        props.setProperty("Type", "LinkProperty");
        props.setProperty("Relation", "UserArtistTag");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property","Tags");
        output[18] = DescriptorFactory.newInstance().createOutputDescriptor(props);
       
        props.setProperty("Type", "Link");
        props.setProperty("Relation", "ListensTo");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.remove("Property");
        output[19] = DescriptorFactory.newInstance().createOutputDescriptor(props);
       
        props.setProperty("Type", "Link");
        props.setProperty("Relation", "Similar");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.remove("Property");
        output[20] = DescriptorFactory.newInstance().createOutputDescriptor(props);
       
        props.setProperty("Type", "Link");
        props.setProperty("Relation", "Friend");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.remove("Property");
        output[21] = DescriptorFactory.newInstance().createOutputDescriptor(props);
       
      }

}
