/*    */ package io.reactivex.internal.operators.single;
/*    */ 
/*    */ import io.reactivex.Single;
/*    */ import io.reactivex.SingleObserver;
/*    */ import io.reactivex.SingleSource;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.exceptions.Exceptions;
/*    */ import io.reactivex.functions.Action;
/*    */ import io.reactivex.internal.disposables.DisposableHelper;
/*    */ import io.reactivex.plugins.RxJavaPlugins;
/*    */ import java.util.concurrent.atomic.AtomicReference;
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
/*    */ public final class SingleDoOnDispose<T>
/*    */   extends Single<T>
/*    */ {
/*    */   final SingleSource<T> source;
/*    */   final Action onDispose;
/*    */   
/*    */   public SingleDoOnDispose(SingleSource<T> source, Action onDispose) {
/* 31 */     this.source = source;
/* 32 */     this.onDispose = onDispose;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void subscribeActual(SingleObserver<? super T> observer) {
/* 38 */     this.source.subscribe(new DoOnDisposeObserver<T>(observer, this.onDispose));
/*    */   }
/*    */ 
/*    */   
/*    */   static final class DoOnDisposeObserver<T>
/*    */     extends AtomicReference<Action>
/*    */     implements SingleObserver<T>, Disposable
/*    */   {
/*    */     private static final long serialVersionUID = -8583764624474935784L;
/*    */     final SingleObserver<? super T> downstream;
/*    */     Disposable upstream;
/*    */     
/*    */     DoOnDisposeObserver(SingleObserver<? super T> actual, Action onDispose) {
/* 51 */       this.downstream = actual;
/* 52 */       lazySet(onDispose);
/*    */     }
/*    */ 
/*    */     
/*    */     public void dispose() {
/* 57 */       Action a = getAndSet(null);
/* 58 */       if (a != null) {
/*    */         try {
/* 60 */           a.run();
/* 61 */         } catch (Throwable ex) {
/* 62 */           Exceptions.throwIfFatal(ex);
/* 63 */           RxJavaPlugins.onError(ex);
/*    */         } 
/* 65 */         this.upstream.dispose();
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean isDisposed() {
/* 71 */       return this.upstream.isDisposed();
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Disposable d) {
/* 76 */       if (DisposableHelper.validate(this.upstream, d)) {
/* 77 */         this.upstream = d;
/* 78 */         this.downstream.onSubscribe(this);
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSuccess(T value) {
/* 84 */       this.downstream.onSuccess(value);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable e) {
/* 89 */       this.downstream.onError(e);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/single/SingleDoOnDispose.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */