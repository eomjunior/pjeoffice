/*     */ package io.reactivex.internal.operators.single;
/*     */ 
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.Single;
/*     */ import io.reactivex.SingleObserver;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.NoSuchElementException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class SingleFromPublisher<T>
/*     */   extends Single<T>
/*     */ {
/*     */   final Publisher<? extends T> publisher;
/*     */   
/*     */   public SingleFromPublisher(Publisher<? extends T> publisher) {
/*  30 */     this.publisher = publisher;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(SingleObserver<? super T> observer) {
/*  35 */     this.publisher.subscribe((Subscriber)new ToSingleObserver<T>(observer));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class ToSingleObserver<T>
/*     */     implements FlowableSubscriber<T>, Disposable
/*     */   {
/*     */     final SingleObserver<? super T> downstream;
/*     */     
/*     */     Subscription upstream;
/*     */     T value;
/*     */     boolean done;
/*     */     volatile boolean disposed;
/*     */     
/*     */     ToSingleObserver(SingleObserver<? super T> downstream) {
/*  50 */       this.downstream = downstream;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/*  55 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/*  56 */         this.upstream = s;
/*     */         
/*  58 */         this.downstream.onSubscribe(this);
/*     */         
/*  60 */         s.request(Long.MAX_VALUE);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*  66 */       if (this.done) {
/*     */         return;
/*     */       }
/*  69 */       if (this.value != null) {
/*  70 */         this.upstream.cancel();
/*  71 */         this.done = true;
/*  72 */         this.value = null;
/*  73 */         this.downstream.onError(new IndexOutOfBoundsException("Too many elements in the Publisher"));
/*     */       } else {
/*  75 */         this.value = t;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/*  81 */       if (this.done) {
/*  82 */         RxJavaPlugins.onError(t);
/*     */         return;
/*     */       } 
/*  85 */       this.done = true;
/*  86 */       this.value = null;
/*  87 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*  92 */       if (this.done) {
/*     */         return;
/*     */       }
/*  95 */       this.done = true;
/*  96 */       T v = this.value;
/*  97 */       this.value = null;
/*  98 */       if (v == null) {
/*  99 */         this.downstream.onError(new NoSuchElementException("The source Publisher is empty"));
/*     */       } else {
/* 101 */         this.downstream.onSuccess(v);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 107 */       return this.disposed;
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 112 */       this.disposed = true;
/* 113 */       this.upstream.cancel();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/single/SingleFromPublisher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */