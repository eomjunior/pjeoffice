/*     */ package net.java.balloontip.styles;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Insets;
/*     */ import java.awt.RenderingHints;
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
/*     */ public class MinimalBalloonStyle
/*     */   extends BalloonTipStyle
/*     */ {
/*     */   private final int arcWidth;
/*     */   private final Color fillColor;
/*     */   
/*     */   public MinimalBalloonStyle(Color fillColor, int arcWidth) {
/*  35 */     this.fillColor = fillColor;
/*  36 */     this.arcWidth = arcWidth;
/*     */   }
/*     */   
/*     */   public Insets getBorderInsets(Component c) {
/*  40 */     if (this.flipY) {
/*  41 */       return new Insets(this.verticalOffset + this.arcWidth, this.arcWidth, this.arcWidth, this.arcWidth);
/*     */     }
/*  43 */     return new Insets(this.arcWidth, this.arcWidth, this.arcWidth + this.verticalOffset, this.arcWidth);
/*     */   }
/*     */   public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
/*     */     int yTop, yBottom;
/*  47 */     Graphics2D g2d = (Graphics2D)g;
/*  48 */     g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
/*     */ 
/*     */ 
/*     */     
/*  52 */     if (this.flipY) {
/*  53 */       yTop = y + this.verticalOffset;
/*  54 */       yBottom = y + height;
/*     */     } else {
/*  56 */       yTop = y;
/*  57 */       yBottom = y + height - this.verticalOffset;
/*     */     } 
/*     */ 
/*     */     
/*  61 */     GeneralPath outline = new GeneralPath();
/*  62 */     outline.moveTo((x + this.arcWidth), yTop);
/*     */ 
/*     */     
/*  65 */     outline.quadTo(x, yTop, x, (yTop + this.arcWidth));
/*     */ 
/*     */     
/*  68 */     outline.lineTo(x, (yBottom - this.arcWidth));
/*     */ 
/*     */     
/*  71 */     outline.quadTo(x, yBottom, (x + this.arcWidth), yBottom);
/*     */ 
/*     */     
/*  74 */     if (!this.flipX && !this.flipY) {
/*  75 */       outline.lineTo((x + this.horizontalOffset - this.verticalOffset), yBottom);
/*  76 */       outline.lineTo((x + this.horizontalOffset), (yBottom + this.verticalOffset));
/*  77 */       outline.lineTo((x + this.horizontalOffset + this.verticalOffset), yBottom);
/*  78 */     } else if (this.flipX && !this.flipY) {
/*  79 */       outline.lineTo((x + width - this.horizontalOffset - this.verticalOffset), yBottom);
/*  80 */       outline.lineTo((x + width - this.horizontalOffset), (yBottom + this.verticalOffset));
/*  81 */       outline.lineTo((x + width - this.horizontalOffset + this.verticalOffset), yBottom);
/*     */     } 
/*  83 */     outline.lineTo((x + width - this.arcWidth), yBottom);
/*     */ 
/*     */     
/*  86 */     outline.quadTo((x + width), yBottom, (x + width), (yBottom - this.arcWidth));
/*     */ 
/*     */     
/*  89 */     outline.lineTo((x + width), (yTop + this.arcWidth));
/*     */ 
/*     */     
/*  92 */     outline.quadTo((x + width), yTop, (x + width - this.arcWidth), yTop);
/*     */ 
/*     */     
/*  95 */     if (!this.flipX && this.flipY) {
/*  96 */       outline.lineTo((x + this.horizontalOffset + this.verticalOffset), yTop);
/*  97 */       outline.lineTo((x + this.horizontalOffset), (yTop - this.verticalOffset));
/*  98 */       outline.lineTo((x + this.horizontalOffset - this.verticalOffset), yTop);
/*  99 */     } else if (this.flipX && this.flipY) {
/* 100 */       outline.lineTo((x + width - this.horizontalOffset + this.verticalOffset), yTop);
/* 101 */       outline.lineTo((x + width - this.horizontalOffset), (yTop - this.verticalOffset));
/* 102 */       outline.lineTo((x + width - this.horizontalOffset - this.verticalOffset), yTop);
/*     */     } 
/*     */     
/* 105 */     outline.closePath();
/*     */     
/* 107 */     g2d.setPaint(this.fillColor);
/* 108 */     g2d.fill(outline);
/*     */   }
/*     */   
/*     */   public int getMinimalHorizontalOffset() {
/* 112 */     return this.arcWidth + this.verticalOffset;
/*     */   }
/*     */   
/*     */   public boolean isBorderOpaque() {
/* 116 */     return (this.fillColor.getAlpha() == 255);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/net/java/balloontip/styles/MinimalBalloonStyle.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */