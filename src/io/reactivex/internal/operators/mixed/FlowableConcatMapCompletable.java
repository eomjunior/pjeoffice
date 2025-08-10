/*     */ package io.reactivex.internal.operators.mixed;
/*     */ 
/*     */ import io.reactivex.Completable;
/*     */ import io.reactivex.CompletableObserver;
/*     */ import io.reactivex.CompletableSource;
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.exceptions.MissingBackpressureException;
/*     */ import io.reactivex.functions.Function;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.fuseable.SimplePlainQueue;
/*     */ import io.reactivex.internal.queue.SpscArrayQueue;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.internal.util.AtomicThrowable;
/*     */ import io.reactivex.internal.util.ErrorMode;
/*     */ import io.reactivex.internal.util.ExceptionHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.concurrent.atomic.AtomicReference;
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
/*     */ public final class FlowableConcatMapCompletable<T>
/*     */   extends Completable
/*     */ {
/*     */   final Flowable<T> source;
/*     */   final Function<? super T, ? extends CompletableSource> mapper;
/*     */   final ErrorMode errorMode;
/*     */   final int prefetch;
/*     */   
/*     */   public FlowableConcatMapCompletable(Flowable<T> source, Function<? super T, ? extends CompletableSource> mapper, ErrorMode errorMode, int prefetch) {
/*  53 */     this.source = source;
/*  54 */     this.mapper = mapper;
/*  55 */     this.errorMode = errorMode;
/*  56 */     this.prefetch = prefetch;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(CompletableObserver observer) {
/*  61 */     this.source.subscribe(new ConcatMapCompletableObserver<T>(observer, this.mapper, this.errorMode, this.prefetch));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class ConcatMapCompletableObserver<T>
/*     */     extends AtomicInteger
/*     */     implements FlowableSubscriber<T>, Disposable
/*     */   {
/*     */     private static final long serialVersionUID = 3610901111000061034L;
/*     */     
/*     */     final CompletableObserver downstream;
/*     */     
/*     */     final Function<? super T, ? extends CompletableSource> mapper;
/*     */     
/*     */     final ErrorMode errorMode;
/*     */     
/*     */     final AtomicThrowable errors;
/*     */     
/*     */     final ConcatMapInnerObserver inner;
/*     */     
/*     */     final int prefetch;
/*     */     
/*     */     final SimplePlainQueue<T> queue;
/*     */     
/*     */     Subscription upstream;
/*     */     
/*     */     volatile boolean active;
/*     */     
/*     */     volatile boolean done;
/*     */     
/*     */     volatile boolean disposed;
/*     */     
/*     */     int consumed;
/*     */ 
/*     */     
/*     */     ConcatMapCompletableObserver(CompletableObserver downstream, Function<? super T, ? extends CompletableSource> mapper, ErrorMode errorMode, int prefetch) {
/*  97 */       this.downstream = downstream;
/*  98 */       this.mapper = mapper;
/*  99 */       this.errorMode = errorMode;
/* 100 */       this.prefetch = prefetch;
/* 101 */       this.errors = new AtomicThrowable();
/* 102 */       this.inner = new ConcatMapInnerObserver(this);
/* 103 */       this.queue = (SimplePlainQueue<T>)new SpscArrayQueue(prefetch);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/* 108 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/* 109 */         this.upstream = s;
/* 110 */         this.downstream.onSubscribe(this);
/* 111 */         s.request(this.prefetch);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 117 */       if (this.queue.offer(t)) {
/* 118 */         drain();
/*     */       } else {
/* 120 */         this.upstream.cancel();
/* 121 */         onError((Throwable)new MissingBackpressureException("Queue full?!"));
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 127 */       if (this.errors.addThrowable(t)) {
/* 128 */         if (this.errorMode == ErrorMode.IMMEDIATE) {
/* 129 */           this.inner.dispose();
/* 130 */           t = this.errors.terminate();
/* 131 */           if (t != ExceptionHelper.TERMINATED) {
/* 132 */             this.downstream.onError(t);
/*     */           }
/* 134 */           if (getAndIncrement() == 0) {
/* 135 */             this.queue.clear();
/*     */           }
/*     */         } else {
/* 138 */           this.done = true;
/* 139 */           drain();
/*     */         } 
/*     */       } else {
/* 142 */         RxJavaPlugins.onError(t);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 148 */       this.done = true;
/* 149 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 154 */       this.disposed = true;
/* 155 */       this.upstream.cancel();
/* 156 */       this.inner.dispose();
/* 157 */       if (getAndIncrement() == 0) {
/* 158 */         this.queue.clear();
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 164 */       return this.disposed;
/*     */     }
/*     */     
/*     */     void innerError(Throwable ex) {
/* 168 */       if (this.errors.addThrowable(ex)) {
/* 169 */         if (this.errorMode == ErrorMode.IMMEDIATE) {
/* 170 */           this.upstream.cancel();
/* 171 */           ex = this.errors.terminate();
/* 172 */           if (ex != ExceptionHelper.TERMINATED) {
/* 173 */             this.downstream.onError(ex);
/*     */           }
/* 175 */           if (getAndIncrement() == 0) {
/* 176 */             this.queue.clear();
/*     */           }
/*     */         } else {
/* 179 */           this.active = false;
/* 180 */           drain();
/*     */         } 
/*     */       } else {
/* 183 */         RxJavaPlugins.onError(ex);
/*     */       } 
/*     */     }
/*     */     
/*     */     void innerComplete() {
/* 188 */       this.active = false;
/* 189 */       drain();
/*     */     }
/*     */     
/*     */     void drain() {
/* 193 */       if (getAndIncrement() != 0) {
/*     */         return;
/*     */       }
/*     */       do {
/*     */         CompletableSource cs;
/* 198 */         if (this.disposed) {
/* 199 */           this.queue.clear();
/*     */           
/*     */           return;
/*     */         } 
/* 203 */         if (this.active)
/*     */           continue; 
/* 205 */         if (this.errorMode == ErrorMode.BOUNDARY && 
/* 206 */           this.errors.get() != null) {
/* 207 */           this.queue.clear();
/* 208 */           Throwable ex = this.errors.terminate();
/* 209 */           this.downstream.onError(ex);
/*     */           
/*     */           return;
/*     */         } 
/*     */         
/* 214 */         boolean d = this.done;
/* 215 */         T v = (T)this.queue.poll();
/* 216 */         boolean empty = (v == null);
/*     */         
/* 218 */         if (d && empty) {
/* 219 */           Throwable ex = this.errors.terminate();
/* 220 */           if (ex != null) {
/* 221 */             this.downstream.onError(ex);
/*     */           } else {
/* 223 */             this.downstream.onComplete();
/*     */           } 
/*     */           
/*     */           return;
/*     */         } 
/* 228 */         if (empty)
/*     */           continue; 
/* 230 */         int limit = this.prefetch - (this.prefetch >> 1);
/* 231 */         int c = this.consumed + 1;
/* 232 */         if (c == limit) {
/* 233 */           this.consumed = 0;
/* 234 */           this.upstream.request(limit);
/*     */         } else {
/* 236 */           this.consumed = c;
/*     */         } 
/*     */ 
/*     */ 
/*     */         
/*     */         try {
/* 242 */           cs = (CompletableSource)ObjectHelper.requireNonNull(this.mapper.apply(v), "The mapper returned a null CompletableSource");
/* 243 */         } catch (Throwable ex) {
/* 244 */           Exceptions.throwIfFatal(ex);
/* 245 */           this.queue.clear();
/* 246 */           this.upstream.cancel();
/* 247 */           this.errors.addThrowable(ex);
/* 248 */           ex = this.errors.terminate();
/* 249 */           this.downstream.onError(ex);
/*     */           return;
/*     */         } 
/* 252 */         this.active = true;
/* 253 */         cs.subscribe(this.inner);
/*     */       
/*     */       }
/* 256 */       while (decrementAndGet() != 0);
/*     */     }
/*     */     
/*     */     static final class ConcatMapInnerObserver
/*     */       extends AtomicReference<Disposable>
/*     */       implements CompletableObserver
/*     */     {
/*     */       private static final long serialVersionUID = 5638352172918776687L;
/*     */       final FlowableConcatMapCompletable.ConcatMapCompletableObserver<?> parent;
/*     */       
/*     */       ConcatMapInnerObserver(FlowableConcatMapCompletable.ConcatMapCompletableObserver<?> parent) {
/* 267 */         this.parent = parent;
/*     */       }
/*     */ 
/*     */       
/*     */       public void onSubscribe(Disposable d) {
/* 272 */         DisposableHelper.replace(this, d);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onError(Throwable e) {
/* 277 */         this.parent.innerError(e);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onComplete() {
/* 282 */         this.parent.innerComplete();
/*     */       }
/*     */       
/*     */       void dispose() {
/* 286 */         DisposableHelper.dispose(this);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/mixed/FlowableConcatMapCompletable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */