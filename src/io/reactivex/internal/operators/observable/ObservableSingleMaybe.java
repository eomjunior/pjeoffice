/*     */ package io.reactivex.internal.operators.observable;
/*     */ 
/*     */ import io.reactivex.Maybe;
/*     */ import io.reactivex.MaybeObserver;
/*     */ import io.reactivex.ObservableSource;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
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
/*     */ public final class ObservableSingleMaybe<T>
/*     */   extends Maybe<T>
/*     */ {
/*     */   final ObservableSource<T> source;
/*     */   
/*     */   public ObservableSingleMaybe(ObservableSource<T> source) {
/*  26 */     this.source = source;
/*     */   }
/*     */ 
/*     */   
/*     */   public void subscribeActual(MaybeObserver<? super T> t) {
/*  31 */     this.source.subscribe(new SingleElementObserver<T>(t));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class SingleElementObserver<T>
/*     */     implements Observer<T>, Disposable
/*     */   {
/*     */     final MaybeObserver<? super T> downstream;
/*     */     Disposable upstream;
/*     */     T value;
/*     */     boolean done;
/*     */     
/*     */     SingleElementObserver(MaybeObserver<? super T> downstream) {
/*  44 */       this.downstream = downstream;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  49 */       if (DisposableHelper.validate(this.upstream, d)) {
/*  50 */         this.upstream = d;
/*  51 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/*  57 */       this.upstream.dispose();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/*  62 */       return this.upstream.isDisposed();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*  67 */       if (this.done) {
/*     */         return;
/*     */       }
/*  70 */       if (this.value != null) {
/*  71 */         this.done = true;
/*  72 */         this.upstream.dispose();
/*  73 */         this.downstream.onError(new IllegalArgumentException("Sequence contains more than one element!"));
/*     */         return;
/*     */       } 
/*  76 */       this.value = t;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/*  81 */       if (this.done) {
/*  82 */         RxJavaPlugins.onError(t);
/*     */         return;
/*     */       } 
/*  85 */       this.done = true;
/*  86 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*  91 */       if (this.done) {
/*     */         return;
/*     */       }
/*  94 */       this.done = true;
/*  95 */       T v = this.value;
/*  96 */       this.value = null;
/*  97 */       if (v == null) {
/*  98 */         this.downstream.onComplete();
/*     */       } else {
/* 100 */         this.downstream.onSuccess(v);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableSingleMaybe.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */