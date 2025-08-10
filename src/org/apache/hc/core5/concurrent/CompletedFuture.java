/*    */ package org.apache.hc.core5.concurrent;
/*    */ 
/*    */ import java.util.concurrent.Future;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import org.apache.hc.core5.annotation.Contract;
/*    */ import org.apache.hc.core5.annotation.ThreadingBehavior;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Contract(threading = ThreadingBehavior.IMMUTABLE)
/*    */ public class CompletedFuture<T>
/*    */   implements Future<T>, Cancellable
/*    */ {
/*    */   private final T result;
/*    */   
/*    */   public CompletedFuture(T result) {
/* 48 */     this.result = result;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isCancelled() {
/* 53 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isDone() {
/* 58 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public T get() {
/* 63 */     return this.result;
/*    */   }
/*    */ 
/*    */   
/*    */   public T get(long timeout, TimeUnit unit) {
/* 68 */     return this.result;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean cancel(boolean mayInterruptIfRunning) {
/* 73 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean cancel() {
/* 78 */     return false;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/concurrent/CompletedFuture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */