/*     */ package io.reactivex.internal.operators.maybe;
/*     */ 
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.MaybeObserver;
/*     */ import io.reactivex.MaybeSource;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.concurrent.TimeoutException;
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
/*     */ 
/*     */ public final class MaybeTimeoutPublisher<T, U>
/*     */   extends AbstractMaybeWithUpstream<T, T>
/*     */ {
/*     */   final Publisher<U> other;
/*     */   final MaybeSource<? extends T> fallback;
/*     */   
/*     */   public MaybeTimeoutPublisher(MaybeSource<T> source, Publisher<U> other, MaybeSource<? extends T> fallback) {
/*  41 */     super(source);
/*  42 */     this.other = other;
/*  43 */     this.fallback = fallback;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(MaybeObserver<? super T> observer) {
/*  48 */     TimeoutMainMaybeObserver<T, U> parent = new TimeoutMainMaybeObserver<T, U>(observer, this.fallback);
/*  49 */     observer.onSubscribe(parent);
/*     */     
/*  51 */     this.other.subscribe((Subscriber)parent.other);
/*     */     
/*  53 */     this.source.subscribe(parent);
/*     */   }
/*     */ 
/*     */   
/*     */   static final class TimeoutMainMaybeObserver<T, U>
/*     */     extends AtomicReference<Disposable>
/*     */     implements MaybeObserver<T>, Disposable
/*     */   {
/*     */     private static final long serialVersionUID = -5955289211445418871L;
/*     */     
/*     */     final MaybeObserver<? super T> downstream;
/*     */     
/*     */     final MaybeTimeoutPublisher.TimeoutOtherMaybeObserver<T, U> other;
/*     */     
/*     */     final MaybeSource<? extends T> fallback;
/*     */     final MaybeTimeoutPublisher.TimeoutFallbackMaybeObserver<T> otherObserver;
/*     */     
/*     */     TimeoutMainMaybeObserver(MaybeObserver<? super T> actual, MaybeSource<? extends T> fallback) {
/*  71 */       this.downstream = actual;
/*  72 */       this.other = new MaybeTimeoutPublisher.TimeoutOtherMaybeObserver<T, U>(this);
/*  73 */       this.fallback = fallback;
/*  74 */       this.otherObserver = (fallback != null) ? new MaybeTimeoutPublisher.TimeoutFallbackMaybeObserver<T>(actual) : null;
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/*  79 */       DisposableHelper.dispose(this);
/*  80 */       SubscriptionHelper.cancel(this.other);
/*  81 */       MaybeTimeoutPublisher.TimeoutFallbackMaybeObserver<T> oo = this.otherObserver;
/*  82 */       if (oo != null) {
/*  83 */         DisposableHelper.dispose(oo);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/*  89 */       return DisposableHelper.isDisposed(get());
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  94 */       DisposableHelper.setOnce(this, d);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSuccess(T value) {
/*  99 */       SubscriptionHelper.cancel(this.other);
/* 100 */       if (getAndSet((Disposable)DisposableHelper.DISPOSED) != DisposableHelper.DISPOSED) {
/* 101 */         this.downstream.onSuccess(value);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/* 107 */       SubscriptionHelper.cancel(this.other);
/* 108 */       if (getAndSet((Disposable)DisposableHelper.DISPOSED) != DisposableHelper.DISPOSED) {
/* 109 */         this.downstream.onError(e);
/*     */       } else {
/* 111 */         RxJavaPlugins.onError(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 117 */       SubscriptionHelper.cancel(this.other);
/* 118 */       if (getAndSet((Disposable)DisposableHelper.DISPOSED) != DisposableHelper.DISPOSED) {
/* 119 */         this.downstream.onComplete();
/*     */       }
/*     */     }
/*     */     
/*     */     public void otherError(Throwable e) {
/* 124 */       if (DisposableHelper.dispose(this)) {
/* 125 */         this.downstream.onError(e);
/*     */       } else {
/* 127 */         RxJavaPlugins.onError(e);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void otherComplete() {
/* 132 */       if (DisposableHelper.dispose(this)) {
/* 133 */         if (this.fallback == null) {
/* 134 */           this.downstream.onError(new TimeoutException());
/*     */         } else {
/* 136 */           this.fallback.subscribe(this.otherObserver);
/*     */         } 
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class TimeoutOtherMaybeObserver<T, U>
/*     */     extends AtomicReference<Subscription>
/*     */     implements FlowableSubscriber<Object>
/*     */   {
/*     */     private static final long serialVersionUID = 8663801314800248617L;
/*     */     final MaybeTimeoutPublisher.TimeoutMainMaybeObserver<T, U> parent;
/*     */     
/*     */     TimeoutOtherMaybeObserver(MaybeTimeoutPublisher.TimeoutMainMaybeObserver<T, U> parent) {
/* 151 */       this.parent = parent;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/* 156 */       SubscriptionHelper.setOnce(this, s, Long.MAX_VALUE);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(Object value) {
/* 161 */       get().cancel();
/* 162 */       this.parent.otherComplete();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/* 167 */       this.parent.otherError(e);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 172 */       this.parent.otherComplete();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class TimeoutFallbackMaybeObserver<T>
/*     */     extends AtomicReference<Disposable>
/*     */     implements MaybeObserver<T>
/*     */   {
/*     */     private static final long serialVersionUID = 8663801314800248617L;
/*     */     final MaybeObserver<? super T> downstream;
/*     */     
/*     */     TimeoutFallbackMaybeObserver(MaybeObserver<? super T> downstream) {
/* 185 */       this.downstream = downstream;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/* 190 */       DisposableHelper.setOnce(this, d);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSuccess(T value) {
/* 195 */       this.downstream.onSuccess(value);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/* 200 */       this.downstream.onError(e);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 205 */       this.downstream.onComplete();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/maybe/MaybeTimeoutPublisher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */