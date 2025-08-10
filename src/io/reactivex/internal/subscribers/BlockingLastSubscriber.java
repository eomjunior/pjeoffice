/*    */ package io.reactivex.internal.subscribers;
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
/*    */ public final class BlockingLastSubscriber<T>
/*    */   extends BlockingBaseSubscriber<T>
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


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/subscribers/BlockingLastSubscriber.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */