/*     */ package io.reactivex.internal.observers;
/*     */ 
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.annotations.Nullable;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DeferredScalarDisposable<T>
/*     */   extends BasicIntQueueDisposable<T>
/*     */ {
/*     */   private static final long serialVersionUID = -5502432239815349361L;
/*     */   protected final Observer<? super T> downstream;
/*     */   protected T value;
/*     */   static final int TERMINATED = 2;
/*     */   static final int DISPOSED = 4;
/*     */   static final int FUSED_EMPTY = 8;
/*     */   static final int FUSED_READY = 16;
/*     */   static final int FUSED_CONSUMED = 32;
/*     */   
/*     */   public DeferredScalarDisposable(Observer<? super T> downstream) {
/*  53 */     this.downstream = downstream;
/*     */   }
/*     */ 
/*     */   
/*     */   public final int requestFusion(int mode) {
/*  58 */     if ((mode & 0x2) != 0) {
/*  59 */       lazySet(8);
/*  60 */       return 2;
/*     */     } 
/*  62 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void complete(T value) {
/*  71 */     int state = get();
/*  72 */     if ((state & 0x36) != 0) {
/*     */       return;
/*     */     }
/*  75 */     Observer<? super T> a = this.downstream;
/*  76 */     if (state == 8) {
/*  77 */       this.value = value;
/*  78 */       lazySet(16);
/*  79 */       a.onNext(null);
/*     */     } else {
/*  81 */       lazySet(2);
/*  82 */       a.onNext(value);
/*     */     } 
/*  84 */     if (get() != 4) {
/*  85 */       a.onComplete();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void error(Throwable t) {
/*  94 */     int state = get();
/*  95 */     if ((state & 0x36) != 0) {
/*  96 */       RxJavaPlugins.onError(t);
/*     */       return;
/*     */     } 
/*  99 */     lazySet(2);
/* 100 */     this.downstream.onError(t);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void complete() {
/* 107 */     int state = get();
/* 108 */     if ((state & 0x36) != 0) {
/*     */       return;
/*     */     }
/* 111 */     lazySet(2);
/* 112 */     this.downstream.onComplete();
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public final T poll() throws Exception {
/* 118 */     if (get() == 16) {
/* 119 */       T v = this.value;
/* 120 */       this.value = null;
/* 121 */       lazySet(32);
/* 122 */       return v;
/*     */     } 
/* 124 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isEmpty() {
/* 129 */     return (get() != 16);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void clear() {
/* 134 */     lazySet(32);
/* 135 */     this.value = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void dispose() {
/* 140 */     set(4);
/* 141 */     this.value = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean tryDispose() {
/* 149 */     return (getAndSet(4) != 4);
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isDisposed() {
/* 154 */     return (get() == 4);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/observers/DeferredScalarDisposable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */