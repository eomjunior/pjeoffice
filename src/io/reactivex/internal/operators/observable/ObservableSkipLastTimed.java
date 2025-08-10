/*     */ package io.reactivex.internal.operators.observable;
/*     */ 
/*     */ import io.reactivex.ObservableSource;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.Scheduler;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.queue.SpscLinkedArrayQueue;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
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
/*     */ public final class ObservableSkipLastTimed<T>
/*     */   extends AbstractObservableWithUpstream<T, T>
/*     */ {
/*     */   final long time;
/*     */   final TimeUnit unit;
/*     */   final Scheduler scheduler;
/*     */   final int bufferSize;
/*     */   final boolean delayError;
/*     */   
/*     */   public ObservableSkipLastTimed(ObservableSource<T> source, long time, TimeUnit unit, Scheduler scheduler, int bufferSize, boolean delayError) {
/*  33 */     super(source);
/*  34 */     this.time = time;
/*  35 */     this.unit = unit;
/*  36 */     this.scheduler = scheduler;
/*  37 */     this.bufferSize = bufferSize;
/*  38 */     this.delayError = delayError;
/*     */   }
/*     */ 
/*     */   
/*     */   public void subscribeActual(Observer<? super T> t) {
/*  43 */     this.source.subscribe(new SkipLastTimedObserver<T>(t, this.time, this.unit, this.scheduler, this.bufferSize, this.delayError));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class SkipLastTimedObserver<T>
/*     */     extends AtomicInteger
/*     */     implements Observer<T>, Disposable
/*     */   {
/*     */     private static final long serialVersionUID = -5677354903406201275L;
/*     */     final Observer<? super T> downstream;
/*     */     final long time;
/*     */     final TimeUnit unit;
/*     */     final Scheduler scheduler;
/*     */     final SpscLinkedArrayQueue<Object> queue;
/*     */     final boolean delayError;
/*     */     Disposable upstream;
/*     */     volatile boolean cancelled;
/*     */     volatile boolean done;
/*     */     Throwable error;
/*     */     
/*     */     SkipLastTimedObserver(Observer<? super T> actual, long time, TimeUnit unit, Scheduler scheduler, int bufferSize, boolean delayError) {
/*  64 */       this.downstream = actual;
/*  65 */       this.time = time;
/*  66 */       this.unit = unit;
/*  67 */       this.scheduler = scheduler;
/*  68 */       this.queue = new SpscLinkedArrayQueue(bufferSize);
/*  69 */       this.delayError = delayError;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  74 */       if (DisposableHelper.validate(this.upstream, d)) {
/*  75 */         this.upstream = d;
/*  76 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*  82 */       SpscLinkedArrayQueue<Object> q = this.queue;
/*     */       
/*  84 */       long now = this.scheduler.now(this.unit);
/*     */       
/*  86 */       q.offer(Long.valueOf(now), t);
/*     */       
/*  88 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/*  93 */       this.error = t;
/*  94 */       this.done = true;
/*  95 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 100 */       this.done = true;
/* 101 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 106 */       if (!this.cancelled) {
/* 107 */         this.cancelled = true;
/* 108 */         this.upstream.dispose();
/*     */         
/* 110 */         if (getAndIncrement() == 0) {
/* 111 */           this.queue.clear();
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 118 */       return this.cancelled;
/*     */     }
/*     */     
/*     */     void drain() {
/* 122 */       if (getAndIncrement() != 0) {
/*     */         return;
/*     */       }
/*     */       
/* 126 */       int missed = 1;
/*     */       
/* 128 */       Observer<? super T> a = this.downstream;
/* 129 */       SpscLinkedArrayQueue<Object> q = this.queue;
/* 130 */       boolean delayError = this.delayError;
/* 131 */       TimeUnit unit = this.unit;
/* 132 */       Scheduler scheduler = this.scheduler;
/* 133 */       long time = this.time;
/*     */ 
/*     */ 
/*     */       
/*     */       while (true) {
/* 138 */         if (this.cancelled) {
/* 139 */           this.queue.clear();
/*     */           
/*     */           return;
/*     */         } 
/* 143 */         boolean d = this.done;
/*     */         
/* 145 */         Long ts = (Long)q.peek();
/*     */         
/* 147 */         boolean empty = (ts == null);
/*     */         
/* 149 */         long now = scheduler.now(unit);
/*     */         
/* 151 */         if (!empty && ts.longValue() > now - time) {
/* 152 */           empty = true;
/*     */         }
/*     */         
/* 155 */         if (d) {
/* 156 */           if (delayError) {
/* 157 */             if (empty) {
/* 158 */               Throwable e = this.error;
/* 159 */               if (e != null) {
/* 160 */                 a.onError(e);
/*     */               } else {
/* 162 */                 a.onComplete();
/*     */               } 
/*     */               return;
/*     */             } 
/*     */           } else {
/* 167 */             Throwable e = this.error;
/* 168 */             if (e != null) {
/* 169 */               this.queue.clear();
/* 170 */               a.onError(e);
/*     */               return;
/*     */             } 
/* 173 */             if (empty) {
/* 174 */               a.onComplete();
/*     */               
/*     */               return;
/*     */             } 
/*     */           } 
/*     */         }
/* 180 */         if (empty) {
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
/* 191 */           missed = addAndGet(-missed);
/* 192 */           if (missed == 0)
/*     */             break; 
/*     */           continue;
/*     */         } 
/*     */         q.poll();
/*     */         T v = (T)q.poll();
/*     */         a.onNext(v);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableSkipLastTimed.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */