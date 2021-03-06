/**

 * Initial copyright jheer - BSD license

 * Modifications copyright Daniel McEnnis, see license.txt

 */

/*
 *   This file is part of GraphRAT.
 *
 *   GraphRAT is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   GraphRAT is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with GraphRAT.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.mcennis.graphrat.graphdisplay;



import java.awt.BorderLayout;

import java.awt.Color;

import java.awt.Component;

import java.awt.Dimension;

import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;



import java.awt.geom.Rectangle2D;



import java.util.HashMap;
import javax.swing.AbstractAction;

import javax.swing.BorderFactory;

import javax.swing.Box;

import javax.swing.BoxLayout;

import javax.swing.JButton;

import javax.swing.JDialog;

import javax.swing.JFrame;

import javax.swing.JLabel;

import javax.swing.JList;

import javax.swing.JPanel;

import javax.swing.JScrollPane;

import javax.swing.JSplitPane;

import javax.swing.KeyStroke;

import javax.swing.ListSelectionModel;

import javax.swing.event.ChangeEvent;

import javax.swing.event.ChangeListener;

import javax.swing.event.ListSelectionEvent;

import javax.swing.event.ListSelectionListener;



import org.mcennis.graphrat.dataAquisition.LoadBibliographyXML;

import org.mcennis.graphrat.graph.MemGraph;

import org.mcennis.graphrat.actor.Actor;

import org.mcennis.graphrat.link.Link;

import org.dynamicfactory.descriptors.Properties;
import prefuse.Display;

import prefuse.Visualization;

import prefuse.action.Action;

import prefuse.action.ActionList;

import prefuse.action.RepaintAction;

import prefuse.action.assignment.ColorAction;

import prefuse.action.assignment.DataColorAction;

import prefuse.action.filter.GraphDistanceFilter;

import prefuse.action.layout.graph.ForceDirectedLayout;

import prefuse.activity.Activity;

import prefuse.controls.DragControl;

import prefuse.controls.FocusControl;

import prefuse.controls.NeighborHighlightControl;

import prefuse.controls.PanControl;

import prefuse.controls.WheelZoomControl;

import prefuse.controls.ZoomControl;

import prefuse.controls.ZoomToFitControl;

import prefuse.data.Graph;

import prefuse.data.Node;

import prefuse.data.Schema;

import prefuse.data.Table;

import prefuse.data.Tuple;

import prefuse.data.event.TupleSetListener;

import prefuse.data.io.GraphMLReader;

import prefuse.data.tuple.TupleSet;

import prefuse.render.DefaultRendererFactory;

import prefuse.render.LabelRenderer;

import prefuse.util.ColorLib;

import prefuse.util.GraphicsLib;

import prefuse.util.display.DisplayLib;

import prefuse.util.display.ItemBoundsListener;

import prefuse.util.force.ForceSimulator;

import prefuse.util.io.IOLib;

import prefuse.util.ui.JForcePanel;

import prefuse.util.ui.JValueSlider;

import prefuse.util.ui.UILib;

import prefuse.visual.VisualGraph;

import prefuse.visual.VisualItem;



/**

 * Altered for RAT by Daniel McEnnis

 * @author <a href="http://jheer.org">jeffrey heer</a>

 */

public class PrefuseGraphView extends JPanel {



    private static org.mcennis.graphrat.graph.Graph bibGraph;

    private static final String graph = "graph";

    private static final String nodes = "graph.nodes";

    private static final String edges = "graph.edges";



    private Visualization m_vis;

    private boolean running=true;

    

    public PrefuseGraphView(Graph g, String label, boolean dataBasedColor, int[] colorPallette) {

    	super(new BorderLayout());

    	

        // create a new, empty visualization for our data

        m_vis = new Visualization();

        

        // --------------------------------------------------------------------

        // set up the renderers

        

        LabelRenderer tr = new LabelRenderer();

        tr.setRoundedCorner(8, 8);

        m_vis.setRendererFactory(new DefaultRendererFactory(tr));



        // --------------------------------------------------------------------

        // register the data with a visualization

        

        // adds graph to visualization and sets renderer label field

        setGraph(g, label);

        

        // fix selected focus nodes

        TupleSet focusGroup = m_vis.getGroup(Visualization.FOCUS_ITEMS); 

        focusGroup.addTupleSetListener(new TupleSetListener() {

            public void tupleSetChanged(TupleSet ts, Tuple[] add, Tuple[] rem)

            {

                for ( int i=0; i<rem.length; ++i )

                    ((VisualItem)rem[i]).setFixed(false);

                for ( int i=0; i<add.length; ++i ) {

                    ((VisualItem)add[i]).setFixed(false);

                    ((VisualItem)add[i]).setFixed(true);

                }

                if ( ts.getTupleCount() == 0 ) {

                    ts.addTuple(rem[0]);

                    ((VisualItem)rem[0]).setFixed(false);

                }

                m_vis.run("draw");

            }

        });

        

        

        

        // --------------------------------------------------------------------

        // create actions to process the visual data



        int hops = 30;

        final GraphDistanceFilter filter = new GraphDistanceFilter(graph, hops);



        Action fill = null;

       

        if(dataBasedColor){

            fill = new DataColorAction(nodes,"subgraph",prefuse.Constants.NOMINAL,VisualItem.FILLCOLOR,colorPallette);

            ((DataColorAction)fill).add(VisualItem.FIXED, ColorLib.rgb(255,100,100));

            ((DataColorAction)fill).add(VisualItem.HIGHLIGHT, ColorLib.rgb(255,200,125));

        }else{

            fill = new ColorAction(nodes, 

                VisualItem.FILLCOLOR, ColorLib.rgb(200,200,255));

            ((ColorAction)fill).add(VisualItem.FIXED, ColorLib.rgb(255,100,100));

            ((ColorAction)fill).add(VisualItem.HIGHLIGHT, ColorLib.rgb(255,200,125));

         }

        

        ActionList draw = new ActionList();

        draw.add(filter);

        draw.add(fill);

         draw.add(new ColorAction(nodes, VisualItem.STROKECOLOR, 0));

        draw.add(new ColorAction(nodes, VisualItem.TEXTCOLOR, ColorLib.rgb(0,0,0)));

        draw.add(new ColorAction(edges, VisualItem.FILLCOLOR, ColorLib.gray(200)));

        draw.add(new ColorAction(edges, VisualItem.STROKECOLOR, ColorLib.gray(200)));

        

        ActionList animate = new ActionList(Activity.INFINITY);

        animate.add(new ForceDirectedLayout(graph));

        animate.add(fill);

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

        

        // --------------------------------------------------------------------        

        // launch the visualization

        

        // create a panel for editing force values

        ForceSimulator fsim = ((ForceDirectedLayout)animate.get(0)).getForceSimulator();

        JForcePanel fpanel = new JForcePanel(fsim);

        

//        JPanel opanel = new JPanel();

//        opanel.setBorder(BorderFactory.createTitledBorder("Overview"));

//        opanel.setBackground(Color.WHITE);

//        opanel.add(overview);

        

        final JValueSlider slider = new JValueSlider("Distance", 0, hops, hops);

        slider.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {

                filter.setDistance(slider.getValue().intValue());

                m_vis.run("draw");

            }

        });

        slider.setBackground(Color.WHITE);

        slider.setPreferredSize(new Dimension(300,30));

        slider.setMaximumSize(new Dimension(300,30));

        

        Box cf = new Box(BoxLayout.Y_AXIS);

        cf.add(slider);

        cf.setBorder(BorderFactory.createTitledBorder("Connectivity Filter"));

        fpanel.add(cf);



        //fpanel.add(opanel);

        

        Box toggle = new Box(BoxLayout.Y_AXIS);

        JButton toggleAnimation = new JButton("Toggle Animation");

        toggleAnimation.setPreferredSize(new Dimension(250,30));

        toggleAnimation.setMaximumSize(new Dimension(250,30));

        toggleAnimation.addActionListener(new ToggleDisplayListener());

        toggle.add(toggleAnimation);

        toggle.setBorder(BorderFactory.createTitledBorder("Animation Control"));

        fpanel.add(Box.createVerticalStrut(400));

        fpanel.add(toggle);



 //       fpanel.add(Box.createVerticalGlue());

        fpanel.setPreferredSize(new Dimension(300,700));

        fpanel.setMaximumSize(new Dimension(300,700));



        // create a new JSplitPane to present the interface

        JSplitPane split = new JSplitPane();

        split.setLeftComponent(display);

        split.setRightComponent(fpanel);

        split.setOneTouchExpandable(true);

        split.setContinuousLayout(false);

        split.setDividerLocation(700);

        

        // now we run our action list

        m_vis.run("draw");

        this.setMaximumSize(new Dimension(1000,700));

        add(split);

    }

    

    public void setGraph(Graph g, String label) {

        // update labeling

        DefaultRendererFactory drf = (DefaultRendererFactory)

                                                m_vis.getRendererFactory();

        ((LabelRenderer)drf.getDefaultRenderer()).setTextField(label);

        

        // update graph

        m_vis.removeGroup(graph);

        VisualGraph vg = m_vis.addGraph(graph, g);

        m_vis.setValue(edges, null, VisualItem.INTERACTIVE, Boolean.FALSE);

        VisualItem f = (VisualItem)vg.getNode(0);

        m_vis.getGroup(Visualization.FOCUS_ITEMS).setTuple(f);

        f.setFixed(false);

    }

    

    // ------------------------------------------------------------------------

    // Main and demo methods

    

    public static void main(String[] args) {

        UILib.setPlatformLookAndFeel();

        

        // create graphview

        String datafile = null;

        String label = "label";

//        if ( args.length > 1 ) {

//            datafile = args[0];

//            label = args[1];

 //       }

        LoadBibliographyXML source = new LoadBibliographyXML();

        Properties props = source.getParameter();

        props.add("FileLocation","c:\\Users\\mcennis\\Documents\\3_clusters.xml");

        source.init(props);

        source.set(new MemGraph());

        source.start();

        bibGraph = source.get();

        JFrame frame = demo(datafile, label);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    

    public static JFrame demo() {

        return demo((String)null, "label");

    }

    

    public static JFrame demo(String datafile, String label) {

        Graph g = null;

        if ( datafile == null ) {

//            g = GraphLib.getGrid(15,15);

            g = getBibGraph();

            label = "label";

        } else {

            try {

                g = new GraphMLReader().readGraph(datafile);

            } catch ( Exception e ) {

                e.printStackTrace();

                System.exit(1);

            }

        }

        return demo(g, label,false,null);

    }

    

    public static JFrame demo(Graph g, String label, boolean type, int[] colorPallette) {

        final PrefuseGraphView view = new PrefuseGraphView(g, label,type,colorPallette);

        

        // set up menu

//        JMenu dataMenu = new JMenu("Data");

//        dataMenu.add(new OpenGraphAction(view));

//        dataMenu.add(new GraphMenuAction("RAT Data","ctrl 6",view) {

//            protected Graph getGraph() {

//                return getBibGraph();

//            }

//        });

//        JMenuBar menubar = new JMenuBar();

//        menubar.add(dataMenu);

//        

//        // launch window

        JFrame frame = new JFrame("p r e f u s e  |  g r a p h v i e w");

//        frame.setJMenuBar(menubar);

        frame.setContentPane(view);

        frame.pack();

        frame.setVisible(true);

        

/*        frame.addWindowListener(new WindowAdapter() {

            @Override

            public void windowActivated(WindowEvent e) {

                view.m_vis.run("layout");

            }

            @Override

            public void windowDeactivated(WindowEvent e) {

                view.m_vis.cancel("layout");

            }

        });

*/        

        return frame;

    }

    

    

    // ------------------------------------------------------------------------

    

    /**

     * Swing menu action that loads a graph into the graph viewer.

     */

    public abstract static class GraphMenuAction extends AbstractAction {

        private PrefuseGraphView m_view;

        public GraphMenuAction(String name, String accel, PrefuseGraphView view) {

            m_view = view;

            this.putValue(AbstractAction.NAME, name);

            this.putValue(AbstractAction.ACCELERATOR_KEY,

                          KeyStroke.getKeyStroke(accel));

        }

        public void actionPerformed(ActionEvent e) {

            m_view.setGraph(getGraph(), "label");

        }

        protected abstract Graph getGraph();

    }

    

    public static class OpenGraphAction extends AbstractAction {

        private PrefuseGraphView m_view;



        public OpenGraphAction(PrefuseGraphView view) {

            m_view = view;

            this.putValue(AbstractAction.NAME, "Open File...");

            this.putValue(AbstractAction.ACCELERATOR_KEY,

                          KeyStroke.getKeyStroke("ctrl O"));

        }

        public void actionPerformed(ActionEvent e) {

            Graph g = IOLib.getGraphFile(m_view);

            if ( g == null ) return;

            String label = getLabel(m_view, g);

            if ( label != null ) {

                m_view.setGraph(g, label);

            }

        }

        public static String getLabel(Component c, Graph g) {

            // get the column names

            Table t = g.getNodeTable();

            int  cc = t.getColumnCount();

            String[] names = new String[cc];

            for ( int i=0; i<cc; ++i )

                names[i] = t.getColumnName(i);

            

            // where to store the result

            final String[] label = new String[1];



            // -- build the dialog -----

            // we need to get the enclosing frame first

            while ( c != null && !(c instanceof JFrame) ) {

                c = c.getParent();

            }

            final JDialog dialog = new JDialog(

                    (JFrame)c, "Choose Label Field", true);

            

            // create the ok/cancel buttons

            final JButton ok = new JButton("OK");

            ok.setEnabled(false);

            ok.addActionListener(new ActionListener() {

               public void actionPerformed(ActionEvent e) {

                   dialog.setVisible(false);

               }

            });

            JButton cancel = new JButton("Cancel");

            cancel.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {

                    label[0] = null;

                    dialog.setVisible(false);

                }

            });

            

            // build the selection list

            final JList list = new JList(names);

            list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            list.getSelectionModel().addListSelectionListener(

            new ListSelectionListener() {

                public void valueChanged(ListSelectionEvent e) {

                    int sel = list.getSelectedIndex(); 

                    if ( sel >= 0 ) {

                        ok.setEnabled(true);

                        label[0] = (String)list.getModel().getElementAt(sel);

                    } else {

                        ok.setEnabled(false);

                        label[0] = null;

                    }

                }

            });

            JScrollPane scrollList = new JScrollPane(list);

            

            JLabel title = new JLabel("Choose a field to use for node labels:");

            

            // layout the buttons

            Box bbox = new Box(BoxLayout.X_AXIS);

            bbox.add(Box.createHorizontalStrut(5));

            bbox.add(Box.createHorizontalGlue());

            bbox.add(ok);

            bbox.add(Box.createHorizontalStrut(5));

            bbox.add(cancel);

            bbox.add(Box.createHorizontalStrut(5));

            

            // put everything into a panel

            JPanel panel = new JPanel(new BorderLayout());

            panel.add(title, BorderLayout.NORTH);

            panel.add(scrollList, BorderLayout.CENTER);

            panel.add(bbox, BorderLayout.SOUTH);

            panel.setBorder(BorderFactory.createEmptyBorder(5,2,2,2));

            

            // show the dialog

            dialog.setContentPane(panel);

            dialog.pack();

            dialog.setLocationRelativeTo(c);

            dialog.setVisible(true);

            dialog.dispose();

            

            // return the label field selection

            return label[0];

        }

    }

    

    public static class FitOverviewListener implements ItemBoundsListener {

        private Rectangle2D m_bounds = new Rectangle2D.Double();

        private Rectangle2D m_temp = new Rectangle2D.Double();

        private double m_d = 15;

        public void itemBoundsChanged(Display d) {

            d.getItemBounds(m_temp);

            GraphicsLib.expand(m_temp, 25/d.getScale());

            

            double dd = m_d/d.getScale();

            double xd = Math.abs(m_temp.getMinX()-m_bounds.getMinX());

            double yd = Math.abs(m_temp.getMinY()-m_bounds.getMinY());

            double wd = Math.abs(m_temp.getWidth()-m_bounds.getWidth());

            double hd = Math.abs(m_temp.getHeight()-m_bounds.getHeight());

            if ( xd>dd || yd>dd || wd>dd || hd>dd ) {

                m_bounds.setFrame(m_temp);

                DisplayLib.fitViewToBounds(d, m_bounds, 0);

            }

        }

    }

    

    public static Graph getBibGraph(){

        Graph g = new Graph(true);

        Schema schema = new Schema();

        schema.addColumn("label", String.class, "");

        g.getNodeTable().addColumns(schema);

        Actor[] a = bibGraph.getActor("Paper").toArray(new Actor[]{});

        Node[] n = new Node[a.length];

        HashMap<Actor,Node> map = new HashMap<Actor,Node>();

        for(int i=0;i<a.length;++i){

            n[i] = g.addNode();

            n[i].setString("label", a[i].getID());

            map.put(a[i],n[i]);

        }

        Link[] l = bibGraph.getLink("References").toArray(new Link[]{});

        for(int i=0;i<l.length;++i){

            Actor s = l[i].getSource();

            Actor d = l[i].getDestination();

            if((s != null)&&(d != null)){

                g.addEdge(map.get(s), map.get(d));

            }else{

                if(s == null){

                    System.out.println("Source actor is null");

                }

                if(d == null){

                    System.out.println("Destination actor is null");

                }

            }

        }

        return g;

    }

    

    public class ToggleDisplayListener implements ActionListener{

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



} // end of class GraphView

