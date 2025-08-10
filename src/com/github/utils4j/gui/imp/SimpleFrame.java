/*    */ package com.github.utils4j.gui.imp;
/*    */ 
/*    */ import java.awt.Dimension;
/*    */ import java.awt.GraphicsConfiguration;
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
/*    */ public class SimpleFrame
/*    */   extends JEscFrame
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public SimpleFrame(String title) {
/* 39 */     super(title);
/*    */   }
/*    */   
/*    */   public SimpleFrame(String title, Image icon) {
/* 43 */     super(title);
/* 44 */     setIconImage(icon);
/*    */   }
/*    */   
/*    */   public SimpleFrame(String title, Image icon, GraphicsConfiguration gc) {
/* 48 */     super(title, gc);
/* 49 */     setIconImage(icon);
/*    */   }
/*    */   
/*    */   public void showToFront() {
/* 53 */     SwingTools.showToFront(this);
/*    */   }
/*    */   
/*    */   public final void toCenter() {
/* 57 */     setLocationRelativeTo(null);
/*    */   }
/*    */   
/*    */   public void close() {
/* 61 */     setVisible(false);
/* 62 */     dispose();
/*    */   }
/*    */   
/*    */   protected void setFixedMinimumSize(Dimension dimension) {
/* 66 */     SwingTools.setFixedMinimumSize(this, dimension);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/gui/imp/SimpleFrame.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */