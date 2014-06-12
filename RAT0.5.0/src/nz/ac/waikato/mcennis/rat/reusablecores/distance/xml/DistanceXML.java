/**
 * DistanceXML
 * Created Jan 17, 2009 - 8:34:18 PM
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.reusablecores.distance.xml;

import nz.ac.waikato.mcennis.rat.reusablecores.distance.DistanceFunction;
import nz.ac.waikato.mcennis.rat.XMLParserObject;
import java.io.Writer;
import java.io.IOException;

/**
 *
 * @author Daniel McEnnis
 */
public interface DistanceXML extends XMLParserObject{

    public DistanceFunction getDistanceMeasure();
    
    public void exportAsXML(Writer writer) throws IOException;
}