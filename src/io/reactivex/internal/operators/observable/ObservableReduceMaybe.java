/*     */ package io.reactivex.internal.operators.observable;
/*     */ 
/*     */ import io.reactivex.Maybe;
/*     */ import io.reactivex.MaybeObserver;
/*     */ import io.reactivex.ObservableSource;
/*     */ import io.reactivex.Observer;
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
/*     */ public final class ObservableReduceMaybe<T>
/*     */   extends Maybe<T>
/*     */ {
/*     */   final ObservableSource<T> source;
/*     */   final BiFunction<T, T, T> reducer;
/*     */   
/*     */   public ObservableReduceMaybe(ObservableSource<T> source, BiFunction<T, T, T> reducer) {
/*  37 */     this.source = source;
/*  38 */     this.reducer = reducer;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(MaybeObserver<? super T> observer) {
/*  43 */     this.source.subscribe(new ReduceObserver<T>(observer, this.reducer));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class ReduceObserver<T>
/*     */     implements Observer<T>, Disposable
/*     */   {
/*     */     final MaybeObserver<? super T> downstream;
/*     */     
/*     */     final BiFunction<T, T, T> reducer;
/*     */     
/*     */     boolean done;
/*     */     T value;
/*     */     Disposable upstream;
/*     */     
/*     */     ReduceObserver(MaybeObserver<? super T> observer, BiFunction<T, T, T> reducer) {
/*  59 */       this.downstream = observer;
/*  60 */       this.reducer = reducer;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  65 */       if (DisposableHelper.validate(this.upstream, d)) {
/*  66 */         this.upstream = d;
/*     */         
/*  68 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T value) {
/*  74 */       if (!this.done) {
/*  75 */         T v = this.value;
/*     */         
/*  77 */         if (v == null) {
/*  78 */           this.value = value;
/*     */         } else {
/*     */           try {
/*  81 */             this.value = (T)ObjectHelper.requireNonNull(this.reducer.apply(v, value), "The reducer returned a null value");
/*  82 */           } catch (Throwable ex) {
/*  83 */             Exceptions.throwIfFatal(ex);
/*  84 */             this.upstream.dispose();
/*  85 */             onError(ex);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/*  93 */       if (this.done) {
/*  94 */         RxJavaPlugins.onError(e);
/*     */         return;
/*     */       } 
/*  97 */       this.done = true;
/*  98 */       this.value = null;
/*  99 */       this.downstream.onError(e);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 104 */       if (this.done) {
/*     */         return;
/*     */       }
/* 107 */       this.done = true;
/* 108 */       T v = this.value;
/* 109 */       this.value = null;
/* 110 */       if (v != null) {
/* 111 */         this.downstream.onSuccess(v);
/*     */       } else {
/* 113 */         this.downstream.onComplete();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 119 */       this.upstream.dispose();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 124 */       return this.upstream.isDisposed();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableReduceMaybe.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */