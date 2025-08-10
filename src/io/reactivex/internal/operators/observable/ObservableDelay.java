/*     */ package io.reactivex.internal.operators.observable;
/*     */ 
/*     */ import io.reactivex.ObservableSource;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.Scheduler;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.observers.SerializedObserver;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ObservableDelay<T>
/*     */   extends AbstractObservableWithUpstream<T, T>
/*     */ {
/*     */   final long delay;
/*     */   final TimeUnit unit;
/*     */   final Scheduler scheduler;
/*     */   final boolean delayError;
/*     */   
/*     */   public ObservableDelay(ObservableSource<T> source, long delay, TimeUnit unit, Scheduler scheduler, boolean delayError) {
/*  31 */     super(source);
/*  32 */     this.delay = delay;
/*  33 */     this.unit = unit;
/*  34 */     this.scheduler = scheduler;
/*  35 */     this.delayError = delayError;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void subscribeActual(Observer<? super T> t) {
/*     */     SerializedObserver serializedObserver;
/*  42 */     if (this.delayError) {
/*  43 */       Observer<? super T> observer = t;
/*     */     } else {
/*  45 */       serializedObserver = new SerializedObserver(t);
/*     */     } 
/*     */     
/*  48 */     Scheduler.Worker w = this.scheduler.createWorker();
/*     */     
/*  50 */     this.source.subscribe(new DelayObserver((Observer<?>)serializedObserver, this.delay, this.unit, w, this.delayError));
/*     */   }
/*     */   
/*     */   static final class DelayObserver<T>
/*     */     implements Observer<T>, Disposable
/*     */   {
/*     */     final Observer<? super T> downstream;
/*     */     final long delay;
/*     */     final TimeUnit unit;
/*     */     final Scheduler.Worker w;
/*     */     final boolean delayError;
/*     */     Disposable upstream;
/*     */     
/*     */     DelayObserver(Observer<? super T> actual, long delay, TimeUnit unit, Scheduler.Worker w, boolean delayError) {
/*  64 */       this.downstream = actual;
/*  65 */       this.delay = delay;
/*  66 */       this.unit = unit;
/*  67 */       this.w = w;
/*  68 */       this.delayError = delayError;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  73 */       if (DisposableHelper.validate(this.upstream, d)) {
/*  74 */         this.upstream = d;
/*  75 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*  81 */       this.w.schedule(new OnNext(t), this.delay, this.unit);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/*  86 */       this.w.schedule(new OnError(t), this.delayError ? this.delay : 0L, this.unit);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*  91 */       this.w.schedule(new OnComplete(), this.delay, this.unit);
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/*  96 */       this.upstream.dispose();
/*  97 */       this.w.dispose();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 102 */       return this.w.isDisposed();
/*     */     }
/*     */     
/*     */     final class OnNext implements Runnable {
/*     */       private final T t;
/*     */       
/*     */       OnNext(T t) {
/* 109 */         this.t = t;
/*     */       }
/*     */ 
/*     */       
/*     */       public void run() {
/* 114 */         ObservableDelay.DelayObserver.this.downstream.onNext(this.t);
/*     */       }
/*     */     }
/*     */     
/*     */     final class OnError implements Runnable {
/*     */       private final Throwable throwable;
/*     */       
/*     */       OnError(Throwable throwable) {
/* 122 */         this.throwable = throwable;
/*     */       }
/*     */ 
/*     */       
/*     */       public void run() {
/*     */         try {
/* 128 */           ObservableDelay.DelayObserver.this.downstream.onError(this.throwable);
/*     */         } finally {
/* 130 */           ObservableDelay.DelayObserver.this.w.dispose();
/*     */         } 
/*     */       }
/*     */     }
/*     */     
/*     */     final class OnComplete
/*     */       implements Runnable {
/*     */       public void run() {
/*     */         try {
/* 139 */           ObservableDelay.DelayObserver.this.downstream.onComplete();
/*     */         } finally {
/* 141 */           ObservableDelay.DelayObserver.this.w.dispose();
/*     */         } 
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableDelay.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */