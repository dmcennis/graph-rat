/**
 * DataVectorXML
 * Created Jan 16, 2009 - 11:20:17 PM
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.reusablecores.datavector.xml;

import nz.ac.waikato.mcennis.rat.reusablecores.datavector.DataVector;
import nz.ac.waikato.mcennis.rat.XMLParserObject;

/**
 *
 * @author Daniel McEnnis
 */
public interface DataVectorXML extends XMLParserObject{

    public DataVector getDataVector();

}
