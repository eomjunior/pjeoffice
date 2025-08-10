/*    */ package io.reactivex.internal.observers;
/*    */ 
/*    */ import io.reactivex.Observer;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.exceptions.Exceptions;
/*    */ import io.reactivex.functions.Action;
/*    */ import io.reactivex.functions.Consumer;
/*    */ import io.reactivex.internal.disposables.DisposableHelper;
/*    */ import io.reactivex.internal.disposables.EmptyDisposable;
/*    */ import io.reactivex.plugins.RxJavaPlugins;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class DisposableLambdaObserver<T>
/*    */   implements Observer<T>, Disposable
/*    */ {
/*    */   final Observer<? super T> downstream;
/*    */   final Consumer<? super Disposable> onSubscribe;
/*    */   final Action onDispose;
/*    */   Disposable upstream;
/*    */   
/*    */   public DisposableLambdaObserver(Observer<? super T> actual, Consumer<? super Disposable> onSubscribe, Action onDispose) {
/* 33 */     this.downstream = actual;
/* 34 */     this.onSubscribe = onSubscribe;
/* 35 */     this.onDispose = onDispose;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void onSubscribe(Disposable d) {
/*    */     try {
/* 42 */       this.onSubscribe.accept(d);
/* 43 */     } catch (Throwable e) {
/* 44 */       Exceptions.throwIfFatal(e);
/* 45 */       d.dispose();
/* 46 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/* 47 */       EmptyDisposable.error(e, this.downstream);
/*    */       return;
/*    */     } 
/* 50 */     if (DisposableHelper.validate(this.upstream, d)) {
/* 51 */       this.upstream = d;
/* 52 */       this.downstream.onSubscribe(this);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void onNext(T t) {
/* 58 */     this.downstream.onNext(t);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onError(Throwable t) {
/* 63 */     if (this.upstream != DisposableHelper.DISPOSED) {
/* 64 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/* 65 */       this.downstream.onError(t);
/*    */     } else {
/* 67 */       RxJavaPlugins.onError(t);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void onComplete() {
/* 73 */     if (this.upstream != DisposableHelper.DISPOSED) {
/* 74 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/* 75 */       this.downstream.onComplete();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void dispose() {
/* 81 */     Disposable d = this.upstream;
/* 82 */     if (d != DisposableHelper.DISPOSED) {
/* 83 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/*    */       try {
/* 85 */         this.onDispose.run();
/* 86 */       } catch (Throwable e) {
/* 87 */         Exceptions.throwIfFatal(e);
/* 88 */         RxJavaPlugins.onError(e);
/*    */       } 
/* 90 */       d.dispose();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isDisposed() {
/* 96 */     return this.upstream.isDisposed();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/observers/DisposableLambdaObserver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */