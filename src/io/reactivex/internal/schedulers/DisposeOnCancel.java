/*    */ package io.reactivex.internal.schedulers;
/*    */ 
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import java.util.concurrent.ExecutionException;
/*    */ import java.util.concurrent.Future;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import java.util.concurrent.TimeoutException;
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
/*    */ final class DisposeOnCancel
/*    */   implements Future<Object>
/*    */ {
/*    */   final Disposable upstream;
/*    */   
/*    */   DisposeOnCancel(Disposable d) {
/* 29 */     this.upstream = d;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean cancel(boolean mayInterruptIfRunning) {
/* 34 */     this.upstream.dispose();
/* 35 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isCancelled() {
/* 40 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isDone() {
/* 45 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public Object get() throws InterruptedException, ExecutionException {
/* 50 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
/* 56 */     return null;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/schedulers/DisposeOnCancel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */