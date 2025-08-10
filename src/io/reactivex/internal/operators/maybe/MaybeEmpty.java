/*    */ package io.reactivex.internal.operators.maybe;
/*    */ 
/*    */ import io.reactivex.Maybe;
/*    */ import io.reactivex.MaybeObserver;
/*    */ import io.reactivex.internal.disposables.EmptyDisposable;
/*    */ import io.reactivex.internal.fuseable.ScalarCallable;
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
/*    */ public final class MaybeEmpty
/*    */   extends Maybe<Object>
/*    */   implements ScalarCallable<Object>
/*    */ {
/* 25 */   public static final MaybeEmpty INSTANCE = new MaybeEmpty();
/*    */ 
/*    */   
/*    */   protected void subscribeActual(MaybeObserver<? super Object> observer) {
/* 29 */     EmptyDisposable.complete(observer);
/*    */   }
/*    */ 
/*    */   
/*    */   public Object call() {
/* 34 */     return null;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/maybe/MaybeEmpty.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */