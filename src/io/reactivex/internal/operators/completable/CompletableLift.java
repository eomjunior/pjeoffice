/*    */ package io.reactivex.internal.operators.completable;
/*    */ 
/*    */ import io.reactivex.Completable;
/*    */ import io.reactivex.CompletableObserver;
/*    */ import io.reactivex.CompletableOperator;
/*    */ import io.reactivex.CompletableSource;
/*    */ import io.reactivex.exceptions.Exceptions;
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
/*    */ public final class CompletableLift
/*    */   extends Completable
/*    */ {
/*    */   final CompletableSource source;
/*    */   final CompletableOperator onLift;
/*    */   
/*    */   public CompletableLift(CompletableSource source, CompletableOperator onLift) {
/* 27 */     this.source = source;
/* 28 */     this.onLift = onLift;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void subscribeActual(CompletableObserver observer) {
/*    */     try {
/* 36 */       CompletableObserver sw = this.onLift.apply(observer);
/*    */       
/* 38 */       this.source.subscribe(sw);
/* 39 */     } catch (NullPointerException ex) {
/* 40 */       throw ex;
/* 41 */     } catch (Throwable ex) {
/* 42 */       Exceptions.throwIfFatal(ex);
/* 43 */       RxJavaPlugins.onError(ex);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/completable/CompletableLift.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */