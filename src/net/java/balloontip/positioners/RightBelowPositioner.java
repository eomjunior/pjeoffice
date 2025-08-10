/*    */ package net.java.balloontip.positioners;
/*    */ 
/*    */ import java.awt.Rectangle;
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
/*    */ public class RightBelowPositioner
/*    */   extends BasicBalloonTipPositioner
/*    */ {
/*    */   public RightBelowPositioner(int hO, int vO) {
/* 20 */     super(hO, vO);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void determineLocation(Rectangle attached) {
/* 25 */     int balloonWidth = (this.balloonTip.getPreferredSize()).width;
/* 26 */     int balloonHeight = (this.balloonTip.getPreferredSize()).height;
/* 27 */     this.flipX = true;
/* 28 */     this.flipY = true;
/*    */     
/* 30 */     this.hOffset = balloonWidth - this.preferredHorizontalOffset;
/* 31 */     if (this.fixedAttachLocation) {
/* 32 */       this.x = (new Float(attached.x + attached.width * this.attachLocationX)).intValue() - this.hOffset;
/* 33 */       this.y = (new Float(attached.y + attached.height * this.attachLocationY)).intValue();
/*    */     } else {
/* 35 */       this.x = attached.x + attached.width - balloonWidth;
/* 36 */       this.y = attached.y + attached.height;
/*    */     } 
/*    */     
/* 39 */     if (this.orientationCorrection) {
/*    */       
/* 41 */       if (this.y + balloonHeight > this.balloonTip.getTopLevelContainer().getHeight()) {
/* 42 */         this.flipY = false;
/* 43 */         if (this.fixedAttachLocation) {
/* 44 */           this.y -= balloonHeight;
/*    */         } else {
/* 46 */           this.y = attached.y - balloonHeight;
/*    */         } 
/*    */       } 
/*    */ 
/*    */       
/* 51 */       if (this.x + balloonWidth > this.balloonTip.getTopLevelContainer().getWidth()) {
/* 52 */         this.flipX = false;
/* 53 */         this.hOffset = balloonWidth - this.hOffset;
/* 54 */         if (this.fixedAttachLocation) {
/* 55 */           this.x += balloonWidth - 2 * this.hOffset;
/*    */         } else {
/* 57 */           this.x = attached.x;
/*    */         } 
/*    */       } 
/*    */     } 
/*    */ 
/*    */     
/* 63 */     if (this.offsetCorrection)
/* 64 */       applyOffsetCorrection(); 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/net/java/balloontip/positioners/RightBelowPositioner.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */