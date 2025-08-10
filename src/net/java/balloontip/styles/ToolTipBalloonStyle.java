/*    */ package net.java.balloontip.styles;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Component;
/*    */ import java.awt.Graphics;
/*    */ import java.awt.Graphics2D;
/*    */ import java.awt.Insets;
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
/*    */ public class ToolTipBalloonStyle
/*    */   extends BalloonTipStyle
/*    */ {
/*    */   private final Color borderColor;
/*    */   private final Color fillColor;
/*    */   
/*    */   public ToolTipBalloonStyle(Color fillColor, Color borderColor) {
/* 34 */     this.borderColor = borderColor;
/* 35 */     this.fillColor = fillColor;
/*    */   }
/*    */   
/*    */   public Insets getBorderInsets(Component c) {
/* 39 */     if (this.flipY) {
/* 40 */       return new Insets(this.verticalOffset + 1, 1, 1, 1);
/*    */     }
/* 42 */     return new Insets(1, 1, this.verticalOffset + 1, 1);
/*    */   }
/*    */   
/*    */   public boolean isBorderOpaque() {
/* 46 */     return true;
/*    */   }
/*    */   public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
/*    */     int yTop, yBottom;
/* 50 */     Graphics2D g2d = (Graphics2D)g;
/* 51 */     width--;
/* 52 */     height--;
/*    */ 
/*    */ 
/*    */     
/* 56 */     if (this.flipY) {
/* 57 */       yTop = y + this.verticalOffset;
/* 58 */       yBottom = y + height;
/*    */     } else {
/* 60 */       yTop = y;
/* 61 */       yBottom = y + height - this.verticalOffset;
/*    */     } 
/*    */ 
/*    */     
/* 65 */     g2d.setPaint(this.fillColor);
/* 66 */     g2d.fillRect(x, yTop, width, yBottom);
/* 67 */     g2d.setPaint(this.borderColor);
/* 68 */     g2d.drawRect(x, yTop, width, yBottom);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/net/java/balloontip/styles/ToolTipBalloonStyle.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */