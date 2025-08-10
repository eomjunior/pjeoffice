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
/*    */ public class LeftAbovePositioner
/*    */   extends BasicBalloonTipPositioner
/*    */ {
/*    */   public LeftAbovePositioner(int hO, int vO) {
/* 20 */     super(hO, vO);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void determineLocation(Rectangle attached) {
/* 25 */     int balloonWidth = (this.balloonTip.getPreferredSize()).width;
/* 26 */     int balloonHeight = (this.balloonTip.getPreferredSize()).height;
/* 27 */     this.flipX = false;
/* 28 */     this.flipY = false;
/*    */     
/* 30 */     this.hOffset = this.preferredHorizontalOffset;
/* 31 */     if (this.fixedAttachLocation) {
/* 32 */       this.x = (new Float(attached.x + attached.width * this.attachLocationX)).intValue() - this.hOffset;
/* 33 */       this.y = (new Float(attached.y + attached.height * this.attachLocationY)).intValue() - balloonHeight;
/*    */     } else {
/* 35 */       this.x = attached.x;
/* 36 */       this.y = attached.y - balloonHeight;
/*    */     } 
/*    */ 
/*    */     
/* 40 */     if (this.orientationCorrection) {
/*    */       
/* 42 */       if (this.y < 0) {
/* 43 */         this.flipY = true;
/* 44 */         if (this.fixedAttachLocation) {
/* 45 */           this.y += balloonHeight;
/*    */         } else {
/* 47 */           this.y = attached.y + attached.height;
/*    */         } 
/*    */       } 
/*    */ 
/*    */       
/* 52 */       if (this.x < 0) {
/* 53 */         this.flipX = true;
/* 54 */         if (this.fixedAttachLocation) {
/* 55 */           this.x -= balloonWidth - 2 * this.hOffset;
/*    */         } else {
/* 57 */           this.x = attached.x + attached.width - balloonWidth;
/*    */         } 
/* 59 */         this.hOffset = balloonWidth - this.hOffset;
/*    */       } 
/*    */     } 
/*    */ 
/*    */     
/* 64 */     if (this.offsetCorrection)
/* 65 */       applyOffsetCorrection(); 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/net/java/balloontip/positioners/LeftAbovePositioner.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */