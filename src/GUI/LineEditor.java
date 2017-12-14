/*
 * Copyright (C) 2017 emnga
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package GUI;

import Core.Assembler.Instruction.Line;
import java.util.ArrayList;

/**
 *
 * @author emnga
 */
public class LineEditor extends javax.swing.JFrame {

    /**
     * Creates new form LineEditor
     */
    public AssemblerEditor parent;
    public Line line;
    public boolean isNew;
    public ArrayList<Line> auxInstructions;

    public void initLists() {
        this.auxInstructions = new ArrayList<>();
        this.auxInstructions.addAll(parent.auxDirectives);
        this.auxInstructions.addAll(parent.auxInstructions);
        this.auxInstructions.addAll(parent.auxLines);
        this.auxInstructions.addAll(parent.auxVarUsages);
    }

    public LineEditor(AssemblerEditor parent) {
        this.parent = parent;
        initLists();
        this.isNew = true;
        initComponents();
    }

    public LineEditor(AssemblerEditor parent, Line line, boolean isDefault) {
        this.parent = parent;
        initLists();
        this.line = line;
        this.isNew = false;
        initComponents();
        initData();
        this.setTitle(this.getTitle() + line.getIdentifier());
        if (isDefault) {
            this.jButton3.setEnabled(false);
            this.txtIdentifier.setEnabled(false);
            this.txtWarning.setVisible(true);
            this.txtWarning.setText("Warning: changing data\nfrom default lines\ncan cause problems.\nUse with caution.");
            pack();
        } else {
            this.txtWarning.setVisible(false);
            pack();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        txtIdentifier = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtSintax = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        txtWarning = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Line Editor: ");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLabel1.setText("Identifier");

        txtIdentifier.setMaximumSize(new java.awt.Dimension(6, 20));

        jLabel2.setText("Sintax");

        txtSintax.setMaximumSize(new java.awt.Dimension(6, 20));

        jButton1.setText("Accept");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Cancel");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Delete");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtWarning, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addComponent(jLabel2))
                            .addComponent(jLabel1))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtIdentifier, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtSintax, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtIdentifier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtSintax, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtWarning, javax.swing.GroupLayout.DEFAULT_SIZE, 77, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        int selection = DialogCreator.createYesNoDialog(this, "Are you sure you want to exit?", "Exiting");
        if (selection == 0) {
            this.parent.setEnabled(true);
            this.parent.toFront();
            this.dispose();
        }
    }//GEN-LAST:event_formWindowClosing

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        String identifier = this.txtIdentifier.getText();
        String sintax = this.txtSintax.getText();
        if (isNew) {
            for (Line lin : this.auxInstructions) {
                if (lin.identifier.equals(identifier)) {
                    DialogCreator.createErrorDialog(this, "Error", "Identifier already defined");
                    return;
                }
            }
            Line lin = new Line(identifier, sintax);
            this.parent.auxLines.add(lin);
            this.parent.updateLines();
            close();
        } else {
            for (Line lin : this.auxInstructions) {
                if (lin.identifier.equals(identifier) || !line.identifier.equals(lin.identifier)) {
                    DialogCreator.createErrorDialog(this, "Error", "Identifier already defined");
                    return;
                }
            }
            this.line.identifier = identifier;
            this.line.setSintax(this.txtSintax.getText());
            this.parent.updateLines();
            this.close();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        this.close();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        if(DialogCreator.createYesNoDialog(this, "Are you sure you want to\ndelete this line?\nThis can't be undone", "WARNING")==0){
            this.parent.deleteLine(this.line);
            this.parent.updateLines();
            close();
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void initData() {
        this.txtIdentifier.setText(this.line.getIdentifier());
        this.txtSintax.setText(this.line.sintax.getOriginal());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JTextField txtIdentifier;
    private javax.swing.JTextField txtSintax;
    private javax.swing.JLabel txtWarning;
    // End of variables declaration//GEN-END:variables

    private void close() {
        this.parent.toFront();
        this.parent.setEnabled(true);
        this.dispose();
    }
}
