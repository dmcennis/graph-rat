/*



 * MainWindow.java



 *



 * Created on 1 September 2007, 14:46



 *



 * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt)



 */







package nz.ac.waikato.mcennis.rat.graphdisplay;







import java.awt.event.MouseAdapter;



import java.awt.event.MouseEvent;



import java.awt.event.MouseListener;



import java.io.File;



import java.io.FileNotFoundException;



import java.util.Collections;

import java.util.Iterator;

import java.util.LinkedList;

import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;



import javax.swing.tree.DefaultTreeModel;



import javax.swing.tree.TreePath;



import nz.ac.waikato.mcennis.rat.graph.MemGraph;



import nz.ac.waikato.mcennis.rat.graph.link.Link;



import nz.ac.waikato.mcennis.rat.graph.actor.Actor;



import nz.ac.waikato.mcennis.rat.graph.property.Property;



import nz.ac.waikato.mcennis.rat.parser.XMLParser;



import nz.ac.waikato.mcennis.rat.parser.xmlHandler.GraphReader;







/**



 *





 *



 * Class for displaying a graph.  Currently, link types are hardwired.



 * @author  Daniel McEnnis

 */







public class MainWindow extends javax.swing.JFrame {



    



    MemGraph graph;



    



    String user;



    



    /** Creates new form MainWindow */



    public MainWindow() {



        initComponents();



        // do double click - from Java 6 Docs...



        MouseListener ml = new MouseAdapter(){



            public void mousePressed(MouseEvent e){



                int selRow = jTree1.getRowForLocation(e.getX(), e.getY());



                TreePath selPath = jTree1.getPathForLocation(e.getX(),e.getY());



                if(selRow != -1){



                    if(e.getClickCount() ==2){



                        treeDoubleClick(selPath);



                    }



                }



            }



        };



        jTree1.addMouseListener(ml);



        



        try {



            loadGraph();



            user = "1";



            loadUser("User","1");



        } catch (FileNotFoundException ex) {



            ex.printStackTrace();



        }



    }



    



    /** This method is called from within the constructor to



     * initialize the form.



     * WARNING: Do NOT modify this code. The content of this method is



     * always regenerated by the Form Editor.



     */



    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents







    private void initComponents() {







        jSplitPane1 = new javax.swing.JSplitPane();







        jScrollPane1 = new javax.swing.JScrollPane();







        jTextPane1 = new javax.swing.JTextPane();







        jPanel1 = new javax.swing.JPanel();







        jComboBox1 = new javax.swing.JComboBox();







        jTextField1 = new javax.swing.JTextField();







        jButton1 = new javax.swing.JButton();







        jButton2 = new javax.swing.JButton();







        jScrollPane2 = new javax.swing.JScrollPane();







        jTree1 = new javax.swing.JTree();







        jComboBox2 = new javax.swing.JComboBox();







        jLabel1 = new javax.swing.JLabel();















        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);







        setTitle("User Name");







        jSplitPane1.setDividerLocation(160);







        jScrollPane1.setViewportView(jTextPane1);















        jSplitPane1.setRightComponent(jScrollPane1);















        jTextField1.setText("jTextField1");















        jButton1.setText("Change User");







        jButton1.addActionListener(new java.awt.event.ActionListener() {







            public void actionPerformed(java.awt.event.ActionEvent evt) {







                jButton1ActionPerformed(evt);







            }







        });















        jButton2.setText("Link");







        jButton2.addActionListener(new java.awt.event.ActionListener() {







            public void actionPerformed(java.awt.event.ActionEvent evt) {







                jButton2ActionPerformed(evt);







            }







        });















        jScrollPane2.setViewportView(jTree1);















        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Min", "Max", "Median" }));







        jComboBox2.getAccessibleContext().setAccessibleName("Aggregator Type");







        jComboBox2.getAccessibleContext().setAccessibleDescription("max, min, or other selector over all links");















        jLabel1.setText("Get");















        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);







        jPanel1.setLayout(jPanel1Layout);







        jPanel1Layout.setHorizontalGroup(







            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)







            .addGroup(jPanel1Layout.createSequentialGroup()







                .addContainerGap()







                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)







                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)







                .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)







                .addContainerGap())







            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE)







            .addGroup(jPanel1Layout.createSequentialGroup()







                .addComponent(jLabel1)







                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)







                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)







                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)







                .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE))







            .addGroup(jPanel1Layout.createSequentialGroup()







                .addContainerGap()







                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE)







                .addContainerGap())







        );







        jPanel1Layout.setVerticalGroup(







            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)







            .addGroup(jPanel1Layout.createSequentialGroup()







                .addContainerGap()







                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)







                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)







                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))







                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)







                .addComponent(jButton1)







                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)







                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)







                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)







                    .addComponent(jLabel1)







                    .addComponent(jButton2))







                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)







                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE))







        );







        jSplitPane1.setLeftComponent(jPanel1);















        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());







        getContentPane().setLayout(layout);







        layout.setHorizontalGroup(







            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)







            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()







                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 685, Short.MAX_VALUE)







                .addContainerGap())







        );







        layout.setVerticalGroup(







            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)







            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()







                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 501, Short.MAX_VALUE)







                .addContainerGap())







        );







        pack();







    }// </editor-fold>//GEN-END:initComponents



    



    



    



    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed



        



        String type = (String)jComboBox2.getSelectedItem();



        Vector<Link> values = new Vector<Link>();

        values.addAll(graph.getLink());



        Collections.sort(values);



        if(type.contentEquals("Min")){



            loadUser(values.get(0).getSource().getMode(),values.get(0).getSource().getID());



        }else if(type.contentEquals("Max")){



            loadUser(values.get(values.size()-1).getSource().getMode(),values.get(values.size()-1).getSource().getID());



        }else{



            int i=values.size()/2;



            loadUser(values.get(i).getSource().getMode(),values.get(i).getSource().getID());



        }



    }//GEN-LAST:event_jButton2ActionPerformed



    



    



    



    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed



        



        String type = (String)jComboBox1.getSelectedItem();



        String user = jTextField1.getText();



        loadUser(type,user);



    }//GEN-LAST:event_jButton1ActionPerformed



    



    



    



    /**



     * Loads from the local file graph.xml or graph.xml.gz and displays using



     * a graphical interface.



     *



     * @param args the command line arguments



     */



    public static void main(String args[]) {



        



        java.awt.EventQueue.invokeLater(new Runnable() {



            



            public void run() {



                



                new MainWindow().setVisible(true);



                



            }



            



        });



        



    }



    



    



    



    protected void loadGraph() throws FileNotFoundException{



        



        graph = new MemGraph();



        



        GraphReader handler = new GraphReader();



        



        handler.setGraph(graph);



        



        XMLParser parser = new XMLParser();



        



        parser.setHandler(handler);



        



        try {



            



            File check = new File("graph.xml.gz");



            



            if(check.exists()){



                



                parser.parse(new java.util.zip.GZIPInputStream(new java.io.FileInputStream("graph.xml.gz")),"graph.xml.gz");



                



            }else{



                



                parser.parse(new java.io.FileInputStream("graph.xml"),"graph.xml");



                



            }



            



            graph = (MemGraph)parser.getHandler().get();



            



            Iterator<String> types = graph.getActorTypes().iterator();

                while(types.hasNext()){

                    jComboBox1.addItem(types.next());

                }

        } catch (FileNotFoundException ex) {

            ex.printStackTrace();

        } catch (Exception ex) {

            ex.printStackTrace();

        }

    }



    protected void loadUser(String type, String name){

        Actor a = graph.getActor(type,name);

        if(a == null){

            jTextField1.setText("");

        }else{

            jTextField1.setText(name);

            this.setTitle(type+" "+name);

            jTextPane1.setText(buildTextPane(a));

            buildJTree(a);

        }

    }



    protected String buildTextPane(Actor a){

        StringBuffer scrollPaneText = new StringBuffer();

        scrollPaneText.append(a.getMode()+" "+a.getID()+"\n\n");

        Iterator<Property> props = a.getProperty().iterator();

        if(props.hasNext()){

            scrollPaneText.append("Properties\n");

            while(props.hasNext()){

                Property property = props.next();

                Iterator<Object> values = property.getValue().iterator();

                if(!values.hasNext()){

                    scrollPaneText.append(property.getType()+"\n\n");

                }else{

                    scrollPaneText.append(property.getType()+"\n");

                    while(values.hasNext()){

                        scrollPaneText.append("\t"+values.next().toString()+"\n");

                    }

                    scrollPaneText.append("\n");

                }

            }

            scrollPaneText.append("\n");

        }

        Iterator<String> types = graph.getLinkTypes().iterator();

        if(types.hasNext()){

            scrollPaneText.append("Forward Links\n");

            while(types.hasNext()){

                String type = types.next();

                LinkedList<Link> links = new LinkedList<Link>();

                links.addAll(graph.getLinkBySource(type,a));

                if(links.size() > 0){

                    Collections.sort(links);

                    scrollPaneText.append(type+"\n");

                    Iterator<Link> link = links.iterator();

                    while(link.hasNext()){

                        Link l = link.next();

                        scrollPaneText.append(l.getDestination().getID()).append("\t");

                        scrollPaneText.append(l.getStrength()).append("\n");

                    }

                    scrollPaneText.append("\n");

                }

            }

            scrollPaneText.append("\n");

            scrollPaneText.append("Backward Links\n");



            types = graph.getLinkTypes().iterator();

            while(types.hasNext()){

                String type = types.next();

                LinkedList<Link> links = new LinkedList<Link>();

                links.addAll(graph.getLinkByDestination(type,a));

                if(links.size()>0){

                    Collections.sort(links);

                    scrollPaneText.append(type+"\n");

                    Iterator<Link> link = links.iterator();

                    while(link.hasNext()){

                        Link l = link.next();

                        scrollPaneText.append(l.getSource().getID()).append("\t");

                        scrollPaneText.append(l.getStrength()).append("\n");

                    }

                }

            }

        }

        return scrollPaneText.toString();

    }



    protected void buildJTree(Actor a){

        // build the tree

        DefaultTreeModel model = (DefaultTreeModel)jTree1.getModel();

        DefaultMutableTreeNode root = new DefaultMutableTreeNode(a.getMode()+" "+a.getID());

        if(a.getMode().contentEquals("User")){

            Iterator<Link> base = graph.getLinkBySourceIterator("Knows",a);

            if(base.hasNext()){

                DefaultMutableTreeNode type = new DefaultMutableTreeNode("Forward Knows");

                while(base.hasNext()){

                    DefaultMutableTreeNode entry = new DefaultMutableTreeNode(base.next().getDestination().getID());

                    type.add(entry);

                }

            }



            



            



            



            base = (graph.getLinkByDestination("Knows",a)).iterator();



            



            if(base.hasNext()){



                



                DefaultMutableTreeNode type = new DefaultMutableTreeNode("Backward Knows");



                



                //                type.setParent(root);



                



                root.add(type);



                



                while(base.hasNext()){



                    



                    DefaultMutableTreeNode entry = new DefaultMutableTreeNode(base.next().getSource().getID());



                    



                    //                    entry.setParent(type);



                    



                    type.add(entry);



                    



                }



                



            }



            



            



            



            base = graph.getLinkBySource("Given",a).iterator();



            



            if(base.hasNext()){



                



                DefaultMutableTreeNode type = new DefaultMutableTreeNode("Given Music");



                



                //                type.setParent(root);



                



                root.add(type);



                



                while(base.hasNext()){



                    



                    DefaultMutableTreeNode entry = new DefaultMutableTreeNode(base.next().getDestination().getID());



                    



                    //                    entry.setParent(type);



                    



                    type.add(entry);



                    



                }



                



            }



            



            base = graph.getLinkBySource("Derived",a).iterator();



            



            if(base != null){



                



                DefaultMutableTreeNode type = new DefaultMutableTreeNode("Predicted Music");



                



                //                type.setParent(root);



                



                root.add(type);



                



                while(base.hasNext()){



                    



                    DefaultMutableTreeNode entry = new DefaultMutableTreeNode(base.next().getDestination().getID());



                    



                    //                    entry.setParent(type);



                    



                    type.add(entry);



                    



                }



                



            }



            



        }else{



            



            Link[] base = graph.getLinkByDestination("Given",a).toArray(new Link[]{});



            



            if(base.length > 0){



                



                DefaultMutableTreeNode type = new DefaultMutableTreeNode("Backward Given Music");



                



                //                type.setParent(root);



                



                root.add(type);



                



                for(int j=0;j<base.length;++j){



                    



                    DefaultMutableTreeNode entry = new DefaultMutableTreeNode(base[j].getSource().getID());



                    



                    //                    entry.setParent(type);



                    



                    type.add(entry);



                    



                }



                



            }



            



            



            



            base = graph.getLinkByDestination("Derived",a).toArray(new Link[]{});



            



            if(base.length>0){



                



                DefaultMutableTreeNode type = new DefaultMutableTreeNode("Backward Predicted Music");



                



                //                type.setParent(root);



                



                root.add(type);



                



                for(int j=0;j<base.length;++j){



                    



                    DefaultMutableTreeNode entry = new DefaultMutableTreeNode(base[j].getSource().getID());



                    



                    //                    entry.setParent(type);



                    



                    type.add(entry);



                    



                }



                



            }



            



            



            



        }



        



        model.setRoot(root);



        



        



        



    }



    



    



    



    protected void treeDoubleClick(TreePath path){



        



        String type;



        



        String id;



        



        Object[] array = path.getPath();



        



        if(array.length==3){



            



            id = (String)((DefaultMutableTreeNode)array[2]).getUserObject();



            



            type = (String)((DefaultMutableTreeNode)array[1]).getUserObject();



            



            if(type.contentEquals("Forward Knows")||type.contentEquals("Backward Knows")){



                



                type = "User";



                



            }else if(type.contentEquals("Given Music")||type.contentEquals("Predicted Music")){



                



                type = "Artist";



                



            }else if(type.contentEquals("Backward Given Music")||type.contentEquals("Backward Predicted Music")){



                



                type = "User";



                



            }



            



            loadUser(type,id);



            



        }



        



    }



    



    



    



    // Variables declaration - do not modify//GEN-BEGIN:variables







    private javax.swing.JButton jButton1;







    private javax.swing.JButton jButton2;







    private javax.swing.JComboBox jComboBox1;







    private javax.swing.JComboBox jComboBox2;







    private javax.swing.JLabel jLabel1;







    private javax.swing.JPanel jPanel1;







    private javax.swing.JScrollPane jScrollPane1;







    private javax.swing.JScrollPane jScrollPane2;







    private javax.swing.JSplitPane jSplitPane1;







    private javax.swing.JTextField jTextField1;







    private javax.swing.JTextPane jTextPane1;







    private javax.swing.JTree jTree1;







    // End of variables declaration//GEN-END:variables



    



    



    



}







