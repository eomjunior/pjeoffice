/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.Single;
/*     */ import io.reactivex.SingleObserver;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.internal.disposables.EmptyDisposable;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.fuseable.FuseToFlowable;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.internal.util.ArrayListSupplier;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.Collection;
/*     */ import java.util.concurrent.Callable;
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
/*     */ public final class FlowableToListSingle<T, U extends Collection<? super T>>
/*     */   extends Single<U>
/*     */   implements FuseToFlowable<U>
/*     */ {
/*     */   final Flowable<T> source;
/*     */   final Callable<U> collectionSupplier;
/*     */   
/*     */   public FlowableToListSingle(Flowable<T> source) {
/*  39 */     this(source, ArrayListSupplier.asCallable());
/*     */   }
/*     */   
/*     */   public FlowableToListSingle(Flowable<T> source, Callable<U> collectionSupplier) {
/*  43 */     this.source = source;
/*  44 */     this.collectionSupplier = collectionSupplier;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(SingleObserver<? super U> observer) {
/*     */     Collection collection;
/*     */     try {
/*  51 */       collection = (Collection)ObjectHelper.requireNonNull(this.collectionSupplier.call(), "The collectionSupplier returned a null collection. Null values are generally not allowed in 2.x operators and sources.");
/*  52 */     } catch (Throwable e) {
/*  53 */       Exceptions.throwIfFatal(e);
/*  54 */       EmptyDisposable.error(e, observer);
/*     */       return;
/*     */     } 
/*  57 */     this.source.subscribe(new ToListSubscriber<Object, U>(observer, (U)collection));
/*     */   }
/*     */ 
/*     */   
/*     */   public Flowable<U> fuseToFlowable() {
/*  62 */     return RxJavaPlugins.onAssembly(new FlowableToList<T, U>(this.source, this.collectionSupplier));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class ToListSubscriber<T, U extends Collection<? super T>>
/*     */     implements FlowableSubscriber<T>, Disposable
/*     */   {
/*     */     final SingleObserver<? super U> downstream;
/*     */     
/*     */     Subscription upstream;
/*     */     U value;
/*     */     
/*     */     ToListSubscriber(SingleObserver<? super U> actual, U collection) {
/*  75 */       this.downstream = actual;
/*  76 */       this.value = collection;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/*  81 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/*  82 */         this.upstream = s;
/*  83 */         this.downstream.onSubscribe(this);
/*  84 */         s.request(Long.MAX_VALUE);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*  90 */       this.value.add(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/*  95 */       this.value = null;
/*  96 */       this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
/*  97 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 102 */       this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
/* 103 */       this.downstream.onSuccess(this.value);
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 108 */       this.upstream.cancel();
/* 109 */       this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 114 */       return (this.upstream == SubscriptionHelper.CANCELLED);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableToListSingle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */