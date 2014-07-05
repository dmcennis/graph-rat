/*

 * Created 26-1-08

 * Copyright Daniel McEnnis, see license.txt

 */

package nz.ac.waikato.mcennis.rat.graphdisplay;



import java.awt.Color;

import java.awt.Dimension;

import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;

import java.util.HashMap;

import java.util.Iterator;

import javax.swing.BorderFactory;

import javax.swing.Box;

import javax.swing.BoxLayout;

import javax.swing.JButton;

import javax.swing.JFrame;

import javax.swing.JLabel;

import javax.swing.JPanel;

import javax.swing.JScrollPane;

import nz.ac.waikato.mcennis.rat.graph.actor.Actor;

import prefuse.data.Node;

import prefuse.util.ColorLib;

import prefuse.visual.VisualItem;



/**

 * JPanel object designed to be embedded next to a Prefuse graph display object 

 * for the purpose of describing actors selected in the graph.

 * @author Daniel McEnnis

 */

public class ActorPanel extends JScrollPane {



    JPanel innerPanel;

    

    JButton visualizationButton;

    JButton toggleAnimation;

    

    HashMap<String, JFrame> bigEntries = new HashMap<String,JFrame>(); 



    /**

     * Create this panel using the given Prefuse actor as the base.  Assumes a

     * given structure of the contents.  The column "actor" gives the graph-RAT 

     * actor.  All other columns are the default columns of a Prefuse actor (basically

     * display information).

     * @param actor prefuse actor to display in this panel

     */

    public ActorPanel(VisualItem actor) {

        

        this.setMinimumSize(new Dimension(300, 700));

        this.setPreferredSize(new Dimension(300, 700));

        innerPanel = new JPanel();

        innerPanel.setMinimumSize(new Dimension(300, 700));

        innerPanel.setPreferredSize(new Dimension(300, 700));

        this.setViewportView(innerPanel);

        innerPanel.setBackground(Color.WHITE);

        toggleAnimation = new JButton("Toggle Animation");

        toggleAnimation.setPreferredSize(new Dimension(250,30));

        toggleAnimation.setMaximumSize(new Dimension(250,30));

        loadPanel(actor);

    }



    /**

     * Load the contents of the panel from the given Prefuse object.  See class 

     * description for structure of data in the object.

     * @param actorVisual Prefuse actor containing graph-RAT info.

     */

    public void loadPanel(VisualItem actorVisual) {

        Actor actor = (Actor)actorVisual.get("actor");

        Iterator<String> key = bigEntries.keySet().iterator();

        while(key.hasNext()){

            String keyString = key.next();

            bigEntries.get(keyString).setVisible(false);

        }

        bigEntries.clear();

        innerPanel.removeAll();

        innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));

//        innerPanel.setBackground(ColorLib.getColor(-26215));



        Box forceSettings = new Box(BoxLayout.Y_AXIS);

        forceSettings.setBorder(BorderFactory.createTitledBorder("Display Controls"));

        visualizationButton = new JButton("Visualization Parameters");

        visualizationButton.setPreferredSize(new Dimension(250,30));

        visualizationButton.setMaximumSize(new Dimension(250,30));

        forceSettings.add(visualizationButton);

        forceSettings.add(Box.createVerticalStrut(10));

        forceSettings.add(toggleAnimation);

        innerPanel.add(forceSettings);

        

        Box mode = new Box(BoxLayout.Y_AXIS);

        mode.setBorder(BorderFactory.createTitledBorder("Actor Mode"));

        JLabel modeLabel = new JLabel(actor.getMode());

        modeLabel.setPreferredSize(new Dimension(250, 30));

        modeLabel.setMaximumSize(new Dimension(250, 30));

        modeLabel.setOpaque(true);

        modeLabel.setBackground(ColorLib.getColor(actorVisual.getFillColor()));

        mode.add(modeLabel);



        innerPanel.add(mode);



        Box id = new Box(BoxLayout.Y_AXIS);

        id.setBorder(BorderFactory.createTitledBorder("Actor ID"));

        JLabel idLabel = new JLabel(actor.getID());

        idLabel.setPreferredSize(new Dimension(250, 30));

        idLabel.setMaximumSize(new Dimension(250, 30));

        idLabel.setOpaque(true);

        idLabel.setBackground(ColorLib.getColor(actorVisual.getFillColor()));

        id.add(idLabel);



//        id.setPreferredSize(new Dimension(300,30));

//        id.setMinimumSize(new Dimension(300,30));

        innerPanel.add(id);



        Property[] propertyList = actor.getProperty().toArray(new Property[]{});

        if (propertyList.length>0) {

            for (int i = 0; i < propertyList.length; ++i) {

                Box property = new Box(BoxLayout.Y_AXIS);

                property.setBorder(BorderFactory.createTitledBorder(propertyList[i].getType()));

                if (propertyList[i].getValue().get(0).toString().length() > 38) {

                    bigEntries.put(propertyList[i].getType(), new DisplayLargeStringPropertyFrame(propertyList[i]));

                    JButton bigEntry = new JButton(propertyList[i].getValue().get(0).toString().substring(0, 35)+"...");

                    bigEntry.setActionCommand(propertyList[i].getType());

                    bigEntry.addActionListener(new PropertyButtonListener());

                    bigEntry.setPreferredSize(new Dimension(250,30));

                    bigEntry.setMaximumSize(new Dimension(250,30));

                    property.add(bigEntry);

                } else {



//                    Box propertyClass = new Box(BoxLayout.Y_AXIS);

//                    propertyClass.setBorder(BorderFactory.createTitledBorder("Class"));

//                    JLabel propertyClassLabel = new JLabel(propertyList[i].getPropertyClass().getName());

//                    propertyClass.add(propertyClassLabel);

//                    property.add(propertyClass);



                    Object[] values = propertyList[i].getValue().toArray();

                    if (values != null) {

                        for (int j = 0; j < values.length; ++j) {

                            JLabel line = new JLabel(values[j].toString());

                            line.setPreferredSize(new Dimension(250,20));

                            line.setMaximumSize(new Dimension(250,20));

                            line.setToolTipText("Class: "+propertyList[i].getPropertyClass().getName());

                            property.add(line);

                        }

                    }

                }

                innerPanel.add(property);

            }

        }

        innerPanel.validate();

        innerPanel.repaint();

    }

    

    /**

     * Allow the caller to register an action to be performed when the button creating

     * the visualization parameter frame is pressed.

     * @param listener action to be performed.

     */

    public void setFPanelListener(ActionListener listener){

        visualizationButton.addActionListener(listener);

    }

    

    /**

     * Allow the caller to register an action to be performed when the button enabling

     * or disabling iterations of the graph display algorithm is pressed.

     * @param listener action to be performed

     */

    public void setToggleListener(ActionListener listener){

        toggleAnimation.addActionListener(listener);

    }

    

    /**

     * Inner class for loading and displaying a display box for properties that

     * can not be fully displayed in the ActorPanel.

     */

    public class PropertyButtonListener implements ActionListener{



        /**

         * Action for displaying a property value when called

         * @param e event to be processed

         */

        public void actionPerformed(ActionEvent e) {

            if(bigEntries.containsKey(e.getActionCommand())){

                JFrame base = bigEntries.get(e.getActionCommand());

                if(!base.isVisible()){

                    base.setVisible(true);

                }

            }

        }

        

    }

}

