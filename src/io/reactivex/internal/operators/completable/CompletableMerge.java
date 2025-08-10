/*     */ package io.reactivex.internal.operators.completable;
/*     */ 
/*     */ import io.reactivex.Completable;
/*     */ import io.reactivex.CompletableObserver;
/*     */ import io.reactivex.CompletableSource;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.disposables.CompositeDisposable;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.internal.util.AtomicThrowable;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import org.reactivestreams.Publisher;
/*     */ import org.reactivestreams.Subscriber;
/*     */ import org.reactivestreams.Subscription;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class CompletableMerge
/*     */   extends Completable
/*     */ {
/*     */   final Publisher<? extends CompletableSource> source;
/*     */   final int maxConcurrency;
/*     */   final boolean delayErrors;
/*     */   
/*     */   public CompletableMerge(Publisher<? extends CompletableSource> source, int maxConcurrency, boolean delayErrors) {
/*  33 */     this.source = source;
/*  34 */     this.maxConcurrency = maxConcurrency;
/*  35 */     this.delayErrors = delayErrors;
/*     */   }
/*     */ 
/*     */   
/*     */   public void subscribeActual(CompletableObserver observer) {
/*  40 */     CompletableMergeSubscriber parent = new CompletableMergeSubscriber(observer, this.maxConcurrency, this.delayErrors);
/*  41 */     this.source.subscribe((Subscriber)parent);
/*     */   }
/*     */ 
/*     */   
/*     */   static final class CompletableMergeSubscriber
/*     */     extends AtomicInteger
/*     */     implements FlowableSubscriber<CompletableSource>, Disposable
/*     */   {
/*     */     private static final long serialVersionUID = -2108443387387077490L;
/*     */     
/*     */     final CompletableObserver downstream;
/*     */     
/*     */     final int maxConcurrency;
/*     */     
/*     */     final boolean delayErrors;
/*     */     final AtomicThrowable error;
/*     */     final CompositeDisposable set;
/*     */     Subscription upstream;
/*     */     
/*     */     CompletableMergeSubscriber(CompletableObserver actual, int maxConcurrency, boolean delayErrors) {
/*  61 */       this.downstream = actual;
/*  62 */       this.maxConcurrency = maxConcurrency;
/*  63 */       this.delayErrors = delayErrors;
/*  64 */       this.set = new CompositeDisposable();
/*  65 */       this.error = new AtomicThrowable();
/*  66 */       lazySet(1);
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/*  71 */       this.upstream.cancel();
/*  72 */       this.set.dispose();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/*  77 */       return this.set.isDisposed();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/*  82 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/*  83 */         this.upstream = s;
/*  84 */         this.downstream.onSubscribe(this);
/*  85 */         if (this.maxConcurrency == Integer.MAX_VALUE) {
/*  86 */           s.request(Long.MAX_VALUE);
/*     */         } else {
/*  88 */           s.request(this.maxConcurrency);
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(CompletableSource t) {
/*  95 */       getAndIncrement();
/*     */       
/*  97 */       MergeInnerObserver inner = new MergeInnerObserver();
/*  98 */       this.set.add(inner);
/*  99 */       t.subscribe(inner);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 104 */       if (!this.delayErrors) {
/* 105 */         this.set.dispose();
/*     */         
/* 107 */         if (this.error.addThrowable(t)) {
/* 108 */           if (getAndSet(0) > 0) {
/* 109 */             this.downstream.onError(this.error.terminate());
/*     */           }
/*     */         } else {
/* 112 */           RxJavaPlugins.onError(t);
/*     */         }
/*     */       
/* 115 */       } else if (this.error.addThrowable(t)) {
/* 116 */         if (decrementAndGet() == 0) {
/* 117 */           this.downstream.onError(this.error.terminate());
/*     */         }
/*     */       } else {
/* 120 */         RxJavaPlugins.onError(t);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 127 */       if (decrementAndGet() == 0) {
/* 128 */         Throwable ex = (Throwable)this.error.get();
/* 129 */         if (ex != null) {
/* 130 */           this.downstream.onError(this.error.terminate());
/*     */         } else {
/* 132 */           this.downstream.onComplete();
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     void innerError(MergeInnerObserver inner, Throwable t) {
/* 138 */       this.set.delete(inner);
/* 139 */       if (!this.delayErrors) {
/* 140 */         this.upstream.cancel();
/* 141 */         this.set.dispose();
/*     */         
/* 143 */         if (this.error.addThrowable(t)) {
/* 144 */           if (getAndSet(0) > 0) {
/* 145 */             this.downstream.onError(this.error.terminate());
/*     */           }
/*     */         } else {
/* 148 */           RxJavaPlugins.onError(t);
/*     */         }
/*     */       
/* 151 */       } else if (this.error.addThrowable(t)) {
/* 152 */         if (decrementAndGet() == 0) {
/* 153 */           this.downstream.onError(this.error.terminate());
/*     */         }
/* 155 */         else if (this.maxConcurrency != Integer.MAX_VALUE) {
/* 156 */           this.upstream.request(1L);
/*     */         } 
/*     */       } else {
/*     */         
/* 160 */         RxJavaPlugins.onError(t);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     void innerComplete(MergeInnerObserver inner) {
/* 166 */       this.set.delete(inner);
/* 167 */       if (decrementAndGet() == 0) {
/* 168 */         Throwable ex = (Throwable)this.error.get();
/* 169 */         if (ex != null) {
/* 170 */           this.downstream.onError(ex);
/*     */         } else {
/* 172 */           this.downstream.onComplete();
/*     */         }
/*     */       
/* 175 */       } else if (this.maxConcurrency != Integer.MAX_VALUE) {
/* 176 */         this.upstream.request(1L);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     final class MergeInnerObserver
/*     */       extends AtomicReference<Disposable>
/*     */       implements CompletableObserver, Disposable
/*     */     {
/*     */       private static final long serialVersionUID = 251330541679988317L;
/*     */       
/*     */       public void onSubscribe(Disposable d) {
/* 188 */         DisposableHelper.setOnce(this, d);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onError(Throwable e) {
/* 193 */         CompletableMerge.CompletableMergeSubscriber.this.innerError(this, e);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onComplete() {
/* 198 */         CompletableMerge.CompletableMergeSubscriber.this.innerComplete(this);
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean isDisposed() {
/* 203 */         return DisposableHelper.isDisposed(get());
/*     */       }
/*     */ 
/*     */       
/*     */       public void dispose() {
/* 208 */         DisposableHelper.dispose(this);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/completable/CompletableMerge.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */