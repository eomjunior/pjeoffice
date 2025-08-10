/*    */ package io.reactivex.internal.operators.single;
/*    */ 
/*    */ import io.reactivex.Single;
/*    */ import io.reactivex.SingleObserver;
/*    */ import io.reactivex.SingleSource;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.exceptions.Exceptions;
/*    */ import io.reactivex.functions.Consumer;
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
/*    */ public final class SingleDoOnSuccess<T>
/*    */   extends Single<T>
/*    */ {
/*    */   final SingleSource<T> source;
/*    */   final Consumer<? super T> onSuccess;
/*    */   
/*    */   public SingleDoOnSuccess(SingleSource<T> source, Consumer<? super T> onSuccess) {
/* 28 */     this.source = source;
/* 29 */     this.onSuccess = onSuccess;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void subscribeActual(SingleObserver<? super T> observer) {
/* 35 */     this.source.subscribe(new DoOnSuccess(observer));
/*    */   }
/*    */   
/*    */   final class DoOnSuccess
/*    */     implements SingleObserver<T> {
/*    */     final SingleObserver<? super T> downstream;
/*    */     
/*    */     DoOnSuccess(SingleObserver<? super T> observer) {
/* 43 */       this.downstream = observer;
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Disposable d) {
/* 48 */       this.downstream.onSubscribe(d);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSuccess(T value) {
/*    */       try {
/* 54 */         SingleDoOnSuccess.this.onSuccess.accept(value);
/* 55 */       } catch (Throwable ex) {
/* 56 */         Exceptions.throwIfFatal(ex);
/* 57 */         this.downstream.onError(ex);
/*    */         return;
/*    */       } 
/* 60 */       this.downstream.onSuccess(value);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable e) {
/* 65 */       this.downstream.onError(e);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/single/SingleDoOnSuccess.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */