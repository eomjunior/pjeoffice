/*     */ package net.java.balloontip.styles;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Insets;
/*     */ import java.awt.geom.GeneralPath;
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
/*     */ public class RoundedBalloonStyle
/*     */   extends BalloonTipStyle
/*     */ {
/*     */   private final int arcWidth;
/*     */   private final int arcHeight;
/*     */   private final Color fillColor;
/*     */   private final Color borderColor;
/*     */   
/*     */   public RoundedBalloonStyle(int arcWidth, int arcHeight, Color fillColor, Color borderColor) {
/*  41 */     this.arcWidth = arcWidth;
/*  42 */     this.arcHeight = arcHeight;
/*  43 */     this.fillColor = fillColor;
/*  44 */     this.borderColor = borderColor;
/*     */   }
/*     */   
/*     */   public Insets getBorderInsets(Component c) {
/*  48 */     if (this.flipY) {
/*  49 */       return new Insets(this.verticalOffset + this.arcHeight, this.arcWidth, this.arcHeight, this.arcWidth);
/*     */     }
/*  51 */     return new Insets(this.arcHeight, this.arcWidth, this.arcHeight + this.verticalOffset, this.arcWidth);
/*     */   }
/*     */   
/*     */   public boolean isBorderOpaque() {
/*  55 */     return true;
/*     */   }
/*     */   public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
/*     */     int yTop, yBottom;
/*  59 */     Graphics2D g2d = (Graphics2D)g;
/*  60 */     width--;
/*  61 */     height--;
/*     */ 
/*     */ 
/*     */     
/*  65 */     if (this.flipY) {
/*  66 */       yTop = y + this.verticalOffset;
/*  67 */       yBottom = y + height;
/*     */     } else {
/*  69 */       yTop = y;
/*  70 */       yBottom = y + height - this.verticalOffset;
/*     */     } 
/*     */ 
/*     */     
/*  74 */     GeneralPath outline = new GeneralPath();
/*  75 */     outline.moveTo((x + this.arcWidth), yTop);
/*     */     
/*  77 */     outline.quadTo(x, yTop, x, (yTop + this.arcHeight));
/*  78 */     outline.lineTo(x, (yBottom - this.arcHeight));
/*  79 */     outline.quadTo(x, yBottom, (x + this.arcWidth), yBottom);
/*     */     
/*  81 */     if (!this.flipX && !this.flipY) {
/*  82 */       outline.lineTo((x + this.horizontalOffset), yBottom);
/*  83 */       outline.lineTo((x + this.horizontalOffset), (yBottom + this.verticalOffset));
/*  84 */       outline.lineTo((x + this.horizontalOffset + this.verticalOffset), yBottom);
/*  85 */     } else if (this.flipX && !this.flipY) {
/*  86 */       outline.lineTo((x + width - this.horizontalOffset - this.verticalOffset), yBottom);
/*  87 */       outline.lineTo((x + width - this.horizontalOffset), (yBottom + this.verticalOffset));
/*  88 */       outline.lineTo((x + width - this.horizontalOffset), yBottom);
/*     */     } 
/*     */     
/*  91 */     outline.lineTo((x + width - this.arcWidth), yBottom);
/*  92 */     outline.quadTo((x + width), yBottom, (x + width), (yBottom - this.arcHeight));
/*  93 */     outline.lineTo((x + width), (yTop + this.arcHeight));
/*  94 */     outline.quadTo((x + width), yTop, (x + width - this.arcWidth), yTop);
/*     */     
/*  96 */     if (!this.flipX && this.flipY) {
/*  97 */       outline.lineTo((x + this.horizontalOffset + this.verticalOffset), yTop);
/*  98 */       outline.lineTo((x + this.horizontalOffset), (yTop - this.verticalOffset));
/*  99 */       outline.lineTo((x + this.horizontalOffset), yTop);
/* 100 */     } else if (this.flipX && this.flipY) {
/* 101 */       outline.lineTo((x + width - this.horizontalOffset), yTop);
/* 102 */       outline.lineTo((x + width - this.horizontalOffset), (yTop - this.verticalOffset));
/* 103 */       outline.lineTo((x + width - this.horizontalOffset - this.verticalOffset), yTop);
/*     */     } 
/*     */     
/* 106 */     outline.closePath();
/*     */     
/* 108 */     g2d.setPaint(this.fillColor);
/* 109 */     g2d.fill(outline);
/* 110 */     g2d.setPaint(this.borderColor);
/* 111 */     g2d.draw(outline);
/*     */   }
/*     */   
/*     */   public int getMinimalHorizontalOffset() {
/* 115 */     return this.arcWidth + this.verticalOffset;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/net/java/balloontip/styles/RoundedBalloonStyle.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */