/*     */ package io.reactivex.internal.operators.observable;
/*     */ 
/*     */ import io.reactivex.ObservableSource;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.Single;
/*     */ import io.reactivex.SingleObserver;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.NoSuchElementException;
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
/*     */ public final class ObservableSingleSingle<T>
/*     */   extends Single<T>
/*     */ {
/*     */   final ObservableSource<? extends T> source;
/*     */   final T defaultValue;
/*     */   
/*     */   public ObservableSingleSingle(ObservableSource<? extends T> source, T defaultValue) {
/*  29 */     this.source = source;
/*  30 */     this.defaultValue = defaultValue;
/*     */   }
/*     */ 
/*     */   
/*     */   public void subscribeActual(SingleObserver<? super T> t) {
/*  35 */     this.source.subscribe(new SingleElementObserver<T>(t, this.defaultValue));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class SingleElementObserver<T>
/*     */     implements Observer<T>, Disposable
/*     */   {
/*     */     final SingleObserver<? super T> downstream;
/*     */     
/*     */     final T defaultValue;
/*     */     Disposable upstream;
/*     */     T value;
/*     */     boolean done;
/*     */     
/*     */     SingleElementObserver(SingleObserver<? super T> actual, T defaultValue) {
/*  50 */       this.downstream = actual;
/*  51 */       this.defaultValue = defaultValue;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  56 */       if (DisposableHelper.validate(this.upstream, d)) {
/*  57 */         this.upstream = d;
/*  58 */         this.downstream.onSubscribe(this);
/*     */       } 
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
/*     */     public void onNext(T t) {
/*  74 */       if (this.done) {
/*     */         return;
/*     */       }
/*  77 */       if (this.value != null) {
/*  78 */         this.done = true;
/*  79 */         this.upstream.dispose();
/*  80 */         this.downstream.onError(new IllegalArgumentException("Sequence contains more than one element!"));
/*     */         return;
/*     */       } 
/*  83 */       this.value = t;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/*  88 */       if (this.done) {
/*  89 */         RxJavaPlugins.onError(t);
/*     */         return;
/*     */       } 
/*  92 */       this.done = true;
/*  93 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*  98 */       if (this.done) {
/*     */         return;
/*     */       }
/* 101 */       this.done = true;
/* 102 */       T v = this.value;
/* 103 */       this.value = null;
/* 104 */       if (v == null) {
/* 105 */         v = this.defaultValue;
/*     */       }
/*     */       
/* 108 */       if (v != null) {
/* 109 */         this.downstream.onSuccess(v);
/*     */       } else {
/* 111 */         this.downstream.onError(new NoSuchElementException());
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableSingleSingle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */