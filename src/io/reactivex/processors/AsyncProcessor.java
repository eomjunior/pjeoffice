/*     */ package io.reactivex.processors;
/*     */ 
/*     */ import io.reactivex.annotations.CheckReturnValue;
/*     */ import io.reactivex.annotations.NonNull;
/*     */ import io.reactivex.annotations.Nullable;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.subscriptions.DeferredScalarSubscription;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.Arrays;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import org.reactivestreams.Subscriber;
/*     */ import org.reactivestreams.Subscription;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class AsyncProcessor<T>
/*     */   extends FlowableProcessor<T>
/*     */ {
/* 120 */   static final AsyncSubscription[] EMPTY = new AsyncSubscription[0];
/*     */ 
/*     */   
/* 123 */   static final AsyncSubscription[] TERMINATED = new AsyncSubscription[0];
/*     */ 
/*     */ 
/*     */   
/*     */   final AtomicReference<AsyncSubscription<T>[]> subscribers;
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
/*     */   public static <T> AsyncProcessor<T> create() {
/* 141 */     return new AsyncProcessor<T>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   AsyncProcessor() {
/* 150 */     this.subscribers = new AtomicReference(EMPTY);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSubscribe(Subscription s) {
/* 155 */     if (this.subscribers.get() == TERMINATED) {
/* 156 */       s.cancel();
/*     */       
/*     */       return;
/*     */     } 
/* 160 */     s.request(Long.MAX_VALUE);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onNext(T t) {
/* 165 */     ObjectHelper.requireNonNull(t, "onNext called with null. Null values are generally not allowed in 2.x operators and sources.");
/* 166 */     if (this.subscribers.get() == TERMINATED) {
/*     */       return;
/*     */     }
/* 169 */     this.value = t;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onError(Throwable t) {
/* 175 */     ObjectHelper.requireNonNull(t, "onError called with null. Null values are generally not allowed in 2.x operators and sources.");
/* 176 */     if (this.subscribers.get() == TERMINATED) {
/* 177 */       RxJavaPlugins.onError(t);
/*     */       return;
/*     */     } 
/* 180 */     this.value = null;
/* 181 */     this.error = t;
/* 182 */     for (AsyncSubscription<T> as : (AsyncSubscription[])this.subscribers.getAndSet(TERMINATED)) {
/* 183 */       as.onError(t);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onComplete() {
/* 190 */     if (this.subscribers.get() == TERMINATED) {
/*     */       return;
/*     */     }
/* 193 */     T v = this.value;
/* 194 */     AsyncSubscription[] arrayOfAsyncSubscription = (AsyncSubscription[])this.subscribers.getAndSet(TERMINATED);
/* 195 */     if (v == null) {
/* 196 */       for (AsyncSubscription<T> as : arrayOfAsyncSubscription) {
/* 197 */         as.onComplete();
/*     */       }
/*     */     } else {
/* 200 */       for (AsyncSubscription<T> as : arrayOfAsyncSubscription) {
/* 201 */         as.complete(v);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasSubscribers() {
/* 208 */     return (((AsyncSubscription[])this.subscribers.get()).length != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasThrowable() {
/* 213 */     return (this.subscribers.get() == TERMINATED && this.error != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasComplete() {
/* 218 */     return (this.subscribers.get() == TERMINATED && this.error == null);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Throwable getThrowable() {
/* 224 */     return (this.subscribers.get() == TERMINATED) ? this.error : null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super T> s) {
/* 229 */     AsyncSubscription<T> as = new AsyncSubscription<T>(s, this);
/* 230 */     s.onSubscribe((Subscription)as);
/* 231 */     if (add(as)) {
/* 232 */       if (as.isCancelled()) {
/* 233 */         remove(as);
/*     */       }
/*     */     } else {
/* 236 */       Throwable ex = this.error;
/* 237 */       if (ex != null) {
/* 238 */         s.onError(ex);
/*     */       } else {
/* 240 */         T v = this.value;
/* 241 */         if (v != null) {
/* 242 */           as.complete(v);
/*     */         } else {
/* 244 */           as.onComplete();
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
/*     */   boolean add(AsyncSubscription<T> ps) {
/*     */     while (true) {
/* 258 */       AsyncSubscription[] arrayOfAsyncSubscription1 = (AsyncSubscription[])this.subscribers.get();
/* 259 */       if (arrayOfAsyncSubscription1 == TERMINATED) {
/* 260 */         return false;
/*     */       }
/*     */       
/* 263 */       int n = arrayOfAsyncSubscription1.length;
/*     */       
/* 265 */       AsyncSubscription[] arrayOfAsyncSubscription2 = new AsyncSubscription[n + 1];
/* 266 */       System.arraycopy(arrayOfAsyncSubscription1, 0, arrayOfAsyncSubscription2, 0, n);
/* 267 */       arrayOfAsyncSubscription2[n] = ps;
/*     */       
/* 269 */       if (this.subscribers.compareAndSet(arrayOfAsyncSubscription1, arrayOfAsyncSubscription2)) {
/* 270 */         return true;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void remove(AsyncSubscription<T> ps) {
/*     */     AsyncSubscription[] arrayOfAsyncSubscription1;
/*     */     AsyncSubscription[] arrayOfAsyncSubscription2;
/*     */     do {
/* 282 */       arrayOfAsyncSubscription1 = (AsyncSubscription[])this.subscribers.get();
/* 283 */       int n = arrayOfAsyncSubscription1.length;
/* 284 */       if (n == 0) {
/*     */         return;
/*     */       }
/*     */       
/* 288 */       int j = -1;
/* 289 */       for (int i = 0; i < n; i++) {
/* 290 */         if (arrayOfAsyncSubscription1[i] == ps) {
/* 291 */           j = i;
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/* 296 */       if (j < 0) {
/*     */         return;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 302 */       if (n == 1) {
/* 303 */         arrayOfAsyncSubscription2 = EMPTY;
/*     */       } else {
/* 305 */         arrayOfAsyncSubscription2 = new AsyncSubscription[n - 1];
/* 306 */         System.arraycopy(arrayOfAsyncSubscription1, 0, arrayOfAsyncSubscription2, 0, j);
/* 307 */         System.arraycopy(arrayOfAsyncSubscription1, j + 1, arrayOfAsyncSubscription2, j, n - j - 1);
/*     */       } 
/* 309 */     } while (!this.subscribers.compareAndSet(arrayOfAsyncSubscription1, arrayOfAsyncSubscription2));
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
/* 321 */     return (this.subscribers.get() == TERMINATED && this.value != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public T getValue() {
/* 331 */     return (this.subscribers.get() == TERMINATED) ? this.value : null;
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
/* 342 */     T v = getValue();
/* 343 */     (new Object[1])[0] = v; return (v != null) ? new Object[1] : new Object[0];
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
/* 357 */     T v = getValue();
/* 358 */     if (v == null) {
/* 359 */       if (array.length != 0) {
/* 360 */         array[0] = null;
/*     */       }
/* 362 */       return array;
/*     */     } 
/* 364 */     if (array.length == 0) {
/* 365 */       array = Arrays.copyOf(array, 1);
/*     */     }
/* 367 */     array[0] = v;
/* 368 */     if (array.length != 1) {
/* 369 */       array[1] = null;
/*     */     }
/* 371 */     return array;
/*     */   }
/*     */   
/*     */   static final class AsyncSubscription<T>
/*     */     extends DeferredScalarSubscription<T> {
/*     */     private static final long serialVersionUID = 5629876084736248016L;
/*     */     final AsyncProcessor<T> parent;
/*     */     
/*     */     AsyncSubscription(Subscriber<? super T> actual, AsyncProcessor<T> parent) {
/* 380 */       super(actual);
/* 381 */       this.parent = parent;
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 386 */       if (tryCancel()) {
/* 387 */         this.parent.remove(this);
/*     */       }
/*     */     }
/*     */     
/*     */     void onComplete() {
/* 392 */       if (!isCancelled()) {
/* 393 */         this.downstream.onComplete();
/*     */       }
/*     */     }
/*     */     
/*     */     void onError(Throwable t) {
/* 398 */       if (isCancelled()) {
/* 399 */         RxJavaPlugins.onError(t);
/*     */       } else {
/* 401 */         this.downstream.onError(t);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/processors/AsyncProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */