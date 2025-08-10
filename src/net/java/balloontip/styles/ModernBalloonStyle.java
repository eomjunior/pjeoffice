/*     */ package net.java.balloontip.styles;
/*     */ 
/*     */ import java.awt.BasicStroke;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.GradientPaint;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Insets;
/*     */ import java.awt.RenderingHints;
/*     */ import java.awt.Stroke;
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
/*     */ public class ModernBalloonStyle
/*     */   extends BalloonTipStyle
/*     */ {
/*     */   private final int arcWidth;
/*     */   private final int arcHeight;
/*     */   private boolean topLeft = true;
/*     */   private boolean topRight = false;
/*     */   private boolean bottomLeft = false;
/*     */   private boolean bottomRight = true;
/*  39 */   private int borderThickness = 1;
/*     */ 
/*     */   
/*     */   private boolean AAenabled = false;
/*     */ 
/*     */   
/*     */   private final Color topFillColor;
/*     */ 
/*     */   
/*     */   private final Color bottomFillColor;
/*     */ 
/*     */   
/*     */   private final Color borderColor;
/*     */ 
/*     */ 
/*     */   
/*     */   public ModernBalloonStyle(int arcWidth, int arcHeight, Color topFillColor, Color bottomFillColor, Color borderColor) {
/*  56 */     this.arcWidth = arcWidth;
/*  57 */     this.arcHeight = arcHeight;
/*  58 */     this.topFillColor = topFillColor;
/*  59 */     this.bottomFillColor = bottomFillColor;
/*  60 */     this.borderColor = borderColor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCornerStyles(boolean topLeft, boolean topRight, boolean bottomLeft, boolean bottomRight) {
/*  72 */     this.topLeft = topLeft;
/*  73 */     this.topRight = topRight;
/*  74 */     this.bottomLeft = bottomLeft;
/*  75 */     this.bottomRight = bottomRight;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBorderThickness(int thickness) {
/*  83 */     this.borderThickness = thickness;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enableAntiAliasing(boolean enable) {
/*  91 */     this.AAenabled = enable;
/*     */   }
/*     */   
/*     */   public Insets getBorderInsets(Component c) {
/*  95 */     if (this.flipY) {
/*  96 */       return new Insets(this.verticalOffset + this.arcHeight, this.arcWidth, this.arcHeight, this.arcWidth);
/*     */     }
/*  98 */     return new Insets(this.arcHeight, this.arcWidth, this.arcHeight + this.verticalOffset, this.arcWidth);
/*     */   }
/*     */   
/*     */   public boolean isBorderOpaque() {
/* 102 */     return true;
/*     */   }
/*     */   public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
/*     */     int yTop, yBottom;
/* 106 */     Graphics2D g2d = (Graphics2D)g;
/* 107 */     if (this.AAenabled) {
/* 108 */       g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 113 */     x += this.borderThickness - 1;
/* 114 */     y += this.borderThickness - 1;
/* 115 */     width -= this.borderThickness * 2;
/* 116 */     height -= this.borderThickness * 2;
/*     */ 
/*     */ 
/*     */     
/* 120 */     if (this.flipY) {
/* 121 */       yTop = y + this.verticalOffset;
/* 122 */       yBottom = y + height;
/*     */     } else {
/* 124 */       yTop = y;
/* 125 */       yBottom = y + height - this.verticalOffset;
/*     */     } 
/*     */ 
/*     */     
/* 129 */     GeneralPath outline = new GeneralPath();
/* 130 */     outline.moveTo((x + this.arcWidth), yTop);
/*     */     
/* 132 */     if (this.topLeft) {
/* 133 */       outline.quadTo(x, yTop, x, (yTop + this.arcHeight));
/*     */     } else {
/* 135 */       outline.lineTo(x, yTop);
/* 136 */       outline.lineTo(x, (yTop + this.arcHeight));
/*     */     } 
/*     */     
/* 139 */     outline.lineTo(x, (yBottom - this.arcHeight));
/*     */     
/* 141 */     if (this.bottomLeft) {
/* 142 */       outline.quadTo(x, yBottom, (x + this.arcWidth), yBottom);
/*     */     } else {
/* 144 */       outline.lineTo(x, yBottom);
/* 145 */       outline.lineTo((x + this.arcWidth), yBottom);
/*     */     } 
/*     */     
/* 148 */     if (!this.flipX && !this.flipY) {
/* 149 */       outline.lineTo((x + this.horizontalOffset), yBottom);
/* 150 */       outline.lineTo((x + this.horizontalOffset), (yBottom + this.verticalOffset));
/* 151 */       outline.lineTo((x + this.horizontalOffset + this.verticalOffset), yBottom);
/* 152 */     } else if (this.flipX && !this.flipY) {
/* 153 */       outline.lineTo((x + width - this.horizontalOffset - this.verticalOffset), yBottom);
/* 154 */       outline.lineTo((x + width - this.horizontalOffset), (yBottom + this.verticalOffset));
/* 155 */       outline.lineTo((x + width - this.horizontalOffset), yBottom);
/*     */     } 
/*     */     
/* 158 */     outline.lineTo((x + width - this.arcWidth), yBottom);
/*     */     
/* 160 */     if (this.bottomRight) {
/* 161 */       outline.quadTo((x + width), yBottom, (x + width), (yBottom - this.arcHeight));
/*     */     } else {
/* 163 */       outline.lineTo((x + width), yBottom);
/* 164 */       outline.lineTo((x + width), (yBottom - this.arcHeight));
/*     */     } 
/*     */     
/* 167 */     outline.lineTo((x + width), (yTop + this.arcHeight));
/*     */     
/* 169 */     if (this.topRight) {
/* 170 */       outline.quadTo((x + width), yTop, (x + width - this.arcWidth), yTop);
/*     */     } else {
/* 172 */       outline.lineTo((x + width), yTop);
/* 173 */       outline.lineTo((x + width - this.arcWidth), yTop);
/*     */     } 
/*     */     
/* 176 */     if (!this.flipX && this.flipY) {
/* 177 */       outline.lineTo((x + this.horizontalOffset + this.verticalOffset), yTop);
/* 178 */       outline.lineTo((x + this.horizontalOffset), (yTop - this.verticalOffset));
/* 179 */       outline.lineTo((x + this.horizontalOffset), yTop);
/* 180 */     } else if (this.flipX && this.flipY) {
/* 181 */       outline.lineTo((x + width - this.horizontalOffset), yTop);
/* 182 */       outline.lineTo((x + width - this.horizontalOffset), (yTop - this.verticalOffset));
/* 183 */       outline.lineTo((x + width - this.horizontalOffset - this.verticalOffset), yTop);
/*     */     } 
/*     */     
/* 186 */     outline.closePath();
/*     */ 
/*     */     
/* 189 */     g2d.setPaint(new GradientPaint(0.0F, yTop, this.topFillColor, 0.0F, yBottom, this.bottomFillColor));
/* 190 */     g2d.fill(outline);
/* 191 */     g2d.setPaint(this.borderColor);
/* 192 */     Stroke backup = g2d.getStroke();
/* 193 */     g2d.setStroke(new BasicStroke(this.borderThickness));
/* 194 */     g2d.draw(outline);
/* 195 */     g2d.setStroke(backup);
/*     */   }
/*     */   
/*     */   public int getMinimalHorizontalOffset() {
/* 199 */     return this.arcWidth + this.verticalOffset + this.borderThickness;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/net/java/balloontip/styles/ModernBalloonStyle.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */