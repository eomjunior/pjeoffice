/*    */ package io.reactivex.internal.operators.maybe;
/*    */ 
/*    */ import io.reactivex.Maybe;
/*    */ import io.reactivex.MaybeObserver;
/*    */ import io.reactivex.MaybeSource;
/*    */ import io.reactivex.exceptions.Exceptions;
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
/*    */ public final class MaybeDefer<T>
/*    */   extends Maybe<T>
/*    */ {
/*    */   final Callable<? extends MaybeSource<? extends T>> maybeSupplier;
/*    */   
/*    */   public MaybeDefer(Callable<? extends MaybeSource<? extends T>> maybeSupplier) {
/* 33 */     this.maybeSupplier = maybeSupplier;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void subscribeActual(MaybeObserver<? super T> observer) {
/*    */     MaybeSource<? extends T> source;
/*    */     try {
/* 41 */       source = (MaybeSource<? extends T>)ObjectHelper.requireNonNull(this.maybeSupplier.call(), "The maybeSupplier returned a null MaybeSource");
/* 42 */     } catch (Throwable ex) {
/* 43 */       Exceptions.throwIfFatal(ex);
/* 44 */       EmptyDisposable.error(ex, observer);
/*    */       
/*    */       return;
/*    */     } 
/* 48 */     source.subscribe(observer);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/maybe/MaybeDefer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */