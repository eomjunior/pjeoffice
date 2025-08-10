/*     */ package io.reactivex.internal.operators.observable;
/*     */ 
/*     */ import io.reactivex.ObservableSource;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.Scheduler;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.observers.SerializedObserver;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
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
/*     */ public final class ObservableSampleTimed<T>
/*     */   extends AbstractObservableWithUpstream<T, T>
/*     */ {
/*     */   final long period;
/*     */   final TimeUnit unit;
/*     */   final Scheduler scheduler;
/*     */   final boolean emitLast;
/*     */   
/*     */   public ObservableSampleTimed(ObservableSource<T> source, long period, TimeUnit unit, Scheduler scheduler, boolean emitLast) {
/*  32 */     super(source);
/*  33 */     this.period = period;
/*  34 */     this.unit = unit;
/*  35 */     this.scheduler = scheduler;
/*  36 */     this.emitLast = emitLast;
/*     */   }
/*     */ 
/*     */   
/*     */   public void subscribeActual(Observer<? super T> t) {
/*  41 */     SerializedObserver<T> serial = new SerializedObserver(t);
/*  42 */     if (this.emitLast) {
/*  43 */       this.source.subscribe(new SampleTimedEmitLast<T>((Observer<? super T>)serial, this.period, this.unit, this.scheduler));
/*     */     } else {
/*  45 */       this.source.subscribe(new SampleTimedNoLast<T>((Observer<? super T>)serial, this.period, this.unit, this.scheduler));
/*     */     } 
/*     */   }
/*     */   
/*     */   static abstract class SampleTimedObserver<T>
/*     */     extends AtomicReference<T>
/*     */     implements Observer<T>, Disposable, Runnable
/*     */   {
/*     */     private static final long serialVersionUID = -3517602651313910099L;
/*     */     final Observer<? super T> downstream;
/*     */     final long period;
/*     */     final TimeUnit unit;
/*     */     final Scheduler scheduler;
/*  58 */     final AtomicReference<Disposable> timer = new AtomicReference<Disposable>();
/*     */     
/*     */     Disposable upstream;
/*     */     
/*     */     SampleTimedObserver(Observer<? super T> actual, long period, TimeUnit unit, Scheduler scheduler) {
/*  63 */       this.downstream = actual;
/*  64 */       this.period = period;
/*  65 */       this.unit = unit;
/*  66 */       this.scheduler = scheduler;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  71 */       if (DisposableHelper.validate(this.upstream, d)) {
/*  72 */         this.upstream = d;
/*  73 */         this.downstream.onSubscribe(this);
/*     */         
/*  75 */         Disposable task = this.scheduler.schedulePeriodicallyDirect(this, this.period, this.period, this.unit);
/*  76 */         DisposableHelper.replace(this.timer, task);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*  82 */       lazySet(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/*  87 */       cancelTimer();
/*  88 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*  93 */       cancelTimer();
/*  94 */       complete();
/*     */     }
/*     */     
/*     */     void cancelTimer() {
/*  98 */       DisposableHelper.dispose(this.timer);
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 103 */       cancelTimer();
/* 104 */       this.upstream.dispose();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 109 */       return this.upstream.isDisposed();
/*     */     }
/*     */     
/*     */     void emit() {
/* 113 */       T value = getAndSet(null);
/* 114 */       if (value != null)
/* 115 */         this.downstream.onNext(value); 
/*     */     }
/*     */     
/*     */     abstract void complete();
/*     */   }
/*     */   
/*     */   static final class SampleTimedNoLast<T>
/*     */     extends SampleTimedObserver<T>
/*     */   {
/*     */     private static final long serialVersionUID = -7139995637533111443L;
/*     */     
/*     */     SampleTimedNoLast(Observer<? super T> actual, long period, TimeUnit unit, Scheduler scheduler) {
/* 127 */       super(actual, period, unit, scheduler);
/*     */     }
/*     */ 
/*     */     
/*     */     void complete() {
/* 132 */       this.downstream.onComplete();
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/* 137 */       emit();
/*     */     }
/*     */   }
/*     */   
/*     */   static final class SampleTimedEmitLast<T>
/*     */     extends SampleTimedObserver<T>
/*     */   {
/*     */     private static final long serialVersionUID = -7139995637533111443L;
/*     */     final AtomicInteger wip;
/*     */     
/*     */     SampleTimedEmitLast(Observer<? super T> actual, long period, TimeUnit unit, Scheduler scheduler) {
/* 148 */       super(actual, period, unit, scheduler);
/* 149 */       this.wip = new AtomicInteger(1);
/*     */     }
/*     */ 
/*     */     
/*     */     void complete() {
/* 154 */       emit();
/* 155 */       if (this.wip.decrementAndGet() == 0) {
/* 156 */         this.downstream.onComplete();
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/* 162 */       if (this.wip.incrementAndGet() == 2) {
/* 163 */         emit();
/* 164 */         if (this.wip.decrementAndGet() == 0)
/* 165 */           this.downstream.onComplete(); 
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableSampleTimed.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */