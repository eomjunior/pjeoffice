/*     */ package net.java.balloontip.styles;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.TexturePaint;
/*     */ import java.awt.geom.GeneralPath;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.io.IOException;
/*     */ import java.net.URL;
/*     */ import javax.imageio.ImageIO;
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
/*     */ public class TexturedBalloonStyle
/*     */   extends BalloonTipStyle
/*     */ {
/*     */   private final int arcWidth;
/*     */   private final int arcHeight;
/*     */   private final BufferedImage bg;
/*     */   private final Rectangle bgBounds;
/*     */   private final Color borderColor;
/*     */   
/*     */   public TexturedBalloonStyle(int arcWidth, int arcHeight, URL background, Color borderColor) throws IOException {
/*  47 */     this.arcWidth = arcWidth;
/*  48 */     this.arcHeight = arcHeight;
/*  49 */     this.bg = ImageIO.read(background);
/*  50 */     this.bgBounds = new Rectangle(0, 0, this.bg.getWidth(), this.bg.getHeight());
/*  51 */     this.borderColor = borderColor;
/*     */   }
/*     */   
/*     */   public Insets getBorderInsets(Component c) {
/*  55 */     if (this.flipY) {
/*  56 */       return new Insets(this.verticalOffset + this.arcHeight, this.arcWidth, this.arcHeight, this.arcWidth);
/*     */     }
/*  58 */     return new Insets(this.arcHeight, this.arcWidth, this.arcHeight + this.verticalOffset, this.arcWidth);
/*     */   }
/*     */   
/*     */   public boolean isBorderOpaque() {
/*  62 */     return true;
/*     */   }
/*     */   public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
/*     */     int yTop, yBottom;
/*  66 */     Graphics2D g2d = (Graphics2D)g;
/*  67 */     width--;
/*  68 */     height--;
/*     */ 
/*     */ 
/*     */     
/*  72 */     if (this.flipY) {
/*  73 */       yTop = y + this.verticalOffset;
/*  74 */       yBottom = y + height;
/*     */     } else {
/*  76 */       yTop = y;
/*  77 */       yBottom = y + height - this.verticalOffset;
/*     */     } 
/*     */ 
/*     */     
/*  81 */     GeneralPath outline = new GeneralPath();
/*  82 */     outline.moveTo((x + this.arcWidth), yTop);
/*     */     
/*  84 */     outline.quadTo(x, yTop, x, (yTop + this.arcHeight));
/*  85 */     outline.lineTo(x, (yBottom - this.arcHeight));
/*  86 */     outline.quadTo(x, yBottom, (x + this.arcWidth), yBottom);
/*     */ 
/*     */     
/*  89 */     if (!this.flipX && !this.flipY) {
/*  90 */       outline.lineTo((x + this.horizontalOffset), yBottom);
/*  91 */       outline.lineTo((x + this.horizontalOffset), (yBottom + this.verticalOffset));
/*  92 */       outline.lineTo((x + this.horizontalOffset + this.verticalOffset), yBottom);
/*  93 */     } else if (this.flipX && !this.flipY) {
/*  94 */       outline.lineTo((x + width - this.horizontalOffset - this.verticalOffset), yBottom);
/*  95 */       outline.lineTo((x + width - this.horizontalOffset), (yBottom + this.verticalOffset));
/*  96 */       outline.lineTo((x + width - this.horizontalOffset), yBottom);
/*     */     } 
/*     */     
/*  99 */     outline.lineTo((x + width - this.arcWidth), yBottom);
/* 100 */     outline.quadTo((x + width), yBottom, (x + width), (yBottom - this.arcHeight));
/* 101 */     outline.lineTo((x + width), (yTop + this.arcHeight));
/* 102 */     outline.quadTo((x + width), yTop, (x + width - this.arcWidth), yTop);
/*     */     
/* 104 */     if (!this.flipX && this.flipY) {
/* 105 */       outline.lineTo((x + this.horizontalOffset + this.verticalOffset), yTop);
/* 106 */       outline.lineTo((x + this.horizontalOffset), (yTop - this.verticalOffset));
/* 107 */       outline.lineTo((x + this.horizontalOffset), yTop);
/* 108 */     } else if (this.flipX && this.flipY) {
/* 109 */       outline.lineTo((x + width - this.horizontalOffset), yTop);
/* 110 */       outline.lineTo((x + width - this.horizontalOffset), (yTop - this.verticalOffset));
/* 111 */       outline.lineTo((x + width - this.horizontalOffset - this.verticalOffset), yTop);
/*     */     } 
/*     */     
/* 114 */     outline.closePath();
/*     */     
/* 116 */     g2d.setPaint(new TexturePaint(this.bg, this.bgBounds));
/* 117 */     g2d.fill(outline);
/* 118 */     g2d.setPaint(this.borderColor);
/* 119 */     g2d.draw(outline);
/*     */   }
/*     */   
/*     */   public int getMinimalHorizontalOffset() {
/* 123 */     return this.arcWidth + this.verticalOffset;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/net/java/balloontip/styles/TexturedBalloonStyle.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */