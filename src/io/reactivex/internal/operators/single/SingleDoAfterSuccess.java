/*    */ package io.reactivex.internal.operators.single;
/*    */ 
/*    */ import io.reactivex.Single;
/*    */ import io.reactivex.SingleObserver;
/*    */ import io.reactivex.SingleSource;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.exceptions.Exceptions;
/*    */ import io.reactivex.functions.Consumer;
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
/*    */ public final class SingleDoAfterSuccess<T>
/*    */   extends Single<T>
/*    */ {
/*    */   final SingleSource<T> source;
/*    */   final Consumer<? super T> onAfterSuccess;
/*    */   
/*    */   public SingleDoAfterSuccess(SingleSource<T> source, Consumer<? super T> onAfterSuccess) {
/* 36 */     this.source = source;
/* 37 */     this.onAfterSuccess = onAfterSuccess;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(SingleObserver<? super T> observer) {
/* 42 */     this.source.subscribe(new DoAfterObserver<T>(observer, this.onAfterSuccess));
/*    */   }
/*    */ 
/*    */   
/*    */   static final class DoAfterObserver<T>
/*    */     implements SingleObserver<T>, Disposable
/*    */   {
/*    */     final SingleObserver<? super T> downstream;
/*    */     final Consumer<? super T> onAfterSuccess;
/*    */     Disposable upstream;
/*    */     
/*    */     DoAfterObserver(SingleObserver<? super T> actual, Consumer<? super T> onAfterSuccess) {
/* 54 */       this.downstream = actual;
/* 55 */       this.onAfterSuccess = onAfterSuccess;
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Disposable d) {
/* 60 */       if (DisposableHelper.validate(this.upstream, d)) {
/* 61 */         this.upstream = d;
/*    */         
/* 63 */         this.downstream.onSubscribe(this);
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSuccess(T t) {
/* 69 */       this.downstream.onSuccess(t);
/*    */       
/*    */       try {
/* 72 */         this.onAfterSuccess.accept(t);
/* 73 */       } catch (Throwable ex) {
/* 74 */         Exceptions.throwIfFatal(ex);
/*    */         
/* 76 */         RxJavaPlugins.onError(ex);
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable e) {
/* 82 */       this.downstream.onError(e);
/*    */     }
/*    */ 
/*    */     
/*    */     public void dispose() {
/* 87 */       this.upstream.dispose();
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean isDisposed() {
/* 92 */       return this.upstream.isDisposed();
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/single/SingleDoAfterSuccess.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */