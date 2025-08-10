/*     */ package io.reactivex.internal.operators.single;
/*     */ 
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.Single;
/*     */ import io.reactivex.SingleObserver;
/*     */ import io.reactivex.SingleSource;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.observers.ResumeSingleObserver;
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
/*     */ public final class SingleDelayWithPublisher<T, U>
/*     */   extends Single<T>
/*     */ {
/*     */   final SingleSource<T> source;
/*     */   final Publisher<U> other;
/*     */   
/*     */   public SingleDelayWithPublisher(SingleSource<T> source, Publisher<U> other) {
/*  34 */     this.source = source;
/*  35 */     this.other = other;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(SingleObserver<? super T> observer) {
/*  40 */     this.other.subscribe((Subscriber)new OtherSubscriber<T, Object>(observer, this.source));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class OtherSubscriber<T, U>
/*     */     extends AtomicReference<Disposable>
/*     */     implements FlowableSubscriber<U>, Disposable
/*     */   {
/*     */     private static final long serialVersionUID = -8565274649390031272L;
/*     */     
/*     */     final SingleObserver<? super T> downstream;
/*     */     
/*     */     final SingleSource<T> source;
/*     */     
/*     */     boolean done;
/*     */     Subscription upstream;
/*     */     
/*     */     OtherSubscriber(SingleObserver<? super T> actual, SingleSource<T> source) {
/*  58 */       this.downstream = actual;
/*  59 */       this.source = source;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/*  64 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/*  65 */         this.upstream = s;
/*     */         
/*  67 */         this.downstream.onSubscribe(this);
/*     */         
/*  69 */         s.request(Long.MAX_VALUE);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(U value) {
/*  75 */       this.upstream.cancel();
/*  76 */       onComplete();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/*  81 */       if (this.done) {
/*  82 */         RxJavaPlugins.onError(e);
/*     */         return;
/*     */       } 
/*  85 */       this.done = true;
/*  86 */       this.downstream.onError(e);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*  91 */       if (this.done) {
/*     */         return;
/*     */       }
/*  94 */       this.done = true;
/*  95 */       this.source.subscribe((SingleObserver)new ResumeSingleObserver(this, this.downstream));
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 100 */       this.upstream.cancel();
/* 101 */       DisposableHelper.dispose(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 106 */       return DisposableHelper.isDisposed(get());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/single/SingleDelayWithPublisher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */