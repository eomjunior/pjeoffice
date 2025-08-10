/*    */ package com.github.utils4j.gui.imp;
/*    */ 
/*    */ import java.awt.Dimension;
/*    */ import java.awt.Frame;
/*    */ import java.awt.Image;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SimpleDialog
/*    */   extends JEscDialog
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public SimpleDialog(String title) {
/* 39 */     super((Frame)null, title);
/*    */   }
/*    */   
/*    */   public SimpleDialog(String title, boolean modal) {
/* 43 */     super((Frame)null, title, modal);
/*    */   }
/*    */   
/*    */   public SimpleDialog(String title, Image icon) {
/* 47 */     this((Frame)null, title, icon);
/*    */   }
/*    */   
/*    */   public SimpleDialog(String title, Image icon, boolean modal) {
/* 51 */     this((Frame)null, title, icon, modal);
/*    */   }
/*    */   
/*    */   public SimpleDialog(Frame owner, String title, Image icon) {
/* 55 */     this(owner, title, icon, false);
/*    */   }
/*    */   
/*    */   public SimpleDialog(Frame owner, String title, Image icon, boolean modal) {
/* 59 */     super(owner, title, modal);
/* 60 */     setIconImage(icon);
/*    */   }
/*    */   
/*    */   protected void setFixedMinimumSize(Dimension dimension) {
/* 64 */     SwingTools.setFixedMinimumSize(this, dimension);
/*    */   }
/*    */   
/*    */   protected final void toCenter() {
/* 68 */     setLocationRelativeTo(null);
/*    */   }
/*    */   
/*    */   public void showToFront() {
/* 72 */     SwingTools.showToFront(this);
/*    */   }
/*    */   
/*    */   public void close() {
/* 76 */     setVisible(false);
/* 77 */     dispose();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/gui/imp/SimpleDialog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */