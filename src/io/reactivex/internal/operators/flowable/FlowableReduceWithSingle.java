/*    */ package io.reactivex.internal.operators.flowable;
/*    */ 
/*    */ import io.reactivex.Single;
/*    */ import io.reactivex.SingleObserver;
/*    */ import io.reactivex.exceptions.Exceptions;
/*    */ import io.reactivex.functions.BiFunction;
/*    */ import io.reactivex.internal.disposables.EmptyDisposable;
/*    */ import io.reactivex.internal.functions.ObjectHelper;
/*    */ import java.util.concurrent.Callable;
/*    */ import org.reactivestreams.Publisher;
/*    */ import org.reactivestreams.Subscriber;
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
/*    */ 
/*    */ public final class FlowableReduceWithSingle<T, R>
/*    */   extends Single<R>
/*    */ {
/*    */   final Publisher<T> source;
/*    */   final Callable<R> seedSupplier;
/*    */   final BiFunction<R, ? super T, R> reducer;
/*    */   
/*    */   public FlowableReduceWithSingle(Publisher<T> source, Callable<R> seedSupplier, BiFunction<R, ? super T, R> reducer) {
/* 43 */     this.source = source;
/* 44 */     this.seedSupplier = seedSupplier;
/* 45 */     this.reducer = reducer;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void subscribeActual(SingleObserver<? super R> observer) {
/*    */     R seed;
/*    */     try {
/* 53 */       seed = (R)ObjectHelper.requireNonNull(this.seedSupplier.call(), "The seedSupplier returned a null value");
/* 54 */     } catch (Throwable ex) {
/* 55 */       Exceptions.throwIfFatal(ex);
/* 56 */       EmptyDisposable.error(ex, observer);
/*    */       return;
/*    */     } 
/* 59 */     this.source.subscribe((Subscriber)new FlowableReduceSeedSingle.ReduceSeedObserver<T, R>(observer, this.reducer, seed));
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableReduceWithSingle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */