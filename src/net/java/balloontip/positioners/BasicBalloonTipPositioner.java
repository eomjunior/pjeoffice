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
/*     */ 
/*     */ public abstract class BasicBalloonTipPositioner
/*     */   extends BalloonTipPositioner
/*     */ {
/*  22 */   protected int x = 0;
/*  23 */   protected int y = 0;
/*  24 */   protected int hOffset = 0;
/*     */   
/*     */   protected boolean flipX = false;
/*     */   
/*     */   protected boolean flipY = false;
/*     */   
/*     */   protected int preferredHorizontalOffset;
/*     */   
/*     */   protected int preferredVerticalOffset;
/*     */   protected int minimumHorizontalOffset;
/*     */   protected boolean offsetCorrection = true;
/*     */   protected boolean orientationCorrection = true;
/*     */   protected boolean fixedAttachLocation = false;
/*  37 */   protected float attachLocationX = 0.0F;
/*  38 */   protected float attachLocationY = 0.0F;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BasicBalloonTipPositioner(int hO, int vO) {
/*  48 */     this.preferredHorizontalOffset = hO;
/*  49 */     this.preferredVerticalOffset = vO;
/*     */   }
/*     */   
/*     */   protected void onStyleChange() {
/*  53 */     this.balloonTip.getStyle().setHorizontalOffset(this.preferredHorizontalOffset);
/*  54 */     this.balloonTip.getStyle().setVerticalOffset(this.preferredVerticalOffset);
/*  55 */     this.minimumHorizontalOffset = this.balloonTip.getStyle().getMinimalHorizontalOffset();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPreferredHorizontalOffset() {
/*  63 */     return this.preferredHorizontalOffset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPreferredHorizontalOffset(int preferredHorizontalOffset) {
/*  71 */     this.preferredHorizontalOffset = preferredHorizontalOffset;
/*  72 */     this.balloonTip.getStyle().setHorizontalOffset(preferredHorizontalOffset);
/*  73 */     this.balloonTip.repaint();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPreferredVerticalOffset() {
/*  81 */     return this.preferredVerticalOffset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPreferredVerticalOffset(int preferredVerticalOffset) {
/*  89 */     this.preferredVerticalOffset = preferredVerticalOffset;
/*  90 */     this.minimumHorizontalOffset = 2 * preferredVerticalOffset;
/*  91 */     this.balloonTip.getStyle().setVerticalOffset(preferredVerticalOffset);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isOffsetCorrected() {
/*  99 */     return this.offsetCorrection;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enableOffsetCorrection(boolean offsetCorrection) {
/* 107 */     this.offsetCorrection = offsetCorrection;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isOrientationCorrected() {
/* 115 */     return this.orientationCorrection;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enableOrientationCorrection(boolean orientationCorrection) {
/* 123 */     this.orientationCorrection = orientationCorrection;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFixedAttachLocation() {
/* 131 */     return this.fixedAttachLocation;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enableFixedAttachLocation(boolean fixedAttachLocation) {
/* 139 */     this.fixedAttachLocation = fixedAttachLocation;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getAttachLocationX() {
/* 148 */     return this.attachLocationX;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getAttachLocationY() {
/* 157 */     return this.attachLocationY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAttachLocation(float attachLocationX, float attachLocationY) {
/* 166 */     this.attachLocationX = attachLocationX;
/* 167 */     this.attachLocationY = attachLocationY;
/*     */   }
/*     */   
/*     */   public Point getTipLocation() {
/* 171 */     int tipX = this.x + this.hOffset;
/* 172 */     int tipY = this.y + this.balloonTip.getHeight();
/*     */     
/* 174 */     if (this.flipX) {
/* 175 */       tipX = this.x + this.hOffset;
/*     */     }
/* 177 */     if (this.flipY) {
/* 178 */       tipY = this.y;
/*     */     }
/*     */     
/* 181 */     return new Point(tipX, tipY);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void applyOffsetCorrection() {
/* 189 */     int overflow = -this.x;
/* 190 */     int balloonWidth = (this.balloonTip.getPreferredSize()).width;
/*     */     
/* 192 */     if (overflow > 0) {
/* 193 */       this.x += overflow;
/* 194 */       this.hOffset -= overflow;
/*     */       
/* 196 */       if (this.hOffset < this.minimumHorizontalOffset) {
/* 197 */         this.hOffset = this.minimumHorizontalOffset;
/* 198 */         if (this.flipX) {
/* 199 */           this.x += -overflow + balloonWidth - this.preferredHorizontalOffset - this.minimumHorizontalOffset;
/*     */         } else {
/* 201 */           this.x += -overflow + this.preferredHorizontalOffset - this.minimumHorizontalOffset;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 207 */     overflow = this.x + balloonWidth - this.balloonTip.getTopLevelContainer().getWidth();
/* 208 */     if (overflow > 0) {
/* 209 */       this.x -= overflow;
/* 210 */       this.hOffset += overflow;
/*     */ 
/*     */       
/* 213 */       if (this.hOffset > balloonWidth - this.minimumHorizontalOffset) {
/* 214 */         this.hOffset = balloonWidth - this.minimumHorizontalOffset;
/* 215 */         if (this.flipX) {
/* 216 */           this.x += overflow + this.preferredHorizontalOffset + this.minimumHorizontalOffset;
/*     */         } else {
/* 218 */           this.x += overflow - balloonWidth - this.preferredHorizontalOffset + this.minimumHorizontalOffset;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void determineAndSetLocation(Rectangle attached) {
/* 225 */     determineLocation(attached);
/*     */     
/* 227 */     if (this.flipX) {
/* 228 */       this.balloonTip.getStyle().setHorizontalOffset((this.balloonTip.getPreferredSize()).width - this.hOffset);
/*     */     } else {
/* 230 */       this.balloonTip.getStyle().setHorizontalOffset(this.hOffset);
/*     */     } 
/*     */     
/* 233 */     this.balloonTip.getStyle().flip(this.flipX, this.flipY);
/* 234 */     this.balloonTip.setBounds(this.x, this.y, (this.balloonTip.getPreferredSize()).width, (this.balloonTip.getPreferredSize()).height);
/*     */     
/* 236 */     this.balloonTip.revalidate();
/* 237 */     if (this.hOffset != this.preferredHorizontalOffset)
/* 238 */       this.balloonTip.repaint(); 
/*     */   }
/*     */   
/*     */   protected abstract void determineLocation(Rectangle paramRectangle);
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/net/java/balloontip/positioners/BasicBalloonTipPositioner.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */