/*     */ package io.reactivex.observers;
/*     */ 
/*     */ import io.reactivex.CompletableObserver;
/*     */ import io.reactivex.MaybeObserver;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.SingleObserver;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.functions.Consumer;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.fuseable.QueueDisposable;
/*     */ import io.reactivex.internal.util.ExceptionHelper;
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
/*     */ public class TestObserver<T>
/*     */   extends BaseTestConsumer<T, TestObserver<T>>
/*     */   implements Observer<T>, Disposable, MaybeObserver<T>, SingleObserver<T>, CompletableObserver
/*     */ {
/*     */   private final Observer<? super T> downstream;
/*  41 */   private final AtomicReference<Disposable> upstream = new AtomicReference<Disposable>();
/*     */ 
/*     */ 
/*     */   
/*     */   private QueueDisposable<T> qd;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> TestObserver<T> create() {
/*  51 */     return new TestObserver<T>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> TestObserver<T> create(Observer<? super T> delegate) {
/*  61 */     return new TestObserver<T>(delegate);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TestObserver() {
/*  68 */     this(EmptyObserver.INSTANCE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TestObserver(Observer<? super T> downstream) {
/*  76 */     this.downstream = downstream;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onSubscribe(Disposable d) {
/*  82 */     this.lastThread = Thread.currentThread();
/*     */     
/*  84 */     if (d == null) {
/*  85 */       this.errors.add(new NullPointerException("onSubscribe received a null Subscription"));
/*     */       return;
/*     */     } 
/*  88 */     if (!this.upstream.compareAndSet(null, d)) {
/*  89 */       d.dispose();
/*  90 */       if (this.upstream.get() != DisposableHelper.DISPOSED) {
/*  91 */         this.errors.add(new IllegalStateException("onSubscribe received multiple subscriptions: " + d));
/*     */       }
/*     */       
/*     */       return;
/*     */     } 
/*  96 */     if (this.initialFusionMode != 0 && 
/*  97 */       d instanceof QueueDisposable) {
/*  98 */       this.qd = (QueueDisposable<T>)d;
/*     */       
/* 100 */       int m = this.qd.requestFusion(this.initialFusionMode);
/* 101 */       this.establishedFusionMode = m;
/*     */       
/* 103 */       if (m == 1) {
/* 104 */         this.checkSubscriptionOnce = true;
/* 105 */         this.lastThread = Thread.currentThread();
/*     */         try {
/*     */           T t;
/* 108 */           while ((t = (T)this.qd.poll()) != null) {
/* 109 */             this.values.add(t);
/*     */           }
/* 111 */           this.completions++;
/*     */           
/* 113 */           this.upstream.lazySet(DisposableHelper.DISPOSED);
/* 114 */         } catch (Throwable ex) {
/*     */           
/* 116 */           this.errors.add(ex);
/*     */         } 
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/*     */     
/* 123 */     this.downstream.onSubscribe(d);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onNext(T t) {
/* 128 */     if (!this.checkSubscriptionOnce) {
/* 129 */       this.checkSubscriptionOnce = true;
/* 130 */       if (this.upstream.get() == null) {
/* 131 */         this.errors.add(new IllegalStateException("onSubscribe not called in proper order"));
/*     */       }
/*     */     } 
/*     */     
/* 135 */     this.lastThread = Thread.currentThread();
/*     */     
/* 137 */     if (this.establishedFusionMode == 2) {
/*     */       try {
/* 139 */         while ((t = (T)this.qd.poll()) != null) {
/* 140 */           this.values.add(t);
/*     */         }
/* 142 */       } catch (Throwable ex) {
/*     */         
/* 144 */         this.errors.add(ex);
/* 145 */         this.qd.dispose();
/*     */       } 
/*     */       
/*     */       return;
/*     */     } 
/* 150 */     this.values.add(t);
/*     */     
/* 152 */     if (t == null) {
/* 153 */       this.errors.add(new NullPointerException("onNext received a null value"));
/*     */     }
/*     */     
/* 156 */     this.downstream.onNext(t);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onError(Throwable t) {
/* 161 */     if (!this.checkSubscriptionOnce) {
/* 162 */       this.checkSubscriptionOnce = true;
/* 163 */       if (this.upstream.get() == null) {
/* 164 */         this.errors.add(new IllegalStateException("onSubscribe not called in proper order"));
/*     */       }
/*     */     } 
/*     */     
/*     */     try {
/* 169 */       this.lastThread = Thread.currentThread();
/* 170 */       if (t == null) {
/* 171 */         this.errors.add(new NullPointerException("onError received a null Throwable"));
/*     */       } else {
/* 173 */         this.errors.add(t);
/*     */       } 
/*     */       
/* 176 */       this.downstream.onError(t);
/*     */     } finally {
/* 178 */       this.done.countDown();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onComplete() {
/* 184 */     if (!this.checkSubscriptionOnce) {
/* 185 */       this.checkSubscriptionOnce = true;
/* 186 */       if (this.upstream.get() == null) {
/* 187 */         this.errors.add(new IllegalStateException("onSubscribe not called in proper order"));
/*     */       }
/*     */     } 
/*     */     
/*     */     try {
/* 192 */       this.lastThread = Thread.currentThread();
/* 193 */       this.completions++;
/*     */       
/* 195 */       this.downstream.onComplete();
/*     */     } finally {
/* 197 */       this.done.countDown();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isCancelled() {
/* 206 */     return isDisposed();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void cancel() {
/* 215 */     dispose();
/*     */   }
/*     */ 
/*     */   
/*     */   public final void dispose() {
/* 220 */     DisposableHelper.dispose(this.upstream);
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isDisposed() {
/* 225 */     return DisposableHelper.isDisposed(this.upstream.get());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean hasSubscription() {
/* 234 */     return (this.upstream.get() != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final TestObserver<T> assertSubscribed() {
/* 243 */     if (this.upstream.get() == null) {
/* 244 */       throw fail("Not subscribed!");
/*     */     }
/* 246 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final TestObserver<T> assertNotSubscribed() {
/* 255 */     if (this.upstream.get() != null) {
/* 256 */       throw fail("Subscribed!");
/*     */     }
/* 258 */     if (!this.errors.isEmpty()) {
/* 259 */       throw fail("Not subscribed but errors found");
/*     */     }
/* 261 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final TestObserver<T> assertOf(Consumer<? super TestObserver<T>> check) {
/*     */     try {
/* 271 */       check.accept(this);
/* 272 */     } catch (Throwable ex) {
/* 273 */       throw ExceptionHelper.wrapOrThrow(ex);
/*     */     } 
/* 275 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final TestObserver<T> setInitialFusionMode(int mode) {
/* 286 */     this.initialFusionMode = mode;
/* 287 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final TestObserver<T> assertFusionMode(int mode) {
/* 298 */     int m = this.establishedFusionMode;
/* 299 */     if (m != mode) {
/* 300 */       if (this.qd != null) {
/* 301 */         throw new AssertionError("Fusion mode different. Expected: " + fusionModeToString(mode) + ", actual: " + 
/* 302 */             fusionModeToString(m));
/*     */       }
/* 304 */       throw fail("Upstream is not fuseable");
/*     */     } 
/*     */     
/* 307 */     return this;
/*     */   }
/*     */   
/*     */   static String fusionModeToString(int mode) {
/* 311 */     switch (mode) { case 0:
/* 312 */         return "NONE";
/* 313 */       case 1: return "SYNC";
/* 314 */       case 2: return "ASYNC"; }
/* 315 */      return "Unknown(" + mode + ")";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final TestObserver<T> assertFuseable() {
/* 326 */     if (this.qd == null) {
/* 327 */       throw new AssertionError("Upstream is not fuseable.");
/*     */     }
/* 329 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final TestObserver<T> assertNotFuseable() {
/* 339 */     if (this.qd != null) {
/* 340 */       throw new AssertionError("Upstream is fuseable.");
/*     */     }
/* 342 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSuccess(T value) {
/* 347 */     onNext(value);
/* 348 */     onComplete();
/*     */   }
/*     */ 
/*     */   
/*     */   enum EmptyObserver
/*     */     implements Observer<Object>
/*     */   {
/* 355 */     INSTANCE;
/*     */     
/*     */     public void onSubscribe(Disposable d) {}
/*     */     
/*     */     public void onNext(Object t) {}
/*     */     
/*     */     public void onError(Throwable t) {}
/*     */     
/*     */     public void onComplete() {}
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/observers/TestObserver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */