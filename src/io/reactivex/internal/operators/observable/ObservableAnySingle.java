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
/*     */ 
/*     */ public final class ObservableAnySingle<T>
/*     */   extends Single<Boolean>
/*     */   implements FuseToObservable<Boolean>
/*     */ {
/*     */   final ObservableSource<T> source;
/*     */   final Predicate<? super T> predicate;
/*     */   
/*     */   public ObservableAnySingle(ObservableSource<T> source, Predicate<? super T> predicate) {
/*  29 */     this.source = source;
/*  30 */     this.predicate = predicate;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(SingleObserver<? super Boolean> t) {
/*  35 */     this.source.subscribe(new AnyObserver<T>(t, this.predicate));
/*     */   }
/*     */ 
/*     */   
/*     */   public Observable<Boolean> fuseToObservable() {
/*  40 */     return RxJavaPlugins.onAssembly(new ObservableAny<T>(this.source, this.predicate));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class AnyObserver<T>
/*     */     implements Observer<T>, Disposable
/*     */   {
/*     */     final SingleObserver<? super Boolean> downstream;
/*     */     final Predicate<? super T> predicate;
/*     */     Disposable upstream;
/*     */     boolean done;
/*     */     
/*     */     AnyObserver(SingleObserver<? super Boolean> actual, Predicate<? super T> predicate) {
/*  53 */       this.downstream = actual;
/*  54 */       this.predicate = predicate;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  59 */       if (DisposableHelper.validate(this.upstream, d)) {
/*  60 */         this.upstream = d;
/*  61 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void onNext(T t) {
/*     */       boolean b;
/*  67 */       if (this.done) {
/*     */         return;
/*     */       }
/*     */       
/*     */       try {
/*  72 */         b = this.predicate.test(t);
/*  73 */       } catch (Throwable e) {
/*  74 */         Exceptions.throwIfFatal(e);
/*  75 */         this.upstream.dispose();
/*  76 */         onError(e);
/*     */         return;
/*     */       } 
/*  79 */       if (b) {
/*  80 */         this.done = true;
/*  81 */         this.upstream.dispose();
/*  82 */         this.downstream.onSuccess(Boolean.valueOf(true));
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/*  88 */       if (this.done) {
/*  89 */         RxJavaPlugins.onError(t);
/*     */         
/*     */         return;
/*     */       } 
/*  93 */       this.done = true;
/*  94 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*  99 */       if (!this.done) {
/* 100 */         this.done = true;
/* 101 */         this.downstream.onSuccess(Boolean.valueOf(false));
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 107 */       this.upstream.dispose();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 112 */       return this.upstream.isDisposed();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableAnySingle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */