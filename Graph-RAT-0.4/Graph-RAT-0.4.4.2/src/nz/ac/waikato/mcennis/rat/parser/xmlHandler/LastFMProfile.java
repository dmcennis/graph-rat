/*
 * Created 13-5-08
 * Copyright Daniel McEnnis, see license.txt
 */
package org.mcennis.graphrat.parser.xmlHandler;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mcennis.graphrat.graph.Graph;
import org.mcennis.graphrat.actor.Actor;
import org.mcennis.graphrat.actor.ActorFactory;
import org.mcennis.graphrat.parser.ParsedObject;
import org.mcennis.graphrat.parser.ToFileParser;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * Parse the Profile XML file from LastFM.  See audioscrobbler.net for a description of the file
 * format.  Usually obtained from the CrawlLastFM data aquisition module. Contains
 * all the demographic information associated with this username.
 *
 * @author Daniel McEnnis
 */
public class LastFMProfile extends Handler {

    Graph graph = null;
    Actor user = null;
    private Locator locator;

    enum State {

        START, SKIP, PROFILE, URL, REALNAME, MBOX, REGISTERED, AGE, GENDER, COUNTRY, PLAYCOUNT, AVATAR, ICON
    };
    State state = State.START;
    ToFileParser parser = null;

    /**
     * Set where the given parser will store files when they are parsed
     * @param parser ToFileParser to store files parsed by this parser
     */
    public void setParser(ToFileParser parser) {
        this.parser = parser;
    }

    @Override
    public void endDocument() throws SAXException {
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        try {
            if (state == State.SKIP) {
                if (localName.contentEquals("profile") || qName.contentEquals("profile")) {
                    state = State.START;
                }
            } else if (state == State.PROFILE) {
                graph.add(user);
                state = State.START;
            } else if (state == State.AGE) {
                state = State.PROFILE;
            } else if (state == State.AVATAR) {
                state = State.PROFILE;
            } else if (state == State.COUNTRY) {
                state = State.PROFILE;
            } else if (state == State.GENDER) {
                state = State.PROFILE;
            } else if (state == State.ICON) {
                state = State.PROFILE;
            } else if (state == State.MBOX) {
                state = State.PROFILE;
            } else if (state == State.PLAYCOUNT) {
                state = State.PROFILE;
            } else if (state == State.REALNAME) {
                state = State.PROFILE;
            } else if (state == State.REGISTERED) {
                state = State.PROFILE;
            } else if (state == State.URL) {
                state = State.PROFILE;
            }
        } catch (NullPointerException ex) {
            Logger.getLogger(LastFMProfile.class.getName()).log(Level.SEVERE, "Null Pointer Exception at line " + locator.getLineNumber());
        }
    }

    @Override
    public void startDocument() throws SAXException {
        state = State.START;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        try {
            if (user != null) {
                if (state == State.URL) {
                    try {
                        String string = new String(ch, start, length);
                        java.net.URL url = new java.net.URL(string);
                        Properties props = new Properties();
                        props.setProperty("PropertyID", "LastFM Homepage");
                        props.setProperty("PropertyClass", "java.net.URL");
                        props.setProperty("PropertyType", "Basic");
                        Property urlProperty = PropertyFactory.newInstance().create(props);
                        urlProperty.add(url);
                        user.add(urlProperty);
                    } catch (InvalidObjectTypeException ex) {
                        Logger.getLogger(LastFMProfile.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (MalformedURLException ex) {
                        Logger.getLogger(LastFMProfile.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else if (state == State.AGE) {
                    try {
                        int age = Integer.parseInt(new String(ch, start, length));
                        Properties props = new Properties();
                        props.setProperty("PropertyID", "Age");
                        props.setProperty("PropertyClass", "java.lang.Integer");
                        props.setProperty("PropertyType", "Basic");
                        Property urlProperty = PropertyFactory.newInstance().create(props);
                        urlProperty.add(new Integer(age));
                        user.add(urlProperty);
                    } catch (InvalidObjectTypeException ex) {
                        Logger.getLogger(LastFMProfile.class.getName()).log(Level.SEVERE, null, ex);
                    }

                } else if (state == State.AVATAR) {
                    try {
                        String string = new String(ch, start, length);
                        java.net.URL url = new java.net.URL(string);
                        Properties props = new Properties();
                        props.setProperty("PropertyID", "Avatar");
                        props.setProperty("PropertyClass", "java.net.URL");
                        props.setProperty("PropertyType", "Basic");
                        Property urlProperty = PropertyFactory.newInstance().create(props);
                        urlProperty.add(url);
                        user.add(urlProperty);
                    } catch (InvalidObjectTypeException ex) {
                        Logger.getLogger(LastFMProfile.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (MalformedURLException ex) {
                        Logger.getLogger(LastFMProfile.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else if (state == State.COUNTRY) {
                    try {
                        String string = new String(ch, start, length);
                        Properties props = new Properties();
                        props.setProperty("PropertyID", "Country");
                        props.setProperty("PropertyClass", "java.lang.String");
                        props.setProperty("PropertyType", "Basic");
                        Property urlProperty = PropertyFactory.newInstance().create(props);
                        urlProperty.add(string);
                        user.add(urlProperty);
                    } catch (InvalidObjectTypeException ex) {
                        Logger.getLogger(LastFMProfile.class.getName()).log(Level.SEVERE, null, ex);
                    }

                } else if (state == State.GENDER) {
                    try {
                        String string = new String(ch, start, length);
                        Properties props = new Properties();
                        props.setProperty("PropertyID", "Gender");
                        props.setProperty("PropertyClass", "java.lang.String");
                        props.setProperty("PropertyType", "Basic");
                        Property urlProperty = PropertyFactory.newInstance().create(props);
                        urlProperty.add(string);
                        user.add(urlProperty);
                    } catch (InvalidObjectTypeException ex) {
                        Logger.getLogger(LastFMProfile.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else if (state == State.ICON) {
                    try {
                        String string = new String(ch, start, length);
                        java.net.URL url = new java.net.URL(string);
                        Properties props = new Properties();
                        props.setProperty("PropertyID", "Icon");
                        props.setProperty("PropertyClass", "java.net.URL");
                        props.setProperty("PropertyType", "Basic");
                        Property urlProperty = PropertyFactory.newInstance().create(props);
                        urlProperty.add(url);
                        user.add(urlProperty);
                    } catch (InvalidObjectTypeException ex) {
                        Logger.getLogger(LastFMProfile.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (MalformedURLException ex) {
                        Logger.getLogger(LastFMProfile.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else if (state == State.MBOX) {
                    try {
                        String string = new String(ch, start, length);
                        Properties props = new Properties();
                        props.setProperty("PropertyID", "MBox");
                        props.setProperty("PropertyClass", "java.lang.String");
                        props.setProperty("PropertyType", "Basic");
                        Property urlProperty = PropertyFactory.newInstance().create(props);
                        urlProperty.add(string);
                        user.add(urlProperty);
                    } catch (InvalidObjectTypeException ex) {
                        Logger.getLogger(LastFMProfile.class.getName()).log(Level.SEVERE, null, ex);
                    }

                } else if (state == State.PLAYCOUNT) {
                    try {
                        int age = Integer.parseInt(new String(ch, start, length));
                        Properties props = new Properties();
                        props.setProperty("PropertyID", "PlayCount");
                        props.setProperty("PropertyClass", "java.lang.Integer");
                        props.setProperty("PropertyType", "Basic");
                        Property urlProperty = PropertyFactory.newInstance().create(props);
                        urlProperty.add(new Integer(age));
                        user.add(urlProperty);
                    } catch (InvalidObjectTypeException ex) {
                        Logger.getLogger(LastFMProfile.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else if (state == State.REALNAME) {
                    try {
                        String string = new String(ch, start, length);
                        Properties props = new Properties();
                        props.setProperty("PropertyID", "name");
                        props.setProperty("PropertyClass", "java.lang.String");
                        props.setProperty("PropertyType", "Basic");
                        Property urlProperty = PropertyFactory.newInstance().create(props);
                        urlProperty.add(string);
                        user.add(urlProperty);
                    } catch (InvalidObjectTypeException ex) {
                        Logger.getLogger(LastFMProfile.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            }
        } catch (NullPointerException ex) {
            Logger.getLogger(LastFMProfile.class.getName()).log(Level.SEVERE, "Null Pointer Exception at line " + locator.getLineNumber());
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        try {
            if ((localName.contentEquals("profile")) || (qName.contentEquals("profile"))) {
                String username = attributes.getValue("username");
                if (parser != null) {
                    parser.setSubDirectory(username + File.separator);
                    parser.setFilename("profile.xml");
                }
                user = graph.getActor("user", username);
                if (user == null) {
                    Properties props = new Properties();
                    props.setProperty("ActorClass", "Basic");
                    props.setProperty("ActorType", "user");
                    props.setProperty("ActorID", username);
                    user = ActorFactory.newInstance().create(props);
                    graph.add(user);
                    state = State.PROFILE;

                    try {
                        int cluster = Integer.parseInt(attributes.getValue("cluster"));
                        props.setProperty("PropertyID", "Cluster ID");
                        props.setProperty("PropertyClass", "java.lang.Integer");
                        props.setProperty("PropertyType", "Basic");
                        Property urlProperty = PropertyFactory.newInstance().create(props);
                        urlProperty.add(new Integer(cluster));
                        user.add(urlProperty);

                        int id = Integer.parseInt(attributes.getValue("id"));
                        props.setProperty("PropertyID", "User ID");
                        props.setProperty("PropertyClass", "java.lang.Integer");
                        props.setProperty("PropertyType", "Basic");
                        urlProperty = PropertyFactory.newInstance().create(props);
                        urlProperty.add(new Integer(id));
                        user.add(urlProperty);
                    } catch (InvalidObjectTypeException ex) {
                        Logger.getLogger(LastFMProfile.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    state = State.SKIP;
                }
            } else if ((localName.contentEquals("url")) || (qName.contentEquals("url"))) {
                if (state != State.SKIP) {
                    state = State.URL;
                }
            } else if ((localName.contentEquals("realname")) || (qName.contentEquals("realname"))) {
                if (state != State.SKIP) {
                    state = State.REALNAME;
                }
            } else if ((localName.contentEquals("mbox_sha1sum")) || (qName.contentEquals("mbox_sha1sum"))) {
                if (state != State.SKIP) {
                    state = State.MBOX;
                }
            } else if ((localName.contentEquals("registered")) || (qName.contentEquals("registered"))) {
                if (state != State.SKIP) {
                    state = State.REGISTERED;
                    try {
                        int registered = Integer.parseInt(attributes.getValue("unixtime"));
                        Properties props = new Properties();
                        props.setProperty("PropertyID", "Date Registered");
                        props.setProperty("PropertyClass", "java.lang.Integer");
                        props.setProperty("PropertyType", "Basic");
                        Property urlProperty = PropertyFactory.newInstance().create(props);
                        urlProperty.add(new Integer(registered));
                        user.add(urlProperty);
                    } catch (InvalidObjectTypeException ex) {
                        Logger.getLogger(LastFMProfile.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } else if ((localName.contentEquals("age")) || (qName.contentEquals("age"))) {
                if (state != State.SKIP) {
                    state = State.AGE;
                }
            } else if ((localName.contentEquals("gender")) || (qName.contentEquals("gender"))) {
                if (state != State.SKIP) {
                    state = State.GENDER;
                }
            } else if ((localName.contentEquals("country")) || (qName.contentEquals("country"))) {
                if (state != State.SKIP) {
                    state = State.COUNTRY;
                }
            } else if ((localName.contentEquals("playcount")) || (qName.contentEquals("playcount"))) {
                if (state != State.SKIP) {
                    state = State.PLAYCOUNT;
                }
            } else if ((localName.contentEquals("avatar")) || (qName.contentEquals("avatar"))) {
                if (state != State.SKIP) {
                    state = State.AVATAR;
                }
            } else if ((localName.contentEquals("icon")) || (qName.contentEquals("icon"))) {
                if (state != State.SKIP) {
                    state = State.ICON;
                }
            } else {
                throw new SAXException("Start tag " + localName + "/" + qName + " is not known");
            }
        } catch (NullPointerException ex) {
            Logger.getLogger(LastFMProfile.class.getName()).log(Level.SEVERE, "Null Pointer Exception at line " + locator.getLineNumber());
        }
    }

    @Override
    public ParsedObject get() {
        return graph;
    }

    @Override
    public void set(ParsedObject o) {
        graph = (Graph) o;
    }

    @Override
    public Handler duplicate() {
        LastFMProfile ret = new LastFMProfile();
        ret.graph = this.graph;

        return ret;
    }

    @Override
    public void setDocumentLocator(Locator locator) {

        this.locator = locator;

    }
}
