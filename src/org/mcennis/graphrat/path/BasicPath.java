/*

 * BasicPath.java

 *

 * Created on 2 July 2007, 15:05

 *

 * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt)

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


package org.mcennis.graphrat.path;



import org.mcennis.graphrat.actor.Actor;

import org.dynamicfactory.model.ModelShell;



/**

 *


 * 

 * Class for a basic Path implementation

 * @author Daniel McEnnis
 */

public class BasicPath extends ModelShell implements Path{

    

    String type;

    

    java.util.HashSet<String> members = null;

    

    java.util.Vector<Actor> path = null;

    

    boolean weak = false;

    

    double cost = Double.NaN;

    

    /** Creates a new instance of BasicPath */

    public BasicPath() {

    }

    

    @Override

    public void setPath(Actor[] participants, double cost,String type) {

        members = new java.util.HashSet<String>();

        path = new java.util.Vector<Actor>();

        for(int i=0;i<participants.length;++i){

            members.add(participants[i].getID());

            path.add(participants[i]);

        }

        this.cost = cost;

        this.type = type;

        this.fireChange(SET_PATH,0);

    }

    

    @Override

    public void setWeak(boolean weak){

        this.weak = weak;
        int value= -1;
        if(weak){
            value = 1;
        }
        this.fireChange(WEAK,value);

    }

    

    @Override

    public Actor[] getPath() throws NotConstructedError{

        if(members==null){

            throw new NotConstructedError("BasicPath");

        }else{

            return path.toArray(new Actor[]{});

        }

    }

    

    @Override

    public Actor getStart()  throws NotConstructedError{

        if(members==null){

            throw new NotConstructedError("BasicPath");

        }else{

            return path.get(0);

        }

    }

    

    @Override

    public Actor getEnd()  throws NotConstructedError{

        if(members==null){

            throw new NotConstructedError("BasicPath");

        }else{

            return path.get(path.size()-1);

        }

    }

    

    @Override

    public Actor[] getMiddle()  throws NotConstructedError{

        if(members==null){

            throw new NotConstructedError("BasicPath");

        }else{

            java.util.Vector<Actor> base = new java.util.Vector<Actor>();

            base.addAll(path);

            base.remove(base.size()-1);

            base.remove(0);

            return base.toArray(new Actor[]{});           

        }

    }

    

    @Override

    public double getCost()  throws NotConstructedError{

        if(members==null){

            throw new NotConstructedError("BasicPath");

        }else{

            return cost;

        }

    }

    

    @Override

    public boolean isWeak()  throws NotConstructedError{

        if(members==null){

            throw new NotConstructedError("BasicPath");

        }else{

            return weak;

        }

    }

    

    @Override

    public boolean contains(Actor node)  throws NotConstructedError{

        if(members==null){

            throw new NotConstructedError("BasicPath");

        }else{

            return members.contains(node.getID());

        }

    }

    

    @Override

    public String getType(){

        return type;

    }



    @Override

    public Path addActor(Actor a,double cost) throws NotConstructedError{

                                java.util.Properties props = new java.util.Properties();

                props.setProperty("PathType","Basic");

                props.setProperty("PathID",type);

        Path ret = PathFactory.newInstance().create(props);

        Actor[] path = this.getPath();

        Actor[] retActor = new Actor[path.length+1];

        for(int i=0;i<path.length;++i){

            retActor[i] = path[i];

        }

        retActor[retActor.length-1] = a;

        ret.setPath(retActor,this.cost+cost,type);

        this.fireChange(ADD_ACTOR,0);

        return ret;

    }

}

