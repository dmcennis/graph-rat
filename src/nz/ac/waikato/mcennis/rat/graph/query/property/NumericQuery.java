/**
 * NumericQuery
 * Created Jan 5, 2009 - 11:23:14 PM
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.graph.query.property;

import java.io.Reader;
import java.io.Writer;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import nz.ac.waikato.mcennis.rat.XMLParserObject.State;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author Daniel McEnnis
 */
public class NumericQuery implements PropertyQuery{
    
    public enum Operation {EQ,GT,LT,GTE,LTE,NE};
    
    boolean not = false;
    
    Operation operation = Operation.GT;
    
    double comparisonValue = 0.0;
    
    transient State state = State.UNINITIALIZED;
    
    public boolean execute(Property property) {
        String name = property.getPropertyClass().getName();
        if(name.contentEquals("java.lang.Double") || 
                name.contentEquals("java.lang.Float")||
                name.contentEquals("java.lang.Long")||
                name.contentEquals("java.lang.Integer")||
                name.contentEquals("java.lang.Short")){
            Collection value = property.getValue();
            if(value.size() > 0){
                double left = ((Number) value.iterator().next()).doubleValue();
                return execute(left);
            }else{
                Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Property "+property.getType()+" has no values");
                return false;
            }
        }else{
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Property "+property.getType()+" is of type "+property.getPropertyClass().getName()+" where a numeric property is expected");
            return false;
        }
    
    }
    
    boolean execute(double left){
                        boolean result = false;
                switch (operation){
                    case EQ:
                        result = (left == comparisonValue);
                        break;
                    case GT:
                        result = (left > comparisonValue);
                        break;
                    case LT:
                        result = (left < comparisonValue);
                        break;
                    case GTE:
                        result = (left >= comparisonValue);
                        break;
                    case LTE:
                        result = (left <= comparisonValue);
                        break; 
                    case NE:
                        result = (left != comparisonValue);
                        break;
                }
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

    public void buildQuery(double comparisonValue, boolean not, Operation operation) {
        state = State.LOADING;
        this.comparisonValue = comparisonValue;
        this.not = not;
        if(operation==null){
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "null value instead of an operation, GreaterThan assumed");
            this.operation = Operation.GT;
        }else{
            this.operation = operation;
        }
        state = State.READY;
    }

    public int compareTo(Object o) {
        if(o.getClass().getName().contentEquals(this.getClass().getName())){
            NumericQuery right = (NumericQuery)o;
            if(!operation.equals(right.operation)){
                return operation.compareTo(right.operation);
            }
            if(comparisonValue - right.comparisonValue != 0.0){
                return (int) comparisonValue;
            }
            if(! not && right.not){
                return -1;
            }
            if(not && right.not){
                return 1;
            }
            return 0;
        }else{
            return this.getClass().getName().compareTo(o.getClass().getName());
        }
       
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public State buildingStatus() {
        return state;
    }

    public NumericQuery prototype() {
        return new NumericQuery();
    }
}
