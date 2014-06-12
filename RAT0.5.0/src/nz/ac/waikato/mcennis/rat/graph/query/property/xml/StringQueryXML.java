/**
 * StringQueryXML
 * Created Jan 12, 2009 - 9:17:08 PM
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.graph.query.property.xml;

import java.util.logging.Level;
import java.util.logging.Logger;
import nz.ac.waikato.mcennis.rat.graph.query.property.PropertyQuery;
import nz.ac.waikato.mcennis.rat.graph.query.property.StringQuery;
import nz.ac.waikato.mcennis.rat.XMLParserObject.State;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author Daniel McEnnis
 */
public class StringQueryXML implements PropertyQueryXML{

    enum InternalState {START, OPERATION, NOT, VALUE};
    
    StringQuery stringQuery = new StringQuery();
    
    boolean not;    
    
    String value = "";
    
    InternalState state = InternalState.START;
    
    StringQuery.Operation op = StringQuery.Operation.EQUALS;

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if(localName.equalsIgnoreCase("StringQuery")&&qName.equalsIgnoreCase("NumericQuery")){
            
        }else if(localName.equalsIgnoreCase("Operation")&&qName.equalsIgnoreCase("Operation")){
            state = InternalState.OPERATION;
        }else if(localName.equalsIgnoreCase("Not")&&qName.equalsIgnoreCase("Not")){
            state = InternalState.NOT;
        }else if(localName.equalsIgnoreCase("Value")&&qName.equalsIgnoreCase("Value")){
            state = InternalState.VALUE;
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
            String contents = new String(ch,start,length);
        if(state == InternalState.OPERATION){
            if(contents.equalsIgnoreCase("EQUALS")){
                op = StringQuery.Operation.EQUALS;
            }else if(contents.equalsIgnoreCase("EQUALS_IGNORE_CASE")){
                op = StringQuery.Operation.EQUALS_IGNORE_CASE;
            }else if(contents.equalsIgnoreCase("CONTAINS")){
                op = StringQuery.Operation.CONTAINS;
            }else if(contents.equalsIgnoreCase("ENDS_WITH")){
                op = StringQuery.Operation.ENDS_WITH;
            }else if(contents.equalsIgnoreCase("MATCHES")){
                op = StringQuery.Operation.MATCHES;
            }else if(contents.equalsIgnoreCase("STARTS_WITH")){
                op = StringQuery.Operation.STARTS_WITH;
            }else{
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE,"Unknown operation '"+contents+"' - assuming Equals");
            }
        }else if(state == InternalState.VALUE){
            value = contents;
        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        switch (state){
            case START:
                stringQuery.buildQuery(value, not, op);
                break;
            case NOT:
                state = InternalState.START;
                break;
            case OPERATION:
                state = InternalState.START;
                break;
            case VALUE:
                state = InternalState.START;
                break;                
        }
    }

    public State buildingStatus() {
        return stringQuery.buildingStatus();
    }

    public StringQuery getQuery() {
        return stringQuery;
    }

    public StringQueryXML newCopy() {
        return new StringQueryXML();
    }
}
