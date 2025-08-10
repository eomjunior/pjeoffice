/*     */ package io.reactivex.internal.operators.observable;
/*     */ 
/*     */ import io.reactivex.Observable;
/*     */ import io.reactivex.ObservableSource;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.Scheduler;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ObservableThrottleLatest<T>
/*     */   extends AbstractObservableWithUpstream<T, T>
/*     */ {
/*     */   final long timeout;
/*     */   final TimeUnit unit;
/*     */   final Scheduler scheduler;
/*     */   final boolean emitLast;
/*     */   
/*     */   public ObservableThrottleLatest(Observable<T> source, long timeout, TimeUnit unit, Scheduler scheduler, boolean emitLast) {
/*  47 */     super((ObservableSource<T>)source);
/*  48 */     this.timeout = timeout;
/*  49 */     this.unit = unit;
/*  50 */     this.scheduler = scheduler;
/*  51 */     this.emitLast = emitLast;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Observer<? super T> observer) {
/*  56 */     this.source.subscribe(new ThrottleLatestObserver<T>(observer, this.timeout, this.unit, this.scheduler.createWorker(), this.emitLast));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class ThrottleLatestObserver<T>
/*     */     extends AtomicInteger
/*     */     implements Observer<T>, Disposable, Runnable
/*     */   {
/*     */     private static final long serialVersionUID = -8296689127439125014L;
/*     */     
/*     */     final Observer<? super T> downstream;
/*     */     
/*     */     final long timeout;
/*     */     
/*     */     final TimeUnit unit;
/*     */     
/*     */     final Scheduler.Worker worker;
/*     */     
/*     */     final boolean emitLast;
/*     */     
/*     */     final AtomicReference<T> latest;
/*     */     
/*     */     Disposable upstream;
/*     */     
/*     */     volatile boolean done;
/*     */     
/*     */     Throwable error;
/*     */     
/*     */     volatile boolean cancelled;
/*     */     
/*     */     volatile boolean timerFired;
/*     */     
/*     */     boolean timerRunning;
/*     */     
/*     */     ThrottleLatestObserver(Observer<? super T> downstream, long timeout, TimeUnit unit, Scheduler.Worker worker, boolean emitLast) {
/*  91 */       this.downstream = downstream;
/*  92 */       this.timeout = timeout;
/*  93 */       this.unit = unit;
/*  94 */       this.worker = worker;
/*  95 */       this.emitLast = emitLast;
/*  96 */       this.latest = new AtomicReference<T>();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/* 101 */       if (DisposableHelper.validate(this.upstream, d)) {
/* 102 */         this.upstream = d;
/* 103 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 109 */       this.latest.set(t);
/* 110 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 115 */       this.error = t;
/* 116 */       this.done = true;
/* 117 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 122 */       this.done = true;
/* 123 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 128 */       this.cancelled = true;
/* 129 */       this.upstream.dispose();
/* 130 */       this.worker.dispose();
/* 131 */       if (getAndIncrement() == 0) {
/* 132 */         this.latest.lazySet(null);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 138 */       return this.cancelled;
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/* 143 */       this.timerFired = true;
/* 144 */       drain();
/*     */     }
/*     */     
/*     */     void drain() {
/* 148 */       if (getAndIncrement() != 0) {
/*     */         return;
/*     */       }
/*     */       
/* 152 */       int missed = 1;
/*     */       
/* 154 */       AtomicReference<T> latest = this.latest;
/* 155 */       Observer<? super T> downstream = this.downstream;
/*     */ 
/*     */ 
/*     */       
/*     */       while (true) {
/* 160 */         if (this.cancelled) {
/* 161 */           latest.lazySet(null);
/*     */           
/*     */           return;
/*     */         } 
/* 165 */         boolean d = this.done;
/*     */         
/* 167 */         if (d && this.error != null) {
/* 168 */           latest.lazySet(null);
/* 169 */           downstream.onError(this.error);
/* 170 */           this.worker.dispose();
/*     */           
/*     */           return;
/*     */         } 
/* 174 */         T v = latest.get();
/* 175 */         boolean empty = (v == null);
/*     */         
/* 177 */         if (d) {
/* 178 */           v = latest.getAndSet(null);
/* 179 */           if (!empty && this.emitLast) {
/* 180 */             downstream.onNext(v);
/*     */           }
/* 182 */           downstream.onComplete();
/* 183 */           this.worker.dispose();
/*     */           
/*     */           return;
/*     */         } 
/* 187 */         if (empty) {
/* 188 */           if (this.timerFired) {
/* 189 */             this.timerRunning = false;
/* 190 */             this.timerFired = false;
/*     */           
/*     */           }
/*     */         
/*     */         }
/* 195 */         else if (!this.timerRunning || this.timerFired) {
/* 196 */           v = latest.getAndSet(null);
/* 197 */           downstream.onNext(v);
/*     */           
/* 199 */           this.timerFired = false;
/* 200 */           this.timerRunning = true;
/* 201 */           this.worker.schedule(this, this.timeout, this.unit);
/*     */ 
/*     */           
/*     */           continue;
/*     */         } 
/*     */         
/* 207 */         missed = addAndGet(-missed);
/* 208 */         if (missed == 0)
/*     */           break; 
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableThrottleLatest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */