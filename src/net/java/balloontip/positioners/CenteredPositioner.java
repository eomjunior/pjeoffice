/*     */ package net.java.balloontip.positioners;
/*     */ 
/*     */ import java.awt.Point;
/*     */ import java.awt.Rectangle;
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
/*     */ public class CenteredPositioner
/*     */   extends BalloonTipPositioner
/*     */ {
/*  21 */   protected int x = 0;
/*  22 */   protected int y = 0;
/*     */   
/*     */   protected boolean flipY = false;
/*     */   
/*     */   protected int preferredVerticalOffset;
/*     */   
/*     */   protected boolean orientationCorrection = true;
/*     */   protected boolean fixedAttachLocation = false;
/*  30 */   protected float attachLocationY = 0.0F;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CenteredPositioner(int vO) {
/*  38 */     this.preferredVerticalOffset = vO;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPreferredVerticalOffset() {
/*  46 */     return this.preferredVerticalOffset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPreferredVerticalOffset(int preferredVerticalOffset) {
/*  54 */     this.preferredVerticalOffset = preferredVerticalOffset;
/*  55 */     this.balloonTip.getStyle().setVerticalOffset(preferredVerticalOffset);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isOrientationCorrected() {
/*  63 */     return this.orientationCorrection;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enableOrientationCorrection(boolean orientationCorrection) {
/*  71 */     this.orientationCorrection = orientationCorrection;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFixedAttachLocation() {
/*  79 */     return this.fixedAttachLocation;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enableFixedAttachLocation(boolean fixedAttachLocation) {
/*  87 */     this.fixedAttachLocation = fixedAttachLocation;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getAttachLocationY() {
/*  96 */     return this.attachLocationY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAttachLocation(float attachLocationX, float attachLocationY) {
/* 104 */     this.attachLocationY = attachLocationY;
/*     */   }
/*     */   
/*     */   public Point getTipLocation() {
/* 108 */     int tipX = this.x + this.balloonTip.getWidth() / 2;
/* 109 */     int tipY = this.y + this.balloonTip.getHeight();
/* 110 */     if (this.flipY) {
/* 111 */       tipY = this.y;
/*     */     }
/* 113 */     return new Point(tipX, tipY);
/*     */   }
/*     */   
/*     */   public void determineAndSetLocation(Rectangle attached) {
/* 117 */     determineLocation(attached);
/* 118 */     this.balloonTip.getStyle().flip(false, this.flipY);
/* 119 */     this.balloonTip.setBounds(this.x, this.y, (this.balloonTip.getPreferredSize()).width, (this.balloonTip.getPreferredSize()).height);
/* 120 */     this.balloonTip.revalidate();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void determineLocation(Rectangle attached) {
/* 129 */     int balloonWidth = (this.balloonTip.getPreferredSize()).width;
/* 130 */     int balloonHeight = (this.balloonTip.getPreferredSize()).height;
/*     */     
/* 132 */     this.flipY = false;
/*     */     
/* 134 */     int hOffset = balloonWidth / 2;
/* 135 */     float attachLocationX = 0.5F;
/*     */     
/* 137 */     this.x = (new Float(attached.x + attached.width * attachLocationX)).intValue() - hOffset;
/* 138 */     if (this.fixedAttachLocation) {
/* 139 */       this.y = (new Float(attached.y + attached.height * this.attachLocationY)).intValue() - balloonHeight;
/*     */     } else {
/* 141 */       this.y = attached.y - balloonHeight;
/*     */     } 
/*     */ 
/*     */     
/* 145 */     if (this.orientationCorrection)
/*     */     {
/* 147 */       if (this.y < 0) {
/* 148 */         this.flipY = true;
/* 149 */         if (this.fixedAttachLocation) {
/* 150 */           this.y += balloonHeight;
/*     */         } else {
/* 152 */           this.y = attached.y + attached.height;
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 158 */     this.balloonTip.getStyle().setHorizontalOffset(hOffset);
/*     */   }
/*     */   
/*     */   protected void onStyleChange() {
/* 162 */     this.balloonTip.getStyle().setHorizontalOffset(getBalloonTip().getWidth() / 2);
/* 163 */     this.balloonTip.getStyle().setVerticalOffset(this.preferredVerticalOffset);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/net/java/balloontip/positioners/CenteredPositioner.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */