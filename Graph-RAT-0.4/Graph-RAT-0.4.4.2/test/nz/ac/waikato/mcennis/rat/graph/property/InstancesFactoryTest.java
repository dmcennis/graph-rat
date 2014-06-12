/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nz.ac.waikato.mcennis.rat.graph.property;

import java.util.Enumeration;
import junit.framework.TestCase;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instances;

/**
 *
 * @author dm75
 */
public class InstancesFactoryTest extends TestCase {
    
    Instances test;
    String string;
    
    public InstancesFactoryTest(String testName) {
        super(testName);
    }            

    @Override
    protected void setUp() throws Exception {
        FastVector attributes = new FastVector(4);

        attributes.addElement(new Attribute("Numeric 1"));

        FastVector nominal = new FastVector();
        nominal.addElement("Value 1");
        nominal.addElement("Value 2");
        attributes.addElement(new Attribute("Nominal 1",nominal));
        
        attributes.addElement(new Attribute("Numeric 2"));
        
        test = new Instances("Training",attributes,100);
        
        string = "Training,Numeric 1,Nominal 1:Value 1:Value 2,Numeric 2";
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of importFromString method, of class InstancesFactory.
     */
    public void testImportFromString() {
        System.out.println("importFromString");
        InstancesFactory instance = new InstancesFactory();
        Instances result = instance.importFromString(string,null);
        assertEquals(test.relationName(),result.relationName());
        Enumeration given = test.enumerateAttributes();
        Enumeration calculated = result.enumerateAttributes();
        int count =0;
        while(given.hasMoreElements() && calculated.hasMoreElements()){
            Attribute gAtt = (Attribute)given.nextElement();
            Attribute cAtt = (Attribute)calculated.nextElement();
            assertEquals(gAtt.type(),cAtt.type());
            assertEquals(gAtt.name(),cAtt.name());
            Enumeration gAttE = gAtt.enumerateValues();
            Enumeration cAttE = cAtt.enumerateValues();
            if(((gAttE == null)&&(cAttE != null))||((gAttE != null)&&(cAttE == null))){
                fail("Enumerations are not equal");
            }
            if(gAttE != null){   
                while(gAttE.hasMoreElements() && cAttE.hasMoreElements()){
                    assertEquals(gAttE.nextElement(),cAttE.nextElement());
                }
            }
            count++;
        }
    }

    /**
     * Test of exportToString method, of class InstancesFactory.
     */
    public void testExportToString() {
        System.out.println("exportToString");
        InstancesFactory instance = new InstancesFactory();
        String result = instance.exportToString(test,null);
        assertEquals(string, result);
    }

}
