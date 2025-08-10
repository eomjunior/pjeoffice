/*     */ package io.reactivex.internal.operators.maybe;
/*     */ 
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.MaybeObserver;
/*     */ import io.reactivex.MaybeSource;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
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
/*     */ public final class MaybeDelaySubscriptionOtherPublisher<T, U>
/*     */   extends AbstractMaybeWithUpstream<T, T>
/*     */ {
/*     */   final Publisher<U> other;
/*     */   
/*     */   public MaybeDelaySubscriptionOtherPublisher(MaybeSource<T> source, Publisher<U> other) {
/*  37 */     super(source);
/*  38 */     this.other = other;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(MaybeObserver<? super T> observer) {
/*  43 */     this.other.subscribe((Subscriber)new OtherSubscriber<T>(observer, this.source));
/*     */   }
/*     */   
/*     */   static final class OtherSubscriber<T>
/*     */     implements FlowableSubscriber<Object>, Disposable
/*     */   {
/*     */     final MaybeDelaySubscriptionOtherPublisher.DelayMaybeObserver<T> main;
/*     */     MaybeSource<T> source;
/*     */     Subscription upstream;
/*     */     
/*     */     OtherSubscriber(MaybeObserver<? super T> actual, MaybeSource<T> source) {
/*  54 */       this.main = new MaybeDelaySubscriptionOtherPublisher.DelayMaybeObserver<T>(actual);
/*  55 */       this.source = source;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/*  60 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/*  61 */         this.upstream = s;
/*     */         
/*  63 */         this.main.downstream.onSubscribe(this);
/*     */         
/*  65 */         s.request(Long.MAX_VALUE);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(Object t) {
/*  71 */       if (this.upstream != SubscriptionHelper.CANCELLED) {
/*  72 */         this.upstream.cancel();
/*  73 */         this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
/*     */         
/*  75 */         subscribeNext();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/*  81 */       if (this.upstream != SubscriptionHelper.CANCELLED) {
/*  82 */         this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
/*     */         
/*  84 */         this.main.downstream.onError(t);
/*     */       } else {
/*  86 */         RxJavaPlugins.onError(t);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*  92 */       if (this.upstream != SubscriptionHelper.CANCELLED) {
/*  93 */         this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
/*     */         
/*  95 */         subscribeNext();
/*     */       } 
/*     */     }
/*     */     
/*     */     void subscribeNext() {
/* 100 */       MaybeSource<T> src = this.source;
/* 101 */       this.source = null;
/*     */       
/* 103 */       src.subscribe(this.main);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 108 */       return DisposableHelper.isDisposed(this.main.get());
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 113 */       this.upstream.cancel();
/* 114 */       this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
/* 115 */       DisposableHelper.dispose(this.main);
/*     */     }
/*     */   }
/*     */   
/*     */   static final class DelayMaybeObserver<T>
/*     */     extends AtomicReference<Disposable>
/*     */     implements MaybeObserver<T>
/*     */   {
/*     */     private static final long serialVersionUID = 706635022205076709L;
/*     */     final MaybeObserver<? super T> downstream;
/*     */     
/*     */     DelayMaybeObserver(MaybeObserver<? super T> downstream) {
/* 127 */       this.downstream = downstream;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/* 132 */       DisposableHelper.setOnce(this, d);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSuccess(T value) {
/* 137 */       this.downstream.onSuccess(value);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/* 142 */       this.downstream.onError(e);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 147 */       this.downstream.onComplete();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/maybe/MaybeDelaySubscriptionOtherPublisher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */