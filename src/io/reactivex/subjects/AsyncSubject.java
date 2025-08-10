/*     */ package io.reactivex.subjects;
/*     */ 
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.annotations.CheckReturnValue;
/*     */ import io.reactivex.annotations.NonNull;
/*     */ import io.reactivex.annotations.Nullable;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.observers.DeferredScalarDisposable;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.Arrays;
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
/*     */ public final class AsyncSubject<T>
/*     */   extends Subject<T>
/*     */ {
/* 114 */   static final AsyncDisposable[] EMPTY = new AsyncDisposable[0];
/*     */ 
/*     */   
/* 117 */   static final AsyncDisposable[] TERMINATED = new AsyncDisposable[0];
/*     */ 
/*     */ 
/*     */   
/*     */   final AtomicReference<AsyncDisposable<T>[]> subscribers;
/*     */ 
/*     */ 
/*     */   
/*     */   Throwable error;
/*     */ 
/*     */   
/*     */   T value;
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckReturnValue
/*     */   @NonNull
/*     */   public static <T> AsyncSubject<T> create() {
/* 135 */     return new AsyncSubject<T>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   AsyncSubject() {
/* 144 */     this.subscribers = new AtomicReference(EMPTY);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSubscribe(Disposable d) {
/* 149 */     if (this.subscribers.get() == TERMINATED) {
/* 150 */       d.dispose();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onNext(T t) {
/* 156 */     ObjectHelper.requireNonNull(t, "onNext called with null. Null values are generally not allowed in 2.x operators and sources.");
/* 157 */     if (this.subscribers.get() == TERMINATED) {
/*     */       return;
/*     */     }
/* 160 */     this.value = t;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onError(Throwable t) {
/* 166 */     ObjectHelper.requireNonNull(t, "onError called with null. Null values are generally not allowed in 2.x operators and sources.");
/* 167 */     if (this.subscribers.get() == TERMINATED) {
/* 168 */       RxJavaPlugins.onError(t);
/*     */       return;
/*     */     } 
/* 171 */     this.value = null;
/* 172 */     this.error = t;
/* 173 */     for (AsyncDisposable<T> as : (AsyncDisposable[])this.subscribers.getAndSet(TERMINATED)) {
/* 174 */       as.onError(t);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onComplete() {
/* 181 */     if (this.subscribers.get() == TERMINATED) {
/*     */       return;
/*     */     }
/* 184 */     T v = this.value;
/* 185 */     AsyncDisposable[] arrayOfAsyncDisposable = (AsyncDisposable[])this.subscribers.getAndSet(TERMINATED);
/* 186 */     if (v == null) {
/* 187 */       for (AsyncDisposable<T> as : arrayOfAsyncDisposable) {
/* 188 */         as.onComplete();
/*     */       }
/*     */     } else {
/* 191 */       for (AsyncDisposable<T> as : arrayOfAsyncDisposable) {
/* 192 */         as.complete(v);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasObservers() {
/* 199 */     return (((AsyncDisposable[])this.subscribers.get()).length != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasThrowable() {
/* 204 */     return (this.subscribers.get() == TERMINATED && this.error != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasComplete() {
/* 209 */     return (this.subscribers.get() == TERMINATED && this.error == null);
/*     */   }
/*     */ 
/*     */   
/*     */   public Throwable getThrowable() {
/* 214 */     return (this.subscribers.get() == TERMINATED) ? this.error : null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Observer<? super T> observer) {
/* 219 */     AsyncDisposable<T> as = new AsyncDisposable<T>(observer, this);
/* 220 */     observer.onSubscribe((Disposable)as);
/* 221 */     if (add(as)) {
/* 222 */       if (as.isDisposed()) {
/* 223 */         remove(as);
/*     */       }
/*     */     } else {
/* 226 */       Throwable ex = this.error;
/* 227 */       if (ex != null) {
/* 228 */         observer.onError(ex);
/*     */       } else {
/* 230 */         T v = this.value;
/* 231 */         if (v != null) {
/* 232 */           as.complete(v);
/*     */         } else {
/* 234 */           as.onComplete();
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean add(AsyncDisposable<T> ps) {
/*     */     while (true) {
/* 248 */       AsyncDisposable[] arrayOfAsyncDisposable1 = (AsyncDisposable[])this.subscribers.get();
/* 249 */       if (arrayOfAsyncDisposable1 == TERMINATED) {
/* 250 */         return false;
/*     */       }
/*     */       
/* 253 */       int n = arrayOfAsyncDisposable1.length;
/*     */       
/* 255 */       AsyncDisposable[] arrayOfAsyncDisposable2 = new AsyncDisposable[n + 1];
/* 256 */       System.arraycopy(arrayOfAsyncDisposable1, 0, arrayOfAsyncDisposable2, 0, n);
/* 257 */       arrayOfAsyncDisposable2[n] = ps;
/*     */       
/* 259 */       if (this.subscribers.compareAndSet(arrayOfAsyncDisposable1, arrayOfAsyncDisposable2)) {
/* 260 */         return true;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void remove(AsyncDisposable<T> ps) {
/*     */     AsyncDisposable[] arrayOfAsyncDisposable1;
/*     */     AsyncDisposable[] arrayOfAsyncDisposable2;
/*     */     do {
/* 272 */       arrayOfAsyncDisposable1 = (AsyncDisposable[])this.subscribers.get();
/* 273 */       int n = arrayOfAsyncDisposable1.length;
/* 274 */       if (n == 0) {
/*     */         return;
/*     */       }
/*     */       
/* 278 */       int j = -1;
/* 279 */       for (int i = 0; i < n; i++) {
/* 280 */         if (arrayOfAsyncDisposable1[i] == ps) {
/* 281 */           j = i;
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/* 286 */       if (j < 0) {
/*     */         return;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 292 */       if (n == 1) {
/* 293 */         arrayOfAsyncDisposable2 = EMPTY;
/*     */       } else {
/* 295 */         arrayOfAsyncDisposable2 = new AsyncDisposable[n - 1];
/* 296 */         System.arraycopy(arrayOfAsyncDisposable1, 0, arrayOfAsyncDisposable2, 0, j);
/* 297 */         System.arraycopy(arrayOfAsyncDisposable1, j + 1, arrayOfAsyncDisposable2, j, n - j - 1);
/*     */       } 
/* 299 */     } while (!this.subscribers.compareAndSet(arrayOfAsyncDisposable1, arrayOfAsyncDisposable2));
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
/*     */   public boolean hasValue() {
/* 311 */     return (this.subscribers.get() == TERMINATED && this.value != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public T getValue() {
/* 321 */     return (this.subscribers.get() == TERMINATED) ? this.value : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public Object[] getValues() {
/* 332 */     T v = getValue();
/* 333 */     (new Object[1])[0] = v; return (v != null) ? new Object[1] : new Object[0];
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
/*     */   @Deprecated
/*     */   public T[] getValues(T[] array) {
/* 347 */     T v = getValue();
/* 348 */     if (v == null) {
/* 349 */       if (array.length != 0) {
/* 350 */         array[0] = null;
/*     */       }
/* 352 */       return array;
/*     */     } 
/* 354 */     if (array.length == 0) {
/* 355 */       array = Arrays.copyOf(array, 1);
/*     */     }
/* 357 */     array[0] = v;
/* 358 */     if (array.length != 1) {
/* 359 */       array[1] = null;
/*     */     }
/* 361 */     return array;
/*     */   }
/*     */   
/*     */   static final class AsyncDisposable<T>
/*     */     extends DeferredScalarDisposable<T> {
/*     */     private static final long serialVersionUID = 5629876084736248016L;
/*     */     final AsyncSubject<T> parent;
/*     */     
/*     */     AsyncDisposable(Observer<? super T> actual, AsyncSubject<T> parent) {
/* 370 */       super(actual);
/* 371 */       this.parent = parent;
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 376 */       if (tryDispose()) {
/* 377 */         this.parent.remove(this);
/*     */       }
/*     */     }
/*     */     
/*     */     void onComplete() {
/* 382 */       if (!isDisposed()) {
/* 383 */         this.downstream.onComplete();
/*     */       }
/*     */     }
/*     */     
/*     */     void onError(Throwable t) {
/* 388 */       if (isDisposed()) {
/* 389 */         RxJavaPlugins.onError(t);
/*     */       } else {
/* 391 */         this.downstream.onError(t);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/subjects/AsyncSubject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */