/*

 * RatGui.java

 *

 * Created on 14 February 2008, 13:44

 * Copyright Daniel McEnnis, see license.txt

 */

package org.mcennis.graphrat.gui;



import java.util.logging.Level;

import java.util.logging.Logger;

import java.util.regex.Pattern;


import javax.swing.table.TableCellRenderer;

import javax.swing.table.TableColumn;

import javax.swing.tree.DefaultMutableTreeNode;


import javax.swing.tree.DefaultTreeModel;

import javax.swing.tree.TreePath;

import org.mcennis.graphrat.RatExecutionThread;

import org.mcennis.graphrat.dataAquisition.DataAquisition;

import org.mcennis.graphrat.dataAquisition.DataAquisitionFactory;

import org.mcennis.graphrat.graph.Graph;

import org.mcennis.graphrat.algorithm.Algorithm;

import org.mcennis.graphrat.algorithm.AlgorithmFactory;

import org.mcennis.graphrat.scheduler.Scheduler;

import org.mcennis.graphrat.graph.GraphFactory;

import org.mcennis.graphrat.query.GraphQueryFactory;
import org.mcennis.graphrat.query.graph.GraphByID;
import org.mcennis.graphrat.scheduler.SchedulerFactory;
import org.dynamicfactory.descriptors.Properties;
import org.dynamicfactory.descriptors.PropertiesFactory;


/**

 * Application Frame for creating and executing graph-RAT applications

 * 

 * @author  Daniel McEnnis

 */

public class RatGui extends javax.swing.JFrame {



    ApplicationTableModel appModel;

    PropertiesTableModel graphPropertiesModel;

    PropertiesTableModel schedulerPropertiesModel;

    Graph base;

    Scheduler scheduler;

    boolean running = false;

    EditComponent componentEditor;

    SwingUpdater updater;

    RatExecutionThread dataThread;



    /** Creates new form RatGui */

    public RatGui() {



        appModel = new ApplicationTableModel();

        graphPropertiesModel = new PropertiesTableModel();

        schedulerPropertiesModel = new PropertiesTableModel();

        initComponents();

        initTables();

        int width = getPreferredWidthForColumn(executionAlgorithmTable, executionAlgorithmTable.getColumn("Type")) + 50;

        executionAlgorithmTable.getColumn("Type").setMinWidth(width);

        executionAlgorithmTable.getColumn("Type").setMaxWidth(width);

        createExecutionPipelineTable.getColumn("Type").setMinWidth(width);

        createExecutionPipelineTable.getColumn("Type").setMaxWidth(width);

        createTree();

        graphComboBox.setSelectedIndex(1);

        schedulerComboBox.setSelectedIndex(0);

        this.getContentPane().setBackground(new java.awt.Color(204, 255, 204));

        ScreenHandler handler = new ScreenHandler();

        handler.setOutputScreen(SystemOutput);

        Logger.getLogger("nz.ac.waikato.mcennis.rat").addHandler(handler);

        Logger.getLogger("nz.ac.waikato.mcennis.rat").setLevel(Level.INFO);

        pack();

    }



    protected void initTables() {

        graphTable.getColumn("Property Name").setCellRenderer(new PropertiesCellRenderer());

        schedulerTable.getColumn("Property Name").setCellRenderer(new PropertiesCellRenderer());

//        createExecutionPipelineTable.getColumn("Type").setCellRenderer(new SizeIncreaseCellRenderer());

//        createExecutionPipelineTable.getColumn("Component Name").setCellRenderer(new SizeIncreaseCellRenderer());

//        DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer)givenAlgorithmsTree.getCellRenderer();

//        renderer.setFont(renderer.getFont().deriveFont((float)16.0));

//        jTabbedPane1.setFont(jTabbedPane1.getFont().deriveFont((float)16.0));

 //       jMenuBar1.setFont(jMenuBar1.getFont().deriveFont((float)16.0));

//        FileMenu.setFont(FileMenu.getFont().deriveFont((float)16.0));

//        EditMenu.setFont(EditMenu.getFont().deriveFont((float)16.0));

//        setUIFont(5);

     }



    /**

     * Shamelessly stolen from Graphic Java: Mastering the JFC

     * 

     * @param tc column whose width is to be calculated

     * @return maximum width of largest cell

     */

    protected int getPreferredWidthForColumn(javax.swing.JTable table, TableColumn tc) {

        int c = tc.getModelIndex();

        int width = 0;

        int maxw = 0;

        for (int r = 0; r < table.getRowCount(); r++) {

            TableCellRenderer renderer = table.getCellRenderer(r, c);

            java.awt.Component comp = renderer.getTableCellRendererComponent(table, table.getValueAt(r, c), false, false, r, c);

            width = comp.getPreferredSize().width;

            maxw = width > maxw ? width : maxw;

        }

        return maxw;

    }



    private void createTree() {

        DefaultMutableTreeNode root = (DefaultMutableTreeNode) givenAlgorithmsTree.getModel().getRoot();

        root.removeAllChildren();



        DefaultMutableTreeNode dataAquisition = new DefaultMutableTreeNode();

        dataAquisition.setUserObject("Data Aquisition Modules");

        String[] dataContent = DataAquisitionFactory.newInstance().getKnownModules();

        for (int i = 0; i < dataContent.length; ++i) {

            DefaultMutableTreeNode element = new DefaultMutableTreeNode();

            element.setUserObject(dataContent[i]);

            dataAquisition.add(element);

        }

        root.add(dataAquisition);



        DefaultMutableTreeNode algorithm = new DefaultMutableTreeNode();

        algorithm.setUserObject("Algorithm Modules");



        DefaultMutableTreeNode algorithmAggregating = new DefaultMutableTreeNode();

        algorithmAggregating.setUserObject("Aggregating Algorithms");

        dataContent = AlgorithmFactory.newInstance().getKnownAggregatorModules();

        for (int i = 0; i < dataContent.length; ++i) {

            DefaultMutableTreeNode element = new DefaultMutableTreeNode();

            element.setUserObject(dataContent[i]);

            algorithmAggregating.add(element);

        }

        algorithm.add(algorithmAggregating);



        DefaultMutableTreeNode algorithmClustering = new DefaultMutableTreeNode();

        algorithmClustering.setUserObject("Clustering Algorithms");

        dataContent = AlgorithmFactory.newInstance().getKnownClusterModules();

        for (int i = 0; i < dataContent.length; ++i) {

            DefaultMutableTreeNode element = new DefaultMutableTreeNode();

            element.setUserObject(dataContent[i]);

            algorithmClustering.add(element);

        }

        algorithm.add(algorithmClustering);



        DefaultMutableTreeNode algorithmCollaborativeFiltering = new DefaultMutableTreeNode();

        algorithmCollaborativeFiltering.setUserObject("Collaborative Filtering Algorithms");

        dataContent = AlgorithmFactory.newInstance().getKnownCollaborativeFilteringModules();

        for (int i = 0; i < dataContent.length; ++i) {

            DefaultMutableTreeNode element = new DefaultMutableTreeNode();

            element.setUserObject(dataContent[i]);

            algorithmCollaborativeFiltering.add(element);

        }

        algorithm.add(algorithmCollaborativeFiltering);



        DefaultMutableTreeNode algorithmDisplay = new DefaultMutableTreeNode();

        algorithmDisplay.setUserObject("Display Algorithms");

        dataContent = AlgorithmFactory.newInstance().getKnownDisplayModules();

        for (int i = 0; i < dataContent.length; ++i) {

            DefaultMutableTreeNode element = new DefaultMutableTreeNode();

            element.setUserObject(dataContent[i]);

            algorithmDisplay.add(element);

        }

        algorithm.add(algorithmDisplay);



        DefaultMutableTreeNode algorithmEvaluation = new DefaultMutableTreeNode();

        algorithmEvaluation.setUserObject("Evaluation Algorithms");

        dataContent = AlgorithmFactory.newInstance().getKnownEvaluationModules();

        for (int i = 0; i < dataContent.length; ++i) {

            DefaultMutableTreeNode element = new DefaultMutableTreeNode();

            element.setUserObject(dataContent[i]);

            algorithmEvaluation.add(element);

        }

        algorithm.add(algorithmEvaluation);



        DefaultMutableTreeNode algorithmML = new DefaultMutableTreeNode();

        algorithmML.setUserObject("Machine Learning Algorithms");

        dataContent = AlgorithmFactory.newInstance().getKnownMachineLearningModules();

        for (int i = 0; i < dataContent.length; ++i) {

            DefaultMutableTreeNode element = new DefaultMutableTreeNode();

            element.setUserObject(dataContent[i]);

            algorithmML.add(element);

        }

        algorithm.add(algorithmML);

        

        DefaultMutableTreeNode algorithmPrestige = new DefaultMutableTreeNode();

        algorithmPrestige.setUserObject("Prestige Algorithms");

        dataContent = AlgorithmFactory.newInstance().getKnownPrestigeModules();

        for (int i = 0; i < dataContent.length; ++i) {

            DefaultMutableTreeNode element = new DefaultMutableTreeNode();

            element.setUserObject(dataContent[i]);

            algorithmPrestige.add(element);

        }

        algorithm.add(algorithmPrestige);



        DefaultMutableTreeNode algorithmSimilarity = new DefaultMutableTreeNode();

        algorithmSimilarity.setUserObject("Similarity Algorithms");

        dataContent = AlgorithmFactory.newInstance().getKnownSimilarityModules();

        for (int i = 0; i < dataContent.length; ++i) {

            DefaultMutableTreeNode element = new DefaultMutableTreeNode();

            element.setUserObject(dataContent[i]);

            algorithmSimilarity.add(element);

        }

        algorithm.add(algorithmSimilarity);



        dataContent = AlgorithmFactory.newInstance().getKnownOtherModules();

        for (int i = 0; i < dataContent.length; ++i) {

            DefaultMutableTreeNode element = new DefaultMutableTreeNode();

            element.setUserObject(dataContent[i]);

            algorithm.add(element);

        }



        root.add(algorithm);

        root.setUserObject("Available Algorithms");



        ((DefaultTreeModel) givenAlgorithmsTree.getModel()).setRoot(root);

    }

//TODO remove this code

/**

 * A method to uniformly increment the fontsize of the user-interface

 * by some increment.

 * @param increment, the value to increment the GUI's default font sizes by.

 * The following link describes how to set the default font for all

 * Swing components. It's been modified to merely change the size of

 * the font by some increment.

 * @see http://www.rgagnon.com/javadetails/java-0335.html

 * @see http://www.java2s.com/Code/Java/Swing-JFC/UIManagerresourcestotweakthelookofapplications.htm

 */

/* public static void setUIFont (int increment) {

     java.util.Enumeration keys = UIManager.getDefaults().keys();

     while (keys.hasMoreElements()) {

             Object key = keys.nextElement();

             Object value = UIManager.get (key);

             if (value instanceof javax.swing.plaf.FontUIResource) {

                     java.awt.Font originalFont = UIManager.getFont(key);

                     java.awt.Font resizedFont = new java.awt.Font(

                                     originalFont.getName(),

                                     originalFont.getStyle(),

                                     originalFont.getSize()+increment);

                     UIManager.put(key, resizedFont);

             }

     }

 }

 */   

    

    /** This method is called from within the constructor to

     * initialize the form.

     * WARNING: Do NOT modify this code. The content of this method is

     * always regenerated by the Form Editor.

     */

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents

    private void initComponents() {

        java.awt.GridBagConstraints gridBagConstraints;



        jTabbedPane1 = new javax.swing.JTabbedPane();

        graphTab = new javax.swing.JPanel();

        graphPanel = new javax.swing.JPanel();

        graphClassLabel = new javax.swing.JLabel();

        graphComboBox = new javax.swing.JComboBox();

        graphTableLabel = new javax.swing.JLabel();

        graphScrollPane = new javax.swing.JScrollPane();

        graphTable = new javax.swing.JTable();

        schedulerPanel = new javax.swing.JPanel();

        schedulerClassLabel = new javax.swing.JLabel();

        schedulerComboBox = new javax.swing.JComboBox();

        schedulerTableLabel = new javax.swing.JLabel();

        schedulerScrollPane = new javax.swing.JScrollPane();

        schedulerTable = new javax.swing.JTable();

        createTab = new javax.swing.JPanel();

        givenAlgorithmsPanel = new javax.swing.JPanel();

        AlgorithmListLabel = new javax.swing.JLabel();

        givenAlgorithmsScrollPane = new javax.swing.JScrollPane();

        givenAlgorithmsTree = new javax.swing.JTree();

        createButtonPanel = new javax.swing.JPanel();

        moveUpButton = new javax.swing.JButton();

        addButton = new javax.swing.JButton();

        deleteButton = new javax.swing.JButton();

        editButton = new javax.swing.JButton();

        moveDownButton = new javax.swing.JButton();

        createExecutionPipelinePanel = new javax.swing.JPanel();

        ApplicationLabel = new javax.swing.JLabel();

        createExecutionPipelineScrollPane = new javax.swing.JScrollPane();

        createExecutionPipelineTable = new javax.swing.JTable();

        executeTab = new javax.swing.JPanel();

        algorithmExecList = new javax.swing.JPanel();

        AlgorithmExecLabel = new javax.swing.JLabel();

        executionAlgorithmScrollPane = new javax.swing.JScrollPane();

        executionAlgorithmTable = new javax.swing.JTable();

        executeButtonPanel = new javax.swing.JPanel();

        executionStart = new javax.swing.JButton();

        executionCancel = new javax.swing.JButton();

        algorithmLabel = new javax.swing.JLabel();

        algorithmProgressBar = new javax.swing.JProgressBar();

        graphLabel = new javax.swing.JLabel();

        graphProgressBar = new javax.swing.JProgressBar();

        systemOutputPanel = new javax.swing.JPanel();

        SystemOutputLabel = new javax.swing.JLabel();

        LoggingLevel = new javax.swing.JComboBox();

        executionOutputScrollPane = new javax.swing.JScrollPane();

        SystemOutput = new javax.swing.JTextArea();

        jMenuBar1 = new javax.swing.JMenuBar();

        FileMenu = new javax.swing.JMenu();

        SaveMenu = new javax.swing.JMenuItem();

        LoadMenu = new javax.swing.JMenuItem();

        QuitMenu = new javax.swing.JMenuItem();

        EditMenu = new javax.swing.JMenu();

        CutAction = new javax.swing.JMenuItem();

        CopyMenu = new javax.swing.JMenuItem();

        PasteMenu = new javax.swing.JMenuItem();



        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        setTitle("MRGraph Toolkit");

        setBackground(new java.awt.Color(204, 255, 204));

        setMinimumSize(new java.awt.Dimension(1050, 600));



        jTabbedPane1.setBackground(new java.awt.Color(204, 255, 204));

        jTabbedPane1.setMinimumSize(new java.awt.Dimension(900, 700));

        jTabbedPane1.setOpaque(true);

        jTabbedPane1.setPreferredSize(new java.awt.Dimension(700, 700));



        graphTab.setBackground(new java.awt.Color(204, 255, 204));

        graphTab.setLayout(new java.awt.BorderLayout());



        graphPanel.setBackground(new java.awt.Color(204, 255, 204));

        graphPanel.setMaximumSize(new java.awt.Dimension(520, 492));

        graphPanel.setMinimumSize(new java.awt.Dimension(520, 490));

        graphPanel.setPreferredSize(new java.awt.Dimension(520, 492));

        graphPanel.setLayout(new java.awt.GridBagLayout());



        graphClassLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        graphClassLabel.setText("Graph Class");

        gridBagConstraints = new java.awt.GridBagConstraints();

        gridBagConstraints.gridx = 0;

        gridBagConstraints.gridy = 0;

        gridBagConstraints.insets = new java.awt.Insets(13, 0, 0, 0);

        graphPanel.add(graphClassLabel, gridBagConstraints);



        graphComboBox.setModel(new javax.swing.DefaultComboBoxModel(GraphFactory.newInstance().getKnownGraphs()));

        graphComboBox.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {

                graphComboBoxActionPerformed(evt);

            }

        });

        gridBagConstraints = new java.awt.GridBagConstraints();

        gridBagConstraints.gridx = 0;

        gridBagConstraints.gridy = 1;

        gridBagConstraints.ipadx = 5;

        gridBagConstraints.ipady = 2;

        graphPanel.add(graphComboBox, gridBagConstraints);



        graphTableLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        graphTableLabel.setText("Graph Properties");

        gridBagConstraints = new java.awt.GridBagConstraints();

        gridBagConstraints.gridx = 0;

        gridBagConstraints.gridy = 2;

        gridBagConstraints.insets = new java.awt.Insets(20, 0, 0, 0);

        graphPanel.add(graphTableLabel, gridBagConstraints);



        graphScrollPane.setMinimumSize(new java.awt.Dimension(450, 600));

        graphScrollPane.setPreferredSize(new java.awt.Dimension(450, 600));



        graphTable.setModel(graphPropertiesModel);

        graphTable.setMinimumSize(new java.awt.Dimension(450, 600));

        graphTable.setPreferredSize(new java.awt.Dimension(450, 600));

        graphScrollPane.setViewportView(graphTable);



        gridBagConstraints = new java.awt.GridBagConstraints();

        gridBagConstraints.gridx = 0;

        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;

        gridBagConstraints.ipady = -210;

        gridBagConstraints.weightx = 1.0;

        gridBagConstraints.weighty = 1.0;

        gridBagConstraints.insets = new java.awt.Insets(0, 30, 30, 30);

        graphPanel.add(graphScrollPane, gridBagConstraints);



        graphTab.add(graphPanel, java.awt.BorderLayout.WEST);



        schedulerPanel.setBackground(new java.awt.Color(204, 255, 204));

        schedulerPanel.setMinimumSize(new java.awt.Dimension(520, 492));

        schedulerPanel.setPreferredSize(new java.awt.Dimension(520, 492));

        schedulerPanel.setLayout(new java.awt.GridBagLayout());



        schedulerClassLabel.setText("Scheduler Class");

        gridBagConstraints = new java.awt.GridBagConstraints();

        gridBagConstraints.gridx = 0;

        gridBagConstraints.gridy = 0;

        gridBagConstraints.insets = new java.awt.Insets(13, 0, 0, 0);

        schedulerPanel.add(schedulerClassLabel, gridBagConstraints);



        schedulerComboBox.setModel(new javax.swing.DefaultComboBoxModel(SchedulerFactory.newInstance().getKnownSchedulers()));

        schedulerComboBox.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {

                schedulerComboBoxActionPerformed(evt);

            }

        });

        gridBagConstraints = new java.awt.GridBagConstraints();

        gridBagConstraints.gridx = 0;

        gridBagConstraints.gridy = 1;

        gridBagConstraints.ipadx = 5;

        gridBagConstraints.ipady = 2;

        schedulerPanel.add(schedulerComboBox, gridBagConstraints);



        schedulerTableLabel.setText("Scheduler Properties");

        gridBagConstraints = new java.awt.GridBagConstraints();

        gridBagConstraints.gridx = 0;

        gridBagConstraints.gridy = 2;

        gridBagConstraints.insets = new java.awt.Insets(20, 0, 0, 0);

        schedulerPanel.add(schedulerTableLabel, gridBagConstraints);



        schedulerScrollPane.setMinimumSize(new java.awt.Dimension(450, 600));

        schedulerScrollPane.setPreferredSize(new java.awt.Dimension(450, 600));



        schedulerTable.setModel(schedulerPropertiesModel);

        schedulerTable.setMinimumSize(new java.awt.Dimension(450, 600));

        schedulerScrollPane.setViewportView(schedulerTable);



        gridBagConstraints = new java.awt.GridBagConstraints();

        gridBagConstraints.gridx = 0;

        gridBagConstraints.gridy = 3;

        gridBagConstraints.gridheight = java.awt.GridBagConstraints.RELATIVE;

        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;

        gridBagConstraints.ipady = -210;

        gridBagConstraints.insets = new java.awt.Insets(0, 30, 30, 30);

        schedulerPanel.add(schedulerScrollPane, gridBagConstraints);



        graphTab.add(schedulerPanel, java.awt.BorderLayout.EAST);



        jTabbedPane1.addTab("Graph", graphTab);



        createTab.setBackground(new java.awt.Color(204, 255, 204));

        createTab.setPreferredSize(new java.awt.Dimension(1100, 700));

        createTab.setLayout(new java.awt.BorderLayout());



        givenAlgorithmsPanel.setBackground(new java.awt.Color(204, 255, 204));

        givenAlgorithmsPanel.setLayout(new javax.swing.BoxLayout(givenAlgorithmsPanel, javax.swing.BoxLayout.Y_AXIS));



        AlgorithmListLabel.setBackground(new java.awt.Color(204, 255, 204));

        AlgorithmListLabel.setText("Algorithm List");

        givenAlgorithmsPanel.add(AlgorithmListLabel);



        givenAlgorithmsScrollPane.setPreferredSize(new java.awt.Dimension(440, 249));



        givenAlgorithmsTree.setMaximumSize(new java.awt.Dimension(450, 300));

        givenAlgorithmsTree.setPreferredSize(new java.awt.Dimension(300, 72));

        givenAlgorithmsTree.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {

                givenAlgorithmsTreeMouseClicked(evt);

            }

        });

        givenAlgorithmsScrollPane.setViewportView(givenAlgorithmsTree);



        givenAlgorithmsPanel.add(givenAlgorithmsScrollPane);



        createTab.add(givenAlgorithmsPanel, java.awt.BorderLayout.WEST);



        createButtonPanel.setBackground(new java.awt.Color(204, 255, 204));

        createButtonPanel.setPreferredSize(new java.awt.Dimension(100, 700));



        moveUpButton.setText("Move Up");

        moveUpButton.setMaximumSize(new java.awt.Dimension(90, 23));

        moveUpButton.setMinimumSize(new java.awt.Dimension(90, 23));

        moveUpButton.setPreferredSize(new java.awt.Dimension(90, 23));

        moveUpButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {

                moveUpButtonActionPerformed(evt);

            }

        });

        createButtonPanel.add(moveUpButton);



        addButton.setText("Add");

        addButton.setMaximumSize(new java.awt.Dimension(90, 23));

        addButton.setMinimumSize(new java.awt.Dimension(90, 23));

        addButton.setPreferredSize(new java.awt.Dimension(90, 23));

        addButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {

                addButtonActionPerformed(evt);

            }

        });

        createButtonPanel.add(addButton);



        deleteButton.setText("Delete");

        deleteButton.setMaximumSize(new java.awt.Dimension(90, 23));

        deleteButton.setMinimumSize(new java.awt.Dimension(90, 23));

        deleteButton.setPreferredSize(new java.awt.Dimension(90, 23));

        deleteButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {

                deleteButtonActionPerformed(evt);

            }

        });

        createButtonPanel.add(deleteButton);



        editButton.setText("Edit");

        editButton.setMaximumSize(new java.awt.Dimension(90, 23));

        editButton.setMinimumSize(new java.awt.Dimension(90, 23));

        editButton.setPreferredSize(new java.awt.Dimension(90, 23));

        editButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {

                editButtonActionPerformed(evt);

            }

        });

        createButtonPanel.add(editButton);



        moveDownButton.setText("Move Down");

        moveDownButton.setMaximumSize(new java.awt.Dimension(90, 23));

        moveDownButton.setMinimumSize(new java.awt.Dimension(90, 23));

        moveDownButton.setPreferredSize(new java.awt.Dimension(90, 23));

        moveDownButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {

                moveDownButtonActionPerformed(evt);

            }

        });

        createButtonPanel.add(moveDownButton);



        createTab.add(createButtonPanel, java.awt.BorderLayout.CENTER);



        createExecutionPipelinePanel.setBackground(new java.awt.Color(204, 255, 204));

        createExecutionPipelinePanel.setLayout(new javax.swing.BoxLayout(createExecutionPipelinePanel, javax.swing.BoxLayout.Y_AXIS));



        ApplicationLabel.setBackground(new java.awt.Color(204, 255, 204));

        ApplicationLabel.setText("Application");

        createExecutionPipelinePanel.add(ApplicationLabel);



        createExecutionPipelineScrollPane.setAutoscrolls(true);

        createExecutionPipelineScrollPane.setPreferredSize(new java.awt.Dimension(440, 700));



        createExecutionPipelineTable.setModel(appModel);

        createExecutionPipelineTable.setPreferredSize(new java.awt.Dimension(450, 700));

        createExecutionPipelineTable.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {

                createExecutionPipelineTableMouseClicked(evt);

            }

        });

        createExecutionPipelineScrollPane.setViewportView(createExecutionPipelineTable);

        createExecutionPipelineTable.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);



        createExecutionPipelinePanel.add(createExecutionPipelineScrollPane);



        createTab.add(createExecutionPipelinePanel, java.awt.BorderLayout.EAST);



        jTabbedPane1.addTab("Create", createTab);



        executeTab.setBackground(new java.awt.Color(204, 255, 204));

        executeTab.setPreferredSize(new java.awt.Dimension(1100, 700));

        executeTab.setLayout(new java.awt.BorderLayout());



        algorithmExecList.setBackground(new java.awt.Color(204, 255, 204));

        algorithmExecList.setLayout(new javax.swing.BoxLayout(algorithmExecList, javax.swing.BoxLayout.Y_AXIS));



        AlgorithmExecLabel.setBackground(new java.awt.Color(204, 255, 204));

        AlgorithmExecLabel.setText("Algorithm List");

        algorithmExecList.add(AlgorithmExecLabel);



        executionAlgorithmScrollPane.setPreferredSize(new java.awt.Dimension(440, 100));



        executionAlgorithmTable.setModel(appModel);

        executionAlgorithmTable.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));

        executionAlgorithmTable.setMinimumSize(new java.awt.Dimension(450, 700));

        executionAlgorithmScrollPane.setViewportView(executionAlgorithmTable);



        algorithmExecList.add(executionAlgorithmScrollPane);



        executeTab.add(algorithmExecList, java.awt.BorderLayout.WEST);



        executeButtonPanel.setBackground(new java.awt.Color(204, 255, 204));



        executionStart.setText("Start");

        executionStart.setMaximumSize(new java.awt.Dimension(90, 23));

        executionStart.setMinimumSize(new java.awt.Dimension(90, 23));

        executionStart.setPreferredSize(new java.awt.Dimension(90, 23));

        executionStart.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {

                executionStartActionPerformed(evt);

            }

        });

        executeButtonPanel.add(executionStart);



        executionCancel.setText("Cancel");

        executionCancel.setMaximumSize(new java.awt.Dimension(90, 23));

        executionCancel.setMinimumSize(new java.awt.Dimension(90, 23));

        executionCancel.setPreferredSize(new java.awt.Dimension(90, 23));

        executionCancel.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {

                executionCancelActionPerformed(evt);

            }

        });

        executeButtonPanel.add(executionCancel);



        algorithmLabel.setText("Item Progress");

        executeButtonPanel.add(algorithmLabel);



        algorithmProgressBar.setAutoscrolls(true);

        algorithmProgressBar.setPreferredSize(new java.awt.Dimension(80, 14));

        executeButtonPanel.add(algorithmProgressBar);



        graphLabel.setText("Graph Progress");

        executeButtonPanel.add(graphLabel);



        graphProgressBar.setPreferredSize(new java.awt.Dimension(80, 14));

        executeButtonPanel.add(graphProgressBar);



        executeTab.add(executeButtonPanel, java.awt.BorderLayout.CENTER);



        systemOutputPanel.setBackground(new java.awt.Color(204, 255, 204));

        systemOutputPanel.setLayout(new javax.swing.BoxLayout(systemOutputPanel, javax.swing.BoxLayout.Y_AXIS));



        SystemOutputLabel.setBackground(new java.awt.Color(204, 255, 204));

        SystemOutputLabel.setText("System Output");

        systemOutputPanel.add(SystemOutputLabel);



        LoggingLevel.setBackground(new java.awt.Color(204, 255, 204));

        LoggingLevel.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "INFO", "FINEST", "FINER", "FINE", "CONFIG", "SEVERE", "WARNING" }));

        LoggingLevel.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(java.awt.event.ItemEvent evt) {

                LoggingLevelItemStateChanged(evt);

            }

        });

        systemOutputPanel.add(LoggingLevel);



        executionOutputScrollPane.setPreferredSize(new java.awt.Dimension(440, 700));



        SystemOutput.setColumns(20);

        SystemOutput.setRows(5);

        SystemOutput.setMinimumSize(new java.awt.Dimension(450, 70));

        executionOutputScrollPane.setViewportView(SystemOutput);



        systemOutputPanel.add(executionOutputScrollPane);



        executeTab.add(systemOutputPanel, java.awt.BorderLayout.LINE_END);



        jTabbedPane1.addTab("Execute", executeTab);



        getContentPane().add(jTabbedPane1, java.awt.BorderLayout.CENTER);



        jMenuBar1.setBackground(new java.awt.Color(204, 255, 204));



        FileMenu.setBackground(new java.awt.Color(204, 255, 204));

        FileMenu.setText("File");



        SaveMenu.setText("Save");

        FileMenu.add(SaveMenu);



        LoadMenu.setText("Load");

        FileMenu.add(LoadMenu);



        QuitMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, 0));

        QuitMenu.setText("Quit");

        QuitMenu.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {

                QuitMenuActionPerformed(evt);

            }

        });

        FileMenu.add(QuitMenu);



        jMenuBar1.add(FileMenu);



        EditMenu.setText("Edit");



        CutAction.setText("Cut");

        EditMenu.add(CutAction);



        CopyMenu.setText("Copy");

        EditMenu.add(CopyMenu);



        PasteMenu.setText("Paste");

        EditMenu.add(PasteMenu);



        jMenuBar1.add(EditMenu);



        setJMenuBar(jMenuBar1);



        pack();

    }// </editor-fold>//GEN-END:initComponents

    private void executionCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_executionCancelActionPerformed

        running = false;

        scheduler.cancel();

        algorithmProgressBar.setValue(0);

        graphProgressBar.setValue(0);

        SystemOutput.setText("");

    }//GEN-LAST:event_executionCancelActionPerformed



    private void executionStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_executionStartActionPerformed

        if ((running == false) || (!dataThread.isAlive())) {

            String error = "";

            boolean bad = false;

            running = true;

            updater = new SwingUpdater(this);



            Properties props = schedulerPropertiesModel.getProperties();

            props.set("scheduler", (String) schedulerComboBox.getSelectedItem());

            scheduler = SchedulerFactory.newInstance().create(props);

            

            props = graphPropertiesModel.getProperties();

            props.set("Graph",(String)graphComboBox.getSelectedItem());

            base = GraphFactory.newInstance().create(props);

            scheduler.set(base);

            DataAquisition[] dataAquisition = appModel.getDataAquisition();

            if (dataAquisition != null) {

                for(int i=0;i<dataAquisition.length;++i){

                dataAquisition[i].addListener(scheduler);

                scheduler.load(dataAquisition[i]);

                }

                Algorithm[] algorithms = appModel.getAlgorithms();

                for (int i = 0; i < algorithms.length; ++i) {
                    GraphByID id = (GraphByID)GraphQueryFactory.newInstance().create("GraphByID");
                    id.buildQuery(Pattern.compile(appModel.getPattern(i+1)));
                    algorithms[i].addListener(scheduler);

                    scheduler.load(algorithms[i], id);

                }

                scheduler.addListener(updater);

                dataThread = new RatExecutionThread(scheduler);

                dataThread.start();

                

            } else {

                error += "Data Aquisition has not been set.  Add an DataAquisiton object in the 'create' tab\n\n";

                bad = true;

            }

        }

    }//GEN-LAST:event_executionStartActionPerformed



    private void moveDownButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moveDownButtonActionPerformed

        if ((!running) && (createExecutionPipelineTable.getSelectedRow() > -1)) {

            appModel.moveDown(createExecutionPipelineTable.getSelectedRow());

        }

    }//GEN-LAST:event_moveDownButtonActionPerformed



    private void editButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editButtonActionPerformed

        int row = createExecutionPipelineTable.getSelectedRow();

        if (row > -1) {

            if ((row > 0) || ((row == 0) && (appModel.getComponent(0) != null))) {

                new EditComponent(appModel, row);

            }

        }

    }//GEN-LAST:event_editButtonActionPerformed



    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed

        if ((!running) && (createExecutionPipelineTable.getSelectedRow() <= appModel.getDataAquisitionCount())) {

            appModel.removeDataAquisition(createExecutionPipelineTable.getSelectedRow());

        } else if ((!running) && (createExecutionPipelineTable.getSelectedRow() > 0)) {

            appModel.removeAlgorithm(createExecutionPipelineTable.getSelectedRow());

        }

    }//GEN-LAST:event_deleteButtonActionPerformed



    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed

        TreePath[] paths = givenAlgorithmsTree.getSelectionPaths();

        if (paths != null) {

            //            System.out.println(((DefaultMutableTreeNode)paths[0].getPathComponent(1)).getUserObject());

                int row = appModel.getRowCount();

            if (((DefaultMutableTreeNode) paths[0].getPathComponent(1)).getUserObject().equals("Data Aquisition Modules")) {

                String type = (String) ((DefaultMutableTreeNode) paths[0].getLastPathComponent()).getUserObject();

                Properties props = PropertiesFactory.newInstance().create();

                props.set("aquisition", type);

                DataAquisition da = DataAquisitionFactory.newInstance().create(props);

                if (da != null) {

                    if (createExecutionPipelineTable.getSelectedRow() != -1) {

                        appModel.addDataAquisition(da, createExecutionPipelineTable.getSelectedRow());

                    } else {

                        appModel.addDataAquisition(da, row);

                    }

                } else {

                // TODO throw message box

                }

            } else {

                String type = (String) ((DefaultMutableTreeNode) paths[0].getLastPathComponent()).getUserObject();

                Properties props = PropertiesFactory.newInstance().create();

                props.set("algorithm", type);

                Algorithm da = AlgorithmFactory.newInstance().create(props);

                if (da != null) {

                    if (createExecutionPipelineTable.getSelectedRow() != -1) {

                        appModel.addAlgorithm(da, createExecutionPipelineTable.getSelectedRow());

                    } else {

                        appModel.addAlgorithm(da, row);

                    }

                // TODO: Force a proper table size

                } else {

                // TODO throw message box

                }

            }

        }

    }//GEN-LAST:event_addButtonActionPerformed



    private void moveUpButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moveUpButtonActionPerformed

        if ((!running) && (createExecutionPipelineTable.getSelectedRow() > -1)) {

            appModel.moveUp(createExecutionPipelineTable.getSelectedRow());

        }

    }//GEN-LAST:event_moveUpButtonActionPerformed



    private void schedulerComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_schedulerComboBoxActionPerformed

        Properties props = PropertiesFactory.newInstance().create();

        props.set("scheduler", (String) ((javax.swing.JComboBox) evt.getSource()).getSelectedItem());

        scheduler = SchedulerFactory.newInstance().create(props);

        schedulerPropertiesModel.loadProperties(scheduler.getParameter());

        schedulerPropertiesModel.fireTableDataChanged();

    }//GEN-LAST:event_schedulerComboBoxActionPerformed



    private void graphComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_graphComboBoxActionPerformed

        Properties props = PropertiesFactory.newInstance().create();

        props.set("Graph", (String) ((javax.swing.JComboBox) evt.getSource()).getSelectedItem());

        base = GraphFactory.newInstance().create(props);

        graphPropertiesModel.loadProperties(base.getParameter());

        graphPropertiesModel.fireTableDataChanged();

    }//GEN-LAST:event_graphComboBoxActionPerformed



    private void createExecutionPipelineTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_createExecutionPipelineTableMouseClicked

        if (evt.getButton() == java.awt.event.MouseEvent.BUTTON1) {

            if (evt.getClickCount() == 2) {

                int row = createExecutionPipelineTable.rowAtPoint(evt.getPoint());

                if ((row > 0) || ((row == 0) && (appModel.getComponent(0) != null))) {

                    new EditComponent(appModel, row);

                }

            }

        }

    }//GEN-LAST:event_createExecutionPipelineTableMouseClicked



    private void QuitMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_QuitMenuActionPerformed

        System.exit(0);

    }//GEN-LAST:event_QuitMenuActionPerformed



private void LoggingLevelItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_LoggingLevelItemStateChanged

Logger.getLogger("nz.ac.waikato.mcennis.rat").setLevel(Level.parse((String)evt.getItem()));    

}//GEN-LAST:event_LoggingLevelItemStateChanged



private void givenAlgorithmsTreeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_givenAlgorithmsTreeMouseClicked

    if(evt.getClickCount() > 1){

        int row = givenAlgorithmsTree.getClosestRowForLocation(evt.getX(), evt.getY());

        

    }

}//GEN-LAST:event_givenAlgorithmsTreeMouseClicked



    /**

     * @param args the command line arguments

     */

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {



            public void run() {

                new RatGui().setVisible(true);

            }

        });

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables

    private javax.swing.JLabel AlgorithmExecLabel;

    private javax.swing.JLabel AlgorithmListLabel;

    private javax.swing.JLabel ApplicationLabel;

    private javax.swing.JMenuItem CopyMenu;

    private javax.swing.JMenuItem CutAction;

    private javax.swing.JMenu EditMenu;

    private javax.swing.JMenu FileMenu;

    private javax.swing.JMenuItem LoadMenu;

    private javax.swing.JComboBox LoggingLevel;

    private javax.swing.JMenuItem PasteMenu;

    private javax.swing.JMenuItem QuitMenu;

    private javax.swing.JMenuItem SaveMenu;

    private javax.swing.JTextArea SystemOutput;

    private javax.swing.JLabel SystemOutputLabel;

    private javax.swing.JButton addButton;

    private javax.swing.JPanel algorithmExecList;

    private javax.swing.JLabel algorithmLabel;

    private javax.swing.JProgressBar algorithmProgressBar;

    private javax.swing.JPanel createButtonPanel;

    private javax.swing.JPanel createExecutionPipelinePanel;

    private javax.swing.JScrollPane createExecutionPipelineScrollPane;

    private javax.swing.JTable createExecutionPipelineTable;

    private javax.swing.JPanel createTab;

    private javax.swing.JButton deleteButton;

    private javax.swing.JButton editButton;

    private javax.swing.JPanel executeButtonPanel;

    private javax.swing.JPanel executeTab;

    private javax.swing.JScrollPane executionAlgorithmScrollPane;

    private javax.swing.JTable executionAlgorithmTable;

    private javax.swing.JButton executionCancel;

    private javax.swing.JScrollPane executionOutputScrollPane;

    private javax.swing.JButton executionStart;

    private javax.swing.JPanel givenAlgorithmsPanel;

    private javax.swing.JScrollPane givenAlgorithmsScrollPane;

    private javax.swing.JTree givenAlgorithmsTree;

    private javax.swing.JLabel graphClassLabel;

    private javax.swing.JComboBox graphComboBox;

    private javax.swing.JLabel graphLabel;

    private javax.swing.JPanel graphPanel;

    private javax.swing.JProgressBar graphProgressBar;

    private javax.swing.JScrollPane graphScrollPane;

    private javax.swing.JPanel graphTab;

    private javax.swing.JTable graphTable;

    private javax.swing.JLabel graphTableLabel;

    private javax.swing.JMenuBar jMenuBar1;

    private javax.swing.JTabbedPane jTabbedPane1;

    private javax.swing.JButton moveDownButton;

    private javax.swing.JButton moveUpButton;

    private javax.swing.JLabel schedulerClassLabel;

    private javax.swing.JComboBox schedulerComboBox;

    private javax.swing.JPanel schedulerPanel;

    private javax.swing.JScrollPane schedulerScrollPane;

    private javax.swing.JTable schedulerTable;

    private javax.swing.JLabel schedulerTableLabel;

    private javax.swing.JPanel systemOutputPanel;

    // End of variables declaration//GEN-END:variables

    

    /**

     * Upadate the progress bars to the values described by the given update object.

     * @param change update object encapsulating descriptions of execution progress.

     */

    public void doUpdate(SwingUpdater.DoChange change) {

        algorithmProgressBar.setMinimum(0);

        algorithmProgressBar.setMaximum(change.algorithmProgressSize);

        algorithmProgressBar.setValue(change.algorithmProgressValue);

        algorithmProgressBar.invalidate();

        graphProgressBar.setMinimum(0);

        graphProgressBar.setMaximum(change.graphProgressSize);

        graphProgressBar.setValue(change.graphProgressValue);

        graphProgressBar.invalidate();

        executionAlgorithmTable.changeSelection(change.algorithmValue, 1, false, false);

    }

}

