/*    */ package br.jus.cnj.pje.office.gui;
/*    */ 
/*    */ import com.github.utils4j.gui.imp.RoundedBorder;
/*    */ import com.github.utils4j.gui.imp.SimpleFrame;
/*    */ import com.github.utils4j.imp.Threads;
/*    */ import java.awt.Color;
/*    */ import java.awt.event.ComponentAdapter;
/*    */ import java.awt.event.ComponentEvent;
/*    */ import java.awt.geom.RoundRectangle2D;
/*    */ import javax.swing.JLabel;
/*    */ import javax.swing.border.Border;
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
/*    */ 
/*    */ 
/*    */ public class PjeSplash
/*    */   extends SimpleFrame
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public PjeSplash() {
/* 48 */     super("");
/* 49 */     setWindowState();
/* 50 */     setupLayout();
/*    */   }
/*    */   
/*    */   public void display() {
/* 54 */     setVisible(true);
/* 55 */     toCenter();
/* 56 */     showToFront();
/* 57 */     Threads.startDaemon(this::close, 4000L);
/*    */   }
/*    */   
/*    */   private void setWindowState() {
/* 61 */     setUndecorated(true);
/* 62 */     getRootPane().setWindowDecorationStyle(0);
/* 63 */     setDefaultCloseOperation(2);
/*    */   }
/*    */   
/*    */   private void setupLayout() {
/* 67 */     JLabel label = new JLabel(PjeImages.PJE_ICON_SPLASH.asIcon());
/* 68 */     label.setBorder((Border)new RoundedBorder(Color.BLACK, 20));
/* 69 */     addComponentListener(new ComponentAdapter()
/*    */         {
/*    */           public void componentResized(ComponentEvent e) {
/* 72 */             PjeSplash.this.setShape(new RoundRectangle2D.Double(0.0D, 0.0D, PjeSplash.this.getWidth(), PjeSplash.this.getHeight(), 20.0D, 20.0D));
/*    */           }
/*    */         });
/* 75 */     add(label);
/* 76 */     pack();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/gui/PjeSplash.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */