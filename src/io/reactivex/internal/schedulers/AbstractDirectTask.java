/*    */ package io.reactivex.internal.schedulers;
/*    */ 
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.internal.functions.Functions;
/*    */ import io.reactivex.schedulers.SchedulerRunnableIntrospection;
/*    */ import java.util.concurrent.Future;
/*    */ import java.util.concurrent.FutureTask;
/*    */ import java.util.concurrent.atomic.AtomicReference;
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
/*    */ abstract class AbstractDirectTask
/*    */   extends AtomicReference<Future<?>>
/*    */   implements Disposable, SchedulerRunnableIntrospection
/*    */ {
/*    */   private static final long serialVersionUID = 1811839108042568751L;
/*    */   protected final Runnable runnable;
/*    */   protected Thread runner;
/* 40 */   protected static final FutureTask<Void> FINISHED = new FutureTask<Void>(Functions.EMPTY_RUNNABLE, null);
/*    */   
/* 42 */   protected static final FutureTask<Void> DISPOSED = new FutureTask<Void>(Functions.EMPTY_RUNNABLE, null);
/*    */   
/*    */   AbstractDirectTask(Runnable runnable) {
/* 45 */     this.runnable = runnable;
/*    */   }
/*    */ 
/*    */   
/*    */   public final void dispose() {
/* 50 */     Future<?> f = get();
/* 51 */     if (f != FINISHED && f != DISPOSED && 
/* 52 */       compareAndSet(f, DISPOSED) && 
/* 53 */       f != null) {
/* 54 */       f.cancel((this.runner != Thread.currentThread()));
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final boolean isDisposed() {
/* 62 */     Future<?> f = get();
/* 63 */     return (f == FINISHED || f == DISPOSED);
/*    */   }
/*    */   public final void setFuture(Future<?> future) {
/*    */     Future<?> f;
/*    */     do {
/* 68 */       f = get();
/* 69 */       if (f == FINISHED) {
/*    */         break;
/*    */       }
/* 72 */       if (f == DISPOSED) {
/* 73 */         future.cancel((this.runner != Thread.currentThread()));
/*    */         break;
/*    */       } 
/* 76 */     } while (!compareAndSet(f, future));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Runnable getWrappedRunnable() {
/* 84 */     return this.runnable;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/schedulers/AbstractDirectTask.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */