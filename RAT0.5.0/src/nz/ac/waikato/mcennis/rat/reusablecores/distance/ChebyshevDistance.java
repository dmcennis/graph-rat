/*

 * Created 23/05/2008-15:36:29

 * Copyright Daniel McEnnis, see license.txt

 */



package nz.ac.waikato.mcennis.rat.reusablecores.distance;



import java.util.HashSet;

import nz.ac.waikato.mcennis.rat.reusablecores.datavector.DataVector;



/**

 * Distance measure as defined in Wikipedia

 * @author Weka Toolkit

 */

public class ChebyshevDistance implements DistanceFunction{



    @Override

    public double distance(DataVector inst1, DataVector inst2) {

        double ret = 0.0;

//        HashSet<Object> seen = new HashSet<Object>();

        inst1.reset();

        while(inst1.hasNext()){

            inst1.next();

            if(Math.abs(inst1.getCurrentValue()-inst2.getValue(inst1.getCurrentIndex()))>ret ){

                ret = Math.abs(inst1.getCurrentValue()-inst2.getValue(inst1.getCurrentIndex()));

            }

        }

        inst2.reset();

        while(inst2.hasNext()){

            inst2.next();

            if(Math.abs(inst2.getCurrentValue()-inst1.getValue(inst2.getCurrentIndex()))>ret){

                ret = Math.abs(inst2.getCurrentValue()-inst1.getValue(inst2.getCurrentIndex()));

            }

        }

        return ret;

    }



    public ChebyshevDistance prototype(){

        return new ChebyshevDistance();

    }

}

