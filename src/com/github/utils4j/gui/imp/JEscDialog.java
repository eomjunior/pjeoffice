/*     */ package com.github.utils4j.gui.imp;
/*     */ 
/*     */ import java.awt.Dialog;
/*     */ import java.awt.Frame;
/*     */ import java.awt.GraphicsConfiguration;
/*     */ import java.awt.Window;
/*     */ import java.awt.event.ActionEvent;
/*     */ import javax.swing.AbstractAction;
/*     */ import javax.swing.JDialog;
/*     */ import javax.swing.JRootPane;
/*     */ import javax.swing.KeyStroke;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JEscDialog
/*     */   extends JDialog
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   
/*     */   public JEscDialog() {
/*  46 */     this((Frame)null, false);
/*     */   }
/*     */   
/*     */   public JEscDialog(Frame owner) {
/*  50 */     this(owner, false);
/*     */   }
/*     */   
/*     */   public JEscDialog(Frame owner, boolean modal) {
/*  54 */     this(owner, (String)null, modal);
/*     */   }
/*     */   
/*     */   public JEscDialog(Frame owner, String title) {
/*  58 */     this(owner, title, false);
/*     */   }
/*     */   
/*     */   public JEscDialog(Frame owner, String title, boolean modal) {
/*  62 */     super(owner, title, modal);
/*     */   }
/*     */   
/*     */   public JEscDialog(Frame owner, String title, boolean modal, GraphicsConfiguration gc) {
/*  66 */     super(owner, title, modal, gc);
/*     */   }
/*     */   
/*     */   public JEscDialog(Dialog owner) {
/*  70 */     this(owner, false);
/*     */   }
/*     */   
/*     */   public JEscDialog(Dialog owner, boolean modal) {
/*  74 */     this(owner, (String)null, modal);
/*     */   }
/*     */   
/*     */   public JEscDialog(Dialog owner, String title) {
/*  78 */     this(owner, title, false);
/*     */   }
/*     */   
/*     */   public JEscDialog(Dialog owner, String title, boolean modal) {
/*  82 */     super(owner, title, modal);
/*     */   }
/*     */   
/*     */   public JEscDialog(Dialog owner, String title, boolean modal, GraphicsConfiguration gc) {
/*  86 */     super(owner, title, modal, gc);
/*     */   }
/*     */   
/*     */   public JEscDialog(Window owner, Dialog.ModalityType modalityType) {
/*  90 */     this(owner, "", modalityType);
/*     */   }
/*     */   
/*     */   public JEscDialog(Window owner, String title, Dialog.ModalityType modalityType) {
/*  94 */     super(owner, title, modalityType);
/*     */   }
/*     */   
/*     */   public JEscDialog(Window owner, String title, Dialog.ModalityType modalityType, GraphicsConfiguration gc) {
/*  98 */     super(owner, title, modalityType, gc);
/*     */   }
/*     */ 
/*     */   
/*     */   protected JRootPane createRootPane() {
/* 103 */     JRootPane rootPane = new JRootPane();
/* 104 */     KeyStroke stroke = KeyStroke.getKeyStroke(27, 0);
/* 105 */     rootPane.getInputMap(2).put(stroke, "escapeKey");
/* 106 */     rootPane.getActionMap().put("escapeKey", new AbstractAction() {
/*     */           private static final long serialVersionUID = 1L;
/*     */           
/*     */           public void actionPerformed(ActionEvent e) {
/* 110 */             JEscDialog.this.onEscPressed(e);
/*     */           }
/*     */         });
/* 113 */     return rootPane;
/*     */   }
/*     */   
/*     */   protected void onEscPressed(ActionEvent e) {
/* 117 */     setVisible(false);
/* 118 */     dispose();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/gui/imp/JEscDialog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */