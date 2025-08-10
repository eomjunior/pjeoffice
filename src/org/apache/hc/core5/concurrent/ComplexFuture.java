/*    */ package org.apache.hc.core5.concurrent;
/*    */ 
/*    */ import java.util.concurrent.Future;
/*    */ import java.util.concurrent.atomic.AtomicReference;
/*    */ import org.apache.hc.core5.util.Args;
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
/*    */ public final class ComplexFuture<T>
/*    */   extends BasicFuture<T>
/*    */   implements CancellableDependency
/*    */ {
/*    */   private final AtomicReference<Cancellable> dependencyRef;
/*    */   
/*    */   public ComplexFuture(FutureCallback<T> callback) {
/* 47 */     super(callback);
/* 48 */     this.dependencyRef = new AtomicReference<>();
/*    */   }
/*    */ 
/*    */   
/*    */   public void setDependency(Cancellable dependency) {
/* 53 */     Args.notNull(dependency, "dependency");
/* 54 */     if (isDone()) {
/* 55 */       dependency.cancel();
/*    */     } else {
/* 57 */       this.dependencyRef.set(dependency);
/*    */     } 
/*    */   }
/*    */   
/*    */   public void setDependency(Future<?> dependency) {
/* 62 */     Args.notNull(dependency, "dependency");
/* 63 */     if (dependency instanceof Cancellable) {
/* 64 */       setDependency((Cancellable)dependency);
/*    */     } else {
/* 66 */       setDependency(() -> dependency.cancel(true));
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean completed(T result) {
/* 72 */     boolean completed = super.completed(result);
/* 73 */     this.dependencyRef.set(null);
/* 74 */     return completed;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean failed(Exception exception) {
/* 79 */     boolean failed = super.failed(exception);
/* 80 */     this.dependencyRef.set(null);
/* 81 */     return failed;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean cancel(boolean mayInterruptIfRunning) {
/* 86 */     boolean cancelled = super.cancel(mayInterruptIfRunning);
/* 87 */     Cancellable dependency = this.dependencyRef.getAndSet(null);
/* 88 */     if (dependency != null) {
/* 89 */       dependency.cancel();
/*    */     }
/* 91 */     return cancelled;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/concurrent/ComplexFuture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */