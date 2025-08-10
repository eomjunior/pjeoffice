/*     */ package io.reactivex.internal.operators.observable;
/*     */ 
/*     */ import io.reactivex.Observable;
/*     */ import io.reactivex.ObservableSource;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.Single;
/*     */ import io.reactivex.SingleObserver;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.fuseable.FuseToObservable;
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
/*     */ public final class ObservableElementAtSingle<T>
/*     */   extends Single<T>
/*     */   implements FuseToObservable<T>
/*     */ {
/*     */   final ObservableSource<T> source;
/*     */   final long index;
/*     */   final T defaultValue;
/*     */   
/*     */   public ObservableElementAtSingle(ObservableSource<T> source, long index, T defaultValue) {
/*  30 */     this.source = source;
/*  31 */     this.index = index;
/*  32 */     this.defaultValue = defaultValue;
/*     */   }
/*     */ 
/*     */   
/*     */   public void subscribeActual(SingleObserver<? super T> t) {
/*  37 */     this.source.subscribe(new ElementAtObserver<T>(t, this.index, this.defaultValue));
/*     */   }
/*     */ 
/*     */   
/*     */   public Observable<T> fuseToObservable() {
/*  42 */     return RxJavaPlugins.onAssembly(new ObservableElementAt<T>(this.source, this.index, this.defaultValue, true));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class ElementAtObserver<T>
/*     */     implements Observer<T>, Disposable
/*     */   {
/*     */     final SingleObserver<? super T> downstream;
/*     */     final long index;
/*     */     final T defaultValue;
/*     */     Disposable upstream;
/*     */     long count;
/*     */     boolean done;
/*     */     
/*     */     ElementAtObserver(SingleObserver<? super T> actual, long index, T defaultValue) {
/*  57 */       this.downstream = actual;
/*  58 */       this.index = index;
/*  59 */       this.defaultValue = defaultValue;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  64 */       if (DisposableHelper.validate(this.upstream, d)) {
/*  65 */         this.upstream = d;
/*  66 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/*  72 */       this.upstream.dispose();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/*  77 */       return this.upstream.isDisposed();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*  82 */       if (this.done) {
/*     */         return;
/*     */       }
/*  85 */       long c = this.count;
/*  86 */       if (c == this.index) {
/*  87 */         this.done = true;
/*  88 */         this.upstream.dispose();
/*  89 */         this.downstream.onSuccess(t);
/*     */         return;
/*     */       } 
/*  92 */       this.count = c + 1L;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/*  97 */       if (this.done) {
/*  98 */         RxJavaPlugins.onError(t);
/*     */         return;
/*     */       } 
/* 101 */       this.done = true;
/* 102 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 107 */       if (!this.done) {
/* 108 */         this.done = true;
/*     */         
/* 110 */         T v = this.defaultValue;
/*     */         
/* 112 */         if (v != null) {
/* 113 */           this.downstream.onSuccess(v);
/*     */         } else {
/* 115 */           this.downstream.onError(new NoSuchElementException());
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableElementAtSingle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */