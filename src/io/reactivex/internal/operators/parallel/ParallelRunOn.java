/*     */ package io.reactivex.internal.operators.parallel;
/*     */ 
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.Scheduler;
/*     */ import io.reactivex.exceptions.MissingBackpressureException;
/*     */ import io.reactivex.internal.fuseable.ConditionalSubscriber;
/*     */ import io.reactivex.internal.queue.SpscArrayQueue;
/*     */ import io.reactivex.internal.schedulers.SchedulerMultiWorkerSupport;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.internal.util.BackpressureHelper;
/*     */ import io.reactivex.parallel.ParallelFlowable;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.concurrent.atomic.AtomicLong;
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
/*     */ public final class ParallelRunOn<T>
/*     */   extends ParallelFlowable<T>
/*     */ {
/*     */   final ParallelFlowable<? extends T> source;
/*     */   final Scheduler scheduler;
/*     */   final int prefetch;
/*     */   
/*     */   public ParallelRunOn(ParallelFlowable<? extends T> parent, Scheduler scheduler, int prefetch) {
/*  46 */     this.source = parent;
/*  47 */     this.scheduler = scheduler;
/*  48 */     this.prefetch = prefetch;
/*     */   }
/*     */ 
/*     */   
/*     */   public void subscribe(Subscriber<? super T>[] subscribers) {
/*  53 */     if (!validate((Subscriber[])subscribers)) {
/*     */       return;
/*     */     }
/*     */     
/*  57 */     int n = subscribers.length;
/*     */ 
/*     */     
/*  60 */     Subscriber[] arrayOfSubscriber = new Subscriber[n];
/*     */     
/*  62 */     if (this.scheduler instanceof SchedulerMultiWorkerSupport) {
/*  63 */       SchedulerMultiWorkerSupport multiworker = (SchedulerMultiWorkerSupport)this.scheduler;
/*  64 */       multiworker.createWorkers(n, new MultiWorkerCallback(subscribers, (Subscriber<T>[])arrayOfSubscriber));
/*     */     } else {
/*  66 */       for (int i = 0; i < n; i++) {
/*  67 */         createSubscriber(i, subscribers, (Subscriber<T>[])arrayOfSubscriber, this.scheduler.createWorker());
/*     */       }
/*     */     } 
/*  70 */     this.source.subscribe(arrayOfSubscriber);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void createSubscriber(int i, Subscriber<? super T>[] subscribers, Subscriber<T>[] parents, Scheduler.Worker worker) {
/*  76 */     Subscriber<? super T> a = subscribers[i];
/*     */     
/*  78 */     SpscArrayQueue<T> q = new SpscArrayQueue(this.prefetch);
/*     */     
/*  80 */     if (a instanceof ConditionalSubscriber) {
/*  81 */       parents[i] = (Subscriber<T>)new RunOnConditionalSubscriber<T>((ConditionalSubscriber<? super T>)a, this.prefetch, q, worker);
/*     */     } else {
/*  83 */       parents[i] = (Subscriber<T>)new RunOnSubscriber<T>(a, this.prefetch, q, worker);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   final class MultiWorkerCallback
/*     */     implements SchedulerMultiWorkerSupport.WorkerCallback
/*     */   {
/*     */     final Subscriber<? super T>[] subscribers;
/*     */     final Subscriber<T>[] parents;
/*     */     
/*     */     MultiWorkerCallback(Subscriber<? super T>[] subscribers, Subscriber<T>[] parents) {
/*  95 */       this.subscribers = subscribers;
/*  96 */       this.parents = parents;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onWorker(int i, Scheduler.Worker w) {
/* 101 */       ParallelRunOn.this.createSubscriber(i, this.subscribers, this.parents, w);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int parallelism() {
/* 107 */     return this.source.parallelism();
/*     */   }
/*     */ 
/*     */   
/*     */   static abstract class BaseRunOnSubscriber<T>
/*     */     extends AtomicInteger
/*     */     implements FlowableSubscriber<T>, Subscription, Runnable
/*     */   {
/*     */     private static final long serialVersionUID = 9222303586456402150L;
/*     */     
/*     */     final int prefetch;
/*     */     
/*     */     final int limit;
/*     */     
/*     */     final SpscArrayQueue<T> queue;
/*     */     
/*     */     final Scheduler.Worker worker;
/*     */     
/*     */     Subscription upstream;
/*     */     
/*     */     volatile boolean done;
/*     */     Throwable error;
/* 129 */     final AtomicLong requested = new AtomicLong();
/*     */     
/*     */     volatile boolean cancelled;
/*     */     
/*     */     int consumed;
/*     */     
/*     */     BaseRunOnSubscriber(int prefetch, SpscArrayQueue<T> queue, Scheduler.Worker worker) {
/* 136 */       this.prefetch = prefetch;
/* 137 */       this.queue = queue;
/* 138 */       this.limit = prefetch - (prefetch >> 2);
/* 139 */       this.worker = worker;
/*     */     }
/*     */ 
/*     */     
/*     */     public final void onNext(T t) {
/* 144 */       if (this.done) {
/*     */         return;
/*     */       }
/* 147 */       if (!this.queue.offer(t)) {
/* 148 */         this.upstream.cancel();
/* 149 */         onError((Throwable)new MissingBackpressureException("Queue is full?!"));
/*     */         return;
/*     */       } 
/* 152 */       schedule();
/*     */     }
/*     */ 
/*     */     
/*     */     public final void onError(Throwable t) {
/* 157 */       if (this.done) {
/* 158 */         RxJavaPlugins.onError(t);
/*     */         return;
/*     */       } 
/* 161 */       this.error = t;
/* 162 */       this.done = true;
/* 163 */       schedule();
/*     */     }
/*     */ 
/*     */     
/*     */     public final void onComplete() {
/* 168 */       if (this.done) {
/*     */         return;
/*     */       }
/* 171 */       this.done = true;
/* 172 */       schedule();
/*     */     }
/*     */ 
/*     */     
/*     */     public final void request(long n) {
/* 177 */       if (SubscriptionHelper.validate(n)) {
/* 178 */         BackpressureHelper.add(this.requested, n);
/* 179 */         schedule();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public final void cancel() {
/* 185 */       if (!this.cancelled) {
/* 186 */         this.cancelled = true;
/* 187 */         this.upstream.cancel();
/* 188 */         this.worker.dispose();
/*     */         
/* 190 */         if (getAndIncrement() == 0) {
/* 191 */           this.queue.clear();
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/*     */     final void schedule() {
/* 197 */       if (getAndIncrement() == 0) {
/* 198 */         this.worker.schedule(this);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   static final class RunOnSubscriber<T>
/*     */     extends BaseRunOnSubscriber<T>
/*     */   {
/*     */     private static final long serialVersionUID = 1075119423897941642L;
/*     */     final Subscriber<? super T> downstream;
/*     */     
/*     */     RunOnSubscriber(Subscriber<? super T> actual, int prefetch, SpscArrayQueue<T> queue, Scheduler.Worker worker) {
/* 210 */       super(prefetch, queue, worker);
/* 211 */       this.downstream = actual;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/* 216 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/* 217 */         this.upstream = s;
/*     */         
/* 219 */         this.downstream.onSubscribe(this);
/*     */         
/* 221 */         s.request(this.prefetch);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/* 227 */       int missed = 1;
/* 228 */       int c = this.consumed;
/* 229 */       SpscArrayQueue<T> q = this.queue;
/* 230 */       Subscriber<? super T> a = this.downstream;
/* 231 */       int lim = this.limit;
/*     */ 
/*     */       
/*     */       while (true) {
/* 235 */         long r = this.requested.get();
/* 236 */         long e = 0L;
/*     */         
/* 238 */         while (e != r) {
/* 239 */           if (this.cancelled) {
/* 240 */             q.clear();
/*     */             
/*     */             return;
/*     */           } 
/* 244 */           boolean d = this.done;
/*     */           
/* 246 */           if (d) {
/* 247 */             Throwable ex = this.error;
/* 248 */             if (ex != null) {
/* 249 */               q.clear();
/*     */               
/* 251 */               a.onError(ex);
/*     */               
/* 253 */               this.worker.dispose();
/*     */               
/*     */               return;
/*     */             } 
/*     */           } 
/* 258 */           T v = (T)q.poll();
/*     */           
/* 260 */           boolean empty = (v == null);
/*     */           
/* 262 */           if (d && empty) {
/* 263 */             a.onComplete();
/*     */             
/* 265 */             this.worker.dispose();
/*     */             
/*     */             return;
/*     */           } 
/* 269 */           if (empty) {
/*     */             break;
/*     */           }
/*     */           
/* 273 */           a.onNext(v);
/*     */           
/* 275 */           e++;
/*     */           
/* 277 */           int p = ++c;
/* 278 */           if (p == lim) {
/* 279 */             c = 0;
/* 280 */             this.upstream.request(p);
/*     */           } 
/*     */         } 
/*     */         
/* 284 */         if (e == r) {
/* 285 */           if (this.cancelled) {
/* 286 */             q.clear();
/*     */             
/*     */             return;
/*     */           } 
/* 290 */           if (this.done) {
/* 291 */             Throwable ex = this.error;
/* 292 */             if (ex != null) {
/* 293 */               q.clear();
/*     */               
/* 295 */               a.onError(ex);
/*     */               
/* 297 */               this.worker.dispose();
/*     */               return;
/*     */             } 
/* 300 */             if (q.isEmpty()) {
/* 301 */               a.onComplete();
/*     */               
/* 303 */               this.worker.dispose();
/*     */               
/*     */               return;
/*     */             } 
/*     */           } 
/*     */         } 
/* 309 */         if (e != 0L && r != Long.MAX_VALUE) {
/* 310 */           this.requested.addAndGet(-e);
/*     */         }
/*     */         
/* 313 */         int w = get();
/* 314 */         if (w == missed) {
/* 315 */           this.consumed = c;
/* 316 */           missed = addAndGet(-missed);
/* 317 */           if (missed == 0)
/*     */             break; 
/*     */           continue;
/*     */         } 
/* 321 */         missed = w;
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class RunOnConditionalSubscriber<T>
/*     */     extends BaseRunOnSubscriber<T>
/*     */   {
/*     */     private static final long serialVersionUID = 1075119423897941642L;
/*     */     final ConditionalSubscriber<? super T> downstream;
/*     */     
/*     */     RunOnConditionalSubscriber(ConditionalSubscriber<? super T> actual, int prefetch, SpscArrayQueue<T> queue, Scheduler.Worker worker) {
/* 334 */       super(prefetch, queue, worker);
/* 335 */       this.downstream = actual;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/* 340 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/* 341 */         this.upstream = s;
/*     */         
/* 343 */         this.downstream.onSubscribe(this);
/*     */         
/* 345 */         s.request(this.prefetch);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/* 351 */       int missed = 1;
/* 352 */       int c = this.consumed;
/* 353 */       SpscArrayQueue<T> q = this.queue;
/* 354 */       ConditionalSubscriber<? super T> a = this.downstream;
/* 355 */       int lim = this.limit;
/*     */ 
/*     */       
/*     */       while (true) {
/* 359 */         long r = this.requested.get();
/* 360 */         long e = 0L;
/*     */         
/* 362 */         while (e != r) {
/* 363 */           if (this.cancelled) {
/* 364 */             q.clear();
/*     */             
/*     */             return;
/*     */           } 
/* 368 */           boolean d = this.done;
/*     */           
/* 370 */           if (d) {
/* 371 */             Throwable ex = this.error;
/* 372 */             if (ex != null) {
/* 373 */               q.clear();
/*     */               
/* 375 */               a.onError(ex);
/*     */               
/* 377 */               this.worker.dispose();
/*     */               
/*     */               return;
/*     */             } 
/*     */           } 
/* 382 */           T v = (T)q.poll();
/*     */           
/* 384 */           boolean empty = (v == null);
/*     */           
/* 386 */           if (d && empty) {
/* 387 */             a.onComplete();
/*     */             
/* 389 */             this.worker.dispose();
/*     */             
/*     */             return;
/*     */           } 
/* 393 */           if (empty) {
/*     */             break;
/*     */           }
/*     */           
/* 397 */           if (a.tryOnNext(v)) {
/* 398 */             e++;
/*     */           }
/*     */           
/* 401 */           int p = ++c;
/* 402 */           if (p == lim) {
/* 403 */             c = 0;
/* 404 */             this.upstream.request(p);
/*     */           } 
/*     */         } 
/*     */         
/* 408 */         if (e == r) {
/* 409 */           if (this.cancelled) {
/* 410 */             q.clear();
/*     */             
/*     */             return;
/*     */           } 
/* 414 */           if (this.done) {
/* 415 */             Throwable ex = this.error;
/* 416 */             if (ex != null) {
/* 417 */               q.clear();
/*     */               
/* 419 */               a.onError(ex);
/*     */               
/* 421 */               this.worker.dispose();
/*     */               return;
/*     */             } 
/* 424 */             if (q.isEmpty()) {
/* 425 */               a.onComplete();
/*     */               
/* 427 */               this.worker.dispose();
/*     */               
/*     */               return;
/*     */             } 
/*     */           } 
/*     */         } 
/* 433 */         if (e != 0L && r != Long.MAX_VALUE) {
/* 434 */           this.requested.addAndGet(-e);
/*     */         }
/*     */         
/* 437 */         int w = get();
/* 438 */         if (w == missed) {
/* 439 */           this.consumed = c;
/* 440 */           missed = addAndGet(-missed);
/* 441 */           if (missed == 0)
/*     */             break; 
/*     */           continue;
/*     */         } 
/* 445 */         missed = w;
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/parallel/ParallelRunOn.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */