/*
 * AlgorithmFactory.java
 *
 * Created on 1 May 2007, 17:04
 *
 * copyright Daniel McEnnis - published under Aferro GPL (see license.txt)
 */
package nz.ac.waikato.mcennis.rat.graph.algorithm;

import nz.ac.waikato.mcennis.rat.graph.algorithm.similarity.SimilarityByLink;
import nz.ac.waikato.mcennis.rat.graph.algorithm.similarity.HierarchyByCooccurance;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import nz.ac.waikato.mcennis.rat.graph.algorithm.prestige.ScalablePageRankPrestige;
import nz.ac.waikato.mcennis.rat.graph.algorithm.prestige.Closeness;
import nz.ac.waikato.mcennis.rat.graph.algorithm.prestige.Betweeness;
import nz.ac.waikato.mcennis.rat.graph.algorithm.prestige.HITSPrestige;
import nz.ac.waikato.mcennis.rat.graph.algorithm.clustering.EnumerateMaximalCliques;
import nz.ac.waikato.mcennis.rat.graph.algorithm.prestige.PageRankPrestige;
import nz.ac.waikato.mcennis.rat.graph.algorithm.prestige.DegreePrestige;
import nz.ac.waikato.bibliography.algorithm.OutputBibliographyXML;
import nz.ac.waikato.bibliography.algorithm.OutputBridgesByCluster;
import nz.ac.waikato.bibliography.algorithm.OutputDifference;
import nz.ac.waikato.mcennis.rat.AbstractFactory;
import nz.ac.waikato.mcennis.rat.graph.algorithm.aggregators.AggregateToGraph;
import nz.ac.waikato.mcennis.rat.graph.algorithm.aggregators.AggregateByLinkProperty;
import nz.ac.waikato.mcennis.rat.graph.algorithm.aggregators.AggregateLinks;
import nz.ac.waikato.mcennis.rat.graph.algorithm.aggregators.AggregateOnActor;
import nz.ac.waikato.mcennis.rat.graph.algorithm.aggregators.FromGraphToActor;
import nz.ac.waikato.mcennis.rat.graph.algorithm.clustering.BicomponentClusterer;
import nz.ac.waikato.mcennis.rat.graph.algorithm.clustering.FindStronglyConnectedComponents;
import nz.ac.waikato.mcennis.rat.graph.algorithm.clustering.FindWeaklyConnectedComponents;
import nz.ac.waikato.mcennis.rat.graph.algorithm.clustering.NormanGirvanEdgeBetweenessClustering;
import nz.ac.waikato.mcennis.rat.graph.algorithm.clustering.TraditionalEdgeBetweenessClustering;
import nz.ac.waikato.mcennis.rat.graph.algorithm.collaborativefiltering.AssociativeMining;
import nz.ac.waikato.mcennis.rat.graph.algorithm.collaborativefiltering.Item2Item;
import nz.ac.waikato.mcennis.rat.graph.algorithm.collaborativefiltering.User2User;
import nz.ac.waikato.mcennis.rat.graph.algorithm.evaluation.HalfLife;
import nz.ac.waikato.mcennis.rat.graph.algorithm.evaluation.KendallTau;
import nz.ac.waikato.mcennis.rat.graph.algorithm.evaluation.MeanErrorEvaluation;
import nz.ac.waikato.mcennis.rat.graph.algorithm.evaluation.PearsonCorrelation;
import nz.ac.waikato.mcennis.rat.graph.algorithm.evaluation.PrecisionRecallFMeasure;
import nz.ac.waikato.mcennis.rat.graph.algorithm.evaluation.ROCAreaEvaluation;
import nz.ac.waikato.mcennis.rat.graph.algorithm.similarity.GraphSimilarityByProperty;
import nz.ac.waikato.mcennis.rat.graph.algorithm.similarity.SimilarityByProperty;
import nz.ac.waikato.mcennis.rat.graph.algorithm.visual.BasicDisplayGraph;
import nz.ac.waikato.mcennis.rat.graph.algorithm.visual.ColoredByPropertyGraph;
import nz.ac.waikato.mcennis.rat.graph.algorithm.visual.DisplayAll;
import nz.ac.waikato.mcennis.rat.graph.query.ActorQuery;
import nz.ac.waikato.mcennis.rat.graph.query.LinkQuery;

/**
 * Class for generating an algorithm object without explicitly linking which one.
 *
 * @author Daniel McEnnis
 * 
 */
public class AlgorithmFactory extends AbstractFactory<Algorithm>{

    static AlgorithmFactory instance = null;

    /**
     * Singleton method for aquiring this instance
     * @return AlgorithmFactory instance.
     */
    public static AlgorithmFactory newInstance() {

        if (instance == null) {

            instance = new AlgorithmFactory();

        }

        return instance;

    }

    /** Creates a new instance of AlgorithmFactory */
    public AlgorithmFactory() {
        ParameterInternal name = ParameterFactory.newInstance().create("AlgorithmClass",String.class);
        SyntaxObject syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("AggregateByLinkProperty");
        properties.add(name);
        
        name = ParameterFactory.newInstance().create("Name",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,Integer.MAX_VALUE,null,String.class);
        name.setRestrictions(syntax);
        properties.add(name);
        
        name = ParameterFactory.newInstance().create("Category",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        properties.add(name);
        
        name = ParameterFactory.newInstance().create("LinkFilter",LinkQuery.class);
        syntax = SyntaxCheckerFactory.newInstance().create(0,1,null,LinkQuery.class);
        name.setRestrictions(syntax);
        properties.add(name);
        
        name = ParameterFactory.newInstance().create("ActorFilter",ActorQuery.class);
        syntax = SyntaxCheckerFactory.newInstance().create(0,1,null,ActorQuery.class);
        name.setRestrictions(syntax);
        properties.add(name);
        
        // all map statements here
        map.put("AddBasicGeodesicPaths",new GeodesicPaths());
        map.put("AddDegreeCentrality",new DegreePrestige());
        map.put("Degree Graph Properties",new DegreeGraphProperties());
        map.put("AddGeodesicProperties",new GeodesicProperties());
        map.put("AddPageRankPrestige",new PageRankPrestige());
        map.put("EnumerateMaximalCliques",new EnumerateMaximalCliques());
        map.put("TrimEdgeUsers",new TrimEdgeUsers());
        map.put("HITSPrestige",new HITSPrestige());
        map.put("ScalablePageRankPrestige",new ScalablePageRankPrestige());
        map.put("OptimizedCloseness",new Closeness());
        map.put("OptimizedBetweeness",new Betweeness());
        map.put("RankingProperties",new RankingProperties());
        map.put("OutputPageRank",new OutputBibliographyXML());
        map.put("OutputDifference",new OutputDifference());
        map.put("Identify Bridges",new IdentifyBridges());
        map.put("Output Bridges By Cluster",new OutputBridgesByCluster());
        map.put("Basic Display Graph",new BasicDisplayGraph());
        map.put("Colored By Property Graph",new ColoredByPropertyGraph());
        map.put("Display All",new DisplayAll());
        map.put("Bicomponent Clusterer",new BicomponentClusterer());
        map.put("Find Strongly Connected Components",new FindStronglyConnectedComponents());
        map.put("Find Weakly Connected Components",new FindWeaklyConnectedComponents());
        map.put("Traditional Edge Betweeness Clustering",new TraditionalEdgeBetweenessClustering());
        map.put("Norman-Girvan Edge Betweeness Clustering",new NormanGirvanEdgeBetweenessClustering());
        map.put("Associative Mining Collaborative Filtering",new AssociativeMining());
        map.put("User to User Collaborative Filtering",new User2User());
        map.put("Item to Item Collaborative Filtering",new Item2Item());
        map.put("Half Life",new HalfLife());
        map.put("Kendall Tau",new KendallTau());
        map.put("Mean Error Evaluation",new MeanErrorEvaluation());
        map.put("Pearson Correlation",new PearsonCorrelation());
        map.put("Precision Recall FMeasure",new PrecisionRecallFMeasure());
        map.put("ROC Area",new ROCAreaEvaluation());
        map.put("Hierarchy By Cooccurance",new HierarchyByCooccurance());
        map.put("Similarity By Link",new SimilarityByLink());
        map.put("Similarity By Property",new SimilarityByProperty());
        map.put("Graph Similarity By Property",new GraphSimilarityByProperty());
        map.put("From Graph To Actor",new FromGraphToActor());
        map.put("Aggregate On Actor",new AggregateOnActor());
        map.put("Aggregate By Link Property",new AggregateByLinkProperty());
        map.put("Aggregate By Graph",new AggregateToGraph());
        map.put("Aggregate Links",new AggregateLinks());
    }

    /**
     * Creates an algorithm
     * <br/>
     * <br/>Algorithm is chosen via the 'algorithm' property.  Properties for each
     * algorithm are described in the init method of each algorithm's JavaDoc.
     * <br/>The algorithms provided are:
     * <ul><li>'AddBasicBetweenessCentrality'</li>
     * <li>'AddBasicGeodesicPaths'</li>
     * <li>'AddBasicInterestLink'</li>
     * <li>'AddClosenessCentrality'</li>
     * <li>'AddDegreeCentrality'</li>
     * <li>'AddDegreeProperties'</li>
     * <li>'AddGeodesicProperties'</li>
     * <li>'AddMusicLinks'</li>
     * <li>'AddMusicRecommendations'</li>
     * <li>'AddMusicReferences'</li>
     * <li>'AddPageRankPrestige'</li>
     * <li>'EnumerateMaximalCliques'</li>
     * <li>'NaiveImplementClique'</li>
     * <li>'Evaluation'</li>
     * <li>'TrimEdgeUsers'</li>
     * <li>'HITSPrestige'</li>
     * <li>'ScalablePageRankPrestige'</li>
     * <li>'ScalableHITSPrestige'</li>
     * <li>'AddCombinedCloseness'</li>
     * <li>'OptimizedCloseness'</li>
     * <li>'OptimizedBetweeness'</li>
     * <li>'ExtractMusicCoverage'</li>
     * <li>'ExecuteWeka'</li>
     * <li>'OutputPageRank'</li>
     * <li>'RankingProperties'</li>
     * <li>'Difference'</li>
     * <li>'OutputDifference'</li>
     * <li>'Identify Bridges'</li>
     * <li>'Output Bridges By Cluster'</li>
     * <li>'Basic Display Graph'</li>
     * <li>'Colored By Property Graph'</li>
     * <li>'Display All'</li>
     * </ul>
     * 
     * @param props mapof parameters for initialiing the algorithm
     * @return newly constructed algorithm
     */

    public String[] getKnownOtherModules() {
        LinkedList<String> knownModulesList = new LinkedList<String>();
        knownModulesList.add("AddBasicGeodesicPaths");
        knownModulesList.add("AddBasicInterestLink");
        knownModulesList.add("AddDegreeGraphProperties");
        knownModulesList.add("AddGeodesicProperties");
        knownModulesList.add("AddMusicLinks");
        knownModulesList.add("AddMusicRecommendations");
        knownModulesList.add("AddMusicReferences");
        knownModulesList.add("TrimEdgeUsers");
        knownModulesList.add("RankingProperties");
        knownModulesList.add("Difference");
        knownModulesList.add("Identify Bridges");
        java.util.Collections.sort(knownModulesList);
        return knownModulesList.toArray(new String[]{});
    }
    
    public String[] getKnownSimilarityModules(){
        LinkedList<String> knownModulesList = new LinkedList<String>();
        knownModulesList.add("Hierarchy By Cooccurance");
        knownModulesList.add("Similarity By Link");
        knownModulesList.add("Similarity By Property");
        knownModulesList.add("Graph Similarity By Property");
        java.util.Collections.sort(knownModulesList);
        return knownModulesList.toArray(new String[]{});
    }
    
    public String[] getKnownAggregatorModules(){
        LinkedList<String> knownModulesList = new LinkedList<String>();
        knownModulesList.add("Aggregate on Actor");
        knownModulesList.add("Aggregate By Link");
        knownModulesList.add("Aggregate By Graph");
        knownModulesList.add("From Graph To Actor");
        java.util.Collections.sort(knownModulesList);
        return knownModulesList.toArray(new String[]{});
   }

    public String[] getKnownPrestigeModules() {
        LinkedList<String> knownModulesList = new LinkedList<String>();
        knownModulesList.add("AddBasicBetweenessCentrality");
        knownModulesList.add("AddBetweenessCentrality");
        knownModulesList.add("OptimizedBetweeness");
        knownModulesList.add("OptimizedCloseness");
        knownModulesList.add("AddCombinedCloseness");
        knownModulesList.add("ScalableHITSPrestige");
        knownModulesList.add("ScalablePageRankPrestige");
        knownModulesList.add("HITSPrestige");
        knownModulesList.add("AddPageRankPrestige");
        knownModulesList.add("AddDegreeCentrality");
        knownModulesList.add("AddClosenessCentrality");
        knownModulesList.add("AddBetweenessCentrality");
        knownModulesList.add("AddBasicBetweenessCentrality");
        java.util.Collections.sort(knownModulesList);
        return knownModulesList.toArray(new String[]{});
    }

    public String[] getKnownClusterModules() {
        LinkedList<String> knownModulesList = new LinkedList<String>();
        knownModulesList.add("NaiveImplementClique");
        knownModulesList.add("EnumerateMaximalCliques");
        knownModulesList.add("Bicomponent Clusterer");
        knownModulesList.add("Find Strongly Connected Components");
        knownModulesList.add("Find Weakly Connected Components");
        knownModulesList.add("Traditional Edge Betweeness Clustering");
        knownModulesList.add("Norman-Girvan Edge Betweeness Clustering");
        java.util.Collections.sort(knownModulesList);
        return knownModulesList.toArray(new String[]{});
    }
    
    public String[] getKnownCollaborativeFilteringModules(){
        LinkedList<String> knownModulesList = new LinkedList<String>();
        knownModulesList.add("Associative Mining Collaborative Filtering");
        knownModulesList.add("User to User Collaborative Filtering");
        knownModulesList.add("Item to Item Collaborative Filtering");
        java.util.Collections.sort(knownModulesList);
        return knownModulesList.toArray(new String[]{});
    }

    public String[] getKnownDisplayModules() {
        LinkedList<String> knownModulesList = new LinkedList<String>();
        knownModulesList.add("ExtractMusicCoverage");
        knownModulesList.add("Basic Display Graph");
        knownModulesList.add("Colored By Property Graph");
        knownModulesList.add("Display All");
        knownModulesList.add("Output Bridges By Cluster");
        knownModulesList.add("OutputDifference");
        knownModulesList.add("OutputPageRank");
        java.util.Collections.sort(knownModulesList);
        return knownModulesList.toArray(new String[]{});
    }
    
    public String[] getKnownMachineLearningModules(){
        LinkedList<String> knownModulesList = new LinkedList<String>();
        knownModulesList.add("MI Weka");
        knownModulesList.add("Weka");
        java.util.Collections.sort(knownModulesList);
        return knownModulesList.toArray(new String[]{});
    }
    
    public String[] getKnownEvaluationModules(){
        LinkedList<String> knownModulesList = new LinkedList<String>();
        knownModulesList.add("Ad Hoc Classifciation");
        knownModulesList.add("Half Life");
        knownModulesList.add("Mean Error Evaluation");
        knownModulesList.add("Kendall Tau");
        knownModulesList.add("Precision Recall FMeasure");
        knownModulesList.add("PearsonCorrelation");
        java.util.Collections.sort(knownModulesList);
        return knownModulesList.toArray(new String[]{});
    }

    @Override 
    public Algorithm create(Properties props){
        return create(null,props);
    }
    
    public Algorithm create(String name){
        return create(name,properties);
    }
    
    public Algorithm create(String classType, Properties parameters){
        if(classType != null){
        if ((parameters.get("AlgorithmClass") != null) && (parameters.get("AlgorithmClass").getParameterClass().getName().contentEquals(String.class.getName()))) {
            classType = (String) parameters.get("AlgorithmClass").getValue().iterator().next();
        } else {
            classType = (String) properties.get("AlgorithmClass").getValue().iterator().next();
        }
        }
        Algorithm ret = null;
        if(map.containsKey(classType)){
            ret = (Algorithm)map.get(classType).prototype();
        }else{
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE,"Algorithm class '"+classType+"' nbot found - assuming AggregateLinks");
            ret = new AggregateLinks();
        }
        ret.init(parameters);
        return ret;
    }
    
    @Override
    public boolean check(Properties parameters) {
        String classType = "";
        if ((parameters.get("AlgorithmClass") != null) && (parameters.get("AlgorithmClass").getParameterClass().getName().contentEquals(String.class.getName()))) {
            classType = (String) parameters.get("AlgorithmClass").getValue().iterator().next();
        } else {
            classType = (String) properties.get("AlgorithmClass").getValue().iterator().next();
        }

        if(map.containsKey(classType)){
            return map.get(classType).getParameter().check(parameters);
        }else{
            Logger.getLogger(this.getClass().getName()).log(Level.INFO,"Algorithm '"+classType+"' does not exist");
            return false;
        }
    }
    
    @Override
    public Properties getParameter(){
        String classType = (String) properties.get("AlgorithmClass").getValue().iterator().next();
        if(map.containsKey(classType)){
            return map.get(classType).getParameter();
        }else{
            return properties;
        }
    }

    @Override
    public Parameter getClassParameter() {
        return properties.get("AlgorithmClass");
    }
}

