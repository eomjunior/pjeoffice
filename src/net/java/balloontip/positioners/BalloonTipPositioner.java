/*    */ package net.java.balloontip.positioners;
/*    */ 
/*    */ import java.awt.Point;
/*    */ import java.awt.Rectangle;
/*    */ import java.beans.PropertyChangeEvent;
/*    */ import java.beans.PropertyChangeListener;
/*    */ import net.java.balloontip.BalloonTip;
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
/*    */ public abstract class BalloonTipPositioner
/*    */ {
/* 25 */   protected BalloonTip balloonTip = null;
/* 26 */   private PropertyChangeListener styleListener = new PropertyChangeListener() {
/*    */       public void propertyChange(PropertyChangeEvent evt) {
/* 28 */         BalloonTipPositioner.this.onStyleChange();
/*    */       }
/*    */     };
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
/*    */   public final BalloonTip getBalloonTip() {
/* 42 */     return this.balloonTip;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final void setBalloonTip(BalloonTip balloonTip) {
/* 51 */     this.balloonTip = balloonTip;
/* 52 */     this.balloonTip.addPropertyChangeListener("style", this.styleListener);
/* 53 */     onStyleChange();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public abstract Point getTipLocation();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public abstract void determineAndSetLocation(Rectangle paramRectangle);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected abstract void onStyleChange();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void finalize() throws Throwable {
/* 75 */     if (this.balloonTip != null) {
/* 76 */       this.balloonTip.removePropertyChangeListener("style", this.styleListener);
/*    */     }
/* 78 */     super.finalize();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/net/java/balloontip/positioners/BalloonTipPositioner.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */