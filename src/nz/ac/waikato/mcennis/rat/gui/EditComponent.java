/*

 * Created 19-02-08

 * Copyright Daniel McEnnis, see license.txt

 */

package nz.ac.waikato.mcennis.rat.gui;



import java.awt.Container;

import java.awt.Dimension;

import java.awt.FlowLayout;

import java.awt.GridBagConstraints;

import java.awt.GridBagLayout;

import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;

import javax.swing.JButton;

import javax.swing.JFrame;

import javax.swing.JLabel;

import javax.swing.JPanel;

import javax.swing.JScrollPane;

import javax.swing.JTable;

import javax.swing.JTextField;

import nz.ac.waikato.mcennis.rat.Component;

import nz.ac.waikato.mcennis.rat.graph.algorithm.Algorithm;
import org.dynamicfactory.descriptors.Properties;



/**

 * Frame for editing the properties of a component and setting its pattern.

 * @author Daniel McEnnis

 */

public class EditComponent extends JFrame {



    Component component;

    PropertiesTableModel model;

    ApplicationTableModel source;

    int row;

    JTextField graphPatternField;

    String pattern;



    /**

     * Create a new EditComponent object linked to the component at the given

     * row of the given ApplicationTableModel

     * @param appModel model of the application

     * @param row which Component to be edited in the appModel

     */

    public EditComponent(ApplicationTableModel appModel, int row) {

        source = appModel;

        this.row = row;

        this.component = appModel.getComponent(row);

        if(row > 0){

            this.pattern = appModel.getPattern(row);

        }else{

            this.pattern = null;

        }

        model = new PropertiesTableModel();

        model.loadProperties(component.getParameter());

        Container panel = this.getContentPane();

        panel.setLayout(new GridBagLayout());



        JLabel featureName = new JLabel(component.getClass().getName());

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;

        constraints.gridy = 0;

        constraints.anchor = GridBagConstraints.CENTER;

        panel.add(featureName, constraints);

        int y = 1;

        if (component instanceof Algorithm) {

            JLabel graphSelectionLabel = new JLabel("Regular Expression for Graph Selection");

            constraints = new GridBagConstraints();

            constraints.gridx = 0;

            constraints.gridy = 1;

            constraints.anchor = GridBagConstraints.CENTER;

            panel.add(graphSelectionLabel, constraints);



            graphPatternField = new JTextField(pattern);

            graphPatternField.setMinimumSize(new Dimension(100,20));

            constraints = new GridBagConstraints();

            constraints.gridx = 0;

            constraints.gridy = 2;

            constraints.anchor = GridBagConstraints.CENTER;

            panel.add(graphPatternField, constraints);

            y += 2;

        }

        JScrollPane scroll = new JScrollPane();

        scroll.setMinimumSize(new Dimension(400, 400));

        scroll.setPreferredSize(new Dimension(400, 400));

        constraints = new GridBagConstraints();

        constraints.gridx = 0;

        constraints.gridy = y+1;

        constraints.anchor = GridBagConstraints.CENTER;



        JTable componentProperties = new JTable();

        componentProperties.setModel(model);

        componentProperties.getColumn("Property Name").setCellRenderer(new PropertiesCellRenderer());

        componentProperties.setPreferredSize(new Dimension(400, 700));

        componentProperties.setMinimumSize(new Dimension(400, 700));

        scroll.setViewportView(componentProperties);

        panel.add(scroll, constraints);



        JPanel buttonPanel = new JPanel();

        buttonPanel.setLayout(new FlowLayout());

        constraints = new GridBagConstraints();

        constraints.gridx = 0;

        constraints.gridy = y+2;

        constraints.anchor = GridBagConstraints.EAST;



        JButton save = new JButton("Save");

        save.addActionListener(new ActionListener() {



            public void actionPerformed(ActionEvent evt) {

                saveAction(evt);

            }

        });

        buttonPanel.add(save);



        JButton cancel = new JButton("Cancel");

        cancel.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent evt) {

                cancelAction(evt);

            }

        });

        buttonPanel.add(cancel);

        panel.add(buttonPanel, constraints);

        pack();

        setVisible(true);

    }



    

    private void saveAction(ActionEvent evt) {

        Properties props = model.getProperties();

        component.init(props);

        if(component instanceof Algorithm){

            source.setPattern(row, graphPatternField.getText());

        }

        this.setVisible(false);

        source=null;

        row = -1;

    }



    private void cancelAction(ActionEvent evt) {

        this.setVisible(false);

        source = null;

        row = -1;

    }

}

