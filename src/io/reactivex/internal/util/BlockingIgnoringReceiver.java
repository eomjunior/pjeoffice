/*    */ package io.reactivex.internal.util;
/*    */ 
/*    */ import io.reactivex.functions.Action;
/*    */ import io.reactivex.functions.Consumer;
/*    */ import java.util.concurrent.CountDownLatch;
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
/*    */ public final class BlockingIgnoringReceiver
/*    */   extends CountDownLatch
/*    */   implements Consumer<Throwable>, Action
/*    */ {
/*    */   public Throwable error;
/*    */   
/*    */   public BlockingIgnoringReceiver() {
/* 29 */     super(1);
/*    */   }
/*    */ 
/*    */   
/*    */   public void accept(Throwable e) {
/* 34 */     this.error = e;
/* 35 */     countDown();
/*    */   }
/*    */ 
/*    */   
/*    */   public void run() {
/* 40 */     countDown();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/util/BlockingIgnoringReceiver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */