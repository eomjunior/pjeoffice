/*    */ package io.reactivex.internal.observers;
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
/*    */ public final class BlockingLastObserver<T>
/*    */   extends BlockingBaseObserver<T>
/*    */ {
/*    */   public void onNext(T t) {
/* 25 */     this.value = t;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onError(Throwable t) {
/* 30 */     this.value = null;
/* 31 */     this.error = t;
/* 32 */     countDown();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/observers/BlockingLastObserver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */