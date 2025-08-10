/*     */ package io.reactivex.internal.operators.observable;
/*     */ 
/*     */ import io.reactivex.ObservableSource;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.CompositeException;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.Action;
/*     */ import io.reactivex.functions.Consumer;
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
/*     */ 
/*     */ 
/*     */ public final class ObservableDoOnEach<T>
/*     */   extends AbstractObservableWithUpstream<T, T>
/*     */ {
/*     */   final Consumer<? super T> onNext;
/*     */   final Consumer<? super Throwable> onError;
/*     */   final Action onComplete;
/*     */   final Action onAfterTerminate;
/*     */   
/*     */   public ObservableDoOnEach(ObservableSource<T> source, Consumer<? super T> onNext, Consumer<? super Throwable> onError, Action onComplete, Action onAfterTerminate) {
/*  33 */     super(source);
/*  34 */     this.onNext = onNext;
/*  35 */     this.onError = onError;
/*  36 */     this.onComplete = onComplete;
/*  37 */     this.onAfterTerminate = onAfterTerminate;
/*     */   }
/*     */ 
/*     */   
/*     */   public void subscribeActual(Observer<? super T> t) {
/*  42 */     this.source.subscribe(new DoOnEachObserver<T>(t, this.onNext, this.onError, this.onComplete, this.onAfterTerminate));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class DoOnEachObserver<T>
/*     */     implements Observer<T>, Disposable
/*     */   {
/*     */     final Observer<? super T> downstream;
/*     */     
/*     */     final Consumer<? super T> onNext;
/*     */     
/*     */     final Consumer<? super Throwable> onError;
/*     */     
/*     */     final Action onComplete;
/*     */     
/*     */     final Action onAfterTerminate;
/*     */     Disposable upstream;
/*     */     boolean done;
/*     */     
/*     */     DoOnEachObserver(Observer<? super T> actual, Consumer<? super T> onNext, Consumer<? super Throwable> onError, Action onComplete, Action onAfterTerminate) {
/*  62 */       this.downstream = actual;
/*  63 */       this.onNext = onNext;
/*  64 */       this.onError = onError;
/*  65 */       this.onComplete = onComplete;
/*  66 */       this.onAfterTerminate = onAfterTerminate;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  71 */       if (DisposableHelper.validate(this.upstream, d)) {
/*  72 */         this.upstream = d;
/*  73 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/*  79 */       this.upstream.dispose();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/*  84 */       return this.upstream.isDisposed();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*  89 */       if (this.done) {
/*     */         return;
/*     */       }
/*     */       try {
/*  93 */         this.onNext.accept(t);
/*  94 */       } catch (Throwable e) {
/*  95 */         Exceptions.throwIfFatal(e);
/*  96 */         this.upstream.dispose();
/*  97 */         onError(e);
/*     */         
/*     */         return;
/*     */       } 
/* 101 */       this.downstream.onNext(t);
/*     */     }
/*     */     
/*     */     public void onError(Throwable t) {
/*     */       CompositeException compositeException;
/* 106 */       if (this.done) {
/* 107 */         RxJavaPlugins.onError(t);
/*     */         return;
/*     */       } 
/* 110 */       this.done = true;
/*     */       try {
/* 112 */         this.onError.accept(t);
/* 113 */       } catch (Throwable e) {
/* 114 */         Exceptions.throwIfFatal(e);
/* 115 */         compositeException = new CompositeException(new Throwable[] { t, e });
/*     */       } 
/* 117 */       this.downstream.onError((Throwable)compositeException);
/*     */       
/*     */       try {
/* 120 */         this.onAfterTerminate.run();
/* 121 */       } catch (Throwable e) {
/* 122 */         Exceptions.throwIfFatal(e);
/* 123 */         RxJavaPlugins.onError(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 129 */       if (this.done) {
/*     */         return;
/*     */       }
/*     */       try {
/* 133 */         this.onComplete.run();
/* 134 */       } catch (Throwable e) {
/* 135 */         Exceptions.throwIfFatal(e);
/* 136 */         onError(e);
/*     */         
/*     */         return;
/*     */       } 
/* 140 */       this.done = true;
/* 141 */       this.downstream.onComplete();
/*     */       
/*     */       try {
/* 144 */         this.onAfterTerminate.run();
/* 145 */       } catch (Throwable e) {
/* 146 */         Exceptions.throwIfFatal(e);
/* 147 */         RxJavaPlugins.onError(e);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableDoOnEach.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */