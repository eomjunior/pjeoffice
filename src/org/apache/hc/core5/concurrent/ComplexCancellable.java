/*    */ package org.apache.hc.core5.concurrent;
/*    */ 
/*    */ import java.util.concurrent.atomic.AtomicMarkableReference;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class ComplexCancellable
/*    */   implements CancellableDependency
/*    */ {
/* 45 */   private final AtomicMarkableReference<Cancellable> dependencyRef = new AtomicMarkableReference<>(null, false);
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isCancelled() {
/* 50 */     return this.dependencyRef.isMarked();
/*    */   }
/*    */ 
/*    */   
/*    */   public void setDependency(Cancellable dependency) {
/* 55 */     Args.notNull(dependency, "dependency");
/* 56 */     Cancellable actualDependency = this.dependencyRef.getReference();
/* 57 */     if (!this.dependencyRef.compareAndSet(actualDependency, dependency, false, false)) {
/* 58 */       dependency.cancel();
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean cancel() {
/* 64 */     while (!this.dependencyRef.isMarked()) {
/* 65 */       Cancellable actualDependency = this.dependencyRef.getReference();
/* 66 */       if (this.dependencyRef.compareAndSet(actualDependency, actualDependency, false, true)) {
/* 67 */         if (actualDependency != null) {
/* 68 */           actualDependency.cancel();
/*    */         }
/* 70 */         return true;
/*    */       } 
/*    */     } 
/* 73 */     return false;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/concurrent/ComplexCancellable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */