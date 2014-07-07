/*

 * Created 19/05/2008-10:51:51

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

package org.mcennis.graphrat.dataAquisition;



import java.io.File;

import java.io.FileNotFoundException;

import java.io.IOException;

import java.io.LineNumberReader;


import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import java.util.logging.Logger;

import org.mcennis.graphrat.crawler.Authenticator;

import org.mcennis.graphrat.crawler.FileListCrawler;

import org.mcennis.graphrat.crawler.WebCrawler;

import org.mcennis.graphrat.graph.Graph;


import org.mcennis.graphrat.descriptors.IODescriptorFactory;



import org.mcennis.graphrat.descriptors.IODescriptor;

import org.mcennis.graphrat.descriptors.IODescriptor.Type;
import org.dynamicfactory.descriptors.*;

import org.dynamicfactory.model.ModelShell;

import org.mcennis.graphrat.parser.Parser;

import org.mcennis.graphrat.parser.ToFileParser;

import org.mcennis.graphrat.parser.XMLParser;

import org.mcennis.graphrat.parser.xmlHandler.LastFMArtistTag;

import org.mcennis.graphrat.parser.xmlHandler.LastFMFriends;

import org.mcennis.graphrat.parser.xmlHandler.LastFMProfile;

import org.mcennis.graphrat.parser.xmlHandler.LastFMSimilarUsers;

import org.mcennis.graphrat.parser.xmlHandler.LastFMUserArtistTags;

import org.mcennis.graphrat.parser.xmlHandler.LastFMUserExpansion;

import org.mcennis.graphrat.parser.xmlHandler.LastFMUserTags;

import org.mcennis.graphrat.parser.xmlHandler.LastFMUserTopArtists;



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

    PropertiesInternal properties = PropertiesFactory.newInstance().create();

    LinkedList<IODescriptor> input = new LinkedList<IODescriptor>();
    LinkedList<IODescriptor> output = new LinkedList<IODescriptor>();



    public CrawlLastFM() {
        ParameterInternal name = ParameterFactory.newInstance().create("AlgorithmClass",String.class);
        SyntaxObject syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("Crawl LastFM");
        properties.add(name);

        name = ParameterFactory.newInstance().create("Name",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,Integer.MAX_VALUE,null,String.class);
        name.setRestrictions(syntax);
        name.add("Crawl LastFM");
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

        name = ParameterFactory.newInstance().create("DownloadDirectory",File.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,File.class);
        name.setRestrictions(syntax);
        properties.add(name);

        name = ParameterFactory.newInstance().create("UsernameFile",File.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,File.class);
        name.setRestrictions(syntax);
        properties.add(name);

        name = ParameterFactory.newInstance().create("StopCount",Integer.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,Integer.class);
        name.setRestrictions(syntax);
        name.add(10000);
        properties.add(name);

        name = ParameterFactory.newInstance().create("ThreadDelay",Integer.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,Integer.class);
        name.setRestrictions(syntax);
        name.add(0);
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



    public void start() {

        WebCrawler crawler = new WebCrawler();

        crawler.createThreads(1);

        crawler.setThreadDelay(((Integer)properties.get("ThreadDelay").get()));

        crawler.setProxy((Boolean) properties.get("UseProxy").get());

        crawler.setSpidering(true);

        crawler.setSiteCheck(false);

        crawler.setMaxCount(((Integer)properties.get("StopCount").get()));

        if ((Boolean)properties.get("UseProxy").get()) {

            Authenticator.setUser((String) properties.get("ProxyUser").get());

            Authenticator.setPassword((String) properties.get("ProxyPassword").get());

            crawler.setProxyHost((String)properties.get("ProxyLocation").get());

            crawler.setProxyPort(((Integer)properties.get("ProxyPort").get()).toString());

            crawler.setProxyType(((Integer)properties.get("ProxyType").get()).toString());

        }



        LastFMUserExpansion expansion = new LastFMUserExpansion();

        Parser[] parsers = expansion.setUpParsers((File) properties.get("DownloadDirectory").get(), graph);



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

        if (((File)properties.get("DownloadDirectory").get()).listFiles().length>0) {

            File[] userDirectory = ((File) (properties.get("DownloadDirectory").get())).listFiles();

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

                reader = new LineNumberReader(new java.io.FileReader((File) properties.get("UsernameFile").get()));

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

    public List<IODescriptor> getOutputType() {

        return output;

    }



    @Override

    public List<IODescriptor> getInputType() {

        return input;

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

     * @param map properties to load - null is permitted.

     */

    public void init(Properties map) {
        if(properties.check(map)){
            properties.merge(map);
            
            IODescriptor desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR, 
                    (String)properties.get("Name").get(), 
                    "user", 
                    null, 
                    null,"");
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR_PROPERTY, 
                    (String)properties.get("Name").get(), 
                    "user", 
                    null, 
                    "LastFM Homepage","");
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR_PROPERTY, 
                    (String)properties.get("Name").get(), 
                    "user", 
                    null, 
                    "Age","");
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR_PROPERTY,
                    (String)properties.get("Name").get(),
                    "user",
                    null,
                    "Avatar","");
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR_PROPERTY,
                    (String)properties.get("Name").get(),
                    "user",
                    null,
                    "Country","");
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR_PROPERTY,
                    (String)properties.get("Name").get(),
                    "user",
                    null,
                    "Gender","");
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR_PROPERTY,
                    (String)properties.get("Name").get(),
                    "user",
                    null,
                    "Icon","");
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR_PROPERTY,
                    (String)properties.get("Name").get(),
                    "user",
                    null,
                    "MBox","");
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR_PROPERTY,
                    (String)properties.get("Name").get(),
                    "user",
                    null,
                    "PlayCount","");
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR_PROPERTY,
                    (String)properties.get("Name").get(),
                    "user",
                    null,
                    "name","");
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR_PROPERTY,
                    (String)properties.get("Name").get(),
                    "user",
                    null,
                    "Cluster ID","");
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR_PROPERTY,
                    (String)properties.get("Name").get(),
                    "user",
                    null,
                    "User ID","");
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR,
                    (String)properties.get("Name").get(),
                    "artist",
                    null,
                    null,"");
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR,
                    (String)properties.get("Name").get(),
                    "tag",
                    null,
                    null,"");
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR_PROPERTY,
                    (String)properties.get("Name").get(),
                    "tag",
                    null,
                    "URL","");
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.LINK,
                    (String)properties.get("Name").get(),
                    "ArtistTag",
                    null,
                    null,"");
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.LINK,
                    (String)properties.get("Name").get(),
                    "UserTag",
                    null,
                    null,"");
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.LINK,
                    (String)properties.get("Name").get(),
                    "UserArtistTag",
                    null,
                    null,"");
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.LINK_PROPERTY,
                    (String)properties.get("Name").get(),
                    "UserArtistTag",
                    null,
                    "Tags","");
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.LINK,
                    (String)properties.get("Name").get(),
                    "ListensTo",
                    null,
                    null,"");
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.LINK,
                    (String)properties.get("Name").get(),
                    "Similar",
                    null,
                    null,"");
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.LINK,
                    (String)properties.get("Name").get(),
                    "Friend",
                    null,
                    null,"");
            output.add(desc);
        }
    }

    public CrawlLastFM prototype(){
        return new CrawlLastFM();
    }

}

