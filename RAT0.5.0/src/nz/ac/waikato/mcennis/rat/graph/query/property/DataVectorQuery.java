/**
 * DataVectorQuery
 * Created Jan 5, 2009 - 11:47:45 PM
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.graph.query.property;

import java.io.Reader;
import java.io.Writer;
import java.util.LinkedList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import nz.ac.waikato.mcennis.rat.reusablecores.InstanceManipulation;
import nz.ac.waikato.mcennis.rat.graph.property.Property;
import nz.ac.waikato.mcennis.rat.reusablecores.datavector.DataVector;
import nz.ac.waikato.mcennis.rat.reusablecores.datavector.DoubleArrayDataVector;
import nz.ac.waikato.mcennis.rat.reusablecores.datavector.InstanceDataVector;
import nz.ac.waikato.mcennis.rat.reusablecores.distance.CosineDistance;
import nz.ac.waikato.mcennis.rat.reusablecores.distance.DistanceFunction;
import nz.ac.waikato.mcennis.rat.XMLParserObject.State;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import weka.core.Instance;

/**
 *
 * @author Daniel McEnnis
 */
public class DataVectorQuery implements PropertyQuery{

    boolean not = false;
    
    DataVector right;
    
    DistanceFunction comparison;
    
    NumericQuery numericQuery;
    
    transient State state = State.UNINITIALIZED;
    
    public boolean execute(Property property) {
        DataVector left = null;
        //TODO: make a factory call instead of a reflection call
        if(property.getPropertyClass().isInstance("nz.ac.waikato.mcennis.rat.graph.algorithm.reusablecores.datavector.Datavector")){
            if(property.getValue().size() > 0){
                left = (DataVector)property.getValue().iterator().next();
            }
        }else{
            LinkedList<Instance> instance = InstanceManipulation.propertyToInstance(property);
            if(instance.size() > 0){
                left = new InstanceDataVector(instance.getFirst());
            }
        }
        if(left == null){
            return not;
        }
        double value = comparison.distance(left, right);
        
        boolean result = numericQuery.execute(value);
        
        if(not){
            return !result;
        }else{
            return result;
        }
    }

    public void exportQuery(Writer writer) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void importQuery(Reader reader) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void buildQuery(DistanceFunction comparison, boolean not, NumericQuery numericQuery, DataVector right) {
        state = State.LOADING;
        if(comparison == null){
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "comparison function is null, using Cosine as default");
            comparison = new CosineDistance();
        }else{
            this.comparison = comparison;
        }
        this.not = not;
        if(numericQuery == null){
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "numeric query is null, using default values");
            numericQuery = new NumericQuery();
        }else{
            this.numericQuery = numericQuery;
        }
        if(right == null){
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "comparison vector is null, using a zero-dimensional vector");
            right = new DoubleArrayDataVector(new double[]{});
        }else{
            this.right = right;
        }
        state = State.READY;
    }

    public int compareTo(Object o) {
        if(o.getClass().getName().contentEquals(this.getClass().getName())){
            DataVectorQuery r = (DataVectorQuery)o;
            if(this.comparison.getClass().getName().compareTo(r.comparison.getClass().getName())!=0){
                return this.comparison.getClass().getName().compareTo(r.comparison.getClass().getName());
            }
            int val = numericQuery.compareTo(r.numericQuery);
            if(val != 0){
                return val;
            }
            if(! not && r.not){
                return -1;
            }
            if(not && r.not){
                return 1;
            }
            return right.compareTo(r);
        }else{
            return this.getClass().getName().compareTo(o.getClass().getName());
        }
    }

    public State buildingStatus() {
        return state;
    }

    public DataVectorQuery prototype() {
        return new DataVectorQuery();
    }
}
