/*     */ package io.reactivex.subjects;
/*     */ 
/*     */ import io.reactivex.Single;
/*     */ import io.reactivex.SingleObserver;
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
/*     */ public final class SingleSubject<T>
/*     */   extends Single<T>
/*     */   implements SingleObserver<T>
/*     */ {
/*     */   final AtomicReference<SingleDisposable<T>[]> observers;
/* 101 */   static final SingleDisposable[] EMPTY = new SingleDisposable[0];
/*     */ 
/*     */   
/* 104 */   static final SingleDisposable[] TERMINATED = new SingleDisposable[0];
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
/*     */   public static <T> SingleSubject<T> create() {
/* 118 */     return new SingleSubject<T>();
/*     */   }
/*     */ 
/*     */   
/*     */   SingleSubject() {
/* 123 */     this.once = new AtomicBoolean();
/* 124 */     this.observers = new AtomicReference(EMPTY);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSubscribe(@NonNull Disposable d) {
/* 129 */     if (this.observers.get() == TERMINATED) {
/* 130 */       d.dispose();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onSuccess(@NonNull T value) {
/* 137 */     ObjectHelper.requireNonNull(value, "onSuccess called with null. Null values are generally not allowed in 2.x operators and sources.");
/* 138 */     if (this.once.compareAndSet(false, true)) {
/* 139 */       this.value = value;
/* 140 */       for (SingleDisposable<T> md : (SingleDisposable[])this.observers.getAndSet(TERMINATED)) {
/* 141 */         md.downstream.onSuccess(value);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onError(@NonNull Throwable e) {
/* 149 */     ObjectHelper.requireNonNull(e, "onError called with null. Null values are generally not allowed in 2.x operators and sources.");
/* 150 */     if (this.once.compareAndSet(false, true)) {
/* 151 */       this.error = e;
/* 152 */       for (SingleDisposable<T> md : (SingleDisposable[])this.observers.getAndSet(TERMINATED)) {
/* 153 */         md.downstream.onError(e);
/*     */       }
/*     */     } else {
/* 156 */       RxJavaPlugins.onError(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(@NonNull SingleObserver<? super T> observer) {
/* 162 */     SingleDisposable<T> md = new SingleDisposable<T>(observer, this);
/* 163 */     observer.onSubscribe(md);
/* 164 */     if (add(md)) {
/* 165 */       if (md.isDisposed()) {
/* 166 */         remove(md);
/*     */       }
/*     */     } else {
/* 169 */       Throwable ex = this.error;
/* 170 */       if (ex != null) {
/* 171 */         observer.onError(ex);
/*     */       } else {
/* 173 */         observer.onSuccess(this.value);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   boolean add(@NonNull SingleDisposable<T> inner) {
/*     */     while (true) {
/* 180 */       SingleDisposable[] arrayOfSingleDisposable1 = (SingleDisposable[])this.observers.get();
/* 181 */       if (arrayOfSingleDisposable1 == TERMINATED) {
/* 182 */         return false;
/*     */       }
/*     */       
/* 185 */       int n = arrayOfSingleDisposable1.length;
/*     */       
/* 187 */       SingleDisposable[] arrayOfSingleDisposable2 = new SingleDisposable[n + 1];
/* 188 */       System.arraycopy(arrayOfSingleDisposable1, 0, arrayOfSingleDisposable2, 0, n);
/* 189 */       arrayOfSingleDisposable2[n] = inner;
/* 190 */       if (this.observers.compareAndSet(arrayOfSingleDisposable1, arrayOfSingleDisposable2))
/* 191 */         return true; 
/*     */     } 
/*     */   }
/*     */   
/*     */   void remove(@NonNull SingleDisposable<T> inner) {
/*     */     SingleDisposable[] arrayOfSingleDisposable1;
/*     */     SingleDisposable[] arrayOfSingleDisposable2;
/*     */     do {
/* 199 */       arrayOfSingleDisposable1 = (SingleDisposable[])this.observers.get();
/* 200 */       int n = arrayOfSingleDisposable1.length;
/* 201 */       if (n == 0) {
/*     */         return;
/*     */       }
/*     */       
/* 205 */       int j = -1;
/*     */       
/* 207 */       for (int i = 0; i < n; i++) {
/* 208 */         if (arrayOfSingleDisposable1[i] == inner) {
/* 209 */           j = i;
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/* 214 */       if (j < 0) {
/*     */         return;
/*     */       }
/*     */       
/* 218 */       if (n == 1) {
/* 219 */         arrayOfSingleDisposable2 = EMPTY;
/*     */       } else {
/* 221 */         arrayOfSingleDisposable2 = new SingleDisposable[n - 1];
/* 222 */         System.arraycopy(arrayOfSingleDisposable1, 0, arrayOfSingleDisposable2, 0, j);
/* 223 */         System.arraycopy(arrayOfSingleDisposable1, j + 1, arrayOfSingleDisposable2, j, n - j - 1);
/*     */       }
/*     */     
/* 226 */     } while (!this.observers.compareAndSet(arrayOfSingleDisposable1, arrayOfSingleDisposable2));
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
/* 238 */     if (this.observers.get() == TERMINATED) {
/* 239 */       return this.value;
/*     */     }
/* 241 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasValue() {
/* 249 */     return (this.observers.get() == TERMINATED && this.value != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Throwable getThrowable() {
/* 258 */     if (this.observers.get() == TERMINATED) {
/* 259 */       return this.error;
/*     */     }
/* 261 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasThrowable() {
/* 269 */     return (this.observers.get() == TERMINATED && this.error != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasObservers() {
/* 277 */     return (((SingleDisposable[])this.observers.get()).length != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int observerCount() {
/* 285 */     return ((SingleDisposable[])this.observers.get()).length;
/*     */   }
/*     */   
/*     */   static final class SingleDisposable<T>
/*     */     extends AtomicReference<SingleSubject<T>>
/*     */     implements Disposable {
/*     */     private static final long serialVersionUID = -7650903191002190468L;
/*     */     final SingleObserver<? super T> downstream;
/*     */     
/*     */     SingleDisposable(SingleObserver<? super T> actual, SingleSubject<T> parent) {
/* 295 */       this.downstream = actual;
/* 296 */       lazySet(parent);
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 301 */       SingleSubject<T> parent = getAndSet(null);
/* 302 */       if (parent != null) {
/* 303 */         parent.remove(this);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 309 */       return (get() == null);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/subjects/SingleSubject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */