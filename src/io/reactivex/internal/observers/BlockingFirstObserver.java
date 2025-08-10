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
/*    */ public final class BlockingFirstObserver<T>
/*    */   extends BlockingBaseObserver<T>
/*    */ {
/*    */   public void onNext(T t) {
/* 25 */     if (this.value == null) {
/* 26 */       this.value = t;
/* 27 */       this.upstream.dispose();
/* 28 */       countDown();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void onError(Throwable t) {
/* 34 */     if (this.value == null) {
/* 35 */       this.error = t;
/*    */     }
/* 37 */     countDown();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/observers/BlockingFirstObserver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */