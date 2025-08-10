/*     */ package io.reactivex.internal.operators.single;
/*     */ 
/*     */ import io.reactivex.Maybe;
/*     */ import io.reactivex.MaybeObserver;
/*     */ import io.reactivex.Notification;
/*     */ import io.reactivex.Single;
/*     */ import io.reactivex.SingleObserver;
/*     */ import io.reactivex.annotations.Experimental;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.Function;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
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
/*     */ @Experimental
/*     */ public final class SingleDematerialize<T, R>
/*     */   extends Maybe<R>
/*     */ {
/*     */   final Single<T> source;
/*     */   final Function<? super T, Notification<R>> selector;
/*     */   
/*     */   public SingleDematerialize(Single<T> source, Function<? super T, Notification<R>> selector) {
/*  39 */     this.source = source;
/*  40 */     this.selector = selector;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(MaybeObserver<? super R> observer) {
/*  45 */     this.source.subscribe(new DematerializeObserver<T, R>(observer, this.selector));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class DematerializeObserver<T, R>
/*     */     implements SingleObserver<T>, Disposable
/*     */   {
/*     */     final MaybeObserver<? super R> downstream;
/*     */     
/*     */     final Function<? super T, Notification<R>> selector;
/*     */     Disposable upstream;
/*     */     
/*     */     DematerializeObserver(MaybeObserver<? super R> downstream, Function<? super T, Notification<R>> selector) {
/*  58 */       this.downstream = downstream;
/*  59 */       this.selector = selector;
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/*  64 */       this.upstream.dispose();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/*  69 */       return this.upstream.isDisposed();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  74 */       if (DisposableHelper.validate(this.upstream, d)) {
/*  75 */         this.upstream = d;
/*  76 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onSuccess(T t) {
/*     */       Notification<R> notification;
/*     */       try {
/*  85 */         notification = (Notification<R>)ObjectHelper.requireNonNull(this.selector.apply(t), "The selector returned a null Notification");
/*  86 */       } catch (Throwable ex) {
/*  87 */         Exceptions.throwIfFatal(ex);
/*  88 */         this.downstream.onError(ex);
/*     */         return;
/*     */       } 
/*  91 */       if (notification.isOnNext()) {
/*  92 */         this.downstream.onSuccess(notification.getValue());
/*  93 */       } else if (notification.isOnComplete()) {
/*  94 */         this.downstream.onComplete();
/*     */       } else {
/*  96 */         this.downstream.onError(notification.getError());
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/* 102 */       this.downstream.onError(e);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/single/SingleDematerialize.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */