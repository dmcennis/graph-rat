/*

 * Created 28-1-08

 * 

 * Copyright Daniel McEnnis, see license.txt

 */



package nz.ac.waikato.mcennis.rat.graphdisplay;



import java.awt.BorderLayout;

import java.awt.Container;

import java.awt.Dimension;

import javax.swing.JFrame;

import javax.swing.JLabel;

import javax.swing.JPanel;

import javax.swing.JScrollPane;

import javax.swing.JTextArea;


/**

 * JFrame object for displaying the contents of strings from a property

 * @author Daniel McEnnis

 */

public class DisplayLargeStringPropertyFrame extends JFrame{



    Property property;

    

    /**

     * Create a new frame for displaying the given property

     * @param property property to be displayed

     */

    public DisplayLargeStringPropertyFrame(Property property) {

//        try {

            if (property == null) {

                throw new NullPointerException("The property to be displayed does not exist");

            }

            this.property = property;

            Object[] values = property.getValue().toArray();

            String content = "";

            for (int i = 0; i < values.length; ++i) {

                content += values[i];

                content += "\n";

            }

            Container panel = this.getContentPane();

            panel.setLayout(new BorderLayout());

            JPanel north = new JPanel();

            north.setLayout(new BorderLayout());

            north.add(new JLabel(property.getType()), BorderLayout.NORTH);

            north.add(new JLabel(property.getPropertyClass().getName()), BorderLayout.SOUTH);

            panel.add(north, BorderLayout.NORTH);

            JScrollPane scroll = new JScrollPane();

            scroll.setPreferredSize(new Dimension(700,700));

            JTextArea contentPane = new JTextArea(content);

            contentPane.setEditable(false);

            contentPane.setWrapStyleWord(true);

            contentPane.setLineWrap(true);

            scroll.setViewportView(contentPane);

            panel.add(scroll,BorderLayout.CENTER);

            this.pack();

            this.setVisible(false);

//        } catch (BadLocationException ex) {

//            Logger.getLogger(DisplayLargeStringPropertyFrame.class.getName()).log(Level.SEVERE, null, ex);

//        }

    }

    

}

