/*    */ package com.github.signer4j.imp;
/*    */ 
/*    */ import com.github.signer4j.IWindowLockDettector;
/*    */ import com.github.signer4j.IWorkstationLockListener;
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
/*    */ public enum WindowLockDettector
/*    */   implements IWindowLockDettector
/*    */ {
/* 34 */   IDLE,
/* 35 */   LINUX,
/* 36 */   MAC,
/* 37 */   WINDOWS {
/* 38 */     private final IWindowLockDettector forWindows = new ForWindowsLockDettector();
/*    */ 
/*    */     
/*    */     public void start() {
/* 42 */       this.forWindows.start();
/*    */     }
/*    */ 
/*    */     
/*    */     public void stop() {
/* 47 */       this.forWindows.stop();
/*    */     }
/*    */ 
/*    */     
/*    */     public IWindowLockDettector notifyTo(IWorkstationLockListener listener) {
/* 52 */       this.forWindows.notifyTo(listener);
/* 53 */       return this;
/*    */     }
/*    */   };
/*    */   
/*    */   public static IWindowLockDettector getBest() {
/* 58 */     return SystemSupport.getDefault().getWindowLockDettector();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void start() {}
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void stop() {}
/*    */ 
/*    */ 
/*    */   
/*    */   public IWindowLockDettector notifyTo(IWorkstationLockListener listener) {
/* 74 */     return this;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/WindowLockDettector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */