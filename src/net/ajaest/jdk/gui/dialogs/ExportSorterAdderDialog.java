/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SorterAdderDialog.java
 *
 * Created on 25-nov-2010, 20:25:26
 */

package net.ajaest.jdk.gui.dialogs;

import javax.swing.JComboBox;

import net.ajaest.jdk.core.main.Messages;
import net.ajaest.jdk.core.winHandlers.ExportSorterAdderWH;

/**
 *
 * @author ajaest10
 */
public class ExportSorterAdderDialog extends javax.swing.JDialog {

	private static final long serialVersionUID = 1244443311826568628L;
	private ExportSorterAdderWH esawh;

    /** Creates new form SorterAdderDialog */
	public ExportSorterAdderDialog(java.awt.Frame parent, boolean modal, ExportSorterAdderWH esawh) {
        super(parent, modal);

        this.esawh = esawh;

        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        FieldCombobox = new javax.swing.JComboBox();
        LevelLabel = new javax.swing.JLabel();
        LevelComboBox = new javax.swing.JComboBox();
        OrderByLabel = new javax.swing.JLabel();
        OrderByComboBox = new javax.swing.JComboBox();
        FieldLabel = new javax.swing.JLabel();
        AddButton = new javax.swing.JButton();
        CancelButton = new javax.swing.JButton();
        FieldValueComboBox = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(esawh.getMessage(Messages.WINEXPORTRESORTERADDER_name));
        setResizable(false);

        FieldCombobox.setModel(esawh.getFieldComboBoxModel());
        FieldCombobox.setSelectedIndex(0);
        FieldCombobox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FieldComboboxActionPerformed(evt);
            }
        });

        LevelLabel.setText(esawh.getMessage(Messages.WINEXPORTRESORTERADDER_LabelLevel));

        LevelComboBox.setModel(esawh.getLevelComboBoxModel());

        OrderByLabel.setText(esawh.getMessage(Messages.WINEXPORTRESORTERADDER_LabelOrderBy));

        OrderByComboBox.setModel(esawh.getOrderByComboBoxModel());

        FieldLabel.setText(esawh.getMessage(Messages.WINEXPORTRESORTERADDER_LabelField));

        AddButton.setText(esawh.getMessage(Messages.WINEXPORTRESORTERADDER_ButtonAdd));
        AddButton.setPreferredSize(new java.awt.Dimension(32, 28));
        AddButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddButtonActionPerformed(evt);
            }
        });

        CancelButton.setText(esawh.getMessage(Messages.WINEXPORTRESORTERADDER_ButtonCancel));
        CancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CancelButtonActionPerformed(evt);
            }
        });

        FieldValueComboBox.setEnabled(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(FieldLabel)
                    .addComponent(FieldCombobox, 0, 303, Short.MAX_VALUE)
                    .addComponent(FieldValueComboBox, 0, 303, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(LevelLabel)
                            .addComponent(LevelComboBox, 0, 97, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(OrderByComboBox, 0, 194, Short.MAX_VALUE)
                            .addComponent(OrderByLabel)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(AddButton, javax.swing.GroupLayout.DEFAULT_SIZE, 143, Short.MAX_VALUE)
                        .addGap(12, 12, 12)
                        .addComponent(CancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(FieldLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(FieldCombobox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(FieldValueComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LevelLabel)
                    .addComponent(OrderByLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(OrderByComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LevelComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(AddButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(CancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void FieldComboboxActionPerformed(java.awt.event.ActionEvent evt) {                                              
		esawh.fieldComboBoxChanged();
	}

    private void AddButtonActionPerformed(java.awt.event.ActionEvent evt) {                                          
		esawh.addButtonPressed();
	}

	private void CancelButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_CancelButtonActionPerformed
		esawh.cancelButtonPressed();
    }                                            

	// ___________________________________________________________
	// ___________________________________________________________

	public JComboBox getFieldComboBox() {

		return FieldCombobox;
	}

	public JComboBox getFieldValueComboBox() {

		return FieldValueComboBox;
	}

	public JComboBox getOrderByComboBox() {

		return OrderByComboBox;
	}

	public JComboBox getLevelComboBox() {

		return LevelComboBox;
	}

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ExportSorterAdderDialog dialog = new ExportSorterAdderDialog(new javax.swing.JFrame(), true,null);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AddButton;
    private javax.swing.JButton CancelButton;
    private javax.swing.JComboBox FieldCombobox;
    private javax.swing.JLabel FieldLabel;
    private javax.swing.JComboBox FieldValueComboBox;
    private javax.swing.JComboBox LevelComboBox;
    private javax.swing.JLabel LevelLabel;
    private javax.swing.JComboBox OrderByComboBox;
    private javax.swing.JLabel OrderByLabel;
    // End of variables declaration//GEN-END:variables

}