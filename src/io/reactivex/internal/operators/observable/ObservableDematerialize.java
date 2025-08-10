/*     */ package io.reactivex.internal.operators.observable;
/*     */ 
/*     */ import io.reactivex.Notification;
/*     */ import io.reactivex.ObservableSource;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.Function;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
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
/*     */ public final class ObservableDematerialize<T, R>
/*     */   extends AbstractObservableWithUpstream<T, R>
/*     */ {
/*     */   final Function<? super T, ? extends Notification<R>> selector;
/*     */   
/*     */   public ObservableDematerialize(ObservableSource<T> source, Function<? super T, ? extends Notification<R>> selector) {
/*  29 */     super(source);
/*  30 */     this.selector = selector;
/*     */   }
/*     */ 
/*     */   
/*     */   public void subscribeActual(Observer<? super R> observer) {
/*  35 */     this.source.subscribe(new DematerializeObserver<T, R>(observer, this.selector));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class DematerializeObserver<T, R>
/*     */     implements Observer<T>, Disposable
/*     */   {
/*     */     final Observer<? super R> downstream;
/*     */     final Function<? super T, ? extends Notification<R>> selector;
/*     */     boolean done;
/*     */     Disposable upstream;
/*     */     
/*     */     DematerializeObserver(Observer<? super R> downstream, Function<? super T, ? extends Notification<R>> selector) {
/*  48 */       this.downstream = downstream;
/*  49 */       this.selector = selector;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  54 */       if (DisposableHelper.validate(this.upstream, d)) {
/*  55 */         this.upstream = d;
/*     */         
/*  57 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/*  63 */       this.upstream.dispose();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/*  68 */       return this.upstream.isDisposed();
/*     */     }
/*     */     
/*     */     public void onNext(T item) {
/*     */       Notification<R> notification;
/*  73 */       if (this.done) {
/*  74 */         if (item instanceof Notification) {
/*  75 */           notification = (Notification)item;
/*  76 */           if (notification.isOnError()) {
/*  77 */             RxJavaPlugins.onError(notification.getError());
/*     */           }
/*     */         } 
/*     */ 
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/*     */       try {
/*  86 */         notification = (Notification<R>)ObjectHelper.requireNonNull(this.selector.apply(item), "The selector returned a null Notification");
/*  87 */       } catch (Throwable ex) {
/*  88 */         Exceptions.throwIfFatal(ex);
/*  89 */         this.upstream.dispose();
/*  90 */         onError(ex);
/*     */         return;
/*     */       } 
/*  93 */       if (notification.isOnError()) {
/*  94 */         this.upstream.dispose();
/*  95 */         onError(notification.getError());
/*     */       }
/*  97 */       else if (notification.isOnComplete()) {
/*  98 */         this.upstream.dispose();
/*  99 */         onComplete();
/*     */       } else {
/* 101 */         this.downstream.onNext(notification.getValue());
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 107 */       if (this.done) {
/* 108 */         RxJavaPlugins.onError(t);
/*     */         return;
/*     */       } 
/* 111 */       this.done = true;
/*     */       
/* 113 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 118 */       if (this.done) {
/*     */         return;
/*     */       }
/* 121 */       this.done = true;
/*     */       
/* 123 */       this.downstream.onComplete();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableDematerialize.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */