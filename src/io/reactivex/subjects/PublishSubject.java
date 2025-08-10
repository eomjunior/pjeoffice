/*     */ package io.reactivex.subjects;
/*     */ 
/*     */ import io.reactivex.Observer;
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
/*     */ public final class PublishSubject<T>
/*     */   extends Subject<T>
/*     */ {
/* 101 */   static final PublishDisposable[] TERMINATED = new PublishDisposable[0];
/*     */ 
/*     */   
/* 104 */   static final PublishDisposable[] EMPTY = new PublishDisposable[0];
/*     */ 
/*     */ 
/*     */   
/*     */   final AtomicReference<PublishDisposable<T>[]> subscribers;
/*     */ 
/*     */ 
/*     */   
/*     */   Throwable error;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckReturnValue
/*     */   @NonNull
/*     */   public static <T> PublishSubject<T> create() {
/* 120 */     return new PublishSubject<T>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   PublishSubject() {
/* 129 */     this.subscribers = new AtomicReference(EMPTY);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Observer<? super T> t) {
/* 134 */     PublishDisposable<T> ps = new PublishDisposable<T>(t, this);
/* 135 */     t.onSubscribe(ps);
/* 136 */     if (add(ps)) {
/*     */ 
/*     */       
/* 139 */       if (ps.isDisposed()) {
/* 140 */         remove(ps);
/*     */       }
/*     */     } else {
/* 143 */       Throwable ex = this.error;
/* 144 */       if (ex != null) {
/* 145 */         t.onError(ex);
/*     */       } else {
/* 147 */         t.onComplete();
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
/*     */   boolean add(PublishDisposable<T> ps) {
/*     */     while (true) {
/* 160 */       PublishDisposable[] arrayOfPublishDisposable1 = (PublishDisposable[])this.subscribers.get();
/* 161 */       if (arrayOfPublishDisposable1 == TERMINATED) {
/* 162 */         return false;
/*     */       }
/*     */       
/* 165 */       int n = arrayOfPublishDisposable1.length;
/*     */       
/* 167 */       PublishDisposable[] arrayOfPublishDisposable2 = new PublishDisposable[n + 1];
/* 168 */       System.arraycopy(arrayOfPublishDisposable1, 0, arrayOfPublishDisposable2, 0, n);
/* 169 */       arrayOfPublishDisposable2[n] = ps;
/*     */       
/* 171 */       if (this.subscribers.compareAndSet(arrayOfPublishDisposable1, arrayOfPublishDisposable2)) {
/* 172 */         return true;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void remove(PublishDisposable<T> ps) {
/*     */     PublishDisposable[] arrayOfPublishDisposable1;
/*     */     PublishDisposable[] arrayOfPublishDisposable2;
/*     */     do {
/* 184 */       arrayOfPublishDisposable1 = (PublishDisposable[])this.subscribers.get();
/* 185 */       if (arrayOfPublishDisposable1 == TERMINATED || arrayOfPublishDisposable1 == EMPTY) {
/*     */         return;
/*     */       }
/*     */       
/* 189 */       int n = arrayOfPublishDisposable1.length;
/* 190 */       int j = -1;
/* 191 */       for (int i = 0; i < n; i++) {
/* 192 */         if (arrayOfPublishDisposable1[i] == ps) {
/* 193 */           j = i;
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/* 198 */       if (j < 0) {
/*     */         return;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 204 */       if (n == 1) {
/* 205 */         arrayOfPublishDisposable2 = EMPTY;
/*     */       } else {
/* 207 */         arrayOfPublishDisposable2 = new PublishDisposable[n - 1];
/* 208 */         System.arraycopy(arrayOfPublishDisposable1, 0, arrayOfPublishDisposable2, 0, j);
/* 209 */         System.arraycopy(arrayOfPublishDisposable1, j + 1, arrayOfPublishDisposable2, j, n - j - 1);
/*     */       } 
/* 211 */     } while (!this.subscribers.compareAndSet(arrayOfPublishDisposable1, arrayOfPublishDisposable2));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onSubscribe(Disposable d) {
/* 219 */     if (this.subscribers.get() == TERMINATED) {
/* 220 */       d.dispose();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onNext(T t) {
/* 226 */     ObjectHelper.requireNonNull(t, "onNext called with null. Null values are generally not allowed in 2.x operators and sources.");
/* 227 */     for (PublishDisposable<T> pd : (PublishDisposable[])this.subscribers.get()) {
/* 228 */       pd.onNext(t);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onError(Throwable t) {
/* 235 */     ObjectHelper.requireNonNull(t, "onError called with null. Null values are generally not allowed in 2.x operators and sources.");
/* 236 */     if (this.subscribers.get() == TERMINATED) {
/* 237 */       RxJavaPlugins.onError(t);
/*     */       return;
/*     */     } 
/* 240 */     this.error = t;
/*     */     
/* 242 */     for (PublishDisposable<T> pd : (PublishDisposable[])this.subscribers.getAndSet(TERMINATED)) {
/* 243 */       pd.onError(t);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onComplete() {
/* 250 */     if (this.subscribers.get() == TERMINATED) {
/*     */       return;
/*     */     }
/* 253 */     for (PublishDisposable<T> pd : (PublishDisposable[])this.subscribers.getAndSet(TERMINATED)) {
/* 254 */       pd.onComplete();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasObservers() {
/* 260 */     return (((PublishDisposable[])this.subscribers.get()).length != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Throwable getThrowable() {
/* 266 */     if (this.subscribers.get() == TERMINATED) {
/* 267 */       return this.error;
/*     */     }
/* 269 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasThrowable() {
/* 274 */     return (this.subscribers.get() == TERMINATED && this.error != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasComplete() {
/* 279 */     return (this.subscribers.get() == TERMINATED && this.error == null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final class PublishDisposable<T>
/*     */     extends AtomicBoolean
/*     */     implements Disposable
/*     */   {
/*     */     private static final long serialVersionUID = 3562861878281475070L;
/*     */ 
/*     */ 
/*     */     
/*     */     final Observer<? super T> downstream;
/*     */ 
/*     */ 
/*     */     
/*     */     final PublishSubject<T> parent;
/*     */ 
/*     */ 
/*     */     
/*     */     PublishDisposable(Observer<? super T> actual, PublishSubject<T> parent) {
/* 302 */       this.downstream = actual;
/* 303 */       this.parent = parent;
/*     */     }
/*     */     
/*     */     public void onNext(T t) {
/* 307 */       if (!get()) {
/* 308 */         this.downstream.onNext(t);
/*     */       }
/*     */     }
/*     */     
/*     */     public void onError(Throwable t) {
/* 313 */       if (get()) {
/* 314 */         RxJavaPlugins.onError(t);
/*     */       } else {
/* 316 */         this.downstream.onError(t);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void onComplete() {
/* 321 */       if (!get()) {
/* 322 */         this.downstream.onComplete();
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 328 */       if (compareAndSet(false, true)) {
/* 329 */         this.parent.remove(this);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 335 */       return get();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/subjects/PublishSubject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */