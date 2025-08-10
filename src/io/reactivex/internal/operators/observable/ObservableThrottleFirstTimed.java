/*     */ package io.reactivex.internal.operators.observable;
/*     */ 
/*     */ import io.reactivex.ObservableSource;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.Scheduler;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.observers.SerializedObserver;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.concurrent.TimeUnit;
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
/*     */ public final class ObservableThrottleFirstTimed<T>
/*     */   extends AbstractObservableWithUpstream<T, T>
/*     */ {
/*     */   final long timeout;
/*     */   final TimeUnit unit;
/*     */   final Scheduler scheduler;
/*     */   
/*     */   public ObservableThrottleFirstTimed(ObservableSource<T> source, long timeout, TimeUnit unit, Scheduler scheduler) {
/*  33 */     super(source);
/*  34 */     this.timeout = timeout;
/*  35 */     this.unit = unit;
/*  36 */     this.scheduler = scheduler;
/*     */   }
/*     */ 
/*     */   
/*     */   public void subscribeActual(Observer<? super T> t) {
/*  41 */     this.source.subscribe(new DebounceTimedObserver((Observer<?>)new SerializedObserver(t), this.timeout, this.unit, this.scheduler
/*     */           
/*  43 */           .createWorker()));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class DebounceTimedObserver<T>
/*     */     extends AtomicReference<Disposable>
/*     */     implements Observer<T>, Disposable, Runnable
/*     */   {
/*     */     private static final long serialVersionUID = 786994795061867455L;
/*     */     
/*     */     final Observer<? super T> downstream;
/*     */     
/*     */     final long timeout;
/*     */     final TimeUnit unit;
/*     */     final Scheduler.Worker worker;
/*     */     Disposable upstream;
/*     */     volatile boolean gate;
/*     */     boolean done;
/*     */     
/*     */     DebounceTimedObserver(Observer<? super T> actual, long timeout, TimeUnit unit, Scheduler.Worker worker) {
/*  63 */       this.downstream = actual;
/*  64 */       this.timeout = timeout;
/*  65 */       this.unit = unit;
/*  66 */       this.worker = worker;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  71 */       if (DisposableHelper.validate(this.upstream, d)) {
/*  72 */         this.upstream = d;
/*  73 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*  79 */       if (!this.gate && !this.done) {
/*  80 */         this.gate = true;
/*     */         
/*  82 */         this.downstream.onNext(t);
/*     */         
/*  84 */         Disposable d = get();
/*  85 */         if (d != null) {
/*  86 */           d.dispose();
/*     */         }
/*  88 */         DisposableHelper.replace(this, this.worker.schedule(this, this.timeout, this.unit));
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/*  94 */       this.gate = false;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/*  99 */       if (this.done) {
/* 100 */         RxJavaPlugins.onError(t);
/*     */       } else {
/* 102 */         this.done = true;
/* 103 */         this.downstream.onError(t);
/* 104 */         this.worker.dispose();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 110 */       if (!this.done) {
/* 111 */         this.done = true;
/* 112 */         this.downstream.onComplete();
/* 113 */         this.worker.dispose();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 119 */       this.upstream.dispose();
/* 120 */       this.worker.dispose();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 125 */       return this.worker.isDisposed();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableThrottleFirstTimed.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */