/*    */ package com.github.utils4j.imp;
/*    */ 
/*    */ import io.reactivex.disposables.Disposable;
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
/*    */ public class Releaseble
/*    */   implements Disposable
/*    */ {
/*    */   private final Disposable token;
/*    */   
/*    */   public Releaseble(Disposable token) {
/* 37 */     this.token = Args.<Disposable>requireNonNull(token, "token is null");
/*    */   }
/*    */ 
/*    */   
/*    */   public void dispose() {
/* 42 */     this.token.dispose();
/* 43 */     onRelease(this.token.isDisposed());
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onRelease(boolean disposed) {}
/*    */ 
/*    */   
/*    */   public boolean isDisposed() {
/* 51 */     return this.token.isDisposed();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/imp/Releaseble.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */