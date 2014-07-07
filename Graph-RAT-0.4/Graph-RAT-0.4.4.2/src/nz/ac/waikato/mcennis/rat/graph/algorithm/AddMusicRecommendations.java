/* * AddMusicRecommendations.java * * Created on 12 June 2007, 21:24 * * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt) */package nz.ac.waikato.mcennis.rat.graph.algorithm;import java.util.Properties;import nz.ac.waikato.mcennis.rat.graph.Graph;import nz.ac.waikato.mcennis.rat.graph.actor.Actor;import org.dynamicfactory.descriptors.DescriptorFactory;import org.dynamicfactory.descriptors.InputDescriptor;import org.dynamicfactory.descriptors.InputDescriptorInternal;import org.dynamicfactory.descriptors.OutputDescriptor;import org.dynamicfactory.descriptors.OutputDescriptorInternal;import org.dynamicfactory.descriptors.SettableParameter;import nz.ac.waikato.mcennis.rat.graph.link.LinkFactory;import nz.ac.waikato.mcennis.rat.graph.link.Link;import java.util.Iterator;import org.dynamicfactory.model.ModelShell;import nz.ac.waikato.mcennis.rat.scheduler.Scheduler;/** * Creates derived user->artist links from knows, interest, and music links. * * NOTE: Adheres to direction in links - music from unequal links not made bidirectional * * FIXME: does not include code for including betweeness scores in calculations. * * @author Daniel McEnnis * */public class AddMusicRecommendations extends ModelShell implements Algorithm {    public static final long serialVersionUID = 2;    ParameterInternal[] parameter = new ParameterInternal[10];    InputDescriptorInternal[] input = new InputDescriptorInternal[4];    OutputDescriptorInternal[] output = new OutputDescriptorInternal[1];    /**     * Weighting applied to the similarity between users based on shared interests     *///    private double interestWeight = 1.0;    /**     * Weighting applied to the similarity between users based on shared musical tastes     *///    private double musicWeight = 1.0;    /**     *Weighting applied to the similarity between users based on knowing the other     *///    private double knowsWeight = 0.1;    /** Creates a new instance of AddMusicRecommendations */    public AddMusicRecommendations() {        init(null);    }    /**     * Build a set of music recommendations based on existing links     */    public void execute(Graph g) {//        Actor[] userList = g.getActor("User");        Iterator<Actor> userIterator = g.getActorIterator((String) parameter[2].getValue());        //        // Build artist recommendations for every user        //        fireChange(Scheduler.SET_ALGORITHM_COUNT,g.getActorCount((String) parameter[2].getValue()));        int actorCount = 0;        while (userIterator.hasNext()) {            Actor user = userIterator.next();            Link[] given = g.getLinkBySource((String) parameter[3].getValue(), user);            Link[] sharedInterests = g.getLinkBySource((String) parameter[4].getValue(), user);            Link[] sharedMusic = g.getLinkBySource((String) parameter[5].getValue(), user);            java.util.HashMap<String, Link> recommendations = new java.util.HashMap<String, Link>();            //            // load up given recommendations            //            if (((!((Boolean) parameter[7].getValue()).booleanValue())) && (given != null)) {                for (int j = 0; j < given.length; ++j) {                    recommendations.put(given[j].getDestination().getID(), given[j]);                }            }            //            // Consider the impact of every member this person declares that they know            //            if (sharedInterests != null) {                for (int j = 0; j < sharedInterests.length; ++j) {                    Link[] neighborsMusic = g.getLinkBySource((String) parameter[3].getValue(), sharedInterests[j].getDestination());                    //                    // For every musician liked by the neighbor, calculate the impact on the user                    //                    if (neighborsMusic != null) {                        for (int k = 0; k < neighborsMusic.length; ++k) {                            String name = neighborsMusic[k].getDestination().getID();                            Link derived;                            //                            // If this artist is already recommended, alter the recommendation, otherwise create it                            //                            if ((recommendations.containsKey(name)) && (recommendations.get(name).getType().contentEquals("Derived"))) {                                derived = recommendations.get(name);                            } else if (!recommendations.containsKey(name)) {                                java.util.Properties props = new java.util.Properties();                                props.setProperty("LinkType", (String) parameter[1].getValue());                                props.setProperty("LinkID", "Basic");                                derived = LinkFactory.newInstance().create(props);                                derived.set(user, 0.0, neighborsMusic[k].getDestination());                                recommendations.put(name, derived);                            }                            // if this isn't a given artist, adjust its weight accordingly                            Link a = recommendations.get(name);                            if (a.getType().contentEquals((String) parameter[1].getValue())) {                                //                                // adjust the weight                                //                                double ret = a.getStrength();                                ret += calculateArtistWeight(sharedInterests[j], sharedInterests, sharedMusic);                                a.set(ret);                            }                        } // for every artist                    }// if neighborMusic!=null                } // for every neighbor            }//            else if(sharedMusic != null){//                for(int j=0;j<sharedMusic.length;++j){//                    ////                    // Get artists from other user//                    ////                    ArtistLink[] artistList = g.getArtistLink("Given",sharedMusic[i].getDestination().getID());//                    for(int k=0;k<artistList.length;++k){//                        double ret = 0.0;//                        ArtistLink al = null;//                        String artistName = artistList[k].getArtist().getID();//                        if((recommendations.containsKey(artistName))&&(recommendations.get(artistName).getType().contentEquals("Derived"))){//                            al = recommendations.get(artistList[k].getArtist().getID());//                            al.set(al.getStrength()+musicWeight * sharedMusic[i].getStrength());//                        }else if(!recommendations.containsKey(artistName)){//                            al = ArtistLinkFactory.newInstance().create("Basic","Derived");//                            al.set(userList[i],artistList[k].getArtist(),musicWeight * sharedMusic[i].getStrength());//                            recommendations.put(artistName,al);//                        }//                    }//                }//            }else{////            }            java.util.Iterator<Link> it = recommendations.values().iterator();            while (it.hasNext()) {                Link l = it.next();                if (l.getType().contentEquals((String) parameter[1].getValue())) {                    g.add(l);                }            }            fireChange(Scheduler.SET_ALGORITHM_PROGRESS,actorCount++);        } // for every user in the graph    }    /**     * Helper function that calculates artist weights based on shared links     * @param knows link to actor whose recommendations are to be extracted     * @param interest set of all interest links from the person to recommend music for     * @param music set of all music links from the person to recommend music for.     * @return recommendation strength for any music this friend listens to     */    public double calculateArtistWeight(Link knows, Link[] interest, Link[] music) {        double ret = 0.1;        if (interest != null) {            for (int i = 0; i < interest.length; ++i) {                if (interest[i].getDestination().getID().contentEquals(knows.getDestination().getID())) {                    ret += ((Double) parameter[8].getValue()).doubleValue() * interest[i].getStrength();                }            }        }        if (music != null) {            for (int i = 0; i < music.length; ++i) {                if (music[i].getDestination().getID().contentEquals(knows.getDestination().getID())) {                    ret += ((Double) parameter[9].getValue()).doubleValue() * music[i].getStrength();                }            }        }        return ret;    }//    /**//     * Set the weight given to shared interests//     *//     * @param i weight to be given to interests//     *///    public void setInterestWeight(double i){//        interestWeight = i;//    }////    /**//     * return the weight given to shared interests//     *//     * @return weight given to shared interests//     *///    public double getInterestWeight(){//        return interestWeight;//    }//    /**//     * Set the weight of shared musical taste//     *//     * @param m weight given to shared musical tastes//     *///    public void setMusicWeight(double m){//        musicWeight = m;//    }////    /**//     *return the weight of shared musical tastes//     *//     * @return weight of shared musical tastes//     *///    public double getMusicWeight(){//        return musicWeight;//    }////    /**//     * set the weight for knows relationships//     *//     * @param weight of knows links//     *///    public void setKnowsWeight(double k){//        knowsWeight = k;//    }////    /**//     * get the weight for knows links//     *//     * @return weight applied to knows links//     *///    public double getKnowsWeight(){//        return knowsWeight;//    }    @Override    public InputDescriptor[] getInputType() {        return input;    }    @Override    public OutputDescriptor[] getOutputType() {        return output;    }    @Override    public Parameter[] getParameter() {        return parameter;    }    @Override    public Parameter getParameter(String param) {        for (int i = 0; i < parameter.length; ++i) {            if (parameter[i].getName().contentEquals(param)) {                return parameter[i];            }        }        return null;    }    @Override    public SettableParameter[] getSettableParameter() {        return new SettableParameter[]{parameter[7], parameter[8], parameter[9]};    }    @Override    public SettableParameter getSettableParameter(String param) {        for (int i = 7; i < 10; ++i) {            if (parameter[i].getName().contentEquals(param)) {                return parameter[i];            }        }        return null;    }    /**     * Parameters for initializing this algorithm     *      * <ol>     * <li>'name' - name of this isntance. Default is 'Basic Music Recommendation'.     * <li>'relation' - type (relation) of link to create.  Default is 'Derived'.     * <li>'actorType' - type (mode) of actor to use. Default is 'User'.     * <li>'groundTruthType' - type (relation) of link representing given musical      * tastes. Default is 'Given'.     * <li>'sourceType1' - type (relation) of link representing degree of shared      * interests. Default is 'Interest'     * <li>'sourceType2' - type (relation) of link representing degree of shared      * music. Deafult is 'Music'.     * <li>'adjustorPropertyType' - name of property defining prestige or      * centrality for actors. Default is'Knows Betweeness'     * <li>'evaluation' - should the system supress 'Derived' links matching      * 'Given' links. Default is 'false'.     * <li>'sourceType1Weight' - weight of 'sourceType1' links. Deafult '1.0'.     * <li>'sourceType2Weight' - weight of 'sourceType2' links. Deafult '1.0'.     * </ol>     * <br>     * <br>Input 1 - Link     * <br>Input 2 - Link     * <br>Input 3 - Link     * <br>Input 4 - Actor Property     * <br>Output 1 - Link     */    public void init(Properties map) {        Properties props = new Properties();        props.setProperty("Type", "java.lang.String");        props.setProperty("Name", "name");        props.setProperty("Class", "Basic");        props.setProperty("Structural", "true");        parameter[0] = DescriptorFactory.newInstance().createParameter(props);        if ((map != null) && (map.getProperty("name") != null)) {            parameter[0].setValue(map.getProperty("name"));        } else {            parameter[0].setValue("Basic Music Recommendation");        }        // Parameter 1 - relation        props.setProperty("Type", "java.lang.String");        props.setProperty("Name", "relation");        props.setProperty("Class", "Basic");        props.setProperty("Structural", "true");        parameter[1] = DescriptorFactory.newInstance().createParameter(props);        if ((map != null) && (map.getProperty("relation") != null)) {            parameter[1].setValue(map.getProperty("relation"));        } else {            parameter[1].setValue("Derived");        }        // Parameter 2 - actor type        props.setProperty("Type", "java.lang.String");        props.setProperty("Name", "actorType");        props.setProperty("Class", "Basic");        props.setProperty("Structural", "true");        parameter[2] = DescriptorFactory.newInstance().createParameter(props);        if ((map != null) && (map.getProperty("actorType") != null)) {            parameter[2].setValue(map.getProperty("actorType"));        } else {            parameter[2].setValue("User");        }        // Parameter 3 - Ground Truth Type        props.setProperty("Type", "java.lang.String");        props.setProperty("Name", "groundTruthType");        props.setProperty("Class", "Basic");        props.setProperty("Structural", "true");        parameter[3] = DescriptorFactory.newInstance().createParameter(props);        if ((map != null) && (map.getProperty("groundTruthType") != null)) {            parameter[3].setValue(map.getProperty("groundTruthType"));        } else {            parameter[3].setValue("Given");        }        // Parameter 4 - source link type        props.setProperty("Type", "java.lang.String");        props.setProperty("Name", "sourceType1");        props.setProperty("Class", "Basic");        props.setProperty("Structural", "true");        parameter[4] = DescriptorFactory.newInstance().createParameter(props);        if ((map != null) && (map.getProperty("sourceType1") != null)) {            parameter[4].setValue(map.getProperty("sourceType1"));        } else {            parameter[4].setValue("Interest");        }        // Parameter 5 - source link type 2        props.setProperty("Type", "java.lang.String");        props.setProperty("Name", "sourceType2");        props.setProperty("Class", "Basic");        props.setProperty("Structural", "true");        parameter[5] = DescriptorFactory.newInstance().createParameter(props);        if ((map != null) && (map.getProperty("sourceType2") != null)) {            parameter[5].setValue(map.getProperty("sourceType2"));        } else {            parameter[5].setValue("Music");        }        // Parameter 6 - adjustor property type        props.setProperty("Type", "java.lang.String");        props.setProperty("Name", "adjustorPropertyType");        props.setProperty("Class", "Basic");        props.setProperty("Structural", "true");        parameter[6] = DescriptorFactory.newInstance().createParameter(props);        if ((map != null) && (map.getProperty("adjustorPropertyType") != null)) {            parameter[6].setValue(map.getProperty("adjustorPropertyType"));        } else {            parameter[6].setValue("Knows Betweeness");        }        // Parameter 7 - evaluation boolean        props.setProperty("Type", "java.lang.Boolean");        props.setProperty("Name", "evaluation");        props.setProperty("Class", "Basic");        props.setProperty("Structural", "false");        parameter[7] = DescriptorFactory.newInstance().createParameter(props);        if ((map != null) && (map.getProperty("evaluation") != null)) {            parameter[7].setValue(Boolean.parseBoolean(map.getProperty("evaluation")));        } else {            parameter[7].setValue(new Boolean(true));        }        // Parameter 8 - sourceType1Weight        props.setProperty("Type", "java.lang.Double");        props.setProperty("Name", "sourceType1Weight");        props.setProperty("Class", "Basic");        props.setProperty("Structural", "false");        parameter[8] = DescriptorFactory.newInstance().createParameter(props);        if ((map != null) && (map.getProperty("sourceType1Weight") != null)) {            parameter[8].setValue(new Double(Double.parseDouble(map.getProperty("sourceType1Weight"))));        } else {            parameter[8].setValue(new Double(1.0));        }        // Parameter 9 - sourceType2Weight        props.setProperty("Type", "java.lang.Double");        props.setProperty("Name", "sourceType2Weight");        props.setProperty("Class", "Basic");        props.setProperty("Structural", "false");        parameter[9] = DescriptorFactory.newInstance().createParameter(props);        if ((map != null) && (map.getProperty("sourceType2Weight") != null)) {            parameter[9].setValue(new Double(Double.parseDouble(map.getProperty("sourceType2Weight"))));        } else {            parameter[9].setValue(new Double(1.0));        }        // init input 0        props.setProperty("Type", "Link");        props.setProperty("Relation", (String) parameter[3].getValue());        props.setProperty("AlgorithmName", (String) parameter[0].getValue());        props.remove("Property");        input[0] = DescriptorFactory.newInstance().createInputDescriptor(props);        // init input 1        props.setProperty("Type", "Link");        props.setProperty("Relation", (String) parameter[4].getValue());        props.setProperty("AlgorithmName", (String) parameter[0].getValue());        props.remove("Property");        input[1] = DescriptorFactory.newInstance().createInputDescriptor(props);        // init input 2        props.setProperty("Type", "Link");        props.setProperty("Relation", (String) parameter[3].getValue());        props.setProperty("AlgorithmName", (String) parameter[0].getValue());        props.remove("Property");        input[2] = DescriptorFactory.newInstance().createInputDescriptor(props);        // init input 3        props.setProperty("Type", "ActorProperty");        props.setProperty("Relation", (String) parameter[2].getValue());        props.setProperty("AlgorithmName", (String) parameter[0].getValue());        props.setProperty("Property", (String) parameter[5].getValue());        input[3] = DescriptorFactory.newInstance().createInputDescriptor(props);        // init output 0        props.setProperty("Type", "Link");        props.setProperty("Relation", (String) parameter[1].getValue());        props.setProperty("AlgorithmName", (String) parameter[0].getValue());        props.remove("Property");        output[0] = DescriptorFactory.newInstance().createOutputDescriptor(props);    }}