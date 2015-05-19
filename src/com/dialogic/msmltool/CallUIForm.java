/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dialogic.msmltool;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.sip.message.Request;
import javax.sip.message.Response;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

/**
 *
 * @author ssatyana
 */
public class CallUIForm extends javax.swing.JFrame {

    App app = new App();

    /**
     * Creates new form CallForm
     */
    private CallUIForm() {
        initComponents();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        hangupButton.setEnabled(false);
        userText.setText("msml");
        addressText.setText(App.getXMSAdr());
        displayInitialMessage();

        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                app.close();
            }
        });
    }

    public static CallUIForm initialize() {
        CallUIForm callForm = new CallUIForm();
        callForm.setVisible(true);
        callForm.setLocationRelativeTo(null);
        return callForm;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        addressText = new javax.swing.JTextField();
        addressLabel = new javax.swing.JLabel();
        callButton = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        callPanel = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        callTextArea = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();
        msmlScriptLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        msmlTextArea = new javax.swing.JTextArea();
        responseLabel = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        responseTextArea = new javax.swing.JTextArea();
        fileButton = new javax.swing.JButton();
        sendMsmlButton = new javax.swing.JButton();
        fileTextField = new javax.swing.JTextField();
        clearButton = new javax.swing.JButton();
        clearResponseButton = new javax.swing.JButton();
        saveMsmlScriptButton = new javax.swing.JButton();
        saveResponseButton = new javax.swing.JButton();
        userText = new javax.swing.JTextField();
        userLabel = new javax.swing.JLabel();
        hangupButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("CallUserInterface");

        addressText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addressTextActionPerformed(evt);
            }
        });

        addressLabel.setText("SIP Address or Phone number");

        callButton.setText("Call");
        callButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                callButtonMouseClicked(evt);
            }
        });
        callButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                callButtonActionPerformed(evt);
            }
        });

        callTextArea.setColumns(20);
        callTextArea.setRows(5);
        jScrollPane3.setViewportView(callTextArea);

        javax.swing.GroupLayout callPanelLayout = new javax.swing.GroupLayout(callPanel);
        callPanel.setLayout(callPanelLayout);
        callPanelLayout.setHorizontalGroup(
            callPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(callPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 474, Short.MAX_VALUE)
                .addContainerGap())
        );
        callPanelLayout.setVerticalGroup(
            callPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(callPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 389, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Call", callPanel);

        msmlScriptLabel.setText("MSML Script");

        msmlTextArea.setColumns(20);
        msmlTextArea.setRows(5);
        jScrollPane1.setViewportView(msmlTextArea);

        responseLabel.setText("Recieved Response");

        responseTextArea.setColumns(20);
        responseTextArea.setRows(5);
        jScrollPane2.setViewportView(responseTextArea);

        fileButton.setText("ChooseFile");
        fileButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                fileButtonMouseClicked(evt);
            }
        });
        fileButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileButtonActionPerformed(evt);
            }
        });

        sendMsmlButton.setText("Send");
        sendMsmlButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                sendMsmlButtonMouseClicked(evt);
            }
        });

        fileTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileTextFieldActionPerformed(evt);
            }
        });

        clearButton.setText("Clear");
        clearButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                clearButtonMouseClicked(evt);
            }
        });
        clearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearButtonActionPerformed(evt);
            }
        });

        clearResponseButton.setText("Clear");
        clearResponseButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                clearResponseButtonMouseClicked(evt);
            }
        });
        clearResponseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearResponseButtonActionPerformed(evt);
            }
        });

        saveMsmlScriptButton.setText("Save");
        saveMsmlScriptButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                saveMsmlScriptButtonMouseClicked(evt);
            }
        });

        saveResponseButton.setText("Save");
        saveResponseButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                saveResponseButtonMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(fileTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 305, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(fileButton, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sendMsmlButton)
                .addGap(15, 15, 15))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(msmlScriptLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(responseLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(clearResponseButton)
                        .addGap(18, 18, 18)
                        .addComponent(saveResponseButton))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(clearButton)
                        .addGap(18, 18, 18)
                        .addComponent(saveMsmlScriptButton)))
                .addContainerGap(346, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(msmlScriptLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(clearButton)
                    .addComponent(saveMsmlScriptButton))
                .addGap(2, 2, 2)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fileTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fileButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(sendMsmlButton))
                .addGap(7, 7, 7)
                .addComponent(responseLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(clearResponseButton, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(saveResponseButton))
                .addContainerGap())
        );

        jTabbedPane1.addTab("MSML", jPanel2);

        userText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userTextActionPerformed(evt);
            }
        });

        userLabel.setText("User");

        hangupButton.setText("Hangup");
        hangupButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                hangupButtonMouseClicked(evt);
            }
        });
        hangupButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hangupButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTabbedPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(userText)
                            .addComponent(userLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(addressText)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(callButton, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(hangupButton, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(addressLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(42, 42, 42)))))
                .addGap(18, 18, 18))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(userLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(addressLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(3, 3, 3)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(userText, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(hangupButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(callButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(addressText, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    //capturing the address for the call
    private void addressTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addressTextActionPerformed

    }//GEN-LAST:event_addressTextActionPerformed

    private void callButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_callButtonActionPerformed

    }//GEN-LAST:event_callButtonActionPerformed

    private void userTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userTextActionPerformed
        System.out.println("Entered text" + this.userText.getText());
    }//GEN-LAST:event_userTextActionPerformed

    public String getUser() {
        return this.userText.getText();
    }

    private void hangupButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hangupButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_hangupButtonActionPerformed

    private void callButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_callButtonMouseClicked

        app.makeCall(this.userText.getText(), this.addressText.getText(), null);
        callButton.setEnabled(false);
        hangupButton.setEnabled(true);
        userText.setEnabled(false);
        addressText.setEnabled(false);
    }//GEN-LAST:event_callButtonMouseClicked

    private void fileTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fileTextFieldActionPerformed

    private void fileButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileButtonActionPerformed

    }//GEN-LAST:event_fileButtonActionPerformed

    private void fileButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fileButtonMouseClicked
        try {
            JFileChooser chooser = new JFileChooser("");
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

            // to populate the text field
            chooser.addPropertyChangeListener(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY, new PropertyChangeListener() {
                public void propertyChange(PropertyChangeEvent evt) {
                    if (JFileChooser.SELECTED_FILE_CHANGED_PROPERTY.equals(evt.getPropertyName())) {
                        JFileChooser chooser = (JFileChooser) evt.getSource();
                        if (chooser.getSelectedFile() != null) {
                            fileTextField.setText(chooser.getSelectedFile().getName());
                        }
                    }
                }
            });
            int returnVal = chooser.showOpenDialog((java.awt.Component) null);
            File inFile = null;
            if (returnVal == chooser.APPROVE_OPTION) {
                inFile = chooser.getSelectedFile();
                System.out.println("Selected File: " + inFile.getAbsolutePath());

                // to display the file contents to the msml script text area
                JTextArea text = this.msmlTextArea;
                DefaultCaret caret = (DefaultCaret) msmlTextArea.getCaret();
                caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
                BufferedReader in = new BufferedReader(new FileReader(inFile));

                String line = in.readLine();
                while (line != null) {
                    text.append(line + "\n");
                    line = in.readLine();
                }
            } else if (returnVal == chooser.CANCEL_OPTION) {
                System.out.println("Open command cancelled by the user");
            }
        } catch (IOException ex) {
            Logger.getLogger(CallUIForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_fileButtonMouseClicked

    private void hangupButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_hangupButtonMouseClicked
        app.hangup();
        callButton.setEnabled(true);
        hangupButton.setEnabled(false);
        userText.setEnabled(true);
        addressText.setEnabled(true);
    }//GEN-LAST:event_hangupButtonMouseClicked

    private void sendMsmlButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sendMsmlButtonMouseClicked
        DefaultCaret caret = (DefaultCaret) msmlTextArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        app.sendMsml(this.msmlTextArea.getText());

        while (Connector.responseMessage != null && Connector.responseMessage.length() > 0) {
            this.responseTextArea.setText(Connector.responseMessage);
        }

    }//GEN-LAST:event_sendMsmlButtonMouseClicked

    private void clearButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_clearButtonMouseClicked
        this.msmlTextArea.setText("");
    }//GEN-LAST:event_clearButtonMouseClicked

    private void clearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_clearButtonActionPerformed

    private void clearResponseButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_clearResponseButtonMouseClicked
        this.responseTextArea.setText("");
    }//GEN-LAST:event_clearResponseButtonMouseClicked

    private void clearResponseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearResponseButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_clearResponseButtonActionPerformed

    private void saveMsmlScriptButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_saveMsmlScriptButtonMouseClicked
        final JFileChooser SaveAs = new JFileChooser();
        SaveAs.setApproveButtonText("Save");
        int actionDialog = SaveAs.showOpenDialog(this);
        if (actionDialog != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File fileName = new File(SaveAs.getSelectedFile() + ".txt");
        BufferedWriter outFile = null;
        try {
            outFile = new BufferedWriter(new FileWriter(fileName));
            this.msmlTextArea.write(outFile);
        } catch (IOException ex) {
            System.out.println(ex);
        } finally {
            if (outFile != null) {
                try {
                    outFile.close();
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
        }
    }//GEN-LAST:event_saveMsmlScriptButtonMouseClicked

    private void saveResponseButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_saveResponseButtonMouseClicked
        final JFileChooser SaveAs = new JFileChooser();
        SaveAs.setApproveButtonText("Save");
        int actionDialog = SaveAs.showOpenDialog(this);
        if (actionDialog != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File fileName = new File(SaveAs.getSelectedFile() + ".txt");
        BufferedWriter outFile = null;
        try {
            outFile = new BufferedWriter(new FileWriter(fileName));
            this.responseTextArea.write(outFile);
        } catch (IOException ex) {
            System.out.println(ex);
        } finally {
            if (outFile != null) {
                try {
                    outFile.close();
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
        }
    }//GEN-LAST:event_saveResponseButtonMouseClicked

    public void updateRecievedMessage(String message) {
        responseTextArea.setText(responseTextArea.getText() + "\n" + timeStamp() + "\n" + message);
        DefaultCaret caret = (DefaultCaret) responseTextArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
    }

    public void updateCallTextArea(int response) {
        switch (response) {
            case Response.TRYING:
                callTextArea.setText(callTextArea.getText() + "\n" + timeStamp() + "\n" + response + " TRYING");
                break;
            case Response.RINGING:
                callTextArea.setText(callTextArea.getText() + "\n" + timeStamp() + "\n" + response + " RINGING");
                break;
            case Response.OK:
                callTextArea.setText(callTextArea.getText() + "\n" + timeStamp() + "\n" + response + " OK");
                break;
        }
    }

    public void updateCallTextArea() {
        System.out.println("set the text");
        callTextArea.setText(callTextArea.getText() + "\n" + timeStamp() + "\n" + Request.BYE);
        callButton.setEnabled(true);
        hangupButton.setEnabled(false);
        userText.setEnabled(true);
        addressText.setEnabled(true);
    }

    public boolean disableButtons() {
        callButton.setEnabled(false);
        hangupButton.setEnabled(false);
        return Boolean.FALSE;
    }

    private String timeStamp() {
        return new SimpleDateFormat("[HH:mm:ss.SSS] ").format(Calendar.getInstance().getTime());
    }

    private void displayInitialMessage() {
        try {
            List<String> lines = ReadFileUtility.readFile();
            String port = null;
            for (int i = 1; i < lines.size(); i += 2) {
                port = lines.get(i);
            }
            callTextArea.setText("Waiting for call at " + Inet4Address.getLocalHost().getHostAddress() + ":" + port + "...");
        } catch (UnknownHostException ex) {
            Logger.getLogger(CallUIForm.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public String getUserTextFieldValue() {
        return this.userText.getText();
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
            java.util.logging.Logger.getLogger(CallUIForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CallUIForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CallUIForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CallUIForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CallUIForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel addressLabel;
    private javax.swing.JTextField addressText;
    private javax.swing.JButton callButton;
    private javax.swing.JPanel callPanel;
    private javax.swing.JTextArea callTextArea;
    private javax.swing.JButton clearButton;
    private javax.swing.JButton clearResponseButton;
    private javax.swing.JButton fileButton;
    private javax.swing.JTextField fileTextField;
    private javax.swing.JButton hangupButton;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel msmlScriptLabel;
    private javax.swing.JTextArea msmlTextArea;
    private javax.swing.JLabel responseLabel;
    private javax.swing.JTextArea responseTextArea;
    private javax.swing.JButton saveMsmlScriptButton;
    private javax.swing.JButton saveResponseButton;
    private javax.swing.JButton sendMsmlButton;
    private javax.swing.JLabel userLabel;
    private javax.swing.JTextField userText;
    // End of variables declaration//GEN-END:variables
}
