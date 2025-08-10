/*     */ package io.reactivex.internal.operators.observable;
/*     */ 
/*     */ import io.reactivex.Observable;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.Scheduler;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.functions.Consumer;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.disposables.ResettableConnectable;
/*     */ import io.reactivex.internal.disposables.SequentialDisposable;
/*     */ import io.reactivex.observables.ConnectableObservable;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.concurrent.TimeUnit;
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
/*     */ public final class ObservableRefCount<T>
/*     */   extends Observable<T>
/*     */ {
/*     */   final ConnectableObservable<T> source;
/*     */   final int n;
/*     */   final long timeout;
/*     */   final TimeUnit unit;
/*     */   final Scheduler scheduler;
/*     */   RefConnection connection;
/*     */   
/*     */   public ObservableRefCount(ConnectableObservable<T> source) {
/*  48 */     this(source, 1, 0L, TimeUnit.NANOSECONDS, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public ObservableRefCount(ConnectableObservable<T> source, int n, long timeout, TimeUnit unit, Scheduler scheduler) {
/*  53 */     this.source = source;
/*  54 */     this.n = n;
/*  55 */     this.timeout = timeout;
/*  56 */     this.unit = unit;
/*  57 */     this.scheduler = scheduler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Observer<? super T> observer) {
/*     */     RefConnection conn;
/*  65 */     boolean connect = false;
/*  66 */     synchronized (this) {
/*  67 */       conn = this.connection;
/*  68 */       if (conn == null) {
/*  69 */         conn = new RefConnection(this);
/*  70 */         this.connection = conn;
/*     */       } 
/*     */       
/*  73 */       long c = conn.subscriberCount;
/*  74 */       if (c == 0L && conn.timer != null) {
/*  75 */         conn.timer.dispose();
/*     */       }
/*  77 */       conn.subscriberCount = c + 1L;
/*  78 */       if (!conn.connected && c + 1L == this.n) {
/*  79 */         connect = true;
/*  80 */         conn.connected = true;
/*     */       } 
/*     */     } 
/*     */     
/*  84 */     this.source.subscribe(new RefCountObserver<T>(observer, this, conn));
/*     */     
/*  86 */     if (connect) {
/*  87 */       this.source.connect(conn);
/*     */     }
/*     */   }
/*     */   
/*     */   void cancel(RefConnection rc) {
/*     */     SequentialDisposable sd;
/*  93 */     synchronized (this) {
/*  94 */       if (this.connection == null || this.connection != rc) {
/*     */         return;
/*     */       }
/*  97 */       long c = rc.subscriberCount - 1L;
/*  98 */       rc.subscriberCount = c;
/*  99 */       if (c != 0L || !rc.connected) {
/*     */         return;
/*     */       }
/* 102 */       if (this.timeout == 0L) {
/* 103 */         timeout(rc);
/*     */         return;
/*     */       } 
/* 106 */       sd = new SequentialDisposable();
/* 107 */       rc.timer = (Disposable)sd;
/*     */     } 
/*     */     
/* 110 */     sd.replace(this.scheduler.scheduleDirect(rc, this.timeout, this.unit));
/*     */   }
/*     */   
/*     */   void terminated(RefConnection rc) {
/* 114 */     synchronized (this) {
/* 115 */       if (this.source instanceof ObservablePublishClassic) {
/* 116 */         if (this.connection != null && this.connection == rc) {
/* 117 */           this.connection = null;
/* 118 */           clearTimer(rc);
/*     */         } 
/*     */         
/* 121 */         if (--rc.subscriberCount == 0L) {
/* 122 */           reset(rc);
/*     */         }
/*     */       } else {
/*     */         
/* 126 */         clearTimer(rc);
/* 127 */         if (this.connection != null && this.connection == rc && --rc.subscriberCount == 0L) {
/* 128 */           this.connection = null;
/* 129 */           reset(rc);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   void clearTimer(RefConnection rc) {
/* 137 */     if (rc.timer != null) {
/* 138 */       rc.timer.dispose();
/* 139 */       rc.timer = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   void reset(RefConnection rc) {
/* 144 */     if (this.source instanceof Disposable) {
/* 145 */       ((Disposable)this.source).dispose();
/* 146 */     } else if (this.source instanceof ResettableConnectable) {
/* 147 */       ((ResettableConnectable)this.source).resetIf(rc.get());
/*     */     } 
/*     */   }
/*     */   
/*     */   void timeout(RefConnection rc) {
/* 152 */     synchronized (this) {
/* 153 */       if (rc.subscriberCount == 0L && rc == this.connection) {
/* 154 */         this.connection = null;
/* 155 */         Disposable connectionObject = rc.get();
/* 156 */         DisposableHelper.dispose(rc);
/*     */         
/* 158 */         if (this.source instanceof Disposable) {
/* 159 */           ((Disposable)this.source).dispose();
/* 160 */         } else if (this.source instanceof ResettableConnectable) {
/* 161 */           if (connectionObject == null) {
/* 162 */             rc.disconnectedEarly = true;
/*     */           } else {
/* 164 */             ((ResettableConnectable)this.source).resetIf(connectionObject);
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
/*     */     final ObservableRefCount<?> parent;
/*     */     
/*     */     Disposable timer;
/*     */     
/*     */     long subscriberCount;
/*     */     boolean connected;
/*     */     boolean disconnectedEarly;
/*     */     
/*     */     RefConnection(ObservableRefCount<?> parent) {
/* 187 */       this.parent = parent;
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/* 192 */       this.parent.timeout(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public void accept(Disposable t) throws Exception {
/* 197 */       DisposableHelper.replace(this, t);
/* 198 */       synchronized (this.parent) {
/* 199 */         if (this.disconnectedEarly) {
/* 200 */           ((ResettableConnectable)this.parent.source).resetIf(t);
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class RefCountObserver<T>
/*     */     extends AtomicBoolean
/*     */     implements Observer<T>, Disposable
/*     */   {
/*     */     private static final long serialVersionUID = -7419642935409022375L;
/*     */     
/*     */     final Observer<? super T> downstream;
/*     */     
/*     */     final ObservableRefCount<T> parent;
/*     */     final ObservableRefCount.RefConnection connection;
/*     */     Disposable upstream;
/*     */     
/*     */     RefCountObserver(Observer<? super T> downstream, ObservableRefCount<T> parent, ObservableRefCount.RefConnection connection) {
/* 220 */       this.downstream = downstream;
/* 221 */       this.parent = parent;
/* 222 */       this.connection = connection;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 227 */       this.downstream.onNext(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 232 */       if (compareAndSet(false, true)) {
/* 233 */         this.parent.terminated(this.connection);
/* 234 */         this.downstream.onError(t);
/*     */       } else {
/* 236 */         RxJavaPlugins.onError(t);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 242 */       if (compareAndSet(false, true)) {
/* 243 */         this.parent.terminated(this.connection);
/* 244 */         this.downstream.onComplete();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 250 */       this.upstream.dispose();
/* 251 */       if (compareAndSet(false, true)) {
/* 252 */         this.parent.cancel(this.connection);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 258 */       return this.upstream.isDisposed();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/* 263 */       if (DisposableHelper.validate(this.upstream, d)) {
/* 264 */         this.upstream = d;
/*     */         
/* 266 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableRefCount.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */