/*
 * Created 25-1-08
 * Derived from Prefuse GraphView Demo (copyright jheer - BSD license)
 * Modifications copyright Daniel McEnnis, see license.txt
 */
package org.mcennis.graphrat.graphdisplay;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JSplitPane;

import prefuse.Display;
import prefuse.Visualization;
import prefuse.action.ActionList;
import prefuse.action.RepaintAction;
import prefuse.action.assignment.ColorAction;
import prefuse.action.assignment.DataColorAction;
import prefuse.action.layout.graph.ForceDirectedLayout;
import prefuse.activity.Activity;
import prefuse.controls.DragControl;
import prefuse.controls.FocusControl;
import prefuse.controls.NeighborHighlightControl;
import prefuse.controls.PanControl;
import prefuse.controls.WheelZoomControl;
import prefuse.controls.ZoomControl;
import prefuse.controls.ZoomToFitControl;
import prefuse.data.Edge;
import prefuse.data.Node;
import prefuse.data.Tuple;
import prefuse.data.event.TupleSetListener;
import prefuse.data.tuple.TupleSet;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.LabelRenderer;
import prefuse.util.ColorLib;
import prefuse.util.ui.JForcePanel;
import prefuse.visual.VisualGraph;
import prefuse.visual.VisualItem;

/**
 * Class for showing a graph in a window
 * @author <a href="http://jheer.org">jeffrey heer</a>
 * @author Daniel McEnnis
 */
public class VisualizationPanel extends JFrame {

    private static final String graph = "graph";
    private static final String nodes = "graph.nodes";
    private static final String edges = "graph.edges";
    boolean running;
    Visualization m_vis;
    prefuse.data.Graph g;
    JSplitPane split;
    ActorPanel actorPanel;
    LinkPanel linkPanel;
    ActionList animate;

    /**
     * Create a new Visualization Panel
     * @param g graph to display
     * @param label type of label to use
     * @param nodeColorPallette order of the colors for modes
     * @param edgeColorPallette order of colors for relations
     */
    public VisualizationPanel(prefuse.data.Graph g, String label, int[] nodeColorPallette, int[] edgeColorPallette) {
        super("MRGraph Visualization Application");
        Container frame = this.getContentPane();
        frame.setLayout(new BorderLayout());

        // create a new, empty visualization for our data
        m_vis = new Visualization();

        this.g = g;

        // --------------------------------------------------------------------
        // set up the renderers

        LabelRenderer tr = new LabelRenderer();
        tr.setRoundedCorner(8, 8);
        m_vis.setRendererFactory(new DefaultRendererFactory(tr));
        DefaultRendererFactory drf = (DefaultRendererFactory) m_vis.getRendererFactory();
        ((LabelRenderer) drf.getDefaultRenderer()).setTextField(label);

        // update graph
        m_vis.removeGroup(graph);
        VisualGraph vg = m_vis.addGraph(graph, g);
//        m_vis.setValue(edges, null, VisualItem.INTERACTIVE, Boolean.FALSE);
        VisualItem f = (VisualItem) vg.getNode(0);
        m_vis.getGroup(Visualization.FOCUS_ITEMS).setTuple(f);
        f.setFixed(false);

        // fix selected focus nodes
        TupleSet focusGroup = m_vis.getGroup(Visualization.FOCUS_ITEMS);
        focusGroup.addTupleSetListener(new TupleSetListener() {

            public void tupleSetChanged(TupleSet ts, Tuple[] add, Tuple[] rem) {
                for (int i = 0; i < rem.length; ++i) {
                    ((VisualItem) rem[i]).setFixed(false);
                }
                for (int i = 0; i < add.length; ++i) {
                    ((VisualItem) add[i]).setFixed(false);
                    ((VisualItem) add[i]).setFixed(true);
                    if(add[i] instanceof Node){
                        split.setRightComponent(actorPanel);
                        actorPanel.loadPanel((VisualItem)add[i]);
//                        split.invalidate();
//                        split.repaint();
                    }else if(add[i] instanceof Edge){
                        split.setRightComponent(linkPanel);
                        linkPanel.loadLink((VisualItem)add[i]);
//                        split.invalidate();
//                        split.repaint();
                    }
                }
                if (ts.getTupleCount() == 0) {
                    ts.addTuple(rem[0]);
                    ((VisualItem) rem[0]).setFixed(false);
                }
                m_vis.run("draw");
            }
        });
        ActionList draw = new ActionList();
        DataColorAction fillNode = new DataColorAction(nodes, "mode", prefuse.Constants.NOMINAL, VisualItem.FILLCOLOR, nodeColorPallette);
        fillNode.add(VisualItem.FIXED, ColorLib.rgb(255, 100, 100));
        fillNode.add(VisualItem.HIGHLIGHT, ColorLib.rgb(255, 200, 125));
        draw.add(fillNode);
        DataColorAction fillEdge = new DataColorAction(edges,"relation",prefuse.Constants.NOMINAL,VisualItem.FILLCOLOR, edgeColorPallette);
        draw.add(fillEdge);
        draw.add(new ColorAction(nodes, VisualItem.STROKECOLOR, 0));
        draw.add(new ColorAction(nodes, VisualItem.TEXTCOLOR, ColorLib.rgb(0,0,0)));
//        draw.add(new ColorAction(edges, VisualItem.FILLCOLOR, ColorLib.gray(200)));
        draw.add(new DataColorAction(edges,"relation",prefuse.Constants.NOMINAL,VisualItem.STROKECOLOR, edgeColorPallette));
        
        animate = new ActionList(Activity.INFINITY);
        animate.add(new ForceDirectedLayout(graph));
        animate.add(fillNode);
        animate.add(new RepaintAction());
                // finally, we register our ActionList with the Visualization.
        // we can later execute our Actions by invoking a method on our
        // Visualization, using the name we've chosen below.
        m_vis.putAction("draw", draw);
        m_vis.putAction("layout", animate);

        m_vis.runAfter("draw", "layout");
        
        
        // --------------------------------------------------------------------
        // set up a display to show the visualization
        
        Display display = new Display(m_vis);
        display.setSize(700,700);
        display.pan(350, 350);
        display.setForeground(Color.GRAY);
        display.setBackground(Color.WHITE);
        
        // main display controls
        display.addControlListener(new FocusControl(1));
        display.addControlListener(new DragControl());
        display.addControlListener(new PanControl());
        display.addControlListener(new ZoomControl());
        display.addControlListener(new WheelZoomControl());
        display.addControlListener(new ZoomToFitControl());
        display.addControlListener(new NeighborHighlightControl());

        // overview display
//        Display overview = new Display(vis);
//        overview.setSize(290,290);
//        overview.addItemBoundsListener(new FitOverviewListener());
        
        display.setForeground(Color.GRAY);
        display.setBackground(Color.WHITE);

//        ForceSimulator fsim = ((ForceDirectedLayout)animate.get(0)).getForceSimulator();
//        JForcePanel fpanel = new JForcePanel(fsim);

        //fpanel.add(opanel);
         actorPanel = new ActorPanel(m_vis.getVisualItem(graph, vg.getNode(0)));
        linkPanel = new LinkPanel(m_vis.getVisualItem(graph, vg.getEdge(0)));
        actorPanel.setFPanelListener(new ForceLayoutListener());
        linkPanel.setFPanelListener(new ForceLayoutListener());
        actorPanel.setToggleListener(new ToggleDisplayListener());
        linkPanel.setToggleListener(new ToggleDisplayListener());
//        fpanel.add(actorPanel);


        split = new JSplitPane();
        split.setLeftComponent(display);
        split.setRightComponent(actorPanel);
        split.setOneTouchExpandable(true);
        split.setContinuousLayout(false);
        split.setDividerLocation(700);
        
        // now we run our action list
        running = true;
        m_vis.run("draw");
        actorPanel.loadPanel(m_vis.getVisualItem(graph, vg.getNode(0)));
        frame.add(split);
        this.pack();
        this.setVisible(true);
//                this.addWindowListener(new WindowAdapter() {
//            @Override
//            public void windowActivated(WindowEvent e) {
//                m_vis.run("layout");
//            }
//            @Override
//            public void windowDeactivated(WindowEvent e) {
//                m_vis.cancel("layout");
//            }
//        });
        
//        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    /**
     * Listener class for displaying a frame which controls visualization parameters.
     */
    public class ForceLayoutListener implements ActionListener{

        /**
         * Display the frame containing visualization parameters.
         * @param e
         */
        public void actionPerformed(ActionEvent e) {
            (JForcePanel.showForcePanel(((ForceDirectedLayout)animate.get(0)).getForceSimulator())).setVisible(true);
        }
    }
    
    /**
     * Listener that toggles animation in the graph display
     */
    public class ToggleDisplayListener implements ActionListener{
        /**
         * Toggle animation in the graph display
         * @param e
         */
        public void actionPerformed(ActionEvent e){
            if(running){
                m_vis.cancel("layout");
                running = false;
            }else{
                m_vis.run("layout");
                running = true;
            }
        }
    }
}
