/*     */ package io.reactivex.internal.operators.completable;
/*     */ 
/*     */ import io.reactivex.Completable;
/*     */ import io.reactivex.CompletableObserver;
/*     */ import io.reactivex.CompletableSource;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.CompositeException;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.Action;
/*     */ import io.reactivex.functions.Consumer;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.disposables.EmptyDisposable;
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
/*     */ public final class CompletablePeek
/*     */   extends Completable
/*     */ {
/*     */   final CompletableSource source;
/*     */   final Consumer<? super Disposable> onSubscribe;
/*     */   final Consumer<? super Throwable> onError;
/*     */   final Action onComplete;
/*     */   final Action onTerminate;
/*     */   final Action onAfterTerminate;
/*     */   final Action onDispose;
/*     */   
/*     */   public CompletablePeek(CompletableSource source, Consumer<? super Disposable> onSubscribe, Consumer<? super Throwable> onError, Action onComplete, Action onTerminate, Action onAfterTerminate, Action onDispose) {
/*  39 */     this.source = source;
/*  40 */     this.onSubscribe = onSubscribe;
/*  41 */     this.onError = onError;
/*  42 */     this.onComplete = onComplete;
/*  43 */     this.onTerminate = onTerminate;
/*  44 */     this.onAfterTerminate = onAfterTerminate;
/*  45 */     this.onDispose = onDispose;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void subscribeActual(CompletableObserver observer) {
/*  51 */     this.source.subscribe(new CompletableObserverImplementation(observer));
/*     */   }
/*     */   
/*     */   final class CompletableObserverImplementation
/*     */     implements CompletableObserver, Disposable
/*     */   {
/*     */     final CompletableObserver downstream;
/*     */     Disposable upstream;
/*     */     
/*     */     CompletableObserverImplementation(CompletableObserver downstream) {
/*  61 */       this.downstream = downstream;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*     */       try {
/*  67 */         CompletablePeek.this.onSubscribe.accept(d);
/*  68 */       } catch (Throwable ex) {
/*  69 */         Exceptions.throwIfFatal(ex);
/*  70 */         d.dispose();
/*  71 */         this.upstream = (Disposable)DisposableHelper.DISPOSED;
/*  72 */         EmptyDisposable.error(ex, this.downstream);
/*     */         return;
/*     */       } 
/*  75 */       if (DisposableHelper.validate(this.upstream, d)) {
/*  76 */         this.upstream = d;
/*  77 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void onError(Throwable e) {
/*     */       CompositeException compositeException;
/*  83 */       if (this.upstream == DisposableHelper.DISPOSED) {
/*  84 */         RxJavaPlugins.onError(e);
/*     */         return;
/*     */       } 
/*     */       try {
/*  88 */         CompletablePeek.this.onError.accept(e);
/*  89 */         CompletablePeek.this.onTerminate.run();
/*  90 */       } catch (Throwable ex) {
/*  91 */         Exceptions.throwIfFatal(ex);
/*  92 */         compositeException = new CompositeException(new Throwable[] { e, ex });
/*     */       } 
/*     */       
/*  95 */       this.downstream.onError((Throwable)compositeException);
/*     */       
/*  97 */       doAfter();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 102 */       if (this.upstream == DisposableHelper.DISPOSED) {
/*     */         return;
/*     */       }
/*     */       
/*     */       try {
/* 107 */         CompletablePeek.this.onComplete.run();
/* 108 */         CompletablePeek.this.onTerminate.run();
/* 109 */       } catch (Throwable e) {
/* 110 */         Exceptions.throwIfFatal(e);
/* 111 */         this.downstream.onError(e);
/*     */         
/*     */         return;
/*     */       } 
/* 115 */       this.downstream.onComplete();
/*     */       
/* 117 */       doAfter();
/*     */     }
/*     */     
/*     */     void doAfter() {
/*     */       try {
/* 122 */         CompletablePeek.this.onAfterTerminate.run();
/* 123 */       } catch (Throwable ex) {
/* 124 */         Exceptions.throwIfFatal(ex);
/* 125 */         RxJavaPlugins.onError(ex);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/*     */       try {
/* 132 */         CompletablePeek.this.onDispose.run();
/* 133 */       } catch (Throwable e) {
/* 134 */         Exceptions.throwIfFatal(e);
/* 135 */         RxJavaPlugins.onError(e);
/*     */       } 
/* 137 */       this.upstream.dispose();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 142 */       return this.upstream.isDisposed();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/completable/CompletablePeek.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */