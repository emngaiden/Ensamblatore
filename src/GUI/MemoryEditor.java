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

import Core.Architecture.Architecture;
import Core.Architecture.Memory.Mapping;
import Core.Architecture.Memory.Memory;
import Core.Architecture.Sector;
import Core.Architecture.Word;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import javax.swing.DefaultListModel;
import javax.swing.JList;

/**
 *
 * @author emnga
 */
public class MemoryEditor extends javax.swing.JFrame {

    /**
     * Creates new form MemoryEditor
     */
    public MemoriesEditor parent;
    public Memory memory;
    public Word[] auxData;
    public boolean isNew;
    private String other = "-other-";
    public int from;

    @SuppressWarnings("unchecked")
    public MemoryEditor(MemoriesEditor parent, Memory memory) {
        this.isNew = false;
        this.parent = parent;
        this.memory = memory;
        this.auxData = this.memory.data.clone();
        initComponents();
        this.jLabel7.setText("<html><pre>WARNING: <br>Modifying this values<br>will reset all memory<br>data</pre></html>");
        pack();
        this.jList1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                JList list = (JList) evt.getSource();
                int index = list.locationToIndex(evt.getPoint());
                if (evt.getClickCount() == 2 && index != -1) {
                    ((DefaultListModel) MemoryEditor.this.jList1.getModel()).removeElementAt(index);
                }
            }
        });
        this.jList2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                JList list = (JList) evt.getSource();
                int index = list.locationToIndex(evt.getPoint());
                if (evt.getClickCount() == 2 && index != -1) {
                    Object actualValue = list.getSelectedValue();
                    String s = null;
                    String[] splited = actualValue.toString().split(",");
                    String binaryValue = splited[2];
                    boolean error = false;
                    do {
                        try {
                            if (!error) {
                                s = DialogCreator.createInputDialogWithText(MemoryEditor.this, "Edit value", "Please enter new binary value\nlsb=right", null, binaryValue).toString();
                            } else {
                                s = DialogCreator.createInputDialogWithText(MemoryEditor.this, "Edit value", "Please enter a valid binary value\nlsb=right", null, s).toString();
                            }
                            error = (s == null || s.isEmpty() || !s.matches("[01]+"));
                        } catch (NullPointerException e) {

                        }
                    } while (error);
                    if (s != null) {
                        ((DefaultListModel) list.getModel()).setElementAt(splited[0] + "," + splited[1] + "," + s, index);
                    }
                }
            }
        });
        this.txtMName.setVisible(false);
        initData();
        this.cbName.addItemListener((ItemEvent ie) -> {
            int state1 = ie.getStateChange();
            if (state1 == ItemEvent.SELECTED) {
                if (ie.getItem().toString().equals(this.other)) {
                    MemoryEditor.this.txtMName.setVisible(true);
                    pack();
                } else {
                    MemoryEditor.this.txtMName.setVisible(false);
                    pack();
                }
            }
        });
    }

    public MemoryEditor(MemoriesEditor parent) {
        this.isNew = true;
        this.parent = parent;
        initComponents();
        this.jButton3.setEnabled(false);
        this.jList1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                JList list = (JList) evt.getSource();
                int index = list.locationToIndex(evt.getPoint());
                if (evt.getClickCount() == 2 && index != -1) {
                    ((DefaultListModel) MemoryEditor.this.jList1.getModel()).removeElementAt(index);
                }
            }
        });
        this.jList2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                JList list = (JList) evt.getSource();
                int index = list.locationToIndex(evt.getPoint());
                if (evt.getClickCount() == 2 && index != -1) {
                    Object actualValue = ((DefaultListModel) MemoryEditor.this.jList1.getModel()).getElementAt(index);
                    String s = null;
                    String[] splited = actualValue.toString().split(",");
                    String binaryValue = splited[2];
                    boolean error = false;
                    do {
                        if (!error) {
                            s = DialogCreator.createInputDialogWithText(MemoryEditor.this, "Edit value", "Please enter new binary value", null, binaryValue).toString();
                        } else {
                            s = DialogCreator.createInputDialogWithText(MemoryEditor.this, "Edit value", "Please enter a valid binary value", null, s).toString();
                        }
                        error = (s == null || s.isEmpty() || !s.matches("[01]+"));
                    } while (error);
                    MemoryEditor.this.auxData[from + index] = Word.parseWord(s);
                    setVisibleAddesses();
                }
            }
        });
        this.txtMName.setVisible(false);
        this.cbName.addItem(Architecture.DATAMEMORYNAME);
        this.cbName.addItem(Architecture.PROGRAMMEMORYNAME);
        this.cbName.addItem(this.other);
        this.cbName.addItemListener((ItemEvent ie) -> {
            int state1 = ie.getStateChange();
            if (state1 == ItemEvent.SELECTED) {
                if (ie.getItem().toString().equals(this.other)) {
                    MemoryEditor.this.txtMName.setVisible(true);
                    pack();
                } else {
                    MemoryEditor.this.txtMName.setVisible(false);
                    pack();
                }
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        txtName = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        spnrFrom = new javax.swing.JSpinner();
        jLabel3 = new javax.swing.JLabel();
        spnrTo = new javax.swing.JSpinner();
        btnAdd = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        cbName = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        spnrRS = new javax.swing.JSpinner();
        jLabel6 = new javax.swing.JLabel();
        spnrS = new javax.swing.JSpinner();
        txtMName = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        btnCancel = new javax.swing.JButton();
        btnAccept = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList<>();
        spnrFrom1 = new javax.swing.JSpinner();
        spnrTo1 = new javax.swing.JSpinner();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Memory editor: ");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Mapping"));

        jList1.setModel(new DefaultListModel());
        jScrollPane1.setViewportView(jList1);

        jLabel1.setText("Name");

        jLabel2.setText("From");

        spnrFrom.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        jLabel3.setText("To");

        spnrTo.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        btnAdd.setText("Add");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtName))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(spnrFrom, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(spnrTo, javax.swing.GroupLayout.DEFAULT_SIZE, 59, Short.MAX_VALUE))
            .addComponent(btnAdd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(spnrFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(spnrTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAdd)
                .addGap(0, 20, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Properties"));

        jLabel4.setText("Name");

        jLabel5.setText("Register Size");

        spnrRS.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        jLabel6.setText("Size");

        spnrS.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        jLabel7.setText("jLabel7");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(spnrRS, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE)
                            .addComponent(spnrS)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtMName, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(cbName, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(cbName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtMName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(spnrRS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(spnrS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        btnAccept.setText("Accept");
        btnAccept.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAcceptActionPerformed(evt);
            }
        });

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Data"));

        jList2.setModel(new DefaultListModel());
        jScrollPane2.setViewportView(jList2);

        spnrFrom1.setModel(new javax.swing.SpinnerNumberModel(10, 0, null, 1));

        spnrTo1.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        jLabel10.setText("From");

        jLabel11.setText("Show");

        jButton1.setText("Show");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Clear all");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(spnrFrom1, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(spnrTo1, javax.swing.GroupLayout.DEFAULT_SIZE, 51, Short.MAX_VALUE))
            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(spnrFrom1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(spnrTo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)))
        );

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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 8, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnAccept)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancel))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancel)
                    .addComponent(btnAccept)
                    .addComponent(jButton3))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    @SuppressWarnings("unchecked")
    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        String sname = this.txtName.getText();
        if (sname.isEmpty()) {
            DialogCreator.createErrorDialog(this, "Error", "Sector name can't be empty");
            return;
        }
        int from = (int) this.spnrFrom.getValue();
        int to = (int) this.spnrTo.getValue();
        if (from > to) {
            DialogCreator.createErrorDialog(this, "Error", "Invalid sector size: from " + from + " to " + to);
            return;
        }
        DefaultListModel model = (DefaultListModel) this.jList1.getModel();
        for (Object o : model.toArray()) {
            if (o.toString().contains(sname)) {
                DialogCreator.createErrorDialog(this, "Error", "Sector " + sname + " already defined.");
                return;
            }
        }
        model.addElement(sname + "," + from + "," + to);
    }//GEN-LAST:event_btnAddActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        setVisibleAddesses();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        int selection = DialogCreator.createYesNoDialog(this, "This will wipe the memory data\nare you sure?", "Warning");
        if (selection == 0) {
            this.memory.clearData();
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void btnAcceptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAcceptActionPerformed
        int registerSize ;
        int size;
        String name=null;
        try{
        registerSize = (int) spnrRS.getValue();
        size = (int) spnrS.getValue();
        name = cbName.getSelectedItem().toString();
        }catch(Exception e){
            DialogCreator.createErrorDialog(this, "Error", "Invalid Data");
            return;
        }
        if (name.equals(this.other)) {
            name = txtMName.getText();
        }
        if (!isNew) {
            switch (hasDataChanged()) {
                case 0:
                    this.memory.setData(auxData);
                    DefaultListModel model = (DefaultListModel) this.jList1.getModel();
                    String noadded = "";
                    this.memory.mapping = new Mapping();
                    if (!model.isEmpty()) {
                        for (Object d : model.toArray()) {
                            Sector sect = Mapping.parseSector(d.toString());
                            if (!this.memory.mapping.addSector(sect)) {
                                noadded += sect.name + " ";
                            }
                        }
                    }
                    if (!noadded.isEmpty()) {
                        DialogCreator.createErrorDialog(this, "Warning", "Error adding the following sectors:\n" + (noadded.replaceAll(" ", "\n")));
                        close();
                    }
                    close();
                    break;
                case 1:
                    boolean aux = !name.equals(this.memory.getIdentifier());
                    for (Memory mem : parent.auxMemories) {
                        if (mem.getIdentifier().equals(name) && aux) {
                            DialogCreator.createErrorDialog(this, "Error", "Memory name already in use");
                            return;
                        } else if (mem.getIdentifier().equals(name) && !aux) {
                            aux = true;
                        }
                    }
                    this.memory.sizeInBytes = size;
                    this.memory.registerSize = registerSize;
                    this.memory.setIdentifier(name);
                    this.memory.setParent(this.parent.parent.cpu.getArchitecture());
                    this.memory.init();
                    this.auxData = this.memory.getData().clone();
                    this.parent.updateData(memory);
                    this.parent.updateMemoriesData();
                    close();
                    break;
                case -1:
                    DialogCreator.createErrorDialog(this, "Empty value", "Empty memory value");
                    return;
            }
        } else {
            for (Memory mem : parent.auxMemories) {
                if (mem.getIdentifier().equals(name)) {
                    DialogCreator.createErrorDialog(this, "Error", "Memory name already in use");
                    return;
                }
            }
            Memory me=new Memory(size, registerSize, name);
            me.init();
            this.parent.addMemory(me);
            this.parent.updateMemoriesData();
            close();
        }
    }//GEN-LAST:event_btnAcceptActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        close();
    }//GEN-LAST:event_formWindowClosing

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        close();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        if(DialogCreator.createYesNoDialog(this, "Are you sure you want to delete\nthis memory?\nThis can't be undone", "WARNING")==0){
            this.parent.deleteMemory(this.memory);
            this.parent.updateMemoriesData();
            close();
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAccept;
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnCancel;
    private javax.swing.JComboBox<String> cbName;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JList<String> jList1;
    private javax.swing.JList<String> jList2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSpinner spnrFrom;
    private javax.swing.JSpinner spnrFrom1;
    private javax.swing.JSpinner spnrRS;
    private javax.swing.JSpinner spnrS;
    private javax.swing.JSpinner spnrTo;
    private javax.swing.JSpinner spnrTo1;
    private javax.swing.JTextField txtMName;
    private javax.swing.JTextField txtName;
    // End of variables declaration//GEN-END:variables

    private void initData() {
        this.cbName.addItem(Architecture.DATAMEMORYNAME);
        this.cbName.addItem(Architecture.PROGRAMMEMORYNAME);
        this.cbName.addItem(this.other);
        this.spnrRS.setValue(this.memory.registerSize);
        this.spnrS.setValue(this.memory.sizeInBytes);
        setVisibleAddesses();
        this.initMappingData();
    }

    @SuppressWarnings("unchecked")
    private void initMappingData() {
        DefaultListModel model = new DefaultListModel();
        for (Sector sect : this.memory.mapping.getSectors()) {
            StringBuilder sb = new StringBuilder(sect.name);
            sb.append(",").append(sect.start);
            sb.append(",").append(sect.finish);
            model.addElement(sb.toString());
        }
        this.jList1.setModel(model);
    }

    @SuppressWarnings("unchecked")
    private void setVisibleAddesses() {
        DefaultListModel model = (DefaultListModel) this.jList2.getModel();
        model.removeAllElements();
        int quantity = (int) spnrFrom1.getValue();
        int initial = (int) spnrTo1.getValue();
        this.from = initial;
        for (int i = 0; i < quantity; i++) {
            int intaux = initial + i;
            try {
                Memory.MemoryAdress aux = this.memory.getAdress(intaux);
                Word d = this.auxData[intaux];
                StringBuilder sb = new StringBuilder();
                sb.append(intaux);
                sb.append(',');
                if (aux.sector.isEmpty()) {
                    sb.append("-none-");
                } else {
                    sb.append(aux.sector);
                }
                sb.append(',');
                sb.append(d.toString());
                model.addElement(sb.toString());
            } catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
                System.out.println("catcheada");
                break;
            }
        }
    }

    private int hasDataChanged() {
        String name = cbName.getSelectedItem().toString();
        if (name.equals(this.other)) {
            String newName = txtMName.getText();
            if (newName.isEmpty()) {
                return -1;
            }
            name = newName;
        }
        if (!name.equals(this.memory.getIdentifier())) {
            return 1;
        }
        int registerSize = (int) spnrRS.getValue();
        if (registerSize != this.memory.getRegisterSize()) {
            return 1;
        }
        int size = (int) spnrS.getValue();
        if (size != this.memory.getSizeInBytes()) {
            return 1;
        }
        return 0;
    }

    private void close() {
        this.parent.toFront();
        this.parent.setEnabled(true);
        this.dispose();
    }
}
