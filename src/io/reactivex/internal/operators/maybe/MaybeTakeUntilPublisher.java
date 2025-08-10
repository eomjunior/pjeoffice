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
/*     */ 
/*     */ public final class MaybeTakeUntilPublisher<T, U>
/*     */   extends AbstractMaybeWithUpstream<T, T>
/*     */ {
/*     */   final Publisher<U> other;
/*     */   
/*     */   public MaybeTakeUntilPublisher(MaybeSource<T> source, Publisher<U> other) {
/*  38 */     super(source);
/*  39 */     this.other = other;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(MaybeObserver<? super T> observer) {
/*  44 */     TakeUntilMainMaybeObserver<T, U> parent = new TakeUntilMainMaybeObserver<T, U>(observer);
/*  45 */     observer.onSubscribe(parent);
/*     */     
/*  47 */     this.other.subscribe((Subscriber)parent.other);
/*     */     
/*  49 */     this.source.subscribe(parent);
/*     */   }
/*     */ 
/*     */   
/*     */   static final class TakeUntilMainMaybeObserver<T, U>
/*     */     extends AtomicReference<Disposable>
/*     */     implements MaybeObserver<T>, Disposable
/*     */   {
/*     */     private static final long serialVersionUID = -2187421758664251153L;
/*     */     final MaybeObserver<? super T> downstream;
/*     */     final TakeUntilOtherMaybeObserver<U> other;
/*     */     
/*     */     TakeUntilMainMaybeObserver(MaybeObserver<? super T> downstream) {
/*  62 */       this.downstream = downstream;
/*  63 */       this.other = new TakeUntilOtherMaybeObserver<U>(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/*  68 */       DisposableHelper.dispose(this);
/*  69 */       SubscriptionHelper.cancel(this.other);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/*  74 */       return DisposableHelper.isDisposed(get());
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  79 */       DisposableHelper.setOnce(this, d);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSuccess(T value) {
/*  84 */       SubscriptionHelper.cancel(this.other);
/*  85 */       if (getAndSet((Disposable)DisposableHelper.DISPOSED) != DisposableHelper.DISPOSED) {
/*  86 */         this.downstream.onSuccess(value);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/*  92 */       SubscriptionHelper.cancel(this.other);
/*  93 */       if (getAndSet((Disposable)DisposableHelper.DISPOSED) != DisposableHelper.DISPOSED) {
/*  94 */         this.downstream.onError(e);
/*     */       } else {
/*  96 */         RxJavaPlugins.onError(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 102 */       SubscriptionHelper.cancel(this.other);
/* 103 */       if (getAndSet((Disposable)DisposableHelper.DISPOSED) != DisposableHelper.DISPOSED) {
/* 104 */         this.downstream.onComplete();
/*     */       }
/*     */     }
/*     */     
/*     */     void otherError(Throwable e) {
/* 109 */       if (DisposableHelper.dispose(this)) {
/* 110 */         this.downstream.onError(e);
/*     */       } else {
/* 112 */         RxJavaPlugins.onError(e);
/*     */       } 
/*     */     }
/*     */     
/*     */     void otherComplete() {
/* 117 */       if (DisposableHelper.dispose(this)) {
/* 118 */         this.downstream.onComplete();
/*     */       }
/*     */     }
/*     */     
/*     */     static final class TakeUntilOtherMaybeObserver<U>
/*     */       extends AtomicReference<Subscription>
/*     */       implements FlowableSubscriber<U>
/*     */     {
/*     */       private static final long serialVersionUID = -1266041316834525931L;
/*     */       final MaybeTakeUntilPublisher.TakeUntilMainMaybeObserver<?, U> parent;
/*     */       
/*     */       TakeUntilOtherMaybeObserver(MaybeTakeUntilPublisher.TakeUntilMainMaybeObserver<?, U> parent) {
/* 130 */         this.parent = parent;
/*     */       }
/*     */ 
/*     */       
/*     */       public void onSubscribe(Subscription s) {
/* 135 */         SubscriptionHelper.setOnce(this, s, Long.MAX_VALUE);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onNext(Object value) {
/* 140 */         SubscriptionHelper.cancel(this);
/* 141 */         this.parent.otherComplete();
/*     */       }
/*     */ 
/*     */       
/*     */       public void onError(Throwable e) {
/* 146 */         this.parent.otherError(e);
/*     */       }
/*     */       
/*     */       public void onComplete()
/*     */       {
/* 151 */         this.parent.otherComplete(); } } } static final class TakeUntilOtherMaybeObserver<U> extends AtomicReference<Subscription> implements FlowableSubscriber<U> { public void onComplete() { this.parent.otherComplete(); }
/*     */ 
/*     */     
/*     */     private static final long serialVersionUID = -1266041316834525931L;
/*     */     final MaybeTakeUntilPublisher.TakeUntilMainMaybeObserver<?, U> parent;
/*     */     
/*     */     TakeUntilOtherMaybeObserver(MaybeTakeUntilPublisher.TakeUntilMainMaybeObserver<?, U> parent) {
/*     */       this.parent = parent;
/*     */     }
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/*     */       SubscriptionHelper.setOnce(this, s, Long.MAX_VALUE);
/*     */     }
/*     */     
/*     */     public void onNext(Object value) {
/*     */       SubscriptionHelper.cancel(this);
/*     */       this.parent.otherComplete();
/*     */     }
/*     */     
/*     */     public void onError(Throwable e) {
/*     */       this.parent.otherError(e);
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/maybe/MaybeTakeUntilPublisher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */