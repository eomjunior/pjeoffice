/*    */ package io.reactivex.internal.operators.single;
/*    */ 
/*    */ import io.reactivex.Single;
/*    */ import io.reactivex.SingleObserver;
/*    */ import io.reactivex.SingleSource;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.exceptions.CompositeException;
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
/*    */ public final class SingleDoOnError<T>
/*    */   extends Single<T>
/*    */ {
/*    */   final SingleSource<T> source;
/*    */   final Consumer<? super Throwable> onError;
/*    */   
/*    */   public SingleDoOnError(SingleSource<T> source, Consumer<? super Throwable> onError) {
/* 28 */     this.source = source;
/* 29 */     this.onError = onError;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void subscribeActual(SingleObserver<? super T> observer) {
/* 35 */     this.source.subscribe(new DoOnError(observer));
/*    */   }
/*    */   
/*    */   final class DoOnError implements SingleObserver<T> {
/*    */     private final SingleObserver<? super T> downstream;
/*    */     
/*    */     DoOnError(SingleObserver<? super T> observer) {
/* 42 */       this.downstream = observer;
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Disposable d) {
/* 47 */       this.downstream.onSubscribe(d);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSuccess(T value) {
/* 52 */       this.downstream.onSuccess(value);
/*    */     }
/*    */     
/*    */     public void onError(Throwable e) {
/*    */       CompositeException compositeException;
/*    */       try {
/* 58 */         SingleDoOnError.this.onError.accept(e);
/* 59 */       } catch (Throwable ex) {
/* 60 */         Exceptions.throwIfFatal(ex);
/* 61 */         compositeException = new CompositeException(new Throwable[] { e, ex });
/*    */       } 
/* 63 */       this.downstream.onError((Throwable)compositeException);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/single/SingleDoOnError.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */