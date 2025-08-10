/*     */ package io.reactivex.internal.operators.observable;
/*     */ 
/*     */ import io.reactivex.ObservableSource;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.Scheduler;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.queue.SpscLinkedArrayQueue;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
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
/*     */ public final class ObservableTakeLastTimed<T>
/*     */   extends AbstractObservableWithUpstream<T, T>
/*     */ {
/*     */   final long count;
/*     */   final long time;
/*     */   final TimeUnit unit;
/*     */   final Scheduler scheduler;
/*     */   final int bufferSize;
/*     */   final boolean delayError;
/*     */   
/*     */   public ObservableTakeLastTimed(ObservableSource<T> source, long count, long time, TimeUnit unit, Scheduler scheduler, int bufferSize, boolean delayError) {
/*  34 */     super(source);
/*  35 */     this.count = count;
/*  36 */     this.time = time;
/*  37 */     this.unit = unit;
/*  38 */     this.scheduler = scheduler;
/*  39 */     this.bufferSize = bufferSize;
/*  40 */     this.delayError = delayError;
/*     */   }
/*     */ 
/*     */   
/*     */   public void subscribeActual(Observer<? super T> t) {
/*  45 */     this.source.subscribe(new TakeLastTimedObserver<T>(t, this.count, this.time, this.unit, this.scheduler, this.bufferSize, this.delayError));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class TakeLastTimedObserver<T>
/*     */     extends AtomicBoolean
/*     */     implements Observer<T>, Disposable
/*     */   {
/*     */     private static final long serialVersionUID = -5677354903406201275L;
/*     */     
/*     */     final Observer<? super T> downstream;
/*     */     final long count;
/*     */     final long time;
/*     */     final TimeUnit unit;
/*     */     final Scheduler scheduler;
/*     */     final SpscLinkedArrayQueue<Object> queue;
/*     */     final boolean delayError;
/*     */     Disposable upstream;
/*     */     volatile boolean cancelled;
/*     */     Throwable error;
/*     */     
/*     */     TakeLastTimedObserver(Observer<? super T> actual, long count, long time, TimeUnit unit, Scheduler scheduler, int bufferSize, boolean delayError) {
/*  67 */       this.downstream = actual;
/*  68 */       this.count = count;
/*  69 */       this.time = time;
/*  70 */       this.unit = unit;
/*  71 */       this.scheduler = scheduler;
/*  72 */       this.queue = new SpscLinkedArrayQueue(bufferSize);
/*  73 */       this.delayError = delayError;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  78 */       if (DisposableHelper.validate(this.upstream, d)) {
/*  79 */         this.upstream = d;
/*  80 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*  86 */       SpscLinkedArrayQueue<Object> q = this.queue;
/*     */       
/*  88 */       long now = this.scheduler.now(this.unit);
/*  89 */       long time = this.time;
/*  90 */       long c = this.count;
/*  91 */       boolean unbounded = (c == Long.MAX_VALUE);
/*     */       
/*  93 */       q.offer(Long.valueOf(now), t);
/*     */       
/*  95 */       while (!q.isEmpty()) {
/*  96 */         long ts = ((Long)q.peek()).longValue();
/*  97 */         if (ts <= now - time || (!unbounded && (q.size() >> 1) > c)) {
/*  98 */           q.poll();
/*  99 */           q.poll();
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 108 */       this.error = t;
/* 109 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 114 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 119 */       if (!this.cancelled) {
/* 120 */         this.cancelled = true;
/* 121 */         this.upstream.dispose();
/*     */         
/* 123 */         if (compareAndSet(false, true)) {
/* 124 */           this.queue.clear();
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 131 */       return this.cancelled;
/*     */     }
/*     */     
/*     */     void drain() {
/* 135 */       if (!compareAndSet(false, true)) {
/*     */         return;
/*     */       }
/*     */       
/* 139 */       Observer<? super T> a = this.downstream;
/* 140 */       SpscLinkedArrayQueue<Object> q = this.queue;
/* 141 */       boolean delayError = this.delayError;
/* 142 */       long timestampLimit = this.scheduler.now(this.unit) - this.time;
/*     */       
/*     */       while (true) {
/* 145 */         if (this.cancelled) {
/* 146 */           q.clear();
/*     */           
/*     */           return;
/*     */         } 
/* 150 */         if (!delayError) {
/* 151 */           Throwable ex = this.error;
/* 152 */           if (ex != null) {
/* 153 */             q.clear();
/* 154 */             a.onError(ex);
/*     */             
/*     */             return;
/*     */           } 
/*     */         } 
/* 159 */         Object ts = q.poll();
/* 160 */         boolean empty = (ts == null);
/*     */         
/* 162 */         if (empty) {
/* 163 */           Throwable ex = this.error;
/* 164 */           if (ex != null) {
/* 165 */             a.onError(ex);
/*     */           } else {
/* 167 */             a.onComplete();
/*     */           } 
/*     */           
/*     */           return;
/*     */         } 
/*     */         
/* 173 */         T o = (T)q.poll();
/*     */         
/* 175 */         if (((Long)ts).longValue() < timestampLimit) {
/*     */           continue;
/*     */         }
/*     */         
/* 179 */         a.onNext(o);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableTakeLastTimed.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */