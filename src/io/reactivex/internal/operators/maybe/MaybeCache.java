/*     */ package io.reactivex.internal.operators.maybe;
/*     */ 
/*     */ import io.reactivex.Maybe;
/*     */ import io.reactivex.MaybeObserver;
/*     */ import io.reactivex.MaybeSource;
/*     */ import io.reactivex.disposables.Disposable;
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
/*     */ public final class MaybeCache<T>
/*     */   extends Maybe<T>
/*     */   implements MaybeObserver<T>
/*     */ {
/*  29 */   static final CacheDisposable[] EMPTY = new CacheDisposable[0];
/*     */ 
/*     */   
/*  32 */   static final CacheDisposable[] TERMINATED = new CacheDisposable[0];
/*     */   
/*     */   final AtomicReference<MaybeSource<T>> source;
/*     */   
/*     */   final AtomicReference<CacheDisposable<T>[]> observers;
/*     */   
/*     */   T value;
/*     */   
/*     */   Throwable error;
/*     */ 
/*     */   
/*     */   public MaybeCache(MaybeSource<T> source) {
/*  44 */     this.source = new AtomicReference<MaybeSource<T>>(source);
/*  45 */     this.observers = new AtomicReference(EMPTY);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(MaybeObserver<? super T> observer) {
/*  50 */     CacheDisposable<T> parent = new CacheDisposable<T>(observer, this);
/*  51 */     observer.onSubscribe(parent);
/*     */     
/*  53 */     if (add(parent)) {
/*  54 */       if (parent.isDisposed()) {
/*  55 */         remove(parent);
/*     */         return;
/*     */       } 
/*     */     } else {
/*  59 */       if (!parent.isDisposed()) {
/*  60 */         Throwable ex = this.error;
/*  61 */         if (ex != null) {
/*  62 */           observer.onError(ex);
/*     */         } else {
/*  64 */           T v = this.value;
/*  65 */           if (v != null) {
/*  66 */             observer.onSuccess(v);
/*     */           } else {
/*  68 */             observer.onComplete();
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/*     */       return;
/*     */     } 
/*  75 */     MaybeSource<T> src = this.source.getAndSet(null);
/*  76 */     if (src != null) {
/*  77 */       src.subscribe(this);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onSubscribe(Disposable d) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void onSuccess(T value) {
/*  89 */     this.value = value;
/*  90 */     for (CacheDisposable<T> inner : (CacheDisposable[])this.observers.getAndSet(TERMINATED)) {
/*  91 */       if (!inner.isDisposed()) {
/*  92 */         inner.downstream.onSuccess(value);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onError(Throwable e) {
/* 100 */     this.error = e;
/* 101 */     for (CacheDisposable<T> inner : (CacheDisposable[])this.observers.getAndSet(TERMINATED)) {
/* 102 */       if (!inner.isDisposed()) {
/* 103 */         inner.downstream.onError(e);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onComplete() {
/* 111 */     for (CacheDisposable<T> inner : (CacheDisposable[])this.observers.getAndSet(TERMINATED)) {
/* 112 */       if (!inner.isDisposed()) {
/* 113 */         inner.downstream.onComplete();
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   boolean add(CacheDisposable<T> inner) {
/*     */     while (true) {
/* 120 */       CacheDisposable[] arrayOfCacheDisposable1 = (CacheDisposable[])this.observers.get();
/* 121 */       if (arrayOfCacheDisposable1 == TERMINATED) {
/* 122 */         return false;
/*     */       }
/* 124 */       int n = arrayOfCacheDisposable1.length;
/*     */ 
/*     */       
/* 127 */       CacheDisposable[] arrayOfCacheDisposable2 = new CacheDisposable[n + 1];
/* 128 */       System.arraycopy(arrayOfCacheDisposable1, 0, arrayOfCacheDisposable2, 0, n);
/* 129 */       arrayOfCacheDisposable2[n] = inner;
/* 130 */       if (this.observers.compareAndSet(arrayOfCacheDisposable1, arrayOfCacheDisposable2))
/* 131 */         return true; 
/*     */     } 
/*     */   }
/*     */   
/*     */   void remove(CacheDisposable<T> inner) {
/*     */     CacheDisposable[] arrayOfCacheDisposable1;
/*     */     CacheDisposable[] arrayOfCacheDisposable2;
/*     */     do {
/* 139 */       arrayOfCacheDisposable1 = (CacheDisposable[])this.observers.get();
/* 140 */       int n = arrayOfCacheDisposable1.length;
/* 141 */       if (n == 0) {
/*     */         return;
/*     */       }
/*     */       
/* 145 */       int j = -1;
/*     */       
/* 147 */       for (int i = 0; i < n; i++) {
/* 148 */         if (arrayOfCacheDisposable1[i] == inner) {
/* 149 */           j = i;
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/* 154 */       if (j < 0) {
/*     */         return;
/*     */       }
/*     */ 
/*     */       
/* 159 */       if (n == 1) {
/* 160 */         arrayOfCacheDisposable2 = EMPTY;
/*     */       } else {
/* 162 */         arrayOfCacheDisposable2 = new CacheDisposable[n - 1];
/* 163 */         System.arraycopy(arrayOfCacheDisposable1, 0, arrayOfCacheDisposable2, 0, j);
/* 164 */         System.arraycopy(arrayOfCacheDisposable1, j + 1, arrayOfCacheDisposable2, j, n - j - 1);
/*     */       } 
/* 166 */     } while (!this.observers.compareAndSet(arrayOfCacheDisposable1, arrayOfCacheDisposable2));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static final class CacheDisposable<T>
/*     */     extends AtomicReference<MaybeCache<T>>
/*     */     implements Disposable
/*     */   {
/*     */     private static final long serialVersionUID = -5791853038359966195L;
/*     */     
/*     */     final MaybeObserver<? super T> downstream;
/*     */ 
/*     */     
/*     */     CacheDisposable(MaybeObserver<? super T> actual, MaybeCache<T> parent) {
/* 181 */       super(parent);
/* 182 */       this.downstream = actual;
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 187 */       MaybeCache<T> mc = getAndSet(null);
/* 188 */       if (mc != null) {
/* 189 */         mc.remove(this);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 195 */       return (get() == null);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/maybe/MaybeCache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */