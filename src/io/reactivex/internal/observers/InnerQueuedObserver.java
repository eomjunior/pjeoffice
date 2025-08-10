/*     */ package io.reactivex.internal.observers;
/*     */ 
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.fuseable.QueueDisposable;
/*     */ import io.reactivex.internal.fuseable.SimpleQueue;
/*     */ import io.reactivex.internal.util.QueueDrainHelper;
/*     */ import java.util.concurrent.atomic.AtomicReference;
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
/*     */ public final class InnerQueuedObserver<T>
/*     */   extends AtomicReference<Disposable>
/*     */   implements Observer<T>, Disposable
/*     */ {
/*     */   private static final long serialVersionUID = -5417183359794346637L;
/*     */   final InnerQueuedObserverSupport<T> parent;
/*     */   final int prefetch;
/*     */   SimpleQueue<T> queue;
/*     */   volatile boolean done;
/*     */   int fusionMode;
/*     */   
/*     */   public InnerQueuedObserver(InnerQueuedObserverSupport<T> parent, int prefetch) {
/*  47 */     this.parent = parent;
/*  48 */     this.prefetch = prefetch;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSubscribe(Disposable d) {
/*  53 */     if (DisposableHelper.setOnce(this, d)) {
/*  54 */       if (d instanceof QueueDisposable) {
/*     */         
/*  56 */         QueueDisposable<T> qd = (QueueDisposable<T>)d;
/*     */         
/*  58 */         int m = qd.requestFusion(3);
/*  59 */         if (m == 1) {
/*  60 */           this.fusionMode = m;
/*  61 */           this.queue = (SimpleQueue<T>)qd;
/*  62 */           this.done = true;
/*  63 */           this.parent.innerComplete(this);
/*     */           return;
/*     */         } 
/*  66 */         if (m == 2) {
/*  67 */           this.fusionMode = m;
/*  68 */           this.queue = (SimpleQueue<T>)qd;
/*     */           
/*     */           return;
/*     */         } 
/*     */       } 
/*  73 */       this.queue = QueueDrainHelper.createQueue(-this.prefetch);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onNext(T t) {
/*  79 */     if (this.fusionMode == 0) {
/*  80 */       this.parent.innerNext(this, t);
/*     */     } else {
/*  82 */       this.parent.drain();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onError(Throwable t) {
/*  88 */     this.parent.innerError(this, t);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onComplete() {
/*  93 */     this.parent.innerComplete(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void dispose() {
/*  98 */     DisposableHelper.dispose(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDisposed() {
/* 103 */     return DisposableHelper.isDisposed(get());
/*     */   }
/*     */   
/*     */   public boolean isDone() {
/* 107 */     return this.done;
/*     */   }
/*     */   
/*     */   public void setDone() {
/* 111 */     this.done = true;
/*     */   }
/*     */   
/*     */   public SimpleQueue<T> queue() {
/* 115 */     return this.queue;
/*     */   }
/*     */   
/*     */   public int fusionMode() {
/* 119 */     return this.fusionMode;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/observers/InnerQueuedObserver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */