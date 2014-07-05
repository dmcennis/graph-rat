/*
 * Created 26-1-08
 * 
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.graphdisplay;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import nz.ac.waikato.mcennis.rat.graph.link.Link;
import prefuse.util.ColorLib;
import prefuse.visual.VisualItem;

/**
 * JPanel for displaying the contents of a link. Cretaed from Prefuse link objects
 * where the graph-RAT link object is stored in the 'link' column.
 * @author Daniel McEnnis
 */
public class LinkPanel extends JScrollPane {

    JPanel innerPanel;
    JButton visualizationButton;
    JButton toggleAnimation;
    HashMap<String, JFrame> bigEntries = new HashMap<String, JFrame>();

    /**
     * Creates a new panel from the given prefuse link object.
     * @param l prefuse link object to initialize the panel with
     */
    public LinkPanel(VisualItem l) {
        this.setMinimumSize(new Dimension(300, 700));
        this.setPreferredSize(new Dimension(300, 700));
        innerPanel = new JPanel();
        innerPanel.setMinimumSize(new Dimension(300, 700));
        innerPanel.setPreferredSize(new Dimension(300, 700));
        innerPanel.setBackground(Color.WHITE);
        toggleAnimation = new JButton("Toggle Animation");
        toggleAnimation.setPreferredSize(new Dimension(250,30));
        toggleAnimation.setMaximumSize(new Dimension(250,30));
        this.setViewportView(innerPanel);
        loadLink(l);
    }

    /**
     * Loads the given Prefuse link into the panel.
     * @param linkItem link to load
     */
    public void loadLink(VisualItem linkItem) {
        Link link = (Link)linkItem.get("link");
        innerPanel.removeAll();
        innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));

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
        mode.setBorder(BorderFactory.createTitledBorder("Link Mode"));
        JLabel typeLabel = new JLabel(link.getType());
        typeLabel.setPreferredSize(new Dimension(250,30));
        typeLabel.setMaximumSize(new Dimension(250,30));
        typeLabel.setOpaque(true);
        typeLabel.setBackground(ColorLib.getColor(linkItem.getFillColor()));
        mode.add(typeLabel);
        innerPanel.add(mode);


        Box source = new Box(BoxLayout.Y_AXIS);
        source.setBorder(BorderFactory.createTitledBorder("Link Source"));

        Box sourceType = new Box(BoxLayout.Y_AXIS);
        sourceType.setBorder(BorderFactory.createTitledBorder("Mode"));
        JLabel sourceTypeLabel = new JLabel(link.getSource().getType());
        sourceTypeLabel.setPreferredSize(new Dimension(240, 30));
        sourceTypeLabel.setMaximumSize(new Dimension(240, 30));
        sourceType.add(sourceTypeLabel);
        source.add(sourceType);

        Box sourceID = new Box(BoxLayout.Y_AXIS);
        sourceID.setBorder(BorderFactory.createTitledBorder("ID"));
        JLabel sourceIDLabel = new JLabel(link.getSource().getID());
        sourceIDLabel.setPreferredSize(new Dimension(240, 30));
        sourceIDLabel.setMaximumSize(new Dimension(240, 30));
        sourceID.add(sourceIDLabel);
        source.add(sourceID);

        innerPanel.add(source);



        Box destination = new Box(BoxLayout.Y_AXIS);
        destination.setBorder(BorderFactory.createTitledBorder("Link Destination"));

        Box destinationType = new Box(BoxLayout.Y_AXIS);
        destinationType.setBorder(BorderFactory.createTitledBorder("Mode"));
        JLabel destinationTypeLabel = new JLabel(link.getDestination().getType());
        destinationTypeLabel.setPreferredSize(new Dimension(240, 30));
        destinationTypeLabel.setMaximumSize(new Dimension(240, 30));
        destinationType.add(destinationTypeLabel);
        destination.add(destinationType);

        Box destinationID = new Box(BoxLayout.Y_AXIS);
        destinationID.setBorder(BorderFactory.createTitledBorder("ID"));
        JLabel destinationIDLabel = new JLabel(link.getDestination().getID());
        destinationIDLabel.setPreferredSize(new Dimension(240, 30));
        destinationIDLabel.setMaximumSize(new Dimension(240, 30));
        destinationID.add(destinationIDLabel);
        destination.add(destinationID);

        innerPanel.add(destination);

        Box strength = new Box(BoxLayout.Y_AXIS);
        strength.setBorder(BorderFactory.createTitledBorder("Link Strength"));
        JLabel strengthLabel = new JLabel(Double.toString(link.getStrength()));
        strengthLabel.setPreferredSize(new Dimension(250,30));
        strengthLabel.setMaximumSize(new Dimension(250,30));
        strength.add(strengthLabel);
        innerPanel.add(strength);
        
        Property[] propertyList = link.getProperty();
        if (propertyList != null) {
            for (int i = 0; i < propertyList.length; ++i) {
                Box property = new Box(BoxLayout.Y_AXIS);
                property.setBorder(BorderFactory.createTitledBorder(propertyList[i].getType()));
                if (propertyList[i].getValue()[0].toString().length() > 30) {
                    bigEntries.put(propertyList[i].getType(), new DisplayLargeStringPropertyFrame(propertyList[i]));
                    JButton bigEntry = new JButton(propertyList[i].getType());
                    bigEntry.addActionListener(new PropertyButtonListener());
                    bigEntry.setPreferredSize(new Dimension(250, 30));
                    bigEntry.setMaximumSize(new Dimension(250, 30));
                    property.add(bigEntry);
                } else {

                    Box propertyClass = new Box(BoxLayout.Y_AXIS);
                    propertyClass.setBorder(BorderFactory.createTitledBorder("Class"));
                    JLabel propertyClassLabel = new JLabel(propertyList[i].getPropertyClass().getName());
                    propertyClass.add(propertyClassLabel);
                    property.add(propertyClass);

                    Object[] values = propertyList[i].getValue();
                    if (values != null) {
                        for (int j = 0; j < values.length; ++j) {
                            JLabel line = new JLabel(values[j].toString());
                            line.setPreferredSize(new Dimension(250, 20));
                            line.setMaximumSize(new Dimension(250, 20));
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
     * can not be fully displayed in the LinkPanel.
     */
    public class PropertyButtonListener implements ActionListener {

        /**
         * Action for displaying a property value when called
         * @param e event to be processed
         */
        public void actionPerformed(ActionEvent e) {
            if (bigEntries.containsKey(e.getActionCommand())) {
                JFrame base = bigEntries.get(e.getActionCommand());
                if (!base.isVisible()) {
                    base.setVisible(true);
                }
            }
        }
    }
}
