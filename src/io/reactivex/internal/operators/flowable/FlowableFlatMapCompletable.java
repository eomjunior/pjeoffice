/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.CompletableObserver;
/*     */ import io.reactivex.CompletableSource;
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.annotations.Nullable;
/*     */ import io.reactivex.disposables.CompositeDisposable;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.Function;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.subscriptions.BasicIntQueueSubscription;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.internal.util.AtomicThrowable;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class FlowableFlatMapCompletable<T>
/*     */   extends AbstractFlowableWithUpstream<T, T>
/*     */ {
/*     */   final Function<? super T, ? extends CompletableSource> mapper;
/*     */   final int maxConcurrency;
/*     */   final boolean delayErrors;
/*     */   
/*     */   public FlowableFlatMapCompletable(Flowable<T> source, Function<? super T, ? extends CompletableSource> mapper, boolean delayErrors, int maxConcurrency) {
/*  46 */     super(source);
/*  47 */     this.mapper = mapper;
/*  48 */     this.delayErrors = delayErrors;
/*  49 */     this.maxConcurrency = maxConcurrency;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super T> subscriber) {
/*  54 */     this.source.subscribe(new FlatMapCompletableMainSubscriber<T>(subscriber, this.mapper, this.delayErrors, this.maxConcurrency));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class FlatMapCompletableMainSubscriber<T>
/*     */     extends BasicIntQueueSubscription<T>
/*     */     implements FlowableSubscriber<T>
/*     */   {
/*     */     private static final long serialVersionUID = 8443155186132538303L;
/*     */     
/*     */     final Subscriber<? super T> downstream;
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
/*     */     volatile boolean cancelled;
/*     */     
/*     */     FlatMapCompletableMainSubscriber(Subscriber<? super T> subscriber, Function<? super T, ? extends CompletableSource> mapper, boolean delayErrors, int maxConcurrency) {
/*  80 */       this.downstream = subscriber;
/*  81 */       this.mapper = mapper;
/*  82 */       this.delayErrors = delayErrors;
/*  83 */       this.errors = new AtomicThrowable();
/*  84 */       this.set = new CompositeDisposable();
/*  85 */       this.maxConcurrency = maxConcurrency;
/*  86 */       lazySet(1);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/*  91 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/*  92 */         this.upstream = s;
/*     */         
/*  94 */         this.downstream.onSubscribe((Subscription)this);
/*     */         
/*  96 */         int m = this.maxConcurrency;
/*  97 */         if (m == Integer.MAX_VALUE) {
/*  98 */           s.request(Long.MAX_VALUE);
/*     */         } else {
/* 100 */           s.request(m);
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onNext(T value) {
/*     */       CompletableSource cs;
/*     */       try {
/* 110 */         cs = (CompletableSource)ObjectHelper.requireNonNull(this.mapper.apply(value), "The mapper returned a null CompletableSource");
/* 111 */       } catch (Throwable ex) {
/* 112 */         Exceptions.throwIfFatal(ex);
/* 113 */         this.upstream.cancel();
/* 114 */         onError(ex);
/*     */         
/*     */         return;
/*     */       } 
/* 118 */       getAndIncrement();
/*     */       
/* 120 */       InnerConsumer inner = new InnerConsumer();
/*     */       
/* 122 */       if (!this.cancelled && this.set.add(inner)) {
/* 123 */         cs.subscribe(inner);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/* 129 */       if (this.errors.addThrowable(e)) {
/* 130 */         if (this.delayErrors) {
/* 131 */           if (decrementAndGet() == 0) {
/* 132 */             Throwable ex = this.errors.terminate();
/* 133 */             this.downstream.onError(ex);
/*     */           }
/* 135 */           else if (this.maxConcurrency != Integer.MAX_VALUE) {
/* 136 */             this.upstream.request(1L);
/*     */           } 
/*     */         } else {
/*     */           
/* 140 */           cancel();
/* 141 */           if (getAndSet(0) > 0) {
/* 142 */             Throwable ex = this.errors.terminate();
/* 143 */             this.downstream.onError(ex);
/*     */           } 
/*     */         } 
/*     */       } else {
/* 147 */         RxJavaPlugins.onError(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 153 */       if (decrementAndGet() == 0) {
/* 154 */         Throwable ex = this.errors.terminate();
/* 155 */         if (ex != null) {
/* 156 */           this.downstream.onError(ex);
/*     */         } else {
/* 158 */           this.downstream.onComplete();
/*     */         }
/*     */       
/* 161 */       } else if (this.maxConcurrency != Integer.MAX_VALUE) {
/* 162 */         this.upstream.request(1L);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 169 */       this.cancelled = true;
/* 170 */       this.upstream.cancel();
/* 171 */       this.set.dispose();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void request(long n) {}
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public T poll() throws Exception {
/* 182 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 187 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void clear() {}
/*     */ 
/*     */ 
/*     */     
/*     */     public int requestFusion(int mode) {
/* 197 */       return mode & 0x2;
/*     */     }
/*     */     
/*     */     void innerComplete(InnerConsumer inner) {
/* 201 */       this.set.delete(inner);
/* 202 */       onComplete();
/*     */     }
/*     */     
/*     */     void innerError(InnerConsumer inner, Throwable e) {
/* 206 */       this.set.delete(inner);
/* 207 */       onError(e);
/*     */     }
/*     */     
/*     */     final class InnerConsumer
/*     */       extends AtomicReference<Disposable> implements CompletableObserver, Disposable {
/*     */       private static final long serialVersionUID = 8606673141535671828L;
/*     */       
/*     */       public void onSubscribe(Disposable d) {
/* 215 */         DisposableHelper.setOnce(this, d);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onComplete() {
/* 220 */         FlowableFlatMapCompletable.FlatMapCompletableMainSubscriber.this.innerComplete(this);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onError(Throwable e) {
/* 225 */         FlowableFlatMapCompletable.FlatMapCompletableMainSubscriber.this.innerError(this, e);
/*     */       }
/*     */ 
/*     */       
/*     */       public void dispose() {
/* 230 */         DisposableHelper.dispose(this);
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean isDisposed() {
/* 235 */         return DisposableHelper.isDisposed(get());
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableFlatMapCompletable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */