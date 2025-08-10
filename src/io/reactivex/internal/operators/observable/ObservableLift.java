/*    */ package io.reactivex.internal.operators.observable;
/*    */ 
/*    */ import io.reactivex.ObservableOperator;
/*    */ import io.reactivex.ObservableSource;
/*    */ import io.reactivex.Observer;
/*    */ import io.reactivex.exceptions.Exceptions;
/*    */ import io.reactivex.internal.functions.ObjectHelper;
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
/*    */ public final class ObservableLift<R, T>
/*    */   extends AbstractObservableWithUpstream<T, R>
/*    */ {
/*    */   final ObservableOperator<? extends R, ? super T> operator;
/*    */   
/*    */   public ObservableLift(ObservableSource<T> source, ObservableOperator<? extends R, ? super T> operator) {
/* 35 */     super(source);
/* 36 */     this.operator = operator;
/*    */   }
/*    */ 
/*    */   
/*    */   public void subscribeActual(Observer<? super R> observer) {
/*    */     Observer<? super T> liftedObserver;
/*    */     try {
/* 43 */       liftedObserver = (Observer<? super T>)ObjectHelper.requireNonNull(this.operator.apply(observer), "Operator " + this.operator + " returned a null Observer");
/* 44 */     } catch (NullPointerException e) {
/* 45 */       throw e;
/* 46 */     } catch (Throwable e) {
/* 47 */       Exceptions.throwIfFatal(e);
/*    */ 
/*    */       
/* 50 */       RxJavaPlugins.onError(e);
/*    */       
/* 52 */       NullPointerException npe = new NullPointerException("Actually not, but can't throw other exceptions due to RS");
/* 53 */       npe.initCause(e);
/* 54 */       throw npe;
/*    */     } 
/*    */     
/* 57 */     this.source.subscribe(liftedObserver);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableLift.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */