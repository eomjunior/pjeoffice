/*    */ package io.reactivex.internal.operators.maybe;
/*    */ 
/*    */ import io.reactivex.MaybeObserver;
/*    */ import io.reactivex.MaybeOperator;
/*    */ import io.reactivex.MaybeSource;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class MaybeLift<T, R>
/*    */   extends AbstractMaybeWithUpstream<T, R>
/*    */ {
/*    */   final MaybeOperator<? extends R, ? super T> operator;
/*    */   
/*    */   public MaybeLift(MaybeSource<T> source, MaybeOperator<? extends R, ? super T> operator) {
/* 32 */     super(source);
/* 33 */     this.operator = operator;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void subscribeActual(MaybeObserver<? super R> observer) {
/*    */     MaybeObserver<? super T> lifted;
/*    */     try {
/* 41 */       lifted = (MaybeObserver<? super T>)ObjectHelper.requireNonNull(this.operator.apply(observer), "The operator returned a null MaybeObserver");
/* 42 */     } catch (Throwable ex) {
/* 43 */       Exceptions.throwIfFatal(ex);
/* 44 */       EmptyDisposable.error(ex, observer);
/*    */       
/*    */       return;
/*    */     } 
/* 48 */     this.source.subscribe(lifted);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/maybe/MaybeLift.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */