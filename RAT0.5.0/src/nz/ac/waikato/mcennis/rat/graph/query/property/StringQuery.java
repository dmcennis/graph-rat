/**
 * StringQuery
 * Created Jan 5, 2009 - 11:47:19 PM
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.graph.query.property;

import java.io.Reader;
import java.io.Writer;
import java.util.Collection;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import nz.ac.waikato.mcennis.rat.graph.property.Property;
import nz.ac.waikato.mcennis.rat.XMLParserObject.State;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author Daniel McEnnis
 */
public class StringQuery implements PropertyQuery{

    public enum Operation {EQUALS, EQUALS_IGNORE_CASE, CONTAINS, ENDS_WITH, MATCHES, STARTS_WITH

    };
    
    Operation operation = Operation.EQUALS;
    
    String comparison = "";
    
    boolean not = false;
    
    transient State state = State.UNINITIALIZED;
    
    public boolean execute(Property property) {
        boolean result = false;
        
        if(property.getPropertyClass().getName().contentEquals("java.lang.String")){
            Collection values = property.getValue();
            if(values.size() >0){
                switch(operation){
                    case EQUALS:
                        result = ((String)values.iterator().next()).equals(comparison);
                        break;
                    case EQUALS_IGNORE_CASE:
                        result = ((String)values.iterator().next()).equalsIgnoreCase(comparison);
                        break;
                    case CONTAINS:
                        result = ((String)values.iterator().next()).contains(comparison);
                        break;
                    case ENDS_WITH:
                        result = ((String)values.iterator().next()).endsWith(comparison);
                        break;
                    case MATCHES:
                        result = ((String)values.iterator().next()).matches(comparison);
                        break;
                    case STARTS_WITH:
                        result = ((String)values.iterator().next()).startsWith(comparison);
                        break;
                }        
                if(not){
                    return !result;
                }else{
                    return result;
                }
            }
        }
        
        return result;
    }

    public void exportQuery(Writer writer) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void importQuery(Reader reader) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void buildQuery(String comparison, boolean not, Operation operation) {
        state = State.LOADING;
        if(comparison==null){
            this.comparison="";
        }else{
            this.comparison=comparison;
        }
        this.not = not;
        if(operation == null){
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Operation is null, assuming Equals");
            this.operation = Operation.EQUALS;
        }else{
            this.operation = operation;
        }
        state = State.READY;
    }

    public int compareTo(Object o) {
        if(o.getClass().getName().contentEquals(this.getClass().getName())){
            StringQuery right = (StringQuery)o;
            if(this.operation.compareTo(right.operation) != 0){
                return operation.compareTo(right.operation);
            }
            if(comparison.compareTo(right.comparison) != 0){
                return  comparison.compareTo(right.comparison);
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

    public StringQuery prototype() {
        return new StringQuery();
    }
}
