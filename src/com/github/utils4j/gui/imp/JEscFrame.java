/*    */ package com.github.utils4j.gui.imp;
/*    */ 
/*    */ import java.awt.GraphicsConfiguration;
/*    */ import java.awt.event.ActionEvent;
/*    */ import javax.swing.AbstractAction;
/*    */ import javax.swing.JFrame;
/*    */ import javax.swing.JRootPane;
/*    */ import javax.swing.KeyStroke;
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
/*    */ 
/*    */ public class JEscFrame
/*    */   extends JFrame
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public JEscFrame() {}
/*    */   
/*    */   public JEscFrame(GraphicsConfiguration gc) {
/* 45 */     super(gc);
/*    */   }
/*    */   
/*    */   public JEscFrame(String title) {
/* 49 */     super(title);
/*    */   }
/*    */   
/*    */   public JEscFrame(String title, GraphicsConfiguration gc) {
/* 53 */     super(title, gc);
/*    */   }
/*    */   
/*    */   protected JRootPane createRootPane() {
/* 57 */     JRootPane rootPane = new JRootPane();
/* 58 */     KeyStroke stroke = KeyStroke.getKeyStroke(27, 0);
/* 59 */     rootPane.getInputMap(2).put(stroke, "escapeKey");
/* 60 */     rootPane.getActionMap().put("escapeKey", new AbstractAction()
/*    */         {
/*    */           public void actionPerformed(ActionEvent e) {
/* 63 */             JEscFrame.this.onEscPressed(e);
/*    */           } private static final long serialVersionUID = 1L;
/*    */         });
/* 66 */     return rootPane;
/*    */   }
/*    */   
/*    */   protected void onEscPressed(ActionEvent e) {
/* 70 */     close();
/*    */   }
/*    */   
/*    */   protected void close() {
/* 74 */     setVisible(false);
/* 75 */     dispose();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/gui/imp/JEscFrame.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */