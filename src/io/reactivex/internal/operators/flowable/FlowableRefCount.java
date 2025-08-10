/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.Scheduler;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.flowables.ConnectableFlowable;
/*     */ import io.reactivex.functions.Consumer;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.disposables.ResettableConnectable;
/*     */ import io.reactivex.internal.disposables.SequentialDisposable;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
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
/*     */ public final class FlowableRefCount<T>
/*     */   extends Flowable<T>
/*     */ {
/*     */   final ConnectableFlowable<T> source;
/*     */   final int n;
/*     */   final long timeout;
/*     */   final TimeUnit unit;
/*     */   final Scheduler scheduler;
/*     */   RefConnection connection;
/*     */   
/*     */   public FlowableRefCount(ConnectableFlowable<T> source) {
/*  51 */     this(source, 1, 0L, TimeUnit.NANOSECONDS, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public FlowableRefCount(ConnectableFlowable<T> source, int n, long timeout, TimeUnit unit, Scheduler scheduler) {
/*  56 */     this.source = source;
/*  57 */     this.n = n;
/*  58 */     this.timeout = timeout;
/*  59 */     this.unit = unit;
/*  60 */     this.scheduler = scheduler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super T> s) {
/*     */     RefConnection conn;
/*  68 */     boolean connect = false;
/*  69 */     synchronized (this) {
/*  70 */       conn = this.connection;
/*  71 */       if (conn == null) {
/*  72 */         conn = new RefConnection(this);
/*  73 */         this.connection = conn;
/*     */       } 
/*     */       
/*  76 */       long c = conn.subscriberCount;
/*  77 */       if (c == 0L && conn.timer != null) {
/*  78 */         conn.timer.dispose();
/*     */       }
/*  80 */       conn.subscriberCount = c + 1L;
/*  81 */       if (!conn.connected && c + 1L == this.n) {
/*  82 */         connect = true;
/*  83 */         conn.connected = true;
/*     */       } 
/*     */     } 
/*     */     
/*  87 */     this.source.subscribe(new RefCountSubscriber<T>(s, this, conn));
/*     */     
/*  89 */     if (connect) {
/*  90 */       this.source.connect(conn);
/*     */     }
/*     */   }
/*     */   
/*     */   void cancel(RefConnection rc) {
/*     */     SequentialDisposable sd;
/*  96 */     synchronized (this) {
/*  97 */       if (this.connection == null || this.connection != rc) {
/*     */         return;
/*     */       }
/* 100 */       long c = rc.subscriberCount - 1L;
/* 101 */       rc.subscriberCount = c;
/* 102 */       if (c != 0L || !rc.connected) {
/*     */         return;
/*     */       }
/* 105 */       if (this.timeout == 0L) {
/* 106 */         timeout(rc);
/*     */         return;
/*     */       } 
/* 109 */       sd = new SequentialDisposable();
/* 110 */       rc.timer = (Disposable)sd;
/*     */     } 
/*     */     
/* 113 */     sd.replace(this.scheduler.scheduleDirect(rc, this.timeout, this.unit));
/*     */   }
/*     */   
/*     */   void terminated(RefConnection rc) {
/* 117 */     synchronized (this) {
/* 118 */       if (this.source instanceof FlowablePublishClassic) {
/* 119 */         if (this.connection != null && this.connection == rc) {
/* 120 */           this.connection = null;
/* 121 */           clearTimer(rc);
/*     */         } 
/*     */         
/* 124 */         if (--rc.subscriberCount == 0L) {
/* 125 */           reset(rc);
/*     */         }
/*     */       } else {
/*     */         
/* 129 */         clearTimer(rc);
/* 130 */         if (this.connection != null && this.connection == rc && --rc.subscriberCount == 0L) {
/* 131 */           this.connection = null;
/* 132 */           reset(rc);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   void clearTimer(RefConnection rc) {
/* 140 */     if (rc.timer != null) {
/* 141 */       rc.timer.dispose();
/* 142 */       rc.timer = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   void reset(RefConnection rc) {
/* 147 */     if (this.source instanceof Disposable) {
/* 148 */       ((Disposable)this.source).dispose();
/* 149 */     } else if (this.source instanceof ResettableConnectable) {
/* 150 */       ((ResettableConnectable)this.source).resetIf(rc.get());
/*     */     } 
/*     */   }
/*     */   
/*     */   void timeout(RefConnection rc) {
/* 155 */     synchronized (this) {
/* 156 */       if (rc.subscriberCount == 0L && rc == this.connection) {
/* 157 */         this.connection = null;
/* 158 */         Disposable connectionObject = rc.get();
/* 159 */         DisposableHelper.dispose(rc);
/* 160 */         if (this.source instanceof Disposable) {
/* 161 */           ((Disposable)this.source).dispose();
/* 162 */         } else if (this.source instanceof ResettableConnectable) {
/* 163 */           if (connectionObject == null) {
/* 164 */             rc.disconnectedEarly = true;
/*     */           } else {
/* 166 */             ((ResettableConnectable)this.source).resetIf(connectionObject);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static final class RefConnection
/*     */     extends AtomicReference<Disposable>
/*     */     implements Runnable, Consumer<Disposable>
/*     */   {
/*     */     private static final long serialVersionUID = -4552101107598366241L;
/*     */     
/*     */     final FlowableRefCount<?> parent;
/*     */     
/*     */     Disposable timer;
/*     */     
/*     */     long subscriberCount;
/*     */     boolean connected;
/*     */     boolean disconnectedEarly;
/*     */     
/*     */     RefConnection(FlowableRefCount<?> parent) {
/* 189 */       this.parent = parent;
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/* 194 */       this.parent.timeout(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public void accept(Disposable t) throws Exception {
/* 199 */       DisposableHelper.replace(this, t);
/* 200 */       synchronized (this.parent) {
/* 201 */         if (this.disconnectedEarly) {
/* 202 */           ((ResettableConnectable)this.parent.source).resetIf(t);
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class RefCountSubscriber<T>
/*     */     extends AtomicBoolean
/*     */     implements FlowableSubscriber<T>, Subscription
/*     */   {
/*     */     private static final long serialVersionUID = -7419642935409022375L;
/*     */     
/*     */     final Subscriber<? super T> downstream;
/*     */     
/*     */     final FlowableRefCount<T> parent;
/*     */     final FlowableRefCount.RefConnection connection;
/*     */     Subscription upstream;
/*     */     
/*     */     RefCountSubscriber(Subscriber<? super T> actual, FlowableRefCount<T> parent, FlowableRefCount.RefConnection connection) {
/* 222 */       this.downstream = actual;
/* 223 */       this.parent = parent;
/* 224 */       this.connection = connection;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 229 */       this.downstream.onNext(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 234 */       if (compareAndSet(false, true)) {
/* 235 */         this.parent.terminated(this.connection);
/* 236 */         this.downstream.onError(t);
/*     */       } else {
/* 238 */         RxJavaPlugins.onError(t);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 244 */       if (compareAndSet(false, true)) {
/* 245 */         this.parent.terminated(this.connection);
/* 246 */         this.downstream.onComplete();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/* 252 */       this.upstream.request(n);
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 257 */       this.upstream.cancel();
/* 258 */       if (compareAndSet(false, true)) {
/* 259 */         this.parent.cancel(this.connection);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/* 265 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/* 266 */         this.upstream = s;
/*     */         
/* 268 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableRefCount.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */