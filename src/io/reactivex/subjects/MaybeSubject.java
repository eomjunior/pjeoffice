/*     */ package io.reactivex.subjects;
/*     */ 
/*     */ import io.reactivex.Maybe;
/*     */ import io.reactivex.MaybeObserver;
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
/*     */ public final class MaybeSubject<T>
/*     */   extends Maybe<T>
/*     */   implements MaybeObserver<T>
/*     */ {
/*     */   final AtomicReference<MaybeDisposable<T>[]> observers;
/* 117 */   static final MaybeDisposable[] EMPTY = new MaybeDisposable[0];
/*     */ 
/*     */   
/* 120 */   static final MaybeDisposable[] TERMINATED = new MaybeDisposable[0];
/*     */ 
/*     */   
/*     */   final AtomicBoolean once;
/*     */ 
/*     */   
/*     */   T value;
/*     */   
/*     */   Throwable error;
/*     */ 
/*     */   
/*     */   @CheckReturnValue
/*     */   @NonNull
/*     */   public static <T> MaybeSubject<T> create() {
/* 134 */     return new MaybeSubject<T>();
/*     */   }
/*     */ 
/*     */   
/*     */   MaybeSubject() {
/* 139 */     this.once = new AtomicBoolean();
/* 140 */     this.observers = new AtomicReference(EMPTY);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSubscribe(Disposable d) {
/* 145 */     if (this.observers.get() == TERMINATED) {
/* 146 */       d.dispose();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onSuccess(T value) {
/* 153 */     ObjectHelper.requireNonNull(value, "onSuccess called with null. Null values are generally not allowed in 2.x operators and sources.");
/* 154 */     if (this.once.compareAndSet(false, true)) {
/* 155 */       this.value = value;
/* 156 */       for (MaybeDisposable<T> md : (MaybeDisposable[])this.observers.getAndSet(TERMINATED)) {
/* 157 */         md.downstream.onSuccess(value);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onError(Throwable e) {
/* 165 */     ObjectHelper.requireNonNull(e, "onError called with null. Null values are generally not allowed in 2.x operators and sources.");
/* 166 */     if (this.once.compareAndSet(false, true)) {
/* 167 */       this.error = e;
/* 168 */       for (MaybeDisposable<T> md : (MaybeDisposable[])this.observers.getAndSet(TERMINATED)) {
/* 169 */         md.downstream.onError(e);
/*     */       }
/*     */     } else {
/* 172 */       RxJavaPlugins.onError(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onComplete() {
/* 179 */     if (this.once.compareAndSet(false, true)) {
/* 180 */       for (MaybeDisposable<T> md : (MaybeDisposable[])this.observers.getAndSet(TERMINATED)) {
/* 181 */         md.downstream.onComplete();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(MaybeObserver<? super T> observer) {
/* 188 */     MaybeDisposable<T> md = new MaybeDisposable<T>(observer, this);
/* 189 */     observer.onSubscribe(md);
/* 190 */     if (add(md)) {
/* 191 */       if (md.isDisposed()) {
/* 192 */         remove(md);
/*     */       }
/*     */     } else {
/* 195 */       Throwable ex = this.error;
/* 196 */       if (ex != null) {
/* 197 */         observer.onError(ex);
/*     */       } else {
/* 199 */         T v = this.value;
/* 200 */         if (v == null) {
/* 201 */           observer.onComplete();
/*     */         } else {
/* 203 */           observer.onSuccess(v);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   boolean add(MaybeDisposable<T> inner) {
/*     */     while (true) {
/* 211 */       MaybeDisposable[] arrayOfMaybeDisposable1 = (MaybeDisposable[])this.observers.get();
/* 212 */       if (arrayOfMaybeDisposable1 == TERMINATED) {
/* 213 */         return false;
/*     */       }
/*     */       
/* 216 */       int n = arrayOfMaybeDisposable1.length;
/*     */       
/* 218 */       MaybeDisposable[] arrayOfMaybeDisposable2 = new MaybeDisposable[n + 1];
/* 219 */       System.arraycopy(arrayOfMaybeDisposable1, 0, arrayOfMaybeDisposable2, 0, n);
/* 220 */       arrayOfMaybeDisposable2[n] = inner;
/* 221 */       if (this.observers.compareAndSet(arrayOfMaybeDisposable1, arrayOfMaybeDisposable2))
/* 222 */         return true; 
/*     */     } 
/*     */   }
/*     */   
/*     */   void remove(MaybeDisposable<T> inner) {
/*     */     MaybeDisposable[] arrayOfMaybeDisposable1;
/*     */     MaybeDisposable[] arrayOfMaybeDisposable2;
/*     */     do {
/* 230 */       arrayOfMaybeDisposable1 = (MaybeDisposable[])this.observers.get();
/* 231 */       int n = arrayOfMaybeDisposable1.length;
/* 232 */       if (n == 0) {
/*     */         return;
/*     */       }
/*     */       
/* 236 */       int j = -1;
/*     */       
/* 238 */       for (int i = 0; i < n; i++) {
/* 239 */         if (arrayOfMaybeDisposable1[i] == inner) {
/* 240 */           j = i;
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/* 245 */       if (j < 0) {
/*     */         return;
/*     */       }
/*     */       
/* 249 */       if (n == 1) {
/* 250 */         arrayOfMaybeDisposable2 = EMPTY;
/*     */       } else {
/* 252 */         arrayOfMaybeDisposable2 = new MaybeDisposable[n - 1];
/* 253 */         System.arraycopy(arrayOfMaybeDisposable1, 0, arrayOfMaybeDisposable2, 0, j);
/* 254 */         System.arraycopy(arrayOfMaybeDisposable1, j + 1, arrayOfMaybeDisposable2, j, n - j - 1);
/*     */       }
/*     */     
/* 257 */     } while (!this.observers.compareAndSet(arrayOfMaybeDisposable1, arrayOfMaybeDisposable2));
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
/*     */   public T getValue() {
/* 269 */     if (this.observers.get() == TERMINATED) {
/* 270 */       return this.value;
/*     */     }
/* 272 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasValue() {
/* 280 */     return (this.observers.get() == TERMINATED && this.value != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Throwable getThrowable() {
/* 289 */     if (this.observers.get() == TERMINATED) {
/* 290 */       return this.error;
/*     */     }
/* 292 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasThrowable() {
/* 300 */     return (this.observers.get() == TERMINATED && this.error != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasComplete() {
/* 308 */     return (this.observers.get() == TERMINATED && this.value == null && this.error == null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasObservers() {
/* 316 */     return (((MaybeDisposable[])this.observers.get()).length != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int observerCount() {
/* 324 */     return ((MaybeDisposable[])this.observers.get()).length;
/*     */   }
/*     */   
/*     */   static final class MaybeDisposable<T>
/*     */     extends AtomicReference<MaybeSubject<T>>
/*     */     implements Disposable {
/*     */     private static final long serialVersionUID = -7650903191002190468L;
/*     */     final MaybeObserver<? super T> downstream;
/*     */     
/*     */     MaybeDisposable(MaybeObserver<? super T> actual, MaybeSubject<T> parent) {
/* 334 */       this.downstream = actual;
/* 335 */       lazySet(parent);
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 340 */       MaybeSubject<T> parent = getAndSet(null);
/* 341 */       if (parent != null) {
/* 342 */         parent.remove(this);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 348 */       return (get() == null);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/subjects/MaybeSubject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */