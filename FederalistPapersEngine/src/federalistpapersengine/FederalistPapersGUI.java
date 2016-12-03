/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package federalistpapersengine;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
/**
 *
 * @author Tim
 */
public class FederalistPapersGUI extends javax.swing.JFrame {

    /**
     * Creates new form FederalistPapersGUI
     */
    public FederalistPapersGUI() {
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

        directoryChooser = new javax.swing.JFileChooser();
        mainPanel = new javax.swing.JPanel();
        hamiltonPanel = new javax.swing.JPanel();
        hamiltonScrollPane = new javax.swing.JScrollPane();
        hamiltonTable = new javax.swing.JTable();
        madisonPanel = new javax.swing.JPanel();
        madisonScrollPane = new javax.swing.JScrollPane();
        madisonTable = new javax.swing.JTable();
        jayPanel = new javax.swing.JPanel();
        jayScrollPane = new javax.swing.JScrollPane();
        jayTable = new javax.swing.JTable();
        rocchioButton = new javax.swing.JButton();
        bayesianButton = new javax.swing.JButton();
        hamiltonLabel = new javax.swing.JLabel();
        madisonLabel = new javax.swing.JLabel();
        jayLabel = new javax.swing.JLabel();
        hamiltonFileLabel = new javax.swing.JLabel();
        madisonFileLabel = new javax.swing.JLabel();
        jayFileLabel = new javax.swing.JLabel();
        fileMenuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        openMenuItem = new javax.swing.JMenuItem();

        directoryChooser.setFileSelectionMode(javax.swing.JFileChooser.DIRECTORIES_ONLY);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Who Wrote the Federalist Papers?");
        getContentPane().setLayout(new java.awt.GridLayout());

        hamiltonTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Papers"
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
        hamiltonTable.getSelectionModel().addListSelectionListener(new
            ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    int row = hamiltonTable.getSelectedRow();

                    if(row >= 0 && !e.getValueIsAdjusting() && !hamiltonTable.getSelectionModel()
                        .isSelectionEmpty()) {
                        String file = hamiltonTable.getValueAt(row, 0).toString();

                        if(file.endsWith(".txt")) {
                            openFile(file);
                        }
                    }
                }
            });
            hamiltonScrollPane.setViewportView(hamiltonTable);

            javax.swing.GroupLayout hamiltonPanelLayout = new javax.swing.GroupLayout(hamiltonPanel);
            hamiltonPanel.setLayout(hamiltonPanelLayout);
            hamiltonPanelLayout.setHorizontalGroup(
                hamiltonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(hamiltonScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
            );
            hamiltonPanelLayout.setVerticalGroup(
                hamiltonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(hamiltonScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            );

            madisonTable.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {

                },
                new String [] {
                    "Papers"
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
            madisonTable.getSelectionModel().addListSelectionListener(new
                ListSelectionListener() {
                    @Override
                    public void valueChanged(ListSelectionEvent e) {
                        int row = madisonTable.getSelectedRow();

                        if(row >= 0 && !e.getValueIsAdjusting() && !madisonTable.getSelectionModel()
                            .isSelectionEmpty()) {
                            String file = madisonTable.getValueAt(row, 0).toString();

                            if(file.endsWith(".txt")) {
                                openFile(file);
                            }
                        }
                    }
                });
                madisonScrollPane.setViewportView(madisonTable);

                javax.swing.GroupLayout madisonPanelLayout = new javax.swing.GroupLayout(madisonPanel);
                madisonPanel.setLayout(madisonPanelLayout);
                madisonPanelLayout.setHorizontalGroup(
                    madisonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(madisonScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                );
                madisonPanelLayout.setVerticalGroup(
                    madisonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(madisonScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                );

                jayTable.setModel(new javax.swing.table.DefaultTableModel(
                    new Object [][] {

                    },
                    new String [] {
                        "Papers"
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
                jayTable.getSelectionModel().addListSelectionListener(new
                    ListSelectionListener() {
                        @Override
                        public void valueChanged(ListSelectionEvent e) {
                            int row = jayTable.getSelectedRow();

                            if(row >= 0 && !e.getValueIsAdjusting() && !jayTable.getSelectionModel()
                                .isSelectionEmpty()) {
                                String file = jayTable.getValueAt(row, 0).toString();

                                if(file.endsWith(".txt")) {
                                    openFile(file);
                                }
                            }
                        }
                    });
                    jayScrollPane.setViewportView(jayTable);

                    javax.swing.GroupLayout jayPanelLayout = new javax.swing.GroupLayout(jayPanel);
                    jayPanel.setLayout(jayPanelLayout);
                    jayPanelLayout.setHorizontalGroup(
                        jayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jayScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)
                    );
                    jayPanelLayout.setVerticalGroup(
                        jayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jayScrollPane)
                    );

                    rocchioButton.setText("Rocchio Classification");
                    rocchioButton.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            rocchioButtonActionPerformed(evt);
                        }
                    });

                    bayesianButton.setText("Bayesian Classification");
                    bayesianButton.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            bayesianButtonActionPerformed(evt);
                        }
                    });

                    hamiltonLabel.setText("Hamilton:");

                    madisonLabel.setText("Madison:");

                    jayLabel.setText("Jay:");

                    hamiltonFileLabel.setText("Documents found: ");

                    madisonFileLabel.setText("Documents found:");

                    jayFileLabel.setText("Documents found:");

                    javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
                    mainPanel.setLayout(mainPanelLayout);
                    mainPanelLayout.setHorizontalGroup(
                        mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(mainPanelLayout.createSequentialGroup()
                            .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(mainPanelLayout.createSequentialGroup()
                                    .addContainerGap()
                                    .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(hamiltonLabel)
                                        .addComponent(hamiltonPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(hamiltonFileLabel))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(madisonLabel)
                                        .addComponent(madisonPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(madisonFileLabel))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jayFileLabel)
                                        .addComponent(jayLabel)
                                        .addComponent(jayPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(mainPanelLayout.createSequentialGroup()
                                    .addGap(125, 125, 125)
                                    .addComponent(rocchioButton)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(bayesianButton)))
                            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    );
                    mainPanelLayout.setVerticalGroup(
                        mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(mainPanelLayout.createSequentialGroup()
                            .addGap(12, 12, 12)
                            .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(hamiltonLabel)
                                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(madisonLabel)
                                    .addComponent(jayLabel)))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(madisonPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(hamiltonPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jayPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(hamiltonFileLabel)
                                .addComponent(madisonFileLabel)
                                .addComponent(jayFileLabel))
                            .addGap(18, 18, 18)
                            .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(rocchioButton)
                                .addComponent(bayesianButton))
                            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    );

                    getContentPane().add(mainPanel);

                    fileMenu.setText("File");

                    openMenuItem.setText("Open Directory");
                    openMenuItem.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            openMenuItemActionPerformed(evt);
                        }
                    });
                    fileMenu.add(openMenuItem);

                    fileMenuBar.add(fileMenu);

                    setJMenuBar(fileMenuBar);

                    pack();
                }// </editor-fold>//GEN-END:initComponents

    private void openMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openMenuItemActionPerformed
        if (directoryChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            String currentDir = directoryChooser.getSelectedFile()
                    .toString();
            /*DirectoryIndex.buildIndexForDirectory(currentDir);
                DirectoryBiwordIndex.buildIndexForDirectory(currentDir);
                dindex = new DiskInvertedIndex(currentDir);
             */
            JOptionPane.showMessageDialog(this, "Successfully indexed "
                    + currentDir + " files.", "Indexed",
                    JOptionPane.INFORMATION_MESSAGE);
        }   
    }//GEN-LAST:event_openMenuItemActionPerformed

    private void rocchioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rocchioButtonActionPerformed
        // pass info to rocchio
    }//GEN-LAST:event_rocchioButtonActionPerformed

    private void bayesianButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bayesianButtonActionPerformed
        // pass info to bayesian
    }//GEN-LAST:event_bayesianButtonActionPerformed

    /**
     * When a user clicks on the file it opens a new window containing the
     * contents of the file
     *
     * @param file
     */
    private void openFile(String file) {
        JFrame frame = new JFrame(file);
        frame.setMinimumSize(new Dimension(800, 600));
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 0));
        JLabel label = new JLabel();
        panel.setBackground(Color.WHITE);
        String folder = getFolder(file);
        
        try {
            BufferedReader br = new BufferedReader(new FileReader(folder));
            String line;
            StringBuilder sb = new StringBuilder();
            
            while((line = br.readLine()) != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
            }

            label.setText("<html><div WIDTH=600px>" + sb.toString() 
                    + "</div><html>");
        } catch (IOException ex) {
            Logger.getLogger(FederalistPapersGUI.class.getName())
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
     * Returns a folder path of where the file is located
     * @param file name of file
     * @return 
     */
    private String getFolder(String file) {
        String path = directoryChooser.getSelectedFile().getAbsolutePath();
        int num = Integer.parseInt(file.substring(6, 7));
        
        if((num >= 49 && num <= 57) || num == 62 || num == 63) {
            return path + "\\HAMILTON OR MADISON\\" + file;
        }
        else {
            return path + "\\ALL\\" + file;
        }
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
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FederalistPapersGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FederalistPapersGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FederalistPapersGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FederalistPapersGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FederalistPapersGUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bayesianButton;
    private javax.swing.JFileChooser directoryChooser;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenuBar fileMenuBar;
    private javax.swing.JLabel hamiltonFileLabel;
    private javax.swing.JLabel hamiltonLabel;
    private javax.swing.JPanel hamiltonPanel;
    private javax.swing.JScrollPane hamiltonScrollPane;
    private javax.swing.JTable hamiltonTable;
    private javax.swing.JLabel jayFileLabel;
    private javax.swing.JLabel jayLabel;
    private javax.swing.JPanel jayPanel;
    private javax.swing.JScrollPane jayScrollPane;
    private javax.swing.JTable jayTable;
    private javax.swing.JLabel madisonFileLabel;
    private javax.swing.JLabel madisonLabel;
    private javax.swing.JPanel madisonPanel;
    private javax.swing.JScrollPane madisonScrollPane;
    private javax.swing.JTable madisonTable;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuItem openMenuItem;
    private javax.swing.JButton rocchioButton;
    // End of variables declaration//GEN-END:variables
}
