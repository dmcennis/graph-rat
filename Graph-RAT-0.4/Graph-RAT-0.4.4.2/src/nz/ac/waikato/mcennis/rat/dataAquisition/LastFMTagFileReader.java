/*
 * Created 7-5-08
 * Copyright Daniel McEnnis, see license.txt
 */

package nz.ac.waikato.mcennis.rat.dataAquisition;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import nz.ac.waikato.mcennis.rat.crawler.FileListCrawler;
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
 * Read a directory of files containing tags for each artist in an XML file read
 * by the LastFMArtistTag XML parser.
 * 
 * @author Daniel McEnnis
 */
public class LastFMTagFileReader extends ModelShell implements DataAquisition{

    ParameterInternal[] parameter = new ParameterInternal[2];
    OutputDescriptor[] output = new OutputDescriptor[4];
    
    Graph graph = null;
    
    public void start() {
        fireChange(Scheduler.SET_GRAPH_COUNT,1);
        java.util.Properties props = new java.util.Properties();
        props.setProperty("ParserType","LastFMArtistTag");
        Parser[] parser = new Parser[]{ParserFactory.newInstance().create(props)};
        parser[0].set(graph);
        FileListCrawler crawler  = new FileListCrawler();
        crawler.set(parser);
        
        java.io.File directory = new java.io.File((String)parameter[1].getValue());
        java.io.File[] files = directory.listFiles();
        fireChange(Scheduler.SET_ALGORITHM_COUNT,files.length);
        for(int i=0;i<files.length;++i){
            try {
                if(i%100==0){
                    Logger.getLogger(LastFMTagFileReader.class.getName()).log(Level.FINE,i +" of "+files.length);
                    fireChange(Scheduler.SET_ALGORITHM_PROGRESS,i);
                }
                crawler.crawl(files[i].getAbsolutePath());
            } catch(Exception e){
                e.printStackTrace();
            }
        }
        
        graph = ((Graph)crawler.getParser()[0].get());
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
     * Parameters:<br/>
     * <br/>
     * <ul>
     * <li/><b>name</b>: name of this algorithm. Default is 'File LastFM Tag Reader'
     * <li/><b>artistTagDirectory</b>: location of the artist directory. Default 
     * is the first root from File.listRoots()
     * </ul>
     * @param map properties to load - null is permitted.
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
            parameter[0].setValue("File LastFM Tag Reader");
        }
        
        props.setProperty("Type","java.lang.String");
        props.setProperty("Name","artistTagDirectory");
        props.setProperty("Class","Basic");
        props.setProperty("Structural","true");
        parameter[1] = DescriptorFactory.newInstance().createParameter(props);
        if((map!=null)&&(map.getProperty("artistTagDirectory") != null)){
            parameter[1].setValue(map.getProperty("artistTagDirectory"));
        }else{
            parameter[1].setValue(java.io.File.listRoots()[0].getAbsolutePath());
        }
        
        props.setProperty("Type", "Actor");
        props.setProperty("Relation", "artist");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.remove("Property");
        output[0] = DescriptorFactory.newInstance().createOutputDescriptor(props);
       
        props.setProperty("Type", "Actor");
        props.setProperty("Relation", "tag");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.remove("Property");
        output[1] = DescriptorFactory.newInstance().createOutputDescriptor(props);

        props.setProperty("Type", "ActorProperty");
        props.setProperty("Relation", "tag");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property","URL");
        output[2] = DescriptorFactory.newInstance().createOutputDescriptor(props);
       
        props.setProperty("Type", "Link");
        props.setProperty("Relation", "ArtistTag");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.remove("Property");
        output[3] = DescriptorFactory.newInstance().createOutputDescriptor(props);
       
    }

}
