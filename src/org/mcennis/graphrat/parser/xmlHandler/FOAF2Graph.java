/**

 * copyright Daniel McEnnis - published under Aferro GPL (see license.txt)

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
package org.mcennis.graphrat.parser.xmlHandler;



import java.io.IOException;

import java.net.MalformedURLException;

import java.net.URL;

import java.util.logging.Level;

import java.util.logging.Logger;
import org.dynamicfactory.descriptors.*;
import org.dynamicfactory.property.Property;
import org.dynamicfactory.property.PropertyFactory;
import org.dynamicfactory.property.InvalidObjectTypeException;

import org.mcennis.graphrat.crawler.Crawler;

import org.mcennis.graphrat.parser.ParsedObject;

import org.xml.sax.Attributes;

import org.xml.sax.SAXException;



import org.mcennis.graphrat.graph.Graph;

import org.mcennis.graphrat.graph.GraphFactory;

import org.mcennis.graphrat.actor.Actor;

import org.mcennis.graphrat.actor.ActorFactory;

import org.mcennis.graphrat.link.Link;

import org.mcennis.graphrat.link.LinkFactory;


import org.mcennis.graphrat.crawler.WebCrawler;



/**

 * SAX XML parser for reading FOAF files of the LiveJournal dialect.  See 

 * the DataAquisition modules CrawlLastFM, FileReader, or FileReader2 for a description

 * of the modes, relations, and property IDs produced by this parser.  

 * 

 * TODO: include a DTD of the LiveJournal FOAF.

 * 

 * @author Daniel McEnnis

 *

 */

public class FOAF2Graph extends Handler {

    

    static Graph foaf;

    Crawler crawler;

    

    Actor parent;

    Actor current;

//	String name = null;

    String title = null;

//	String givenname = null;

//	String family_name = null;

//	String nick = null;

    String homepage = null;

    String phone = null;

    String workplacehomepage = null;

    String workinfohomepage = null;

    String schoolhomepage = null;

    String topic_interest = null;

    Property interest = null;

    String gender = null;

    String unknown_arg1 = null;

    String unknown_arg2 = null;

    String country = null;

    String city = null;

    String dateOfBirth = null;

    String page = null;

    String aim = null;

    String msn = null;

    String weblog = null;

    String bio = null;

    String document = null;

   

    boolean child = false;

    

    static final int START = 0;

    static final int RDF = 1;

    static final int PERSON = 2;

//	static final int NAME = 3;

    static final int TITLE = 4;

//	static final int GIVENNAME = 5;

//	static final int FAMILY_NAME = 6;

    static final int NICK = 8;

    static final int HOMEPAGE = 9;

    static final int PHONE = 10;

    static final int WORKPLACEHOMEPAGE = 11;

    static final int WORKINFOHOMEPAGE = 12;

    static final int SCHOOLHOMEPAGE = 13;

    static final int KNOWS = 14;

//    static final int MBOX = 15;

    static final int TOPIC_INTEREST = 16;

    static final int INTEREST = 17;

//    static final int MBOX2 = 18;

    static final int GENDER = 19;

    static final int COUNTRY = 20;

    static final int CITY = 21;

    static final int BIRTH = 22;

    static final int PAGE = 23;

    static final int DOCUMENT = 24;

    static final int AIM = 25;

    static final int MSN = 26;

    static final int WEBLOG = 27;

    static final int BIO = 28;

    

    boolean unknown = false;

    

    int state = START;

    int oldstate = START;

    

    /**

     * Create a new Parser using the default graph

     */

    public FOAF2Graph(){

        super();

        properties.get("ParserClass").add("FOAF");

        properties.get("Name").add("FOAF");

        

        ParameterInternal linkType = ParameterFactory.newInstance().create("LinkRelation",String.class);

        SyntaxObject restriction = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);

        linkType.setRestrictions(restriction);

        linkType.add("Knows");

        properties.add(linkType);

        

        ParameterInternal actorType = ParameterFactory.newInstance().create("ActorMode",String.class);

        restriction = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);

        actorType.setRestrictions(restriction);

        actorType.add("User");

        properties.add(actorType);

        

        

        foaf = GraphFactory.newInstance().create("Root");

        crawler = null;

    }

    

    /**

     * Create a new Parser using this graph to load data into

     * @param m graph to be loaded

     */

    public FOAF2Graph(Graph m){

        super();

        foaf = m;

        crawler = null;

    }

    

    /**

     * Create a new Parser using this graph and parsing new FOAF sites using

     * this crawler

     * @param m graph to be loaded

     * @param c crawler to parse new sites

     */

    public FOAF2Graph(Graph m, WebCrawler c){

        super();

        foaf = m;

        crawler = c;

    }

    

    

    /**

     * Return the crawler used or null if none is assigned.

     * @return crawler assigned to this object

     */

    public Crawler getCrawler() {

        return crawler;

    }

    

    @Override

    public void setCrawler(Crawler crawler) {

        this.crawler = crawler;

    }

    

    @Override

    public ParsedObject get() {

        return foaf;

    }

    

    /**

     * Set this graph for loading.

     * @param foaf graph to be loaded.

     */

    public void setGraph(Graph foaf) {

        this.foaf = foaf;

    }

    

        /* (non-Javadoc)

         * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)

         */

    @Override

    public void characters(char[] arg0, int arg1, int arg2) throws SAXException {

        // TODO Auto-generated method stub

//		super.characters(arg0, arg1, arg2);

//		if(state == NAME){

//			System.out.println(new String(arg0,arg1,arg2));

//			Literal name = foaf.createLiteral(new String(arg0,arg1,arg2));

//			name = new String(arg0,arg1,arg2);

//		} else if(state == GIVENNAME){

//			System.out.println(new String(arg0,arg1,arg2));

//			givenname = new String(arg0,arg1,arg2);

//		} else if(state == FAMILY_NAME){

//			System.out.println(new String(arg0,arg1,arg2));

//			family_name = new String(arg0,arg1,arg2);

//		} else

        if(state == NICK){

            String id = new String(arg0,arg1,arg2);

            if(foaf.getActor((String)properties.get("ActorType").getValue().get(0),id)==null){

                current = ActorFactory.newInstance().create((String)properties.get("ActorType").getValue().get(0),new String(arg0,arg1,arg2));

                foaf.add(current);

                if(child && (crawler != null)){

                    try {

                        crawler.crawl("http://" + id + ".livejournal.com/data/foaf");

                    } catch (MalformedURLException ex) {

                        Logger.getLogger(FOAF2Graph.class.getName()).log(Level.SEVERE, null, ex);

                    } catch (IOException ex) {

                        Logger.getLogger(FOAF2Graph.class.getName()).log(Level.SEVERE, null, ex);

                    }

                }

            }else{

                current = foaf.getActor((String)properties.get("ActorType").getValue().get(0),id);

            }

//		} else

//        if (state == MBOX){

//            String id = new String(arg0,arg1,arg2);

//            if(foaf.getUser(id)==null){

//                current = UserFactory.newInstance().create("Basic",new String(arg0,arg1,arg2));

//                foaf.add(current);

//            }else{

//                current = foaf.getUser(id);

//            }

////            current = foaf.createResource(new String(arg0,arg1,arg2));

//        }else if(state == MBOX2){

////            current = foaf.createResource(new String(arg0,arg1,arg2));

//            String id = new String(arg0,arg1,arg2);

//            if(foaf.getUser(id)==null){

//                current = UserFactory.newInstance().create("Basic",new String(arg0,arg1,arg2));

//                foaf.add(current);

//            }else{

//                current = foaf.getUser(id);

//            }

        }else if (state == TITLE){

            title = new String(arg0,arg1,arg2);

        }else if (state == GENDER){

            gender = new String(arg0,arg1,arg2);

        }else if (state == BIRTH){

            dateOfBirth = new String(arg0,arg1,arg2);

        }else if(state == AIM){

            aim = new String(arg0,arg1,arg2);

        }else if(state == MSN){

            msn = new String(arg0,arg1,arg2);

        }else if(state == BIO){

            bio = new String(arg0,arg1,arg2);

        }

    }

    

        /* (non-Javadoc)

         * @see org.xml.sax.helpers.DefaultHandler#endDocument()

         */

    @Override

    public void endDocument() throws SAXException {

        super.endDocument();

//        System.out.println("End Document");

    }

    

        /* (non-Javadoc)

         * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)

         */

    @Override

    public void endElement(String arg0, String arg1, String arg2) throws SAXException {

//		System.out.println("End: "+arg0 + " - " + arg1 + " - " + arg2);

        if(unknown){

            if((arg2 != null) && (arg2.compareTo(unknown_arg2)==0)){

                unknown = false;

                unknown_arg1 = null;

                unknown_arg2 = null;

            }

        }else if(arg1.compareTo("RDF")==0){

            state = START;

        }else if((arg2.compareTo("foaf:Person")==0)||(arg1.compareTo("Person")==0)){

            state = oldstate;

            if(child && (current != null)){

                Link stat = LinkFactory.newInstance().create((String)properties.get("LinkType").getValue().get(0));

                stat.set(parent,1.0,current);

                foaf.add(stat);

                current = null;

            }else if(current != null){

//                System.out.println("Creating user");

//					if(name != null){

//						current.addProperty(foaf.createProperty("http://xmlns.com/foaf/0.1","name"),name);

//						name = null;

//					}

                if(title != null){

                    try {

                        Property property = PropertyFactory.newInstance().create((String)PropertyFactory.newInstance().getClassParameter().get(),"foaf:title",String.class);

                        property.add(title);

                        current.add(property);

                        title = null;

                    } catch (InvalidObjectTypeException ex) {

                        Logger.getLogger(FOAF2Graph.class.getName()).log(Level.SEVERE, null, ex);

                    }

                }

//					if(givenname != null){

//						current.addProperty(foaf.createProperty("http://xmlns.com/foaf/0.1","givenname"),givenname);

//						givenname = null;

//					}

//					if(family_name != null){

//						current.addProperty(foaf.createProperty("http://xmlns.com/foaf/0.1", "family_name"),family_name);

//						family_name = null;

//					}

//					if(nick != null){

//						current.addProperty(foaf.createProperty("http://xmlns.com/foaf/0.1","nick"),nick);

//						nick = null;

//					}

                if(homepage != null){

                    try{

                    Property props = PropertyFactory.newInstance().create("BasicProperty","Homepage",URL.class);

                    props.add(new URL(homepage));

                    current.add(props);

                    if(crawler != null){

//                        crawler.crawl(homepage);

                    }

                    }catch(MalformedURLException ex){

                        Logger.getLogger(this.getClass().getName()).log(Level.WARNING,"homepage '"+homepage+"' of user '"+current.getID()+"'is not a valid URL");

                    }catch(InvalidObjectTypeException ex){

                        Logger.getLogger(FOAF2Graph.class.getName()).log(Level.SEVERE, null, ex);

                    }

                    homepage = null;

                }

                if(phone != null){

                    try {

                        Property property = PropertyFactory.newInstance().create("BasicProperty","foaf:phone",String.class);

                        property.add(phone);

                        current.add(property);

                        phone = null;

                    } catch (InvalidObjectTypeException ex) {

                        Logger.getLogger(FOAF2Graph.class.getName()).log(Level.SEVERE, null, ex);

                    }

                }

                if(workplacehomepage != null){

                    try{

                    Property props = PropertyFactory.newInstance().create("BasicProperty","WorkplaceHomePage",URL.class);

                    props.add(new URL(workplacehomepage));

                    current.add(props);

                    if(crawler != null){

//                        crawler.crawl(workplacehomepage);

                    }

                    }catch(MalformedURLException ex){

                        Logger.getLogger(this.getClass().getName()).log(Level.WARNING,"WorkplaceHomePage '"+workplacehomepage+"' of user '"+current.getID()+"'is not a valid URL");

                    }catch(InvalidObjectTypeException ex){

                        Logger.getLogger(FOAF2Graph.class.getName()).log(Level.SEVERE, null, ex);

                    }



                    workplacehomepage = null;

                }

                if(workinfohomepage != null){

                    try{

                    Property props = PropertyFactory.newInstance().create("BasicProperty","WorkInfoHomePage",URL.class);

                    props.add(new URL(workinfohomepage));

                    current.add(props);

                    if(crawler != null){

//                        crawler.crawl(workinfohomepage);

                    }

                    }catch(MalformedURLException ex){

                        Logger.getLogger(this.getClass().getName()).log(Level.WARNING,"WorkInfoHomePage '"+workinfohomepage+"' of user '"+current.getID()+"'is not a valid URL");

                    }catch(InvalidObjectTypeException ex){

                        Logger.getLogger(FOAF2Graph.class.getName()).log(Level.SEVERE, null, ex);

                    }

                    workinfohomepage = null;

                }

                if(schoolhomepage != null){

                    try{

                    Property props = PropertyFactory.newInstance().create("BasicProperty","SchoolHomePage",URL.class);

                    props.add(new URL(schoolhomepage));

                    current.add(props);

                    if(crawler != null){

//                        crawler.crawl(schoolhomepage);

                    }

                    }catch(MalformedURLException ex){

                        Logger.getLogger(this.getClass().getName()).log(Level.WARNING,"SchoolHomePage '"+schoolhomepage+"' of user '"+current.getID()+"'is not a valid URL");

                    }catch(InvalidObjectTypeException ex){

                        Logger.getLogger(FOAF2Graph.class.getName()).log(Level.SEVERE, null, ex);

                    }

                    schoolhomepage = null;

                }

//                if(interest != null){

//                    webPage = PageFactory.newInstance().create("Basic",interest);

//                    current.add(webPage);

//                    if(crawler != null){

//                        crawler.crawl(schoolhomepage);

//                    }

//                    interest = null;

//                }

                if(gender != null){

                    try {

                        Property property = PropertyFactory.newInstance().create("BasicProperty","Gender",String.class);

                        property.add(gender);

                        current.add(property);

                    } catch (InvalidObjectTypeException ex) {

                        Logger.getLogger(FOAF2Graph.class.getName()).log(Level.SEVERE, null, ex);

                    }

                }

                if(country != null){

                    try {

                        Property property = PropertyFactory.newInstance().create("BasicProperty","ya:country",String.class);

                        property.add(country);

                        current.add(property);

                        country = null;

                    } catch (InvalidObjectTypeException ex) {

                        Logger.getLogger(FOAF2Graph.class.getName()).log(Level.SEVERE, null, ex);

                    }

                }

                if(city != null){

                    try {

                        Property property = PropertyFactory.newInstance().create("BasicProperty","ya:city",String.class);

                        property.add(city);

                        current.add(property);

                        city = null;

                    } catch (InvalidObjectTypeException ex) {

                        Logger.getLogger(FOAF2Graph.class.getName()).log(Level.SEVERE, null, ex);

                    }

                }

                if(dateOfBirth != null){

                    try {

                       Property property = PropertyFactory.newInstance().create("BasicProperty","foaf:dateOfBirth",String.class);

                        property.add(dateOfBirth);

                        current.add(property);

                        dateOfBirth = null;

                    } catch (InvalidObjectTypeException ex) {

                        Logger.getLogger(FOAF2Graph.class.getName()).log(Level.SEVERE, null, ex);

                    }

                }

                if(document != null){

                    try{

                    Property props = PropertyFactory.newInstance().create("BasicProperty","Document",URL.class);

                    props.add(new URL(document));

                    current.add(props);

                    if(crawler != null){

//                        crawler.crawl(document);

                    }

                    }catch(MalformedURLException ex){

                        Logger.getLogger(this.getClass().getName()).log(Level.WARNING,"Document '"+document+"' of user '"+current.getID()+"'is not a valid URL");

                    }catch(InvalidObjectTypeException ex){

                        Logger.getLogger(FOAF2Graph.class.getName()).log(Level.SEVERE, null, ex);

                    }

                    document = null;

                }

                if(aim != null){

                    try {

                       Property property = PropertyFactory.newInstance().create("BasicProperty","foaf:aimChatID",String.class);

                        property.add(aim);

                        current.add(property);

                        aim = null;

                    } catch (InvalidObjectTypeException ex) {

                        Logger.getLogger(FOAF2Graph.class.getName()).log(Level.SEVERE, null, ex);

                    }

                }

                if(msn != null){

                    try {

                       Property property = PropertyFactory.newInstance().create("BasicProperty","foaf:msnChatID",String.class);

                        property.add(msn);

                        current.add(property);

                        msn = null;

                    } catch (InvalidObjectTypeException ex) {

                        Logger.getLogger(FOAF2Graph.class.getName()).log(Level.SEVERE, null, ex);

                    }

                }

                if(weblog != null){

                    try{

                    Property props = PropertyFactory.newInstance().create("BasicProperty","Weblog",URL.class);

                    props.add(new URL(weblog));

                    current.add(props);

                    if(crawler != null){

//                        crawler.crawl(weblog);

                    }

                    }catch(MalformedURLException ex){

                        Logger.getLogger(this.getClass().getName()).log(Level.WARNING,"Weblog '"+weblog+"' of user '"+current.getID()+"'is not a valid URL");

                    }catch(InvalidObjectTypeException ex){

                        Logger.getLogger(FOAF2Graph.class.getName()).log(Level.SEVERE, null, ex);

                    }

                    weblog = null;

                }

                if(bio != null){

                    try {

                       Property property = PropertyFactory.newInstance().create("BasicProperty","ya:bio",String.class);

                        property.add(bio);

                        current.add(property);

                        bio = null;

                    } catch (InvalidObjectTypeException ex) {

                        Logger.getLogger(FOAF2Graph.class.getName()).log(Level.SEVERE, null, ex);

                    }

                }

//                if(interest.getValue().length > 0){

                current.add(interest);

//                    System.out.println("Added interest property");

                interest = PropertyFactory.newInstance().create("BasicProperty","interest",String.class);

//                }

            }

            

//		}else		if((arg2.compareTo("foaf:name")==0)||(arg1.compareTo("name")==0)){

//			state = PERSON;

        }else		if((arg2.compareTo("foaf:title")==0)||(arg1.compareTo("title")==0)){

            state = PERSON;

//		}else		if((arg2.compareTo("foaf:givenname")==0)||(arg1.compareTo("givenname")==0)){

//			state = PERSON;

//		}else		if((arg2.compareTo("foaf:family_name")==0)|(arg1.compareTo("family_name")==0)){

//			state = PERSON;

        }else		if((arg2.compareTo("foaf:nick")==0)||(arg1.compareTo("nick")==0)){

            state = PERSON;

        }else		if((arg2.compareTo("foaf:homepage")==0)||(arg1.compareTo("homepage")==0)){

            state = PERSON;

        }else		if((arg2.compareTo("foaf:phone")==0)||(arg1.compareTo("phone")==0)){

            state = PERSON;

        }else		if((arg2.compareTo("foaf:workplaceHomepage")==0)||(arg1.compareTo("workplaceHomepage")==0)){

            state = PERSON;

        }else		if((arg2.compareTo("foaf:workinfoHomepage")==0)||(arg1.compareTo("workinfoHomepage")==0)){

            state = PERSON;

        }else		if((arg2.compareTo("foaf:schoolHomepage")==0)||(arg1.compareTo("schoolHomepage")==0)){

            state = PERSON;

        }else		if((arg2.compareTo("foaf:knows")==0)||(arg1.compareTo("knows")==0)){

            state = PERSON;

            current = parent;

            child = false;

        }else if ((arg2.compareTo("foaf:mbox_sha1sum")==0)||(arg1.compareTo("mbox_sha1sum")==0)){

            state = PERSON;

        }else if ((arg2.compareTo("foaf:topic_interest")==0)||(arg1.compareTo("topic_interest")==0)){

            state = PERSON;

        }else if ((arg2.compareTo("foaf:interest")==0)||(arg1.compareTo("interest")==0)){

            state = PERSON;

        }else if ((arg2.compareTo("foaf:mbox")==0)||(arg1.compareTo("mbox")==0)){

            state = PERSON;

        }else if((arg2.compareTo("foaf:gender")==0)||(arg1.compareTo("gender")==0)){

            state = PERSON;

        }else if((arg2.compareTo("ya:country")==0)||(arg1.compareTo("country")==0)){

            state = PERSON;

        }else if((arg2.compareTo("ya:city")==0)||(arg1.compareTo("city")==0)){

            state = PERSON;

        }else if((arg2.compareTo("foaf:dateOfBirth")==0)||(arg1.compareTo("dateOfBirth")==0)){

            state = PERSON;

        }else if((arg2.compareTo("foaf:page")==0)||(arg1.compareTo("page")==0)){

            state = PERSON;

        }else if((arg2.compareTo("foaf:Document")==0)||(arg1.compareTo("Document")==0)){

            state = PAGE;

        }else if((arg2.compareTo("foaf:aimChatID")==0)||(arg1.compareTo("aimChatID")==0)){

            state = PERSON;

        }else if((arg2.compareTo("foaf:msnChatID")==0)||(arg1.compareTo("msnChatID")==0)){

            state = PERSON;

        }else if((arg2.compareTo("foaf:weblog")==0)||(arg1.compareTo("weblog")==0)){

            state = PERSON;

        }else if((arg2.compareTo("ya:bio")==0)||(arg1.compareTo("bio")==0)){

            state = PERSON;

        }

    }

    

        /* (non-Javadoc)

         * @see org.xml.sax.helpers.DefaultHandler#startDocument()

         */

    @Override

    public void startDocument() throws SAXException {

//		super.startDocument();

        state = START;

//		name = null;

        title = null;

//		givenname = null;

//		family_name = null;

//		nick = null;

        homepage = null;

        phone = null;

        workplacehomepage = null;

        workinfohomepage = null;

        schoolhomepage = null;

        child = false;

        unknown_arg1=null;

        unknown_arg2 = null;

        current = null;

        parent = null;

        gender = null;

        country = null;

        city = null;

        dateOfBirth = null;

        page = null;

        aim = null;

        msn = null;

        weblog = null;

        bio = null;

        document = null;

        interest = PropertyFactory.newInstance().create("BasicProperty","interest",String.class);

    }

    

        /* (non-Javadoc)

         * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)

         */

    @Override

    public void startElement(String arg0, String arg1, String arg2, Attributes arg3) throws SAXException {

//		super.startElement(arg0, arg1, arg2, arg3);

//		System.out.print("Start: " + arg0 + " - " + arg1 + " - " + arg2);

//                if(arg3.getLength()>0){

//                   System.out.println("-"+arg3.getValue(0));

//              }else{

//                 System.out.println();

//                }

        if(!unknown){

            if(arg1.compareTo("RDF")==0){

                oldstate = state;

                state = RDF;

            }else 		if((arg2.compareTo("foaf:Person")==0)||(arg1.compareTo("Person")==0)){

                oldstate = state;

                state = PERSON;

//		}else		if((arg2.compareTo("foaf:name")==0)||(arg1.compareTo("foaf:name")==0)){

//			oldstate = state;

//			state = NAME;

            }else	if((arg2.compareTo("foaf:title")==0)||(arg1.compareTo("title")==0)){

                oldstate = state;

                state = TITLE;

                if(arg3 != null){

                    String temp = arg3.getValue("rdf:resource");

                    if((temp!=null)&&(!temp.contentEquals(""))){

                        title = temp;

                    }

                }

//		}else		if((arg2.compareTo("foaf:givenname")==0)||(arg1.compareTo("givenname")==0)){

//			oldstate = state;

//			state = GIVENNAME;

//		}else		if((arg2.compareTo("foaf:family_name")==0)||(arg1.compareTo("family_name")==0)){

//			oldstate = state;

//			state = FAMILY_NAME;

            }else		if((arg2.compareTo("foaf:nick")==0)||(arg1.compareTo("nick")==0)){

                oldstate = state;

                state = NICK;

            }else if((arg2.compareTo("foaf:homepage")==0)||(arg1.compareTo("homepage")==0)){

                oldstate = state;

                state = HOMEPAGE;

                if(arg3!=null){

                    String temp = arg3.getValue("rdf:resource");

                    if((temp!=null)&&(!temp.contentEquals(""))){

                        homepage = temp;

                    }

                }

            }else		if((arg2.compareTo("foaf:phone")==0)||(arg1.compareTo("phone")==0)){

                oldstate = state;

                state = PHONE;

                if(arg3 != null){

                    String temp = arg3.getValue("rdf:resource");

                    if((temp!=null)&&(!temp.contentEquals(""))){

                        phone = temp;

                    }

                }

            }else		if((arg2.compareTo("foaf:workplaceHomepage")==0)||(arg1.compareTo("workplaceHomepage")==0)){

                oldstate = state;

                state = WORKPLACEHOMEPAGE;

                if(arg3!=null){

                    String temp = arg3.getValue("rdf:resource");

                    if((temp!=null)&&(!temp.contentEquals(""))){

                        workplacehomepage = temp;

                    }

                }

            }else if((arg2.compareTo("foaf:workinfoHomepage")==0)||(arg1.compareTo("workinfoHomepage")==0)){

                oldstate = state;

                state = WORKINFOHOMEPAGE;

                if(arg3!=null){

                    String temp = arg3.getValue("rdf:resource");

                    if((temp!=null)&&(!temp.contentEquals(""))){

                        workinfohomepage = temp;

                    }

                }

            }else if((arg2.compareTo("foaf:schoolHomepage")==0)||(arg1.compareTo("schoolHomepage")==0)){

                oldstate = state;

                state = SCHOOLHOMEPAGE;

                if(arg3!=null){

                    String temp = arg3.getValue("rdf:resource");

                    if((temp!=null)&&(!temp.contentEquals(""))){

                        schoolhomepage = temp;

                    }

                }

            }else if((arg2.compareTo("foaf:knows")==0)||(arg1.compareTo("knows")==0)){

                oldstate = state;

                state = KNOWS;

                child = true;

                parent = current;

            }

//           else if((arg2.compareTo("foaf:mbox_sha1sum")==0)||(arg1.compareTo("mbox_sha1sum")==0)){

//                oldstate = state;

//                state = MBOX;

//            }

            else if((arg2.compareTo("foaf:interest")==0)||(arg1.compareTo("interest")==0)){

                oldstate = state;

                state = INTEREST;

                if(arg3!=null){

                    String temp = arg3.getValue("dc:title");

                    if((temp != null)&&(!temp.contentEquals(""))){

                        try {

                            interest.add(temp);

                        } catch (InvalidObjectTypeException ex) {

                            Logger.getLogger(FOAF2Graph.class.getName()).log(Level.SEVERE, null, ex);

                        }

//                        System.out.println("Added "+temp);

                    }else{

                        temp = arg3.getValue("rdf:resource");

                        if((temp!=null)&&(!temp.contentEquals(""))){

//                            interest = temp;

                        }

                    }

                }

            } else if((arg2.compareTo("foaf:topic_interest")==0)||(arg1.compareTo("topic_interest")==0)){

                oldstate = state;

                state = TOPIC_INTEREST;

                String box = arg3.getValue("dc:title");

                if((box != null)&&(!box.contentEquals(""))){

                    /*

                    Property props = PropertyFactory.newInstance().create("Music",URL.class);

                    props.add(new URL(arg3.getValue("rdf:resource")));

                    current.add(props);

//                    String temp = arg3.getValue("rdf:resource");

//                    if((temp != null)&&(!temp.contentEquals(""))){

//                        Page page = PageFactory.newInstance().create("Music",temp);

//                        current.add(page);

//                        if(crawler!=null){

//                            crawler.crawl(temp);

//                        }

//                    }

                     */

                    

                }else{

                    /*

                    box = arg3.getValue("rdf:resource");

                    if((box != null)&&(!box.contentEquals(""))){

                        

                    Property props = PropertyFactory.newInstance().create("rdf:resource",URL.class);

                    props.add(new URL(box));

                    current.add(props);

                        if(crawler!=null){

//                            crawler.crawl(box);

                        }

                    }

                    */

                }

//            }else if((arg2.compareTo("foaf:mbox")==0)||(arg1.compareTo("mbox")==0)){

//                oldstate = state;

//                state = MBOX2;

//                String box = arg3.getValue("rdf:resource");

//                if((box != null)&&(!box.contentEquals(""))){

//                    current = UserFactory.newInstance().create("Basic",box);

//                    foaf.add(current);

//                }

            }else if((arg2.compareTo("foaf:gender")==0)||(arg1.compareTo("gender")==0)){

                oldstate = state;

                state = GENDER;

                String box = arg3.getValue("rdf:resource");

                if((box != null)&&(!box.contentEquals(""))){

                    gender = box;

                }

            }else if ((arg2.compareTo("ya:country")==0)||(arg1.compareTo("country")==0)){

                oldstate = state;

                state = COUNTRY;

                String box = arg3.getValue("dc:title");

                if((box != null)&&(!box.contentEquals(""))){

                    country = box;

                }

            }else if ((arg2.compareTo("ya:city")==0)||(arg1.compareTo("city")==0)){

                oldstate = state;

                state = CITY;

                String box = arg3.getValue("dc:title");

                if((box != null)&&(!box.contentEquals(""))){

                    city = box;

                }

            }else if ((arg2.compareTo("foaf:dateOfBirth")==0)||(arg1.compareTo("dateOfBirth")==0)){

                oldstate = state;

                state = BIRTH;

            }else if ((arg2.compareTo("foaf:page")==0)||(arg1.compareTo("page")==0)){

                oldstate = state;

                state = PAGE;

            }else if ((arg2.compareTo("foaf:aimChatID")==0)||(arg1.compareTo("aimChatID")==0)){

                oldstate = state;

                state = AIM;

            }else if ((arg2.compareTo("foaf:msnChatID")==0)||(arg1.compareTo("msnChatID")==0)){

                oldstate = state;

                state = MSN;

            }else if ((arg2.compareTo("foaf:weblog")==0)||(arg1.compareTo("weblog")==0)){

                oldstate = state;

                state = WEBLOG;

                String box = arg3.getValue("rdf:resource");

                if((box != null)&&(!box.contentEquals(""))){

                    weblog = box;

                }

            }else if ((arg2.compareTo("ya:bio")==0)||(arg1.compareTo("bio")==0)){

                oldstate = state;

                state = BIO;

            }else if((arg2.compareTo("foaf:Document")==0)||(arg1.compareTo("Document")==0)){

                oldstate = state;

                state = DOCUMENT;

                String box = arg3.getValue("rdf:about");

                if((box != null)&&(!box.contentEquals(""))){

                    document = box;

                }

            }else if (state != START){

                unknown = true;

                unknown_arg1 = arg1;

                unknown_arg2 = arg2;

            }

        }

    }

    

    @Override

    public Handler duplicate() {

        return new FOAF2Graph(foaf);

    }



    @Override

    public void set(ParsedObject o) {

        if(o instanceof Graph){

            foaf = (Graph)o;

        }

    }

    

}

