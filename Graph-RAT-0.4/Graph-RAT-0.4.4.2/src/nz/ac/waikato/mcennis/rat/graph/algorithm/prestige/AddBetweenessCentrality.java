/* * AddBetweenessCentrality.java * * Created on 10 July 2007, 17:35 * * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt) */package nz.ac.waikato.mcennis.rat.graph.algorithm.prestige;import nz.ac.waikato.mcennis.rat.graph.algorithm.prestige.AddBasicBetweenessCentrality;import nz.ac.waikato.mcennis.rat.graph.path.Path;import nz.ac.waikato.mcennis.rat.graph.path.PathSet;import nz.ac.waikato.mcennis.rat.graph.actor.Actor;/** * *  * Class that provides a betweeness metric that matches Freeman79. * @author Daniel McEnnis */public class AddBetweenessCentrality extends AddBasicBetweenessCentrality{        /** Creates a new instance of AddBetweenessCentrality */    public AddBetweenessCentrality() {        super();    }        /**     * Claculates betweeness with summing of multiple geodesic paths with the same     * start and end actors as specified in Freeman79      *      * @see nz.ac.waikato.mcennis.rat.grapoh.algorithm.AddBasicBetweenessCentrality     */    protected void sumPath(int i,int j, PathSet pathSet){        Path[] paths = pathSet.getPath(userList[i].getID(),userList[j].getID());        if(paths != null){            for(int k=0;k<paths.length;++k){                Actor[] actorList = paths[k].getMiddle();                for(int l=0;l<actorList.length;++l){                    betweeness[map.get(actorList[l])]+= 1.0/((double)paths.length);                }            }        }    }}