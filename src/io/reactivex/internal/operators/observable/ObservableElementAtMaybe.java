/*     */ package io.reactivex.internal.operators.observable;
/*     */ 
/*     */ import io.reactivex.Maybe;
/*     */ import io.reactivex.MaybeObserver;
/*     */ import io.reactivex.Observable;
/*     */ import io.reactivex.ObservableSource;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.fuseable.FuseToObservable;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ObservableElementAtMaybe<T>
/*     */   extends Maybe<T>
/*     */   implements FuseToObservable<T>
/*     */ {
/*     */   final ObservableSource<T> source;
/*     */   final long index;
/*     */   
/*     */   public ObservableElementAtMaybe(ObservableSource<T> source, long index) {
/*  26 */     this.source = source;
/*  27 */     this.index = index;
/*     */   }
/*     */ 
/*     */   
/*     */   public void subscribeActual(MaybeObserver<? super T> t) {
/*  32 */     this.source.subscribe(new ElementAtObserver<T>(t, this.index));
/*     */   }
/*     */ 
/*     */   
/*     */   public Observable<T> fuseToObservable() {
/*  37 */     return RxJavaPlugins.onAssembly(new ObservableElementAt<T>(this.source, this.index, null, false));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class ElementAtObserver<T>
/*     */     implements Observer<T>, Disposable
/*     */   {
/*     */     final MaybeObserver<? super T> downstream;
/*     */     final long index;
/*     */     Disposable upstream;
/*     */     long count;
/*     */     boolean done;
/*     */     
/*     */     ElementAtObserver(MaybeObserver<? super T> actual, long index) {
/*  51 */       this.downstream = actual;
/*  52 */       this.index = index;
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
/*     */     
/*     */     public void dispose() {
/*  65 */       this.upstream.dispose();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/*  70 */       return this.upstream.isDisposed();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*  75 */       if (this.done) {
/*     */         return;
/*     */       }
/*  78 */       long c = this.count;
/*  79 */       if (c == this.index) {
/*  80 */         this.done = true;
/*  81 */         this.upstream.dispose();
/*  82 */         this.downstream.onSuccess(t);
/*     */         return;
/*     */       } 
/*  85 */       this.count = c + 1L;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/*  90 */       if (this.done) {
/*  91 */         RxJavaPlugins.onError(t);
/*     */         return;
/*     */       } 
/*  94 */       this.done = true;
/*  95 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 100 */       if (!this.done) {
/* 101 */         this.done = true;
/* 102 */         this.downstream.onComplete();
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableElementAtMaybe.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */