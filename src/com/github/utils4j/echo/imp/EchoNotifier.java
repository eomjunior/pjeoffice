/*    */ package com.github.utils4j.echo.imp;
/*    */ 
/*    */ import com.github.utils4j.echo.IEchoNotifier;
/*    */ import io.reactivex.Observable;
/*    */ import io.reactivex.subjects.BehaviorSubject;
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
/*    */ 
/*    */ public abstract class EchoNotifier
/*    */   implements IEchoNotifier
/*    */ {
/*    */   protected BehaviorSubject<String> echo;
/*    */   
/*    */   public final boolean isOpen() {
/* 41 */     return (this.echo != null);
/*    */   }
/*    */ 
/*    */   
/*    */   public final void open() {
/* 46 */     if (!isOpen()) {
/* 47 */       doOpen();
/*    */     }
/*    */   }
/*    */   
/*    */   protected final Observable<String> getEcho() {
/* 52 */     return (Observable<String>)this.echo;
/*    */   }
/*    */   
/*    */   protected final void onNext(String message) {
/* 56 */     show();
/* 57 */     this.echo.onNext(message);
/*    */   }
/*    */   
/*    */   protected void doOpen() {
/* 61 */     this.echo = BehaviorSubject.create();
/*    */   }
/*    */ 
/*    */   
/*    */   public final void close() {
/* 66 */     if (isOpen()) {
/* 67 */       doClose();
/*    */     }
/*    */   }
/*    */   
/*    */   protected void doClose() {
/* 72 */     if (this.echo != null) {
/* 73 */       this.echo.onComplete();
/* 74 */       this.echo = null;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public final boolean isVisible() {
/* 80 */     return (isOpen() && isDisplayed());
/*    */   }
/*    */ 
/*    */   
/*    */   public final void show() {
/* 85 */     open();
/* 86 */     if (!isVisible())
/* 87 */       display(); 
/*    */   }
/*    */   
/*    */   protected abstract void display();
/*    */   
/*    */   protected abstract boolean isDisplayed();
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/echo/imp/EchoNotifier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */