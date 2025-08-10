/*    */ package io.reactivex.internal.operators.single;
/*    */ 
/*    */ import io.reactivex.Single;
/*    */ import io.reactivex.SingleObserver;
/*    */ import io.reactivex.SingleOperator;
/*    */ import io.reactivex.SingleSource;
/*    */ import io.reactivex.exceptions.Exceptions;
/*    */ import io.reactivex.internal.disposables.EmptyDisposable;
/*    */ import io.reactivex.internal.functions.ObjectHelper;
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
/*    */ public final class SingleLift<T, R>
/*    */   extends Single<R>
/*    */ {
/*    */   final SingleSource<T> source;
/*    */   final SingleOperator<? extends R, ? super T> onLift;
/*    */   
/*    */   public SingleLift(SingleSource<T> source, SingleOperator<? extends R, ? super T> onLift) {
/* 28 */     this.source = source;
/* 29 */     this.onLift = onLift;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void subscribeActual(SingleObserver<? super R> observer) {
/*    */     SingleObserver<? super T> sr;
/*    */     try {
/* 37 */       sr = (SingleObserver<? super T>)ObjectHelper.requireNonNull(this.onLift.apply(observer), "The onLift returned a null SingleObserver");
/* 38 */     } catch (Throwable ex) {
/* 39 */       Exceptions.throwIfFatal(ex);
/* 40 */       EmptyDisposable.error(ex, observer);
/*    */       
/*    */       return;
/*    */     } 
/* 44 */     this.source.subscribe(sr);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/single/SingleLift.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */