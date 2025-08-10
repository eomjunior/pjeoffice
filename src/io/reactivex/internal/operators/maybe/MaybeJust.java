/*    */ package io.reactivex.internal.operators.maybe;
/*    */ 
/*    */ import io.reactivex.Maybe;
/*    */ import io.reactivex.MaybeObserver;
/*    */ import io.reactivex.disposables.Disposables;
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
/*    */ 
/*    */ 
/*    */ public final class MaybeJust<T>
/*    */   extends Maybe<T>
/*    */   implements ScalarCallable<T>
/*    */ {
/*    */   final T value;
/*    */   
/*    */   public MaybeJust(T value) {
/* 30 */     this.value = value;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(MaybeObserver<? super T> observer) {
/* 35 */     observer.onSubscribe(Disposables.disposed());
/* 36 */     observer.onSuccess(this.value);
/*    */   }
/*    */ 
/*    */   
/*    */   public T call() {
/* 41 */     return this.value;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/maybe/MaybeJust.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */