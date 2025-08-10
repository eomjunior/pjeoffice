/*    */ package io.reactivex.internal.subscribers;
/*    */ 
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class BlockingFirstSubscriber<T>
/*    */   extends BlockingBaseSubscriber<T>
/*    */ {
/*    */   public void onNext(T t) {
/* 27 */     if (this.value == null) {
/* 28 */       this.value = t;
/* 29 */       this.upstream.cancel();
/* 30 */       countDown();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void onError(Throwable t) {
/* 36 */     if (this.value == null) {
/* 37 */       this.error = t;
/*    */     } else {
/* 39 */       RxJavaPlugins.onError(t);
/*    */     } 
/* 41 */     countDown();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/subscribers/BlockingFirstSubscriber.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */