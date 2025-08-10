/*    */ package io.reactivex.internal.operators.maybe;
/*    */ 
/*    */ import io.reactivex.MaybeObserver;
/*    */ import io.reactivex.MaybeSource;
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
/*    */ public final class MaybeUnsafeCreate<T>
/*    */   extends AbstractMaybeWithUpstream<T, T>
/*    */ {
/*    */   public MaybeUnsafeCreate(MaybeSource<T> source) {
/* 26 */     super(source);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(MaybeObserver<? super T> observer) {
/* 31 */     this.source.subscribe(observer);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/maybe/MaybeUnsafeCreate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */