/*     */ package io.reactivex.subjects;
/*     */ 
/*     */ import io.reactivex.Completable;
/*     */ import io.reactivex.CompletableObserver;
/*     */ import io.reactivex.annotations.CheckReturnValue;
/*     */ import io.reactivex.annotations.NonNull;
/*     */ import io.reactivex.annotations.Nullable;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
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
/*     */ public final class CompletableSubject
/*     */   extends Completable
/*     */   implements CompletableObserver
/*     */ {
/*     */   final AtomicReference<CompletableDisposable[]> observers;
/*  93 */   static final CompletableDisposable[] EMPTY = new CompletableDisposable[0];
/*     */   
/*  95 */   static final CompletableDisposable[] TERMINATED = new CompletableDisposable[0];
/*     */ 
/*     */   
/*     */   final AtomicBoolean once;
/*     */ 
/*     */   
/*     */   Throwable error;
/*     */ 
/*     */   
/*     */   @CheckReturnValue
/*     */   @NonNull
/*     */   public static CompletableSubject create() {
/* 107 */     return new CompletableSubject();
/*     */   }
/*     */   
/*     */   CompletableSubject() {
/* 111 */     this.once = new AtomicBoolean();
/* 112 */     this.observers = (AtomicReference)new AtomicReference<CompletableDisposable>(EMPTY);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSubscribe(Disposable d) {
/* 117 */     if (this.observers.get() == TERMINATED) {
/* 118 */       d.dispose();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onError(Throwable e) {
/* 124 */     ObjectHelper.requireNonNull(e, "onError called with null. Null values are generally not allowed in 2.x operators and sources.");
/* 125 */     if (this.once.compareAndSet(false, true)) {
/* 126 */       this.error = e;
/* 127 */       for (CompletableDisposable md : (CompletableDisposable[])this.observers.getAndSet(TERMINATED)) {
/* 128 */         md.downstream.onError(e);
/*     */       }
/*     */     } else {
/* 131 */       RxJavaPlugins.onError(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onComplete() {
/* 137 */     if (this.once.compareAndSet(false, true)) {
/* 138 */       for (CompletableDisposable md : (CompletableDisposable[])this.observers.getAndSet(TERMINATED)) {
/* 139 */         md.downstream.onComplete();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(CompletableObserver observer) {
/* 146 */     CompletableDisposable md = new CompletableDisposable(observer, this);
/* 147 */     observer.onSubscribe(md);
/* 148 */     if (add(md)) {
/* 149 */       if (md.isDisposed()) {
/* 150 */         remove(md);
/*     */       }
/*     */     } else {
/* 153 */       Throwable ex = this.error;
/* 154 */       if (ex != null) {
/* 155 */         observer.onError(ex);
/*     */       } else {
/* 157 */         observer.onComplete();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   boolean add(CompletableDisposable inner) {
/*     */     while (true) {
/* 164 */       CompletableDisposable[] a = this.observers.get();
/* 165 */       if (a == TERMINATED) {
/* 166 */         return false;
/*     */       }
/*     */       
/* 169 */       int n = a.length;
/*     */       
/* 171 */       CompletableDisposable[] b = new CompletableDisposable[n + 1];
/* 172 */       System.arraycopy(a, 0, b, 0, n);
/* 173 */       b[n] = inner;
/* 174 */       if (this.observers.compareAndSet(a, b))
/* 175 */         return true; 
/*     */     } 
/*     */   }
/*     */   void remove(CompletableDisposable inner) {
/*     */     CompletableDisposable[] a;
/*     */     CompletableDisposable[] b;
/*     */     do {
/* 182 */       a = this.observers.get();
/* 183 */       int n = a.length;
/* 184 */       if (n == 0) {
/*     */         return;
/*     */       }
/*     */       
/* 188 */       int j = -1;
/*     */       
/* 190 */       for (int i = 0; i < n; i++) {
/* 191 */         if (a[i] == inner) {
/* 192 */           j = i;
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/* 197 */       if (j < 0) {
/*     */         return;
/*     */       }
/*     */       
/* 201 */       if (n == 1) {
/* 202 */         b = EMPTY;
/*     */       } else {
/* 204 */         b = new CompletableDisposable[n - 1];
/* 205 */         System.arraycopy(a, 0, b, 0, j);
/* 206 */         System.arraycopy(a, j + 1, b, j, n - j - 1);
/*     */       }
/*     */     
/* 209 */     } while (!this.observers.compareAndSet(a, b));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Throwable getThrowable() {
/* 221 */     if (this.observers.get() == TERMINATED) {
/* 222 */       return this.error;
/*     */     }
/* 224 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasThrowable() {
/* 232 */     return (this.observers.get() == TERMINATED && this.error != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasComplete() {
/* 240 */     return (this.observers.get() == TERMINATED && this.error == null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasObservers() {
/* 248 */     return (((CompletableDisposable[])this.observers.get()).length != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int observerCount() {
/* 256 */     return ((CompletableDisposable[])this.observers.get()).length;
/*     */   }
/*     */   
/*     */   static final class CompletableDisposable
/*     */     extends AtomicReference<CompletableSubject>
/*     */     implements Disposable {
/*     */     private static final long serialVersionUID = -7650903191002190468L;
/*     */     final CompletableObserver downstream;
/*     */     
/*     */     CompletableDisposable(CompletableObserver actual, CompletableSubject parent) {
/* 266 */       this.downstream = actual;
/* 267 */       lazySet(parent);
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 272 */       CompletableSubject parent = getAndSet(null);
/* 273 */       if (parent != null) {
/* 274 */         parent.remove(this);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 280 */       return (get() == null);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/subjects/CompletableSubject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */