/*    */ package com.github.utils4j.gui.imp;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Component;
/*    */ import java.awt.Graphics;
/*    */ import java.awt.Graphics2D;
/*    */ import java.awt.Insets;
/*    */ import java.awt.geom.RoundRectangle2D;
/*    */ import javax.swing.border.AbstractBorder;
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
/*    */ public class RoundedBorder
/*    */   extends AbstractBorder
/*    */ {
/*    */   private final Color color;
/*    */   private final int gap;
/*    */   
/*    */   public RoundedBorder(Color c, int g) {
/* 44 */     this.color = c;
/* 45 */     this.gap = g;
/*    */   }
/*    */ 
/*    */   
/*    */   public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
/* 50 */     Graphics2D g2d = (Graphics2D)g.create();
/* 51 */     g2d.setColor(this.color);
/* 52 */     g2d.draw(new RoundRectangle2D.Double(x, y, (width - 1), (height - 1), this.gap, this.gap));
/* 53 */     g2d.dispose();
/*    */   }
/*    */ 
/*    */   
/*    */   public Insets getBorderInsets(Component c) {
/* 58 */     return getBorderInsets(c, new Insets(this.gap, this.gap, this.gap, this.gap));
/*    */   }
/*    */ 
/*    */   
/*    */   public Insets getBorderInsets(Component c, Insets insets) {
/* 63 */     insets.left = insets.top = insets.right = insets.bottom = this.gap / 2;
/* 64 */     return insets;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isBorderOpaque() {
/* 69 */     return false;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/gui/imp/RoundedBorder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */