/*    */ package net.java.balloontip.styles;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Component;
/*    */ import java.awt.Graphics;
/*    */ import java.awt.Graphics2D;
/*    */ import java.awt.Insets;
/*    */ import java.awt.geom.GeneralPath;
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
/*    */ public class EdgedBalloonStyle
/*    */   extends BalloonTipStyle
/*    */ {
/*    */   private final Color borderColor;
/*    */   private final Color fillColor;
/*    */   
/*    */   public EdgedBalloonStyle(Color fillColor, Color borderColor) {
/* 36 */     this.borderColor = borderColor;
/* 37 */     this.fillColor = fillColor;
/*    */   }
/*    */   
/*    */   public Insets getBorderInsets(Component c) {
/* 41 */     if (this.flipY) {
/* 42 */       return new Insets(this.verticalOffset + 1, 1, 1, 1);
/*    */     }
/* 44 */     return new Insets(1, 1, this.verticalOffset + 1, 1);
/*    */   }
/*    */   
/*    */   public boolean isBorderOpaque() {
/* 48 */     return true;
/*    */   }
/*    */   public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
/*    */     int yTop, yBottom;
/* 52 */     Graphics2D g2d = (Graphics2D)g;
/* 53 */     width--;
/* 54 */     height--;
/*    */ 
/*    */ 
/*    */     
/* 58 */     if (this.flipY) {
/* 59 */       yTop = y + this.verticalOffset;
/* 60 */       yBottom = y + height;
/*    */     } else {
/* 62 */       yTop = y;
/* 63 */       yBottom = y + height - this.verticalOffset;
/*    */     } 
/*    */ 
/*    */     
/* 67 */     GeneralPath outline = new GeneralPath();
/* 68 */     outline.moveTo(x, yTop);
/* 69 */     outline.lineTo(x, yBottom);
/*    */     
/* 71 */     if (!this.flipX && !this.flipY) {
/* 72 */       outline.lineTo((x + this.horizontalOffset), yBottom);
/* 73 */       outline.lineTo((x + this.horizontalOffset), (yBottom + this.verticalOffset));
/* 74 */       outline.lineTo((x + this.horizontalOffset + this.verticalOffset), yBottom);
/* 75 */     } else if (this.flipX && !this.flipY) {
/* 76 */       outline.lineTo((x + width - this.horizontalOffset - this.verticalOffset), yBottom);
/* 77 */       outline.lineTo((x + width - this.horizontalOffset), (yBottom + this.verticalOffset));
/* 78 */       outline.lineTo((x + width - this.horizontalOffset), yBottom);
/*    */     } 
/*    */     
/* 81 */     outline.lineTo((x + width), yBottom);
/* 82 */     outline.lineTo((x + width), yTop);
/*    */     
/* 84 */     if (!this.flipX && this.flipY) {
/* 85 */       outline.lineTo((x + this.horizontalOffset + this.verticalOffset), yTop);
/* 86 */       outline.lineTo((x + this.horizontalOffset), (yTop - this.verticalOffset));
/* 87 */       outline.lineTo((x + this.horizontalOffset), yTop);
/* 88 */     } else if (this.flipX && this.flipY) {
/* 89 */       outline.lineTo((x + width - this.horizontalOffset), yTop);
/* 90 */       outline.lineTo((x + width - this.horizontalOffset), (yTop - this.verticalOffset));
/* 91 */       outline.lineTo((x + width - this.horizontalOffset - this.verticalOffset), yTop);
/*    */     } 
/*    */     
/* 94 */     outline.closePath();
/*    */     
/* 96 */     g2d.setPaint(this.fillColor);
/* 97 */     g2d.fill(outline);
/* 98 */     g2d.setPaint(this.borderColor);
/* 99 */     g2d.draw(outline);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/net/java/balloontip/styles/EdgedBalloonStyle.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */