/*     */ package io.reactivex.internal.schedulers;
/*     */ 
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.internal.disposables.DisposableContainer;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.atomic.AtomicReferenceArray;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ScheduledRunnable
/*     */   extends AtomicReferenceArray<Object>
/*     */   implements Runnable, Callable<Object>, Disposable
/*     */ {
/*     */   private static final long serialVersionUID = -6120223772001106981L;
/*     */   final Runnable actual;
/*  30 */   static final Object PARENT_DISPOSED = new Object();
/*     */   
/*  32 */   static final Object SYNC_DISPOSED = new Object();
/*     */   
/*  34 */   static final Object ASYNC_DISPOSED = new Object();
/*     */   
/*  36 */   static final Object DONE = new Object();
/*     */ 
/*     */   
/*     */   static final int PARENT_INDEX = 0;
/*     */ 
/*     */   
/*     */   static final int FUTURE_INDEX = 1;
/*     */ 
/*     */   
/*     */   static final int THREAD_INDEX = 2;
/*     */ 
/*     */   
/*     */   public ScheduledRunnable(Runnable actual, DisposableContainer parent) {
/*  49 */     super(3);
/*  50 */     this.actual = actual;
/*  51 */     lazySet(0, parent);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object call() {
/*  57 */     run();
/*  58 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void run() {
/*  63 */     lazySet(2, Thread.currentThread());
/*     */     try {
/*     */       try {
/*  66 */         this.actual.run();
/*  67 */       } catch (Throwable e) {
/*     */         
/*  69 */         RxJavaPlugins.onError(e);
/*     */       } 
/*     */     } finally {
/*  72 */       lazySet(2, null);
/*  73 */       Object o = get(0);
/*  74 */       if (o != PARENT_DISPOSED && compareAndSet(0, o, DONE) && o != null) {
/*  75 */         ((DisposableContainer)o).delete(this);
/*     */       }
/*     */       
/*     */       do {
/*  79 */         o = get(1);
/*  80 */       } while (o != SYNC_DISPOSED && o != ASYNC_DISPOSED && !compareAndSet(1, o, DONE));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFuture(Future<?> f) {
/*     */     Object o;
/*     */     do {
/*  89 */       o = get(1);
/*  90 */       if (o == DONE) {
/*     */         return;
/*     */       }
/*  93 */       if (o == SYNC_DISPOSED) {
/*  94 */         f.cancel(false);
/*     */         return;
/*     */       } 
/*  97 */       if (o == ASYNC_DISPOSED) {
/*  98 */         f.cancel(true);
/*     */         return;
/*     */       } 
/* 101 */     } while (!compareAndSet(1, o, f));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void dispose() {
/*     */     while (true) {
/* 110 */       Object o = get(1);
/* 111 */       if (o == DONE || o == SYNC_DISPOSED || o == ASYNC_DISPOSED) {
/*     */         break;
/*     */       }
/* 114 */       boolean async = (get(2) != Thread.currentThread());
/* 115 */       if (compareAndSet(1, o, async ? ASYNC_DISPOSED : SYNC_DISPOSED)) {
/* 116 */         if (o != null) {
/* 117 */           ((Future)o).cancel(async);
/*     */         }
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/*     */     while (true) {
/* 124 */       Object o = get(0);
/* 125 */       if (o == DONE || o == PARENT_DISPOSED || o == null) {
/*     */         return;
/*     */       }
/* 128 */       if (compareAndSet(0, o, PARENT_DISPOSED)) {
/* 129 */         ((DisposableContainer)o).delete(this);
/*     */         return;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDisposed() {
/* 137 */     Object o = get(0);
/* 138 */     return (o == PARENT_DISPOSED || o == DONE);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/schedulers/ScheduledRunnable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */