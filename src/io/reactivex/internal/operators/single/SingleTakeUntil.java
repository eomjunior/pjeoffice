/*     */ package io.reactivex.internal.operators.single;
/*     */ 
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.Single;
/*     */ import io.reactivex.SingleObserver;
/*     */ import io.reactivex.SingleSource;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.concurrent.CancellationException;
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
/*     */ public final class SingleTakeUntil<T, U>
/*     */   extends Single<T>
/*     */ {
/*     */   final SingleSource<T> source;
/*     */   final Publisher<U> other;
/*     */   
/*     */   public SingleTakeUntil(SingleSource<T> source, Publisher<U> other) {
/*  40 */     this.source = source;
/*  41 */     this.other = other;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(SingleObserver<? super T> observer) {
/*  46 */     TakeUntilMainObserver<T> parent = new TakeUntilMainObserver<T>(observer);
/*  47 */     observer.onSubscribe(parent);
/*     */     
/*  49 */     this.other.subscribe((Subscriber)parent.other);
/*     */     
/*  51 */     this.source.subscribe(parent);
/*     */   }
/*     */ 
/*     */   
/*     */   static final class TakeUntilMainObserver<T>
/*     */     extends AtomicReference<Disposable>
/*     */     implements SingleObserver<T>, Disposable
/*     */   {
/*     */     private static final long serialVersionUID = -622603812305745221L;
/*     */     
/*     */     final SingleObserver<? super T> downstream;
/*     */     final SingleTakeUntil.TakeUntilOtherSubscriber other;
/*     */     
/*     */     TakeUntilMainObserver(SingleObserver<? super T> downstream) {
/*  65 */       this.downstream = downstream;
/*  66 */       this.other = new SingleTakeUntil.TakeUntilOtherSubscriber(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/*  71 */       DisposableHelper.dispose(this);
/*  72 */       this.other.dispose();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/*  77 */       return DisposableHelper.isDisposed(get());
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  82 */       DisposableHelper.setOnce(this, d);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSuccess(T value) {
/*  87 */       this.other.dispose();
/*     */       
/*  89 */       Disposable a = getAndSet((Disposable)DisposableHelper.DISPOSED);
/*  90 */       if (a != DisposableHelper.DISPOSED) {
/*  91 */         this.downstream.onSuccess(value);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/*  97 */       this.other.dispose();
/*     */       
/*  99 */       Disposable a = get();
/* 100 */       if (a != DisposableHelper.DISPOSED) {
/* 101 */         a = getAndSet((Disposable)DisposableHelper.DISPOSED);
/* 102 */         if (a != DisposableHelper.DISPOSED) {
/* 103 */           this.downstream.onError(e);
/*     */           return;
/*     */         } 
/*     */       } 
/* 107 */       RxJavaPlugins.onError(e);
/*     */     }
/*     */     
/*     */     void otherError(Throwable e) {
/* 111 */       Disposable a = get();
/* 112 */       if (a != DisposableHelper.DISPOSED) {
/* 113 */         a = getAndSet((Disposable)DisposableHelper.DISPOSED);
/* 114 */         if (a != DisposableHelper.DISPOSED) {
/* 115 */           if (a != null) {
/* 116 */             a.dispose();
/*     */           }
/* 118 */           this.downstream.onError(e);
/*     */           return;
/*     */         } 
/*     */       } 
/* 122 */       RxJavaPlugins.onError(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class TakeUntilOtherSubscriber
/*     */     extends AtomicReference<Subscription>
/*     */     implements FlowableSubscriber<Object>
/*     */   {
/*     */     private static final long serialVersionUID = 5170026210238877381L;
/*     */     final SingleTakeUntil.TakeUntilMainObserver<?> parent;
/*     */     
/*     */     TakeUntilOtherSubscriber(SingleTakeUntil.TakeUntilMainObserver<?> parent) {
/* 135 */       this.parent = parent;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/* 140 */       SubscriptionHelper.setOnce(this, s, Long.MAX_VALUE);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(Object t) {
/* 145 */       if (SubscriptionHelper.cancel(this)) {
/* 146 */         this.parent.otherError(new CancellationException());
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 152 */       this.parent.otherError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 157 */       if (get() != SubscriptionHelper.CANCELLED) {
/* 158 */         lazySet((Subscription)SubscriptionHelper.CANCELLED);
/* 159 */         this.parent.otherError(new CancellationException());
/*     */       } 
/*     */     }
/*     */     
/*     */     public void dispose() {
/* 164 */       SubscriptionHelper.cancel(this);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/single/SingleTakeUntil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */