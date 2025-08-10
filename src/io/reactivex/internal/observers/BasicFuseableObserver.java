/*     */ package io.reactivex.internal.observers;
/*     */ 
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.fuseable.QueueDisposable;
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
/*     */ public abstract class BasicFuseableObserver<T, R>
/*     */   implements Observer<T>, QueueDisposable<R>
/*     */ {
/*     */   protected final Observer<? super R> downstream;
/*     */   protected Disposable upstream;
/*     */   protected QueueDisposable<T> qd;
/*     */   protected boolean done;
/*     */   protected int sourceMode;
/*     */   
/*     */   public BasicFuseableObserver(Observer<? super R> downstream) {
/*  50 */     this.downstream = downstream;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void onSubscribe(Disposable d) {
/*  57 */     if (DisposableHelper.validate(this.upstream, d)) {
/*     */       
/*  59 */       this.upstream = d;
/*  60 */       if (d instanceof QueueDisposable) {
/*  61 */         this.qd = (QueueDisposable<T>)d;
/*     */       }
/*     */       
/*  64 */       if (beforeDownstream()) {
/*     */         
/*  66 */         this.downstream.onSubscribe((Disposable)this);
/*     */         
/*  68 */         afterDownstream();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean beforeDownstream() {
/*  79 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void afterDownstream() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onError(Throwable t) {
/*  95 */     if (this.done) {
/*  96 */       RxJavaPlugins.onError(t);
/*     */       return;
/*     */     } 
/*  99 */     this.done = true;
/* 100 */     this.downstream.onError(t);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void fail(Throwable t) {
/* 108 */     Exceptions.throwIfFatal(t);
/* 109 */     this.upstream.dispose();
/* 110 */     onError(t);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onComplete() {
/* 115 */     if (this.done) {
/*     */       return;
/*     */     }
/* 118 */     this.done = true;
/* 119 */     this.downstream.onComplete();
/*     */   }
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
/*     */   protected final int transitiveBoundaryFusion(int mode) {
/* 133 */     QueueDisposable<T> qd = this.qd;
/* 134 */     if (qd != null && (
/* 135 */       mode & 0x4) == 0) {
/* 136 */       int m = qd.requestFusion(mode);
/* 137 */       if (m != 0) {
/* 138 */         this.sourceMode = m;
/*     */       }
/* 140 */       return m;
/*     */     } 
/*     */     
/* 143 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void dispose() {
/* 152 */     this.upstream.dispose();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDisposed() {
/* 157 */     return this.upstream.isDisposed();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 162 */     return this.qd.isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 167 */     this.qd.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean offer(R e) {
/* 176 */     throw new UnsupportedOperationException("Should not be called!");
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean offer(R v1, R v2) {
/* 181 */     throw new UnsupportedOperationException("Should not be called!");
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/observers/BasicFuseableObserver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */