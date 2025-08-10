/*     */ package io.reactivex.internal.operators.single;
/*     */ 
/*     */ import io.reactivex.Single;
/*     */ import io.reactivex.SingleObserver;
/*     */ import io.reactivex.SingleSource;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
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
/*     */ public final class SingleCache<T>
/*     */   extends Single<T>
/*     */   implements SingleObserver<T>
/*     */ {
/*  24 */   static final CacheDisposable[] EMPTY = new CacheDisposable[0];
/*     */   
/*  26 */   static final CacheDisposable[] TERMINATED = new CacheDisposable[0];
/*     */   
/*     */   final SingleSource<? extends T> source;
/*     */   
/*     */   final AtomicInteger wip;
/*     */   
/*     */   final AtomicReference<CacheDisposable<T>[]> observers;
/*     */   
/*     */   T value;
/*     */   
/*     */   Throwable error;
/*     */ 
/*     */   
/*     */   public SingleCache(SingleSource<? extends T> source) {
/*  40 */     this.source = source;
/*  41 */     this.wip = new AtomicInteger();
/*  42 */     this.observers = new AtomicReference(EMPTY);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(SingleObserver<? super T> observer) {
/*  47 */     CacheDisposable<T> d = new CacheDisposable<T>(observer, this);
/*  48 */     observer.onSubscribe(d);
/*     */     
/*  50 */     if (add(d)) {
/*  51 */       if (d.isDisposed()) {
/*  52 */         remove(d);
/*     */       }
/*     */     } else {
/*  55 */       Throwable ex = this.error;
/*  56 */       if (ex != null) {
/*  57 */         observer.onError(ex);
/*     */       } else {
/*  59 */         observer.onSuccess(this.value);
/*     */       } 
/*     */       
/*     */       return;
/*     */     } 
/*  64 */     if (this.wip.getAndIncrement() == 0) {
/*  65 */       this.source.subscribe(this);
/*     */     }
/*     */   }
/*     */   
/*     */   boolean add(CacheDisposable<T> observer) {
/*     */     while (true) {
/*  71 */       CacheDisposable[] arrayOfCacheDisposable1 = (CacheDisposable[])this.observers.get();
/*  72 */       if (arrayOfCacheDisposable1 == TERMINATED) {
/*  73 */         return false;
/*     */       }
/*  75 */       int n = arrayOfCacheDisposable1.length;
/*     */       
/*  77 */       CacheDisposable[] arrayOfCacheDisposable2 = new CacheDisposable[n + 1];
/*  78 */       System.arraycopy(arrayOfCacheDisposable1, 0, arrayOfCacheDisposable2, 0, n);
/*  79 */       arrayOfCacheDisposable2[n] = observer;
/*  80 */       if (this.observers.compareAndSet(arrayOfCacheDisposable1, arrayOfCacheDisposable2))
/*  81 */         return true; 
/*     */     } 
/*     */   }
/*     */   
/*     */   void remove(CacheDisposable<T> observer) {
/*     */     CacheDisposable[] arrayOfCacheDisposable1;
/*     */     CacheDisposable[] arrayOfCacheDisposable2;
/*     */     do {
/*  89 */       arrayOfCacheDisposable1 = (CacheDisposable[])this.observers.get();
/*  90 */       int n = arrayOfCacheDisposable1.length;
/*  91 */       if (n == 0) {
/*     */         return;
/*     */       }
/*     */       
/*  95 */       int j = -1;
/*  96 */       for (int i = 0; i < n; i++) {
/*  97 */         if (arrayOfCacheDisposable1[i] == observer) {
/*  98 */           j = i;
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/* 103 */       if (j < 0) {
/*     */         return;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 109 */       if (n == 1) {
/* 110 */         arrayOfCacheDisposable2 = EMPTY;
/*     */       } else {
/* 112 */         arrayOfCacheDisposable2 = new CacheDisposable[n - 1];
/* 113 */         System.arraycopy(arrayOfCacheDisposable1, 0, arrayOfCacheDisposable2, 0, j);
/* 114 */         System.arraycopy(arrayOfCacheDisposable1, j + 1, arrayOfCacheDisposable2, j, n - j - 1);
/*     */       } 
/* 116 */     } while (!this.observers.compareAndSet(arrayOfCacheDisposable1, arrayOfCacheDisposable2));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onSubscribe(Disposable d) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onSuccess(T value) {
/* 130 */     this.value = value;
/*     */     
/* 132 */     for (CacheDisposable<T> d : (CacheDisposable[])this.observers.getAndSet(TERMINATED)) {
/* 133 */       if (!d.isDisposed()) {
/* 134 */         d.downstream.onSuccess(value);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onError(Throwable e) {
/* 142 */     this.error = e;
/*     */     
/* 144 */     for (CacheDisposable<T> d : (CacheDisposable[])this.observers.getAndSet(TERMINATED)) {
/* 145 */       if (!d.isDisposed()) {
/* 146 */         d.downstream.onError(e);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static final class CacheDisposable<T>
/*     */     extends AtomicBoolean
/*     */     implements Disposable
/*     */   {
/*     */     private static final long serialVersionUID = 7514387411091976596L;
/*     */     
/*     */     final SingleObserver<? super T> downstream;
/*     */     final SingleCache<T> parent;
/*     */     
/*     */     CacheDisposable(SingleObserver<? super T> actual, SingleCache<T> parent) {
/* 162 */       this.downstream = actual;
/* 163 */       this.parent = parent;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 168 */       return get();
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 173 */       if (compareAndSet(false, true))
/* 174 */         this.parent.remove(this); 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/single/SingleCache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */