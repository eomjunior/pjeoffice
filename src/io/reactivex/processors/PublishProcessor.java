/*     */ package io.reactivex.processors;
/*     */ 
/*     */ import io.reactivex.annotations.CheckReturnValue;
/*     */ import io.reactivex.annotations.NonNull;
/*     */ import io.reactivex.annotations.Nullable;
/*     */ import io.reactivex.exceptions.MissingBackpressureException;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.internal.util.BackpressureHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.concurrent.atomic.AtomicLong;
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
/*     */ public final class PublishProcessor<T>
/*     */   extends FlowableProcessor<T>
/*     */ {
/* 113 */   static final PublishSubscription[] TERMINATED = new PublishSubscription[0];
/*     */ 
/*     */   
/* 116 */   static final PublishSubscription[] EMPTY = new PublishSubscription[0];
/*     */ 
/*     */ 
/*     */   
/*     */   final AtomicReference<PublishSubscription<T>[]> subscribers;
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
/*     */   public static <T> PublishProcessor<T> create() {
/* 132 */     return new PublishProcessor<T>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   PublishProcessor() {
/* 141 */     this.subscribers = new AtomicReference(EMPTY);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super T> t) {
/* 146 */     PublishSubscription<T> ps = new PublishSubscription<T>(t, this);
/* 147 */     t.onSubscribe(ps);
/* 148 */     if (add(ps)) {
/*     */ 
/*     */       
/* 151 */       if (ps.isCancelled()) {
/* 152 */         remove(ps);
/*     */       }
/*     */     } else {
/* 155 */       Throwable ex = this.error;
/* 156 */       if (ex != null) {
/* 157 */         t.onError(ex);
/*     */       } else {
/* 159 */         t.onComplete();
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
/*     */   boolean add(PublishSubscription<T> ps) {
/*     */     while (true) {
/* 172 */       PublishSubscription[] arrayOfPublishSubscription1 = (PublishSubscription[])this.subscribers.get();
/* 173 */       if (arrayOfPublishSubscription1 == TERMINATED) {
/* 174 */         return false;
/*     */       }
/*     */       
/* 177 */       int n = arrayOfPublishSubscription1.length;
/*     */       
/* 179 */       PublishSubscription[] arrayOfPublishSubscription2 = new PublishSubscription[n + 1];
/* 180 */       System.arraycopy(arrayOfPublishSubscription1, 0, arrayOfPublishSubscription2, 0, n);
/* 181 */       arrayOfPublishSubscription2[n] = ps;
/*     */       
/* 183 */       if (this.subscribers.compareAndSet(arrayOfPublishSubscription1, arrayOfPublishSubscription2)) {
/* 184 */         return true;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void remove(PublishSubscription<T> ps) {
/*     */     PublishSubscription[] arrayOfPublishSubscription1;
/*     */     PublishSubscription[] arrayOfPublishSubscription2;
/*     */     do {
/* 196 */       arrayOfPublishSubscription1 = (PublishSubscription[])this.subscribers.get();
/* 197 */       if (arrayOfPublishSubscription1 == TERMINATED || arrayOfPublishSubscription1 == EMPTY) {
/*     */         return;
/*     */       }
/*     */       
/* 201 */       int n = arrayOfPublishSubscription1.length;
/* 202 */       int j = -1;
/* 203 */       for (int i = 0; i < n; i++) {
/* 204 */         if (arrayOfPublishSubscription1[i] == ps) {
/* 205 */           j = i;
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/* 210 */       if (j < 0) {
/*     */         return;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 216 */       if (n == 1) {
/* 217 */         arrayOfPublishSubscription2 = EMPTY;
/*     */       } else {
/* 219 */         arrayOfPublishSubscription2 = new PublishSubscription[n - 1];
/* 220 */         System.arraycopy(arrayOfPublishSubscription1, 0, arrayOfPublishSubscription2, 0, j);
/* 221 */         System.arraycopy(arrayOfPublishSubscription1, j + 1, arrayOfPublishSubscription2, j, n - j - 1);
/*     */       } 
/* 223 */     } while (!this.subscribers.compareAndSet(arrayOfPublishSubscription1, arrayOfPublishSubscription2));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onSubscribe(Subscription s) {
/* 231 */     if (this.subscribers.get() == TERMINATED) {
/* 232 */       s.cancel();
/*     */       
/*     */       return;
/*     */     } 
/* 236 */     s.request(Long.MAX_VALUE);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onNext(T t) {
/* 241 */     ObjectHelper.requireNonNull(t, "onNext called with null. Null values are generally not allowed in 2.x operators and sources.");
/* 242 */     for (PublishSubscription<T> s : (PublishSubscription[])this.subscribers.get()) {
/* 243 */       s.onNext(t);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onError(Throwable t) {
/* 250 */     ObjectHelper.requireNonNull(t, "onError called with null. Null values are generally not allowed in 2.x operators and sources.");
/* 251 */     if (this.subscribers.get() == TERMINATED) {
/* 252 */       RxJavaPlugins.onError(t);
/*     */       return;
/*     */     } 
/* 255 */     this.error = t;
/*     */     
/* 257 */     for (PublishSubscription<T> s : (PublishSubscription[])this.subscribers.getAndSet(TERMINATED)) {
/* 258 */       s.onError(t);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onComplete() {
/* 265 */     if (this.subscribers.get() == TERMINATED) {
/*     */       return;
/*     */     }
/* 268 */     for (PublishSubscription<T> s : (PublishSubscription[])this.subscribers.getAndSet(TERMINATED)) {
/* 269 */       s.onComplete();
/*     */     }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean offer(T t) {
/* 288 */     if (t == null) {
/* 289 */       onError(new NullPointerException("onNext called with null. Null values are generally not allowed in 2.x operators and sources."));
/* 290 */       return true;
/*     */     } 
/* 292 */     PublishSubscription[] arrayOfPublishSubscription = (PublishSubscription[])this.subscribers.get();
/*     */     
/* 294 */     for (PublishSubscription<T> s : arrayOfPublishSubscription) {
/* 295 */       if (s.isFull()) {
/* 296 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 300 */     for (PublishSubscription<T> s : arrayOfPublishSubscription) {
/* 301 */       s.onNext(t);
/*     */     }
/* 303 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasSubscribers() {
/* 308 */     return (((PublishSubscription[])this.subscribers.get()).length != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Throwable getThrowable() {
/* 314 */     if (this.subscribers.get() == TERMINATED) {
/* 315 */       return this.error;
/*     */     }
/* 317 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasThrowable() {
/* 322 */     return (this.subscribers.get() == TERMINATED && this.error != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasComplete() {
/* 327 */     return (this.subscribers.get() == TERMINATED && this.error == null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final class PublishSubscription<T>
/*     */     extends AtomicLong
/*     */     implements Subscription
/*     */   {
/*     */     private static final long serialVersionUID = 3562861878281475070L;
/*     */ 
/*     */ 
/*     */     
/*     */     final Subscriber<? super T> downstream;
/*     */ 
/*     */ 
/*     */     
/*     */     final PublishProcessor<T> parent;
/*     */ 
/*     */ 
/*     */     
/*     */     PublishSubscription(Subscriber<? super T> actual, PublishProcessor<T> parent) {
/* 350 */       this.downstream = actual;
/* 351 */       this.parent = parent;
/*     */     }
/*     */     
/*     */     public void onNext(T t) {
/* 355 */       long r = get();
/* 356 */       if (r == Long.MIN_VALUE) {
/*     */         return;
/*     */       }
/* 359 */       if (r != 0L) {
/* 360 */         this.downstream.onNext(t);
/* 361 */         BackpressureHelper.producedCancel(this, 1L);
/*     */       } else {
/* 363 */         cancel();
/* 364 */         this.downstream.onError((Throwable)new MissingBackpressureException("Could not emit value due to lack of requests"));
/*     */       } 
/*     */     }
/*     */     
/*     */     public void onError(Throwable t) {
/* 369 */       if (get() != Long.MIN_VALUE) {
/* 370 */         this.downstream.onError(t);
/*     */       } else {
/* 372 */         RxJavaPlugins.onError(t);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void onComplete() {
/* 377 */       if (get() != Long.MIN_VALUE) {
/* 378 */         this.downstream.onComplete();
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/* 384 */       if (SubscriptionHelper.validate(n)) {
/* 385 */         BackpressureHelper.addCancel(this, n);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 391 */       if (getAndSet(Long.MIN_VALUE) != Long.MIN_VALUE) {
/* 392 */         this.parent.remove(this);
/*     */       }
/*     */     }
/*     */     
/*     */     public boolean isCancelled() {
/* 397 */       return (get() == Long.MIN_VALUE);
/*     */     }
/*     */     
/*     */     boolean isFull() {
/* 401 */       return (get() == 0L);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/processors/PublishProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */