/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.mcennis.graphrat.dataAquisition;

import java.util.Properties;
import junit.framework.TestCase;
import org.mcennis.graphrat.graph.MemGraph;
import org.dynamicfactory.descriptors.InputDescriptor;
import org.dynamicfactory.descriptors.OutputDescriptor;
import org.dynamicfactory.descriptors.SettableParameter;

/**
 *
 * @author dm75
 */
public class ReadAudioFilesTest extends TestCase {
    
    MemGraph base;
    
    public ReadAudioFilesTest(String testName) {
        super(testName);
        base = new MemGraph();
    }            

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of start method, of class ReadAudioFiles.
     */
    public void testStart() {
        System.out.println("start");
        Properties properties = new Properties();
        properties.setProperty("audioDirectoryLocation", "/research/Music/Enya/A Day Without Rain");
        properties.setProperty("settingsFile", "/research/NetBeansProjects/RAT0.4.3/jAudioBatchFile.xml");
        properties.setProperty("featuresFile", "/research/NetBeansProjects/RAT0.4.3/features.xml");
        ReadAudioFiles instance = new ReadAudioFiles();
        instance.init(properties);
        instance.set(base);
        instance.start();
        
    }


}
