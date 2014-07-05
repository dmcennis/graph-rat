/*

 * Created 21/05/2008-16:54:01

 * Copyright Daniel McEnnis, see license.txt

 */



package nz.ac.waikato.mcennis.rat.reusablecores.distance;



import java.util.logging.Level;

import java.util.logging.Logger;

import nz.ac.waikato.mcennis.rat.AbstractFactory;


/**

 * Class for creating a distance function.  The Functions are created by 

 * creating new static DistanceFactory and then submitting a java.util.Properties

 * object where the 'DistanceFunction' parameter is the simple name of the desired

 * distance function's simple class name. 

 * 

 * @author Daniel McEnnis

 */

public class DistanceFactory extends AbstractFactory<DistanceFunction>{

    static DistanceFactory instance = null;

    

    /**

     * Return a singleton DistanceFactory, creating it if necessary.

     * @return DistanceFactory

     */

    public static DistanceFactory newInstance(){

        if(instance == null){

            instance = new DistanceFactory();

        }

        return instance;

    }

    

    private DistanceFactory(){

        ParameterInternal name = ParameterFactory.newInstance().create("DistanceClass", String.class);

        SyntaxObject syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);

        name.setRestrictions(syntax);

        name.add("CosineDistance");

        properties.add(name);

        

        map.put("ChebyshevDistance", new ChebyshevDistance());

        map.put("CosineDistance", new CosineDistance());

        map.put("DotProductDistance", new DotProductDistance());

        map.put("EuclideanDistance",new EuclideanDistance());

        map.put("ExponentialDistance",new ExponentialDistance());

        map.put("JaccardDistance",new JaccardDistance());

        map.put("ManhattanDistance",new ManhattanDistance());

        map.put("PearsonDistance",new PearsonDistance());

        map.put("WeightedKLDDistance",new WeightedKLDDistance());

    }

    

    /**

     * Create a distance function using the 'DistanceFunction' parameter.  Each 

     * distance function can be modified using class-specific parameters. Returns

     * null if no distance function is found of the given name.  Returns null if

     * the property object either doesn't contain a 'DistanceFunction' property

     * or is null.

     * @param props map of parameters for creating a distance function

     * @return newly created distance function

     */

    @Override

    public DistanceFunction create(Properties props) {

        return create(null,props);

    }

    

    public DistanceFunction create(String distance){

        return create(distance,properties);

    }

    

    public DistanceFunction create(String name, Properties parameters){

        if(name == null){

        if ((parameters.get("DistanceClass") != null) && (parameters.get("DistanceClass").getParameterClass().getName().contentEquals(String.class.getName()))) {

            name = (String) parameters.get("DistanceClass").getValue().iterator().next();

        } else {

            name = (String) properties.get("DistanceClass").getValue().iterator().next();

        }

        }



        if(map.containsKey(name)){

            return map.get(name).prototype();

        }else{

            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE,"Distance class '"+name+"' not found - assuming CosineDistance");

            return new CosineDistance();

        }

    }

    public Parameter getClassParameter(){

        return properties.get("DistanceClass");

    }

}

