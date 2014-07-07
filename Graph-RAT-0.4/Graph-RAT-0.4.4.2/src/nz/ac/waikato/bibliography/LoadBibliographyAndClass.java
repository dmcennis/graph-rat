/*
 * LoadBibliographyXML.java
 * 
 * Created on 8/01/2008, 14:04:48
 * Copyright Daniel McEnnis, see license.txt
 */

package nz.ac.waikato.bibliography;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import nz.ac.waikato.mcennis.rat.crawler.FileListCrawler;
import nz.ac.waikato.mcennis.rat.dataAquisition.DataAquisition;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import org.dynamicfactory.descriptors.DescriptorFactory;
import org.dynamicfactory.descriptors.InputDescriptor;
import org.dynamicfactory.descriptors.OutputDescriptor;
import org.dynamicfactory.descriptors.SettableParameter;
import org.dynamicfactory.model.ModelShell;
import nz.ac.waikato.mcennis.rat.parser.Parser;
import nz.ac.waikato.mcennis.rat.parser.ParserFactory;
import nz.ac.waikato.mcennis.rat.scheduler.Scheduler;

/**
 * Loads two XML files describing a set of papers and their authors.  See ParseBibliographyXML 
 * for the structure of the base XML file and ParseClassLabel for the structure 
 * of the ground truth XML file.<br/>
 * <br/>
 * Once the two files are read into the graph, the graph will contain two actor 
 * types representing papers and the authors that write them.  See init() for a 
 * detailed description of the constructed graph.<br/>
 * 
 * @author Daniel McEnnis
 */
public class LoadBibliographyAndClass extends ModelShell implements DataAquisition {
    
    Graph graph = null;

    ParameterInternal[] parameter = new ParameterInternal[8];
    
    InputDescriptor[] input = new InputDescriptor[0];
    
    OutputDescriptor[] output = new OutputDescriptor[12];
    
    public LoadBibliographyAndClass() {
        init(null);
    }

    @Override
    public void start() {
        java.util.Properties props = new java.util.Properties();
        props.setProperty("ParserType","BibliographyXML");
        props.setProperty("authorMode",(String)parameter[2].getValue());
        props.setProperty("paperMode",(String)parameter[3].getValue());
        props.setProperty("wroteRelation",(String)parameter[4].getValue());
        props.setProperty("referencesRelation",(String)parameter[5].getValue());
        props.setProperty("bidirectionalReferences",(String)parameter[6].getValue());
        props.setProperty("addClusters","false");
        Parser[] parser = new Parser[]{ParserFactory.newInstance().create(props)};
        parser[0].set(graph);
        fireChange(Scheduler.SET_ALGORITHM_COUNT,2);
        FileListCrawler crawler  = new FileListCrawler();
        crawler.set(parser);
        Logger.getLogger(LoadBibliographyAndClass.class.getName()).log(Level.INFO,"Analyzing "+(String)parameter[1].getValue());
        crawler.crawl((String)parameter[1].getValue());
        graph = ((Graph)crawler.getParser()[0].get());
        fireChange(Scheduler.SET_ALGORITHM_PROGRESS,1);
        props.setProperty("ParserType","ParseClassLabel");
        parser[0] = ParserFactory.newInstance().create(props);
        parser[0].set(graph);
        Logger.getLogger(LoadBibliographyAndClass.class.getName()).log(Level.INFO,"Analyzing "+(String)parameter[6].getValue());
        crawler.set(parser);
        crawler.crawl((String)parameter[6].getValue());
        graph = ((Graph)crawler.getParser()[0].get());
        fireChange(Scheduler.SET_ALGORITHM_PROGRESS,2);
    }

    @Override
    public void set(Graph g) {
        graph = g;
    }

    @Override
    public Graph get() {
        return graph;
    }

    @Override
    public void cancel() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public InputDescriptor[] getInputType() {
        return new InputDescriptor[]{};
    }
    
    @Override
    public OutputDescriptor[] getOutputType() {
        return output;
    }
    
    @Override
    public Parameter[] getParameter() {
        return parameter;
    }
    
    @Override
    public Parameter getParameter(String param) {
        for(int i=0;i<parameter.length;++i){
            if(parameter[i].getName().contentEquals(param)){
                return parameter[i];
            }
        }
        return null;
    }
    
    @Override
    public SettableParameter[] getSettableParameter() {
        return null;
    }
    
    @Override
    public SettableParameter getSettableParameter(String param) {
        return null;
    }

    /**
     * Parameters for this object:
     * <ul>
     * <li/><b>name</b>: Name of this algorithm.  Default is 'Bibliography and Class XML Reader'
     * <li/><b>fileLocation</b>: file name of the base bibliography XML file. Default
     *  is '/tmp/bibliography.xml'
     * <li/><b>actorMode</b>: actor type of authors. Default is 'Author'
     * <li/><b>paperMode</b>: actor type of papers. Default is 'Paper'
     * <li/><b>wroteRelation</b>: link relation between authors and papers. Default 
     * is 'Authored'
     * <li/><b>referencesRelation</b>: relation for citations between papers. Deafult
     * is 'Authored'
     * <li/><b>classLabelFile</b>: file name of the ground truth XML file. Default
     * is '/tmp/idLabel.xml'
     * <li/><b>bidirectionalReferences</b>: boolean determining the direction of the 
     * links between papers. Default is 'false'
     * </ul><br/>
     * @param map map of all parameters for this object
     */
    public void init(Properties map) {
        Properties props = new Properties();
        props.setProperty("Type","java.lang.String");
        props.setProperty("Name","name");
        props.setProperty("Class","Basic");
        props.setProperty("Structural","true");
        parameter[0] = DescriptorFactory.newInstance().createParameter(props);
        if((map!=null)&&(map.getProperty("name") != null)){
            parameter[0].setValue(map.getProperty("name"));
        }else{
            parameter[0].setValue("Bibliography and Class XML Reader");
        }

        props.setProperty("Type","java.lang.String");
        props.setProperty("Name","fileLocation");
        props.setProperty("Class","Basic");
        props.setProperty("Structural","true");
        parameter[1] = DescriptorFactory.newInstance().createParameter(props);
        if((map!=null)&&(map.getProperty("fileLocation") != null)){
            parameter[1].setValue(map.getProperty("fileLocation"));
        }else{
            parameter[1].setValue("/tmp/bibliography.xml");
        }

        props.setProperty("Type","java.lang.String");
        props.setProperty("Name","actorMode");
        props.setProperty("Class","Basic");
        props.setProperty("Structural","true");
        parameter[2] = DescriptorFactory.newInstance().createParameter(props);
        if((map!=null)&&(map.getProperty("actorMode") != null)){
            parameter[2].setValue(map.getProperty("actorMode"));
        }else{
            parameter[2].setValue("Author");
        }

        props.setProperty("Type","java.lang.String");
        props.setProperty("Name","paperMode");
        props.setProperty("Class","Basic");
        props.setProperty("Structural","true");
        parameter[3] = DescriptorFactory.newInstance().createParameter(props);
        if((map!=null)&&(map.getProperty("paperMode") != null)){
            parameter[3].setValue(map.getProperty("paperMode"));
        }else{
            parameter[3].setValue("Paper");
        }
        
        props.setProperty("Type","java.lang.String");
        props.setProperty("Name","wroteRelation");
        props.setProperty("Class","Basic");
        props.setProperty("Structural","true");
        parameter[4] = DescriptorFactory.newInstance().createParameter(props);
        if((map!=null)&&(map.getProperty("wroteRelation") != null)){
            parameter[4].setValue(map.getProperty("wroteRelation"));
        }else{
            parameter[4].setValue("Authored");
        }
        
        props.setProperty("Type","java.lang.String");
        props.setProperty("Name","referencesRelation");
        props.setProperty("Class","Basic");
        props.setProperty("Structural","true");
        parameter[5] = DescriptorFactory.newInstance().createParameter(props);
        if((map!=null)&&(map.getProperty("referencesRelation") != null)){
            parameter[5].setValue(map.getProperty("referencesRelation"));
        }else{
            parameter[5].setValue("References");
        }
        
        props.setProperty("Type","java.lang.String");
        props.setProperty("Name","classLabelFile");
        props.setProperty("Class","Basic");
        props.setProperty("Structural","true");
        parameter[6] = DescriptorFactory.newInstance().createParameter(props);
        if((map!=null)&&(map.getProperty("classLabelFile") != null)){
            parameter[6].setValue(map.getProperty("classLabelFile"));
        }else{
            parameter[6].setValue("/tmp/idLabel.xml");
        }
        
        props.setProperty("Type","java.lang.String");
        props.setProperty("Name","bidirectionalReferences");
        props.setProperty("Class","Basic");
        props.setProperty("Structural","true");
        parameter[7] = DescriptorFactory.newInstance().createParameter(props);
        if((map!=null)&&(map.getProperty("bidirectionalReferences") != null)){
            parameter[7].setValue(map.getProperty("bidirectionalReferences"));
        }else{
            parameter[7].setValue("false");
        }
        
        props.setProperty("Type", "Actor");
        props.setProperty("Relation", (String)parameter[2].getValue());
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.remove("Property");
        output[0] = DescriptorFactory.newInstance().createOutputDescriptor(props);
       
        props.setProperty("Type", "ActorProperty");
        props.setProperty("Relation", (String)parameter[2].getValue());
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property","name");
        output[1] = DescriptorFactory.newInstance().createOutputDescriptor(props);
       
        props.setProperty("Type", "Actor");
        props.setProperty("Relation", (String)parameter[3].getValue());
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.remove("Property");
        output[2] = DescriptorFactory.newInstance().createOutputDescriptor(props);
       
        props.setProperty("Type", "ActorProperty");
        props.setProperty("Relation", (String)parameter[3].getValue());
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property","title");
        output[3] = DescriptorFactory.newInstance().createOutputDescriptor(props);
       
        props.setProperty("Type", "ActorProperty");
        props.setProperty("Relation", (String)parameter[3].getValue());
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property","abstract");
        output[4] = DescriptorFactory.newInstance().createOutputDescriptor(props);
       
        props.setProperty("Type", "ActorProperty");
        props.setProperty("Relation", (String)parameter[3].getValue());
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property","year");
        output[5] = DescriptorFactory.newInstance().createOutputDescriptor(props);
       
        props.setProperty("Type", "ActorProperty");
        props.setProperty("Relation", (String)parameter[3].getValue());
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property","file");
        output[6] = DescriptorFactory.newInstance().createOutputDescriptor(props);
       
        props.setProperty("Type", "ActorProperty");
        props.setProperty("Relation", (String)parameter[3].getValue());
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property","type");
        output[7] = DescriptorFactory.newInstance().createOutputDescriptor(props);
       
        props.setProperty("Type", "ActorProperty");
        props.setProperty("Relation", (String)parameter[3].getValue());
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property","journal");
        output[8] = DescriptorFactory.newInstance().createOutputDescriptor(props);
       
        props.setProperty("Type", "ActorProperty");
        props.setProperty("Relation", (String)parameter[3].getValue());
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property","cluster");
        output[9] = DescriptorFactory.newInstance().createOutputDescriptor(props);
              
        props.setProperty("Type", "Link");
        props.setProperty("Relation", (String)parameter[5].getValue());
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.remove("Property");
        output[10] = DescriptorFactory.newInstance().createOutputDescriptor(props);
       
        props.setProperty("Type", "Link");
        props.setProperty("Relation", (String)parameter[4].getValue());
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.remove("Property");
        output[11] = DescriptorFactory.newInstance().createOutputDescriptor(props);
       

    }

}
