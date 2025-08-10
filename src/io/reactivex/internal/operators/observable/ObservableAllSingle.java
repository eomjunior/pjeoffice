/*     */ package io.reactivex.internal.operators.observable;
/*     */ 
/*     */ import io.reactivex.Observable;
/*     */ import io.reactivex.ObservableSource;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.Single;
/*     */ import io.reactivex.SingleObserver;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.Predicate;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.fuseable.FuseToObservable;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ObservableAllSingle<T>
/*     */   extends Single<Boolean>
/*     */   implements FuseToObservable<Boolean>
/*     */ {
/*     */   final ObservableSource<T> source;
/*     */   final Predicate<? super T> predicate;
/*     */   
/*     */   public ObservableAllSingle(ObservableSource<T> source, Predicate<? super T> predicate) {
/*  28 */     this.source = source;
/*  29 */     this.predicate = predicate;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(SingleObserver<? super Boolean> t) {
/*  34 */     this.source.subscribe(new AllObserver<T>(t, this.predicate));
/*     */   }
/*     */ 
/*     */   
/*     */   public Observable<Boolean> fuseToObservable() {
/*  39 */     return RxJavaPlugins.onAssembly(new ObservableAll<T>(this.source, this.predicate));
/*     */   }
/*     */   
/*     */   static final class AllObserver<T>
/*     */     implements Observer<T>, Disposable
/*     */   {
/*     */     final SingleObserver<? super Boolean> downstream;
/*     */     final Predicate<? super T> predicate;
/*     */     Disposable upstream;
/*     */     boolean done;
/*     */     
/*     */     AllObserver(SingleObserver<? super Boolean> actual, Predicate<? super T> predicate) {
/*  51 */       this.downstream = actual;
/*  52 */       this.predicate = predicate;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  57 */       if (DisposableHelper.validate(this.upstream, d)) {
/*  58 */         this.upstream = d;
/*  59 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void onNext(T t) {
/*     */       boolean b;
/*  65 */       if (this.done) {
/*     */         return;
/*     */       }
/*     */       
/*     */       try {
/*  70 */         b = this.predicate.test(t);
/*  71 */       } catch (Throwable e) {
/*  72 */         Exceptions.throwIfFatal(e);
/*  73 */         this.upstream.dispose();
/*  74 */         onError(e);
/*     */         return;
/*     */       } 
/*  77 */       if (!b) {
/*  78 */         this.done = true;
/*  79 */         this.upstream.dispose();
/*  80 */         this.downstream.onSuccess(Boolean.valueOf(false));
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/*  86 */       if (this.done) {
/*  87 */         RxJavaPlugins.onError(t);
/*     */         return;
/*     */       } 
/*  90 */       this.done = true;
/*  91 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*  96 */       if (this.done) {
/*     */         return;
/*     */       }
/*  99 */       this.done = true;
/* 100 */       this.downstream.onSuccess(Boolean.valueOf(true));
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 105 */       this.upstream.dispose();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 110 */       return this.upstream.isDisposed();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableAllSingle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */