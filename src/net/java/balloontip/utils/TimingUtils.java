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
/*    */ public final class TimingUtils
/*    */ {
/*    */   public static void showTimedBalloon(final BalloonTip balloon, int time) {
/* 37 */     showTimedBalloon(balloon, time, new ActionListener() {
/*    */           public void actionPerformed(ActionEvent e) {
/* 39 */             balloon.closeBalloon();
/*    */           }
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void showTimedBalloon(BalloonTip balloon, int time, ActionListener onTimeout) {
/* 51 */     balloon.setVisible(true);
/* 52 */     Timer timer = new Timer(time, onTimeout);
/* 53 */     timer.setRepeats(false);
/* 54 */     timer.start();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/net/java/balloontip/utils/TimingUtils.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */