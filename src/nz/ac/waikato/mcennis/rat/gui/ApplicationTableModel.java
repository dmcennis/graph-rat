/*

 * Created 14-02-08

 * Copyright Daniel McEnnis, see license.txt

 */

package nz.ac.waikato.mcennis.rat.gui;



import java.util.Vector;

import java.util.logging.Level;

import java.util.logging.Logger;

import java.util.regex.Pattern;

import javax.swing.table.DefaultTableModel;

import nz.ac.waikato.mcennis.rat.Component;

import nz.ac.waikato.mcennis.rat.dataAquisition.DataAquisition;

import nz.ac.waikato.mcennis.rat.graph.algorithm.Algorithm;



/**

 * JTable model containing the DataAquisition and Algorithm modules that

 * describe applications in the GUI.

 * @author Daniel McEnnis

 */

public class ApplicationTableModel extends DefaultTableModel {



    Vector<DataAquisition> dataSource = new Vector<DataAquisition>();

    Vector<Algorithm> algorithms = new Vector<Algorithm>();

    Vector<String> algorithmsPatterns = new Vector<String>();



    /**

     * Create an empty model with an empty module for data aquisition.

     */

    public ApplicationTableModel() {

        super(new Object[][]{new Object[]{"DataAquisition", "None"}}, new Object[]{"Type", "Component Name"});

    }



    @Override

    public boolean isCellEditable(int row, int column) {

        return false;

    }



    /**

     * Fix the data aquisition module tothe given object.  Replaces the old value

     * if one already exists.  Throws a NullPointerException if the module is null.

     * @param aquisition module to load

     */

    public void addDataAquisition(DataAquisition aquisition, int row) {

        if (row < dataSource.size()) {

            Vector newRow = new Vector();

            newRow.add("DataAquisition");

            newRow.add(aquisition.getClass().getSimpleName());

            dataSource.add(row, aquisition);

            dataVector.add(row, newRow);

            this.fireTableDataChanged();

        } else if (dataSource.size() == 0) {

            ((Vector) (this.dataVector.get(0))).set(1, aquisition.getClass().getSimpleName());

            dataSource.add(aquisition);

            this.fireTableCellUpdated(0, 1);

        } else {

            Vector newRow = new Vector();

            newRow.add("DataAquisition");

            newRow.add(aquisition.getClass().getSimpleName());

            dataSource.add(aquisition);

            dataVector.add(row, newRow);

            this.fireTableDataChanged();

        }

    }



    /**

     * Removes the data aquisition module if present, null operation otherwise.

     */

    public void removeDataAquisition(int row) {

        if (dataSource.size() == 1) {

            dataSource.clear();

            ((Vector) (this.dataVector.get(0))).set(1, "None");

            this.fireTableCellUpdated(0, 1);

        } else if (row < dataSource.size()) {

            dataSource.remove(row);

            dataVector.remove(row);

            this.fireTableDataChanged();

        }

    }



    /**

     * If the row is valid, add the algorithm at the given row.  Otherwise add

     * the algorithm as the first algorithm.  Throws a NullPointerException if the

     * Algorithm is null.

     * @param algorithm algorithm to insert

     * @param row place to insert algorithm

     */

    public void addAlgorithm(Algorithm algorithm, int row) {

        if ((row >= dataSource.size()) && (row <= dataVector.size())) {

            int adjustedRow = 0;

            if (dataSource.size() != 0) {

                adjustedRow = row - dataSource.size();

            }

            algorithms.add(adjustedRow, algorithm);

            algorithmsPatterns.add(adjustedRow, ".*");

            Vector newRow = new Vector();

            newRow.add("Algorithm");

            newRow.add(algorithm.getClass().getSimpleName());

            this.dataVector.add(row, newRow);

            this.fireTableDataChanged();

        } else {

            algorithms.add(0, algorithm);

            algorithmsPatterns.add(0, ".*");

            Vector newRow = new Vector();

            newRow.add("Algorithm");

            newRow.add(algorithm.getClass().getSimpleName());

            if (dataSource.size() == 0) {

                this.dataVector.add(1, newRow);

            } else {

                this.dataVector.add(dataSource.size(), newRow);

            }

            this.fireTableDataChanged();

        }

    }



    /**

     * Remove the algorithm at the given row value or log an error if the row is invalid.

     * @param row

     */

    public void removeAlgorithm(int row) {

        if ((row >= dataSource.size()) && (row < dataVector.size()) && (row != 0)) {

            if (dataSource.size() == 0) {

                algorithmsPatterns.remove(row - 1);

                algorithms.remove(row - 1);

                this.dataVector.remove(row);

                this.fireTableDataChanged();

            } else {

                algorithmsPatterns.remove(row - dataSource.size());

                algorithms.remove(row - dataSource.size());

                this.dataVector.remove(row);

                this.fireTableDataChanged();

            }

        } else {

            Logger.getLogger(ApplicationTableModel.class.getName()).log(Level.SEVERE, "Remove Algorithm had bad row '" + row + "'");

        }

    }



    /**

     * move the algorithm one step up (moving the one above it to the given row)

     * if the row is valid or log an error message.

     * @param row location of the algorithm to move

     */

    public void moveUp(int row) {

        if ((row != 0)&&(row < dataSource.size())){

            DataAquisition hold = dataSource.get(row - 1);

            dataSource.set(row - 1, dataSource.get(row));

            dataSource.set(row, hold);

            String name = (String) ((Vector) this.dataVector.get(row - 1)).get(1);

            ((Vector) this.dataVector.get(row - 1)).set(1, ((Vector) dataVector.get(row)).get(1));

            ((Vector) this.dataVector.get(row)).set(1, name);

            this.fireTableCellUpdated(row - 1, 1);

            this.fireTableCellUpdated(row, 1);

        } else if ((row > Math.max(1,dataSource.size())) && (row < dataVector.size())) {

            int adjustedRow = 1;

            if ((row != 0) || (dataSource.size() != 0)) {

                adjustedRow = row - dataSource.size();

            }

            Algorithm hold = algorithms.get(adjustedRow - 1);

            algorithms.set(adjustedRow - 1, algorithms.get(adjustedRow));

            algorithms.set(adjustedRow, hold);

            String holdPattern = algorithmsPatterns.get(adjustedRow - 1);

            algorithmsPatterns.set(adjustedRow - 1, algorithmsPatterns.get(adjustedRow));

            algorithmsPatterns.set(adjustedRow, holdPattern);

            String name = (String) ((Vector) this.dataVector.get(row - 1)).get(1);

            ((Vector) this.dataVector.get(row - 1)).set(1, ((Vector) this.dataVector.get(row)).get(1));

            ((Vector) this.dataVector.get(row)).set(1, name);

            this.fireTableCellUpdated(row - 1, 1);

            this.fireTableCellUpdated(row, 1);

        } else if ((row != 0) && (row != Math.max(1, dataSource.size()))) {

            Logger.getLogger(ApplicationTableModel.class.getName()).log(Level.SEVERE, "MoveDown had bad row '" + row + "'");

        }

    }



    /**

     * move the algorithm one step down (moving the one below it to the given row)

     * if the row is valid or log an error message.

     * @param row location of the algorithm to move

     */

    public void moveDown(int row) {

        int adjustedRow = 1;

        if (row < (dataSource.size() - 1)) {

            DataAquisition hold = dataSource.get(row + 1);

            dataSource.set(row + 1, dataSource.get(row));

            dataSource.set(row, hold);

            String name = (String) ((Vector) this.dataVector.get(row + 1)).get(1);

            ((Vector) this.dataVector.get(row + 1)).set(1, ((Vector) dataVector.get(row)).get(1));

            ((Vector) this.dataVector.get(row)).set(1, name);

            this.fireTableCellUpdated(row + 1, 1);

            this.fireTableCellUpdated(row, 1);

        } else if ((row >= Math.max(1, dataSource.size())) && (row < (dataVector.size() - 1))) {

            if ((row != 0) || (dataSource.size() != 0)) {

                adjustedRow = row - dataSource.size();

            }



            Algorithm hold = algorithms.get(adjustedRow + 1);

            algorithms.set(adjustedRow + 1, algorithms.get(adjustedRow));

            algorithms.set(adjustedRow, hold);

            String holdPattern = algorithmsPatterns.get(adjustedRow + 1);

            algorithmsPatterns.set(adjustedRow + 1, algorithmsPatterns.get(adjustedRow));

            algorithmsPatterns.set(adjustedRow, holdPattern);

            String name = (String) ((Vector) this.dataVector.get(row + 1)).get(1);

            ((Vector) this.dataVector.get(row + 1)).set(1, ((Vector) this.dataVector.get(row)).get(1));

            ((Vector) this.dataVector.get(row)).set(1, name);

            this.fireTableCellUpdated(row + 1, 1);

            this.fireTableCellUpdated(row, 1);

        } else if ((row!=dataVector.size()-1)&&(row != dataSource.size()-1)) {

            Logger.getLogger(ApplicationTableModel.class.getName()).log(Level.SEVERE, "MoveDown had bad row '" + row + "'");

        }

    }



    /**

     * Return the component at the given row.  Returns null if there is no component at the 

     * row provided.

     * @param row location of the component to retrieve

     * @return component on this row

     */

    public Component getComponent(

            int row) {

        if ((row == 0) && (dataSource.size() == 0)) {

            return null;

        }



        if (row < dataSource.size()) {

            return dataSource.get(row);

        } else if ((row > dataSource.size()) && (row < dataVector.size())) {

            if (dataSource.size() == 0) {

                return algorithms.get(row - 1);

            } else {

                return algorithms.get(row - dataSource.size());

            }







        } else {

            Logger.getLogger(ApplicationTableModel.class.getName()).log(Level.SEVERE, "getComponent had bad row '" + row + "'");







            return null;

        }

    }



    /**

     * Return the data aquisition module or null if it has not been set yet

     * @return data aquisition module of this application

     */

    public DataAquisition[] getDataAquisition() {

        return dataSource.toArray(new DataAquisition[]{});

    }



    /**

     * Return the algorithms of this application in an ordered array.  Returns an empty

     * array if no algorithms are present.

     * @return array of algorithms

     */

    public Algorithm[] getAlgorithms() {

        return algorithms.toArray(new Algorithm[]{});

    }



    /**

     * Sets the regular expression describing the graph(s) which will be executed against

     * the algorithm at this location.  See Graph.getGraphs(Pattern) for a description 

     * of which graphs are returned using this pattern. This logs an error if the row

     * does not resolve to an Algorithm object.

     * @param row which algorithm to set a pattern for 

     * @param pattern regular expression describing which graph objects to execute

     * against this algorithm.

     */

    public void setPattern(int row, String pattern) {

        if ((row > 0) && (row <= algorithmsPatterns.size())) {

            if (dataSource.size() == 0) {

                algorithmsPatterns.set(row - 1, pattern);

            } else {

                algorithmsPatterns.set(row - dataSource.size(), pattern);

            }





        } else {

            Logger.getLogger(ApplicationTableModel.class.getName()).log(Level.SEVERE, "Out of bounds - algorithms size '" + algorithmsPatterns.size() + "' but accessed row '" + row + "'");

        }

    }



    /**

     * Returns the pattern object associated with the Algorithm at this row.  Returns

     * null if the row is not associated with a valid Algorithm object.

     * @param row which algorithm's pattern to return

     * @return string representation of this pattern.

     */

    public String getPattern(

            int row) {

        if ((row > 0) && (row <= algorithmsPatterns.size())) {

            Logger.getLogger(ApplicationTableModel.class.getName()).log(Level.SEVERE, "Out of bounds - algorithms size '" + algorithmsPatterns.size() + "' but accessed row '" + row + "'");







            if (dataSource.size() == 0) {

                return algorithmsPatterns.get(row - 1);

            } else {

                return algorithmsPatterns.get(row - dataSource.size());

            }

        } else {

            return null;

        }



    }



    /**

     * Returns the number of data aquisition rows in the table

     * @return

     */

    public int getDataAquisitionCount() {

        return Math.max(1, dataSource.size());

    }

}

