/*

 * Created 21-1-08

 */

package nz.ac.waikato.bibliography.algorithm;



import java.io.File;
import java.io.FileWriter;

import java.io.IOException;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import java.util.logging.Level;

import java.util.logging.Logger;

import nz.ac.waikato.mcennis.rat.graph.Graph;

import nz.ac.waikato.mcennis.rat.graph.actor.Actor;

import nz.ac.waikato.mcennis.rat.graph.algorithm.Algorithm;

import nz.ac.waikato.mcennis.rat.graph.algorithm.AlgorithmMacros;
import nz.ac.waikato.mcennis.rat.graph.descriptors.IODescriptor;
import nz.ac.waikato.mcennis.rat.graph.descriptors.IODescriptorFactory;
import nz.ac.waikato.mcennis.rat.graph.descriptors.IODescriptorInternal;
import org.dynamicfactory.descriptors.*;

import org.dynamicfactory.model.ModelShell;

import nz.ac.waikato.mcennis.rat.graph.query.actor.ActorByMode;
import nz.ac.waikato.mcennis.rat.scheduler.Scheduler;

import nz.ac.waikato.mcennis.rat.util.Duples;
import org.dynamicfactory.property.Property;


/**

 *

 * @author Daniel McEnnis

 */

public class OutputBridgesByCluster extends ModelShell implements Algorithm {



    PropertiesInternal parameter = PropertiesFactory.newInstance().create();

    LinkedList<IODescriptor> input = new LinkedList<IODescriptor>();

    LinkedList<IODescriptor> output = new LinkedList<IODescriptor>();





    public OutputBridgesByCluster(){
        ParameterInternal name = ParameterFactory.newInstance().create("AlgorithmClass",String.class);
        SyntaxObject syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("Output Bridges By Cluster");
        parameter.add(name);
        
        name = ParameterFactory.newInstance().create("Name",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,Integer.MAX_VALUE,null,String.class);
        name.setRestrictions(syntax);
        name.add("Output Bridges By Cluster");
        parameter.add(name);
        
        name = ParameterFactory.newInstance().create("Category",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("Bibliography");
        parameter.add(name);
        

        name = ParameterFactory.newInstance().create("Mode",String.class);

        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);

        name.setRestrictions(syntax);

        name.add("Paper");

        parameter.add(name);

        

        name = ParameterFactory.newInstance().create("OutputFile",File.class);

        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,File.class);

        name.setRestrictions(syntax);

        parameter.add(name);

        

        name = ParameterFactory.newInstance().create("ClusterID",String.class);

        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);

        name.setRestrictions(syntax);

        name.add("cluster");

        parameter.add(name);

        

        name = ParameterFactory.newInstance().create("BridgeEntry",String.class);

        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);

        name.setRestrictions(syntax);

        name.add("Bridge Endpoint");

        parameter.add(name);

        

        name = ParameterFactory.newInstance().create("BridgeMagnitude",String.class);

        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);

        name.setRestrictions(syntax);

        name.add("Bridge Magnitude");

        parameter.add(name);

        

    }



    public void execute(Graph g) {

        ActorByMode query = new ActorByMode();
        query.buildQuery((String)parameter.get("Mode").get(),".*",false);
        Iterator<Actor> actor = AlgorithmMacros.filterActor(parameter,g,query,null,null);
        int actorCount = g.getActorCount((String)parameter.get("Mode").get());
        FileWriter output = null;

        try {

            output = new FileWriter((File)parameter.get("OutputFile").get());

            if (actorCount>0) {

                fireChange(Scheduler.SET_ALGORITHM_COUNT,actorCount*2);

                Vector<Duples<Double, Actor>> results = new Vector<Duples<Double, Actor>>();
                int count=0;
                while (actor.hasNext()) {
                    Actor a = actor.next();
                    Property magnitude = a.getProperty((String) parameter.get("Bridge Magnitude").get());

                    double maxExternal = 0.0;

                    if ((magnitude != null) && (magnitude.getValue().size()>0)) {
                        Iterator value = magnitude.getValue().iterator();
                        while (value.hasNext()) {
                            Double mag = (Double)value.next();
                            if (mag.doubleValue() > maxExternal) {

                                maxExternal = mag.doubleValue();

                            }

                        }

                        Duples<Double, Actor> ret = new Duples<Double, Actor>();

                        ret.setLeft(maxExternal);

                        ret.setRight(a);

                        results.add(ret);

                    }

                    fireChange(Scheduler.SET_ALGORITHM_PROGRESS,count++);

                }

                fireChange(Scheduler.SET_ALGORITHM_COUNT,actorCount+results.size());

                Collections.sort(results);

                Logger.getLogger(OutputBridgesByCluster.class.getName()).log(Level.INFO,"Total number of Bridges Found " + results.size());

                output.append("<?xml version=\"1.0\"?>\n");

                output.append("<!DOCTYPE bridges [\n");

                output.append("\t<!ELEMENT bridges (paper+)>\n");

                output.append("\t<!ELEMENT paper (paperID,title,link*)>\n");

                output.append("\t<!ELEMENT paperID (#PCDATA)>\n");

                output.append("\t<!ELEMENT title (#PCDATA)>\n");

                output.append("\t<!ELEMENT link (cluster,score)>\n");

                output.append("\t<!ELEMENT cluster (#PCDATA)>\n");

                output.append("\t<!ELEMENT score (#PCDATA)>\n");

                output.append("]>\n\n");

                output.append("<bridges>\n");

                for (int i = 0; i < results.size(); ++i) {

//                    System.out.println("");

                    String title = (String) results.get(i).getRight().getProperty("title").getValue().get(0);

                    output.append("\t<paper>\n");

                    output.append("\t\t<paperID>" + results.get(i).getRight().getID() + "</paperID>\n");

                    output.append("\t\t<title>" + title + "</title>\n");

                    Property cluster = results.get(i).getRight().getProperty((String) parameter.get("ClusterID").get());

                    if ((cluster != null) && (cluster.getValue() != null)) {

                        Logger.getLogger(OutputBridgesByCluster.class.getName()).log(Level.FINER,(String) cluster.getValue().get(0) + "\t");

                    }



                    Logger.getLogger(OutputBridgesByCluster.class.getName()).log(Level.FINE,results.get(i).getRight().getID() + "\t" + title);

                    Property bridgeEndpoint = results.get(i).getRight().getProperty((String) parameter.get("BridgeEntry").get());

                    Property bridgeMagnitude = results.get(i).getRight().getProperty((String) parameter.get("BridgeMagnitude").get());
                    count = 0;
                    if ((bridgeEndpoint != null) && (bridgeMagnitude != null) && (bridgeEndpoint.getValue() != null) && (bridgeMagnitude.getValue() != null) && (bridgeEndpoint.getValue().size() == bridgeMagnitude.getValue().size())) {
                        Iterator end = bridgeEndpoint.getValue().iterator();
                        Iterator mag = bridgeMagnitude.getValue().iterator();
                        while (end.hasNext() && mag.hasNext()) {
                            Double m = (Double)mag.next();
                            String e = (String)end.next();
                            output.append("\t\t<link>\n");

                            output.append("\t\t\t<cluster>" + e + "</cluster>\n");

                            output.append("\t\t\t<score>" + m.toString() + "</score>\n");

                            Logger.getLogger(OutputBridgesByCluster.class.getName()).log(Level.FINE,e + "\t" + (m).toString());

                            output.append("\t\t</link>\n");

                        }

                    }

                    output.append("\t</paper>");

                    fireChange(Scheduler.SET_ALGORITHM_PROGRESS,actorCount+(count++));

                }

                output.append("</bridges>\n");

            // output source , dest, magnitude, title

            // output source , dest, magnitude, title

            } else {

                Logger.getLogger(OutputBridgesByCluster.class.getName()).log(Level.WARNING,"No actors of type '" + (String) parameter.get("Mode").get() + "' found");

            }

        } catch (IOException ex) {

            Logger.getLogger(OutputBridgesByCluster.class.getName()).log(Level.SEVERE, null, ex);

        } finally {

            try {

                output.close();

            } catch (IOException ex) {

                Logger.getLogger(OutputBridgesByCluster.class.getName()).log(Level.SEVERE, null, ex);

            }

        }

    }



    @Override

    public List<IODescriptor> getInputType() {

        return input;

    }



    @Override

    public List<IODescriptor> getOutputType() {

        return output;

    }



    @Override

    public Properties getParameter() {

        return parameter;

    }



    @Override

    public Parameter getParameter(String param) {

        return parameter.get(param);

    }



    public void init(Properties map) {

	if(parameter.check(map)){

	    parameter = parameter.merge(map);

	    IODescriptorInternal descriptor = IODescriptorFactory.newInstance().create(

		 IODescriptor.Type.ACTOR_PROPERTY, 

                (String)parameter.get("Name").get(),

		 (String)parameter.get("Mode").get(), 

                null,

		 (String)parameter.get("ClusterID").get(),"");

	    input.add(descriptor);

		descriptor = IODescriptorFactory.newInstance().create(

		 IODescriptor.Type.ACTOR_PROPERTY, 

		 (String)parameter.get("Name").get(),

		 (String)parameter.get("Mode").get(), 

		 null,

		 "title","");

	    input.add(descriptor);

		descriptor = IODescriptorFactory.newInstance().create(

		 IODescriptor.Type.ACTOR_PROPERTY, 

		 (String)parameter.get("Name").get(),

		 (String)parameter.get("Mode").get(), 

		 null,

		 (String)parameter.get("BridgeEntry").get(),"");

	    input.add(descriptor);

		descriptor = IODescriptorFactory.newInstance().create(

		 IODescriptor.Type.ACTOR_PROPERTY, 

		 (String)parameter.get("Name").get(),

		 (String)parameter.get("Mode").get(),

         null,

		 (String)parameter.get("BridgeMagnitude").get(),"");

	    input.add(descriptor);

	}

    }

    public OutputBridgesByCluster prototype() {
        return new OutputBridgesByCluster();
    }

}

