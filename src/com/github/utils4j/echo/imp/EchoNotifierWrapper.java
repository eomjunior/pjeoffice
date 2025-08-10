/*    */ package com.github.utils4j.echo.imp;
/*    */ 
/*    */ import com.github.utils4j.echo.IEchoNotifier;
/*    */ import com.github.utils4j.imp.Args;
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
/*    */ public class EchoNotifierWrapper
/*    */   implements IEchoNotifier
/*    */ {
/*    */   private final IEchoNotifier notifier;
/*    */   
/*    */   protected EchoNotifierWrapper(IEchoNotifier notifier) {
/* 38 */     this.notifier = (IEchoNotifier)Args.requireNonNull(notifier, "notifier is null");
/*    */   }
/*    */ 
/*    */   
/*    */   public void open() {
/* 43 */     this.notifier.open();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isOpen() {
/* 48 */     return this.notifier.isOpen();
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() {
/* 53 */     this.notifier.close();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isVisible() {
/* 58 */     return this.notifier.isVisible();
/*    */   }
/*    */ 
/*    */   
/*    */   public void show() {
/* 63 */     this.notifier.show();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/echo/imp/EchoNotifierWrapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */