/*    */ package io.reactivex.internal.operators.observable;
/*    */ 
/*    */ import io.reactivex.ObservableSource;
/*    */ import io.reactivex.Single;
/*    */ import io.reactivex.SingleObserver;
/*    */ import io.reactivex.exceptions.Exceptions;
/*    */ import io.reactivex.functions.BiFunction;
/*    */ import io.reactivex.internal.disposables.EmptyDisposable;
/*    */ import io.reactivex.internal.functions.ObjectHelper;
/*    */ import java.util.concurrent.Callable;
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
/*    */ 
/*    */ 
/*    */ public final class ObservableReduceWithSingle<T, R>
/*    */   extends Single<R>
/*    */ {
/*    */   final ObservableSource<T> source;
/*    */   final Callable<R> seedSupplier;
/*    */   final BiFunction<R, ? super T, R> reducer;
/*    */   
/*    */   public ObservableReduceWithSingle(ObservableSource<T> source, Callable<R> seedSupplier, BiFunction<R, ? super T, R> reducer) {
/* 41 */     this.source = source;
/* 42 */     this.seedSupplier = seedSupplier;
/* 43 */     this.reducer = reducer;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void subscribeActual(SingleObserver<? super R> observer) {
/*    */     R seed;
/*    */     try {
/* 51 */       seed = (R)ObjectHelper.requireNonNull(this.seedSupplier.call(), "The seedSupplier returned a null value");
/* 52 */     } catch (Throwable ex) {
/* 53 */       Exceptions.throwIfFatal(ex);
/* 54 */       EmptyDisposable.error(ex, observer);
/*    */       return;
/*    */     } 
/* 57 */     this.source.subscribe(new ObservableReduceSeedSingle.ReduceSeedObserver<T, R>(observer, this.reducer, seed));
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableReduceWithSingle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */