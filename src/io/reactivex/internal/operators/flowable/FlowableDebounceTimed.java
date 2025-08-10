/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.Scheduler;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.MissingBackpressureException;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.internal.util.BackpressureHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import io.reactivex.subscribers.SerializedSubscriber;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
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
/*     */ public final class FlowableDebounceTimed<T>
/*     */   extends AbstractFlowableWithUpstream<T, T>
/*     */ {
/*     */   final long timeout;
/*     */   final TimeUnit unit;
/*     */   final Scheduler scheduler;
/*     */   
/*     */   public FlowableDebounceTimed(Flowable<T> source, long timeout, TimeUnit unit, Scheduler scheduler) {
/*  37 */     super(source);
/*  38 */     this.timeout = timeout;
/*  39 */     this.unit = unit;
/*  40 */     this.scheduler = scheduler;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super T> s) {
/*  45 */     this.source.subscribe(new DebounceTimedSubscriber((Subscriber<?>)new SerializedSubscriber(s), this.timeout, this.unit, this.scheduler
/*     */           
/*  47 */           .createWorker()));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class DebounceTimedSubscriber<T>
/*     */     extends AtomicLong
/*     */     implements FlowableSubscriber<T>, Subscription
/*     */   {
/*     */     private static final long serialVersionUID = -9102637559663639004L;
/*     */     
/*     */     final Subscriber<? super T> downstream;
/*     */     
/*     */     final long timeout;
/*     */     final TimeUnit unit;
/*     */     final Scheduler.Worker worker;
/*     */     Subscription upstream;
/*     */     Disposable timer;
/*     */     volatile long index;
/*     */     boolean done;
/*     */     
/*     */     DebounceTimedSubscriber(Subscriber<? super T> actual, long timeout, TimeUnit unit, Scheduler.Worker worker) {
/*  68 */       this.downstream = actual;
/*  69 */       this.timeout = timeout;
/*  70 */       this.unit = unit;
/*  71 */       this.worker = worker;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/*  76 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/*  77 */         this.upstream = s;
/*  78 */         this.downstream.onSubscribe(this);
/*  79 */         s.request(Long.MAX_VALUE);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*  85 */       if (this.done) {
/*     */         return;
/*     */       }
/*  88 */       long idx = this.index + 1L;
/*  89 */       this.index = idx;
/*     */       
/*  91 */       Disposable d = this.timer;
/*  92 */       if (d != null) {
/*  93 */         d.dispose();
/*     */       }
/*     */       
/*  96 */       FlowableDebounceTimed.DebounceEmitter<T> de = new FlowableDebounceTimed.DebounceEmitter<T>(t, idx, this);
/*  97 */       this.timer = de;
/*  98 */       d = this.worker.schedule(de, this.timeout, this.unit);
/*  99 */       de.setResource(d);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 104 */       if (this.done) {
/* 105 */         RxJavaPlugins.onError(t);
/*     */         return;
/*     */       } 
/* 108 */       this.done = true;
/* 109 */       Disposable d = this.timer;
/* 110 */       if (d != null) {
/* 111 */         d.dispose();
/*     */       }
/* 113 */       this.downstream.onError(t);
/* 114 */       this.worker.dispose();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 119 */       if (this.done) {
/*     */         return;
/*     */       }
/* 122 */       this.done = true;
/*     */       
/* 124 */       Disposable d = this.timer;
/* 125 */       if (d != null) {
/* 126 */         d.dispose();
/*     */       }
/*     */ 
/*     */       
/* 130 */       FlowableDebounceTimed.DebounceEmitter<T> de = (FlowableDebounceTimed.DebounceEmitter<T>)d;
/* 131 */       if (de != null) {
/* 132 */         de.emit();
/*     */       }
/*     */       
/* 135 */       this.downstream.onComplete();
/* 136 */       this.worker.dispose();
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/* 141 */       if (SubscriptionHelper.validate(n)) {
/* 142 */         BackpressureHelper.add(this, n);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 148 */       this.upstream.cancel();
/* 149 */       this.worker.dispose();
/*     */     }
/*     */     
/*     */     void emit(long idx, T t, FlowableDebounceTimed.DebounceEmitter<T> emitter) {
/* 153 */       if (idx == this.index) {
/* 154 */         long r = get();
/* 155 */         if (r != 0L) {
/* 156 */           this.downstream.onNext(t);
/* 157 */           BackpressureHelper.produced(this, 1L);
/*     */           
/* 159 */           emitter.dispose();
/*     */         } else {
/* 161 */           cancel();
/* 162 */           this.downstream.onError((Throwable)new MissingBackpressureException("Could not deliver value due to lack of requests"));
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   static final class DebounceEmitter<T>
/*     */     extends AtomicReference<Disposable>
/*     */     implements Runnable, Disposable
/*     */   {
/*     */     private static final long serialVersionUID = 6812032969491025141L;
/*     */     final T value;
/*     */     final long idx;
/*     */     final FlowableDebounceTimed.DebounceTimedSubscriber<T> parent;
/* 176 */     final AtomicBoolean once = new AtomicBoolean();
/*     */     
/*     */     DebounceEmitter(T value, long idx, FlowableDebounceTimed.DebounceTimedSubscriber<T> parent) {
/* 179 */       this.value = value;
/* 180 */       this.idx = idx;
/* 181 */       this.parent = parent;
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/* 186 */       emit();
/*     */     }
/*     */     
/*     */     void emit() {
/* 190 */       if (this.once.compareAndSet(false, true)) {
/* 191 */         this.parent.emit(this.idx, this.value, this);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 197 */       DisposableHelper.dispose(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 202 */       return (get() == DisposableHelper.DISPOSED);
/*     */     }
/*     */     
/*     */     public void setResource(Disposable d) {
/* 206 */       DisposableHelper.replace(this, d);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableDebounceTimed.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */