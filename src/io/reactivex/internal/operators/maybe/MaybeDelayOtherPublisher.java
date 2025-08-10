/*     */ package io.reactivex.internal.operators.maybe;
/*     */ 
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.MaybeObserver;
/*     */ import io.reactivex.MaybeSource;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.CompositeException;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
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
/*     */ public final class MaybeDelayOtherPublisher<T, U>
/*     */   extends AbstractMaybeWithUpstream<T, T>
/*     */ {
/*     */   final Publisher<U> other;
/*     */   
/*     */   public MaybeDelayOtherPublisher(MaybeSource<T> source, Publisher<U> other) {
/*  37 */     super(source);
/*  38 */     this.other = other;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(MaybeObserver<? super T> observer) {
/*  43 */     this.source.subscribe(new DelayMaybeObserver<T, U>(observer, this.other));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class DelayMaybeObserver<T, U>
/*     */     implements MaybeObserver<T>, Disposable
/*     */   {
/*     */     final MaybeDelayOtherPublisher.OtherSubscriber<T> other;
/*     */     final Publisher<U> otherSource;
/*     */     Disposable upstream;
/*     */     
/*     */     DelayMaybeObserver(MaybeObserver<? super T> actual, Publisher<U> otherSource) {
/*  55 */       this.other = new MaybeDelayOtherPublisher.OtherSubscriber<T>(actual);
/*  56 */       this.otherSource = otherSource;
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/*  61 */       this.upstream.dispose();
/*  62 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/*  63 */       SubscriptionHelper.cancel(this.other);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/*  68 */       return (this.other.get() == SubscriptionHelper.CANCELLED);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  73 */       if (DisposableHelper.validate(this.upstream, d)) {
/*  74 */         this.upstream = d;
/*     */         
/*  76 */         this.other.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSuccess(T value) {
/*  82 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/*  83 */       this.other.value = value;
/*  84 */       subscribeNext();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/*  89 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/*  90 */       this.other.error = e;
/*  91 */       subscribeNext();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*  96 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/*  97 */       subscribeNext();
/*     */     }
/*     */     
/*     */     void subscribeNext() {
/* 101 */       this.otherSource.subscribe((Subscriber)this.other);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class OtherSubscriber<T>
/*     */     extends AtomicReference<Subscription>
/*     */     implements FlowableSubscriber<Object>
/*     */   {
/*     */     private static final long serialVersionUID = -1215060610805418006L;
/*     */     
/*     */     final MaybeObserver<? super T> downstream;
/*     */     
/*     */     T value;
/*     */     Throwable error;
/*     */     
/*     */     OtherSubscriber(MaybeObserver<? super T> downstream) {
/* 118 */       this.downstream = downstream;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/* 123 */       SubscriptionHelper.setOnce(this, s, Long.MAX_VALUE);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(Object t) {
/* 128 */       Subscription s = get();
/* 129 */       if (s != SubscriptionHelper.CANCELLED) {
/* 130 */         lazySet((Subscription)SubscriptionHelper.CANCELLED);
/* 131 */         s.cancel();
/* 132 */         onComplete();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 138 */       Throwable e = this.error;
/* 139 */       if (e == null) {
/* 140 */         this.downstream.onError(t);
/*     */       } else {
/* 142 */         this.downstream.onError((Throwable)new CompositeException(new Throwable[] { e, t }));
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 148 */       Throwable e = this.error;
/* 149 */       if (e != null) {
/* 150 */         this.downstream.onError(e);
/*     */       } else {
/* 152 */         T v = this.value;
/* 153 */         if (v != null) {
/* 154 */           this.downstream.onSuccess(v);
/*     */         } else {
/* 156 */           this.downstream.onComplete();
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/maybe/MaybeDelayOtherPublisher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */