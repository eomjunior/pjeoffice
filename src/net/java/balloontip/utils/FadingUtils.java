/*    */ package net.java.balloontip.utils;
/*    */ 
/*    */ import java.awt.event.ActionEvent;
/*    */ import java.awt.event.ActionListener;
/*    */ import javax.swing.Timer;
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
/*    */ public final class FadingUtils
/*    */ {
/*    */   public static void fadeInBalloon(final BalloonTip balloon, final ActionListener onStop, final int time, int refreshRate) {
/* 38 */     balloon.setOpacity(0.0F);
/* 39 */     balloon.setVisible(true);
/*    */     
/* 41 */     final int timeDelta = 1000 / refreshRate;
/*    */     
/* 43 */     Timer timer = new Timer(timeDelta, new ActionListener() {
/* 44 */           int curTime = 0;
/*    */           public void actionPerformed(ActionEvent e) {
/* 46 */             this.curTime += timeDelta;
/* 47 */             float newOpacity = this.curTime / time;
/* 48 */             if (newOpacity >= 0.9999999F || Float.isNaN(newOpacity)) {
/* 49 */               ((Timer)e.getSource()).stop();
/*    */ 
/*    */               
/* 52 */               balloon.setOpacity(0.9999999F);
/* 53 */               if (onStop != null) {
/* 54 */                 onStop.actionPerformed(e);
/*    */               }
/*    */             } else {
/* 57 */               balloon.setOpacity(newOpacity);
/*    */             } 
/*    */           }
/*    */         });
/* 61 */     timer.setRepeats(true);
/* 62 */     timer.start();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void fadeOutBalloon(final BalloonTip balloon, final ActionListener onStop, final int time, int refreshRate) {
/* 73 */     balloon.setOpacity(0.9999999F);
/* 74 */     balloon.setVisible(true);
/*    */     
/* 76 */     final int timeDelta = 1000 / refreshRate;
/* 77 */     Timer timer = new Timer(timeDelta, new ActionListener() {
/* 78 */           int curTime = 0;
/*    */           public void actionPerformed(ActionEvent e) {
/* 80 */             this.curTime += timeDelta;
/* 81 */             float newOpacity = -1.0F / time * this.curTime + 1.0F;
/* 82 */             if (newOpacity <= 0.0F || Float.isNaN(newOpacity)) {
/* 83 */               ((Timer)e.getSource()).stop();
/* 84 */               balloon.setOpacity(0.0F);
/* 85 */               if (onStop != null) {
/* 86 */                 onStop.actionPerformed(e);
/*    */               }
/*    */             } else {
/* 89 */               balloon.setOpacity(newOpacity);
/*    */             } 
/*    */           }
/*    */         });
/* 93 */     timer.setRepeats(true);
/* 94 */     timer.start();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/net/java/balloontip/utils/FadingUtils.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */