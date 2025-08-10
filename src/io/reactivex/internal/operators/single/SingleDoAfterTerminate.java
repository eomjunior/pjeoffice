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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class SingleDoAfterTerminate<T>
/*    */   extends Single<T>
/*    */ {
/*    */   final SingleSource<T> source;
/*    */   final Action onAfterTerminate;
/*    */   
/*    */   public SingleDoAfterTerminate(SingleSource<T> source, Action onAfterTerminate) {
/* 38 */     this.source = source;
/* 39 */     this.onAfterTerminate = onAfterTerminate;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(SingleObserver<? super T> observer) {
/* 44 */     this.source.subscribe(new DoAfterTerminateObserver<T>(observer, this.onAfterTerminate));
/*    */   }
/*    */ 
/*    */   
/*    */   static final class DoAfterTerminateObserver<T>
/*    */     implements SingleObserver<T>, Disposable
/*    */   {
/*    */     final SingleObserver<? super T> downstream;
/*    */     final Action onAfterTerminate;
/*    */     Disposable upstream;
/*    */     
/*    */     DoAfterTerminateObserver(SingleObserver<? super T> actual, Action onAfterTerminate) {
/* 56 */       this.downstream = actual;
/* 57 */       this.onAfterTerminate = onAfterTerminate;
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Disposable d) {
/* 62 */       if (DisposableHelper.validate(this.upstream, d)) {
/* 63 */         this.upstream = d;
/*    */         
/* 65 */         this.downstream.onSubscribe(this);
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSuccess(T t) {
/* 71 */       this.downstream.onSuccess(t);
/*    */       
/* 73 */       onAfterTerminate();
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable e) {
/* 78 */       this.downstream.onError(e);
/*    */       
/* 80 */       onAfterTerminate();
/*    */     }
/*    */ 
/*    */     
/*    */     public void dispose() {
/* 85 */       this.upstream.dispose();
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean isDisposed() {
/* 90 */       return this.upstream.isDisposed();
/*    */     }
/*    */     
/*    */     private void onAfterTerminate() {
/*    */       try {
/* 95 */         this.onAfterTerminate.run();
/* 96 */       } catch (Throwable ex) {
/* 97 */         Exceptions.throwIfFatal(ex);
/* 98 */         RxJavaPlugins.onError(ex);
/*    */       } 
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/single/SingleDoAfterTerminate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */