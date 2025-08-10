/*     */ package org.apache.log4j.chainsaw;
/*     */ 
/*     */ import java.awt.GridBagConstraints;
/*     */ import java.awt.GridBagLayout;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JTextField;
/*     */ import javax.swing.event.DocumentEvent;
/*     */ import javax.swing.event.DocumentListener;
/*     */ import org.apache.log4j.Level;
/*     */ import org.apache.log4j.Logger;
/*     */ import org.apache.log4j.Priority;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class ControlPanel
/*     */   extends JPanel
/*     */ {
/*  42 */   private static final Logger LOG = Logger.getLogger(ControlPanel.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ControlPanel(final MyTableModel aModel) {
/*  50 */     setBorder(BorderFactory.createTitledBorder("Controls: "));
/*  51 */     GridBagLayout gridbag = new GridBagLayout();
/*  52 */     GridBagConstraints c = new GridBagConstraints();
/*  53 */     setLayout(gridbag);
/*     */ 
/*     */     
/*  56 */     c.ipadx = 5;
/*  57 */     c.ipady = 5;
/*     */ 
/*     */     
/*  60 */     c.gridx = 0;
/*  61 */     c.anchor = 13;
/*     */     
/*  63 */     c.gridy = 0;
/*  64 */     JLabel label = new JLabel("Filter Level:");
/*  65 */     gridbag.setConstraints(label, c);
/*  66 */     add(label);
/*     */     
/*  68 */     c.gridy++;
/*  69 */     label = new JLabel("Filter Thread:");
/*  70 */     gridbag.setConstraints(label, c);
/*  71 */     add(label);
/*     */     
/*  73 */     c.gridy++;
/*  74 */     label = new JLabel("Filter Logger:");
/*  75 */     gridbag.setConstraints(label, c);
/*  76 */     add(label);
/*     */     
/*  78 */     c.gridy++;
/*  79 */     label = new JLabel("Filter NDC:");
/*  80 */     gridbag.setConstraints(label, c);
/*  81 */     add(label);
/*     */     
/*  83 */     c.gridy++;
/*  84 */     label = new JLabel("Filter Message:");
/*  85 */     gridbag.setConstraints(label, c);
/*  86 */     add(label);
/*     */ 
/*     */     
/*  89 */     c.weightx = 1.0D;
/*     */     
/*  91 */     c.gridx = 1;
/*  92 */     c.anchor = 17;
/*     */     
/*  94 */     c.gridy = 0;
/*  95 */     Level[] allPriorities = { Level.FATAL, Level.ERROR, Level.WARN, Level.INFO, Level.DEBUG, Level.TRACE };
/*     */ 
/*     */     
/*  98 */     final JComboBox<Level> priorities = new JComboBox<Level>(allPriorities);
/*  99 */     Level lowest = allPriorities[allPriorities.length - 1];
/* 100 */     priorities.setSelectedItem(lowest);
/* 101 */     aModel.setPriorityFilter((Priority)lowest);
/* 102 */     gridbag.setConstraints(priorities, c);
/* 103 */     add(priorities);
/* 104 */     priorities.setEditable(false);
/* 105 */     priorities.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent aEvent) {
/* 107 */             aModel.setPriorityFilter((Priority)priorities.getSelectedItem());
/*     */           }
/*     */         });
/*     */     
/* 111 */     c.fill = 2;
/* 112 */     c.gridy++;
/* 113 */     final JTextField threadField = new JTextField("");
/* 114 */     threadField.getDocument().addDocumentListener(new DocumentListener() {
/*     */           public void insertUpdate(DocumentEvent aEvent) {
/* 116 */             aModel.setThreadFilter(threadField.getText());
/*     */           }
/*     */           
/*     */           public void removeUpdate(DocumentEvent aEvente) {
/* 120 */             aModel.setThreadFilter(threadField.getText());
/*     */           }
/*     */           
/*     */           public void changedUpdate(DocumentEvent aEvent) {
/* 124 */             aModel.setThreadFilter(threadField.getText());
/*     */           }
/*     */         });
/* 127 */     gridbag.setConstraints(threadField, c);
/* 128 */     add(threadField);
/*     */     
/* 130 */     c.gridy++;
/* 131 */     final JTextField catField = new JTextField("");
/* 132 */     catField.getDocument().addDocumentListener(new DocumentListener() {
/*     */           public void insertUpdate(DocumentEvent aEvent) {
/* 134 */             aModel.setCategoryFilter(catField.getText());
/*     */           }
/*     */           
/*     */           public void removeUpdate(DocumentEvent aEvent) {
/* 138 */             aModel.setCategoryFilter(catField.getText());
/*     */           }
/*     */           
/*     */           public void changedUpdate(DocumentEvent aEvent) {
/* 142 */             aModel.setCategoryFilter(catField.getText());
/*     */           }
/*     */         });
/* 145 */     gridbag.setConstraints(catField, c);
/* 146 */     add(catField);
/*     */     
/* 148 */     c.gridy++;
/* 149 */     final JTextField ndcField = new JTextField("");
/* 150 */     ndcField.getDocument().addDocumentListener(new DocumentListener() {
/*     */           public void insertUpdate(DocumentEvent aEvent) {
/* 152 */             aModel.setNDCFilter(ndcField.getText());
/*     */           }
/*     */           
/*     */           public void removeUpdate(DocumentEvent aEvent) {
/* 156 */             aModel.setNDCFilter(ndcField.getText());
/*     */           }
/*     */           
/*     */           public void changedUpdate(DocumentEvent aEvent) {
/* 160 */             aModel.setNDCFilter(ndcField.getText());
/*     */           }
/*     */         });
/* 163 */     gridbag.setConstraints(ndcField, c);
/* 164 */     add(ndcField);
/*     */     
/* 166 */     c.gridy++;
/* 167 */     final JTextField msgField = new JTextField("");
/* 168 */     msgField.getDocument().addDocumentListener(new DocumentListener() {
/*     */           public void insertUpdate(DocumentEvent aEvent) {
/* 170 */             aModel.setMessageFilter(msgField.getText());
/*     */           }
/*     */           
/*     */           public void removeUpdate(DocumentEvent aEvent) {
/* 174 */             aModel.setMessageFilter(msgField.getText());
/*     */           }
/*     */           
/*     */           public void changedUpdate(DocumentEvent aEvent) {
/* 178 */             aModel.setMessageFilter(msgField.getText());
/*     */           }
/*     */         });
/*     */     
/* 182 */     gridbag.setConstraints(msgField, c);
/* 183 */     add(msgField);
/*     */ 
/*     */     
/* 186 */     c.weightx = 0.0D;
/* 187 */     c.fill = 2;
/* 188 */     c.anchor = 13;
/* 189 */     c.gridx = 2;
/*     */     
/* 191 */     c.gridy = 0;
/* 192 */     JButton exitButton = new JButton("Exit");
/* 193 */     exitButton.setMnemonic('x');
/* 194 */     exitButton.addActionListener(ExitAction.INSTANCE);
/* 195 */     gridbag.setConstraints(exitButton, c);
/* 196 */     add(exitButton);
/*     */     
/* 198 */     c.gridy++;
/* 199 */     JButton clearButton = new JButton("Clear");
/* 200 */     clearButton.setMnemonic('c');
/* 201 */     clearButton.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent aEvent) {
/* 203 */             aModel.clear();
/*     */           }
/*     */         });
/* 206 */     gridbag.setConstraints(clearButton, c);
/* 207 */     add(clearButton);
/*     */     
/* 209 */     c.gridy++;
/* 210 */     final JButton toggleButton = new JButton("Pause");
/* 211 */     toggleButton.setMnemonic('p');
/* 212 */     toggleButton.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent aEvent) {
/* 214 */             aModel.toggle();
/* 215 */             toggleButton.setText(aModel.isPaused() ? "Resume" : "Pause");
/*     */           }
/*     */         });
/* 218 */     gridbag.setConstraints(toggleButton, c);
/* 219 */     add(toggleButton);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/chainsaw/ControlPanel.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */