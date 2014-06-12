/*
 * Created 21/05/2008-16:54:01
 * Copyright Daniel McEnnis, see license.txt
 */

package nz.ac.waikato.mcennis.rat.graph.algorithm.reusablecores.distance;

import java.util.Properties;

/**
 * Class for creating a distance function.  The Functions are created by 
 * creating new static DistanceFactory and then submitting a java.util.Properties
 * object where the 'DistanceFunction' parameter is the simple name of the desired
 * distance function's simple class name. 
 * 
 * @author Daniel McEnnis
 */
public class DistanceFactory {
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
    public DistanceFunction create(Properties props){
        if((props != null)&&(props.getProperty("DistanceFunction")!=null)){
            String name = props.getProperty("DistanceFunction");
            if(name.contentEquals(CosineDistance.class.getSimpleName())){
                return new CosineDistance();
            }else if(name.contentEquals(JaccardDistance.class.getSimpleName())){
                return new JaccardDistance();
            }else if(name.contentEquals(PearsonDistance.class.getSimpleName())){
                return new PearsonDistance();
            }else if(name.contentEquals(WeightedKLDDistance.class.getSimpleName())){
                return new WeightedKLDDistance();
            }else if(name.contentEquals(EuclideanDistance.class.getSimpleName())){
                return new EuclideanDistance();
            }else if(name.contentEquals(ChebyshevDistance.class.getSimpleName())){
                return new WeightedKLDDistance();
            }else if(name.contentEquals(DotProductDistance.class.getSimpleName())){
                return new DotProductDistance();
            }else if(name.contentEquals(ManhattanDistance.class.getSimpleName())){
                return new ManhattanDistance();
            }else{
                return null;
            }
        }else{
            return null;
        }
    }
}
