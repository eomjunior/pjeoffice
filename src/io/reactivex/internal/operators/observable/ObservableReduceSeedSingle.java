/*     */ package io.reactivex.internal.operators.observable;
/*     */ 
/*     */ import io.reactivex.ObservableSource;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.Single;
/*     */ import io.reactivex.SingleObserver;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.BiFunction;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ObservableReduceSeedSingle<T, R>
/*     */   extends Single<R>
/*     */ {
/*     */   final ObservableSource<T> source;
/*     */   final R seed;
/*     */   final BiFunction<R, ? super T, R> reducer;
/*     */   
/*     */   public ObservableReduceSeedSingle(ObservableSource<T> source, R seed, BiFunction<R, ? super T, R> reducer) {
/*  40 */     this.source = source;
/*  41 */     this.seed = seed;
/*  42 */     this.reducer = reducer;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(SingleObserver<? super R> observer) {
/*  47 */     this.source.subscribe(new ReduceSeedObserver<T, R>(observer, this.reducer, this.seed));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class ReduceSeedObserver<T, R>
/*     */     implements Observer<T>, Disposable
/*     */   {
/*     */     final SingleObserver<? super R> downstream;
/*     */     
/*     */     final BiFunction<R, ? super T, R> reducer;
/*     */     R value;
/*     */     Disposable upstream;
/*     */     
/*     */     ReduceSeedObserver(SingleObserver<? super R> actual, BiFunction<R, ? super T, R> reducer, R value) {
/*  61 */       this.downstream = actual;
/*  62 */       this.value = value;
/*  63 */       this.reducer = reducer;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  68 */       if (DisposableHelper.validate(this.upstream, d)) {
/*  69 */         this.upstream = d;
/*     */         
/*  71 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T value) {
/*  77 */       R v = this.value;
/*  78 */       if (v != null) {
/*     */         try {
/*  80 */           this.value = (R)ObjectHelper.requireNonNull(this.reducer.apply(v, value), "The reducer returned a null value");
/*  81 */         } catch (Throwable ex) {
/*  82 */           Exceptions.throwIfFatal(ex);
/*  83 */           this.upstream.dispose();
/*  84 */           onError(ex);
/*     */         } 
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/*  91 */       R v = this.value;
/*  92 */       if (v != null) {
/*  93 */         this.value = null;
/*  94 */         this.downstream.onError(e);
/*     */       } else {
/*  96 */         RxJavaPlugins.onError(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 102 */       R v = this.value;
/* 103 */       if (v != null) {
/* 104 */         this.value = null;
/* 105 */         this.downstream.onSuccess(v);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 111 */       this.upstream.dispose();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 116 */       return this.upstream.isDisposed();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableReduceSeedSingle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */