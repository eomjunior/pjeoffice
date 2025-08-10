/*    */ package io.reactivex.internal.operators.maybe;
/*    */ 
/*    */ import io.reactivex.Maybe;
/*    */ import io.reactivex.MaybeObserver;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.disposables.Disposables;
/*    */ import io.reactivex.exceptions.Exceptions;
/*    */ import java.util.concurrent.Future;
/*    */ import java.util.concurrent.TimeUnit;
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
/*    */ public final class MaybeFromFuture<T>
/*    */   extends Maybe<T>
/*    */ {
/*    */   final Future<? extends T> future;
/*    */   final long timeout;
/*    */   final TimeUnit unit;
/*    */   
/*    */   public MaybeFromFuture(Future<? extends T> future, long timeout, TimeUnit unit) {
/* 37 */     this.future = future;
/* 38 */     this.timeout = timeout;
/* 39 */     this.unit = unit;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(MaybeObserver<? super T> observer) {
/* 44 */     Disposable d = Disposables.empty();
/* 45 */     observer.onSubscribe(d);
/* 46 */     if (!d.isDisposed()) {
/*    */       T v;
/*    */       try {
/* 49 */         if (this.timeout <= 0L) {
/* 50 */           v = this.future.get();
/*    */         } else {
/* 52 */           v = this.future.get(this.timeout, this.unit);
/*    */         } 
/* 54 */       } catch (Throwable ex) {
/* 55 */         if (ex instanceof java.util.concurrent.ExecutionException) {
/* 56 */           ex = ex.getCause();
/*    */         }
/* 58 */         Exceptions.throwIfFatal(ex);
/* 59 */         if (!d.isDisposed()) {
/* 60 */           observer.onError(ex);
/*    */         }
/*    */         return;
/*    */       } 
/* 64 */       if (!d.isDisposed())
/* 65 */         if (v == null) {
/* 66 */           observer.onComplete();
/*    */         } else {
/* 68 */           observer.onSuccess(v);
/*    */         }  
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/maybe/MaybeFromFuture.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */