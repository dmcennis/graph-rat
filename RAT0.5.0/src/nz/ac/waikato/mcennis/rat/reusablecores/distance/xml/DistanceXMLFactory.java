/**
 * DistanceXMLFactory
 * Created Jan 17, 2009 - 8:33:28 PM
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.reusablecores.distance.xml;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import nz.ac.waikato.mcennis.rat.AbstractFactory;
import nz.ac.waikato.mcennis.rat.graph.descriptors.Parameter;
import nz.ac.waikato.mcennis.rat.graph.descriptors.ParameterFactory;
import nz.ac.waikato.mcennis.rat.graph.descriptors.ParameterInternal;
import nz.ac.waikato.mcennis.rat.graph.descriptors.Properties;
import nz.ac.waikato.mcennis.rat.graph.descriptors.SyntaxCheckerFactory;
import nz.ac.waikato.mcennis.rat.graph.descriptors.SyntaxObject;

/**
 *
 * @author Daniel McEnnis
 */
public class DistanceXMLFactory extends AbstractFactory<DistanceXML>{
    static DistanceXMLFactory instance = null;
    
    static public DistanceXMLFactory newInstance(){
        if(instance == null){
            instance = new DistanceXMLFactory();
        }
        return instance;
    }
    
    DistanceXMLFactory(){
        ParameterInternal name = ParameterFactory.newInstance().create("DistanceXMLClass", String.class);
        SyntaxObject syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("CosineDistance");
        properties.add(name);
        
        
        map.put("ChebyshevDistance",new ChebyshevDistanceXML());
        map.put("CosineDistance",new CosineDistanceXML());
        map.put("DotProductDistance",new DotProductDistanceXML());
        map.put("EuclideanDistance",new EuclideanDistanceXML());
        map.put("JaccardDistance",new JaccardDistanceXML());
        map.put("ManhattanDistance",new ManhattanDistanceXML());
        map.put("PearsonDistance",new PearsonDistanceXML());
        map.put("WeightedKLDDistance", new WeightedKLDDistanceXML());
    }
    
    @Override
    public DistanceXML create(Properties props) {
        return create(null,props);
    }
    
    public DistanceXML create(String name){
        return create(name,properties);
    }
    
    public DistanceXML create(String name, Properties parameters){
        if(name == null){
        if ((parameters.get("DistanceXMLClass") != null) && (parameters.get("DistanceXMLClass").getParameterClass().getName().contentEquals(String.class.getName()))) {
            name = (String) parameters.get("DistanceXMLClass").getValue().iterator().next();
        } else {
            name = (String) properties.get("DistanceXMLClass").getValue().iterator().next();
        }
        }

        if(map.containsKey(name)){
            return (DistanceXML)map.get(name).newCopy();
        }else{
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE,"Distance class '"+name+"' not found - assuming CosineDistanceXML");
            return new CosineDistanceXML();
        }
    }
    
    public Parameter getClassParameter(){
        return properties.get("DistanceXMLClass");
    }
}
