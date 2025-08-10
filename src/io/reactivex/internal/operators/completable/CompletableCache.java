/*     */ package io.reactivex.internal.operators.completable;
/*     */ 
/*     */ import io.reactivex.Completable;
/*     */ import io.reactivex.CompletableObserver;
/*     */ import io.reactivex.CompletableSource;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
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
/*     */ public final class CompletableCache
/*     */   extends Completable
/*     */   implements CompletableObserver
/*     */ {
/*  28 */   static final InnerCompletableCache[] EMPTY = new InnerCompletableCache[0];
/*     */   
/*  30 */   static final InnerCompletableCache[] TERMINATED = new InnerCompletableCache[0];
/*     */   
/*     */   final CompletableSource source;
/*     */   
/*     */   final AtomicReference<InnerCompletableCache[]> observers;
/*     */   
/*     */   final AtomicBoolean once;
/*     */   
/*     */   Throwable error;
/*     */   
/*     */   public CompletableCache(CompletableSource source) {
/*  41 */     this.source = source;
/*  42 */     this.observers = (AtomicReference)new AtomicReference<InnerCompletableCache>(EMPTY);
/*  43 */     this.once = new AtomicBoolean();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(CompletableObserver observer) {
/*  48 */     InnerCompletableCache inner = new InnerCompletableCache(observer);
/*  49 */     observer.onSubscribe(inner);
/*     */     
/*  51 */     if (add(inner)) {
/*  52 */       if (inner.isDisposed()) {
/*  53 */         remove(inner);
/*     */       }
/*     */       
/*  56 */       if (this.once.compareAndSet(false, true)) {
/*  57 */         this.source.subscribe(this);
/*     */       }
/*     */     } else {
/*  60 */       Throwable ex = this.error;
/*  61 */       if (ex != null) {
/*  62 */         observer.onError(ex);
/*     */       } else {
/*  64 */         observer.onComplete();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onSubscribe(Disposable d) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void onError(Throwable e) {
/*  76 */     this.error = e;
/*  77 */     for (InnerCompletableCache inner : (InnerCompletableCache[])this.observers.getAndSet(TERMINATED)) {
/*  78 */       if (!inner.get()) {
/*  79 */         inner.downstream.onError(e);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onComplete() {
/*  86 */     for (InnerCompletableCache inner : (InnerCompletableCache[])this.observers.getAndSet(TERMINATED)) {
/*  87 */       if (!inner.get()) {
/*  88 */         inner.downstream.onComplete();
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   boolean add(InnerCompletableCache inner) {
/*     */     while (true) {
/*  95 */       InnerCompletableCache[] a = this.observers.get();
/*  96 */       if (a == TERMINATED) {
/*  97 */         return false;
/*     */       }
/*  99 */       int n = a.length;
/* 100 */       InnerCompletableCache[] b = new InnerCompletableCache[n + 1];
/* 101 */       System.arraycopy(a, 0, b, 0, n);
/* 102 */       b[n] = inner;
/* 103 */       if (this.observers.compareAndSet(a, b))
/* 104 */         return true; 
/*     */     } 
/*     */   }
/*     */   void remove(InnerCompletableCache inner) {
/*     */     InnerCompletableCache[] a;
/*     */     InnerCompletableCache[] b;
/*     */     do {
/* 111 */       a = this.observers.get();
/* 112 */       int n = a.length;
/* 113 */       if (n == 0) {
/*     */         return;
/*     */       }
/*     */       
/* 117 */       int j = -1;
/*     */       
/* 119 */       for (int i = 0; i < n; i++) {
/* 120 */         if (a[i] == inner) {
/* 121 */           j = i;
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/* 126 */       if (j < 0) {
/*     */         return;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 132 */       if (n == 1) {
/* 133 */         b = EMPTY;
/*     */       } else {
/* 135 */         b = new InnerCompletableCache[n - 1];
/* 136 */         System.arraycopy(a, 0, b, 0, j);
/* 137 */         System.arraycopy(a, j + 1, b, j, n - j - 1);
/*     */       }
/*     */     
/* 140 */     } while (!this.observers.compareAndSet(a, b));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   final class InnerCompletableCache
/*     */     extends AtomicBoolean
/*     */     implements Disposable
/*     */   {
/*     */     private static final long serialVersionUID = 8943152917179642732L;
/*     */     
/*     */     final CompletableObserver downstream;
/*     */ 
/*     */     
/*     */     InnerCompletableCache(CompletableObserver downstream) {
/* 155 */       this.downstream = downstream;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 160 */       return get();
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 165 */       if (compareAndSet(false, true))
/* 166 */         CompletableCache.this.remove(this); 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/completable/CompletableCache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */