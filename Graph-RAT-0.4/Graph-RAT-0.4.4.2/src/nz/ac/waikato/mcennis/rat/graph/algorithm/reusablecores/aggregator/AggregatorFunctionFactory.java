/**
 * Jul 23, 2008-5:42:31 PM
 * Copyright Daniel McEnnis, see license.txt
 */

package nz.ac.waikato.mcennis.rat.graph.algorithm.reusablecores.aggregator;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Daniel McEnnis
 */
public class AggregatorFunctionFactory {
    static AggregatorFunctionFactory instance = null;
    
    public static AggregatorFunctionFactory newInstance(){
        if(instance == null){
            instance = new AggregatorFunctionFactory();
        }
        return instance;
    }
    
    private void AggregatorFunctionFactory(){
    }
    
    public AggregatorFunction create(Properties props){
        try {
            if (props == null) {
                return null;
            }
            if (props.getProperty("AggregatorFunction") == null) {
                return null;
            }
            return (AggregatorFunction) Class.forName(props.getProperty("AggregatorFunction")).newInstance();
        } catch (InstantiationException ex) {
            Logger.getLogger(AggregatorFunctionFactory.class.getName()).log(Level.SEVERE, "Bad AggregatorFunction type "+props.getProperty("AggregatorFunction"), ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(AggregatorFunctionFactory.class.getName()).log(Level.SEVERE, "Bad AggregatorFunction type "+props.getProperty("AggregatorFunction"), ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AggregatorFunctionFactory.class.getName()).log(Level.SEVERE, "Bad AggregatorFunction type "+props.getProperty("AggregatorFunction"), ex);
        }
            return null;

    }
}
