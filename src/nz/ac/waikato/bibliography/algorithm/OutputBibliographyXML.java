/*

 * OutputBibliographyXML.java

 * 

 * Created on 8/01/2008, 14:01:39

 * 

 * 

 */

package nz.ac.waikato.bibliography.algorithm;



import java.io.File;

import java.io.FileWriter;

import java.io.IOException;

import java.util.Collection;

import java.util.Iterator;

import java.util.LinkedList;

import java.util.List;

import java.util.logging.Level;

import java.util.logging.Logger;

import nz.ac.waikato.mcennis.rat.graph.Graph;

import nz.ac.waikato.mcennis.rat.graph.actor.Actor;

import nz.ac.waikato.mcennis.rat.graph.algorithm.Algorithm;


import nz.ac.waikato.mcennis.rat.graph.algorithm.AlgorithmMacros;
import org.dynamicfactory.descriptors.IODescriptorFactory;

import org.dynamicfactory.descriptors.IODescriptor;

import org.dynamicfactory.descriptors.IODescriptorInternal;

import org.dynamicfactory.descriptors.Parameter;

import org.dynamicfactory.descriptors.ParameterFactory;

import org.dynamicfactory.descriptors.ParameterInternal;

import org.dynamicfactory.descriptors.Properties;

import org.dynamicfactory.descriptors.PropertiesFactory;

import org.dynamicfactory.descriptors.PropertiesInternal;

import org.dynamicfactory.descriptors.SyntaxCheckerFactory;

import org.dynamicfactory.descriptors.SyntaxObject;

import nz.ac.waikato.mcennis.rat.graph.query.Query;

import nz.ac.waikato.mcennis.rat.graph.model.ModelShell;

import org.dynamicfactory.property.Property;

import nz.ac.waikato.mcennis.rat.graph.query.ActorQuery;

import nz.ac.waikato.mcennis.rat.graph.query.LinkQuery;

import nz.ac.waikato.mcennis.rat.graph.query.LinkQueryFactory;

import nz.ac.waikato.mcennis.rat.graph.query.actor.ActorByMode;

import nz.ac.waikato.mcennis.rat.graph.query.link.LinkByRelation;

import nz.ac.waikato.mcennis.rat.scheduler.Scheduler;



/**

 * Class for outputting into an XML format the pagerank of each file.

 * 

 * @author Daniel McEnnis

 */

public class OutputBibliographyXML extends ModelShell implements Algorithm {



    PropertiesInternal parameter = PropertiesFactory.newInstance().create();

    LinkedList<IODescriptor> input = new LinkedList<IODescriptor>();

    LinkedList<IODescriptor> output = new LinkedList<IODescriptor>();



    public OutputBibliographyXML() {

        ParameterInternal name = ParameterFactory.newInstance().create("AlgorithmClass",String.class);
        SyntaxObject syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("Output Bibliography XML");
        parameter.add(name);
        
        name = ParameterFactory.newInstance().create("Name",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,Integer.MAX_VALUE,null,String.class);
        name.setRestrictions(syntax);
        name.add("Output Bibliography XML");
        parameter.add(name);
        
        name = ParameterFactory.newInstance().create("Category",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("Bibliography");
        parameter.add(name);
        
        name = ParameterFactory.newInstance().create("LinkFilter",LinkQuery.class);
        syntax = SyntaxCheckerFactory.newInstance().create(0,1,null,LinkQuery.class);
        name.setRestrictions(syntax);
        parameter.add(name);
        

        name = ParameterFactory.newInstance().create("ActorFilter",ActorQuery.class);
        syntax = SyntaxCheckerFactory.newInstance().create(0,1,null,Query.class);
        name.setRestrictions(syntax);
        parameter.add(name);
       

        name = ParameterFactory.newInstance().create("Mode",ActorQuery.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        ActorByMode actor = new ActorByMode();
        actor.buildQuery("Paper",".*",false);
        name.add(actor);
        parameter.add(name);
        
        name = ParameterFactory.newInstance().create("Relation",LinkQuery.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        LinkByRelation relation = (LinkByRelation) LinkQueryFactory.newInstance().create("LinkByRelation");
        relation.buildQuery("Paper", false);
        name.add(relation);
        parameter.add(name);
       
        name = ParameterFactory.newInstance().create("OutputFile",File.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,File.class);
        name.setRestrictions(syntax);
        parameter.add(name);
        
        name = ParameterFactory.newInstance().create("PropertyNames",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,Integer.MAX_VALUE,null,String.class);
        name.setRestrictions(syntax);
        name.add("Global PageRank Centrality");
        name.add("Knows PageRank Centrality");
        name.add("SubGraph PageRank");
        parameter.add(name);
      
    }



    public void execute(Graph g) {

        FileWriter output = null;

        try {

            output = new FileWriter((File) parameter.get("OutputFile"));

            output.append("<?xml version=\"1.0\"?>\n");

            output.append("<!DOCTYPE pageRankValues [\n");

            output.append("  <!ELEMENT pageRankValues (paper*)>\n");

            output.append("  <!ELEMENT paper (paperID,title,pageRank*)>\n");

            output.append("  <!ELEMENT paperID (#PCDATA)>\n");

            output.append("  <!ELEMENT title (#PCDATA)>\n");

            output.append("  <!ELEMENT pageRank (#PCDATA)>\n");

            output.append("]>\n\n");

            output.append("<pageRankValues>\n");

            Collection<Actor> listColl = AlgorithmMacros.filterActor(parameter,g,((ActorQuery)parameter.get("Mode")).execute(g, null, null));

	    Iterator<Actor> list = listColl.iterator();

            if (list.hasNext()) {

                fireChange(Scheduler.SET_ALGORITHM_COUNT, listColl.size());

                int i=0;

                while(list.hasNext()) {

		    Actor actor = list.next();

                    output.append("\t<paper>\n");

                    output.append("\t\t<paperID>" + actor.getID() + "</paperID>\n");

                    String title = (String) actor.getProperty("title").getValue().get(0);

                    output.append("\t\t<title>").append(title).append("</title>\n");

		    Iterator propertyNames = parameter.get("PropertyNames").getValue().iterator();

		    while(propertyNames.hasNext()){

			String propertyName = (String)propertyNames.next();

			Property globalProperty = actor.getProperty(propertyName);

			if (globalProperty != null) {

			    if (globalProperty.getPropertyClass().isAssignableFrom(Double.class)) {

				if (globalProperty.getValue() != null) {

				    output.append("\t\t<pageRank>" + ((Double) globalProperty.getValue().get(0)).toString() + "</pageRank>\n");

				} else {

				    Logger.getLogger(OutputBibliographyXML.class.getName()).log(Level.WARNING,propertyName + " of actor ID " + actor.getID() + " has no values");

				}

			    } else {

				Logger.getLogger(OutputBibliographyXML.class.getName()).log(Level.SEVERE,propertyName + " of actor ID " + actor.getID() + "is not a double property type");

			    }

			} else {

                        Logger.getLogger(OutputBibliographyXML.class.getName()).log(Level.WARNING,propertyName + " of actor ID " + actor.getID() + " does not exist");

			}



		    }

		    

                    fireChange(Scheduler.SET_ALGORITHM_PROGRESS, i++);

                }

                output.append("</pageRankValues>");

            } else {

                Logger.getLogger(OutputBibliographyXML.class.getName()).log(Level.WARNING,"No actors were found");

            }

        } catch (IOException ex) {

            Logger.getLogger(OutputBibliographyXML.class.getName()).log(Level.SEVERE, null, ex);

        } finally {

            try {

                output.close();

            } catch (IOException ex) {

                Logger.getLogger(OutputBibliographyXML.class.getName()).log(Level.SEVERE, null, ex);

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



    /**

     * Initializes the object using the property map provided

     * 

     * Parameters of this algorithm:

     * <ul>

     *  <li>name: name of this algorithm. default 'Output PageRank'

     *  <li>actorType: mode of actor to evaluate over. default 'Paper'

     *  <li>outputFile: file to write results to. default '/tmp/PageRank'

     *  <li>property1: property name to write contents of. default 'Knows PageRank Centrality'

     *  <li>property2: property name to write contents of. default 'SubGraph PageRank'

     * </ul>

     * 

     * Input 1: ActorProperty

     * Input 2: ActorProperty

     * 

     * @param map

     */

    public void init(Properties map) {

	if(parameter.check(map)){

	    parameter = parameter.merge(map);

	    Iterator props = parameter.get("PropertyNames").getValue().iterator();



	    IODescriptorInternal descriptor = IODescriptorFactory.newInstance().create(

		 IODescriptor.Type.ACTOR_PROPERTY, 

                (String)parameter.get("Name").get(),

                (String)null, 

                (Query)parameter.get("Mode").get(),

                null,"");

	    input.add(descriptor);

	    while(props.hasNext()){

		descriptor = IODescriptorFactory.newInstance().create(

		 IODescriptor.Type.ACTOR_PROPERTY, 

                (String)parameter.get("Name").get(),

                (String)null, 

                (Query)parameter.get("Mode").get(),

                (String)props.next(),"");

	    input.add(descriptor);

	    }

	}

    }



    public Algorithm prototype() {

        return new OutputBibliographyXML();

    }

}

