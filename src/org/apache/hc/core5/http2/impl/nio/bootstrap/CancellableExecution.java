/*    */ package org.apache.hc.core5.http2.impl.nio.bootstrap;
/*    */ 
/*    */ import java.util.concurrent.atomic.AtomicBoolean;
/*    */ import java.util.concurrent.atomic.AtomicReference;
/*    */ import org.apache.hc.core5.concurrent.Cancellable;
/*    */ import org.apache.hc.core5.concurrent.CancellableDependency;
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
/*    */ final class CancellableExecution
/*    */   implements CancellableDependency
/*    */ {
/* 41 */   private final AtomicBoolean cancelled = new AtomicBoolean(false);
/* 42 */   private final AtomicReference<Cancellable> dependencyRef = new AtomicReference<>();
/*    */ 
/*    */ 
/*    */   
/*    */   public void setDependency(Cancellable cancellable) {
/* 47 */     this.dependencyRef.set(cancellable);
/* 48 */     if (this.cancelled.get()) {
/* 49 */       Cancellable dependency = this.dependencyRef.getAndSet(null);
/* 50 */       if (dependency != null) {
/* 51 */         dependency.cancel();
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isCancelled() {
/* 58 */     return this.cancelled.get();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean cancel() {
/* 63 */     if (this.cancelled.compareAndSet(false, true)) {
/* 64 */       Cancellable dependency = this.dependencyRef.getAndSet(null);
/* 65 */       if (dependency != null) {
/* 66 */         dependency.cancel();
/*    */       }
/* 68 */       return true;
/*    */     } 
/* 70 */     return false;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http2/impl/nio/bootstrap/CancellableExecution.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */