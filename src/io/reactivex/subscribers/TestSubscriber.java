/*     */ package io.reactivex.subscribers;
/*     */ 
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.functions.Consumer;
/*     */ import io.reactivex.internal.fuseable.QueueSubscription;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.internal.util.ExceptionHelper;
/*     */ import io.reactivex.observers.BaseTestConsumer;
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
/*     */ public class TestSubscriber<T>
/*     */   extends BaseTestConsumer<T, TestSubscriber<T>>
/*     */   implements FlowableSubscriber<T>, Subscription, Disposable
/*     */ {
/*     */   private final Subscriber<? super T> downstream;
/*     */   private volatile boolean cancelled;
/*     */   private final AtomicReference<Subscription> upstream;
/*     */   private final AtomicLong missedRequested;
/*     */   private QueueSubscription<T> qs;
/*     */   
/*     */   public static <T> TestSubscriber<T> create() {
/*  63 */     return new TestSubscriber<T>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> TestSubscriber<T> create(long initialRequested) {
/*  73 */     return new TestSubscriber<T>(initialRequested);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> TestSubscriber<T> create(Subscriber<? super T> delegate) {
/*  83 */     return new TestSubscriber<T>(delegate);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TestSubscriber() {
/*  90 */     this((Subscriber<? super T>)EmptySubscriber.INSTANCE, Long.MAX_VALUE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TestSubscriber(long initialRequest) {
/* 100 */     this((Subscriber<? super T>)EmptySubscriber.INSTANCE, initialRequest);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TestSubscriber(Subscriber<? super T> downstream) {
/* 108 */     this(downstream, Long.MAX_VALUE);
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
/*     */   public TestSubscriber(Subscriber<? super T> actual, long initialRequest) {
/* 120 */     if (initialRequest < 0L) {
/* 121 */       throw new IllegalArgumentException("Negative initial request not allowed");
/*     */     }
/* 123 */     this.downstream = actual;
/* 124 */     this.upstream = new AtomicReference<Subscription>();
/* 125 */     this.missedRequested = new AtomicLong(initialRequest);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onSubscribe(Subscription s) {
/* 131 */     this.lastThread = Thread.currentThread();
/*     */     
/* 133 */     if (s == null) {
/* 134 */       this.errors.add(new NullPointerException("onSubscribe received a null Subscription"));
/*     */       return;
/*     */     } 
/* 137 */     if (!this.upstream.compareAndSet(null, s)) {
/* 138 */       s.cancel();
/* 139 */       if (this.upstream.get() != SubscriptionHelper.CANCELLED) {
/* 140 */         this.errors.add(new IllegalStateException("onSubscribe received multiple subscriptions: " + s));
/*     */       }
/*     */       
/*     */       return;
/*     */     } 
/* 145 */     if (this.initialFusionMode != 0 && 
/* 146 */       s instanceof QueueSubscription) {
/* 147 */       this.qs = (QueueSubscription<T>)s;
/*     */       
/* 149 */       int m = this.qs.requestFusion(this.initialFusionMode);
/* 150 */       this.establishedFusionMode = m;
/*     */       
/* 152 */       if (m == 1) {
/* 153 */         this.checkSubscriptionOnce = true;
/* 154 */         this.lastThread = Thread.currentThread();
/*     */         try {
/*     */           T t;
/* 157 */           while ((t = (T)this.qs.poll()) != null) {
/* 158 */             this.values.add(t);
/*     */           }
/* 160 */           this.completions++;
/* 161 */         } catch (Throwable ex) {
/*     */           
/* 163 */           this.errors.add(ex);
/*     */         } 
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/*     */     
/* 170 */     this.downstream.onSubscribe(s);
/*     */     
/* 172 */     long mr = this.missedRequested.getAndSet(0L);
/* 173 */     if (mr != 0L) {
/* 174 */       s.request(mr);
/*     */     }
/*     */     
/* 177 */     onStart();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onStart() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onNext(T t) {
/* 189 */     if (!this.checkSubscriptionOnce) {
/* 190 */       this.checkSubscriptionOnce = true;
/* 191 */       if (this.upstream.get() == null) {
/* 192 */         this.errors.add(new IllegalStateException("onSubscribe not called in proper order"));
/*     */       }
/*     */     } 
/* 195 */     this.lastThread = Thread.currentThread();
/*     */     
/* 197 */     if (this.establishedFusionMode == 2) {
/*     */       try {
/* 199 */         while ((t = (T)this.qs.poll()) != null) {
/* 200 */           this.values.add(t);
/*     */         }
/* 202 */       } catch (Throwable ex) {
/*     */         
/* 204 */         this.errors.add(ex);
/* 205 */         this.qs.cancel();
/*     */       } 
/*     */       
/*     */       return;
/*     */     } 
/* 210 */     this.values.add(t);
/*     */     
/* 212 */     if (t == null) {
/* 213 */       this.errors.add(new NullPointerException("onNext received a null value"));
/*     */     }
/*     */     
/* 216 */     this.downstream.onNext(t);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onError(Throwable t) {
/* 221 */     if (!this.checkSubscriptionOnce) {
/* 222 */       this.checkSubscriptionOnce = true;
/* 223 */       if (this.upstream.get() == null) {
/* 224 */         this.errors.add(new NullPointerException("onSubscribe not called in proper order"));
/*     */       }
/*     */     } 
/*     */     try {
/* 228 */       this.lastThread = Thread.currentThread();
/* 229 */       this.errors.add(t);
/*     */       
/* 231 */       if (t == null) {
/* 232 */         this.errors.add(new IllegalStateException("onError received a null Throwable"));
/*     */       }
/*     */       
/* 235 */       this.downstream.onError(t);
/*     */     } finally {
/* 237 */       this.done.countDown();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onComplete() {
/* 243 */     if (!this.checkSubscriptionOnce) {
/* 244 */       this.checkSubscriptionOnce = true;
/* 245 */       if (this.upstream.get() == null) {
/* 246 */         this.errors.add(new IllegalStateException("onSubscribe not called in proper order"));
/*     */       }
/*     */     } 
/*     */     try {
/* 250 */       this.lastThread = Thread.currentThread();
/* 251 */       this.completions++;
/*     */       
/* 253 */       this.downstream.onComplete();
/*     */     } finally {
/* 255 */       this.done.countDown();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void request(long n) {
/* 261 */     SubscriptionHelper.deferredRequest(this.upstream, this.missedRequested, n);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void cancel() {
/* 266 */     if (!this.cancelled) {
/* 267 */       this.cancelled = true;
/* 268 */       SubscriptionHelper.cancel(this.upstream);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isCancelled() {
/* 277 */     return this.cancelled;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void dispose() {
/* 282 */     cancel();
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isDisposed() {
/* 287 */     return this.cancelled;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean hasSubscription() {
/* 297 */     return (this.upstream.get() != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final TestSubscriber<T> assertSubscribed() {
/* 308 */     if (this.upstream.get() == null) {
/* 309 */       throw fail("Not subscribed!");
/*     */     }
/* 311 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final TestSubscriber<T> assertNotSubscribed() {
/* 320 */     if (this.upstream.get() != null) {
/* 321 */       throw fail("Subscribed!");
/*     */     }
/* 323 */     if (!this.errors.isEmpty()) {
/* 324 */       throw fail("Not subscribed but errors found");
/*     */     }
/* 326 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final TestSubscriber<T> setInitialFusionMode(int mode) {
/* 337 */     this.initialFusionMode = mode;
/* 338 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final TestSubscriber<T> assertFusionMode(int mode) {
/* 349 */     int m = this.establishedFusionMode;
/* 350 */     if (m != mode) {
/* 351 */       if (this.qs != null) {
/* 352 */         throw new AssertionError("Fusion mode different. Expected: " + fusionModeToString(mode) + ", actual: " + 
/* 353 */             fusionModeToString(m));
/*     */       }
/* 355 */       throw fail("Upstream is not fuseable");
/*     */     } 
/*     */     
/* 358 */     return this;
/*     */   }
/*     */   
/*     */   static String fusionModeToString(int mode) {
/* 362 */     switch (mode) { case 0:
/* 363 */         return "NONE";
/* 364 */       case 1: return "SYNC";
/* 365 */       case 2: return "ASYNC"; }
/* 366 */      return "Unknown(" + mode + ")";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final TestSubscriber<T> assertFuseable() {
/* 377 */     if (this.qs == null) {
/* 378 */       throw new AssertionError("Upstream is not fuseable.");
/*     */     }
/* 380 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final TestSubscriber<T> assertNotFuseable() {
/* 390 */     if (this.qs != null) {
/* 391 */       throw new AssertionError("Upstream is fuseable.");
/*     */     }
/* 393 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final TestSubscriber<T> assertOf(Consumer<? super TestSubscriber<T>> check) {
/*     */     try {
/* 403 */       check.accept(this);
/* 404 */     } catch (Throwable ex) {
/* 405 */       throw ExceptionHelper.wrapOrThrow(ex);
/*     */     } 
/* 407 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final TestSubscriber<T> requestMore(long n) {
/* 418 */     request(n);
/* 419 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   enum EmptySubscriber
/*     */     implements FlowableSubscriber<Object>
/*     */   {
/* 426 */     INSTANCE;
/*     */     
/*     */     public void onSubscribe(Subscription s) {}
/*     */     
/*     */     public void onNext(Object t) {}
/*     */     
/*     */     public void onError(Throwable t) {}
/*     */     
/*     */     public void onComplete() {}
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/subscribers/TestSubscriber.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */