/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package searchengineproject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Timothy
 */
public class SearchEngineGUI extends javax.swing.JFrame {
    String currentDirectory = "";
    DiskInvertedIndex dindex = null;
    
    /**
     * Creates new form SearchEngineGUI
     */
    public SearchEngineGUI() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fileChooser = new javax.swing.JFileChooser();
        queryModeButtonGroup = new javax.swing.ButtonGroup();
        mainLayout = new javax.swing.JPanel();
        searchBar = new javax.swing.JTextField();
        searchButton = new javax.swing.JButton();
        stemButton = new javax.swing.JButton();
        tableScrollPane = new javax.swing.JScrollPane();
        docTable = new javax.swing.JTable();
        docsFoundLabel = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        openMenu = new javax.swing.JMenuItem();
        openExistingMenu = new javax.swing.JMenuItem();
        exitMenu = new javax.swing.JMenuItem();
        viewMenu = new javax.swing.JMenu();
        vocabMenu = new javax.swing.JMenuItem();
        biwordVocab = new javax.swing.JMenuItem();
        optionsMenu = new javax.swing.JMenu();
        booleanOption = new javax.swing.JCheckBoxMenuItem();
        rankedRetMenu = new javax.swing.JMenu();
        defaultOption = new javax.swing.JCheckBoxMenuItem();
        tfidfOption = new javax.swing.JCheckBoxMenuItem();
        okapiOption = new javax.swing.JCheckBoxMenuItem();
        wackyOption = new javax.swing.JCheckBoxMenuItem();

        fileChooser.setFileSelectionMode(javax.swing.JFileChooser.DIRECTORIES_ONLY);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new java.awt.GridLayout(1, 0));

        mainLayout.setPreferredSize(new java.awt.Dimension(550, 300));
        mainLayout.setLayout(null);

        searchBar.setToolTipText("");
        searchBar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchBarActionPerformed(evt);
            }
        });
        mainLayout.add(searchBar);
        searchBar.setBounds(10, 10, 350, 30);

        searchButton.setText("Search");
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });
        mainLayout.add(searchButton);
        searchButton.setBounds(460, 10, 80, 30);

        stemButton.setText("Stem");
        stemButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stemButtonActionPerformed(evt);
            }
        });
        mainLayout.add(stemButton);
        stemButton.setBounds(370, 10, 80, 30);

        docTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Documents"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        docTable.getSelectionModel().addListSelectionListener(new
            ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    int row = docTable.getSelectedRow();

                    if(row >= 0 && !e.getValueIsAdjusting() && !docTable.getSelectionModel()
                        .isSelectionEmpty()) {
                        String file = docTable.getValueAt(row, 0).toString();

                        if(file.endsWith(".json")) {
                            openFile(file);
                        }
                    }
                }
            });
            tableScrollPane.setViewportView(docTable);
            if (docTable.getColumnModel().getColumnCount() > 0) {
                docTable.getColumnModel().getColumn(0).setResizable(false);
            }

            mainLayout.add(tableScrollPane);
            tableScrollPane.setBounds(10, 50, 530, 220);

            docsFoundLabel.setText("Documents found:");
            mainLayout.add(docsFoundLabel);
            docsFoundLabel.setBounds(20, 280, 380, 14);

            getContentPane().add(mainLayout);

            fileMenu.setText("File");

            openMenu.setText("Open Directory");
            openMenu.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    openMenuActionPerformed(evt);
                }
            });
            fileMenu.add(openMenu);

            openExistingMenu.setText("Open Existing Directory");
            openExistingMenu.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    openExistingMenuActionPerformed(evt);
                }
            });
            fileMenu.add(openExistingMenu);

            exitMenu.setText("Exit");
            exitMenu.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    exitMenuActionPerformed(evt);
                }
            });
            fileMenu.add(exitMenu);

            jMenuBar1.add(fileMenu);

            viewMenu.setText("View");

            vocabMenu.setText("Vocabulary");
            vocabMenu.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    vocabMenuActionPerformed(evt);
                }
            });
            viewMenu.add(vocabMenu);

            biwordVocab.setText("Biword Vocabulary");
            biwordVocab.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    biwordVocabActionPerformed(evt);
                }
            });
            viewMenu.add(biwordVocab);

            jMenuBar1.add(viewMenu);

            optionsMenu.setText("Options");

            queryModeButtonGroup.add(booleanOption);
            booleanOption.setSelected(true);
            booleanOption.setText("Boolean Query");
            optionsMenu.add(booleanOption);

            rankedRetMenu.setText("Ranked Retrieval");

            queryModeButtonGroup.add(defaultOption);
            defaultOption.setText("Default");
            rankedRetMenu.add(defaultOption);

            queryModeButtonGroup.add(tfidfOption);
            tfidfOption.setText("tf-idf");
            rankedRetMenu.add(tfidfOption);

            queryModeButtonGroup.add(okapiOption);
            okapiOption.setText("Okapi");
            rankedRetMenu.add(okapiOption);

            queryModeButtonGroup.add(wackyOption);
            wackyOption.setText("Wacky");
            rankedRetMenu.add(wackyOption);

            optionsMenu.add(rankedRetMenu);

            jMenuBar1.add(optionsMenu);

            setJMenuBar(jMenuBar1);

            pack();
        }// </editor-fold>//GEN-END:initComponents

    private void exitMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuActionPerformed
        System.exit(0);
    }//GEN-LAST:event_exitMenuActionPerformed
    /**
     * Opens up the file chooser to let the user select a directory then index
     * the directory and pop up a dialog specifying how many files were indexed
     * @param evt 
     */
    private void openMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openMenuActionPerformed
        if(fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                String currentDir = fileChooser.getSelectedFile()
                        .toString();
                DirectoryIndex.buildIndexForDirectory(currentDir);
                DirectoryBiwordIndex.buildIndexForDirectory(currentDir);
                dindex = new DiskInvertedIndex(currentDir);
                //creat biword disk index here
                JOptionPane.showMessageDialog(this, "Successfully indexed "
                        + dindex.getDocumentCount() + " files.", "Indexed",
                        JOptionPane.INFORMATION_MESSAGE);
                
            } catch (IOException ex) {
                Logger.getLogger(SearchEngineGUI.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        }   
    }//GEN-LAST:event_openMenuActionPerformed

    /**
     * Calls the search button event when the user pushes enter in the search
     * bar
     * @param evt 
     */
    private void searchBarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchBarActionPerformed
        searchButtonActionPerformed(evt);
    }//GEN-LAST:event_searchBarActionPerformed
    
    /**
     * Creates the vocabulary table for the positional inverted index when
     * selected from the drop-down menu
     * @param evt 
     */
    private void vocabMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vocabMenuActionPerformed
        resultsTableBuilder(dindex.getPositionalIndexTerms(), "Terms", 1);
    }//GEN-LAST:event_vocabMenuActionPerformed
    
    /**
     * Stem the query in the search bar when the stem button is pushed and
     * output the stemmed word in the table
     * @param evt 
     */
    private void stemButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stemButtonActionPerformed
        String token = searchBar.getText();
        ArrayList<String> stemmedToken = new ArrayList<>();
        stemmedToken.add(PorterStemmer.processToken(token));
        resultsTableBuilder(stemmedToken, "Stemmed Term", 0);
    }//GEN-LAST:event_stemButtonActionPerformed

    /**
     * Retrieves the string variable from the search bar then get the results
     * for the query to display in the table
     * @param evt 
     */
    private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchButtonActionPerformed
        String query = searchBar.getText();
        query = query.replaceAll("[-]+|[\']", "");
        
        if(queryModeButtonGroup.isSelected(booleanOption.getModel())) {
            booleanRetrieval(query);
        }
        else {
            Strategy[] algorithms = {new DefaultRankedRetrieval(),
                new tfidfRankedRetrieval(), new OkapiRankedRetrieval(),
                new WackyRankedRetrieval()};
            
            if(queryModeButtonGroup.isSelected(defaultOption.getModel())) {
                rankedRetrievalSelection(algorithms[0], query);
            }
            else if(queryModeButtonGroup.isSelected(tfidfOption.getModel())) {
                rankedRetrievalSelection(algorithms[1], query);
            }
            else if(queryModeButtonGroup.isSelected(okapiOption.getModel())) {
                rankedRetrievalSelection(algorithms[2], query);
            }
            else {
                rankedRetrievalSelection(algorithms[3], query);
            }
        }
    }//GEN-LAST:event_searchButtonActionPerformed
    
    /**
     * Option for boolean query retrieval
     * @param query 
     */
    private void booleanRetrieval(String query) {
        resultsTableBuilder(ParseQueries.searchResults(query, dindex)
                , "Documents", 0);
    }
    
    /**
     * Option for ranked query retrieval
     * @param strat
     * @param query 
     */
    private void rankedRetrievalSelection(Strategy strat, String query) {
        resultsTableBuilder(strat.rankingAlgorithm(query), "Documents", 0);
        
    }
    
    /**
     * Method to build a table to display in the GUI
     * @param results the list of strings to display in the table
     * @param name the name of the column
     * @param type the type of operation, which alters the label at the bottom
     */
    private void resultsTableBuilder(ArrayList<String> results, 
            String name, int type) {
        String count = "";
        String columnNames[] = new String[] {name};
        DefaultTableModel model = (DefaultTableModel) docTable.getModel();
        model.setRowCount(0);
        model.setColumnIdentifiers(columnNames);
        
        for(String file : results) {
            model.addRow(new Object[]{file});
        }
        
        if(type == 0) {
            count = "Documents found: ";
            
        }
        else {
            count = "Vocabulary found: ";
        }
        
        docsFoundLabel.setText(count + results.size());    
    }
    /**
     * Displays the vocabulary for the biword index when selected from the
     * drop-down menu in the table
     * @param evt 
     */
    private void biwordVocabActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_biwordVocabActionPerformed
        resultsTableBuilder(dindex.getBiwordIndexTerms(), "Terms", 1);
    }//GEN-LAST:event_biwordVocabActionPerformed

    /**
     * Used to select a directory which already have the bin files
     * @param evt 
     */
    private void openExistingMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openExistingMenuActionPerformed
        if(fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                String currentDir = fileChooser.getSelectedFile()
                        .toString();
                dindex = new DiskInvertedIndex(currentDir);
                //creat biword disk index here
                JOptionPane.showMessageDialog(this, "Successfully indexed "
                        + dindex.getDocumentCount() + " files.", "Indexed",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                Logger.getLogger(SearchEngineGUI.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        }  
    }//GEN-LAST:event_openExistingMenuActionPerformed

    /**
     * When a user clicks on the file it opens a new window containing the
     * contents of the file
     * @param file 
     */
    private void openFile(String file) {
        JFrame frame = new JFrame(file);
        frame.setMinimumSize(new Dimension(800, 600));
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1,0));
        JLabel label = new JLabel(); 
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        
        try {
            NPSDocument json = gson.fromJson(new FileReader(fileChooser
                    .getSelectedFile() + "\\" + file), NPSDocument.class);
            label.setText("<html><div WIDTH=600px>" + json.title + "<br>"
                    + json.body + "<br>" + json.url + "</div><html>");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SearchEngineGUI.class.getName())
                    .log(Level.SEVERE, null, ex);
        }

        label.setHorizontalAlignment(JLabel.LEFT);
        label.setVerticalAlignment(JLabel.NORTH);
        panel.add(label);
        JScrollPane scrollBar = new JScrollPane(panel);
        frame.add(scrollBar);
        frame.pack();
        frame.setVisible(true);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing
                    .UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException 
                | IllegalAccessException 
                | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SearchEngineGUI.class.getName())
                    .log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        //</editor-fold>
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SearchEngineGUI().setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem biwordVocab;
    private javax.swing.JCheckBoxMenuItem booleanOption;
    private javax.swing.JCheckBoxMenuItem defaultOption;
    private javax.swing.JTable docTable;
    private javax.swing.JLabel docsFoundLabel;
    private javax.swing.JMenuItem exitMenu;
    private javax.swing.JFileChooser fileChooser;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel mainLayout;
    private javax.swing.JCheckBoxMenuItem okapiOption;
    private javax.swing.JMenuItem openExistingMenu;
    private javax.swing.JMenuItem openMenu;
    private javax.swing.JMenu optionsMenu;
    private javax.swing.ButtonGroup queryModeButtonGroup;
    private javax.swing.JMenu rankedRetMenu;
    private javax.swing.JTextField searchBar;
    private javax.swing.JButton searchButton;
    private javax.swing.JButton stemButton;
    private javax.swing.JScrollPane tableScrollPane;
    private javax.swing.JCheckBoxMenuItem tfidfOption;
    private javax.swing.JMenu viewMenu;
    private javax.swing.JMenuItem vocabMenu;
    private javax.swing.JCheckBoxMenuItem wackyOption;
    // End of variables declaration//GEN-END:variables
}
