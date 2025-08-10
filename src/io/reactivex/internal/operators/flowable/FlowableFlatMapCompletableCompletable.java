/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Completable;
/*     */ import io.reactivex.CompletableObserver;
/*     */ import io.reactivex.CompletableSource;
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.disposables.CompositeDisposable;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.Function;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.fuseable.FuseToFlowable;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.internal.util.AtomicThrowable;
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
/*     */ public final class FlowableFlatMapCompletableCompletable<T>
/*     */   extends Completable
/*     */   implements FuseToFlowable<T>
/*     */ {
/*     */   final Flowable<T> source;
/*     */   final Function<? super T, ? extends CompletableSource> mapper;
/*     */   final int maxConcurrency;
/*     */   final boolean delayErrors;
/*     */   
/*     */   public FlowableFlatMapCompletableCompletable(Flowable<T> source, Function<? super T, ? extends CompletableSource> mapper, boolean delayErrors, int maxConcurrency) {
/*  48 */     this.source = source;
/*  49 */     this.mapper = mapper;
/*  50 */     this.delayErrors = delayErrors;
/*  51 */     this.maxConcurrency = maxConcurrency;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(CompletableObserver observer) {
/*  56 */     this.source.subscribe(new FlatMapCompletableMainSubscriber<T>(observer, this.mapper, this.delayErrors, this.maxConcurrency));
/*     */   }
/*     */ 
/*     */   
/*     */   public Flowable<T> fuseToFlowable() {
/*  61 */     return RxJavaPlugins.onAssembly(new FlowableFlatMapCompletable<T>(this.source, this.mapper, this.delayErrors, this.maxConcurrency));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class FlatMapCompletableMainSubscriber<T>
/*     */     extends AtomicInteger
/*     */     implements FlowableSubscriber<T>, Disposable
/*     */   {
/*     */     private static final long serialVersionUID = 8443155186132538303L;
/*     */     
/*     */     final CompletableObserver downstream;
/*     */     
/*     */     final AtomicThrowable errors;
/*     */     
/*     */     final Function<? super T, ? extends CompletableSource> mapper;
/*     */     
/*     */     final boolean delayErrors;
/*     */     
/*     */     final CompositeDisposable set;
/*     */     
/*     */     final int maxConcurrency;
/*     */     
/*     */     Subscription upstream;
/*     */     volatile boolean disposed;
/*     */     
/*     */     FlatMapCompletableMainSubscriber(CompletableObserver observer, Function<? super T, ? extends CompletableSource> mapper, boolean delayErrors, int maxConcurrency) {
/*  87 */       this.downstream = observer;
/*  88 */       this.mapper = mapper;
/*  89 */       this.delayErrors = delayErrors;
/*  90 */       this.errors = new AtomicThrowable();
/*  91 */       this.set = new CompositeDisposable();
/*  92 */       this.maxConcurrency = maxConcurrency;
/*  93 */       lazySet(1);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/*  98 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/*  99 */         this.upstream = s;
/*     */         
/* 101 */         this.downstream.onSubscribe(this);
/*     */         
/* 103 */         int m = this.maxConcurrency;
/* 104 */         if (m == Integer.MAX_VALUE) {
/* 105 */           s.request(Long.MAX_VALUE);
/*     */         } else {
/* 107 */           s.request(m);
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onNext(T value) {
/*     */       CompletableSource cs;
/*     */       try {
/* 117 */         cs = (CompletableSource)ObjectHelper.requireNonNull(this.mapper.apply(value), "The mapper returned a null CompletableSource");
/* 118 */       } catch (Throwable ex) {
/* 119 */         Exceptions.throwIfFatal(ex);
/* 120 */         this.upstream.cancel();
/* 121 */         onError(ex);
/*     */         
/*     */         return;
/*     */       } 
/* 125 */       getAndIncrement();
/*     */       
/* 127 */       InnerObserver inner = new InnerObserver();
/*     */       
/* 129 */       if (!this.disposed && this.set.add(inner)) {
/* 130 */         cs.subscribe(inner);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/* 136 */       if (this.errors.addThrowable(e)) {
/* 137 */         if (this.delayErrors) {
/* 138 */           if (decrementAndGet() == 0) {
/* 139 */             Throwable ex = this.errors.terminate();
/* 140 */             this.downstream.onError(ex);
/*     */           }
/* 142 */           else if (this.maxConcurrency != Integer.MAX_VALUE) {
/* 143 */             this.upstream.request(1L);
/*     */           } 
/*     */         } else {
/*     */           
/* 147 */           dispose();
/* 148 */           if (getAndSet(0) > 0) {
/* 149 */             Throwable ex = this.errors.terminate();
/* 150 */             this.downstream.onError(ex);
/*     */           } 
/*     */         } 
/*     */       } else {
/* 154 */         RxJavaPlugins.onError(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 160 */       if (decrementAndGet() == 0) {
/* 161 */         Throwable ex = this.errors.terminate();
/* 162 */         if (ex != null) {
/* 163 */           this.downstream.onError(ex);
/*     */         } else {
/* 165 */           this.downstream.onComplete();
/*     */         }
/*     */       
/* 168 */       } else if (this.maxConcurrency != Integer.MAX_VALUE) {
/* 169 */         this.upstream.request(1L);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 176 */       this.disposed = true;
/* 177 */       this.upstream.cancel();
/* 178 */       this.set.dispose();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 183 */       return this.set.isDisposed();
/*     */     }
/*     */     
/*     */     void innerComplete(InnerObserver inner) {
/* 187 */       this.set.delete(inner);
/* 188 */       onComplete();
/*     */     }
/*     */     
/*     */     void innerError(InnerObserver inner, Throwable e) {
/* 192 */       this.set.delete(inner);
/* 193 */       onError(e);
/*     */     }
/*     */     
/*     */     final class InnerObserver
/*     */       extends AtomicReference<Disposable> implements CompletableObserver, Disposable {
/*     */       private static final long serialVersionUID = 8606673141535671828L;
/*     */       
/*     */       public void onSubscribe(Disposable d) {
/* 201 */         DisposableHelper.setOnce(this, d);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onComplete() {
/* 206 */         FlowableFlatMapCompletableCompletable.FlatMapCompletableMainSubscriber.this.innerComplete(this);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onError(Throwable e) {
/* 211 */         FlowableFlatMapCompletableCompletable.FlatMapCompletableMainSubscriber.this.innerError(this, e);
/*     */       }
/*     */ 
/*     */       
/*     */       public void dispose() {
/* 216 */         DisposableHelper.dispose(this);
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean isDisposed() {
/* 221 */         return DisposableHelper.isDisposed(get());
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableFlatMapCompletableCompletable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */