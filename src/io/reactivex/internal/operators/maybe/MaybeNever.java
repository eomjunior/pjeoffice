/*    */ package io.reactivex.internal.operators.maybe;
/*    */ 
/*    */ import io.reactivex.Maybe;
/*    */ import io.reactivex.MaybeObserver;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.internal.disposables.EmptyDisposable;
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
/*    */ public final class MaybeNever
/*    */   extends Maybe<Object>
/*    */ {
/* 24 */   public static final MaybeNever INSTANCE = new MaybeNever();
/*    */ 
/*    */   
/*    */   protected void subscribeActual(MaybeObserver<? super Object> observer) {
/* 28 */     observer.onSubscribe((Disposable)EmptyDisposable.NEVER);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/maybe/MaybeNever.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */