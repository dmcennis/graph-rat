/**
 * XMLParserObject
 * Created Jan 16, 2009 - 11:57:13 PM
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author Daniel McEnnis
 */
public interface XMLParserObject {

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException;

    public void characters(char[] ch, int start, int length) throws SAXException;

    public void endElement(String uri, String localName, String qName) throws SAXException;    
    
    public State buildingStatus();
    
    public enum State {UNINITIALIZED, LOADING, READY};
    
    public XMLParserObject newCopy();

}
