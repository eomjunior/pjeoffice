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
/*     */ public class IsometricBalloonStyle
/*     */   extends BalloonTipStyle
/*     */ {
/*     */   private final Color sideColor;
/*     */   private final Color frontColor;
/*     */   private int depth;
/*     */   
/*     */   public IsometricBalloonStyle(Color frontColor, Color sideColor, int depth) {
/*  37 */     this.sideColor = sideColor;
/*  38 */     this.frontColor = frontColor;
/*  39 */     this.depth = depth;
/*     */   }
/*     */   
/*     */   public Insets getBorderInsets(Component c) {
/*  43 */     if (this.flipY) {
/*  44 */       return new Insets(this.verticalOffset + this.depth + 1, 1, 1, this.depth + 1);
/*     */     }
/*  46 */     return new Insets(this.depth + 1, 1, this.verticalOffset + 1, this.depth + 1);
/*     */   }
/*     */   
/*     */   public boolean isBorderOpaque() {
/*  50 */     return true;
/*     */   }
/*     */   public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
/*     */     int yTop, yBottom;
/*  54 */     Graphics2D g2d = (Graphics2D)g;
/*  55 */     width -= this.depth + 1;
/*  56 */     height--;
/*     */ 
/*     */ 
/*     */     
/*  60 */     if (this.flipY) {
/*  61 */       yTop = y + this.verticalOffset + this.depth;
/*  62 */       yBottom = y + height;
/*     */     } else {
/*  64 */       yTop = y + this.depth;
/*  65 */       yBottom = y + height - this.verticalOffset;
/*     */     } 
/*     */ 
/*     */     
/*  69 */     GeneralPath top = new GeneralPath();
/*  70 */     top.moveTo(x, yTop);
/*  71 */     top.lineTo(width, yTop);
/*  72 */     top.lineTo((width + this.depth), (yTop - this.depth));
/*  73 */     top.lineTo((x + this.depth), (yTop - this.depth));
/*  74 */     top.closePath();
/*  75 */     g2d.setPaint(this.sideColor);
/*  76 */     g2d.fill(top);
/*     */ 
/*     */     
/*  79 */     GeneralPath side = new GeneralPath();
/*  80 */     side.moveTo(width, yTop);
/*  81 */     side.lineTo((width + this.depth), (yTop - this.depth));
/*  82 */     side.lineTo((width + this.depth), (yBottom - this.depth));
/*  83 */     side.lineTo(width, yBottom);
/*  84 */     side.closePath();
/*  85 */     g2d.setPaint(this.sideColor.darker());
/*  86 */     g2d.fill(side);
/*     */ 
/*     */     
/*  89 */     if (this.flipX && !this.flipY) {
/*  90 */       GeneralPath tipSide = new GeneralPath();
/*  91 */       tipSide.moveTo((x + width - this.horizontalOffset), yBottom);
/*  92 */       tipSide.lineTo((x + width - this.horizontalOffset + this.depth), yBottom);
/*  93 */       tipSide.lineTo((x + width - this.horizontalOffset + this.depth), (yBottom + this.verticalOffset - this.depth));
/*  94 */       tipSide.lineTo((x + width - this.horizontalOffset), (yBottom + this.verticalOffset));
/*  95 */       tipSide.closePath();
/*  96 */       g2d.setPaint(this.sideColor.darker());
/*  97 */       g2d.fill(tipSide);
/*  98 */     } else if (!this.flipX && this.flipY) {
/*  99 */       GeneralPath tipSide = new GeneralPath();
/* 100 */       tipSide.moveTo((x + this.horizontalOffset), (yTop - this.verticalOffset));
/* 101 */       tipSide.lineTo((x + this.horizontalOffset + this.depth), (yTop - this.verticalOffset - this.depth));
/* 102 */       tipSide.lineTo((x + this.horizontalOffset + this.verticalOffset + this.depth), (yTop - this.depth));
/* 103 */       tipSide.lineTo((x + this.horizontalOffset + this.verticalOffset), yTop);
/* 104 */       tipSide.closePath();
/* 105 */       g2d.setPaint(this.sideColor.darker());
/* 106 */       g2d.fill(tipSide);
/* 107 */     } else if (this.flipX && this.flipY) {
/* 108 */       GeneralPath tipSide = new GeneralPath();
/* 109 */       tipSide.moveTo((x + width - this.horizontalOffset), yTop);
/* 110 */       tipSide.lineTo((x + width - this.horizontalOffset + this.depth), (yTop - this.depth));
/* 111 */       tipSide.lineTo((x + width - this.horizontalOffset + this.depth), (yTop - this.depth - this.verticalOffset));
/* 112 */       tipSide.lineTo((x + width - this.horizontalOffset), (yTop - this.verticalOffset));
/* 113 */       tipSide.closePath();
/* 114 */       g2d.setPaint(this.sideColor.darker());
/* 115 */       g2d.fill(tipSide);
/*     */     } 
/*     */ 
/*     */     
/* 119 */     GeneralPath front = new GeneralPath();
/* 120 */     front.moveTo(x, yTop);
/* 121 */     front.lineTo(x, yBottom);
/*     */     
/* 123 */     if (!this.flipX && !this.flipY) {
/* 124 */       front.lineTo((x + this.horizontalOffset), yBottom);
/* 125 */       front.lineTo((x + this.horizontalOffset), (yBottom + this.verticalOffset));
/* 126 */       front.lineTo((x + this.horizontalOffset + this.verticalOffset), yBottom);
/* 127 */     } else if (this.flipX && !this.flipY) {
/* 128 */       front.lineTo((x + width - this.horizontalOffset - this.verticalOffset), yBottom);
/* 129 */       front.lineTo((x + width - this.horizontalOffset), (yBottom + this.verticalOffset));
/* 130 */       front.lineTo((x + width - this.horizontalOffset), yBottom);
/*     */     } 
/*     */     
/* 133 */     front.lineTo((x + width), yBottom);
/* 134 */     front.lineTo((x + width), yTop);
/*     */     
/* 136 */     if (!this.flipX && this.flipY) {
/* 137 */       front.lineTo((x + this.horizontalOffset + this.verticalOffset), yTop);
/* 138 */       front.lineTo((x + this.horizontalOffset), (yTop - this.verticalOffset));
/* 139 */       front.lineTo((x + this.horizontalOffset), yTop);
/* 140 */     } else if (this.flipX && this.flipY) {
/* 141 */       front.lineTo((x + width - this.horizontalOffset), yTop);
/* 142 */       front.lineTo((x + width - this.horizontalOffset), (yTop - this.verticalOffset));
/* 143 */       front.lineTo((x + width - this.horizontalOffset - this.verticalOffset), yTop);
/*     */     } 
/*     */     
/* 146 */     front.closePath();
/*     */     
/* 148 */     g2d.setPaint(this.frontColor);
/* 149 */     g2d.fill(front);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/net/java/balloontip/styles/IsometricBalloonStyle.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */